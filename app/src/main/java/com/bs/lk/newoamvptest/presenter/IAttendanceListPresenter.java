package com.bs.lk.newoamvptest.presenter;

public interface IAttendanceListPresenter {

    /**
     * 传入关于数量与类型的参数，用于获取新闻数据
     * @param time
     * @param addresInfo
     * @param state
     * @param userOid
     * @param orgId
     * @param carType
     * @param isUpdate
     */
    public void doDataForPrams(String time,String addresInfo,String state,String userOid,String orgId,int carType,Boolean isUpdate);
    public void doAttendanceDataForPrams(String time,String userOid);

    public void onResultToM(String data);
    public void onAttendanceResultToM(String attendanceData);
}
