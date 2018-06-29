package com.bs.lk.newoamvptest.presenter;

import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.bs.lk.newoamvptest.bean.BannerBean;
import com.bs.lk.newoamvptest.bean.NewsBean;
import com.bs.lk.newoamvptest.model.IWeidoorModel;
import com.bs.lk.newoamvptest.model.WeidoorModel;
import com.bs.lk.newoamvptest.util.GsonUtil;
import com.bs.lk.newoamvptest.view.activity.IWeiDoorView;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class WeidoorPresenter implements IWeidoorPresenter {
    private IWeiDoorView weiDoorView;

    IWeidoorModel weidoorModel;

    public WeidoorPresenter(IWeiDoorView weiDoorView) {
        this.weiDoorView = weiDoorView;
        weidoorModel = new WeidoorModel(this);
    }

    @Override
    public void doDataForPrams(String json) {

        weidoorModel.doParamsForWeiPWithText(json);
    }

    @Override
    public void doImageForPrams(String json) {
        weidoorModel.doParamsForWeiPWithImage(json);
    }

    @Override
    public void onResultToM(String data) {
        if ("0".equals(data)){
            weiDoorView.onResultForError();
        }else if (data.equals("anyType{}")){
            weiDoorView.onResultForError();

        }else {
            List<NewsBean> resps=new ArrayList<NewsBean>(JSONArray.parseArray(data,NewsBean.class));
//                            CourtDataBean resp = JSON.parseObject(response, CourtDataBean.class);
            Log.e("lk",""+resps.get(0).getOid());

            Log.e("list",""+resps.get(0).getFtitle());
            Log.e("list",""+resps.get(0).getKeyword());
            weiDoorView.onResultToP(resps);

        }
    }

    @Override
    public void onResultToMWithIV(String data) {
        if (data != null){
            List<BannerBean> bannerBeen_list = GsonUtil.getListFromJson(data, new TypeToken<List<BannerBean>>(){});
            weiDoorView.onResultToPWithIV(bannerBeen_list);
        }
    }
}
