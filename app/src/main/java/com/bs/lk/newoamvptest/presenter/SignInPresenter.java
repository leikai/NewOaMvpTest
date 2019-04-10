package com.bs.lk.newoamvptest.presenter;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.bs.lk.newoamvptest.bean.AttendanceResultBean;
import com.bs.lk.newoamvptest.bean.NewsContentPattsRoot;
import com.bs.lk.newoamvptest.model.ISignInModel;
import com.bs.lk.newoamvptest.model.SignInModel;
import com.bs.lk.newoamvptest.view.activity.ISignInView;

import java.util.ArrayList;
import java.util.List;

public class SignInPresenter implements ISignInPresenter{
    private ISignInView signInView;
    ISignInModel signInModel;

    public SignInPresenter(ISignInView signInView) {
        this.signInView = signInView;
        signInModel = new SignInModel(this);
    }

    @Override
    public void doDataForPrams(String time, String addresInfo, String state, String userOid,String orgId) {
        signInModel.doParamsForWeiPWithText(time,addresInfo,state,userOid,orgId);
    }

    @Override
    public void onResultToM(String data) {

        AttendanceResultBean feedBackResult = JSON.parseObject(data,AttendanceResultBean.class);
        signInView.onResultToP(feedBackResult);
//        NewsContentPattsRoot resps = JSON.parseObject(data,NewsContentPattsRoot.class);
////                        List<NewsBean> resps=new ArrayList<NewsBean>(JSONArray.parseArray(response,NewsBean.class));
////                            CourtDataBean resp = JSON.parseObject(response, CourtDataBean.class);
//        List<NewsContentPattsRoot> newsDetails = new ArrayList<NewsContentPattsRoot>();
//        newsDetails.add(resps);
//        Log.e("lk",""+resps.getFtitle());
//
//        signInView.onResultToP(newsDetails);


    }
}
