package com.bs.lk.newoamvptest.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bs.lk.newoamvptest.CApplication;
import com.bs.lk.newoamvptest.R;
import com.bs.lk.newoamvptest.TabsActivity;
import com.bs.lk.newoamvptest.adapter.UsersRelationAdapter;
import com.bs.lk.newoamvptest.bean.TokenRoot;
import com.bs.lk.newoamvptest.bean.UserNewBean;
import com.bs.lk.newoamvptest.util.WebServiceUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author lk
 */
public class SwitchAccountActivity extends AppCompatActivity {


    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.iv_courtoa_icon)
    ImageView ivCourtoaIcon;
    @BindView(R.id.tv_remind)
    TextView tvRemind;
    @BindView(R.id.iv_decorate)
    ImageView ivDecorate;
    @BindView(R.id.rv_users_relation)
    RecyclerView rvUsersRelation;
    @BindView(R.id.tv_clear_login)
    TextView tvClearLogin;
    private Handler handlergetdata = null;
    private List<UserNewBean> usersRelation;


    public static final int SHOW_RESPONSE_USER_RELATION = 0;

    public static final int SHOW_RESPONSE_RELATION_SWITCH = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_account);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        ButterKnife.bind(this);
        initEvent();

        initData();


    }

    private void initEvent() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initData() {

        new Thread() {
            @Override
            public void run() {
                UserNewBean user = CApplication.getInstance().getCurrentUser();
                String relation = user.getRelation();
                String resp = WebServiceUtil.getInstance().getUsersByRelation(relation);
                List<UserNewBean> userList = JSON.parseArray(resp, UserNewBean.class);
                if (userList!=null){
                    String ceshi = userList.get(0).getUserName();
                    Message message = new Message();
                    message.what = SHOW_RESPONSE_USER_RELATION;
                    message.obj = userList;
                    handlergetdata.sendMessage(message);
                    Log.e("resp", "" + ceshi);
                }else {
                    Log.e("resp", "暂无关联账号" );
                }


            }
        }.start();

        handlergetdata = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case SHOW_RESPONSE_USER_RELATION:
                        usersRelation = (ArrayList) msg.obj;
                        LinearLayoutManager layoutManager = new LinearLayoutManager(SwitchAccountActivity.this);
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        rvUsersRelation.setLayoutManager(layoutManager);
                        UsersRelationAdapter adapter = new UsersRelationAdapter(SwitchAccountActivity.this,usersRelation);
                        rvUsersRelation.setAdapter(adapter);

                        adapter.setOnItemClickListener(new UsersRelationAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClickListener(View view, int position) {
                                UserNewBean userNewBean = usersRelation.get(position);
                                ObtainNewRelationToken(userNewBean);
                            }
                        });
                        break;
                    case SHOW_RESPONSE_RELATION_SWITCH:
                        Intent intent = new Intent(SwitchAccountActivity.this, TabsActivity.class);
                        startActivity(intent);
                        Toast.makeText(SwitchAccountActivity.this,"跳微门户模块！",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

    }

    private void ObtainNewRelationToken(UserNewBean user) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String token = null;
                String resultStr = WebServiceUtil.getInstance().createSession(user);
                Log.e("resultStr",""+resultStr);
                if (resultStr!=null){
                    if (!TextUtils.isEmpty(resultStr)) {
                        TokenRoot resps = JSON.parseObject(resultStr,TokenRoot.class);
                        if (resps!=null){
                            Log.e("resps",""+resps.getMsginfo());
                            token = resps.getMsginfo().substring(10);
                            token = token.substring(0,token.length()-2);
                            Log.e("获取到的Token：",""+token);
                            if (resps.getOpresult()&&resps.getMsg().equals("登录成功!")){
                                UserNewBean userInfo = WebServiceUtil.getInstance().getUserInfo(user.getUserName(),null);
                                String ceshi = userInfo.getOrgid();
                                Log.e("ceshi",""+ceshi);
                                user.copyUserInfo(userInfo);
//                            WebServiceUtil.getInstance().getUserDepartmentInfo(user);
//                            WebServiceUtil.getInstance().getUserRoleInfo(user);

                                CApplication.getInstance().setUser(user);
                                CApplication.getInstance().setToken(token);
                                Message message = new Message();
                                message.what = SHOW_RESPONSE_RELATION_SWITCH;
                                message.obj = "1";
                                handlergetdata.sendMessage(message);

                            }


                        }else {

                        }

                    }
                }

            }
        }).start();
    }


}
