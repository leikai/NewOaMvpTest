package com.bs.lk.newoamvptest.presenter;

import android.util.Log;

import com.bs.lk.newoamvptest.base.mvp.ApiCallBack;
import com.bs.lk.newoamvptest.bean.SessionGsonFormatBean;
import com.bs.lk.newoamvptest.bean.UserNewBean;
import com.bs.lk.newoamvptest.model.ILoginModel;
import com.bs.lk.newoamvptest.model.LoginModel;
import com.bs.lk.newoamvptest.view.activity.ILoginView;

public class LoginPresenter extends ILoginPresenter {
    private ILoginView loginView;
    private UserNewBean user;

    ILoginModel loginModel;

    public LoginPresenter(ILoginView loginView) {
        this.loginView = loginView;
        loginModel = new LoginModel(this);
    }
    @Override
    public void clear() {
        loginView.onClearText();
    }

    @Override
    public void doLogin(String name, String password) {
        user = new UserNewBean();
        String token = null;
        user.setUserName(name);
        user.setUserPassword(password);
        loginModel.doLoginData(name,password);
        addSubscription(loginModel.loadAndroidVersion("http://47.92.241.228:40/gz/document",0),new ApiCallBack<SessionGsonFormatBean>(){
            @Override
            public void onSuccess(SessionGsonFormatBean modelBean) {
                Log.e("RxJava + retrofit","实践成功");
            }

            @Override
            public void onFailure(String errorMsg) {
                Log.e("","");
            }

            @Override
            public void onFinished() {
                Log.e("","");
            }
        });


    }

    @Override
    public void onLoginModelResult(String token) {
        final boolean[] result = {false};
        final int[] code = {0};

        if (token.startsWith("登录失败!")){
            loginView.onLoginResult(result[0], code[0],user.getUserName(),user.getUserPassword(),token);
        } else {
            result[0] = true;
            code[0] = 1;
            loginView.onLoginResult(result[0], code[0],user.getUserName(),user.getUserPassword(),token);
        }


    }

    @Override
    public void subscribe() {

    }
}
