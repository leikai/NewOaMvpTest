package com.bs.lk.newoamvptest.model;

public interface INewsModel {
    public void doParamsForWeiPWithText(String json);//接收从新闻模块P层传递过来的用于获取新闻文字的参数数据
    public void doParamsForWeiPWithImage(String json);//接收从新闻模块P层传递过来的用于获取新闻图片的参数数据
}
