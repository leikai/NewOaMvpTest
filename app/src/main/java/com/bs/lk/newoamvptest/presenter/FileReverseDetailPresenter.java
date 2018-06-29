package com.bs.lk.newoamvptest.presenter;

import com.alibaba.fastjson.JSON;
import com.bs.lk.newoamvptest.bean.DepositDataListsBean;
import com.bs.lk.newoamvptest.bean.DepositDataListsRoot;
import com.bs.lk.newoamvptest.model.FileDepositDetailModel;
import com.bs.lk.newoamvptest.model.FileReverseDetailModel;
import com.bs.lk.newoamvptest.model.IFileReverseDetailModel;
import com.bs.lk.newoamvptest.view.activity.IFileReverseDetailView;

import java.util.ArrayList;

public class FileReverseDetailPresenter implements IFileReverseDetailPresenter {
    private IFileReverseDetailView fileReverseDetailView;
    IFileReverseDetailModel fileReverseDetailModel;

    private  int row;//记录目前所查询的页码；

    ArrayList<DepositDataListsBean> waitassignList = new ArrayList<>();//用于储存待指派的数据

    private String userOaid;

    public FileReverseDetailPresenter(IFileReverseDetailView fileReverseDetailView) {
        this.fileReverseDetailView = fileReverseDetailView;
    }

    @Override
    public void doFileDetailData(int i, String oaid) {
        row = i;
        userOaid = oaid;
        fileReverseDetailModel = new FileReverseDetailModel(this);
        fileReverseDetailModel.doParamsForDepositDetail(i,oaid);
    }

    @Override
    public void onFileDetailData(String data) {
        if (data.equals("0")){
            fileReverseDetailView.onReverseErrorDatas(data);
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
                fileReverseDetailModel.doParamsForDepositDetail(row,userOaid);
            }else {
                fileReverseDetailView.onFileReverseDatas(waitassignList);
            }
        }

    }
}
