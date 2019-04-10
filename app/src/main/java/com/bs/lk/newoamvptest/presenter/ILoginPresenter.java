package com.bs.lk.newoamvptest.presenter;

import com.bs.lk.newoamvptest.base.mvp.BasePresenter;
import com.bs.lk.newoamvptest.view.activity.ILoginView;

public abstract class ILoginPresenter extends BasePresenter<ILoginView> {
    public abstract void clear();
    public abstract void doLogin(String name,String password);
    public abstract void onLoginModelResult(String token);

}
