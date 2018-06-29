package com.bs.lk.newoamvptest.presenter;

import com.alibaba.fastjson.JSON;
import com.bs.lk.newoamvptest.bean.DepositDataListsBean;
import com.bs.lk.newoamvptest.bean.DepositDataListsRoot;
import com.bs.lk.newoamvptest.model.FileDepositedDataModel;
import com.bs.lk.newoamvptest.model.IFileDepositedDataModel;
import com.bs.lk.newoamvptest.view.activity.IFileDepositedDataView;

import java.util.ArrayList;

public class FileDepositedDataPresenter implements IFileDepositedDataPresenter {
    private IFileDepositedDataView fileDepositDataView;
    IFileDepositedDataModel fileDepositedDataModel;
    private  int row;//记录目前所查询的页码；

    ArrayList<DepositDataListsBean> waitassignList = new ArrayList<>();//用于储存待指派的数据

    private String userOaid;


    public FileDepositedDataPresenter(IFileDepositedDataView fileDepositDataView) {
        this.fileDepositDataView = fileDepositDataView;

    }

    @Override
    public void doFileDetailData(int i, String oaid) {
        row = i;
        userOaid = oaid;
        fileDepositedDataModel = new FileDepositedDataModel(this);
        fileDepositedDataModel.doParamsForDepositDetail(i,oaid);
    }

    @Override
    public void onFileDetailData(String data) {
        String ceshi = data;
        DepositDataListsRoot resps = JSON.parseObject(data,DepositDataListsRoot.class);
        //对数据进行处理,根据目前的文件状态进行判断如果是为指派状态进行显示，否则不显示
        for (int i = 0;i<resps.getData().size();i++){
            if (resps.getData().get(i).getFileState() != 1){
                waitassignList.add(resps.getData().get(i));
            }
        }
        if (waitassignList.size()<10 && resps.getData().size()>0 ){
            row = row+1;
            fileDepositedDataModel.doParamsForDepositDetail(row,userOaid);
        }else {
            fileDepositDataView.onDepositListDatas(waitassignList);
        }
    }
}
