package com.bs.lk.newoamvptest.presenter;

public interface INewsPresenter {
    public void doDataForPrams(String json);//传入关于数量与类型的参数，用于获取新闻数据
    public void doImageForPrams(String json);//传入关于数量与类型的参数，用于获取图片数据
    public void onResultToM(String data);
    public void onResultToMWithIV(String data);//将获得的关于图片的数据传回到M层进行处理

}
