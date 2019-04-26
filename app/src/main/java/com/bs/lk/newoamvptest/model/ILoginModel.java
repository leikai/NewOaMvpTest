package com.bs.lk.newoamvptest.model;

import com.bs.lk.newoamvptest.base.BaseModel;
import com.bs.lk.newoamvptest.bean.SessionGsonFormatBean;

import io.reactivex.Observable;

public abstract class ILoginModel extends BaseModel {
    public abstract void doLoginData(String name,String password);

    public abstract Observable<SessionGsonFormatBean>  loadAndroidVersion (String url, int type);



}
