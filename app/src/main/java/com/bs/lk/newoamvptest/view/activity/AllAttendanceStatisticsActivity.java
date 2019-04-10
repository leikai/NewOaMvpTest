package com.bs.lk.newoamvptest.view.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.lk.newoamvptest.CApplication;
import com.bs.lk.newoamvptest.R;
import com.bs.lk.newoamvptest.bean.AttendanceDataBean;
import com.bs.lk.newoamvptest.bean.AttendanceShiftsBean;
import com.bs.lk.newoamvptest.bean.DemoBean;
import com.bs.lk.newoamvptest.bean.UserNewBean;
import com.bs.lk.newoamvptest.presenter.AllAttendanceStatisticsPresenter;
import com.bs.lk.newoamvptest.util.ExcelUtil;
import com.bs.lk.newoamvptest.util.file.FileAccessUtil;
import com.bs.lk.newoamvptest.widget.LoadingDialog;

import org.litepal.LitePal;

import java.io.File;
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

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * 部门、单位考勤数据
 *
 * @author lk
 */
public class AllAttendanceStatisticsActivity extends AppCompatActivity implements IAllAttendanceStatisticsView {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.attendance_statistic_bar)
    AppBarLayout attendanceStatisticBar;
    @BindView(R.id.iv_user_head)
    ImageView ivUserHead;
    @BindView(R.id.tv_user_dept)
    TextView tvUserDept;
    @BindView(R.id.tv_date_clock)
    TextView tvDateClock;
    @BindView(R.id.rl_attendance_statistics_user)
    RelativeLayout rlAttendanceStatisticsUser;
    @BindView(R.id.iv_user_line)
    ImageView ivUserLine;
    @BindView(R.id.item_tv_attendance_statistics_people)
    TextView itemTvAttendanceStatisticsPeople;
    @BindView(R.id.item_tv_attendance_statistics_people_number)
    TextView itemTvAttendanceStatisticsPeopleNumber;
    @BindView(R.id.rl_attendance_statistics_days)
    RelativeLayout rlAttendanceStatisticsDays;
    @BindView(R.id.item_tv_attendance_statistics_miss)
    TextView itemTvAttendanceStatisticsMiss;
    @BindView(R.id.item_tv_attendance_statistics_miss_people_number)
    TextView itemTvAttendanceStatisticsMissPeopleNumber;
    @BindView(R.id.rl_attendance_statistics_miss)
    RelativeLayout rlAttendanceStatisticsMiss;
    @BindView(R.id.item_tv_attendance_statistics_leave)
    TextView itemTvAttendanceStatisticsLeave;
    @BindView(R.id.item_tv_attendance_statistics_leave_people_number)
    TextView itemTvAttendanceStatisticsLeavePeopleNumber;
    @BindView(R.id.rl_attendance_statistics_leave)
    RelativeLayout rlAttendanceStatisticsLeave;
    @BindView(R.id.item_tv_attendance_statistics_late)
    TextView itemTvAttendanceStatisticsLate;
    @BindView(R.id.item_tv_attendance_statistics_late_people_number)
    TextView itemTvAttendanceStatisticsLatePeopleNumber;
    @BindView(R.id.rl_attendance_statistics_late)
    RelativeLayout rlAttendanceStatisticsLate;
    @BindView(R.id.item_tv_attendance_statistics_leave_early)
    TextView itemTvAttendanceStatisticsLeaveEarly;
    @BindView(R.id.item_tv_attendance_statistics_leave_early_people_number)
    TextView itemTvAttendanceStatisticsLeaveEarlyPeopleNumber;
    @BindView(R.id.rl_attendance_statistics_leave_early)
    RelativeLayout rlAttendanceStatisticsLeaveEarly;
    @BindView(R.id.ll_attendance_statistic)
    LinearLayout llAttendanceStatistic;
    @BindView(R.id.btn_select)
    Button btnSelect;
    //    @BindView(R.id.ll_content)
    public static LinearLayout llContent;
    //    @BindView(R.id.swip_refresh)
