package com.bs.lk.newoamvptest.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageButton;

import com.bs.lk.newoamvptest.CApplication;
import com.bs.lk.newoamvptest.R;
import com.bs.lk.newoamvptest.TabsActivity;
import com.bs.lk.newoamvptest.bean.UserNewBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author lk
 */
public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.btn_welcome)
    ImageButton btnWelcome;

    private Handler handler;
    private boolean isJump = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        Intent intent1 = getIntent();
        Uri uri=intent1.getData();
        if(uri!=null) {
            String name = uri.getQueryParameter("name");
            String scheme = uri.getScheme();
            String host = uri.getHost();
            String port = uri.getPort() + "";
            String path = uri.getPath();
            String query = uri.getQuery();
            Log.e("获得的数据name", "获得的数据name=" + name + "/r" + "scheme" + scheme + "/r" + "host" +
                    "host" + host + "/r" + "port" + port + "/r" + "path" + path + "/r" + "query" + query);
        }
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isJump){
                    Intent intent = new Intent();

                    UserNewBean userNewBean = CApplication.getInstance().getCurrentUser();
                    if (userNewBean != null) {
                        Log.e("当前的userNewBean", "" + userNewBean.getEmpname());
                    }
                    String token = CApplication.getInstance().getCurrentToken();
                    if (token != null) {
                        Log.e("当前的token", "" + token);
                    }
                    if (CApplication.getInstance().getCurrentUser() == null || CApplication.getInstance().getCurrentToken() == null) {
                        intent.setClass(SplashActivity.this, LoginActivity.class);
                    } else {
                        intent.setClass(SplashActivity.this, TabsActivity.class);
                    }
                    startActivity(intent);
                    finish();
                }
            }
        }, 3000);



    }

    @OnClick(R.id.btn_welcome)
    public void onViewClicked() {


        Intent intent = new Intent();
        isJump = false;
        UserNewBean userNewBean = CApplication.getInstance().getCurrentUser();
        if (userNewBean != null) {
            Log.e("当前的userNewBean", "" + userNewBean.getEmpname());
        }
        String token = CApplication.getInstance().getCurrentToken();
        if (token != null) {
            Log.e("当前的token", "" + token);
        }


        if (CApplication.getInstance().getCurrentUser() == null || CApplication.getInstance().getCurrentToken() == null) {
            intent.setClass(SplashActivity.this, LoginActivity.class);
        } else {
            intent.setClass(SplashActivity.this, LoginActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
