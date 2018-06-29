package com.bs.lk.newoamvptest.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.lk.newoamvptest.CApplication;
import com.bs.lk.newoamvptest.R;
import com.bs.lk.newoamvptest.adapter.SwipeMenuAdapter;
import com.bs.lk.newoamvptest.bean.DepositDataListsBean;
import com.bs.lk.newoamvptest.bean.UserNewBean;
import com.bs.lk.newoamvptest.presenter.FileDepositedDataPresenter;
import com.bs.lk.newoamvptest.presenter.IFileDepositedDataPresenter;
import com.bs.lk.newoamvptest.widget.SampleHeader;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FileDepositedDataActivity extends AppCompatActivity implements IFileDepositedDataView {

    @BindView(R.id.btn_file_deposit_detail_back)
    Button btnFileDepositDetailBack;
    @BindView(R.id.left_view)
    LinearLayout leftView;
    @BindView(R.id.right_imbt)
    ImageButton rightImbt;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.rl_deposit_title)
    RelativeLayout rlDepositTitle;
    @BindView(R.id.tv_nodata)
    TextView tvNodata;
    @BindView(R.id.list)
    LRecyclerView list;


    private static final String TAG = "lzx";

    /**服务器端一共多少条数据*/
    private static final int TOTAL_COUNTER = 11;//如果服务器没有返回总数据或者总页数，这里设置为最大值比如10000，什么时候没有数据了根据接口返回判断

    /**每一页展示多少条数据*/
    private static final int REQUEST_COUNT = 10;

    public int index = 1;//目前所在的页数


    /**已经获取到多少条数据了*/
    private static int mCurrentCounter = 0;
    private boolean isRefresh = false;

    ArrayList<DepositDataListsBean> dataList = new ArrayList<>();
    ArrayList<DepositDataListsBean> newList = new ArrayList<>();
    ArrayList<DepositDataListsBean> waitassignList = new ArrayList<>();

    private SwipeMenuAdapter mDataAdapter = null;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;

    IFileDepositedDataPresenter fileDepositedDataPresenter;

    private WeakReference<FileDepositedDataActivity> ref;

    private FileDepositedDataActivity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_file_deposit_data);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        ButterKnife.bind(this);
        fileDepositedDataPresenter = new FileDepositedDataPresenter(this); //绑定P层
        dataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DepositDataListsBean depositDataBean = new DepositDataListsBean();
            depositDataBean.setInitiatorName("");
            depositDataBean.setAddress("");
            dataList.add(depositDataBean);
        }

        ref = new WeakReference<>(this);
        activity = ref.get();
        setAdapter();//设置适配器
    }

        private void setAdapter() {
            mDataAdapter = new SwipeMenuAdapter(getApplicationContext());
            mDataAdapter.setDataList(dataList);
            mDataAdapter.setOnDelListener(new SwipeMenuAdapter.onSwipeListener() {
                @Override
                public void onDel(int pos) {
                    Toast.makeText(FileDepositedDataActivity.this, "删除:" + pos, Toast.LENGTH_SHORT).show();
                    mDataAdapter.getDataList().remove(pos);
                    mDataAdapter.notifyItemRemoved(pos);//推荐用这个

                    if(pos != (mDataAdapter.getDataList().size())){ // 如果移除的是最后一个，忽略 注意：这里的mDataAdapter.getDataList()不需要-1，因为上面已经-1了
                        mDataAdapter.notifyItemRangeChanged(pos, mDataAdapter.getDataList().size() - pos);
                    }
                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
                }

            @Override
            public void onTop(int pos) {//置顶功能有bug，后续解决
                DepositDataListsBean itemModel = mDataAdapter.getDataList().get(pos);

                mDataAdapter.getDataList().remove(pos);
                mDataAdapter.notifyItemRemoved(pos);
                mDataAdapter.getDataList().add(0, itemModel);
                mDataAdapter.notifyItemInserted(0);


                if(pos != (mDataAdapter.getDataList().size())){ // 如果移除的是最后一个，忽略
                    mDataAdapter.notifyItemRangeChanged(0, mDataAdapter.getDataList().size() - 1,"jdsjlzx");
                }

                list.scrollToPosition(0);

            }
        });
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        list.setAdapter(mLRecyclerViewAdapter);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        list.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);
        list.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);

        final View header = LayoutInflater.from(this).inflate(R.layout.sample_header,(ViewGroup)findViewById(android.R.id.content), false);
        mLRecyclerViewAdapter.addHeaderView(new SampleHeader(this));
        list.setPullRefreshEnabled(true);//设置不能下拉刷新
        list.setOnRefreshListener(new OnRefreshListener() {//下拉刷新的操作
            @Override
            public void onRefresh() {
                mDataAdapter.clear();
                tvNodata.setVisibility(View.GONE);
                mLRecyclerViewAdapter.notifyDataSetChanged();//fix bug:crapped or attached views may not be recycled. isScrap:false isAttached:true
                mCurrentCounter = 0;
                isRefresh = true;
                UserNewBean user = CApplication.getInstance().getCurrentUser();
                fileDepositedDataPresenter.doFileDetailData(index,user.getOaid());
            }
        });
        //是否禁用自动加载更多功能,false为禁用, 默认开启自动加载更多功能
        list.setLoadMoreEnabled(true);
        list.setOnLoadMoreListener(new OnLoadMoreListener() {//自动加载操作
            @Override
            public void onLoadMore() {

                if (newList.size() !=10) {//每页的数据设置的为10条,如果不是10条说明是最后一页
                    //the end
                    list.setNoMore(true);

                } else {
                    // loading more
                    UserNewBean user = CApplication.getInstance().getCurrentUser();
                    fileDepositedDataPresenter.doFileDetailData(index,user.getOaid());
                }
            }
        });

        list.setLScrollListener(new LRecyclerView.LScrollListener() {

            @Override
            public void onScrollUp() {
            }

            @Override
            public void onScrollDown() {
            }


            @Override
            public void onScrolled(int distanceX, int distanceY) {
            }

            @Override
            public void onScrollStateChanged(int state) {

            }

        });

        //设置头部加载颜色
        list.setHeaderViewColor(R.color.colorAccent, R.color.dark ,android.R.color.white);
        //设置底部加载颜色
        list.setFooterViewColor(R.color.colorAccent, R.color.dark ,android.R.color.white);
        //设置底部加载文字提示
        list.setFooterViewHint("拼命加载中","已经全部为你呈现了","网络不给力啊，点击再试一次吧");
        list.refresh();


        btnFileDepositDetailBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onDepositListDatas(ArrayList<DepositDataListsBean> depositDataListsBeans) {
        if (depositDataListsBeans.size() != 0) {
            tvNodata.setVisibility(View.GONE);
        }else{
            tvNodata.setVisibility(View.VISIBLE);
        }
        activity.addItems(depositDataListsBeans);
        activity.list.refreshComplete(REQUEST_COUNT);
        isRefresh = false;
    }

    private void addItems(ArrayList<DepositDataListsBean> list) {
        mDataAdapter.addAll(list);
        mCurrentCounter += list.size();

    }
}
