package com.bs.lk.newoamvptest.view.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bs.lk.newoamvptest.CApplication;
import com.bs.lk.newoamvptest.R;
import com.bs.lk.newoamvptest.bean.UserNewBean;
import com.bs.lk.newoamvptest.cache.CSharedPreferences;
import com.bs.lk.newoamvptest.util.NetUtils;
import com.bs.lk.newoamvptest.view.activity.LoginActivity;
import com.bs.lk.newoamvptest.view.activity.ModifyPwdActivity;
import com.bs.lk.newoamvptest.view.activity.SwitchAccountActivity;
import com.geek.thread.GeekThreadManager;
import com.geek.thread.ThreadPriority;
import com.geek.thread.ThreadType;
import com.geek.thread.task.GeekRunnable;

import static com.bs.lk.newoamvptest.util.NetUtils.LOGIN_PATH;

public class SettingFragment extends BaseFragment implements View.OnClickListener {
    private UserNewBean userCurrent;
    private TextView tvUserName;
    private TextView tvCompany;
    private TextView tvDepartment;
    private TextView tvMobile;
    private TextView tvEmail;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = "设置";
        mShowBtnBack = View.INVISIBLE;
        mLogo = View.INVISIBLE;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return onCreateView(inflater, R.layout.fragment_setting, container, savedInstanceState);
    }

    @Nullable
    @Override
    protected void initFragment(@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView.findViewById(R.id.btn_modify_pwd).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_modify_handle_pwd).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_modify_username).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_modify_serverip).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_switch_account).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_logout).setOnClickListener(this);
        //个人信息
        userCurrent = CApplication.getInstance().getCurrentUser();
        tvUserName = mRootView.findViewById(R.id.tv_user_name);
        tvCompany = mRootView.findViewById(R.id.tv_company);
        tvDepartment = mRootView.findViewById(R.id.tv_department);
        tvEmail = mRootView.findViewById(R.id.tv_email);
        tvMobile = mRootView.findViewById(R.id.tv_mobile);

        String empMobilePhone = userCurrent.getEmpMobilephone();
        String empEmail= userCurrent.getEmpEmail();


        tvUserName.setText(userCurrent.getEmpname());
        tvCompany.setText(userCurrent.getOrgname());
        tvDepartment.setText(userCurrent.getDeptname());
        if (empMobilePhone!=null){
            if (!empMobilePhone.equals("null")){
                tvMobile.setText(empMobilePhone);
            }else {
                tvMobile.setText("");
            }
        }else {
            tvMobile.setText("");
        }
        if (empEmail!=null){
            if (!empEmail.equals("null")){
                tvEmail.setText(empEmail);
            }else {
                tvEmail.setText("");
            }
        }else {
            tvEmail.setText("");
        }



        String name = CApplication.getInstance().getCurrentUser().getUserName();
        String password = CApplication.getInstance().getCurrentUser().getUserPassword();

        if ("admin".equals(name) && "1231".equals(password)){
            mRootView.findViewById(R.id.btn_modify_courtoa_list).setVisibility(View.VISIBLE);
            mRootView.findViewById(R.id.btn_modify_courtoa_list).setOnClickListener(this);
        }





    }

    @Override
    public void showChildFragment(Bundle bundle) {
        mPreFragment.showChildFragment(bundle);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_modify_pwd: {

                startActivity(new Intent(getActivity(), ModifyPwdActivity.class));


//                Bundle bundle = new Bundle();
//                bundle.putInt(SettingManagerFragment.PARAM_CHILD_TYPE, SettingManagerFragment.CHILD_TYPE_MODIFYPWD);
//                mPreFragment.showChildFragment(bundle);
                break;
            }
            case R.id.btn_modify_handle_pwd: {
                Bundle bundle = new Bundle();
                bundle.putInt(SettingManagerFragment.PARAM_CHILD_TYPE, SettingManagerFragment.CHILD_TYPE_MODIFYHANDLEPWD);
                mPreFragment.showChildFragment(bundle);
                break;
            }
            case R.id.btn_modify_username: {
                Bundle bundle = new Bundle();
                bundle.putInt(SettingManagerFragment.PARAM_CHILD_TYPE, SettingManagerFragment
                        .CHILD_TYPE_MODIFYUSERNAME);
                mPreFragment.showChildFragment(bundle);
                break;
            }
            case R.id.btn_modify_serverip: {
                Bundle bundle = new Bundle();
                bundle.putInt(SettingManagerFragment.PARAM_CHILD_TYPE, SettingManagerFragment
                        .CHILD_TYPE_MODIFYSERVERIP);
                mPreFragment.showChildFragment(bundle);
                break;
            }
            case R.id.btn_modify_courtoa_list: {
                Bundle bundle = new Bundle();
                bundle.putInt(SettingManagerFragment.PARAM_CHILD_TYPE, SettingManagerFragment
                        .CHILD_TYPE_MODIFYCOURTOALIST);
                mPreFragment.showChildFragment(bundle);
                break;
            }
            case R.id.btn_switch_account: {
                Intent intent = new Intent();
                intent.setClass(getActivity(), SwitchAccountActivity.class);
                startActivity(intent);
//                getActivity().finish();
                break;
            }
            case R.id.btn_logout: {
                CSharedPreferences.getInstance().clear();
                Intent intent = new Intent();
                intent.setClass(getActivity(), LoginActivity.class);

//                GeekThreadManager.getInstance().execute(new GeekRunnable(ThreadPriority.NORMAL) {
//                    @Override
//                    public void run() {
//                        String v3Session = NetUtils.requestByPost(jsonObjString,loginPath);
//                    }
//                }, ThreadType.NORMAL_THREAD);





                startActivity(intent);
                getActivity().finish();





                break;
            }
            default:
                break;
        }
    }

}
