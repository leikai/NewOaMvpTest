package com.bs.lk.newoamvptest.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CountDownService extends Service {

    private RelativeLayout countDown;
    // 倒计时
    private String daysTv, hoursTv, minutesTv, secondsTv;
    private long mDay = 30;
    private long mHour = 00;
    private long mMin = 00;
    private long mSecond = 03;// 天 ,小时,分钟,秒
    private boolean isRun = true;
    private Handler timeHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==1) {
                computeTime();
                daysTv = mDay+"";
                Log.e("daysTv",""+daysTv);
                hoursTv = mHour+"";
                Log.e("hoursTv",""+hoursTv);
                minutesTv = mMin+"";
                Log.e("minutesTv",""+minutesTv);
                secondsTv = mSecond+"";
                Log.e("secondsTv",""+secondsTv);
                if (mDay==0&&mHour==0&&mMin==0&&mSecond==0) {
//                    countDown.setVisibility(View.GONE);
                }
            }
        }
    };


    public CountDownService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * 服务创建时调用
     */
    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * 服务开始时调用
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        startRun();

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 销毁服务时调用，回收资源
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    /**
     * 开启倒计时
     */
    private void startRun() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (isRun) {
                    try {
                        Thread.sleep(1000); // sleep 1000ms
                        Message message = Message.obtain();
                        message.what = 1;
                        timeHandler.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * 倒计时计算
     */
    private void computeTime() {
        mSecond--;
        if (mSecond < 0) {
            mMin--;
            mSecond = 59;
            if (mMin < 0) {
                mMin = 59;
                mHour--;
                if (mHour < 0) {
                    // 倒计时结束
                    mHour = 23;
                    mDay--;
                }
            }
        }
    }
}
