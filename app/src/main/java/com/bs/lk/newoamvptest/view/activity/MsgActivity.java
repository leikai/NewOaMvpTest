package com.bs.lk.newoamvptest.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bs.lk.newoamvptest.CApplication;
import com.bs.lk.newoamvptest.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MsgActivity extends AppCompatActivity {

    @BindView(R.id.fg_webview_msg)
    WebView fgWebviewMsg;
    private String ceshi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);
        ButterKnife.bind(this);
        ceshi  = CApplication.getInstance().getCurrentToken();
        fgWebviewMsg.getSettings().setJavaScriptEnabled(true);
        fgWebviewMsg.getSettings().setDomStorageEnabled(true);
        fgWebviewMsg.setWebViewClient(new WebViewClient());
        Log.e("取出来的token",""+ceshi);
        fgWebviewMsg.loadUrl("http://192.168.3.61:8080/mcourtoa/moffice/sign/list_daiban.html?token="+ceshi);
    }
    /**
     * 拦截手机返回键的处理
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            return  true;
        }
        return  super.onKeyDown(keyCode, event);

    }

}
