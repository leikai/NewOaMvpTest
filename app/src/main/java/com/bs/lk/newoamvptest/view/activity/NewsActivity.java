package com.bs.lk.newoamvptest.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bs.lk.newoamvptest.R;
import com.bs.lk.newoamvptest.bean.BannerBean;
import com.bs.lk.newoamvptest.bean.NewsBean;
import com.bs.lk.newoamvptest.bean.NewsContentPattsRoot;
import com.bs.lk.newoamvptest.bean.WebInfosBean;
import com.bs.lk.newoamvptest.presenter.INewsPresenter;
import com.bs.lk.newoamvptest.presenter.NewsPresenter;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewsActivity extends AppCompatActivity implements INewsView{

    @BindView(R.id.btn_news_back)
    Button btnNewsBack;
    @BindView(R.id.left_view)
    LinearLayout leftView;
    @BindView(R.id.news_right_imbt)
    ImageButton newsRightImbt;
    @BindView(R.id.tv_news_title)
    TextView tvNewsTitle;
    @BindView(R.id.rl_news_title)
    RelativeLayout rlNewsTitle;
    @BindView(R.id.news_tv_Title)
    TextView newsTvTitle;
    @BindView(R.id.news_tv_time)
    TextView newsTvTime;
    @BindView(R.id.news_web_content)
    WebView newsWebContent;

    private INewsPresenter newsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        ButterKnife.bind(this);
        newsPresenter = new NewsPresenter(this);
        String oid = getIntentData();
        initData(oid);
    }

    private void initData(String oid) {
        WebInfosBean webInfosBean = new WebInfosBean();
        webInfosBean.setOid(oid);
        Gson gson1 = new Gson();
        String jsonObjString1 = gson1.toJson(webInfosBean);
        newsPresenter.doDataForPrams(jsonObjString1);
    }

    @OnClick(R.id.btn_news_back)
    public void onViewClicked() {
    }

    private String getIntentData() {
        Intent intentGetData = getIntent();
        Bundle bundle = intentGetData.getExtras();
        String oid = bundle.getString("oid");
        Log.e("oid",""+oid);
        return oid;
    }

    @Override
    public void onResultToP(List<NewsContentPattsRoot> newsDetails ) {
        NewsContentPattsRoot newsContentPattsRoot = (NewsContentPattsRoot)newsDetails.get(0);
        newsTvTitle.setText(newsContentPattsRoot.getFtitle());
        newsTvTime.setText(newsContentPattsRoot.getReleasetime());
        newsWebContent  .loadDataWithBaseURL("about:blank",newsContentPattsRoot.getContent(),"text/html","utf-8",null);

    }

    @Override
    public void onResultToPWithIV(List<BannerBean> mNewsDatas) {

    }

    @Override
    public void onResultForError() {

    }
}
