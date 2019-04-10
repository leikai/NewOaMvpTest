package com.bs.lk.newoamvptest.model;

import android.util.Log;

import com.bs.lk.newoamvptest.presenter.IHomePageNewsPresenter;
import com.bs.lk.newoamvptest.util.SoapClient;
import com.bs.lk.newoamvptest.util.SoapParams;
import com.bs.lk.newoamvptest.util.WebServiceUtil;

import org.ksoap2.serialization.SoapSerializationEnvelope;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author lk
 */
public class HomePageNewsModel implements IHomePageNewsModel {
    private IHomePageNewsPresenter homePageNewsPresenter;

    public HomePageNewsModel(IHomePageNewsPresenter homePageNewsPresenter) {
        this.homePageNewsPresenter = homePageNewsPresenter;
    }


    @Override
    public void doParamsForWeiPWithText(String json) {

    }

    @Override
    public void doParamsForWeiPWithImage(String json) {

    }
}
