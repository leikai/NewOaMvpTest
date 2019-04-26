package com.bs.lk.newoamvptest.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.bs.lk.newoamvptest.R;
import com.bs.lk.newoamvptest.bean.CustomDate;
import com.bs.lk.newoamvptest.bean.Schedual;
//import com.bs.lk.newoamvptest.dao.DBHelper;
//import com.bs.lk.newoamvptest.dao.ScheduleDao;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import static com.bs.lk.newoamvptest.view.activity.CalendarActivity.MAIN_ACTIVITY_CLICK_DATE;


/**
 *
 * @author 殇冰无恨
 * @date 2017/11/20
 */

public class ScheduleActivity extends Activity {
    private TextView tvContent;
    private TextView tvRemindCreateTime;
    private TextView tvRemindEndTime;
    private TextView tvRemindAlarmTime;
    private Button btnCreate;
    private Button btnDelete;

//    private ScheduleDao scheduleDao;
//    private DBHelper dbHelper = DBHelper.getInstance();
    private String TABLE_NAME = "schedule.db";

    private int i = 0;
    List<Schedual> scheduals= new ArrayList<>();

    private CustomDate mClickDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_detail);
        getIntentData();
        initView();
        scheduals = LitePal.findAll(Schedual.class);
        bindData();
        initEvent();
    }

    private void initEvent() {
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scheduals.get(i).delete();
                Intent jumptoCalendarActivity = new Intent(ScheduleActivity.this,CalendarActivity.class);
                jumptoCalendarActivity.putExtra("i",i);
                startActivity(jumptoCalendarActivity);
                finish();
            }
        });
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickDate = new CustomDate();
                Intent jumptoAdd = new Intent(getApplicationContext(),AddPlanActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable(MAIN_ACTIVITY_CLICK_DATE,mClickDate);
                jumptoAdd.putExtras(mBundle);
                startActivity(jumptoAdd);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
            }
        });
    }

    private void bindData() {
        if (scheduals.size()!=0){
            tvContent.setText(scheduals.get(i).getContent());
            tvRemindCreateTime.setText(scheduals.get(i).getStartTime());
            tvRemindEndTime.setText(scheduals.get(i).getEndTime());
            tvRemindAlarmTime.setText(scheduals.get(i).getStartTime());
        }else {
            tvContent.setText("");
            tvRemindCreateTime.setText("");
            tvRemindEndTime.setText("");
            tvRemindAlarmTime.setText("");
        }

    }

    private void getIntentData() {
        i = getIntent().getIntExtra("i",0);
//        i = getIntent().getExtras().getInt("i");
    }

    private void initView() {
        tvContent = (TextView) findViewById(R.id.tv_content);
        tvRemindCreateTime = (TextView)findViewById(R.id.tv_remind_create);
        tvRemindEndTime = (TextView)findViewById(R.id.tv_remind_end);
        tvRemindAlarmTime = (TextView)findViewById(R.id.tv_remind_alarm);
        btnCreate = (Button) findViewById(R.id.btn_create);
        btnDelete = (Button) findViewById(R.id.btn_delete);
    }


}
