package com.bs.lk.newoamvptest.view.activity;

import com.bs.lk.newoamvptest.bean.AttendanceResultBean;

public interface ISignInView {
    public void onResultToP(AttendanceResultBean feedBackResult);
    public void onResultForError();
}
