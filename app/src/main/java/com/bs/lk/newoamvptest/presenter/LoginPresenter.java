package com.bs.lk.newoamvptest.presenter;

import com.bs.lk.newoamvptest.CApplication;
import com.bs.lk.newoamvptest.bean.UserNewBean;
import com.bs.lk.newoamvptest.model.ILoginModel;
import com.bs.lk.newoamvptest.model.LoginModel;
import com.bs.lk.newoamvptest.view.activity.ILoginView;

public class LoginPresenter implements ILoginPresenter{
    private ILoginView loginView;
    private UserNewBean user;

    ILoginModel loginModel;

    public LoginPresenter(ILoginView loginView) {
        this.loginView = loginView;
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
        loginModel = new LoginModel(this);
        loginModel.doLoginData(name,password);
    }

    @Override
    public void onLoginModelResult(String token) {
        final boolean[] result = {false};
        final int[] code = {0};

        if (token ==null){
            loginView.onLoginResult(result[0], code[0],user.getUserName(),user.getUserPassword(),token);
        }else {
            result[0] = true;
            code[0] = 1;
            loginView.onLoginResult(result[0], code[0],user.getUserName(),user.getUserPassword(),token);
        }


    }
}
