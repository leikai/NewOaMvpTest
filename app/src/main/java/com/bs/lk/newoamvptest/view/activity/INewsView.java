package com.bs.lk.newoamvptest.view.activity;

import com.bs.lk.newoamvptest.bean.BannerBean;
import com.bs.lk.newoamvptest.bean.NewsBean;
import com.bs.lk.newoamvptest.bean.NewsContentPattsRoot;

import java.util.List;

public interface INewsView {
    public void onResultToP(List<NewsContentPattsRoot> newsDetails );
    public void onResultToPWithIV(List<BannerBean>  mNewsDatas);
    public void onResultForError();
}
