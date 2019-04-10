package com.bs.lk.newoamvptest.presenter;

import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.bs.lk.newoamvptest.bean.BannerBean;
import com.bs.lk.newoamvptest.bean.NewsBean;
import com.bs.lk.newoamvptest.model.HomePageNewsModel;
import com.bs.lk.newoamvptest.model.IHomePageNewsModel;
import com.bs.lk.newoamvptest.util.GsonUtil;
import com.bs.lk.newoamvptest.view.activity.fragment.IHomePageNewsView;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class HomePageNewsPresenter implements IHomePageNewsPresenter{
    private IHomePageNewsView homePageNewsView;
    private IHomePageNewsModel homePageNewsModel;

    public HomePageNewsPresenter(IHomePageNewsView homePageNewsView) {
        this.homePageNewsView = homePageNewsView;
        homePageNewsModel = new HomePageNewsModel(this);
    }

    @Override
    public void doDataForPrams(String json) {
        homePageNewsModel.doParamsForWeiPWithText(json);
    }

    @Override
    public void doImageForPrams(String json) {
        homePageNewsModel.doParamsForWeiPWithImage(json);
    }

    @Override
    public void onResultToM(String data) {
        if ("0".equals(data)){
            homePageNewsView.onResultForError();
        }else if (data.equals("anyType{}")){
            homePageNewsView.onResultForError();

        }else {
            List<NewsBean> resps=new ArrayList<NewsBean>(JSONArray.parseArray(data,NewsBean.class));
//                            CourtDataBean resp = JSON.parseObject(response, CourtDataBean.class);
            Log.e("lk",""+resps.get(0).getOid());

            Log.e("list",""+resps.get(0).getFtitle());
            Log.e("list",""+resps.get(0).getKeyword());
            homePageNewsView.onResultToP(resps);

        }
    }

    @Override
    public void onResultToMWithIV(String data) {
        if (data != null){
            List<BannerBean> bannerBeen_list = GsonUtil.getListFromJson(data, new TypeToken<List<BannerBean>>(){});
            homePageNewsView.onResultToPWithIV(bannerBeen_list);
        }
    }
}
