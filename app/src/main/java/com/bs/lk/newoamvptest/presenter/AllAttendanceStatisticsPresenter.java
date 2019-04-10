package com.bs.lk.newoamvptest.presenter;

import com.alibaba.fastjson.JSON;
import com.bs.lk.newoamvptest.bean.AttendanceDataBean;
import com.bs.lk.newoamvptest.bean.AttendanceShiftsBean;
import com.bs.lk.newoamvptest.model.AllAttendanceStatisticsModel;
import com.bs.lk.newoamvptest.model.AttendanceStatisticsModel;
import com.bs.lk.newoamvptest.model.IAllAttendanceStatisticsModel;
import com.bs.lk.newoamvptest.model.IAttendanceStatisticsModel;
import com.bs.lk.newoamvptest.view.activity.IAllAttendanceStatisticsView;
import com.bs.lk.newoamvptest.view.activity.IAttendanceStatisticsView;

import org.litepal.LitePal;

import java.util.List;

public class AllAttendanceStatisticsPresenter implements IAllAttendanceStatisticsPresenter{
    private IAllAttendanceStatisticsView allAttendanceStatisticsView;
    IAllAttendanceStatisticsModel allAttendanceStatisticsModel;

    public AllAttendanceStatisticsPresenter(IAllAttendanceStatisticsView allAttendanceStatisticsView) {
        this.allAttendanceStatisticsView = allAttendanceStatisticsView;
        allAttendanceStatisticsModel = new AllAttendanceStatisticsModel(this);

    }

    @Override
    public void doAttendanceDataForPrams() {
        allAttendanceStatisticsModel.doParamsForAttendancePWithText();
    }

    @Override
    public void onAttendanceResultToM(String attendanceData) {
        String [] strJson =  attendanceData.split("&");
        String[]  strsShiftsJson = strJson[0].split("=");
        String[]  strsCeShiJson = strJson[1].split("=");
        if (strsCeShiJson[0].equals("ceshi1 ")){
            List<AttendanceDataBean> attendanceDatas  = JSON.parseArray(strsCeShiJson[1],AttendanceDataBean.class);
            LitePal.deleteAll(AttendanceDataBean.class);
            LitePal.saveAll(attendanceDatas);
//            attendanceStatisticsView.onResultToPData(attendanceDatas.get(0),1);
        }
        if (strsShiftsJson[0].equals("shiftsJson ")){
            List<AttendanceShiftsBean> attendanceShifts = JSON.parseArray(strsShiftsJson[1],AttendanceShiftsBean.class);
            LitePal.deleteAll(AttendanceShiftsBean.class);
            LitePal.saveAll(attendanceShifts);
        }
        allAttendanceStatisticsView.onResultToPData();
    }
}
