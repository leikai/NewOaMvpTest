package com.bs.lk.newoamvptest.view.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
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
import com.bs.lk.newoamvptest.bean.AttendanceResultBean;
import com.bs.lk.newoamvptest.bean.UserNewBean;
import com.bs.lk.newoamvptest.presenter.ISignInPresenter;
import com.bs.lk.newoamvptest.presenter.SignInPresenter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 签到页面
 *
 * @author lk
 */
public class SignInActivity extends AppCompatActivity implements SensorEventListener, View.OnClickListener, ISignInView {

    @BindView(R.id.mapview)
    MapView mapview;
    @BindView(R.id.distance_tv)
    TextView distanceTv;
    @BindView(R.id.arriver_timetv)
    TextView arriverTimetv;
    @BindView(R.id.arriver_bt)
    RelativeLayout arriverBt;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    /**
     * 规定到达距离范围距离
     */
    private int DISTANCE = 200;
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

    ISignInPresenter signInPresenter;

    private final String TAG = "SignInActivity";

    /**
     * 当前位置信息
     */
    private String curLocationInfor;
    private String clockState;
    /**
     * 当前用户
     */
    private UserNewBean curUser;
    /**
     * 打卡时间
     */
    private String clockTime;
    /**
     * 打卡类型
     */
    private int clockType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationClient = new LocationClient(getApplicationContext());
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_sign_in);
        setSupportActionBar(toolBar);
        ButterKnife.bind(this);
        curUser = CApplication.getInstance().getCurrentUser();
        signInPresenter = new SignInPresenter(this);
        initBaiduMap();
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(SignInActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(SignInActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(SignInActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(SignInActivity.this, permissions, 1);
        } else {
            requestLocation();
        }
        //设置系统时间
        mHandler.post(run);
        arriverBt.setOnClickListener(this);

    }

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

    @Override
    protected void onResume() {
        super.onResume();
        mapview.onResume();
        //为系统的方向传感器注册监听
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_UI);

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
        mLocationClient.registerLocationListener(BDAbstractLocationListener);
    }

//    /** 获得所在位置经纬度及详细地址 */
//    public void getLocation(View view) {
//        // 声明定位参数
//        LocationClientOption option = new LocationClientOption();
//        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式 高精度
//        option.setCoorType("bd09ll");// 设置返回定位结果是百度经纬度 默认gcj02
//        option.setScanSpan(5000);// 设置发起定位请求的时间间隔 单位ms
//        option.setIsNeedAddress(true);// 设置定位结果包含地址信息
//        option.setNeedDeviceDirect(true);// 设置定位结果包含手机机头 的方向
//        // 设置定位参数
//        mLocationClient.setLocOption(option);
//        // 启动定位
//        mLocationClient.start();
//
//    }

    private class MyBDLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // 非空判断
            if (location != null) {
                // 根据BDLocation 对象获得经纬度以及详细地址信息
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                String address = location.getAddrStr();
                Log.i(TAG, "address:" + address + " latitude:" + latitude
                        + " longitude:" + longitude + "—");
                if (mLocationClient.isStarted()) {
                    // 获得位置之后停止定位
                    mLocationClient.stop();
                }
            }
        }
    }


    /**
     * 接收定位结果消息，并显示在地图上
     */
    private BDAbstractLocationListener BDAbstractLocationListener = new BDAbstractLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //定位方向
            mCurrentLat = bdLocation.getLatitude();
            mCurrentLon = bdLocation.getLongitude();
            curLocationInfor = bdLocation.getAddrStr();
            Log.e("curLocationInfor位置", "" + curLocationInfor);
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
            if (mDistance <= DISTANCE) {
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
                .center(mDestinationPoint).stroke(new Stroke(1, 0xB6FFFFFF)).radius(DISTANCE);
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
     * @param LocationPoint
     * @return
     */
    private float getZoomScale(LatLng LocationPoint) {
        double maxLong;    //最大经度
        double minLong;    //最小经度
        double maxLat;     //最大纬度
        double minLat;     //最小纬度
        //经度集合
        List<Double> longItems = new ArrayList<Double>();
        //纬度集合
        List<Double> latItems = new ArrayList<Double>();

        if (null != LocationPoint) {
            longItems.add(LocationPoint.longitude);
            latItems.add(LocationPoint.latitude);
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
            mHandler.postDelayed(run, 1000);
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
                if (mDistance <= DISTANCE) {
                    clockState = "打卡成功";
                    clockTime = arriverTimetv.getText().toString().trim();
                    Log.e("curLocationInfor", "" + curLocationInfor);

                    signInPresenter.doDataForPrams(clockTime, curLocationInfor, clockState, curUser.getOid(), curUser.getOrgid());
                } else {
                    clockTime = arriverTimetv.getText().toString().trim();
                    Log.e("curLocationInfor", "" + curLocationInfor);
                    clockState = "外勤打卡";
                    signInPresenter.doDataForPrams(clockTime, curLocationInfor, clockState, curUser.getOid(), curUser.getOrgid());


                }
                break;
            default:
        }

    }

    @Override
    public void onResultToP(AttendanceResultBean feedBackResult) {
        //年-月-日 时-分
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            try {
                //当前时间
                Date date1 = dateFormat.parse(clockTime);
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

                if (date2.getTime()>date1.getTime()){
                    //打卡时间位置上班时间以前：属于上午上班正常卡
                    clockType = 1;
                }else if (date3.getTime()>date1.getTime()){
                    //打卡时间位于上午下班以前：属于上午上班迟到卡
                    clockType = 2;
                }else if (date4.getTime()>date1.getTime()){
                    //打卡时间位于下午上班以前：属于上午下班正常卡
                    clockType = 3;
                }else if (date5.getTime()>date1.getTime()){
                    //打卡时间位于下午下班以前：属于下午上班正常卡
                    clockType = 4;
                }else if (date6.getTime()>date1.getTime()){
                    //打卡时间位于下午下班以前：属于下午上班迟到卡
                    clockType = 5;
                }else {
                    //打卡时间位于下午下班以前：属于下午下午下班正常卡
                    clockType = 6;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }




        Intent intent = new Intent(SignInActivity.this,AttendanceListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("打卡时间",clockTime);
        bundle.putString("打卡位置",curLocationInfor);
        bundle.putString("打卡状态",clockState);
        bundle.putString("打卡结果",feedBackResult.getMsg());
        bundle.putInt("打卡类型",clockType);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onResultForError() {

    }
}
