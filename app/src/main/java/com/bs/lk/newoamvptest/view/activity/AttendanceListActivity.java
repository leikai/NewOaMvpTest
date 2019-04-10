package com.bs.lk.newoamvptest.view.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.CoordUtil;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.inner.Point;
import com.baidu.mapapi.utils.DistanceUtil;
import com.bs.lk.newoamvptest.CApplication;
import com.bs.lk.newoamvptest.R;
import com.bs.lk.newoamvptest.bean.AttendanceDataBean;
import com.bs.lk.newoamvptest.bean.AttendanceResultBean;
import com.bs.lk.newoamvptest.bean.AttendanceShiftsBean;
import com.bs.lk.newoamvptest.bean.UserNewBean;
import com.bs.lk.newoamvptest.presenter.AttendanceListPresenter;
import com.bs.lk.newoamvptest.util.BitmapUtil;
import com.bs.lk.newoamvptest.widget.LoadingDialog;

import org.litepal.LitePal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 考勤列表页
 *
 * @author lk
 */
public class AttendanceListActivity extends AppCompatActivity implements SensorEventListener, View.OnClickListener,
        IAttendanceListView {

    @BindView(R.id.tv_morning_work_card)
    TextView tvMorningWorkCard;
    @BindView(R.id.tv_morning_work_card_time)
    TextView tvMorningWorkCardTime;
    @BindView(R.id.tv_morning_work_card_address)
    TextView tvMorningWorkCardAddress;
    @BindView(R.id.tv_morning_work_finish_card)
    TextView tvMorningWorkFinishCard;
    @BindView(R.id.tv_morning_work_finish_card_time)
    TextView tvMorningWorkFinishCardTime;
    @BindView(R.id.tv_morning_work_finish_card_address)
    TextView tvMorningWorkFinishCardAddress;
    @BindView(R.id.tv_afternoon_work_card)
    TextView tvAfternoonWorkCard;
    @BindView(R.id.tv_afternoon_work_card_time)
    TextView tvAfternoonWorkCardTime;
    @BindView(R.id.tv_afternoon_work_card_address)
    TextView tvAfternoonWorkCardAddress;
    @BindView(R.id.tv_afternoon_work_finish_card)
    TextView tvAfternoonWorkFinishCard;
    @BindView(R.id.tv_afternoon_work_finish_card_time)
    TextView tvAfternoonWorkFinishCardTime;
    @BindView(R.id.tv_afternoon_work_finish_card_address)
    TextView tvAfternoonWorkFinishCardAddress;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.iv_user_head)
    ImageView ivUserHead;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_user_department)
    TextView tvUserDepartment;
    @BindView(R.id.tv_date_clock)
    TextView tvDateClock;
    @BindView(R.id.civ_gry_morning_work_attendance)
    CircleImageView civGryMorningWorkAttendance;
    @BindView(R.id.civ_blue_morning_work_attendance)
    CircleImageView civBlueMorningWorkAttendance;
    @BindView(R.id.civ_gry_morning_work_finish_attendance)
    CircleImageView civGryMorningWorkFinishAttendance;
    @BindView(R.id.civ_blue_morning_work_finish_attendance)
    CircleImageView civBlueMorningWorkFinishAttendance;
    @BindView(R.id.civ_gry_afternoon_work_attendance)
    CircleImageView civGryAfternoonWorkAttendance;
    @BindView(R.id.civ_blue_afternoon_work_finish_attendance)
    CircleImageView civBlueAfternoonWorkFinishAttendance;
    @BindView(R.id.civ_gry_afternoon_work_finish_attendance)
    CircleImageView civGryAfternoonWorkFinishAttendance;
    @BindView(R.id.civ_blue_afternoon_work_finish_finish_attendance)
    CircleImageView civBlueAfternoonWorkFinishFinishAttendance;
    @BindView(R.id.arriver_timetv_morning_work_card)
    TextView arriverTimetvMorningWorkCard;
    @BindView(R.id.arriver_bt_morning_work_card)
    RelativeLayout arriverBtMorningWorkCard;
    @BindView(R.id.arriver_timetv_morning_work_finish_card)
    TextView arriverTimetvMorningWorkFinishCard;
    @BindView(R.id.arriver_bt_morning_work_finish_card)
    RelativeLayout arriverBtMorningWorkFinishCard;
    @BindView(R.id.arriver_timetv_afternoon_work_card)
    TextView arriverTimetvAfternoonWorkCard;
    @BindView(R.id.arriver_bt_afternoon_work_card)
    RelativeLayout arriverBtAfternoonWorkCard;
    @BindView(R.id.arriver_timetv_afternoon_work_finish_card)
    TextView arriverTimetvAfternoonWorkFinishCard;
    @BindView(R.id.arriver_bt_afternoon_work_finish_card)
    RelativeLayout arriverBtAfternoonWorkFinishCard;
    @BindView(R.id.mapview)
    MapView mapview;
    @BindView(R.id.distance_tv)
    TextView distanceTv;
    @BindView(R.id.arriver_timetv)
    TextView arriverTimetv;
    @BindView(R.id.arriver_bt)
    RelativeLayout arriverBt;
    @BindView(R.id.ll_morning_to_work_card)
    LinearLayout llMorningToWorkCard;
    @BindView(R.id.ll_morning_work_finish_card)
    LinearLayout llMorningWorkFinishCard;
    @BindView(R.id.ll_afternoon_to_work_card)
    LinearLayout llAfternoonToWorkCard;
    @BindView(R.id.ll_afternoon_work_finish_card)
    LinearLayout llAfternoonWorkFinishCard;
    @BindView(R.id.tv_morning_work_card_update)
    TextView tvMorningWorkCardUpdate;
    @BindView(R.id.tv_morning_work_finish_card_update)
    TextView tvMorningWorkFinishCardUpdate;
    @BindView(R.id.tv_afternoon_work_card_update)
    TextView tvAfternoonWorkCardUpdate;
    @BindView(R.id.tv_afternoon_work_finish_card_update)
    TextView tvAfternoonWorkFinishCardUpdate;

    /**
     * 打卡时间
     */
    private String clockTime;
    /**
     * 打卡地址
     */
    private String curLocationInfor;
    /**
     * 打卡状态
     */
    private String clockState;
    /**
     * 打卡结果
     */
    private String clockResult;
    /**
     * 打卡类型
     */
    private int clockType;


    /**
     * 规定到达距离范围距离
     */
    private int distance = 200;
    public LocationClient mLocationClient;
    private BaiduMap baiduMap;
    /**
     * 方向传感器
     */
    private SensorManager mSensorManager;
    /**
     * 目的地坐标点
     */
    private LatLng mDestinationPoint;
    /**
     * 地图文字位置提醒
     */
    private InfoWindow mInfoWindow;
    private boolean isFirstLocate = true;
    private Double lastX = 0.0;
    private int mCurrentDirection = 0;
    /**
     * 缩放级别的次数数量
     */
    private int mLevelNum = 18;

    /**
     * 定位坐标
     */
    private MyLocationData locData;
    private LatLng mCenterPos;
    private double mDistance = 0;
    /**
     * 比例
     */
    private float mZoomScale = 0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private final String TAG = "AttendanceListActivity";
    /**
     * 当前用户
     */
    private UserNewBean curUser;
    private AttendanceListPresenter attendanceListPresenter;
    /**
     * 是否使用特殊的标题栏背景颜色，android5.0以上可以设置状态栏背景色，如果不使用则使用透明色值
     */
    protected boolean useThemestatusBarColor = false;
    /**
     * 是否使用状态栏文字和图标为暗色，如果状态栏采用了白色系，则需要使状态栏和图标为暗色，android6.0以上可以设置
     */
    protected boolean useStatusBarColor = true;

    /**
     * 上午上班卡时间
     */
    private String morningWorkCardTime;
    /**
     * 上午上班卡地点
     */
    private String morningWorkCardAddress;
    /**
     * 上午下班卡时间
     */
    private String morningWorkFinishCardTime;
    /**
     * 上午下班卡地点
     */
    private String morningWorkFinishCardAddress;
    /**
     * 下午上班卡时间
     */
    private String afternoonWorkCardTime;
    /**
     * 下午上班卡地点
     */
    private String afternoonWorkCardAddress;
    /**
     * 下午下班卡时间
     */
    private String afternoonWorkFinishCardTime;
    /**
     * 下午下班卡地点
     */
    private String afternoonWorkFinishCardAddress;

    /**
     * 加载中Dialog
     */
    private LoadingDialog dialog;

    /**
     * 数据库中的数值为"null"
     */
    private String dbValueNull="null";
    /**
     * 判断页面数据显示状态:"缺卡"
     */
    private String missName = "缺卡";
    /**
     * 操作失败
     */
    private String operationFailed = "操作失败";
    /**
     * 上午上班卡
     */
    private static int morningToWorkCarType = 1;
    /**
     * 上午上班卡
     */
    private static int morningWorkedCarType = 2;
    /**
     * 上午上班卡
     */
    private static int afternoonToWorkCarType = 3;
    /**
     * 上午上班卡
     */
    private static int afternoonWorkedCarType = 4;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationClient = new LocationClient(getApplicationContext());
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_attendance_list);
        setSupportActionBar(toolBar);
        setStatusBar();
        ButterKnife.bind(this);
        curUser = CApplication.getInstance().getCurrentUser();
        dialog=new LoadingDialog(AttendanceListActivity.this,"玩命加载中...");
        //显示Dialog
        dialog.show();
        attendanceListPresenter = new AttendanceListPresenter(this);
        initView();
        initBaiduMap();
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(AttendanceListActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(AttendanceListActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(AttendanceListActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(AttendanceListActivity.this, permissions, 1);
        } else {
            requestLocation();
        }
        //设置系统时间
        mHandler.post(run);
        arriverBt.setOnClickListener(this);
        arriverBtMorningWorkCard.setOnClickListener(this);
        arriverBtMorningWorkFinishCard.setOnClickListener(this);
        arriverBtAfternoonWorkCard.setOnClickListener(this);
        arriverBtAfternoonWorkFinishCard.setOnClickListener(this);
    }

    /**
     * 初始化页面数据
     */
    private void initView() {
        //初始化头部数据
        initHeadDataView();
        clockTime = arriverTimetv.getText().toString().trim();
    }

    /**
     * 初始化头部数据
     */
    private void initHeadDataView() {
        tvTitle.setText(curUser.getOrgname());
        Bitmap bp = BitmapUtil.GetUserImageByNickName(AttendanceListActivity.this, curUser.getEmpname());
        ivUserHead.setImageBitmap(bp);
        tvUserName.setText(curUser.getEmpname());
        tvUserDepartment.setText(curUser.getDeptname());
        String curDate = getCurDateAndTime("yyyy年MM月dd日");
        tvDateClock.setText(curDate);
    }

    /**
     * 从远程数据库获取此用户当天到目前为止的打卡数据
     */
    private void initData() {
        String curDate = getCurDateAndTime("yyyy-MM-dd");
        attendanceListPresenter.doAttendanceDataForPrams(curDate, curUser.getOid());

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
     * 初始化打卡部分的界面
     *
     * @param curCarType
     */
    private void initColockView(int curCarType) {
        if (curCarType == 1) {
            arriverBtMorningWorkCard.setVisibility(View.VISIBLE);
            arriverBtMorningWorkFinishCard.setVisibility(View.GONE);
            arriverBtAfternoonWorkCard.setVisibility(View.GONE);
            arriverBtAfternoonWorkFinishCard.setVisibility(View.GONE);
            civGryMorningWorkAttendance.setVisibility(View.GONE);
            civBlueMorningWorkAttendance.setVisibility(View.VISIBLE);
        } else if (curCarType == morningWorkedCarType) {
            arriverBtMorningWorkCard.setVisibility(View.GONE);
            arriverBtMorningWorkFinishCard.setVisibility(View.VISIBLE);
            arriverBtAfternoonWorkCard.setVisibility(View.GONE);
            arriverBtAfternoonWorkFinishCard.setVisibility(View.GONE);
            civGryMorningWorkFinishAttendance.setVisibility(View.GONE);
            civBlueMorningWorkFinishAttendance.setVisibility(View.VISIBLE);
            tvMorningWorkCardTime.setText("缺卡");
        } else if (curCarType == afternoonToWorkCarType) {
            arriverBtMorningWorkCard.setVisibility(View.GONE);
            arriverBtMorningWorkFinishCard.setVisibility(View.GONE);
            arriverBtAfternoonWorkCard.setVisibility(View.VISIBLE);
            arriverBtAfternoonWorkFinishCard.setVisibility(View.GONE);
            civGryAfternoonWorkAttendance.setVisibility(View.GONE);
            civBlueAfternoonWorkFinishAttendance.setVisibility(View.VISIBLE);
            tvMorningWorkCardTime.setText("缺卡");
            tvMorningWorkFinishCardTime.setText("缺卡");
        } else if (curCarType == afternoonWorkedCarType) {
            arriverBtMorningWorkCard.setVisibility(View.GONE);
            arriverBtMorningWorkFinishCard.setVisibility(View.GONE);
            arriverBtAfternoonWorkCard.setVisibility(View.GONE);
            arriverBtAfternoonWorkFinishCard.setVisibility(View.VISIBLE);
            civGryAfternoonWorkFinishAttendance.setVisibility(View.GONE);
            civBlueAfternoonWorkFinishFinishAttendance.setVisibility(View.VISIBLE);
            tvMorningWorkCardTime.setText("缺卡");
            tvMorningWorkFinishCardTime.setText("缺卡");
            tvAfternoonWorkCardTime.setText("缺卡");
        }

    }

    /**
     * 判断当前卡的类型
     *
     * @param curDate
     * @return
     */
    private int judgeCurCardType(String curDate) {
        //年-月-日 时-分
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        try {
            //当前时间
            Date date1 = dateFormat.parse(curDate);
            String morningworkclockendtime = "09:00:00";
            String morningworkfinishcolokendtime = "12:00:00";
            //上午卡截止时间
            String morningworkedclockendtime = "13:00:00";
            String afternoonworkclockendtime = "15:00:00";
            String afternoonworkfinishcolokendtime = "18:00:00";
            //上午上班打卡结束时间
            Date date2 = dateFormat.parse(morningworkclockendtime);
            //上午下班打卡结束时间
            Date date3 = dateFormat.parse(morningworkfinishcolokendtime);
            //上午打卡结束时间
            Date date4 = dateFormat.parse(morningworkedclockendtime);
            //下午上班打卡结束时间
            Date date5 = dateFormat.parse(afternoonworkclockendtime);
            //下午下班打卡结束时间
            Date date6 = dateFormat.parse(afternoonworkfinishcolokendtime);

            if (date2.getTime() > date1.getTime()) {
                //打卡时间位置上班时间以前：属于上午上班正常卡
                clockType = 1;
            } else if (date3.getTime() > date1.getTime()) {
                //打卡时间位于上午下班以前：属于上午上班迟到卡
                clockType = 1;
                String bz = "属于上午上班迟到卡";
            } else if (date4.getTime() > date1.getTime()) {
                //打卡时间位于下午上班以前：属于上午下班正常卡
                clockType = 2;
            } else if (date5.getTime() > date1.getTime()) {
                //打卡时间位于下午下班以前：属于下午上班正常卡
                clockType = 3;
            } else if (date6.getTime() > date1.getTime()) {
                //打卡时间位于下午下班以前：属于下午上班迟到卡
                clockType = 3;
                String bz = "属于下午上班迟到卡";
            } else {
                //打卡时间位于下午下班以前：属于下午下午下班正常卡
                clockType = 4;
            }
            return clockType;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 初始化百度地图
     */
    private void initBaiduMap() {
        baiduMap = mapview.getMap();
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        //开启定位图层
        baiduMap.setMyLocationEnabled(true);
        //获取传感器管理服务
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        tvTitle.setText("打卡签到");

    }


    private void navigateTo(BDLocation location) {
        if (isFirstLocate) {
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            baiduMap.animateMapStatus(update);
            //3-19值越大，地图显示的信息越详细
            update = MapStatusUpdateFactory.zoomTo(16f);
            baiduMap.animateMapStatus(update);
            isFirstLocate = false;
        }
        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.latitude(location.getLatitude());
        locationBuilder.longitude(location.getLongitude());
        MyLocationData locationData = locationBuilder.build();
        baiduMap.setMyLocationData(locationData);
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

    @Override
    protected void onResume() {
        super.onResume();
        mapview.onResume();
        //为系统的方向传感器注册监听
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_UI);
        String curDate = getCurDateAndTime("yyyy-MM-dd");
        initData();
    }

    /**
     * 根据查询到的数据布置考勤界面
     *
     * @param attendanceDataBean
     * @param curCarType
     */
    private void initColockView(AttendanceDataBean attendanceDataBean, int curCarType) {
        dialog.close();
        if (attendanceDataBean.getState() != null) {
            switch (attendanceDataBean.getState()) {
                case "":
                    switch (curCarType) {
                        case 1:
                            if (dbValueNull.equals(attendanceDataBean.getSsignintimte())){
                                viewToDispose(attendanceDataBean,false,false,false,false,true,false,false,false);
                            }else {
                                viewToDispose(attendanceDataBean,false,false,false,false,false,false,false,false);
                            }
                            if (missName.equals(tvMorningWorkCardTime.getText())){
                                tvMorningWorkCardTime.setText("");
                            }
                            if (missName.equals(tvMorningWorkFinishCardTime.getText())){
                                tvMorningWorkFinishCardTime.setText("");
                            }
                            if (missName.equals(tvAfternoonWorkCardTime.getText())){
                                tvAfternoonWorkCardTime.setText("");
                            }
                            if (missName.equals(tvAfternoonWorkFinishCardTime.getText())){
                                tvAfternoonWorkFinishCardTime.setText("");
                            }
                            break;
                        case 2:
                            if (dbValueNull.equals(attendanceDataBean.getSsignbacktime())){
                                viewToDispose(attendanceDataBean,true,false,
                                        false,false,false,true,false,false);
                            }else {
                                viewToDispose(attendanceDataBean,true,false,
                                        false,false,false,false,false,false);
                            }
                            if (missName.equals(tvMorningWorkFinishCardTime.getText())){
                                tvMorningWorkFinishCardTime.setText("");
                            }
                            if (missName.equals(tvAfternoonWorkCardTime.getText())){
                                tvAfternoonWorkCardTime.setText("");
                            }
                            if (missName.equals(tvAfternoonWorkFinishCardTime.getText())){
                                tvAfternoonWorkFinishCardTime.setText("");
                            }
                            break;
                        case 3:
                            if (dbValueNull.equals(attendanceDataBean.getXsignintimte())){
                                viewToDispose(attendanceDataBean,true,true,
                                        false,false,false,false,true,false);
                            }else {
                                viewToDispose(attendanceDataBean,true,true,
                                        false,false,false,false,false,false);
                            }
                            if (missName.equals(tvAfternoonWorkCardTime.getText())){
                                tvAfternoonWorkCardTime.setText("");
                            }
                            if (missName.equals(tvAfternoonWorkFinishCardTime.getText())){
                                tvAfternoonWorkFinishCardTime.setText("");
                            }
                            break;
                        case 4:
                            if (dbValueNull.equals(attendanceDataBean.getXsignbacktime())){
                                viewToDispose(attendanceDataBean,true,true,
                                        true,false,false,false,false,true);
                            }else {
                                viewToDispose(attendanceDataBean,true,true,
                                        true,false,false,false,false,false);
                            }
                            tvAfternoonWorkFinishCardTime.setText("");
                            if (missName.equals(tvAfternoonWorkFinishCardTime.getText())){
                                tvAfternoonWorkFinishCardTime.setText("");
                            }
                            break;
                        default:

                    }
                    break;
                case "1":
                    switch (curCarType) {
                        case 1:
                            if (dbValueNull.equals(attendanceDataBean.getSsignbacktime())){
                                viewToDispose(attendanceDataBean,false,false,
                                        false,false,false,true,false,false);
                            }else {
                                viewToDispose(attendanceDataBean,false,false,
                                        false,false,false,false,false,false);
                            }
                            if (missName.equals(tvMorningWorkFinishCardTime.getText())){
                                tvMorningWorkFinishCardTime.setText("");
                            }
                            if (missName.equals(tvAfternoonWorkCardTime.getText())){
                                tvAfternoonWorkCardTime.setText("");
                            }
                            if (missName.equals(tvAfternoonWorkFinishCardTime.getText())){
                                tvAfternoonWorkFinishCardTime.setText("");
                            }
                            break;
                        case 2:
                            if (dbValueNull.equals(attendanceDataBean.getSsignbacktime())){
                                viewToDispose(attendanceDataBean,false,false,
                                        false,false,false,true,false,false);
                            }else {
                                viewToDispose(attendanceDataBean,false,false,
                                        false,false,false,false,false,false);
                            }
                            if (missName.equals(tvMorningWorkFinishCardTime.getText())){
                                tvMorningWorkFinishCardTime.setText("");
                            }
                            if (missName.equals(tvAfternoonWorkCardTime.getText())){
                                tvAfternoonWorkCardTime.setText("");
                            }
                            if (missName.equals(tvAfternoonWorkFinishCardTime.getText())){
                                tvAfternoonWorkFinishCardTime.setText("");
                            }
                            break;
                        case 3:
                            if (dbValueNull.equals(attendanceDataBean.getXsignintimte())){
                                viewToDispose(attendanceDataBean,false,true,
                                        false,false,false,false,true,false);
                            }else {
                                viewToDispose(attendanceDataBean,false,true,
                                        false,false,false,false,false,false);
                            }
                            if (missName.equals(tvAfternoonWorkCardTime.getText())){
                                tvAfternoonWorkCardTime.setText("");
                            }
                            if (missName.equals(tvAfternoonWorkFinishCardTime.getText())){
                                tvAfternoonWorkFinishCardTime.setText("");
                            }

                            break;
                        case 4:
                            if (dbValueNull.equals(attendanceDataBean.getXsignbacktime())){
                                viewToDispose(attendanceDataBean,false,true,
                                        true,false,false,false,true,true);
                            }else {
                                viewToDispose(attendanceDataBean,false,true,
                                        true,false,false,false,true,false);
                            }
                            arriverBtAfternoonWorkFinishCard.setVisibility(View.VISIBLE);
                            if (missName.equals(tvAfternoonWorkFinishCardTime.getText())){
                                tvAfternoonWorkFinishCardTime.setText("");
                            }
                            break;
                        default:

                    }
                    break;
                case "2":
                    switch (curCarType) {
                        case 1:
                            deploymenOfAttendanceData(attendanceDataBean,
                                    false, false,
                                    false, false);
                            arriverBtMorningWorkCard.setVisibility(View.GONE);
                            arriverBtMorningWorkFinishCard.setVisibility(View.GONE);
                            if (dbValueNull.equals(attendanceDataBean.getXsignintimte())){
                                arriverBtAfternoonWorkCard.setVisibility(View.VISIBLE);
                            }else {
                                arriverBtAfternoonWorkCard.setVisibility(View.GONE);
                            }
                            arriverBtAfternoonWorkFinishCard.setVisibility(View.GONE);
                            if (missName.equals(tvAfternoonWorkCardTime.getText())){
                                tvAfternoonWorkCardTime.setText("");
                            }
                            if (missName.equals(tvAfternoonWorkFinishCardTime.getText())){
                                tvAfternoonWorkFinishCardTime.setText("");
                            }



                            break;
                        case 2:
                            deploymenOfAttendanceData(attendanceDataBean,
                                    false, false,
                                    false, false);
                            arriverBtMorningWorkCard.setVisibility(View.GONE);
                            arriverBtMorningWorkFinishCard.setVisibility(View.GONE);
                            if (dbValueNull.equals(attendanceDataBean.getXsignintimte())){
                                arriverBtAfternoonWorkCard.setVisibility(View.VISIBLE);
                            }else {
                                arriverBtAfternoonWorkCard.setVisibility(View.GONE);
                            }
                            arriverBtAfternoonWorkFinishCard.setVisibility(View.GONE);
                            if (missName.equals(tvAfternoonWorkCardTime.getText())){
                                tvAfternoonWorkCardTime.setText("");
                            }
                            if (missName.equals(tvAfternoonWorkFinishCardTime.getText())){
                                tvAfternoonWorkFinishCardTime.setText("");
                            }



                            break;
                        case 3:
                            deploymenOfAttendanceData(attendanceDataBean,
                                    false, false,
                                    false, false);
                            arriverBtMorningWorkCard.setVisibility(View.GONE);
                            arriverBtMorningWorkFinishCard.setVisibility(View.GONE);
                            if (dbValueNull.equals(attendanceDataBean.getXsignintimte())){
                                arriverBtAfternoonWorkCard.setVisibility(View.VISIBLE);
                            }else {
                                arriverBtAfternoonWorkCard.setVisibility(View.GONE);
                            }

                            arriverBtAfternoonWorkFinishCard.setVisibility(View.GONE);
                            if (missName.equals(tvAfternoonWorkCardTime.getText())){
                                tvAfternoonWorkCardTime.setText("");
                            }
                            if (missName.equals(tvAfternoonWorkFinishCardTime.getText())){
                                tvAfternoonWorkFinishCardTime.setText("");
                            }



                            break;
                        case 4:
                            deploymenOfAttendanceData(attendanceDataBean,
                                    false, false,
                                    true, false);
                            arriverBtMorningWorkCard.setVisibility(View.GONE);
                            arriverBtMorningWorkFinishCard.setVisibility(View.GONE);
                            arriverBtAfternoonWorkCard.setVisibility(View.GONE);

                            if (dbValueNull.equals(attendanceDataBean.getXsignbacktime())){
                                arriverBtAfternoonWorkFinishCard.setVisibility(View.VISIBLE);
                            }else {
                                arriverBtAfternoonWorkFinishCard.setVisibility(View.GONE);
                            }
                            if (missName.equals(tvAfternoonWorkFinishCardTime.getText())){
                                tvAfternoonWorkFinishCardTime.setText("");
                            }

                            break;
                        default:

                    }
                    break;
                case "3":
                    switch (curCarType) {
                        case 1:
                            deploymenOfAttendanceData(attendanceDataBean,
                                    false, false,
                                    false, false);
                            arriverBtMorningWorkCard.setVisibility(View.GONE);
                            arriverBtMorningWorkFinishCard.setVisibility(View.GONE);
                            arriverBtAfternoonWorkCard.setVisibility(View.GONE);

                            if (dbValueNull.equals(attendanceDataBean.getXsignbacktime())){
                                arriverBtAfternoonWorkFinishCard.setVisibility(View.VISIBLE);
                            }else {
                                arriverBtAfternoonWorkFinishCard.setVisibility(View.GONE);
                            }
                            if (missName.equals(tvAfternoonWorkFinishCardTime.getText())){
                                tvAfternoonWorkFinishCardTime.setText("");
                            }
                            break;
                        case 2:
                            deploymenOfAttendanceData(attendanceDataBean,
                                    false, false,
                                    false, false);
                            arriverBtMorningWorkCard.setVisibility(View.GONE);
                            arriverBtMorningWorkFinishCard.setVisibility(View.GONE);
                            arriverBtAfternoonWorkCard.setVisibility(View.GONE);
                            if (dbValueNull.equals(attendanceDataBean.getXsignbacktime())){
                                arriverBtAfternoonWorkFinishCard.setVisibility(View.VISIBLE);
                            }else {
                                arriverBtAfternoonWorkFinishCard.setVisibility(View.GONE);
                            }
                            if (missName.equals(tvAfternoonWorkFinishCardTime.getText())){
                                tvAfternoonWorkFinishCardTime.setText("");
                            }
                            break;
                        case 3:
                            deploymenOfAttendanceData(attendanceDataBean,
                                    false, false,
                                    false, false);
                            arriverBtMorningWorkCard.setVisibility(View.GONE);
                            arriverBtMorningWorkFinishCard.setVisibility(View.GONE);
                            arriverBtAfternoonWorkCard.setVisibility(View.GONE);
                            if (dbValueNull.equals(attendanceDataBean.getXsignbacktime())){
                                arriverBtAfternoonWorkFinishCard.setVisibility(View.VISIBLE);
                            }else {
                                arriverBtAfternoonWorkFinishCard.setVisibility(View.GONE);
                            }
                            if (missName.equals(tvAfternoonWorkFinishCardTime.getText())){
                                tvAfternoonWorkFinishCardTime.setText("");
                            }



                            break;
                        case 4:
                            deploymenOfAttendanceData(attendanceDataBean,
                                    false, false,
                                    false, false);
                            arriverBtMorningWorkCard.setVisibility(View.GONE);
                            arriverBtMorningWorkFinishCard.setVisibility(View.GONE);
                            arriverBtAfternoonWorkCard.setVisibility(View.GONE);
                            if (dbValueNull.equals(attendanceDataBean.getXsignbacktime())){
                                arriverBtAfternoonWorkFinishCard.setVisibility(View.VISIBLE);
                            }else {
                                arriverBtAfternoonWorkFinishCard.setVisibility(View.GONE);
                            }
                            if (missName.equals(tvAfternoonWorkFinishCardTime.getText())){
                                tvAfternoonWorkFinishCardTime.setText("");
                            }
                            break;
                        default:

                    }
                    break;
                case "4":
                    switch (curCarType) {
                        case 1:
                            deploymenOfAttendanceData(attendanceDataBean,
                                    false, false,
                                    false, false);
                            arriverBtMorningWorkCard.setVisibility(View.GONE);
                            arriverBtMorningWorkFinishCard.setVisibility(View.GONE);
                            arriverBtAfternoonWorkCard.setVisibility(View.GONE);
                            arriverBtAfternoonWorkFinishCard.setVisibility(View.GONE);
                            break;
                        case 2:
                            deploymenOfAttendanceData(attendanceDataBean,
                                    false, false,
                                    false, false);
                            arriverBtMorningWorkCard.setVisibility(View.GONE);
                            arriverBtMorningWorkFinishCard.setVisibility(View.GONE);
                            arriverBtAfternoonWorkCard.setVisibility(View.GONE);
                            arriverBtAfternoonWorkFinishCard.setVisibility(View.GONE);
                            break;
                        case 3:
                            deploymenOfAttendanceData(attendanceDataBean,
                                    false, false,
                                    false, false);
                            arriverBtMorningWorkCard.setVisibility(View.GONE);
                            arriverBtMorningWorkFinishCard.setVisibility(View.GONE);
                            arriverBtAfternoonWorkCard.setVisibility(View.GONE);
                            arriverBtAfternoonWorkFinishCard.setVisibility(View.GONE);
                            break;
                        case 4:
                            deploymenOfAttendanceData(attendanceDataBean,
                                    false, false,
                                    false, false);
                            arriverBtMorningWorkCard.setVisibility(View.GONE);
                            arriverBtMorningWorkFinishCard.setVisibility(View.GONE);
                            arriverBtAfternoonWorkCard.setVisibility(View.GONE);
                            arriverBtAfternoonWorkFinishCard.setVisibility(View.GONE);
                            break;
                        default:

                    }
                    break;
                default:
            }
        } else {
            switch (curCarType) {
                case 1:
                    deploymenOfAttendanceData(attendanceDataBean,
                            false, false,
                            false, false);

                    if (dbValueNull.equals(attendanceDataBean.getSsignintimte())){
                        arriverBtMorningWorkCard.setVisibility(View.VISIBLE);
                    }else {
                        arriverBtMorningWorkCard.setVisibility(View.GONE);
                    }
                    arriverBtMorningWorkFinishCard.setVisibility(View.GONE);
                    arriverBtAfternoonWorkCard.setVisibility(View.GONE);
                    arriverBtAfternoonWorkFinishCard.setVisibility(View.GONE);
                    if (missName.equals(tvMorningWorkCardTime.getText())){
                        tvMorningWorkCardTime.setText("");
                    }
                    if (missName.equals(tvMorningWorkFinishCardTime.getText())){
                        tvMorningWorkFinishCardTime.setText("");
                    }
                    if (missName.equals(tvAfternoonWorkCardTime.getText())){
                        tvAfternoonWorkCardTime.setText("");
                    }
                    if (missName.equals(tvAfternoonWorkFinishCardTime.getText())){
                        tvAfternoonWorkFinishCardTime.setText("");
                    }
                    break;
                case 2:
                    deploymenOfAttendanceData(attendanceDataBean,
                            true, false,
                            false, false);
                    arriverBtMorningWorkCard.setVisibility(View.GONE);

                    if (dbValueNull.equals(attendanceDataBean.getSsignbacktime())){
                        arriverBtMorningWorkFinishCard.setVisibility(View.VISIBLE);
                    }else {
                        arriverBtMorningWorkFinishCard.setVisibility(View.GONE);
                    }

                    arriverBtAfternoonWorkCard.setVisibility(View.GONE);
                    arriverBtAfternoonWorkFinishCard.setVisibility(View.GONE);

                    if (missName.equals(tvMorningWorkFinishCardTime.getText())){
                        tvMorningWorkFinishCardTime.setText("");
                    }
                    if (missName.equals(tvAfternoonWorkCardTime.getText())){
                        tvAfternoonWorkCardTime.setText("");
                    }
                    if (missName.equals(tvAfternoonWorkFinishCardTime.getText())){
                        tvAfternoonWorkFinishCardTime.setText("");
                    }



                    break;
                case 3:
                    deploymenOfAttendanceData(attendanceDataBean,
                            true, true,
                            false, false);
                    arriverBtMorningWorkCard.setVisibility(View.GONE);
                    arriverBtMorningWorkFinishCard.setVisibility(View.GONE);
                    if (dbValueNull.equals(attendanceDataBean.getXsignintimte())){
                        arriverBtAfternoonWorkCard.setVisibility(View.VISIBLE);
                    }else {
                        arriverBtAfternoonWorkCard.setVisibility(View.GONE);
                    }
                    arriverBtAfternoonWorkFinishCard.setVisibility(View.GONE);
                    if (missName.equals(tvAfternoonWorkCardTime.getText())){
                        tvAfternoonWorkCardTime.setText("");
                    }
                    if (missName.equals(tvAfternoonWorkFinishCardTime.getText())){
                        tvAfternoonWorkFinishCardTime.setText("");
                    }


                    break;
                case 4:
                    deploymenOfAttendanceData(attendanceDataBean,
                            true, true,
                            true, false);
                    arriverBtMorningWorkCard.setVisibility(View.GONE);
                    arriverBtMorningWorkFinishCard.setVisibility(View.GONE);
                    arriverBtAfternoonWorkCard.setVisibility(View.GONE);
                    if (dbValueNull.equals(attendanceDataBean.getXsignbacktime())){
                        arriverBtAfternoonWorkFinishCard.setVisibility(View.VISIBLE);
                    }else {
                        arriverBtAfternoonWorkFinishCard.setVisibility(View.GONE);
                    }
                    if (missName.equals(tvAfternoonWorkFinishCardTime.getText())){
                        tvAfternoonWorkFinishCardTime.setText("");
                    }
                    break;
                default:
            }
        }
    }

    /**
     * 部署界面
     * @param attendanceDataBean
     * @param morningToWorkCard
     * @param morningWorkedCard
     * @param afternoonToWorkCard
     * @param afternoonWorkedCard
     * @param b4
     * @param b5
     * @param b6
     * @param b7
     */
    private void viewToDispose(AttendanceDataBean attendanceDataBean, boolean morningToWorkCard, boolean morningWorkedCard, boolean afternoonToWorkCard, boolean afternoonWorkedCard, boolean b4, boolean b5, boolean b6, boolean b7) {
        deploymenOfAttendanceData(attendanceDataBean,
                morningToWorkCard, morningWorkedCard,
                afternoonToWorkCard, afternoonWorkedCard);
        if (b4){
            arriverBtMorningWorkCard.setVisibility(View.GONE);
        }else {
            arriverBtMorningWorkCard.setVisibility(View.VISIBLE);
        }
        if (!b5){
            arriverBtMorningWorkFinishCard.setVisibility(View.GONE);
        }
        if (!b6){
            arriverBtAfternoonWorkCard.setVisibility(View.GONE);
        }
        if (!b7){
            arriverBtAfternoonWorkFinishCard.setVisibility(View.GONE);
        }
    }

    /**
     * 根据当前的数据以及当前所处的打卡时间段
     * 部署考勤数据，及缺卡位置
     */
    private void deploymenOfAttendanceData(AttendanceDataBean attendanceDataBean,
                                           Boolean morningToWorkCard, Boolean morningWorkFinishCard,
                                           Boolean afernoonToWorkCard, Boolean afternoonWorkFinishCard) {

        if (morningToWorkCard) {
            if (dbValueNull.equals(attendanceDataBean.getSsignintimte())){
                tvMorningWorkCardTime.setText("缺卡");
            }else {
                morningWorkCardTime = attendanceDataBean.getSsignintimte();
                tvMorningWorkCardTime.setText(morningWorkCardTime);
                if (!dbValueNull.equals(attendanceDataBean.getAmbdkwz())) {
                    morningWorkCardAddress = attendanceDataBean.getAmbdkwz();
                    tvMorningWorkCardAddress.setText(morningWorkCardAddress);
                }

            }

        } else {
            if (!dbValueNull.equals(attendanceDataBean.getSsignintimte())) {

                morningWorkCardTime = attendanceDataBean.getSsignintimte();
                tvMorningWorkCardTime.setText(morningWorkCardTime);

            }else {
                tvMorningWorkCardTime.setText("缺卡");
            }
            if (!dbValueNull.equals(attendanceDataBean.getAmbdkwz())) {
                morningWorkCardAddress = attendanceDataBean.getAmbdkwz();
                tvMorningWorkCardAddress.setText(morningWorkCardAddress);
            }
        }
        if (morningWorkFinishCard) {
            if (dbValueNull.equals(attendanceDataBean.getSsignbacktime())){
                tvMorningWorkFinishCardTime.setText("缺卡");
            }else {
                if (!dbValueNull.equals(attendanceDataBean.getSsignbacktime())) {
                    morningWorkFinishCardTime = attendanceDataBean.getSsignbacktime();
                    tvMorningWorkFinishCardTime.setText(morningWorkFinishCardTime);
                }
                if (!dbValueNull.equals(attendanceDataBean.getAmedkwz())) {
                    morningWorkFinishCardAddress = attendanceDataBean.getAmedkwz();
                    tvMorningWorkFinishCardAddress.setText(morningWorkFinishCardAddress);
                }
            }

        } else {
            if (!dbValueNull.equals(attendanceDataBean.getSsignbacktime())) {
                morningWorkFinishCardTime = attendanceDataBean.getSsignbacktime();
                tvMorningWorkFinishCardTime.setText(morningWorkFinishCardTime);
            }else {
                tvMorningWorkFinishCardTime.setText("缺卡");
            }
            if (!dbValueNull.equals(attendanceDataBean.getAmedkwz())) {
                morningWorkFinishCardAddress = attendanceDataBean.getAmedkwz();
                tvMorningWorkFinishCardAddress.setText(morningWorkFinishCardAddress);
            }
        }

        if (afernoonToWorkCard) {
            if (dbValueNull.equals(attendanceDataBean.getXsignintimte())){
                tvAfternoonWorkCardTime.setText("缺卡");
            }else {
                if (!dbValueNull.equals(attendanceDataBean.getXsignintimte())) {
                    afternoonWorkCardTime = attendanceDataBean.getXsignintimte();
                    tvAfternoonWorkCardTime.setText(afternoonWorkCardTime);
                }
                if (!dbValueNull.equals(attendanceDataBean.getPmbdkwz())) {
                    afternoonWorkCardAddress = attendanceDataBean.getPmbdkwz();
                    tvAfternoonWorkCardAddress.setText(afternoonWorkCardAddress);
                }
            }

        } else {
            if (!dbValueNull.equals(attendanceDataBean.getXsignintimte())) {
                afternoonWorkCardTime = attendanceDataBean.getXsignintimte();
                tvAfternoonWorkCardTime.setText(afternoonWorkCardTime);
            }else {
                tvAfternoonWorkCardTime.setText("缺卡");
            }
            if (!dbValueNull.equals(attendanceDataBean.getPmbdkwz())) {
                afternoonWorkCardAddress = attendanceDataBean.getPmbdkwz();
                tvAfternoonWorkCardAddress.setText(afternoonWorkCardAddress);
            }
        }
        if (afternoonWorkFinishCard) {
            if (dbValueNull.equals(attendanceDataBean.getXsignbacktime())){
                tvAfternoonWorkFinishCardTime.setText("缺卡");
            }else {
                if (!dbValueNull.equals(attendanceDataBean.getXsignbacktime())) {
                    afternoonWorkFinishCardTime = attendanceDataBean.getXsignbacktime();
                    tvAfternoonWorkFinishCardTime.setText(afternoonWorkFinishCardTime);
                }
                if (!dbValueNull.equals(attendanceDataBean.getPmedkwz())) {
                    afternoonWorkFinishCardAddress = attendanceDataBean.getPmedkwz();
                    tvAfternoonWorkFinishCardAddress.setText(afternoonWorkFinishCardAddress);
                }
            }

        } else {
            if (!dbValueNull.equals(attendanceDataBean.getXsignbacktime())) {
                afternoonWorkFinishCardTime = attendanceDataBean.getXsignbacktime();
                tvAfternoonWorkFinishCardTime.setText(afternoonWorkFinishCardTime);
            }else {
                tvAfternoonWorkFinishCardTime.setText("缺卡");
            }
            if (!dbValueNull.equals(attendanceDataBean.getPmedkwz())) {
                afternoonWorkFinishCardAddress = attendanceDataBean.getPmedkwz();
                tvAfternoonWorkFinishCardAddress.setText(afternoonWorkFinishCardAddress);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapview.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
        //取消注册传感器监听
        mSensorManager.unregisterListener(this);
    }

    private void requestLocation() {
        initLocation();//初始化定位设置
        mLocationClient.start();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        //可选，默认高精度（优先GPS模式，其次网络模式），高精度，低功耗
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用一般是bd0911
        option.setCoorType("bd0911");
        //可选默认是0，即仅定位一次，设置发起连续定位请求的间隔必须大于1s才有效
        option.setScanSpan(5000);
        //设置是否需要地址信息，默认否
        option.setIsNeedAddress(true);
        //设置是否需要地址描述
        option.setIsNeedLocationDescribe(true);
        //设置是否需要设备方向结果
        option.setNeedDeviceDirect(true);
        //可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setLocationNotify(true);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setIgnoreKillProcess(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationDescribe(false);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIsNeedLocationPoiList(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否开启Gps定位
        option.setOpenGps(true);
        //可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
        option.setIsNeedAltitude(false);
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(bdabstractlocationlisteners);
    }

    /**
     * 更新打卡监听
     * @param view
     */
    @OnClick({R.id.tv_morning_work_card_update, R.id.tv_morning_work_finish_card_update, R.id.tv_afternoon_work_card_update, R.id.tv_afternoon_work_finish_card_update})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_morning_work_card_update:
                clockTime = arriverTimetv.getText().toString().trim();
                if (mDistance <= distance) {
                    clockState = "打卡成功";
                } else {
                    clockState = "外勤打卡";
                }
                attendanceListPresenter.doDataForPrams(clockTime, curLocationInfor, clockState, curUser.getOid(), curUser.getOrgid(), 1,true);
                break;
            case R.id.tv_morning_work_finish_card_update:
                clockTime = arriverTimetv.getText().toString().trim();
                if (mDistance <= distance) {
                    clockState = "打卡成功";
                } else {
                    clockState = "外勤打卡";
                }
                attendanceListPresenter.doDataForPrams(clockTime, curLocationInfor, clockState, curUser.getOid(), curUser.getOrgid(), 2,true);
                break;
            case R.id.tv_afternoon_work_card_update:
                clockTime = arriverTimetv.getText().toString().trim();

                if (mDistance <= distance) {
                    clockState = "打卡成功";
                } else {
                    clockState = "外勤打卡";
                }
                attendanceListPresenter.doDataForPrams(clockTime, curLocationInfor, clockState, curUser.getOid(), curUser.getOrgid(), 3,true);
                break;
            case R.id.tv_afternoon_work_finish_card_update:
                clockTime = arriverTimetv.getText().toString().trim();
                if (mDistance <= distance) {
                    clockState = "打卡成功";
                } else {
                    clockState = "外勤打卡";
                }
                attendanceListPresenter.doDataForPrams(clockTime, curLocationInfor, clockState, curUser.getOid(), curUser.getOrgid(), 4,true);
                break;
                default:
        }
    }


    /**
     * 接收定位结果消息，并显示在地图上
     */
    private BDAbstractLocationListener bdabstractlocationlisteners = new BDAbstractLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //定位方向
            mCurrentLat = bdLocation.getLatitude();
            mCurrentLon = bdLocation.getLongitude();
            String addTes = bdLocation.getAddrStr();
            if (bdLocation.getAddress()==null){
            }else if (dbValueNull.equals(addTes)){
            }else if (!TextUtils.isEmpty((bdLocation.getAddress().toString()))){
                curLocationInfor = bdLocation.getAddrStr();
            }
            //个人定位
            locData = new MyLocationData.Builder()
                    .direction(mCurrentDirection).latitude(bdLocation.getLatitude()).latitude(bdLocation.getLongitude()).build();
            baiduMap.setMyLocationData(locData);
            baiduMap.setMyLocationConfiguration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null));
            //更改UI
            Message message = new Message();
            message.obj = bdLocation;
            mHandler.sendMessage(message);
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BDLocation location = (BDLocation) msg.obj;
            LatLng locationPoint = new LatLng(location.getLatitude(), location.getLongitude());
            //打卡范围
//            mDestinationPoint = new LatLng(37.809687 * 1.0001, 112.527109 * 1.0001);
            mDestinationPoint = new LatLng(37.809687, 112.527109);
            setCircleOptions();
            //计算两点之间的距离，单位“米”
            mDistance = DistanceUtil.getDistance(mDestinationPoint, locationPoint);
            if (mDistance <= distance) {
                //显示文字
                setTextOptions(mDestinationPoint, "您已在考勤范围内", "#7ED321");
                //目的地图标
                setMarkerOptions(mDestinationPoint, R.mipmap.arrive_icon);
                //按钮颜色
                arriverBt.setBackgroundDrawable(getResources().getDrawable(R.mipmap.restaurant_btbg_yellow));
                baiduMap.setMyLocationEnabled(true);
            } else {
                setTextOptions(locationPoint, "您不在考勤范围之内", "#FF6C6C");
                setMarkerOptions(mDestinationPoint, R.mipmap.restaurant_icon);
                arriverBt.setBackgroundDrawable(getResources().getDrawable(R.mipmap.restaurant_btbg_gray));
                baiduMap.setMyLocationEnabled(true);
            }
            distanceTv.setText("距离目的地：" + mDistance + "米");
            //缩放地图
            setMapZoomScale(locationPoint);
        }
    };

    /**
     * 设置打卡目标范围圈
     */
    private void setCircleOptions() {
        if (mDestinationPoint == null) {
            return;
        }
        OverlayOptions ooCircle = new CircleOptions().fillColor(0x4057FFF8)
                .center(mDestinationPoint).stroke(new Stroke(1, 0xB6FFFFFF)).radius(distance);
        baiduMap.addOverlay(ooCircle);
    }

    /***
     * 添加地图文字
     *
     */
    private void setTextOptions(LatLng point, String str, String color) {
        //使用MakerInfoWindow
        if (point == null) {
            return;
        }
        TextView textView = new TextView(getApplicationContext());
        textView.setBackgroundResource(R.mipmap.map_textbg);
        textView.setPadding(0, 23, 0, 0);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setTextSize(14);
        textView.setGravity(Gravity.CENTER);
        textView.setText(str);
        textView.setTextColor(Color.parseColor(color));
        mInfoWindow = new InfoWindow(textView, point, 170);
        baiduMap.showInfoWindow(mInfoWindow);

    }

    /**
     * 设置Marker覆盖物
     *
     * @param ll
     * @param icon
     */
    private void setMarkerOptions(LatLng ll, int icon) {
        if (ll == null) {
            return;
        }
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(icon);
        MarkerOptions ooD = new MarkerOptions().position(ll).icon(bitmap);
        baiduMap.addOverlay(ooD);
    }

    /**
     * 改变地图缩放
     *
     * @param ll
     */
    private void setMapZoomScale(LatLng ll) {
        if (mDestinationPoint == null) {
            mZoomScale = getZoomScale(ll);
            baiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(ll, mZoomScale));
        } else {
            mZoomScale = getZoomScale(ll);
            //缩放
            baiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(mCenterPos, mZoomScale));
        }
    }

    /**
     * 获取地图的中心点及缩放比例
     *
     * @param locationpoint
     * @return
     */
    private float getZoomScale(LatLng locationpoint) {
        double maxLong;    //最大经度
        double minLong;    //最小经度
        double maxLat;     //最大纬度
        double minLat;     //最小纬度
        //经度集合
        List<Double> longItems = new ArrayList<Double>();
        //纬度集合
        List<Double> latItems = new ArrayList<Double>();

        if (null != locationpoint) {
            longItems.add(locationpoint.longitude);
            latItems.add(locationpoint.latitude);
        }
        if (null != mDestinationPoint) {
            longItems.add(mDestinationPoint.longitude);
            latItems.add(mDestinationPoint.latitude);
        }
        //最大经度
        maxLong = longItems.get(0);
        //最小经度
        minLong = longItems.get(0);
        //最大纬度
        maxLat = latItems.get(0);
        //最小纬度
        minLat = latItems.get(0);

        for (int i = 0; i < longItems.size(); i++) {
            //获取集合中的最大经度
            maxLong = Math.max(maxLong, longItems.get(i));
            //获取集合中的最小经度
            minLong = Math.min(minLong, longItems.get(i));
        }

        for (int i = 0; i < latItems.size(); i++) {
            //获取集合中的最大纬度
            maxLat = Math.max(maxLat, latItems.get(i));
            //获取集合中的最小纬度
            minLat = Math.min(minLat, latItems.get(i));
        }
        double latCenter = (maxLat + minLat) / 2;
        double longCenter = (maxLong + minLong) / 2;
        //缩放比例参数
        int jl = (int) getDistance(new LatLng(maxLat, maxLong), new LatLng(minLat, minLong));
        //获取中心点经纬度
        mCenterPos = new LatLng(latCenter, longCenter);
        int[] zoomLevel = {2500000, 2000000, 1000000, 500000, 200000, 100000,
                50000, 25000, 20000, 10000, 5000, 2000, 1000, 500, 100, 50, 20, 0};
        int i;
        for (i = 0; i < mLevelNum; i++) {
            if (zoomLevel[i] < jl) {
                break;
            }
        }
//        float zoom = i + 4;
        float zoom = i;
        return zoom;
    }

    /**
     * 缩放比例参数
     *
     * @param latLng
     * @param latLng1
     * @return
     */
    private double getDistance(LatLng latLng, LatLng latLng1) {
        if (latLng != null && latLng1 != null) {
            Point var2 = CoordUtil.ll2point(latLng);
            Point var3 = CoordUtil.ll2point(latLng1);
            return var2 != null && var3 != null ? CoordUtil.getDistance(var2, var3) : -1.0D;
        } else {
            return -1.0D;
        }
    }

    /**
     * 设置系统时间
     */
    private Runnable run = new Runnable() {
        @Override
        public void run() {
            // HH:mm:ss
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
            //获取当前时间
            Date date = new Date(System.currentTimeMillis());
            //更新时间
            arriverTimetv.setText(simpleDateFormat.format(date));
            arriverTimetvMorningWorkCard.setText(simpleDateFormat.format(date));
            arriverTimetvMorningWorkFinishCard.setText(simpleDateFormat.format(date));
            arriverTimetvAfternoonWorkCard.setText(simpleDateFormat.format(date));
            arriverTimetvAfternoonWorkFinishCard.setText(simpleDateFormat.format(date));
            mHandler.postDelayed(run, 3000);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        mapview.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        double x = event.values[SensorManager.DATA_X];
        if (Math.abs(x - lastX) > 1.0) {
            mCurrentDirection = (int) x;
            locData = new MyLocationData.Builder()
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(mCurrentLat)
                    .longitude(mCurrentLon).build();
            baiduMap.setMyLocationData(locData);
        }
        lastX = x;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.arriver_bt:
                break;
            case R.id.arriver_bt_morning_work_card:
                clockTime = arriverTimetv.getText().toString().trim();
                if (mDistance <= distance) {
                    clockState = "打卡成功";
                } else {
                    clockState = "外勤打卡";
                }
                attendanceListPresenter.doDataForPrams(clockTime, curLocationInfor, clockState, curUser.getOid(), curUser.getOrgid(), 1,false);
                break;
            case R.id.arriver_bt_morning_work_finish_card:
                clockTime = arriverTimetv.getText().toString().trim();
                if (mDistance <= distance) {
                    clockState = "打卡成功";
                } else {
                    clockState = "外勤打卡";
                }
                attendanceListPresenter.doDataForPrams(clockTime, curLocationInfor, clockState, curUser.getOid(), curUser.getOrgid(), 2,false);
                break;
            case R.id.arriver_bt_afternoon_work_card:
                clockTime = arriverTimetv.getText().toString().trim();

                if (mDistance <= distance) {
                    clockState = "打卡成功";
                } else {
                    clockState = "外勤打卡";
                }
                attendanceListPresenter.doDataForPrams(clockTime, curLocationInfor, clockState, curUser.getOid(), curUser.getOrgid(), 3,false);
                break;
            case R.id.arriver_bt_afternoon_work_finish_card:
                clockTime = arriverTimetv.getText().toString().trim();
                if (mDistance <= distance) {
                    clockState = "打卡成功";
                } else {
                    clockState = "外勤打卡";
                }
                attendanceListPresenter.doDataForPrams(clockTime, curLocationInfor, clockState, curUser.getOid(), curUser.getOrgid(), 4,false);
                break;
            default:
        }
    }

    @Override
    public void onResultToP(AttendanceResultBean feedBackResult, int cardType,Boolean isUpdate) {
        if (feedBackResult.getSuccess()) {
            if (operationFailed.equals(feedBackResult.getMsg())) {
                Toast.makeText(AttendanceListActivity.this, feedBackResult.getMsg() + ",请联系管理员!", Toast.LENGTH_SHORT).show();
            } else {
                updateView(clockType, String.valueOf(cardType), getCurDateAndTime("yyyy-MM-dd") + " " + clockTime, curLocationInfor,isUpdate);
            }
        }
    }

    /**
     * 获取远程数据库当前用户目前考勤数据的结果
     *
     * @param o    返回的实体类
     * @param code 返回实体类的类型代码
     */
    @Override
    public void onResultToPData(Object o, int code) {
        //关闭Dialog

        switch (code) {
            case 0:
                if (o!=null){
                    AttendanceShiftsBean attendanceShifts = (AttendanceShiftsBean) o;
                    Log.e("", "");
                    initColockOfShift(attendanceShifts);
                }else {
                    Toast.makeText(AttendanceListActivity.this, "未设置打卡班次，请联系管理员！", Toast.LENGTH_SHORT).show();
                }

                break;
            case 1:
                AttendanceDataBean attendanceDataBean = (AttendanceDataBean) o;
                int curCarType = judgeCurCardType(getCurDateAndTime("HH:mm:ss"));
                initColockView(attendanceDataBean, curCarType);
                List<AttendanceDataBean> attendanceMsg = new ArrayList<>();
                attendanceMsg.add(attendanceDataBean);
                String curDate = getCurDateAndTime("yyyy-MM-dd");
                List<AttendanceDataBean> attendanceDataBeanList =  LitePal.where("date = ?",curDate).find(AttendanceDataBean.class);
                if (attendanceDataBeanList!=null){
                    if (attendanceDataBeanList.size()!=0){
                        attendanceDataBeanList.get(0).updateAll();
                    }else {
                        LitePal.saveAll(attendanceMsg);
                    }
                }else {
                    LitePal.saveAll(attendanceMsg);
                }
                break;
            default:
        }
    }

    private void initColockOfShift(AttendanceShiftsBean attendanceShifts) {
        String typeShift = attendanceShifts.getShift();
        switch (typeShift) {
            case "2":
                llAfternoonToWorkCard.setVisibility(View.GONE);
                llAfternoonWorkFinishCard.setVisibility(View.GONE);
                tvMorningWorkCard.setText("上午上班时间: " + attendanceShifts.getSworktime());
                tvMorningWorkFinishCard.setText("下午下班时间: " + attendanceShifts.getSbacktime());
                break;
            case "4":
                tvMorningWorkCard.setText("上午上班时间: " + attendanceShifts.getSworktime());
                tvMorningWorkFinishCard.setText("上午下班时间: " + attendanceShifts.getSbacktime());
                tvAfternoonWorkCard.setText("下午上班时间: " + attendanceShifts.getXworktime());
                tvAfternoonWorkFinishCard.setText("下午下班时间: " + attendanceShifts.getXbacktime());
                break;
            default:

        }

    }

    /**
     * 根据远程数据库返回的值，更新界面
     *
     * @param curCarType
     * @param cardType
     */
    private void updateView(int curCarType, String cardType, String colockTime, String colockAddress,Boolean isUpdate) {
        switch (cardType) {
            case "1":
                if (!isUpdate){
                    arriverBtMorningWorkCard.setVisibility(View.GONE);
                    arriverBtMorningWorkFinishCard.setVisibility(View.VISIBLE);
                }
                //更新数据
                tvMorningWorkCardTime.setText(colockTime);
                tvMorningWorkCardAddress.setText(colockAddress);
                break;
            case "2":
                if (!isUpdate){
                    arriverBtMorningWorkFinishCard.setVisibility(View.GONE);
                    arriverBtAfternoonWorkCard.setVisibility(View.VISIBLE);
                }
                tvMorningWorkFinishCardTime.setText(colockTime);
                tvMorningWorkFinishCardAddress.setText(colockAddress);

                break;
            case "3":
                if (!isUpdate){
                    arriverBtAfternoonWorkCard.setVisibility(View.GONE);
                    arriverBtAfternoonWorkFinishCard.setVisibility(View.VISIBLE);
                }

                tvAfternoonWorkCardTime.setText(colockTime);
                tvAfternoonWorkCardAddress.setText(colockAddress);

                break;
            case "4":
                if (!isUpdate){
                    arriverBtAfternoonWorkFinishCard.setVisibility(View.GONE);
                }

                tvAfternoonWorkFinishCardTime.setText(colockTime);
                tvAfternoonWorkFinishCardAddress.setText(colockAddress);
                break;
            case "请假":
                arriverBtAfternoonWorkFinishCard.setVisibility(View.GONE);
                tvAfternoonWorkFinishCardTime.setText(clockTime);
                tvAfternoonWorkFinishCardAddress.setText(curLocationInfor);
                break;
            default:

        }
    }

    @Override
    public void onResultForError() {
    }
}
