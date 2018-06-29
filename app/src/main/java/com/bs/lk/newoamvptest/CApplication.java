package com.bs.lk.newoamvptest;

import android.app.Application;

//import org.sxchinacourt.bean.UserBean;
//import org.sxchinacourt.cache.CSharedPreferences;
//import org.sxchinacourt.util.WebServiceUtil;
//import org.sxchinacourt.util.network.SSLRequest;

import com.bs.lk.newoamvptest.bean.UserNewBean;
import com.bs.lk.newoamvptest.cache.CSharedPreferences;
import com.bs.lk.newoamvptest.util.WebServiceUtil;
import com.bs.lk.newoamvptest.util.network.SSLRequest;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

//import cn.jpush.android.api.JPushInterface;


/**
 * Created by baggio on 2017/2/6.
 */

public class CApplication extends Application {
    public static CApplication mInstance;
    private UserNewBean mCurrentUser;
    private String mToken;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        try {
            SSLRequest.allowAllSSL(WebServiceUtil.BASE_SERVER_URL,
                    getBaseContext());//证书验证
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        mCurrentUser = CSharedPreferences.getInstance().getCurrentUser();
        mToken = CSharedPreferences.getInstance().getToken();
    }

    public static CApplication getInstance() {
        return mInstance;
    }

    public UserNewBean getCurrentUser() {
        return mCurrentUser;
    }

    public void setUser(UserNewBean user) {
        if (user != null) {
            mCurrentUser = user;
            CSharedPreferences.getInstance().setCurrentUser(user);
        }
    }

    public String getCurrentToken() {
        return mToken;
    }
    public void setToken(String token) {
        if (token != null) {
            mToken = token;
            CSharedPreferences.getInstance().setToken(token);
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        mCurrentUser = null;
        mInstance = null;
    }

}