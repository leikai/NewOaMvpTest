package com.bs.lk.newoamvptest.model;

public interface IAttendanceListModel {
    //接收从新闻模块P层传递过来的用于获取新闻文字的参数数据
    public void doParamsForWeiPWithText(String time,String addresInfo,String state,String userOid,String orgId,int cardType);
    //接收从新闻模块P层传递过来的用于获取新闻文字的参数数据
    public void doParamsForAttendancePWithText(String time,String userOid);

}
