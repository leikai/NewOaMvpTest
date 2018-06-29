package com.bs.lk.newoamvptest.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bs.lk.newoamvptest.R;
import com.bs.lk.newoamvptest.adapter.NewsAdapter;
import com.bs.lk.newoamvptest.bean.BannerBean;
import com.bs.lk.newoamvptest.bean.NewsBean;
import com.bs.lk.newoamvptest.bean.NewsParamsBean;
import com.bs.lk.newoamvptest.bean.ParameterBean;
import com.bs.lk.newoamvptest.presenter.IWeidoorPresenter;
import com.bs.lk.newoamvptest.presenter.WeidoorPresenter;
import com.bumptech.glide.Glide;
import com.cjj.MaterialRefreshLayout;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WeidoorActivity extends AppCompatActivity implements IWeiDoorView{

    @BindView(R.id.btn_news_back)
    Button btnNewsBack;
    @BindView(R.id.left_view)
    LinearLayout leftView;
    @BindView(R.id.news_right_imbt)
    ImageButton newsRightImbt;
    @BindView(R.id.tv_news_title)
    TextView tvNewsTitle;
    @BindView(R.id.rl_weidoor_title)
    RelativeLayout rlWeidoorTitle;
    @BindView(R.id.activity_weidoor_convenientBanner)
    ConvenientBanner activityWeidoorConvenientBanner;
    @BindView(R.id.lv_weidoor_news)
    ListView lvWeidoorNews;
    @BindView(R.id.weodoor_refresh)
    MaterialRefreshLayout weodoorRefresh;

    private IWeidoorPresenter weidoorPresenter;
    private NewsAdapter newsAdapter;
    private static String BASE_SERVER_URL_NEWS = "http://111.53.181.200:6688/mserver/upfile/webimg/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weidoor);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        ButterKnife.bind(this);
        weidoorPresenter = new WeidoorPresenter(this);
        initData();

    }

    private void initData() {
        NewsParamsBean user = new NewsParamsBean();

        user.setNums("5");
        user.setTypename("法院要闻");
        Gson gson = new Gson();
        String jsonObjString = gson.toJson(user);
        weidoorPresenter.doDataForPrams(jsonObjString);
        ParameterBean bean = new ParameterBean();
        bean.setNums("5");
        Gson gsonToIV = new Gson();
        String pp = gsonToIV.toJson(bean);
        weidoorPresenter.doImageForPrams(pp);
    }

    @OnClick(R.id.btn_news_back)
    public void onViewClicked() {
    }

    @Override
    public void onResultToP(List<NewsBean>  mNewsDatas) {
        newsAdapter = new NewsAdapter(getApplicationContext(),mNewsDatas);
        lvWeidoorNews.setAdapter(newsAdapter);
        setItemAdapter(mNewsDatas);
    }

    private void setItemAdapter(List<NewsBean>  mNewsDatas) {
        lvWeidoorNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent jumptoNews = new Intent(getApplicationContext(), NewsActivity.class);
                Bundle bundle = new Bundle();
                NewsBean newsData = (NewsBean)mNewsDatas.get(position);
                bundle.putString("oid",newsData.getOid() );
                Log.e("oid",""+bundle);
                jumptoNews.putExtras(bundle);
                startActivity(jumptoNews);
            }
        });
    }

    @Override
    public void onResultToPWithIV(List<BannerBean> mNewsDatas) {
        activityWeidoorConvenientBanner.setPages(new CBViewHolderCreator<LocalImageHolderView>() {
            @Override
            public LocalImageHolderView createHolder() {
                return new LocalImageHolderView();
            }
        }, mNewsDatas)
                .setPointViewVisible(true)
                .setPageIndicator(new int[]{R.drawable.circle_gray, R.drawable.circle_white})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                .startTurning(3000)
                .setManualPageable(true);
    }

    @Override
    public void onResultForError() {
        Toast.makeText(WeidoorActivity.this,"网络问题请重试！",Toast.LENGTH_SHORT).show();
    }

    public class LocalImageHolderView implements Holder<BannerBean> {

        private ImageView imageView;

        @Override
        public View createView(Context context) {
            View view = View.inflate(context, R.layout.view_imageview, null);
            imageView = (ImageView) view.findViewById(R.id.iv_view_imageview);
            return view;
        }

        @Override
        public void UpdateUI(final Context context, int position, final BannerBean bean) {
            Glide.with(context).load(BASE_SERVER_URL_NEWS+bean.getAttname()).into(imageView);
            weodoorRefresh.finishRefresh();
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
