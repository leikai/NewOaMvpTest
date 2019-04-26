package com.bs.lk.newoamvptest.view.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;

import com.bs.lk.newoamvptest.R;
import com.bs.lk.newoamvptest.adapter.CalendarAdapter;
import com.bs.lk.newoamvptest.bean.CustomDate;
import com.bs.lk.newoamvptest.bean.Schedual;
import com.bs.lk.newoamvptest.util.CalendarViewBuilder;
import com.bs.lk.newoamvptest.util.DateUtil;
import com.bs.lk.newoamvptest.widget.CalendarView;
import com.bs.lk.newoamvptest.widget.CalendarViewPagerLisenter;
import com.bs.lk.newoamvptest.widget.CircleTextView;
import com.bs.lk.newoamvptest.widget.CustomViewPagerAdapter;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 日历页面
 *
 * @author lk
 */
@SuppressWarnings("deprecation")
public class CalendarActivity extends Activity implements  CalendarView.CallBack {
    @BindView(R.id.show_month_view)
    TextView showMonthView;
    @BindView(R.id.show_week_view)
    TextView showWeekView;
    @BindView(R.id.show_year_view)
    TextView showYearView;
    @BindView(R.id.month_calendar_button)
    TextView monthCalendarButton;
    @BindView(R.id.week_calendar_button)
    TextView weekCalendarButton;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.handlerText)
    LinearLayout handlerText;
    @BindView(R.id.lv_content)
    ListView lvContent;
    @BindView(R.id.contentText)
    LinearLayout contentText;
    @BindView(R.id.sildingDrawer)
    SlidingDrawer sildingDrawer;
    @BindView(R.id.contentPager)
    RelativeLayout contentPager;
    @BindView(R.id.now_circle_view)
    CircleTextView nowCircleView;
    @BindView(R.id.subscibe_circle_view)
    CircleTextView subscibeCircleView;
    @BindView(R.id.add_circle_view)
    CircleTextView addCircleView;
    private CalendarView[] views;
    private CalendarViewBuilder builder = new CalendarViewBuilder();
    private CustomDate mClickDate;
    public static final String MAIN_ACTIVITY_CLICK_DATE = "main_click_date";
    private List<Schedual> scheduals;
    private String tableName = "schedule.db";
    private CalendarAdapter myAdapter;
    private Handler handler1 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化页面数据
     */
    @SuppressLint("HandlerLeak")
    private void initView() {
        handler1 = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
//                if (scheduleDao != null) {
                    scheduals = LitePal.findAll(Schedual.class);
                    myAdapter = new CalendarAdapter(getApplicationContext(), mClickDate, scheduals);
                    lvContent.setAdapter(myAdapter);
                    lvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent jumptoDetailActivity = new Intent(getApplicationContext(), ScheduleActivity.class);
                            jumptoDetailActivity.putExtra("i", i);
                            startActivity(jumptoDetailActivity);
                            finish();

                        }
                    });
                    String schedualContent = "";
                    for (int i = 0; i < scheduals.size(); i++) {
                        schedualContent += scheduals.get(i).getContent();
                    }
//                 else {
//                    scheduals = LitePal.findAll(Schedual.class);
//                    myAdapter = new CalendarAdapter(getApplicationContext(), mClickDate, scheduals);
//                    String schedualContent = "";
//                    for (int i = 0; i < scheduals.size(); i++) {
//                        schedualContent += scheduals.get(i).getContent();
//                    }
//                }
            }
        };
        views = builder.createMassCalendarViews(this, 5, this);
        setViewPager();
        swtichBackgroundForButton(true);
        builder.swtichCalendarViewsStyle(CalendarView.MONTH_STYLE);
        sildingDrawer.close();
        setOnDrawListener();

    }


    private void setViewPager() {
        CustomViewPagerAdapter<CalendarView> viewPagerAdapter = new CustomViewPagerAdapter<CalendarView>(views);
        viewpager.setAdapter(viewPagerAdapter);
        viewpager.setCurrentItem(498);
        viewpager.setOnPageChangeListener(new CalendarViewPagerLisenter(viewPagerAdapter));
    }

    /**
     * 设置抽屉sildingDrawer开关的适配器
     */
    private void setOnDrawListener() {
        sildingDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {

            @Override
            public void onDrawerOpened() {
                builder.swtichCalendarViewsStyle(CalendarView.WEEK_STYLE);
                builder.backTodayCalendarViews();
                swtichBackgroundForButton(false);
                sildingDrawer.open();
            }
        });
        sildingDrawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {
                builder.swtichCalendarViewsStyle(CalendarView.MONTH_STYLE);
                swtichBackgroundForButton(true);
                sildingDrawer.close();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void setShowDateViewText(int year, int month) {
        showYearView.setText(year + "");
        showMonthView.setText(month + "月");
        showWeekView.setText(DateUtil.weekName[DateUtil.getWeekDay() - 1]);
    }

    /**
     * 改变标题栏周/月视图模式的背景色
     * @param isMonth
     */
    private void swtichBackgroundForButton(boolean isMonth) {
        if (isMonth) {
            monthCalendarButton.setBackgroundResource(R.drawable.press_left_text_bg);
            weekCalendarButton.setBackgroundColor(Color.TRANSPARENT);
        } else {
            weekCalendarButton.setBackgroundResource(R.drawable.press_right_text_bg);
            monthCalendarButton.setBackgroundColor(Color.TRANSPARENT);
        }
    }


    @Override
    public void onMesureCellHeight(int cellSpace) {
        sildingDrawer.getLayoutParams().height = contentPager.getHeight() - cellSpace;
    }

    /**
     * 点击日期的监听，所做的事情
     * @param date
     */
    @Override
    public void clickDate(CustomDate date) {

        mClickDate = date;
        //通知UI线程更新Listview;
        Message message = new Message();
        handler1.sendEmptyMessage(1);

        //Toast.makeText(this, date.year+"-"+date.month+"-"+date.day, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void changeDate(CustomDate date) {
        setShowDateViewText(date.year, date.month);

    }

    @OnClick({R.id.month_calendar_button, R.id.week_calendar_button, R.id.now_circle_view, R.id.subscibe_circle_view, R.id.add_circle_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.month_calendar_button:
                swtichBackgroundForButton(true);
                builder.swtichCalendarViewsStyle(CalendarView.MONTH_STYLE);
                sildingDrawer.close();
                break;
            case R.id.week_calendar_button:
                swtichBackgroundForButton(false);
                builder.backTodayCalendarViews();
                sildingDrawer.open();
                break;
            case R.id.now_circle_view:
                builder.backTodayCalendarViews();
                break;
            case R.id.subscibe_circle_view:
                break;
            case R.id.add_circle_view:
                Intent i = new Intent(this, AddPlanActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable(MAIN_ACTIVITY_CLICK_DATE, mClickDate);
                i.putExtras(mBundle);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                break;
                default:
        }
    }
}
