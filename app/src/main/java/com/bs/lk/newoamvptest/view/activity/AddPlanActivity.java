package com.bs.lk.newoamvptest.view.activity;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bs.lk.newoamvptest.R;
import com.bs.lk.newoamvptest.bean.CustomDate;
//import com.bs.lk.newoamvptest.dao.DBHelper;
//import com.bs.lk.newoamvptest.dao.ScheduleDao;
import com.bs.lk.newoamvptest.bean.Schedual;
import com.bs.lk.newoamvptest.util.DateUtil;
import com.bs.lk.newoamvptest.wheelview.DateUtils;
import com.bs.lk.newoamvptest.wheelview.JudgeDate;
import com.bs.lk.newoamvptest.wheelview.ScreenInfo;
import com.bs.lk.newoamvptest.wheelview.WheelMain;
import com.bs.lk.newoamvptest.wheelview.WheelWeekMain;
import com.bs.lk.newoamvptest.widget.SwitchButton;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 添加日程页面
 * @author lk
 */
public class AddPlanActivity extends Activity implements OnClickListener {
	private TextView mCancelTv;
	private TextView mConfirmTv;


	private CustomDate mCustomDate;
	private EditText mPlanContentTv;
	private TextView mSelectStartTime;
	private TextView mSelectEndTime;
	private TextView mStartPlanTimeTv;
	private TextView mEndPlanTimeTv;
	private TextView mNoCancelPlanTv;
	private TextView mConfirmCancelPlanTv;

	private SwitchButton mSwitchButton;
	private View mShowDialogLayout;
	private View mCancelDialogLayout;

	private String TABLE_NAME = "schedule.db";
	List<Schedual> scheduals = new ArrayList<>();

	private TextView tv_center;
	private WheelMain wheelMainDate;
	private WheelWeekMain wheelWeekMainDate;
	private String beginTime;
	private Schedual schedual = null;

