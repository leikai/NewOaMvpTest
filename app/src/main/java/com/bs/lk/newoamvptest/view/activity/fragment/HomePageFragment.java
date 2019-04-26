package com.bs.lk.newoamvptest.view.activity.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bs.lk.newoamvptest.CApplication;
import com.bs.lk.newoamvptest.R;
import com.bs.lk.newoamvptest.adapter.HomePageAdapter;
import com.bs.lk.newoamvptest.adapter.NotificationAdapter;
import com.bs.lk.newoamvptest.bean.AppBean;
import com.bs.lk.newoamvptest.bean.BannerBean;
import com.bs.lk.newoamvptest.bean.MessageEvent;
import com.bs.lk.newoamvptest.bean.NewsBean;
import com.bs.lk.newoamvptest.bean.NotificationAnnouncementBean;
import com.bs.lk.newoamvptest.bean.ToDoDataRootBean;
import com.bs.lk.newoamvptest.bean.ToDoDatainfoBean;
import com.bs.lk.newoamvptest.bean.UserNewBean;
import com.bs.lk.newoamvptest.presenter.HomePageNewsPresenter;
import com.bs.lk.newoamvptest.presenter.IHomePageNewsPresenter;
import com.bs.lk.newoamvptest.util.WebServiceUtil;
import com.bs.lk.newoamvptest.view.activity.AttendanceListActivity;
import com.bs.lk.newoamvptest.view.activity.DailyAndWeeklyActivity;
import com.bs.lk.newoamvptest.view.activity.LeaveInformal1Activity;
import com.bs.lk.newoamvptest.view.activity.LoginActivity;
import com.bumptech.glide.Glide;
import com.geek.thread.GeekThreadManager;
import com.geek.thread.ThreadPriority;
import com.geek.thread.ThreadType;
import com.geek.thread.task.GeekRunnable;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 首页碎片
 * @author lk
 */
public class HomePageFragment extends BaseFragment implements IHomePageNewsView {
    private ConvenientBanner convenientBanner;
    private ArrayList<Integer> localImages = new ArrayList<Integer>();
    private RecyclerView rvHomepageWeidoorRecycleView;
    private LinearLayout cabinet;
    private LinearLayout machine;
    private LinearLayout leave;
    private LinearLayout leaveInformal;
    GridView mAppsGridView;
    HomePageAdapter mAdapter;
    int[] mResourceIdArray = {R.drawable.icon_dbsy, R.drawable.icon_ybsy, R.drawable
            .icon_wdlc, R.drawable.icon_cjlc};
    String[] mAppNameArray = {"待办事宜", "已办事宜", "通知公告", "任务管理"};
    int[] mFragmentIndexArray = {AppBean.FRAGMENT_TO_DO_TASK, AppBean.FRAGMENT_HISTORY_TASK, AppBean
            .FRAGMENT_NOTICE_TASK, AppBean.FRAGMENT_MANAGER_TASK};

    private List<NewsBean> fruitList = new ArrayList<>();
    private Context context;
    private IHomePageNewsPresenter homePageNewsPresenter;

    /**
     * 晋中中院轮播图地址
     */
    public static String BASE_SERVER_URL_NEWS_IMG_BANNER = "http://111.53.181.200:6688/mserver/upfile/webimg/";

