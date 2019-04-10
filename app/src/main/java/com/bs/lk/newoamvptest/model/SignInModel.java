package com.bs.lk.newoamvptest.model;

import android.util.Log;

import com.bs.lk.newoamvptest.presenter.ISignInPresenter;
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
public class SignInModel implements ISignInModel {
    private ISignInPresenter signInPresenter;

    public SignInModel(ISignInPresenter signInPresenter) {
        this.signInPresenter = signInPresenter;
    }

    @Override
    public void doParamsForWeiPWithText(String time, String addresInfo, String state, String userOid,String orgId) {
        //创建观察者
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                signInPresenter.onResultToM(s);
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


                String ceshi =  WebServiceUtil.getInstance().updateSign(time,addresInfo,state,null,null,null,
                        null,null,null,
                        null,null,null,
                        userOid,orgId);
                Log.e("ceshi = ",""+ceshi);
                if (ceshi==null){
                    Log.e("ceshi = ","考勤反馈结果错误");
                }else {
                    emitter.onNext(ceshi);
                }


//                SoapParams soapParams = new SoapParams().put("ssignintimte",time).put("ambdkwz",addresInfo).put("ambstate",state).put("oid",userOid);
//                Log.e("jsonstr = ",""+soapParams);
//                WebServiceUtil.getInstance().updateSign(soapParams, new SoapClient.ISoapUtilCallback() {
//                    @Override
//                    public void onSuccess(SoapSerializationEnvelope envelope) throws Exception {
//                        String resp = envelope.getResponse().toString();
//                        emitter.onNext(resp);
//                    }
//
//                    @Override
//                    public void onFailure(Exception e) {
//                        //用“0”代表获取后台数据错误
//                        String code = "0";
//                        emitter.onNext(code);
//                    }
//                });



            }
        })
                .subscribeOn(Schedulers.io())// 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread());// 指定 Subscriber 的回调发生在主线程

        //订阅
        observable.subscribe(observer);
    }
}
