package com.bs.lk.newoamvptest.view.activity;

import com.bs.lk.newoamvptest.bean.DepositDataListsBean;

import java.util.ArrayList;

public interface IFileReverseDetailView {
    public void onFileReverseDatas(ArrayList<DepositDataListsBean> reverseDataListsBeans);
    public void onReverseErrorDatas(String code);
}
