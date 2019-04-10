package com.bs.lk.newoamvptest.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.lk.newoamvptest.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 应用界面
 */
public class SendSelectActivity extends FragmentActivity implements View.OnClickListener {

    @BindView(R.id.send_one_cabinet)
    LinearLayout sendOneCabinet;
    @BindView(R.id.send_one_message_machine)
    LinearLayout sendOneMessageMachine;
    @BindView(R.id.send_one_schedule)
    LinearLayout sendOneSchedule;//日程
    @BindView(R.id.send_one_attendance_card)
    LinearLayout sendOneAttendanceCard;//签到
    @BindView(R.id.send_one_personal_attendance_statistics)
    LinearLayout sendOnePersonalAttendanceStatistics;
    @BindView(R.id.send_one_department_attendance_statistics)
    LinearLayout sendOneDepartmentAttendanceStatistics;
    @BindView(R.id.send_one_close_img)
    ImageView sendOneCloseImg;
    @BindView(R.id.send_tab_one)
    TableLayout sendTabOne;
    @BindView(R.id.send_two_qiu_img)
    ImageView sendTwoQiuImg;
    @BindView(R.id.send_two_qiu_tv)
    TextView sendTwoQiuTv;
    @BindView(R.id.send_two_qiu)
    LinearLayout sendTwoQiu;
    @BindView(R.id.send_two_chu_img)
    ImageView sendTwoChuImg;
    @BindView(R.id.send_two_chu_tv)
    TextView sendTwoChuTv;
    @BindView(R.id.send_two_chu)
    LinearLayout sendTwoChu;
    @BindView(R.id.send_two_null)
    LinearLayout sendTwoNull;
    @BindView(R.id.send_two_back_img)
    ImageView sendTwoBackImg;
    @BindView(R.id.send_two_close_img)
    ImageView sendTwoCloseImg;
    @BindView(R.id.send_tab_two)
    TableLayout sendTabTwo;

    private SendSelectActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_select);
        context = SendSelectActivity.this;
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendTabOne.setVisibility(View.VISIBLE);
        sendTabOne.setAnimation(moveToViewLocation());


        sendOneCabinet.setOnClickListener(this);
        sendOneMessageMachine.setOnClickListener(this);
        //日程
        sendOneSchedule.setVisibility(View.VISIBLE);
        sendOneSchedule.setOnClickListener(this);
        //考勤打卡
        sendOneAttendanceCard.setVisibility(View.VISIBLE);
        sendOneAttendanceCard.setOnClickListener(this);
        sendOnePersonalAttendanceStatistics.setVisibility(View.VISIBLE);
        sendOnePersonalAttendanceStatistics.setOnClickListener(this);
        sendOneDepartmentAttendanceStatistics.setVisibility(View.VISIBLE);
        sendOneDepartmentAttendanceStatistics.setOnClickListener(this);

        sendOneCloseImg.setOnClickListener(this);
        sendTwoCloseImg.setOnClickListener(this);
        sendTwoBackImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_one_close_img:
                finish();
                break;
            case R.id.send_two_close_img:
                finish();
                break;
            case R.id.send_two_back_img:
                sendTabTwo.setVisibility(View.GONE);
                sendTabTwo.setAnimation(moveToRightHide());
                sendTabOne.setVisibility(View.VISIBLE);
                sendTabOne.setAnimation(moveToRightShow());
                break;
            case R.id.send_one_cabinet:

//                startActivity(new Intent(SendSelectActivity.this,CabinetActivity.class));
                Toast.makeText(context, "跳转至云柜", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.send_one_message_machine:
//                startActivity(new Intent(SendSelectActivity.this,MachineActivity.class));
                Toast.makeText(context, "跳转至留言机", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.send_one_schedule://日程
//                sendTwoQiuImg.setImageResource(R.mipmap.pic_fabu_shebei_zl);
//                sendTwoChuImg.setImageResource(R.mipmap.pic_fabu_shebei_cz);
//                sendTwoQiuTv.setText("设备求租");
//                sendTwoChuTv.setText("设备出租");
//                sendTabOne.setVisibility(View.GONE);
//                sendTabOne.setAnimation(moveToLeftHide());
//                sendTabTwo.setVisibility(View.VISIBLE);
//                sendTabTwo.setAnimation(moveToLeftShow());
//                sendTwoQiu.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(SendSelectActivity.this, "设备出租", Toast.LENGTH_SHORT).show();
//                        finish();
//                    }
//                });
//                sendTwoChu.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(SendSelectActivity.this, "设备求租", Toast.LENGTH_SHORT).show();
//                        finish();
//                    }
//                });

                Toast.makeText(context, "跳转至日程", Toast.LENGTH_SHORT).show();
                break;
                //考勤打卡
            case R.id.send_one_attendance_card:
               startActivity(new Intent(SendSelectActivity.this,AttendanceListActivity.class));
                Toast.makeText(context, "跳转至考勤打卡", Toast.LENGTH_SHORT).show();
               break;
               //考勤统计
            case R.id.send_one_personal_attendance_statistics:
                startActivity(new Intent(SendSelectActivity.this,AttendanceStatisticsActivity.class));
                Toast.makeText(context, "跳转至考勤统计", Toast.LENGTH_SHORT).show();
                break;
            case R.id.send_one_department_attendance_statistics:
                Toast.makeText(context, "跳转至部门考勤统计", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(SendSelectActivity.this,AllAttendanceStatisticsActivity.class));

                break;
            default:
                break;
        }
    }
    /**
     * 从控件所在位置移动到控件的底部
     */
    public static TranslateAnimation moveToViewBottom() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        mHiddenAction.setDuration(300);
        return mHiddenAction;
    }


    /**
     * 从控件的底部移动到控件所在位置
     */
    public static TranslateAnimation moveToViewLocation() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(300);
        return mHiddenAction;
    }

    /**
     * 从控件所在位置移动到左侧
     */
    public static TranslateAnimation moveToLeftHide() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(300);
        return mHiddenAction;
    }

    /**
     * 从右侧移动到控件所在位置
     */
    public static TranslateAnimation moveToLeftShow() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(300);
        return mHiddenAction;
    }

    /**
     * 从控件所在位置移动到右侧
     */
    public static TranslateAnimation moveToRightHide() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(300);
        return mHiddenAction;
    }

    /**
     * 从左侧移动到控件所在位置
     */
    public static TranslateAnimation moveToRightShow() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(300);
        return mHiddenAction;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
