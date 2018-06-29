package com.bs.lk.newoamvptest.presenter;

import com.bs.lk.newoamvptest.model.CabinetModel;
import com.bs.lk.newoamvptest.model.ICabinetModel;
import com.bs.lk.newoamvptest.view.activity.ICabinetView;

public class CabinetPresenter implements ICabinetPresenter {
    private ICabinetView cabinetView;

    ICabinetModel cabinetModel;

    public CabinetPresenter(ICabinetView cabinetView) {
        this.cabinetView = cabinetView;
    }

    @Override
    public void doDeposit() {
        cabinetModel = new CabinetModel(this);
        cabinetModel.doCabinetData();
    }

    @Override
    public void onQRCodeData(String mQRCodeData) {
        cabinetView.onQRCodePData(mQRCodeData);
    }
}
