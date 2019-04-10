package com.bs.lk.newoamvptest.presenter;

import com.alibaba.fastjson.JSON;
import com.bs.lk.newoamvptest.bean.AttendanceDataBean;
import com.bs.lk.newoamvptest.bean.AttendanceResultBean;
import com.bs.lk.newoamvptest.bean.AttendanceShiftsBean;
import com.bs.lk.newoamvptest.model.AttendanceListMdel;
import com.bs.lk.newoamvptest.model.IAttendanceListModel;
import com.bs.lk.newoamvptest.model.ISignInModel;
import com.bs.lk.newoamvptest.model.SignInModel;
import com.bs.lk.newoamvptest.view.activity.IAttendanceListView;
import com.bs.lk.newoamvptest.view.activity.ISignInView;

import org.litepal.LitePal;

import java.util.List;

/**
 * @author lk
 */
public class AttendanceListPresenter implements IAttendanceListPresenter{
    private IAttendanceListView attendanceListView;
    IAttendanceListModel attendanceListModel;
    private int cardType;
    private Boolean isUpdateCard;

    public AttendanceListPresenter(IAttendanceListView attendanceListView) {
        this.attendanceListView = attendanceListView;
        attendanceListModel = new AttendanceListMdel(this);
    }

    @Override
    public void doDataForPrams(String time, String addresInfo, String state, String userOid,String orgId,int cardtype,Boolean isUpdate) {
        cardType = cardtype;
        isUpdateCard = isUpdate;
        attendanceListModel.doParamsForWeiPWithText(time,addresInfo,state,userOid,orgId,cardType);
    }

    @Override
    public void doAttendanceDataForPrams(String time, String userOid) {
        attendanceListModel.doParamsForAttendancePWithText(time,userOid);
    }

    @Override
    public void onResultToM(String data) {
        AttendanceResultBean feedBackResult = JSON.parseObject(data,AttendanceResultBean.class);
        attendanceListView.onResultToP(feedBackResult,cardType,isUpdateCard);
    }

    @Override
    public void onAttendanceResultToM(String attendanceData) {
        String[]  strs = attendanceData.split("=");
        if (strs[0].equals("ceshi1 ")){
            List<AttendanceDataBean> attendanceDatas  = JSON.parseArray(strs[1],AttendanceDataBean.class);
            if (attendanceDatas.size() == 0){
                AttendanceDataBean attendanceDataBean = new AttendanceDataBean();
                attendanceDataBean.setSsignintimte("null");
                attendanceDataBean.setAmbdkwz("null");
                attendanceDataBean.setSsignbacktime("null");
                attendanceDataBean.setAmedkwz("null");
                attendanceDataBean.setXsignintimte("null");
                attendanceDataBean.setPmbdkwz("null");
                attendanceDataBean.setXsignbacktime("null");
                attendanceDataBean.setPmedkwz("null");
                attendanceListView.onResultToPData(attendanceDataBean,1);
            }else {
                attendanceListView.onResultToPData(attendanceDatas.get(0),1);
            }


        }
        if (strs[0].equals("shiftsJson ")){
            List<AttendanceShiftsBean> attendanceShifts = JSON.parseArray(strs[1],AttendanceShiftsBean.class);
            LitePal.saveAll(attendanceShifts);
            if (attendanceShifts.size()!=0){
                attendanceListView.onResultToPData(attendanceShifts.get(0),0);
            }else {
                attendanceListView.onResultToPData(null,0);
            }
        }


    }
}
