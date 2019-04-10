package com.bs.lk.newoamvptest.view.activity;

import com.bs.lk.newoamvptest.bean.AttendanceDataBean;
import com.bs.lk.newoamvptest.bean.AttendanceResultBean;

public interface IAttendanceListView<T> {
    public void onResultToP(AttendanceResultBean feedBackResult,int cardType,Boolean isUpdate);
    public void onResultToPData(T t,int code);
    public void onResultForError();
}
