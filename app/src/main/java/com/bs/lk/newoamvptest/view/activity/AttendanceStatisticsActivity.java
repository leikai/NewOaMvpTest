package com.bs.lk.newoamvptest.view.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.lk.newoamvptest.CApplication;
import com.bs.lk.newoamvptest.R;
import com.bs.lk.newoamvptest.bean.AttendanceDataBean;
import com.bs.lk.newoamvptest.bean.AttendanceMsgBean;
import com.bs.lk.newoamvptest.bean.AttendanceShiftsBean;
import com.bs.lk.newoamvptest.bean.UserNewBean;
import com.bs.lk.newoamvptest.presenter.AttendanceStatisticsPresenter;
import com.bs.lk.newoamvptest.widget.LoadingDialog;

import org.litepal.LitePal;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 个人考勤统计
 *
 * @author lk
 */
public class AttendanceStatisticsActivity extends AppCompatActivity implements IAttendanceStatisticsView{

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.attendance_statistic_bar)
    AppBarLayout attendanceStatisticBar;
    @BindView(R.id.iv_user_head)
    ImageView ivUserHead;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_user_department)
    TextView tvUserDepartment;
    @BindView(R.id.tv_date_clock)
    TextView tvDateClock;
    @BindView(R.id.rl_attendance_statistics_user)
    RelativeLayout rlAttendanceStatisticsUser;
    @BindView(R.id.iv_user_line)
    ImageView ivUserLine;
    private AttendanceStatisticsPresenter attendanceStatisticsPresenter;



    /**
     * 是否使用特殊的标题栏背景颜色，android5.0以上可以设置状态栏背景色，如果不使用则使用透明色值
     */
    protected boolean useThemestatusBarColor = false;
    /**
     * 是否使用状态栏文字和图标为暗色，如果状态栏采用了白色系，则需要使状态栏和图标为暗色，android6.0以上可以设置
     */
    protected boolean useStatusBarColor = true;
    @BindView(R.id.item_tv_attendance_statistics_days)
    TextView itemTvAttendanceStatisticsDays;
    @BindView(R.id.item_tv_attendance_statistics_days_times)
    TextView itemTvAttendanceStatisticsDaysTimes;
    @BindView(R.id.rl_attendance_statistics_days)
    RelativeLayout rlAttendanceStatisticsDays;
    @BindView(R.id.item_tv_attendance_statistics_miss)
    TextView itemTvAttendanceStatisticsMiss;
    @BindView(R.id.item_tv_attendance_statistics_miss_times)
    TextView itemTvAttendanceStatisticsMissTimes;
    @BindView(R.id.rl_attendance_statistics_miss)
    RelativeLayout rlAttendanceStatisticsMiss;
    @BindView(R.id.item_tv_attendance_statistics_leave)
    TextView itemTvAttendanceStatisticsLeave;
    @BindView(R.id.item_tv_attendance_statistics_leave_times)
    TextView itemTvAttendanceStatisticsLeaveTimes;
    @BindView(R.id.rl_attendance_statistics_leave)
    RelativeLayout rlAttendanceStatisticsLeave;
    @BindView(R.id.item_tv_attendance_statistics_late)
    TextView itemTvAttendanceStatisticsLate;
    @BindView(R.id.item_tv_attendance_statistics_late_times)
    TextView itemTvAttendanceStatisticsLateTimes;
    @BindView(R.id.rl_attendance_statistics_late)
    RelativeLayout rlAttendanceStatisticsLate;
    @BindView(R.id.item_tv_attendance_statistics_leave_early)
    TextView itemTvAttendanceStatisticsLeaveEarly;
    @BindView(R.id.item_tv_attendance_statistics_leave_early_times)
    TextView itemTvAttendanceStatisticsLeaveEarlyTimes;
    @BindView(R.id.rl_attendance_statistics_leave_early)
    RelativeLayout rlAttendanceStatisticsLeaveEarly;
    @BindView(R.id.ll_attendance_statistic)
    LinearLayout llAttendanceStatistic;


    /**
     * 本月考勤
     */
    private List<AttendanceMsgBean> attendanceMsgBeanList = new ArrayList<>();

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
    /**
     * 当前用户
     */
    private UserNewBean curUser;

    /**
     * 开始时间
     */
    private String start_date;

    /**
     * 结束时间
     */
    private String end_date;
    /**
     * 出勤天数数据统计集合
     */
    private List<AttendanceDataBean> intentDaysAttendanceDataBeanLists = new ArrayList<>();
    /**
     * 缺卡数据统计集合
     */
    private List<AttendanceDataBean> intentMissCardAttendanceDataBeanLists = new ArrayList<>();
    /**
     * 请假数据统计集合
     */
    private List<AttendanceDataBean> intentLeaveAttendanceDataBeanLists = new ArrayList<>();
    /**
     * 迟到数据统计集合
     */
    private List<AttendanceDataBean> intentLateAttendanceDataBeanLists = new ArrayList<>();
    /**
     * 早退数据统计集合
     */
    private List<AttendanceDataBean> intentLeaveEarlyAttendanceDataBeanLists = new ArrayList<>();

    private Date toWorkMornningDate;
    private Date workFinishMornningDate;
    private Date toWorkAfternoonDate;
    private Date workFinishAfternoonDate;
    /**
     * 加载中Dialog
     */
    private LoadingDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_statistics);
        setSupportActionBar(toolBar);
        setStatusBar();
        ButterKnife.bind(this);

        curUser = CApplication.getInstance().getCurrentUser();
        dialog=new LoadingDialog(AttendanceStatisticsActivity.this,"玩命加载中...");
        //显示Dialog
        dialog.show();
        tvUserName.setText(curUser.getEmpname());
        tvUserDepartment.setText(curUser.getDeptname());
        tvTitle.setText("个人考勤统计");
        attendanceStatisticsPresenter = new AttendanceStatisticsPresenter(this);
        initData();

    }

    private void initData() {

        attendanceStatisticsPresenter.doAttendanceDataForPrams(null,curUser.getOid());
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


    @OnClick({R.id.item_tv_attendance_statistics_days_times, R.id.rl_attendance_statistics_days, R.id.item_tv_attendance_statistics_miss_times, R.id.rl_attendance_statistics_miss, R.id.item_tv_attendance_statistics_leave_times, R.id.rl_attendance_statistics_leave, R.id.item_tv_attendance_statistics_late_times, R.id.rl_attendance_statistics_late, R.id.item_tv_attendance_statistics_leave_early_times, R.id.rl_attendance_statistics_leave_early})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.item_tv_attendance_statistics_days_times:
                break;
            case R.id.rl_attendance_statistics_days:
//                intentDaysAttendanceDataBeanLists = LitePal.where("date<=? and date>=?", end_date, start_date).find(AttendanceDataBean.class);
                if (intentDaysAttendanceDataBeanLists == null){
                    Log.e("attendanceDataBeanList：", "考勤天数" );
                }else {
                    Log.e("attendanceDataBeanList：", "" + intentDaysAttendanceDataBeanLists.size());
                    Log.e("attendanceDataBeanList：", "" + intentDaysAttendanceDataBeanLists.size());
                }
                Intent intent = new Intent(AttendanceStatisticsActivity.this,AttendanceStatisticsDetailActivity.class);
                intent.putExtra("考勤详情类型","考勤天数");
                intent.putExtra("考勤详情数据", (Serializable) intentDaysAttendanceDataBeanLists);

                startActivity(intent);
                break;
            case R.id.item_tv_attendance_statistics_miss_times:
                break;
            case R.id.rl_attendance_statistics_miss:
                Intent missIntent = new Intent(AttendanceStatisticsActivity.this,AttendanceStatisticsDetailActivity.class);
                missIntent.putExtra("考勤详情类型","缺卡统计");
                missIntent.putExtra("考勤详情数据", (Serializable) intentMissCardAttendanceDataBeanLists);
                startActivity(missIntent);
                break;
            case R.id.item_tv_attendance_statistics_leave_times:
                break;
            case R.id.rl_attendance_statistics_leave:
                intentLeaveAttendanceDataBeanLists  = LitePal.where("date<=? and date>=? and state=?",end_date,start_date,"请假").find(AttendanceDataBean.class);
                if (intentLeaveAttendanceDataBeanLists == null){
                    Log.e("attendanceDataBeanList：", "无请假数据" );
                }else {
                    Log.e("attendanceDataBeanList：", "" + intentLeaveAttendanceDataBeanLists.size());
                    Log.e("attendanceDataBeanList：", "" + intentLeaveAttendanceDataBeanLists.size());
                }
                Intent leaveIntent = new Intent(AttendanceStatisticsActivity.this,AttendanceStatisticsDetailActivity.class);
                leaveIntent.putExtra("考勤详情类型","请假统计");
                leaveIntent.putExtra("考勤详情数据", (Serializable) intentLeaveAttendanceDataBeanLists);
                startActivity(leaveIntent);
                break;
            case R.id.item_tv_attendance_statistics_late_times:
                break;
            case R.id.rl_attendance_statistics_late:
                Intent lateIntent = new Intent(AttendanceStatisticsActivity.this,AttendanceStatisticsDetailActivity.class);
                lateIntent.putExtra("考勤详情类型","迟到统计");
                lateIntent.putExtra("考勤详情数据", (Serializable) intentLateAttendanceDataBeanLists);
                startActivity(lateIntent);
                break;
            case R.id.item_tv_attendance_statistics_leave_early_times:
                break;
            case R.id.rl_attendance_statistics_leave_early:
                Intent leaveEarlyIntent = new Intent(AttendanceStatisticsActivity.this,AttendanceStatisticsDetailActivity.class);
                leaveEarlyIntent.putExtra("考勤详情类型","早退统计");
                leaveEarlyIntent.putExtra("考勤详情数据", (Serializable) intentLeaveEarlyAttendanceDataBeanLists);
                startActivity(leaveEarlyIntent);
                break;
                default:
        }
    }

    @Override
    public void onResultToPData() {
        //关闭Dialog
        dialog.close();
        List<AttendanceShiftsBean> shiftsBeanList = LitePal.findAll(AttendanceShiftsBean.class);
        if (shiftsBeanList!=null){
            if (shiftsBeanList.size()!=0){
                AttendanceShiftsBean attendanceShift = shiftsBeanList.get(0);
                processData(attendanceShift);
            }
        }
    }

    /**
     * 对数据进行加工，得到各种统计数据
     * @param attendanceShiftsBean
     */
    private void processData(AttendanceShiftsBean attendanceShiftsBean) {
        setDefaultTime();
        List<AttendanceDataBean> attendanceDataBeanListAll  = LitePal.findAll(AttendanceDataBean.class);
        Log.e("sss",""+attendanceDataBeanListAll.size());
        //获取用户当月的考勤数据
        intentDaysAttendanceDataBeanLists  = LitePal.where("date<=? and date>=?",end_date,start_date).find(AttendanceDataBean.class);
        Log.e("attendanceDataBeanList：",""+intentDaysAttendanceDataBeanLists.size());
        Log.e("attendanceDataBeanList：",""+intentDaysAttendanceDataBeanLists.size());
        //获取请假的数据
        List<AttendanceDataBean> attendanceDataBeanListLeave  = LitePal.where("date<=? and date>=? and state=?",end_date,start_date,"请假").find(AttendanceDataBean.class);
        String leaveTimes = String.valueOf(attendanceDataBeanListLeave.size());
        Log.e("请假次数：",""+attendanceDataBeanListLeave.size());
        itemTvAttendanceStatisticsLeaveTimes.setText(leaveTimes +"次");
        //获取缺卡数据
        String shiftTimes = attendanceShiftsBean.getShift();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        switch (shiftTimes){
            case "2":
                try {
                    //上班时间
                    String toWorkTime = getCurDateAndTime("yyyy-MM-dd")+" "+attendanceShiftsBean.getSworktime();
                    Date toWorkStandardDate = dateFormat.parse(toWorkTime);
                    //下班时间
                    String workFinishTime =  getCurDateAndTime("yyyy-MM-dd")+" "+attendanceShiftsBean.getSbacktime();
                    Date workFinishStandardDate = dateFormat.parse(workFinishTime);
                    for (int i=0;i < intentDaysAttendanceDataBeanLists.size();i++){
                        String ceshi = intentDaysAttendanceDataBeanLists.get(i).getSsignintimte();
                        String ceshi2 = intentDaysAttendanceDataBeanLists.get(i).getSsignintimte();
                        if (ceshi!= null&&ceshi2!=null){
                            if (ceshi.equals("null")|ceshi2.equals("null")){
                                missCardTimes = ++missCardTimes;
                            }else {
                                Log.e("ceshi",""+ceshi);
                                Date toWorkDate = dateFormat.parse(intentDaysAttendanceDataBeanLists.get(i).getSsignintimte());
                                Date workFinishDate = dateFormat.parse(intentDaysAttendanceDataBeanLists.get(i).getSsignbacktime());
                                if (toWorkDate.getTime()>toWorkStandardDate.getTime()){
                                    lateCardTimes =++lateCardTimes;
                                    Log.e("lateCardTimes",""+lateCardTimes);
                                }
                                if (workFinishDate.getTime()<workFinishStandardDate.getTime()){
                                    leaveEarlyCardTimes = ++leaveEarlyCardTimes;
                                }
                            }
                        }
                    }
                    String lateCardTimes1 = String.valueOf(lateCardTimes);
                    Log.e("lateCardTimes1",""+lateCardTimes1);
                    String leaveEarlyCardTimes1 = String.valueOf(leaveEarlyCardTimes);
                    Log.e("lateCardTimes1",""+leaveEarlyCardTimes1);
                    //出勤天数
                    itemTvAttendanceStatisticsDaysTimes.setText(String.valueOf(intentDaysAttendanceDataBeanLists.size())+"天");
                    //缺卡
                    itemTvAttendanceStatisticsMissTimes.setText(String.valueOf(missCardTimes)+"次");
                    //迟到
                    itemTvAttendanceStatisticsLateTimes.setText(String.valueOf(lateCardTimes)+"次");
                    //早退
                    itemTvAttendanceStatisticsLeaveEarlyTimes.setText(String.valueOf(leaveEarlyCardTimes)+"次");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case "4":

                try {
                    //上午上班时间
                    String toWorkMornningTime = getCurDateAndTime("yyyy-MM-dd")+" "+attendanceShiftsBean.getSworktime();
                    Date toWorkMornningStandardDate = dateFormat.parse(toWorkMornningTime);
                    //上午下班时间
                    String workFinishMorningTime = getCurDateAndTime("yyyy-MM-dd")+" "+attendanceShiftsBean.getSbacktime();
                    Date workFinishStandardMorningDate = dateFormat.parse(workFinishMorningTime);
                    //下午上班时间
                    String toWorkAfternoonTime = getCurDateAndTime("yyyy-MM-dd")+" "+attendanceShiftsBean.getXworktime();
                    Date toWorkStandardAfternoonDate = dateFormat.parse(toWorkAfternoonTime);
                    //下午下班时间
                    String workFinishAfternoonTime =  getCurDateAndTime("yyyy-MM-dd")+" "+attendanceShiftsBean.getXbacktime();
                    Date workFinishStandardAfternoonDate = dateFormat.parse(workFinishAfternoonTime);
                    for (int i=0;i < intentDaysAttendanceDataBeanLists.size();i++){
                        String ceshi = intentDaysAttendanceDataBeanLists.get(i).getSsignintimte();
                        String ceshi2 = intentDaysAttendanceDataBeanLists.get(i).getSsignbacktime();
                        String ceshi3 = intentDaysAttendanceDataBeanLists.get(i).getXsignintimte();
                        String ceshi4 = intentDaysAttendanceDataBeanLists.get(i).getXsignbacktime();
                        if (ceshi!= null&&ceshi2!=null&&ceshi3!=null&&ceshi4!=null){
                            if (ceshi.equals("null")){
                                missCardTimes = ++missCardTimes;
                                intentDaysAttendanceDataBeanLists.get(i).setAmbstate("缺卡");
                                if (!intentMissCardAttendanceDataBeanLists.contains(intentDaysAttendanceDataBeanLists.get(i))){

                                    intentMissCardAttendanceDataBeanLists.add(intentDaysAttendanceDataBeanLists.get(i));

                                }
                            }
                            if (ceshi2.equals("null")){
                                missCardTimes = ++missCardTimes;
                                intentDaysAttendanceDataBeanLists.get(i).setAmestate("缺卡");
                                if (!intentMissCardAttendanceDataBeanLists.contains(intentDaysAttendanceDataBeanLists.get(i))){

                                    intentMissCardAttendanceDataBeanLists.add(intentDaysAttendanceDataBeanLists.get(i));
                                }

                            }
                            if (ceshi3.equals("null")){
                                missCardTimes = ++missCardTimes;
                                intentDaysAttendanceDataBeanLists.get(i).setPmbstate("缺卡");
                                if (!intentMissCardAttendanceDataBeanLists.contains(intentDaysAttendanceDataBeanLists.get(i))){

                                    intentMissCardAttendanceDataBeanLists.add(intentDaysAttendanceDataBeanLists.get(i));
                                }

                            }
                            if (ceshi4.equals("null")){
                                missCardTimes = ++missCardTimes;
                                intentDaysAttendanceDataBeanLists.get(i).setPmestate("缺卡");
                                if (!intentMissCardAttendanceDataBeanLists.contains(intentDaysAttendanceDataBeanLists.get(i))){

                                    intentMissCardAttendanceDataBeanLists.add(intentDaysAttendanceDataBeanLists.get(i));
                                }

                            }else {
                                Log.e("ceshi",""+ceshi);
                                if (!intentDaysAttendanceDataBeanLists.get(i).getSsignintimte().equals("null")){
                                    toWorkMornningDate = dateFormat.parse(intentDaysAttendanceDataBeanLists.get(i).getSsignintimte());
                                    if (toWorkMornningDate.getTime()>toWorkMornningStandardDate.getTime()){
                                        lateCardTimes =++lateCardTimes;
                                        intentDaysAttendanceDataBeanLists.get(i).setAmbstate("迟到");
                                        if (!intentLateAttendanceDataBeanLists.contains(intentDaysAttendanceDataBeanLists.get(i))){

                                            intentLateAttendanceDataBeanLists.add(intentDaysAttendanceDataBeanLists.get(i));
                                        }

                                        Log.e("lateCardTimes",""+lateCardTimes);
                                    }
                                }
                                if (!intentDaysAttendanceDataBeanLists.get(i).getSsignbacktime().equals("null")){
                                    workFinishMornningDate = dateFormat.parse(intentDaysAttendanceDataBeanLists.get(i).getSsignbacktime());
                                    if (workFinishMornningDate.getTime()<workFinishStandardMorningDate.getTime()){
                                        intentDaysAttendanceDataBeanLists.get(i).setAmestate("早退");
                                        leaveEarlyCardTimes = ++leaveEarlyCardTimes;
                                        if (!intentLeaveEarlyAttendanceDataBeanLists.contains(intentDaysAttendanceDataBeanLists.get(i))){

                                            intentLeaveEarlyAttendanceDataBeanLists.add(intentDaysAttendanceDataBeanLists.get(i));
                                        }

                                    }
                                }
                                if (!intentDaysAttendanceDataBeanLists.get(i).getXsignintimte().equals("null")){
                                    toWorkAfternoonDate = dateFormat.parse(intentDaysAttendanceDataBeanLists.get(i).getXsignintimte());
                                    if (toWorkAfternoonDate.getTime()>toWorkStandardAfternoonDate.getTime()){
                                        lateCardTimes =++lateCardTimes;
                                        intentDaysAttendanceDataBeanLists.get(i).setPmbstate("迟到");
                                        if (!intentLateAttendanceDataBeanLists.contains(intentDaysAttendanceDataBeanLists.get(i))){

                                            intentLateAttendanceDataBeanLists.add(intentDaysAttendanceDataBeanLists.get(i));
                                        }
                                        Log.e("lateCardTimes",""+lateCardTimes);
                                    }
                                }
                                if (!intentDaysAttendanceDataBeanLists.get(i).getXsignbacktime().equals("null")){
                                    workFinishAfternoonDate = dateFormat.parse(intentDaysAttendanceDataBeanLists.get(i).getXsignbacktime());
                                    if (workFinishAfternoonDate.getTime()<workFinishStandardAfternoonDate.getTime()){
                                        intentDaysAttendanceDataBeanLists.get(i).setPmestate("早退");
                                        leaveEarlyCardTimes = ++leaveEarlyCardTimes;
                                        if (!intentLeaveEarlyAttendanceDataBeanLists.contains(intentDaysAttendanceDataBeanLists.get(i))){

                                            intentLeaveEarlyAttendanceDataBeanLists.add(intentDaysAttendanceDataBeanLists.get(i));
                                        }

                                    }
                                }
                            }
                        }
                    }
                    String lateCardTimes1 = String.valueOf(lateCardTimes);
                    Log.e("lateCardTimes1",""+lateCardTimes1);
                    String leaveEarlyCardTimes1 = String.valueOf(leaveEarlyCardTimes);
                    Log.e("lateCardTimes1",""+leaveEarlyCardTimes1);
                    //出勤天数
                    itemTvAttendanceStatisticsDaysTimes.setText(String.valueOf(intentDaysAttendanceDataBeanLists.size())+"天");
                    //缺卡
                    itemTvAttendanceStatisticsMissTimes.setText(String.valueOf(missCardTimes)+"次");
                    //迟到
                    itemTvAttendanceStatisticsLateTimes.setText(String.valueOf(lateCardTimes)+"次");
                    //早退
                    itemTvAttendanceStatisticsLeaveEarlyTimes.setText(String.valueOf(leaveEarlyCardTimes)+"次");
                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.e("个人考勤统计报错：", e.toString());
                }


                break;
                default:
        }


    }

    @Override
    public void onResultForError() {

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
        calendar1.set(Calendar.DAY_OF_MONTH,1);
        start_date = dateFormater.format(calendar1.getTime());
        Log.e("开始时间：",start_date);

        end_date =  getCurDateAndTime("yyyy-MM-dd");
        Log.e("结束时间：",end_date);

//        String curDate = getCurDateAndTime("yyyy-MM-dd");
        tvDateClock.setText(start_date+"至"+end_date);

    }


}
