package com.bs.lk.newoamvptest.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bs.lk.newoamvptest.CApplication;
import com.bs.lk.newoamvptest.R;
import com.bs.lk.newoamvptest.bean.UserNewBean;
import com.bs.lk.newoamvptest.widget.CustomProgress;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author lk
 */
public class ModifyPwdActivity extends Activity {

    @BindView(R.id.fg_webview_modify_pwd)
    WebView fgWebviewModifyPwd;
    @BindView(R.id.loading)
    CustomProgress loading;
    private UserNewBean userCur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_modify_pwd);
        ButterKnife.bind(this);
        fgWebviewModifyPwd.getSettings().setJavaScriptEnabled(true);
        fgWebviewModifyPwd.getSettings().setDomStorageEnabled(true);
        fgWebviewModifyPwd.setWebViewClient(new WebViewClient());
        fgWebviewModifyPwd.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                if (!message.equals("")){
                    finish();
                }
                result.confirm();
                return true;
            }
        });
        //设置缓存模式：不使用缓存
        fgWebviewModifyPwd.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
    }
    @Override
    protected void onResume() {
        super.onResume();
        userCur  = CApplication.getInstance().getCurrentUser();
        Log.e("取出来的token",""+userCur.getOid());
        fgWebviewModifyPwd.loadUrl("http://47.92.241.228:8899/bo_dev/bo/user/html/user_edit_mobile.html?oid="+userCur.getOid());
    }
}
