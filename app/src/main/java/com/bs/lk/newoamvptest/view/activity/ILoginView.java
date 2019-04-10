package com.bs.lk.newoamvptest.view.activity;

import com.bs.lk.newoamvptest.base.mvp.Iview;

public interface ILoginView extends Iview {
    public void onClearText();
    public void onLoginResult(Boolean result,int code ,String userName, String passWord, String token);
}
