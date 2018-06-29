package com.bs.lk.newoamvptest.view.activity;

import com.bs.lk.newoamvptest.bean.DepositDataListsBean;

import java.util.ArrayList;

public interface IFileDepositDetailView {
    public void onDepositDatas(ArrayList<DepositDataListsBean> depositDataListsBeans);
    public void onDepositErrorDatas(String code);
}
