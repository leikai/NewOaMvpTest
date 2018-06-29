package com.bs.lk.newoamvptest.view.activity.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bs.lk.newoamvptest.R;
import com.bs.lk.newoamvptest.adapter.FruitAdapter;
import com.bs.lk.newoamvptest.adapter.HomePageAdapter;
import com.bs.lk.newoamvptest.bean.AppBean;
import com.bs.lk.newoamvptest.bean.Fruit;
import com.bs.lk.newoamvptest.view.activity.CabinetActivity;
import com.bs.lk.newoamvptest.view.activity.Leave1Activity;
import com.bs.lk.newoamvptest.view.activity.LeaveInformal1Activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class HomePageFragment extends BaseFragment  {
    private ConvenientBanner convenientBanner;
    private ArrayList<Integer> localImages = new ArrayList<Integer>();
    private RecyclerView rvHomepageWeidoorRecycleView;
    private LinearLayout cabinet;
    private LinearLayout machine;
    private LinearLayout leave;
    private LinearLayout leave_informal;
    GridView mAppsGridView;
    HomePageAdapter mAdapter;
    int[] mResourceIdArray = {R.drawable.icon_dbsy, R.drawable.icon_ybsy, R.drawable
            .icon_wdlc, R.drawable.icon_cjlc};//, R.drawable.app_email_icon
    String[] mAppNameArray = {"待办事宜", "已办事宜", "通知公告", "任务管理"};//, "收发邮件"
    int[] mFragmentIndexArray = {AppBean.FRAGMENT_TO_DO_TASK, AppBean.FRAGMENT_HISTORY_TASK, AppBean
            .FRAGMENT_NOTICE_TASK, AppBean.FRAGMENT_MANAGER_TASK};//, AppBean.FRAGMENT_EMAIL_TASK

    private List<Fruit> fruitList = new ArrayList<>();
    private Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = "晋中市中级人民法院";
        mShowBtnBack = View.INVISIBLE;
        context = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return onCreateView(inflater, R.layout.fragment_home_page, container, savedInstanceState);
    }

    @Nullable
    @Override
    public void initFragment(@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initFruits();
        rvHomepageWeidoorRecycleView = (RecyclerView) mRootView.findViewById(R.id.home_page_view_wei_door);
        convenientBanner = (ConvenientBanner) mRootView.findViewById(R.id.fragment_home_convenientBanner);
        cabinet = (LinearLayout) mRootView.findViewById(R.id.view_homepage_ll_cabinet);
        machine = (LinearLayout) mRootView.findViewById(R.id.view_homepage_ll_machine);
        leave = (LinearLayout) mRootView.findViewById(R.id.view_homepage_ll_leave);
        leave_informal = (LinearLayout) mRootView.findViewById(R.id.view_homepage_ll_tzgg);
        mAppsGridView = (GridView) mRootView.findViewById(R.id.apps_gridview);

        ArrayList<AppBean> apps = new ArrayList<>();
        for (int i = 0; i < mResourceIdArray.length; i++) {
            AppBean app = new AppBean();
            app.setResourceId(mResourceIdArray[i]);
            app.setName(mAppNameArray[i]);
            app.setFragmentIndex(mFragmentIndexArray[i]);
            apps.add(app);
        }
        mAdapter = new HomePageAdapter(this, apps);
        mAppsGridView.setAdapter(mAdapter);


        //获取本地图片
        getLocalImage();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvHomepageWeidoorRecycleView.setLayoutManager(layoutManager);
        FruitAdapter adapter = new FruitAdapter(context,fruitList);
        rvHomepageWeidoorRecycleView.setAdapter(adapter);

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

    private void initEvent() {
        cabinet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent jumptoCabinet = new Intent(getContext(), CabinetActivity.class);
                startActivity(jumptoCabinet);

                Toast.makeText(getContext(),"跳云柜模块！",Toast.LENGTH_SHORT).show();

            }
        });
        machine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(getContext(), MachineActivity.class));

                Toast.makeText(getContext(),"跳留言机模块！",Toast.LENGTH_SHORT).show();
            }
        });
        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),Leave1Activity.class));
                Toast.makeText(getContext(),"跳正式员工请假模块！",Toast.LENGTH_SHORT).show();
            }
        });
        leave_informal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),LeaveInformal1Activity.class));
                Toast.makeText(getContext(),"跳合同制员工请假模块！",Toast.LENGTH_SHORT).show();
            }
        });

    }


    //为了方便改写，来实现复杂布局的切换
    private class LocalImageHolderView implements Holder<Integer> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            //你可以通过layout文件来创建，不一定是Image，任何控件都可以进行翻页
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, int position, Integer data) {
            imageView.setImageResource(data);
        }
    }

    private void getLocalImage() {
        //获取本地的图片
        for (int position = 0; position < 6; position++) {
            localImages.add(getResId("banner_" + position, R.mipmap.class));
        }
    }

    @Override
    public void showChildFragment(Bundle bundle) {
        mPreFragment.showChildFragment(bundle);
    }
    private void initFruits() {

        for (int i=0;i<1;i++){
            Fruit apple = new Fruit("晋中中院院长史红波带队在最高院考察学习信息化建设工作", R.drawable.weidoor_1);
            fruitList.add(apple);
            Fruit banana = new Fruit("晋中市龙泉生态牧林庄园和魏某某等34名被告人集资诈骗、非法吸收公众存款、掩饰隐瞒犯罪所得一案一审宣判", R.drawable.weidoor_2);
            fruitList.add(banana);
            Fruit orange = new Fruit("榆次区人民法院开发区人民法庭正式挂牌运行", R.drawable.weidoor_3);
            fruitList.add(orange);
            Fruit watermelon = new Fruit("新年伊始，晋中市中级人民法院迎来了“开门红”", R.drawable.weidoor_4);
            fruitList.add(watermelon);
            Fruit pear = new Fruit("晋中全市法院党建工作会议在榆社召开", R.drawable.weidoor_5);
            fruitList.add(pear);

        }
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
}
