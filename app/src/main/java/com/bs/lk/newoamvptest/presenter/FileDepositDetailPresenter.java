package com.bs.lk.newoamvptest.presenter;

import com.alibaba.fastjson.JSON;
import com.bs.lk.newoamvptest.bean.DepositDataListsBean;
import com.bs.lk.newoamvptest.bean.DepositDataListsRoot;
import com.bs.lk.newoamvptest.model.FileDepositDetailModel;
import com.bs.lk.newoamvptest.model.IFileDepositDetailModel;
import com.bs.lk.newoamvptest.view.activity.IFileDepositDetailView;

import java.util.ArrayList;

public class FileDepositDetailPresenter implements IFileDepositDetailPresenter {
    private IFileDepositDetailView fileDepositDetailView;

    IFileDepositDetailModel fileDepositDetailModel;

    private  int row;//记录目前所查询的页码；

    ArrayList<DepositDataListsBean> waitassignList = new ArrayList<>();//用于储存待指派的数据

    private String userOaid;

    public FileDepositDetailPresenter(IFileDepositDetailView fileDepositDetailView) {
        this.fileDepositDetailView = fileDepositDetailView;
    }

    @Override
    public void doFileDetailData(int i, String oaid) {
        row = i;
        userOaid = oaid;
        fileDepositDetailModel = new FileDepositDetailModel(this);
        fileDepositDetailModel.doParamsForDepositDetail(i,oaid);
    }

    @Override
    public void onFileDetailData(String data) {

        if (data.equals("0")){
            fileDepositDetailView.onDepositErrorDatas(data);
        }else {
            DepositDataListsRoot resps = JSON.parseObject(data,DepositDataListsRoot.class);
            //对数据进行处理,根据目前的文件状态进行判断如果是为指派状态进行显示，否则不显示
            for (int i = 0;i<resps.getData().size();i++){
                if (resps.getData().get(i).getFileState() == 1){
                    waitassignList.add(resps.getData().get(i));
                }
            }
            if (waitassignList.size()<10 && resps.getData().size()>0 ){
                row = row+1;
                fileDepositDetailModel.doParamsForDepositDetail(row,userOaid);
            }else {
                fileDepositDetailView.onDepositDatas(waitassignList);
            }
        }

    }
}
