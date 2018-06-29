package com.bs.lk.newoamvptest.view.activity;

public interface ILoginView {
    public void onClearText();
    public void onLoginResult(Boolean result,int code ,String userName, String passWord, String token);
}
