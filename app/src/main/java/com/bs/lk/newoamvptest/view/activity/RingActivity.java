package com.bs.lk.newoamvptest.view.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bs.lk.newoamvptest.R;


/**
 * Created by 殇冰无恨 on 2017/11/20.
 */

public class RingActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring);
        //播放音乐
        mediaPlayer = MediaPlayer.create(this, R.raw.nqmm);
        mediaPlayer.start();

        //显示通知栏
        NotificationCompat.Builder notificationCompat=new NotificationCompat.Builder(this);
        //设置参数
        notificationCompat.setDefaults(NotificationCompat.DEFAULT_ALL);
        notificationCompat.setContentTitle("提醒");
        notificationCompat.setContentText("您有一件事情需要办理，请及时处理");
        notificationCompat.setSmallIcon(android.R.drawable.ic_lock_idle_alarm);
        Notification notification=notificationCompat.build();


        //通知管理器
        NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //发送通知
        notificationManager.notify(0x101,notification);

    }
    public void close(View view){
        mediaPlayer.stop();
        finish();
    }
}