    private List<ToDoDatainfoBean> toDoTasks = new ArrayList<>();
    /**
     * 获取网络返回的待办数量
     */
    private String taskListJson;
    private String token;
    private UserNewBean user;
    private ArrayList<AppBean> apps;
    private List<NotificationAnnouncementBean> toDoDatainfoListProtogenesis;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        token = CApplication.getInstance().getCurrentToken();
        user = CApplication.getInstance().getCurrentUser();
        mTitle = "百斯奥格移动办公平台";
        mLogo = View.GONE;
        mShowBtnBack = View.INVISIBLE;
        context = getContext();
        //注册
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return onCreateView(inflater, R.layout.fragment_home_page, container, savedInstanceState);
    }

    @Nullable
    @Override
    public void initFragment(@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rvHomepageWeidoorRecycleView = (RecyclerView) mRootView.findViewById(R.id.home_page_view_wei_door);
        convenientBanner = (ConvenientBanner) mRootView.findViewById(R.id.fragment_home_convenientBanner);
        cabinet = (LinearLayout) mRootView.findViewById(R.id.view_homepage_ll_cabinet);
        machine = (LinearLayout) mRootView.findViewById(R.id.view_homepage_ll_machine);
        leave = (LinearLayout) mRootView.findViewById(R.id.view_homepage_ll_leave);
        leaveInformal = (LinearLayout) mRootView.findViewById(R.id.view_homepage_ll_tzgg);
        mAppsGridView = (GridView) mRootView.findViewById(R.id.apps_gridview);

        apps = new ArrayList<>();
        for (int i = 0; i < mResourceIdArray.length; i++) {
            AppBean app = new AppBean();
            app.setResourceId(mResourceIdArray[i]);
            app.setName(mAppNameArray[i]);
            app.setFragmentIndex(mFragmentIndexArray[i]);
            apps.add(app);
        }
        //获取本地图片
        getLocalImage();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setReverseLayout(false);
        rvHomepageWeidoorRecycleView.setLayoutManager(layoutManager);
        rvHomepageWeidoorRecycleView.setVisibility(View.VISIBLE);

        //开始自动翻页
        convenientBanner.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new LocalImageHolderView();
            }
        },localImages)
                //设置指示器是否可见
                .setPointViewVisible(true)
                //设置自动切换（同时设置了切换时间间隔）
                .startTurning(2000)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.drawable.circle_gray, R.drawable.circle_white})
                //设置指示器的方向（左、中、右）
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                //设置点击监听事件
//                .setOnItemClickListener(this)
                //设置手动影响（设置了该项无法手动切换）
                .setManualPageable(true);


        //设置翻页的效果，不需要翻页效果可用不设
        //setPageTransformer(Transformer.DefaultTransformer);   // 集成特效之后会有白屏现象，新版已经分离，如果要集成特效的例子可以看Demo的点击响应。

        initEvent();


    }

    @Override
    public void onResume() {
        super.onResume();
        if (token == null){
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }else {
            initTodoTaskCount();
            mAdapter = new HomePageAdapter(getContext(),
                    this, apps,toDoTasks);
            mAppsGridView.setAdapter(mAdapter);
            initNewsData();
        }
    }

    /**
     * 获取远程数据库数据
     */
    private void initTodoTaskCount(){
        GeekThreadManager.getInstance().execute(new GeekRunnable(ThreadPriority.NORMAL) {
            @Override
            public void run() {
                initTaskCount();
                String resultStr = WebServiceUtil.getInstance().getTzggAll(user.getOid());
                Log.e("resultStr",""+resultStr);
                if (resultStr!=null){
                    toDoDatainfoListProtogenesis = new ArrayList<NotificationAnnouncementBean>(JSONArray.parseArray(resultStr,NotificationAnnouncementBean.class));
                }
                final List<NotificationAnnouncementBean> notificationAnnouncementBeanList = new ArrayList<>();
                if(toDoDatainfoListProtogenesis == null){
                    NotificationAnnouncementBean notificationAnnouncementBean = new NotificationAnnouncementBean();
                    notificationAnnouncementBean.setTitle("暂无通知信息");
                    notificationAnnouncementBeanList.add(notificationAnnouncementBean);
                }else {
                    if (toDoDatainfoListProtogenesis.size()<5){
                        for (int i=0; i<toDoDatainfoListProtogenesis.size();i++){
                            notificationAnnouncementBeanList.add(toDoDatainfoListProtogenesis.get(i));
                        }
                    }else {
                        for (int i=0; i<5;i++){
                            notificationAnnouncementBeanList.add(toDoDatainfoListProtogenesis.get(i));
                        }
                    }
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        NotificationAdapter newsListAdapter = new NotificationAdapter(getContext(),notificationAnnouncementBeanList);
                        rvHomepageWeidoorRecycleView.setAdapter(newsListAdapter);
                    }
                });
            }
        }, ThreadType.NORMAL_THREAD);
    }

    private void initTaskCount() {
        for (int i = 0;i<3;i++){
            taskListJson  = WebServiceUtil.getInstance().getTaskList(token);
            if (!taskListJson.equals("error")){
                break;
            }
        }
        Log.e("taskListJson",""+taskListJson);
        if (!"error".equals(taskListJson)){
            ToDoDataRootBean toDoDataRoot = JSONObject.parseObject(taskListJson, ToDoDataRootBean.class);
            Log.e("toDoDataRoot",""+toDoDataRoot.getMsginfo());

            List<ToDoDatainfoBean> toDoDatainfoList = new ArrayList<>();
            //原生刚从输出库查出的待办数量（包括处于第一个节点）
            if (toDoDataRoot.getOpresult()){
                List<ToDoDatainfoBean> toDoDatainfoListProtogenesis = new ArrayList<ToDoDatainfoBean>(JSONArray.parseArray(toDoDataRoot.getMsginfo(),ToDoDatainfoBean.class));
                for (int i=0;i<toDoDatainfoListProtogenesis.size();i++){
                    if (1 != toDoDatainfoListProtogenesis.get(i).getFlowstepno()){
                        toDoDatainfoList.add(toDoDatainfoListProtogenesis.get(i));
                    }
                }
            }
//        List<ToDoDatainfoBean> toDoDatainfoList = new ArrayList<ToDoDatainfoBean>(JSONArray.parseArray(toDoDataRoot.getMsginfo(),ToDoDatainfoBean.class));
            if (toDoDatainfoList.size()!=0){
                Log.e("toDoDataRoot",""+toDoDatainfoList.get(0).getActorname());
            }
            //本地数据库中的待办数据
            List<ToDoDatainfoBean> toDoDatainfoListLocation = LitePal.findAll(ToDoDatainfoBean.class);
            boolean ceshi = isListEqual(toDoDatainfoList,toDoDatainfoListLocation);
            if (!isListEqual(toDoDatainfoList,toDoDatainfoListLocation)){
                LitePal.deleteAll(ToDoDatainfoBean.class);
                LitePal.saveAll(toDoDatainfoList);
                EventBus.getDefault().post(new MessageEvent("本地数据更新完毕",String.valueOf(toDoDatainfoList.size())));
            }
            EventBus.getDefault().post(new MessageEvent("本地数据更新完毕",String.valueOf(toDoDatainfoList.size())));

        }


    }

    /**
     * 接收服务传递过来的待办数据
     * @param messageEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        List<ToDoDatainfoBean> ceshi = LitePal.findAll(ToDoDatainfoBean.class);
        toDoTasks.clear();
        toDoTasks.addAll(ceshi);
        mAdapter.notifyDataSetChanged();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        //注销订阅
        EventBus.getDefault().unregister(this);
    }

    private void initNewsData() {
        homePageNewsPresenter = new HomePageNewsPresenter(HomePageFragment.this);
    }

    private void initEvent() {
        cabinet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent jumptoCabinet = new Intent(getContext(), DailyAndWeeklyActivity.class);
                startActivity(jumptoCabinet);
            }
        });
        machine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AttendanceListActivity.class));
            }
        });
        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), DailyAndWeeklyActivity.class));
                Toast.makeText(getContext(),"跳正式员工请假模块！",Toast.LENGTH_SHORT).show();
            }
        });
        leaveInformal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),LeaveInformal1Activity.class));
                Toast.makeText(getContext(),"跳合同制员工请假模块！",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onResultToP(List<NewsBean> mNewsDatas) {
        fruitList.clear();
        fruitList.addAll(mNewsDatas);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResultToPWithIV(List<BannerBean> mNewsDatas) {
        localImages = null;


        convenientBanner.setPages(new CBViewHolderCreator<LocalBannerImageHolderView>() {
            @Override
            public LocalBannerImageHolderView createHolder() {
                return new LocalBannerImageHolderView();
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
        if (getActivity()!=null){
//            Toast.makeText(getActivity(),"网络问题请重试！",Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 为了方便改写，来实现复杂布局的切换
     */
    private class LocalImageHolderView implements Holder<Integer> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            View view = View.inflate(context, R.layout.view_imageview, null);
            imageView = (ImageView) view.findViewById(R.id.iv_view_imageview);
            return view;
        }

        @Override
        public void UpdateUI(Context context, int position, Integer data) {
            imageView.setImageResource(data);
        }
    }

    public class LocalBannerImageHolderView implements Holder<BannerBean> {

        private ImageView imageView;
        @Override
        public View createView(Context context) {
            View view = View.inflate(context, R.layout.view_imageview, null);
            imageView = (ImageView) view.findViewById(R.id.iv_view_imageview);
            return view;
        }

        @Override
        public void UpdateUI(final Context context, int position, final BannerBean bean) {
            Glide.with(context).load(BASE_SERVER_URL_NEWS_IMG_BANNER+bean.getAttname()).into(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }





    /**
     * 获取本地的图片
     */
    private void getLocalImage() {
        //轮播图最大容量
        int sowingMapCapacity = 5;
        for (int position = 0; position < sowingMapCapacity; position++) {
            localImages.add(getResId("banner_" + position, R.mipmap.class));
        }
    }

    @Override
    public void showChildFragment(Bundle bundle) {
        mPreFragment.showChildFragment(bundle);
    }

    /**
     * 通过文件名获取资源id 例子：getResId("icon", R.drawable.class);
     *
     * @param variableName
     * @param c
     * @return
     */
    public static int getResId(String variableName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        convenientBanner.stopTurning();
    }

    /**
     * 首先进行入参检查防止出现空指针异常
     * 如果两个参数都为空，则返回true
     * 如果有一项为空，则返回false
     * 接着对第一个list进行遍历，如果某一项第二个list里面没有，则返回false
     * 还要再将两个list反过来比较，因为可能一个list是两一个list的子集
     * 如果成功遍历结束，返回true
     * @param l0
     * @param l1
     * @return
     */
    public static boolean isListEqual(List l0, List l1){
        if (l0 == l1){
            return true;
        }
        if (l0 == null && l1 == null){
            return true;
        }
        if (l0 == null || l1 == null){
            return false;
        }
        if (l0.size() != l1.size()){
            return false;
        }
        for (Object o : l0) {
            boolean test = l1.contains(o);

            Log.e("test",""+test);

            if (!l1.contains(o)){
                return false;
            }

        }
        for (Object o : l1) {
            if (!l0.contains(o)){
                return false;
            }

        }
        return true;
    }
}
