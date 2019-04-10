package com.bs.lk.newoamvptest.view.activity.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bs.lk.newoamvptest.CApplication;
import com.bs.lk.newoamvptest.R;
import com.bs.lk.newoamvptest.TabsActivity;
import com.bs.lk.newoamvptest.bean.TokenRoot;
import com.bs.lk.newoamvptest.bean.UserNewBean;
import com.bs.lk.newoamvptest.common.Contstants;
import com.bs.lk.newoamvptest.util.PinYin.SharedPreferencesUtil;
import com.bs.lk.newoamvptest.util.WebServiceUtil;
import com.bs.lk.newoamvptest.view.activity.SignOnActivity;
import com.wangnan.library.GestureLockView;
import com.wangnan.library.listener.OnGestureLockListener;

import static android.support.constraint.Constraints.TAG;

public class ModifyHandlePasswordFragment extends BaseFragment implements View.OnClickListener {
    GestureLockView mGestureLockView;
    UserNewBean currentUser;
    TextView mRemind;
    String setHandlePwdOriginal ;
    private ChangeHandlePwdTask mAuthTask = null;
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = "修改手势密码";
        mShowBtnBack = View.VISIBLE;
        mLogo = View.INVISIBLE;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return onCreateView(inflater, R.layout.fragment_modify_handle_pwd, container, savedInstanceState);
    }


    @Nullable
    @Override
    protected void initFragment(@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        currentUser = CApplication.getInstance().getCurrentUser();
        mRemind = mRootView.findViewById(R.id.tv_remind);
        mGestureLockView = (GestureLockView) mRootView.findViewById(R.id.modify_handle_pwd_glv);
        mGestureLockView.setPainter(new SignOnActivity.NiCaiFu360Painter());
        //设置手势监听器
        mGestureLockView.setGestureLockListener(new OnGestureLockListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(String progress) {

            }

            @Override
            public void onComplete(String result) {

                Log.d(TAG, "onComplete: "+"  "+result);
                if (TextUtils.isEmpty(result)){
                    return;
                }else {
                    String userName = "gaohongwei";
//                    String remind = mRemind.getText().toString();
                    if ("请输入原手势密码".equals(mRemind.getText().toString())){
                        if (result.equals(currentUser.getHandPassword())){
                            mRemind.setText("请绘制修改后的手势密码");
                            mGestureLockView.clearView();
                        }else {
                            mGestureLockView.clearView();
                            Toast.makeText(getActivity(),"对不起，您输入的原密码错误",Toast.LENGTH_SHORT).show();
                        }
                    }else if ("请绘制修改后的手势密码".equals(mRemind.getText().toString())){
                         setHandlePwdOriginal = result;
                         mRemind.setText("请再次绘制修改后的手势密码");
                         mGestureLockView.clearView();
                    }else if ("请再次绘制修改后的手势密码".equals(mRemind.getText().toString())){
                        if (setHandlePwdOriginal.equals(result)){
                            mAuthTask = new ChangeHandlePwdTask(currentUser.getUserName(), result);
                            mAuthTask.execute();

                        }else {
                            mRemind.setText("请输入原手势密码");
                            Toast.makeText(getActivity(),"密码修改失败，请重试",Toast.LENGTH_SHORT).show();
                            mGestureLockView.clearView();

                        }
                    }

                    return;

                }
            }
        });
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class ChangeHandlePwdTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mHandPassword;
        private String mMessage;

        ChangeHandlePwdTask(String username, String handPassword) {
            mUsername = username;
            mHandPassword = handPassword;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mUsername)) {
                    return pieces[1].equals(mHandPassword);
                }
            }
            UserNewBean user = new UserNewBean();
            String token = null;

            user.setUserName(mUsername);
            user.setHandPassword(mHandPassword);
//            user.setUserPassword(mHandPassword);

            String resultStr = WebServiceUtil.getInstance().changeUserPWD(mUsername,mHandPassword);
            Log.e("resultStr",""+resultStr);
            if (resultStr!=null){
                if (!TextUtils.isEmpty(resultStr)) {
                    if ("true".equals(resultStr)){
                        mMessage = "修改成功";
                        return true;
                    }else {
                        mMessage = "修改失败";
                        return false;
                    }

                }
            }
            mMessage = "修改失败";
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
//            mProgressView.stop();
            if (!TextUtils.isEmpty(mMessage)) {
                Toast.makeText(getActivity(), mMessage, Toast.LENGTH_LONG).show();
                mRemind.setText("手势密码修改完成");
//                mGestureLockView.clearView();
//                return;
            }
            if (success) {
                mGestureLockView.clearView();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
//            mProgressView.stop();
        }
    }


    @Override
    public void onClick(View view) {

    }




}
