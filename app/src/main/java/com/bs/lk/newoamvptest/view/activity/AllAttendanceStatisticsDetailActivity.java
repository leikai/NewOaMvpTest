package com.bs.lk.newoamvptest.view.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.lk.newoamvptest.R;
import com.bs.lk.newoamvptest.adapter.AttendanceStatisticsDetailAdapter;
import com.bs.lk.newoamvptest.bean.AttendanceDataBean;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllAttendanceStatisticsDetailActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.attendance_statistic_bar)
    AppBarLayout attendanceStatisticBar;
    @BindView(R.id.rl_detail_attendance_statisitcs)
    RecyclerView rlDetailAttendanceStatisitcs;

    /**
     * 开始时间
     */
    private String start_date;

    /**
     * 结束时间
     */
    private String end_date;

    /**
     * 是否使用特殊的标题栏背景颜色，android5.0以上可以设置状态栏背景色，如果不使用则使用透明色值
     */
    protected boolean useThemestatusBarColor = false;
    /**
     * 是否使用状态栏文字和图标为暗色，如果状态栏采用了白色系，则需要使状态栏和图标为暗色，android6.0以上可以设置
     */
    protected boolean useStatusBarColor = true;
    /**
     * 考勤数据
     */
    private List<AttendanceDataBean> attendanceDataBeanList;

    /**
     * 缺卡次数
     */
    private int missCardTimes;

    /**
     * 请假次数
     */
    private int leaveCardTimes;
    /**
     * 迟到次数
     */
    private int lateCardTimes;
    /**
     * 早退次数
     */
    private int leaveEarlyCardTimes;

    private LinearLayoutManager layoutManager;
    private AttendanceStatisticsDetailAdapter attendanceStatisticsDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_attendance_statistics_detail);
        setSupportActionBar(toolBar);
        setStatusBar();
        ButterKnife.bind(this);
        setDefaultTime();
        String attendanceStatisticsType =  getIntent().getStringExtra("考勤详情类型");
        attendanceDataBeanList =  (List<AttendanceDataBean>)getIntent().getSerializableExtra("考勤详情数据");
        tvTitle.setText(attendanceStatisticsType);


        layoutManager = new LinearLayoutManager(AllAttendanceStatisticsDetailActivity.this);
        rlDetailAttendanceStatisitcs.setLayoutManager(layoutManager);
        attendanceStatisticsDetailAdapter = new AttendanceStatisticsDetailAdapter(attendanceDataBeanList);
        rlDetailAttendanceStatisitcs.setAdapter(attendanceStatisticsDetailAdapter);
    }

    /**
     * 设置状态栏
     */
    protected void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0及以上
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            //根据上面设置是否对状态栏单独设置颜色
            if (useThemestatusBarColor) {
                //设置状态栏背景色
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
            } else {
                //设置状态栏背景色
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));

            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //4.4到5.0
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        } else {
            Toast.makeText(this, "低于4.4的android系统版本不存在沉浸式状态栏", Toast.LENGTH_SHORT).show();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && useStatusBarColor) {
            //android6.0以后可以对状态栏文字颜色和图标进行修改
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }


    /**
     * 设置默认开始时间，结束时间，默认统计上个月的考勤数据
     */
    private void setDefaultTime() {
        SimpleDateFormat dateFormater = new SimpleDateFormat(
                "yyyy-MM-dd");
        //获取前一个月第一天
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.MONTH, 0);
        calendar1.set(Calendar.DAY_OF_MONTH, 1);
        start_date = dateFormater.format(calendar1.getTime());
        Log.e("开始时间：", start_date);

        end_date = getCurDateAndTime("yyyy-MM-dd");
        Log.e("结束时间：", end_date);

//        String curDate = getCurDateAndTime("yyyy-MM-dd");

    }

    /**
     * 获取当前的时间
     *
     * @param pattern 获取的时间格式
     * @return
     */
    private String getCurDateAndTime(String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

}
