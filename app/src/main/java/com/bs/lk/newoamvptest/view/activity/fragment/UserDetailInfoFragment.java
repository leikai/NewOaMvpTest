package com.bs.lk.newoamvptest.view.activity.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.lk.newoamvptest.R;
import com.bs.lk.newoamvptest.TabsActivity;
import com.bs.lk.newoamvptest.bean.UserNewBean;
import com.bs.lk.newoamvptest.util.WebServiceUtil;
import com.bs.lk.newoamvptest.widget.CustomProgress;

import java.util.Objects;
import java.util.Vector;

/**
 * 通讯录详情
 * @author asus
 */
public class UserDetailInfoFragment extends BaseFragment implements View.OnClickListener {
    public static final String PARAM_USER = "user";
    UserNewBean mUser;
    RefreshUserInfoTask mRefreshUserInfoTask;
    CustomProgress mCustomProgress;
    LinearLayout mUserInfoContainer;
    TextView mUserNameView;
    TextView mDepartmentView;
    private static final int SDK_VERSION = 23;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(PARAM_USER)) {
            mUser = (UserNewBean) getArguments().getSerializable(PARAM_USER);
            assert mUser != null;
            Log.e("用户信息",""+mUser.getEmpname());

        }
        mTitle = "通讯录";
        mShowBtnBack = View.INVISIBLE;
        mLogo = View.INVISIBLE;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return onCreateView(inflater, R.layout.fragment_user_detail_info, container,
                savedInstanceState);
    }

    @Override
    protected void initFragment(@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mUserInfoContainer =  mRootView.findViewById(R.id.userinfo_container);
        mCustomProgress = mRootView.findViewById(R.id.loading);
        mUserNameView =  mRootView.findViewById(R.id.tv_username);
        mDepartmentView =  mRootView.findViewById(R.id.tv_department);
        if (!TextUtils.isEmpty(mUser.getEmpname())) {
            mUserNameView.setText(mUser.getEmpname());
        }
        if (!TextUtils.isEmpty(mUser.getDeptname())) {
            mDepartmentView.setText(mUser.getDeptname());
        }
        mRootView.findViewById(R.id.btn_tail).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_sms).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        mRefreshUserInfoTask = new RefreshUserInfoTask();
        mRefreshUserInfoTask.execute();
    }
    /**
     * 通讯录中个人详情页面
     */
    @SuppressLint("HandlerLeak")
    private Handler mUpdateUserInfoHandler = new Handler() {
        @SuppressLint("InflateParams")
        @Override
        public void dispatchMessage(Message msg) {
            mUser = (UserNewBean) msg.obj;
            if (!TextUtils.isEmpty(mUser.getEmpname())) {
                mUserNameView.setText(mUser.getEmpname());
            }
            if (!TextUtils.isEmpty(mUser.getDeptname())) {
                mDepartmentView.setText(mUser.getDeptname());
            }
            View childView = LayoutInflater.from(getContext()).inflate(R.layout.user_info_item,
                    null);
            TextView labelView = childView.findViewById(R.id.tv_label);
            TextView valueView = childView.findViewById(R.id.tv_value);
            labelView.setText("手机");
            if (!TextUtils.isEmpty(mUser.getEmpMobilephone())) {
                valueView.setText(mUser.getEmpMobilephone());
            }
            mUserInfoContainer.addView(childView);

            childView = LayoutInflater.from(getContext()).inflate(R.layout.user_info_item,
                    null);
            labelView = childView.findViewById(R.id.tv_label);
            valueView = childView.findViewById(R.id.tv_value);
            labelView.setText("电话");
            if (!TextUtils.isEmpty(mUser.getEmpMobilephone())) {
                valueView.setText(mUser.getEmpMobilephone());
            }
            mUserInfoContainer.addView(childView);
            // TODO: 2017/2/17

            childView = LayoutInflater.from(getContext()).inflate(R.layout.user_info_item,
                    null);
            labelView = childView.findViewById(R.id.tv_label);
            valueView = childView.findViewById(R.id.tv_value);
            labelView.setText("邮箱");
            if (!TextUtils.isEmpty(mUser.getEmpEmail())) {
                valueView.setText(mUser.getEmpEmail());
            }
            mUserInfoContainer.addView(childView);

            childView = LayoutInflater.from(getContext()).inflate(R.layout.user_info_item,
                    null);
            labelView = childView.findViewById(R.id.tv_label);
            valueView = childView.findViewById(R.id.tv_value);
            labelView.setText("部门");
            if (!TextUtils.isEmpty(mUser.getDeptname())) {
                valueView.setText(mUser.getDeptname());
            }
            mUserInfoContainer.addView(childView);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //通讯录个人详情页面
            case R.id.btn_tail: {
                Vector<String> nums = new Vector<>();
                if(!TextUtils.isEmpty(mUser.getEmpMobilephone())){
                    nums.add(mUser.getEmpMobilephone());
                }
                if(!TextUtils.isEmpty(mUser.getEmpMobilephone())){
                    nums.add(mUser.getEmpMobilephone());
                }
                if(nums.size()>0){
                    String[] phoneNumArrsy = new String[nums.size()];
                    nums.toArray(phoneNumArrsy);
                    DisplayMetrics ds = getResources().getDisplayMetrics();
                    int height = 2 * ds.heightPixels / 3;
                    View anchorView = mRootView.findViewById(R.id.tool_bar);
                    int yOff = -height;
                    anchorView.getHeight();
                    showGroupPopUpWindow(anchorView, phoneNumArrsy, yOff,
                            mPhoneNumsOnClickListener);
                }
                break;
            }
            //通讯录个人详情页面
            case R.id.btn_sms: {
                if (!TextUtils.isEmpty(mUser.getEmpMobilephone())) {
                    if (android.os.Build.VERSION.SDK_INT < SDK_VERSION || PackageManager.PERMISSION_GRANTED ==
                            ContextCompat
                                    .checkSelfPermission(Objects.requireNonNull(getActivity()),
                                            Manifest.permission.SEND_SMS)) {
                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("smsto:" + mUser.getEmpMobilephone()));
                        startActivity(intent);
                    } else {
                        String[] permissions = {Manifest
                                .permission.SEND_SMS};
                        ActivityCompat.requestPermissions(getActivity(), permissions,
                                TabsActivity.REQUEST_SEND_SMS);
                    }
                }
                break;
            }
            default:
                break;
        }
    }

    private AdapterView.OnItemClickListener mPhoneNumsOnClickListener = (parent, view, position, id) -> {
        String phoneNum = parent.getAdapter().getItem(position).toString();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    };

    /**
     * 获取用户信息
     */
    @SuppressLint("StaticFieldLeak")
    private class RefreshUserInfoTask extends AsyncTask<Void, Void, UserNewBean> {
        RefreshUserInfoTask() {
        }

        @Override
        protected void onPreExecute() {
            mCustomProgress.start();
        }

        @Override
        protected UserNewBean doInBackground(Void... params) {
            return WebServiceUtil.getInstance().getUserInfo(null,mUser.getOid());
        }

        @Override
        protected void onPostExecute(UserNewBean user) {
            try {
                if (user != null) {
                    Message msg = mUpdateUserInfoHandler.obtainMessage();
                    msg.obj = user;
                    mUpdateUserInfoHandler.sendMessage(msg);
                } else {
                    Toast.makeText(getContext(), "获取用户信息失败", Toast.LENGTH_LONG).show();
                }
            } finally {
                mRefreshUserInfoTask = null;
                mCustomProgress.stop();
            }
        }

        @Override
        protected void onCancelled() {
            mRefreshUserInfoTask = null;
            mCustomProgress.stop();
        }
    }
}
