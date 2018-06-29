package com.bs.lk.newoamvptest.presenter;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.bs.lk.newoamvptest.bean.NewsContentPattsRoot;
import com.bs.lk.newoamvptest.model.INewsModel;
import com.bs.lk.newoamvptest.model.NewsModel;
import com.bs.lk.newoamvptest.view.activity.INewsView;

import java.util.ArrayList;
import java.util.List;

public class NewsPresenter implements INewsPresenter {
    private INewsView newsView;
    INewsModel newsModel;

    public NewsPresenter(INewsView newsView) {
        this.newsView = newsView;
        newsModel = new NewsModel(this);
    }

    @Override
    public void doDataForPrams(String json) {
        newsModel.doParamsForWeiPWithText(json);
    }

    @Override
    public void doImageForPrams(String json) {

    }

    @Override
    public void onResultToM(String data) {
        NewsContentPattsRoot resps = JSON.parseObject(data,NewsContentPattsRoot.class);
//                        List<NewsBean> resps=new ArrayList<NewsBean>(JSONArray.parseArray(response,NewsBean.class));
//                            CourtDataBean resp = JSON.parseObject(response, CourtDataBean.class);
        List<NewsContentPattsRoot> newsDetails = new ArrayList<NewsContentPattsRoot>();
        newsDetails.add(resps);
        Log.e("lk",""+resps.getFtitle());

        newsView.onResultToP(newsDetails);

    }

    @Override
    public void onResultToMWithIV(String data) {

    }
}
