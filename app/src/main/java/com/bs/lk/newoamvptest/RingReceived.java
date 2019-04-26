package com.bs.lk.newoamvptest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.bs.lk.newoamvptest.view.activity.RingActivity;


/**
 * Created by 殇冰无恨 on 2017/11/20.
 */

public class RingReceived extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if("com.example.mycalender.Ring".equals(intent.getAction())){
            Log.i("test1","收到广播了");
            //跳转到Activity
            Intent intent1=new Intent(context, RingActivity.class);
            //给Intent设置标志位Flag
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        }
    }
}