	private AlarmManager alarmManager;
	private PendingIntent pendingIntent;





	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add_plan);
		getIntentData();
		findById();

		//获取闹钟管理者
		alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		schedual = new Schedual();
		schedual.setIsAllday(false);
		setTextData();


	}

	private void findById() {
		mCancelTv = (TextView) this.findViewById(R.id.cancel_tv);
		mConfirmTv = (TextView)this.findViewById(R.id.confirm_tv);
		mPlanContentTv = (EditText) this.findViewById(R.id.plan_content_tv);
		mStartPlanTimeTv = (TextView)this.findViewById(R.id.start_plan_time_tv);
		mEndPlanTimeTv = (TextView)this.findViewById(R.id.end_plan_time_tv);
		mNoCancelPlanTv = (TextView)this.findViewById(R.id.no_cancel_plan_tv);
		mConfirmCancelPlanTv = (TextView)this.findViewById(R.id.confirm_cancel_plan_tv);
		mSwitchButton = (SwitchButton) this.findViewById(R.id.checkbox);
		mShowDialogLayout = this.findViewById(R.id.dialog_show_layout);
		mCancelDialogLayout = this.findViewById(R.id.cancel_dialog_layout);
		mSelectStartTime = (TextView)this.findViewById(R.id.start_plan_time_tv);
		tv_center = (TextView) findViewById(R.id.tv_based_pop);;
		mSelectEndTime = (TextView)this.findViewById(R.id.end_plan_time_tv);

		setOnClickListener();
		
	}

	private void setTextData() {
		StringBuffer str = new StringBuffer();
		str.append(mCustomDate.toString());
		Log.i("huang", mCustomDate.toString());
		str.append(DateUtil.weekName[mCustomDate.week]);
		str.append(DateUtil.getHour());
		str.append(":");
		int minute1 = DateUtil.getMinute();
		str.append(minute1 < 10 ? "0"+minute1 : minute1);
		mStartPlanTimeTv.setText(str.toString());
		str = new StringBuffer();
		str.append(mCustomDate.toString());
		str.append(DateUtil.weekName[mCustomDate.week]);
		str.append(DateUtil.getHour()+1);
		str.append(":");
		int minute2 = DateUtil.getMinute();
		str.append(minute2 < 10 ? "0"+minute2 : minute2);
		mEndPlanTimeTv.setText(str.toString());
		
	}

	private void setOnClickListener() {
		mCancelTv.setOnClickListener(this);//取消监听
		mConfirmTv.setOnClickListener(this);//确定监听
		mPlanContentTv.setOnClickListener(this);//edittext 添加监听
		mStartPlanTimeTv.setOnClickListener(this);//开始
		mEndPlanTimeTv.setOnClickListener(this);//结束
		mNoCancelPlanTv.setOnClickListener(this);//取消
		mConfirmCancelPlanTv.setOnClickListener(this);//确定
		mSelectStartTime.setOnClickListener(this);
		mSelectEndTime.setOnClickListener(this);
		mSwitchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				if (b){
					schedual.setIsAllday(true);
				}else {
					schedual.setIsAllday(false);
				}
			}
		});

		
	}


	private void getIntentData(){
		 mCustomDate = (CustomDate)getIntent()
				 .getSerializableExtra(CalendarActivity.MAIN_ACTIVITY_CLICK_DATE);
	}
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancel_tv://activity_add_plan    取消
			showCancelDialogState(true);
			break;
		case R.id.no_cancel_plan_tv://comp_dialog_layout    取消
			showCancelDialogState(false);
			break;
		case R.id.confirm_cancel_plan_tv://comp_dialog_layout    确定
			Intent jumpTomainfromcancel = new Intent(getApplicationContext(),CalendarActivity.class);
			startActivity(jumpTomainfromcancel);
			finish();
			break;
		case R.id.confirm_tv://activity_add_plan      确定
			addSchedule();
			Intent jumpTomain = new Intent(getApplicationContext(),CalendarActivity.class);
			startActivity(jumpTomain);
			finish();
			break;
		case R.id.start_plan_time_tv:
			showStartWeekBottoPopupWindow();
			break;
		case R.id.end_plan_time_tv:
			showEndWeekBottoPopupWindow();
			break;



		}
	}

	private void addSchedule() {
		schedual.setContent(mPlanContentTv.getText().toString().trim());
		schedual.setStartTime(mStartPlanTimeTv.getText().toString().trim());
		schedual.setEndTime(mEndPlanTimeTv.getText().toString().trim());

		schedual.save();
		scheduals =  LitePal.findAll(Schedual.class);
		String schedualContent = "";
		for(int i = 0;i < scheduals.size();i++){
			schedualContent += scheduals.get(i).getContent();
			Log.e("schedualContent",""+schedualContent);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        	showCancelDialogState(true);
             return true;
         }
         return super.onKeyDown(keyCode, event);
     }
	
	private void showCancelDialogState(boolean isVisable){
		Animation anim = null;
		if(isVisable){
		    anim = AnimationUtils.loadAnimation(this, R.anim.slide_bottom_to_up);
			mCancelDialogLayout.setAnimation(anim);
			mShowDialogLayout.setVisibility(View.VISIBLE);
			
			
		}else{
			anim = AnimationUtils.loadAnimation(this, R.anim.slide_up_to_bottom);
			mCancelDialogLayout.setAnimation(anim);
			mShowDialogLayout.setVisibility(View.GONE);
		}
	}



	private java.text.DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	public void showStartWeekBottoPopupWindow() {
		WindowManager manager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
		Display defaultDisplay = manager.getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics();
		defaultDisplay.getMetrics(outMetrics);
		int width = outMetrics.widthPixels;
		View menuView = LayoutInflater.from(this).inflate(R.layout.show_week_popup_window,null);
		final PopupWindow mPopupWindow = new PopupWindow(menuView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
//		final PopupWindow mPopupWindow = new PopupWindow(menuView, (int)(width*0.8),
//				ActionBar.LayoutParams.WRAP_CONTENT);
		ScreenInfo screenInfoDate = new ScreenInfo(this);
		wheelWeekMainDate = new WheelWeekMain(menuView, true);
		wheelWeekMainDate.screenheight = screenInfoDate.getHeight();
		String time = DateUtils.currentMonth().toString();
		Calendar calendar = Calendar.getInstance();
		if (JudgeDate.isDate(time, "yyyy-MM-DD")) {
			try {
				calendar.setTime(new Date(time));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hours = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		wheelWeekMainDate.initDateTimePicker(year, month, day, hours,minute);
		final String currentTime = wheelWeekMainDate.getTime().toString();
		mPopupWindow.setAnimationStyle(R.style.AnimationPreview);
		mPopupWindow.setTouchable(true);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.showAtLocation(tv_center, Gravity.CENTER, 0, 0);
		mPopupWindow.setOnDismissListener(new poponDismissListener());
		backgroundAlpha(0.6f);
		TextView tv_cancle = (TextView) menuView.findViewById(R.id.tv_cancle);
		TextView tv_ensure = (TextView) menuView.findViewById(R.id.tv_ensure);
		TextView tv_pop_title = (TextView) menuView.findViewById(R.id.tv_pop_title);
		tv_pop_title.setText("选择起始时间");
		tv_cancle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mPopupWindow.dismiss();
				backgroundAlpha(1f);
			}
		});
		tv_ensure.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				beginTime = wheelWeekMainDate.getTime().toString();
				mSelectStartTime.setText(DateUtils.formateStringH(beginTime,DateUtils.yyyyMMddHHmm));
				mPopupWindow.dismiss();
				backgroundAlpha(1f);
			}
		});
	}
	public void showEndWeekBottoPopupWindow() {
		WindowManager manager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
		Display defaultDisplay = manager.getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics();
		defaultDisplay.getMetrics(outMetrics);
		int width = outMetrics.widthPixels;
		View menuView = LayoutInflater.from(this).inflate(R.layout.show_week_popup_window,null);

		final PopupWindow mPopupWindow = new PopupWindow(menuView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);

//		final PopupWindow mPopupWindow = new PopupWindow(menuView, (int)(width*0.8),
//				ActionBar.LayoutParams.WRAP_CONTENT);
		ScreenInfo screenInfoDate = new ScreenInfo(this);
		wheelWeekMainDate = new WheelWeekMain(menuView, true);
		wheelWeekMainDate.screenheight = screenInfoDate.getHeight();
		String time = DateUtils.currentMonth().toString();
		Calendar calendar = Calendar.getInstance();
		if (JudgeDate.isDate(time, "yyyy-MM-DD")) {
			try {
				calendar.setTime(new Date(time));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hours = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		wheelWeekMainDate.initDateTimePicker(year, month, day, hours,minute);
		final String currentTime = wheelWeekMainDate.getTime().toString();
		mPopupWindow.setAnimationStyle(R.style.AnimationPreview);
		mPopupWindow.setTouchable(true);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.showAtLocation(tv_center, Gravity.CENTER, 0, 0);
		mPopupWindow.setOnDismissListener(new poponDismissListener());
		backgroundAlpha(0.6f);
		TextView tv_cancle = (TextView) menuView.findViewById(R.id.tv_cancle);
		TextView tv_ensure = (TextView) menuView.findViewById(R.id.tv_ensure);
		TextView tv_pop_title = (TextView) menuView.findViewById(R.id.tv_pop_title);
		tv_pop_title.setText("选择结束时间");
		tv_cancle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mPopupWindow.dismiss();
				backgroundAlpha(1f);
			}
		});
		tv_ensure.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				beginTime = wheelWeekMainDate.getTime().toString();
				mSelectEndTime.setText(DateUtils.formateStringH(beginTime,DateUtils.yyyyMMddHHmm));
				mPopupWindow.dismiss();
				backgroundAlpha(1f);
			}
		});
	}
	public void backgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = bgAlpha;
		getWindow().setAttributes(lp);
	}
	class poponDismissListener implements PopupWindow.OnDismissListener {
		@Override
		public void onDismiss() {
			backgroundAlpha(1f);
		}

	}

	/**
	 * 设置闹钟提醒
	 * @param view
	 */
	public void setAlarmOne(View view){
		//获取系统当前时间
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		//弹出对话框
		TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
			boolean fired = false;
			@Override
			public void onTimeSet(TimePicker timePicker, int i, int i1) {
				if (fired == true){
					fired = false;
				}else {
					fired = true;
					Calendar c = Calendar.getInstance();
					c.set(Calendar.HOUR_OF_DAY,i);
					c.set(Calendar.MINUTE,i1);
					//当时间一到,将执行的响应
					Intent intent = new Intent(getApplicationContext(),RingActivity.class);
					PendingIntent pendingIntent= PendingIntent.getBroadcast(AddPlanActivity.this,0x101,intent,0);
					alarmManager.set(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pendingIntent);
					startActivity(intent);
				}
			}
		},hour,minute,true);
		timePickerDialog.show();
	}

}
