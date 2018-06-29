package com.bs.lk.newoamvptest.model;

import android.util.Log;

import com.bs.lk.newoamvptest.presenter.INewsPresenter;
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

public class NewsModel implements INewsModel {
    private INewsPresenter newsPresenter;

    public NewsModel(INewsPresenter newsPresenter) {
        this.newsPresenter = newsPresenter;
    }

    @Override
    public void doParamsForWeiPWithText(String json) {
        //创建观察者
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                Log.e("获取新闻详情模块的Json数据：",""+s);
                newsPresenter.onResultToM(s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                //执行一些其他操作（耗时操作）
                //.............
                //执行完毕，触发回调，通知观察者（可以设置观察者的线程）
                SoapParams soapParams = new SoapParams().put("jsonstr",json);
                Log.e("jsonstr = ",""+json);
                WebServiceUtil.getInstance().getWebInfos(soapParams, new SoapClient.ISoapUtilCallback() {
                    @Override
                    public void onSuccess(SoapSerializationEnvelope envelope) throws Exception {
                        String resp = envelope.getResponse().toString();
                        emitter.onNext(resp);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        String code = "0";//用“0”代表获取后台数据错误
                        emitter.onNext(code);
                    }
                });



            }
        })
                .subscribeOn(Schedulers.io())// 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread());// 指定 Subscriber 的回调发生在主线程

        //订阅
        observable.subscribe(observer);
    }

    @Override
    public void doParamsForWeiPWithImage(String json) {

    }
}
