package com.bs.lk.newoamvptest.model;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.bs.lk.newoamvptest.CApplication;
import com.bs.lk.newoamvptest.Interface.IOAInterfaceV3Service;
import com.bs.lk.newoamvptest.bean.SessionGsonFormatBean;
import com.bs.lk.newoamvptest.bean.TokenRoot;
import com.bs.lk.newoamvptest.bean.UserNewBean;
import com.bs.lk.newoamvptest.bean.UserParamsBean;
import com.bs.lk.newoamvptest.presenter.ILoginPresenter;
import com.bs.lk.newoamvptest.util.NetUtils;
import com.bs.lk.newoamvptest.util.WebServiceUtil;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.bs.lk.newoamvptest.util.NetUtils.LOGIN_PATH;

/**
 * @author lk
 */
public class LoginModel extends ILoginModel {
    private ILoginPresenter loginPresenter;
    private String loginResult;
    private String userName;
    private String passWord;
    public  static String sessionId;
    /**
     * 定义时创建 service,并且实例化
     */
    private IOAInterfaceV3Service service=createService(IOAInterfaceV3Service.class);

    public LoginModel(ILoginPresenter loginPresenter) {
        this.loginPresenter = loginPresenter;
    }

    @Override
    public Observable<SessionGsonFormatBean> loadAndroidVersion(String url, int type) {
        Map<String, String> map = new HashMap<>();
        return service.getServiceAndroidversion(url,map);
    }

    @Override
    public void doLoginData(String name, String password) {
        userName = name;
        passWord = password;
        UserNewBean user = new UserNewBean();
        String token = null;
        user.setUserName(name);
        user.setUserPassword(password);



        //创建观察者
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                Log.e("我接收到数据",""+s);
                Log.e("观察者所在的线程：",Thread.currentThread().getName());
                Log.e("resultStr",""+s);
                    loginPresenter.onLoginModelResult(s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };


        //创建被观察者
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                //执行一些其他操作（耗时操作）
                //.............
                //执行完毕，触发回调，通知观察者（可以设置观察者的线程）
                String resultStr = WebServiceUtil.getInstance().createSession(user);
                UserParamsBean userParamsBean = new UserParamsBean();
                userParamsBean.setUserName(userName+","+"fh"+","+passWord+","+"fh"+","+"0");

                Gson gson = new Gson();
                String jsonObjString = gson.toJson(userParamsBean);
                try {
                    String v3Session = NetUtils.requestByPost(jsonObjString,LOGIN_PATH);
                    Log.e("v3Session",v3Session);
                    SessionGsonFormatBean  sessionGsonFormatBean = gson.fromJson(v3Session,SessionGsonFormatBean.class);
                    sessionId = sessionGsonFormatBean.getSessionid();
                    Log.e("ceshi",sessionId);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                Log.e("resultStr",""+resultStr);
                String token = null;
                if (!TextUtils.isEmpty(resultStr)) {
                    TokenRoot resps = JSON.parseObject(resultStr,TokenRoot.class);
                    if (resps!=null){
                        if (resps.getOpresult()){
                            Log.e("resps",""+resps.getMsginfo());
                            token = resps.getMsginfo().substring(10);
                            token = token.substring(0,token.length()-2);
                            Log.e("获取到的Token：",""+token);

                            if (resps.getOpresult()&&resps.getMsg().equals("登录成功!")){
                                UserNewBean userInfo = WebServiceUtil.getInstance().getUserInfo(user.getUserName(),null);
                                user.copyUserInfo(userInfo);

                                CApplication.getInstance().setUser(user);
                                CApplication.getInstance().setToken(token);
                                loginResult = resps.getMsg();

                            }
                        }else {
                            loginResult = resps.getMsg()+resps.getMsginfo();
                        }
                        emitter.onNext(loginResult );
                    }else {
                        Log.e("resps","登录失败！"+resps);
                        emitter.onNext(loginResult );
                    }
                }else{
                    Log.e("resultStr",""+loginResult);
                    emitter.onNext(loginResult );
                }
                Log.e("被观察者所在的线程：",Thread.currentThread().getName());
            }
        })
                // 指定 subscribe() 发生在 IO 线程
                .subscribeOn(Schedulers.io())
                // 指定 Subscriber 的回调发生在主线程
                .observeOn(AndroidSchedulers.mainThread());


        //订阅
        observable.subscribe(observer);

    }
}