//    public SwipeRefreshLayout swipRefresh;
    @BindView(R.id.drawer_layout)
    public DrawerLayout drawerLayout;
    @BindView(R.id.item_tv_attendance_statistics_go_out)
    TextView itemTvAttendanceStatisticsGoOut;
    @BindView(R.id.item_tv_attendance_statistics_go_out_people_number)
    TextView itemTvAttendanceStatisticsGoOutPeopleNumber;
    @BindView(R.id.rl_attendance_statistics_go_out)
    RelativeLayout rlAttendanceStatisticsGoOut;
    @BindView(R.id.btn_build_excle)
    Button btnBuildExcle;

    /**
     * 当前用户
     */
    private UserNewBean curUser;

    /**
     * 是否使用特殊的标题栏背景颜色，android5.0以上可以设置状态栏背景色，如果不使用则使用透明色值
     */
    protected boolean useThemestatusBarColor = false;
    /**
     * 是否使用状态栏文字和图标为暗色，如果状态栏采用了白色系，则需要使状态栏和图标为暗色，android6.0以上可以设置
     */
    protected boolean useStatusBarColor = true;
    private AllAttendanceStatisticsPresenter allAttendanceStatisticsPresenter;

    /**
     * 开始时间
     */
    private String start_date;

    /**
     * 结束时间
     */
    private String end_date;

    /**
     * 缺卡次数
     */
    private int missCardTimes;
    /**
     * 请假次数
     */
    private int leaveCardTimes;
    /**
     * 外出次数
     */
    private int goOutTimes;
    /**
     * 迟到次数
     */
    private int lateCardTimes;
    /**
     * 早退次数
     */
    private int leaveEarlyCardTimes;

    /**
     * 班次类型
     */
    private int shiftType;

    private List<AttendanceDataBean> attendanceDataBeanList = new ArrayList<>();

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
     * 缺卡数据统计集合
     */
    private List<AttendanceDataBean> intentGoOutAttendanceDataBeanLists = new ArrayList<>();
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

    private AlertDialog alertDialog;
    private AlertDialog mDialog;


    private static final int NOT_NOTICE = 2;//如果勾选了不再询问
    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_attendance_statistics);
        setSupportActionBar(toolBar);
        setStatusBar();
        ButterKnife.bind(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
            Toast.makeText(this, "您还没申请权限!", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            Toast.makeText(this, "您已经申请了权限!", Toast.LENGTH_SHORT).show();
        }
//        swipRefresh.stopNestedScroll();
        llContent = findViewById(R.id.ll_content);
        curUser = CApplication.getInstance().getCurrentUser();
        dialog = new LoadingDialog(AllAttendanceStatisticsActivity.this, "玩命加载中...");
        //显示Dialog
        dialog.show();
        tvUserDept.setText(curUser.getDeptname());
        String curDate = getCurDateAndTime("yyyy年MM月dd日");
        tvDateClock.setText(curDate);
        tvTitle.setText("考勤统计");
        allAttendanceStatisticsPresenter = new AllAttendanceStatisticsPresenter(this);
        initData();
    }

    private void initData() {
        allAttendanceStatisticsPresenter.doAttendanceDataForPrams();
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

    @OnClick({R.id.rl_attendance_statistics_days, R.id.rl_attendance_statistics_miss, R.id.rl_attendance_statistics_go_out, R.id.rl_attendance_statistics_leave, R.id.rl_attendance_statistics_late, R.id.rl_attendance_statistics_leave_early, R.id.btn_build_excle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_attendance_statistics_days:

                if (intentDaysAttendanceDataBeanLists == null) {
                    Log.e("attendanceDataBeanList：", "考勤天数");
                } else {
                    Log.e("attendanceDataBeanList：", "" + intentDaysAttendanceDataBeanLists.size());
                    Log.e("attendanceDataBeanList：", "" + intentDaysAttendanceDataBeanLists.size());
                }
                Intent intent = new Intent(AllAttendanceStatisticsActivity.this, AllAttendanceStatisticsDetailActivity.class);
                intent.putExtra("考勤详情类型", "考勤天数");
                intent.putExtra("考勤详情数据", (Serializable) intentDaysAttendanceDataBeanLists);

                startActivity(intent);
//                AllAttendanceStatisticsDetailActivity
//                showPromptDownloadDialog();
                break;
            case R.id.rl_attendance_statistics_miss:
                Intent missIntent = new Intent(AllAttendanceStatisticsActivity.this, AllAttendanceStatisticsDetailActivity.class);
                missIntent.putExtra("考勤详情类型", "缺卡统计");
                missIntent.putExtra("考勤详情数据", (Serializable) intentMissCardAttendanceDataBeanLists);
                startActivity(missIntent);


//                Intent intent2 = new Intent(AllAttendanceStatisticsActivity.this,AllAttendanceStatisticsDetailActivity.class);
//                startActivity(intent2);
                break;
            case R.id.rl_attendance_statistics_leave:
                Intent leaveIntent = new Intent(AllAttendanceStatisticsActivity.this, AllAttendanceStatisticsDetailActivity.class);
                leaveIntent.putExtra("考勤详情类型", "请假统计");
                leaveIntent.putExtra("考勤详情数据", (Serializable) intentLeaveAttendanceDataBeanLists);
                startActivity(leaveIntent);

                break;
            case R.id.rl_attendance_statistics_go_out:
                Intent goOutIntent = new Intent(AllAttendanceStatisticsActivity.this, AllAttendanceStatisticsDetailActivity.class);
                goOutIntent.putExtra("考勤详情类型", "外出统计");
                goOutIntent.putExtra("考勤详情数据", (Serializable) intentGoOutAttendanceDataBeanLists);
                startActivity(goOutIntent);

                break;
            case R.id.rl_attendance_statistics_late:
                Intent lateIntent = new Intent(AllAttendanceStatisticsActivity.this, AllAttendanceStatisticsDetailActivity.class);
                lateIntent.putExtra("考勤详情类型", "迟到统计");
                lateIntent.putExtra("考勤详情数据", (Serializable) intentLateAttendanceDataBeanLists);
                startActivity(lateIntent);

                break;
            case R.id.rl_attendance_statistics_leave_early:
                Intent leaveEarlyIntent = new Intent(AllAttendanceStatisticsActivity.this, AllAttendanceStatisticsDetailActivity.class);
                leaveEarlyIntent.putExtra("考勤详情类型", "早退统计");
                leaveEarlyIntent.putExtra("考勤详情数据", (Serializable) intentLeaveEarlyAttendanceDataBeanLists);
                startActivity(leaveEarlyIntent);

                break;
            case R.id.btn_build_excle:
                exportExcel(this);
                break;
        }
    }

    /**
     * 导出Excel表格
     * @param context
     */
    private void exportExcel(Context context) {

        String filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+ "/AndroidExcelDemo");
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }

        String excelFileName ="/"+getCurDateAndTime("yyyy年MM月")+ "考勤统计表.xls";

        String[] title = {"姓名", "部门", "出勤天数", "缺卡", "请假", "迟到", "早退"};
        String sheetName = getCurDateAndTime("yyyy年MM月")+"考勤统计";
        List<DemoBean> demoBeanList = new ArrayList<>();
        for (int i=0;i<intentDaysAttendanceDataBeanLists.size();i++){
            DemoBean demoBean1 = new DemoBean(intentDaysAttendanceDataBeanLists.get(i).getUsername(),
                    intentDaysAttendanceDataBeanLists.get(i).getDeptname(),
                    String.valueOf(intentDaysAttendanceDataBeanLists.size()),
                    String.valueOf(missCardTimes),
                    String.valueOf(intentLeaveAttendanceDataBeanLists.size()),
                    String.valueOf(lateCardTimes),
                    String.valueOf(leaveEarlyCardTimes));
            demoBeanList.add(demoBean1);
        }
        filePath = filePath + excelFileName;

        ExcelUtil.initExcel(filePath, sheetName, title);

        ExcelUtil.writeObjListToExcel(demoBeanList, filePath, context);
        Toast.makeText(AllAttendanceStatisticsActivity.this,"excel已导出至：" + filePath,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResultToPData() {

        List<AttendanceShiftsBean> shiftsBeanList = LitePal.findAll(AttendanceShiftsBean.class);
        if (shiftsBeanList != null) {
            if (shiftsBeanList.size() != 0) {
                AttendanceShiftsBean attendanceShift = shiftsBeanList.get(0);
                processData(attendanceShift, "", "", "", "");
            }
        }
        Log.e("", "");

    }

    /**
     * 对数据进行加工，得到各种统计数据
     *
     * @param attendanceShiftsBean
     */
    private void processData(AttendanceShiftsBean attendanceShiftsBean, String orgName, String deptName, String startDate, String endDate) {
        setDefaultTime();
        int curShiftType = judgeCurShiftType(getCurDateAndTime("HH:mm:ss"));

        List<AttendanceDataBean> attendanceDataBeanListAll = LitePal.findAll(AttendanceDataBean.class);
        Log.e("sss", "" + attendanceDataBeanListAll.size());
        intentDaysAttendanceDataBeanLists.clear();
        intentLeaveAttendanceDataBeanLists.clear();
        intentMissCardAttendanceDataBeanLists.clear();
        intentLateAttendanceDataBeanLists.clear();
        intentLeaveEarlyAttendanceDataBeanLists.clear();
        //获取用户当月的考勤数据
        if (orgName.equals("") && deptName.equals("") && startDate.equals("") && endDate.equals("")) {

            intentDaysAttendanceDataBeanLists = LitePal.where("date=? and deptid=?", getCurDateAndTime("yyyy-MM-dd"), curUser.getDeptid()).find(AttendanceDataBean.class);
            Log.e("attendanceDataBeanList：", "" + intentDaysAttendanceDataBeanLists.size());
            Log.e("attendanceDataBeanList：", "" + intentDaysAttendanceDataBeanLists.size());
            //获取请假的数据
            List<AttendanceDataBean> attendanceDataBeanListLeave = LitePal.where("date=? and state=?", getCurDateAndTime("yyyy-MM-dd"), "请假").find(AttendanceDataBean.class);
            intentLeaveAttendanceDataBeanLists = attendanceDataBeanListLeave;
            String leaveTimes = String.valueOf(attendanceDataBeanListLeave.size());
            Log.e("请假次数：", "" + attendanceDataBeanListLeave.size());
            itemTvAttendanceStatisticsLeavePeopleNumber.setText(leaveTimes + "人");
        } else {
            if (deptName.equals("所有部门")) {
                intentDaysAttendanceDataBeanLists = LitePal.where("date>=? and date<=? and orgname=? ", startDate, endDate, orgName).find(AttendanceDataBean.class);
            } else {
                intentDaysAttendanceDataBeanLists = LitePal.where("date>=? and date<=? and orgname=? and deptname=?", startDate, endDate, orgName, deptName).find(AttendanceDataBean.class);
            }
            Log.e("attendanceDataBeanList：", "" + intentDaysAttendanceDataBeanLists.size());
            Log.e("attendanceDataBeanList：", "" + intentDaysAttendanceDataBeanLists.size());
            //获取请假的数据
            if (deptName.equals("所有部门")) {
                List<AttendanceDataBean> attendanceDataBeanListLeave = LitePal.where("date>=? and date<=? and orgname=? and state=?", startDate, endDate, orgName, "请假").find(AttendanceDataBean.class);
                intentLeaveAttendanceDataBeanLists = attendanceDataBeanListLeave;
                String leaveTimes = String.valueOf(attendanceDataBeanListLeave.size());
                Log.e("请假次数：", "" + attendanceDataBeanListLeave.size());
                itemTvAttendanceStatisticsLeavePeopleNumber.setText(leaveTimes + "人");
            } else {
                List<AttendanceDataBean> attendanceDataBeanListLeave = LitePal.where("date>=? and date<=? and orgname=? and deptname=? and state=?", startDate, endDate, orgName, deptName, "请假").find(AttendanceDataBean.class);
                intentLeaveAttendanceDataBeanLists = attendanceDataBeanListLeave;
                String leaveTimes = String.valueOf(attendanceDataBeanListLeave.size());
                Log.e("请假次数：", "" + attendanceDataBeanListLeave.size());
                itemTvAttendanceStatisticsLeavePeopleNumber.setText(leaveTimes + "人");
            }

        }

        String shiftTimes = attendanceShiftsBean.getShift();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        switch (shiftTimes) {
            case "2":
                try {
                    //上班时间
                    String toWorkTime = getCurDateAndTime("yyyy-MM-dd") + " " + attendanceShiftsBean.getSworktime();
                    Date toWorkStandardDate = dateFormat.parse(toWorkTime);
                    //下班时间
                    String workFinishTime = getCurDateAndTime("yyyy-MM-dd") + " " + attendanceShiftsBean.getSbacktime();
                    Date workFinishStandardDate = dateFormat.parse(workFinishTime);
                    for (int i = 0; i < attendanceDataBeanList.size(); i++) {
                        String ceshi = attendanceDataBeanList.get(i).getSsignintimte();
                        String ceshi2 = attendanceDataBeanList.get(i).getSsignbacktime();
                        if (ceshi != null && ceshi2 != null) {
                            if (ceshi.equals("null") | ceshi2.equals("null")) {
                                missCardTimes = ++missCardTimes;
                            } else {
                                Log.e("ceshi", "" + ceshi);
                                Date toWorkDate = dateFormat.parse(attendanceDataBeanList.get(i).getSsignintimte());
                                Date workFinishDate = dateFormat.parse(attendanceDataBeanList.get(i).getSsignbacktime());
                                if (toWorkDate.getTime() > toWorkStandardDate.getTime()) {
                                    lateCardTimes = ++lateCardTimes;
                                    Log.e("lateCardTimes", "" + lateCardTimes);
                                }
                                if (workFinishDate.getTime() < workFinishStandardDate.getTime()) {
                                    leaveEarlyCardTimes = ++leaveEarlyCardTimes;
                                }
                            }
                        }
                    }
                    String lateCardTimes1 = String.valueOf(lateCardTimes);
                    Log.e("lateCardTimes1", "" + lateCardTimes1);
                    String leaveEarlyCardTimes1 = String.valueOf(leaveEarlyCardTimes);
                    Log.e("lateCardTimes1", "" + leaveEarlyCardTimes1);
                    //出勤天数
                    itemTvAttendanceStatisticsPeopleNumber.setText(String.valueOf(attendanceDataBeanList.size()) + "次");
                    //缺卡
                    itemTvAttendanceStatisticsMissPeopleNumber.setText(String.valueOf(missCardTimes) + "次");
                    //迟到
                    itemTvAttendanceStatisticsLatePeopleNumber.setText(String.valueOf(lateCardTimes) + "次");
                    //早退
                    itemTvAttendanceStatisticsLeaveEarlyPeopleNumber.setText(String.valueOf(leaveEarlyCardTimes) + "次");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case "4":

                try {
                    //上午上班时间
                    String toWorkMornningTime = getCurDateAndTime("yyyy-MM-dd") + " " + attendanceShiftsBean.getSworktime();
                    Date toWorkMornningStandardDate = dateFormat.parse(toWorkMornningTime);
                    //上午下班时间
                    String workFinishMorningTime = getCurDateAndTime("yyyy-MM-dd") + " " + attendanceShiftsBean.getSbacktime();
                    Date workFinishStandardMorningDate = dateFormat.parse(workFinishMorningTime);
                    //下午上班时间
                    String toWorkAfternoonTime = getCurDateAndTime("yyyy-MM-dd") + " " + attendanceShiftsBean.getXworktime();
                    Date toWorkStandardAfternoonDate = dateFormat.parse(toWorkAfternoonTime);
                    //下午下班时间
                    String workFinishAfternoonTime = getCurDateAndTime("yyyy-MM-dd") + " " + attendanceShiftsBean.getXbacktime();
                    Date workFinishStandardAfternoonDate = dateFormat.parse(workFinishAfternoonTime);
                    for (int i = 0; i < intentDaysAttendanceDataBeanLists.size(); i++) {
                        String ceshi = intentDaysAttendanceDataBeanLists.get(i).getSsignintimte();
                        String ceshi2 = intentDaysAttendanceDataBeanLists.get(i).getSsignbacktime();
                        String ceshi3 = intentDaysAttendanceDataBeanLists.get(i).getXsignintimte();
                        String ceshi4 = intentDaysAttendanceDataBeanLists.get(i).getXsignbacktime();
                        if (ceshi != null && ceshi2 != null && ceshi3 != null && ceshi4 != null) {
                            //设置部门的考勤缺卡状态
                            if (ceshi.equals("null")) {
                                intentDaysAttendanceDataBeanLists.get(i).setAmbstate("缺卡");
                            }
                            if (ceshi2.equals("null")) {
                                intentDaysAttendanceDataBeanLists.get(i).setAmestate("缺卡");
                            }
                            if (ceshi3.equals("null")) {
                                intentDaysAttendanceDataBeanLists.get(i).setPmbstate("缺卡");
                            }
                            if (ceshi4.equals("null")) {
                                intentDaysAttendanceDataBeanLists.get(i).setPmestate("缺卡");
                            }
                            //将缺卡状态下的考勤信息收集，以人为单位
                            if (ceshi.equals("null")) {
                                missCardTimes = ++missCardTimes;
                                if (!intentMissCardAttendanceDataBeanLists.contains(intentDaysAttendanceDataBeanLists.get(i))) {
                                    intentMissCardAttendanceDataBeanLists.add(intentDaysAttendanceDataBeanLists.get(i));

                                }
                            } else if (ceshi2.equals("null")) {
                                missCardTimes = ++missCardTimes;
                                if (!intentMissCardAttendanceDataBeanLists.contains(intentDaysAttendanceDataBeanLists.get(i))) {

                                    intentMissCardAttendanceDataBeanLists.add(intentDaysAttendanceDataBeanLists.get(i));
                                }

                            } else if (ceshi3.equals("null")) {
                                missCardTimes = ++missCardTimes;
                                if (!intentMissCardAttendanceDataBeanLists.contains(intentDaysAttendanceDataBeanLists.get(i))) {

                                    intentMissCardAttendanceDataBeanLists.add(intentDaysAttendanceDataBeanLists.get(i));
                                }

                            } else if (ceshi4.equals("null")) {
                                missCardTimes = ++missCardTimes;
                                if (!intentMissCardAttendanceDataBeanLists.contains(intentDaysAttendanceDataBeanLists.get(i))) {

                                    intentMissCardAttendanceDataBeanLists.add(intentDaysAttendanceDataBeanLists.get(i));
                                }

                            } else {
                                //设置部门的考勤缺卡状态
                                if (ceshi.equals("外出")) {
                                    intentDaysAttendanceDataBeanLists.get(i).setAmbstate("外出");
                                }
                                if (ceshi2.equals("外出")) {
                                    intentDaysAttendanceDataBeanLists.get(i).setAmestate("外出");
                                }
                                if (ceshi3.equals("外出")) {
                                    intentDaysAttendanceDataBeanLists.get(i).setPmbstate("外出");
                                }
                                if (ceshi4.equals("外出")) {
                                    intentDaysAttendanceDataBeanLists.get(i).setPmestate("外出");
                                }
                                //将外出状态下的考勤信息收集，以人为单位
                                if (ceshi.equals("外出")) {
                                    goOutTimes = ++goOutTimes;
                                    if (!intentGoOutAttendanceDataBeanLists.contains(intentDaysAttendanceDataBeanLists.get(i))) {
                                        intentGoOutAttendanceDataBeanLists.add(intentDaysAttendanceDataBeanLists.get(i));

                                    }
                                } else if (ceshi2.equals("外出")) {
                                    goOutTimes = ++goOutTimes;
                                    if (!intentGoOutAttendanceDataBeanLists.contains(intentDaysAttendanceDataBeanLists.get(i))) {

                                        intentGoOutAttendanceDataBeanLists.add(intentDaysAttendanceDataBeanLists.get(i));
                                    }

                                } else if (ceshi3.equals("外出")) {
                                    goOutTimes = ++goOutTimes;
                                    if (!intentGoOutAttendanceDataBeanLists.contains(intentDaysAttendanceDataBeanLists.get(i))) {

                                        intentGoOutAttendanceDataBeanLists.add(intentDaysAttendanceDataBeanLists.get(i));
                                    }

                                } else if (ceshi4.equals("外出")) {
                                    goOutTimes = ++goOutTimes;
                                    if (!intentGoOutAttendanceDataBeanLists.contains(intentDaysAttendanceDataBeanLists.get(i))) {

                                        intentGoOutAttendanceDataBeanLists.add(intentDaysAttendanceDataBeanLists.get(i));
                                    }

                                } else {
                                    Log.e("ceshi", "" + ceshi);
                                    //设置每次打卡状态：迟到、早退
                                    if (!intentDaysAttendanceDataBeanLists.get(i).getSsignintimte().equals("null")) {
                                        toWorkMornningDate = dateFormat.parse(intentDaysAttendanceDataBeanLists.get(i).getSsignintimte());
                                        if (toWorkMornningDate.getTime() > toWorkMornningStandardDate.getTime()) {
                                            intentDaysAttendanceDataBeanLists.get(i).setAmbstate("迟到");
                                        }
                                    }
                                    if (!intentDaysAttendanceDataBeanLists.get(i).getSsignbacktime().equals("null")) {
                                        workFinishMornningDate = dateFormat.parse(intentDaysAttendanceDataBeanLists.get(i).getSsignbacktime());
                                        if (workFinishMornningDate.getTime() < workFinishStandardMorningDate.getTime()) {
                                            intentDaysAttendanceDataBeanLists.get(i).setAmestate("早退");
                                        }
                                    }
                                    if (!intentDaysAttendanceDataBeanLists.get(i).getXsignintimte().equals("null")) {
                                        toWorkAfternoonDate = dateFormat.parse(intentDaysAttendanceDataBeanLists.get(i).getXsignintimte());
                                        if (toWorkAfternoonDate.getTime() > toWorkStandardAfternoonDate.getTime()) {
                                            intentDaysAttendanceDataBeanLists.get(i).setPmbstate("迟到");
                                        }
                                    }
                                    if (!intentDaysAttendanceDataBeanLists.get(i).getXsignbacktime().equals("null")) {
                                        workFinishAfternoonDate = dateFormat.parse(intentDaysAttendanceDataBeanLists.get(i).getXsignbacktime());
                                        if (workFinishAfternoonDate.getTime() < workFinishStandardAfternoonDate.getTime()) {
                                            intentDaysAttendanceDataBeanLists.get(i).setPmestate("早退");
                                        }
                                    }
                                    //收集分类各种状态下的考勤信息
                                    if (!intentDaysAttendanceDataBeanLists.get(i).getSsignintimte().equals("null")) {
                                        toWorkMornningDate = dateFormat.parse(intentDaysAttendanceDataBeanLists.get(i).getSsignintimte());
                                        if (toWorkMornningDate.getTime() > toWorkMornningStandardDate.getTime()) {
                                            lateCardTimes = ++lateCardTimes;
                                            if (!intentLateAttendanceDataBeanLists.contains(intentDaysAttendanceDataBeanLists.get(i))) {
                                                intentLateAttendanceDataBeanLists.add(intentDaysAttendanceDataBeanLists.get(i));
                                            }

                                            Log.e("lateCardTimes", "" + lateCardTimes);
                                        }
                                    }
                                    if (!intentDaysAttendanceDataBeanLists.get(i).getSsignbacktime().equals("null")) {
                                        workFinishMornningDate = dateFormat.parse(intentDaysAttendanceDataBeanLists.get(i).getSsignbacktime());
                                        if (workFinishMornningDate.getTime() < workFinishStandardMorningDate.getTime()) {
                                            leaveEarlyCardTimes = ++leaveEarlyCardTimes;
                                            if (!intentLeaveEarlyAttendanceDataBeanLists.contains(intentDaysAttendanceDataBeanLists.get(i))) {
                                                intentLeaveEarlyAttendanceDataBeanLists.add(intentDaysAttendanceDataBeanLists.get(i));
                                            }
                                        }
                                    }
                                    if (!intentDaysAttendanceDataBeanLists.get(i).getXsignintimte().equals("null")) {
                                        toWorkAfternoonDate = dateFormat.parse(intentDaysAttendanceDataBeanLists.get(i).getXsignintimte());
                                        if (toWorkAfternoonDate.getTime() > toWorkStandardAfternoonDate.getTime()) {
                                            lateCardTimes = ++lateCardTimes;
                                            if (!intentLateAttendanceDataBeanLists.contains(intentDaysAttendanceDataBeanLists.get(i))) {

                                                intentLateAttendanceDataBeanLists.add(intentDaysAttendanceDataBeanLists.get(i));
                                            }
                                            Log.e("lateCardTimes", "" + lateCardTimes);
                                        }
                                    }
                                    if (!intentDaysAttendanceDataBeanLists.get(i).getXsignbacktime().equals("null")) {
                                        workFinishAfternoonDate = dateFormat.parse(intentDaysAttendanceDataBeanLists.get(i).getXsignbacktime());
                                        if (workFinishAfternoonDate.getTime() < workFinishStandardAfternoonDate.getTime()) {
                                            leaveEarlyCardTimes = ++leaveEarlyCardTimes;
                                            if (!intentLeaveEarlyAttendanceDataBeanLists.contains(intentDaysAttendanceDataBeanLists.get(i))) {

                                                intentLeaveEarlyAttendanceDataBeanLists.add(intentDaysAttendanceDataBeanLists.get(i));
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                    String lateCardTimes1 = String.valueOf(lateCardTimes);
                    Log.e("lateCardTimes1", "" + lateCardTimes1);
                    String leaveEarlyCardTimes1 = String.valueOf(leaveEarlyCardTimes);
                    Log.e("lateCardTimes1", "" + leaveEarlyCardTimes1);
                    //出勤天数
                    itemTvAttendanceStatisticsPeopleNumber.setText(String.valueOf(intentDaysAttendanceDataBeanLists.size()) + "次");
                    itemTvAttendanceStatisticsGoOutPeopleNumber.setText(String.valueOf(goOutTimes) + "人");
                    //缺卡
                    itemTvAttendanceStatisticsMissPeopleNumber.setText(String.valueOf(missCardTimes) + "人");
                    //迟到
                    itemTvAttendanceStatisticsLatePeopleNumber.setText(String.valueOf(lateCardTimes) + "人/次");
                    //早退
                    itemTvAttendanceStatisticsLeaveEarlyPeopleNumber.setText(String.valueOf(leaveEarlyCardTimes) + "人/次");
                    dialog.close();
                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.e("个人考勤统计报错：", e.toString());
                }

                break;
            default:
        }


    }


    /**
     * 判断当前卡的类型
     *
     * @param curDate
     * @return
     */
    private int judgeCurShiftType(String curDate) {
        //年-月-日 时-分
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        try {
            //当前时间
            Date date1 = dateFormat.parse(curDate);
            String MorningWorkClockEndTime = "09:00:00";
            String MorningWorkFinishColokEndTime = "12:00:00";
            //上午卡截止时间
            String MorningWorkedClockEndTime = "13:00:00";
            String AfternoonWorkClockEndTime = "15:00:00";
            String AfternoonWorkFinishColokEndTime = "18:00:00";
            //上午上班打卡结束时间
            Date date2 = dateFormat.parse(MorningWorkClockEndTime);
            //上午下班打卡结束时间
            Date date3 = dateFormat.parse(MorningWorkFinishColokEndTime);
            //上午打卡结束时间
            Date date4 = dateFormat.parse(MorningWorkedClockEndTime);
            //下午上班打卡结束时间
            Date date5 = dateFormat.parse(AfternoonWorkClockEndTime);
            //下午下班打卡结束时间
            Date date6 = dateFormat.parse(AfternoonWorkFinishColokEndTime);

            if (date2.getTime() > date1.getTime()) {
                //打卡时间位置上班时间以前：属于当前无班次类型
                shiftType = 0;
            } else if (date3.getTime() > date1.getTime()) {
                //打卡时间位于上午下班以前：属于上午上班卡
                shiftType = 1;
            } else if (date4.getTime() > date1.getTime()) {
                //打卡时间位于下午上班以前：属于上午下班卡
                shiftType = 2;
            } else if (date5.getTime() > date1.getTime()) {
                //打卡时间位于下午下班以前：属于上午上班卡
                shiftType = 2;
            } else if (date6.getTime() > date1.getTime()) {
                //打卡时间位于下午下班以前：属于下午上班卡
                shiftType = 3;
                String bz = "属于下午上班迟到卡";
            } else {
                //打卡时间位于下午下班以前：属于下午下午下班卡
                shiftType = 4;
            }
            return shiftType;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public void requestSmartData(String orgName, String deptname, String start_date, String end_date) {

        List<AttendanceShiftsBean> shiftsBeanList = LitePal.findAll(AttendanceShiftsBean.class);
        if (shiftsBeanList != null) {
            if (shiftsBeanList.size() != 0) {
                try {
                    AttendanceShiftsBean attendanceShift = shiftsBeanList.get(0);
                    missCardTimes = 0;
                    leaveCardTimes = 0;
                    lateCardTimes = 0;
                    leaveEarlyCardTimes = 0;
                    tvUserDept.setText(deptname);
                    //年-月-日 时-分
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date startDate = dateFormat.parse(start_date);
                    Date endDate = dateFormat.parse(end_date);
                    if (startDate.getTime() == endDate.getTime()) {
                        tvDateClock.setText(start_date);
                    } else {
                        tvDateClock.setText(start_date + "至" + end_date);
                    }


                    processData(attendanceShift, orgName, deptname, start_date, end_date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        }
        Log.e("", "");

    }

    /**
     * 设置默认班次
     */
    private void setDefaultShift() {


    }


    /**
     * 显示详情的弹窗
     */
    private void showPromptDownloadDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    AllAttendanceStatisticsActivity.this);
            builder.setTitle("下载");
            builder.setMessage("确认要下载附件吗？");
            builder.setPositiveButton("下载",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setCancelable(false);
            builder.create().show();
        } catch (Throwable e) {
            e.printStackTrace();
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

    @Override
    public void onResultForError() {

    }

    @OnClick(R.id.btn_select)
    public void onViewClicked() {
        drawerLayout.openDrawer(GravityCompat.END);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PERMISSION_GRANTED) {//选择了“始终允许”
                    Toast.makeText(this, "" + "权限" + permissions[i] + "申请成功", Toast.LENGTH_SHORT).show();
                } else {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {//用户选择了禁止不再询问

                        AlertDialog.Builder builder = new AlertDialog.Builder(AllAttendanceStatisticsActivity.this);
                        builder.setTitle("permission")
                                .setMessage("点击允许才可以使用我们的app哦")
                                .setPositiveButton("去允许", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (mDialog != null && mDialog.isShowing()) {
                                            mDialog.dismiss();
                                        }
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", getPackageName(), null);//注意就是"package",不用改成自己的包名
                                        intent.setData(uri);
                                        startActivityForResult(intent, NOT_NOTICE);
                                    }
                                });
                        mDialog = builder.create();
                        mDialog.setCanceledOnTouchOutside(false);
                        mDialog.show();


                    } else {//选择禁止
                        AlertDialog.Builder builder = new AlertDialog.Builder(AllAttendanceStatisticsActivity.this);
                        builder.setTitle("permission")
                                .setMessage("点击允许才可以使用我们的app哦")
                                .setPositiveButton("去允许", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (alertDialog != null && alertDialog.isShowing()) {
                                            alertDialog.dismiss();
                                        }
                                        ActivityCompat.requestPermissions(AllAttendanceStatisticsActivity.this,
                                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                                    }
                                });
                        alertDialog = builder.create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();
                    }

                }
            }
        }
    }

    private void showDialogTipUserRequestPermission() {
        ActivityCompat.requestPermissions(this, permissions, 321);
    }


}
