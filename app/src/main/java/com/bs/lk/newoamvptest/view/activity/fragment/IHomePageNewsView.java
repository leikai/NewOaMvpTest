package com.bs.lk.newoamvptest.view.activity.fragment;

import com.bs.lk.newoamvptest.bean.BannerBean;
import com.bs.lk.newoamvptest.bean.NewsBean;

import java.util.List;

public interface IHomePageNewsView {
    public void onResultToP(List<NewsBean> mNewsDatas);
    public void onResultToPWithIV(List<BannerBean>  mNewsDatas);
    public void onResultForError();

}
