package com.bs.lk.newoamvptest.view.activity;

import android.Manifest;
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
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
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
import com.bs.lk.newoamvptest.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 签到页面
 */
public class SignInActivity extends AppCompatActivity implements SensorEventListener, View.OnClickListener {

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
    private SensorManager mSensorManager;//方向传感器
    private LatLng mDestinationPoint;//目的地坐标点
    private InfoWindow mInfoWindow;//地图文字位置提醒
    private boolean isFirstLocate = true;
    private Double lastX = 0.0;
    private int mCurrentDirection = 0;

    private MyLocationData locData;//定位坐标
    private LatLng mCenterPos;
    private double mDistance = 0;
    private float mZoomScale = 0; //比例
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationClient = new LocationClient(getApplicationContext());
//        mLocationClient.registerLocationListener(new MyLocationListener());
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_sign_in);
        setSupportActionBar(toolBar);
        ButterKnife.bind(this);
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
        mHandler.post(run);//设置系统时间
        arriverBt.setOnClickListener(this);

    }

    private void initBaiduMap() {
        baiduMap = mapview.getMap();
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        baiduMap.setMyLocationEnabled(true);//开启定位图层
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);//获取传感器管理服务
        tvTitle.setText("打卡签到");

    }

    private void navigateTo(BDLocation location) {
        if (isFirstLocate) {
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            baiduMap.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomTo(16f);//3-19值越大，地图显示的信息越详细
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
        mSensorManager.unregisterListener(this);//取消注册传感器监听
    }

    private void requestLocation() {
        initLocation();//初始化定位设置
        mLocationClient.start();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度（优先GPS模式，其次网络模式），高精度，低功耗
        option.setCoorType("bd0911");//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用一般是bd0911
        option.setScanSpan(5000);//可选默认是0，即仅定位一次，设置发起连续定位请求的间隔必须大于1s才有效
        option.setIsNeedAddress(true);//设置是否需要地址信息，默认否
        option.setIsNeedLocationDescribe(true);//设置是否需要地址描述
        option.setNeedDeviceDirect(true);//设置是否需要设备方向结果
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setIsNeedLocationDescribe(false);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(false);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setOpenGps(true);//可选，默认false，设置是否开启Gps定位
        option.setIsNeedAltitude(false);//可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(BDAbstractLocationListener);
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

    //设置打卡目标范围圈
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
            baiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(mCenterPos, mZoomScale));//缩放
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
        List<Double> longItems = new ArrayList<Double>();    //经度集合
        List<Double> latItems = new ArrayList<Double>();     //纬度集合

        if (null != LocationPoint) {
            longItems.add(LocationPoint.longitude);
            latItems.add(LocationPoint.latitude);
        }
        if (null != mDestinationPoint) {
            longItems.add(mDestinationPoint.longitude);
            latItems.add(mDestinationPoint.latitude);
        }

        maxLong = longItems.get(0);    //最大经度
        minLong = longItems.get(0);    //最小经度
        maxLat = latItems.get(0);     //最大纬度
        minLat = latItems.get(0);     //最小纬度

        for (int i = 0; i < longItems.size(); i++) {
            maxLong = Math.max(maxLong, longItems.get(i));   //获取集合中的最大经度
            minLong = Math.min(minLong, longItems.get(i));   //获取集合中的最小经度
        }

        for (int i = 0; i < latItems.size(); i++) {
            maxLat = Math.max(maxLat, latItems.get(i));   //获取集合中的最大纬度
            minLat = Math.min(minLat, latItems.get(i));   //获取集合中的最小纬度
        }
        double latCenter = (maxLat + minLat) / 2;
        double longCenter = (maxLong + minLong) / 2;
        int jl = (int) getDistance(new LatLng(maxLat, maxLong), new LatLng(minLat, minLong));//缩放比例参数
        mCenterPos = new LatLng(latCenter, longCenter);   //获取中心点经纬度
        int zoomLevel[] = {2500000, 2000000, 1000000, 500000, 200000, 100000,
                50000, 25000, 20000, 10000, 5000, 2000, 1000, 500, 100, 50, 20, 0};
        int i;
        for (i = 0; i < 18; i++) {
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
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");// HH:mm:ss
            Date date = new Date(System.currentTimeMillis());//获取当前时间
            arriverTimetv.setText(simpleDateFormat.format(date)); //更新时间
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
                    Toast.makeText(this, "打卡成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "外勤打卡", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }
}
