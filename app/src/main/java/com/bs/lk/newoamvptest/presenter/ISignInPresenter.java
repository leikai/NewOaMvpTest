package com.bs.lk.newoamvptest.presenter;

public interface ISignInPresenter {
    public void doDataForPrams(String time,String addresInfo,String state,String userOid,String orgId);//传入关于数量与类型的参数，用于获取新闻数据
    public void onResultToM(String data);
}
