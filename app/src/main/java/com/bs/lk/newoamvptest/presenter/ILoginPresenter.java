package com.bs.lk.newoamvptest.presenter;

public interface ILoginPresenter {
    public void clear();
    public void doLogin(String name,String password);
    public void onLoginModelResult(String token);

}
