package com.bs.lk.newoamvptest.model;

import android.util.Log;

import com.bs.lk.newoamvptest.CApplication;
import com.bs.lk.newoamvptest.bean.UserNewBean;
import com.bs.lk.newoamvptest.presenter.IAttendanceListPresenter;
import com.bs.lk.newoamvptest.presenter.IAttendanceStatisticsPresenter;
import com.bs.lk.newoamvptest.util.WebServiceUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AttendanceStatisticsModel implements IAttendanceStatisticsModel{
    private IAttendanceStatisticsPresenter attendanceStatisticsPresenter;

    public AttendanceStatisticsModel(IAttendanceStatisticsPresenter attendanceStatisticsPresenter) {
        this.attendanceStatisticsPresenter = attendanceStatisticsPresenter;
    }

    @Override
    public void doParamsForAttendancePWithText(String time, String userOid) {
        //创建观察者
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                attendanceStatisticsPresenter.onAttendanceResultToM(s);
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
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                //获取当前时间
                Date date = new Date(System.currentTimeMillis());
                String curDate = simpleDateFormat.format(date);
                UserNewBean user = CApplication.getInstance().getCurrentUser();
                String ceshi1 =  WebServiceUtil.getInstance().getOrgSign(null,userOid);
                Log.e("ceshi1 = ",""+ceshi1);
                if (ceshi1==null){
                    Log.e("ceshi1 = ","考勤反馈结果错误");
                }else {
//                    emitter.onNext(ceshi1);
                }
                String shiftsJson = WebServiceUtil.getInstance().getTheShift(user.getOrgid());
                Log.e("shiftsJson = ",""+shiftsJson);
                if (shiftsJson==null){
                    Log.e("shiftsJson = ","考勤班次结果错误");
                }else {
                    emitter.onNext("shiftsJson ="+shiftsJson+"&"+"ceshi1 ="+ceshi1);
                }

            }
        })
                .subscribeOn(Schedulers.io())// 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread());// 指定 Subscriber 的回调发生在主线程

        //订阅
        observable.subscribe(observer);
    }
}
