package com.bs.lk.newoamvptest.view.activity.fragment;

import android.annotation.SuppressLint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

//import org.sxchinacourt.R;
//import org.sxchinacourt.activity.AbNormalSmartTableActivity;
//import org.sxchinacourt.activity.SmartTableActivity;

import com.bs.lk.newoamvptest.CApplication;
import com.bs.lk.newoamvptest.R;
import com.bs.lk.newoamvptest.adapter.DepartmentAdapter;
import com.bs.lk.newoamvptest.bean.DepartmentNewBean;
import com.bs.lk.newoamvptest.bean.UserNewBean;
import com.bs.lk.newoamvptest.util.WebServiceUtil;
import com.bs.lk.newoamvptest.view.activity.AllAttendanceStatisticsActivity;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

//import static org.sxchinacourt.activity.SmartTableActivity.table;

public class SearchAreaFragment extends Fragment {
    private TextView userName;
    private EditText userType;
    private Spinner spinnerDepartment;
    private EditText startDate;
    private EditText endDate;
    private Button selectStart;
    private Button selectEnd;
    private Button searchConfirm;
    private Button btnCancle;
    private Button btnConfirm;

    private String userNameString;
    private String userTypeString;
    private String userDeptString;
    private String userStartDateString;
    private String userEndDateString;

    // 定义5个记录当前时间的变量
    private int yearMain;
    private int monthMain;
    private int dayMain;
    private int hourMain;
    private int minuteMain;
    private String monStr;
    private String dayStr;

    private UserNewBean curUser;

    /**
     * 法院ID
     */
    private String orgId;
    /**
     * 法院名称
     */
    private String orgName;

    /**
     * 部门分组集合
     */
    public List<DepartmentNewBean> mDepartments = new ArrayList<>();
    /**
     * 联系人分组集合
     */
    public List<UserNewBean> mUsersList =  new ArrayList<>();

    private RefreshGroupTask mRefreshGroupTask;
    private String token;





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_area,container,false);
        userName = (TextView) view.findViewById(R.id.tv_smart_user_name);
        spinnerDepartment = (Spinner) view.findViewById(R.id.spinner_department);
        startDate = (EditText) view.findViewById(R.id.et_smart_start);
        endDate = (EditText) view.findViewById(R.id.et_smart_end);
        selectStart = (Button) view.findViewById(R.id.btn_select_start);
        selectEnd = (Button) view.findViewById(R.id.btn_select_end);
        searchConfirm = (Button) view.findViewById(R.id.btn_search_confirm);
        curUser = CApplication.getInstance().getCurrentUser();
        token =CApplication.getInstance().getCurrentToken();
        userName.setText(curUser.getEmpname());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //查询数据库中的数据
        mDepartments = LitePal.where("orgname = ?",curUser.getOrgname()).find(DepartmentNewBean.class);

        if (mDepartments.size() == 0){
            mRefreshGroupTask = new RefreshGroupTask(curUser.getOrgid());
            mRefreshGroupTask.execute();
        }else {
            setDeptData();
        }





        selectStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                // 引入窗口配置文件
                View viewPop = inflater.inflate(R.layout.view_popupwindow_date_picker_dialog, null);
                final DatePicker datePicker = (DatePicker) viewPop.findViewById(R.id.date_pick);
                btnCancle = (Button)viewPop.findViewById(R.id.btn_cancel);
                btnConfirm = (Button) viewPop.findViewById(R.id.btn_confirm);
                // 获取当前的时间

                // Calendar ca = Calendar.getInstance();
                Calendar ca = Calendar.getInstance();

                yearMain = ca.get(Calendar.YEAR);
                monthMain = ca.get(Calendar.MONTH);
                dayMain = ca.get(Calendar.DAY_OF_MONTH);
                hourMain = ca.get(Calendar.HOUR);
                minuteMain = ca.get(Calendar.MINUTE);
                Date d = new Date();
                d.setTime(System.currentTimeMillis());



                // 初始化DatePicker控件
                datePicker.init(yearMain, monthMain, dayMain, new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        yearMain = year;
                        monthMain = monthOfYear;
                        dayMain = dayOfMonth;

                    }
                });

                // 创建PopupWindow对象
                final PopupWindow pop = new PopupWindow(viewPop, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, false);
                pop.setAnimationStyle(R.style.mypopwindow_anim_style);
                ColorDrawable dw = new ColorDrawable(0xb0000000);
                pop.setBackgroundDrawable(dw);
                // 需要设置一下此参数，点击外边可消失
                pop.setBackgroundDrawable(new BitmapDrawable());
                //设置点击窗口外边窗口消失
                pop.setOutsideTouchable(true);
                // 设置此参数获得焦点，否则无法点击
                pop.setFocusable(true);


                if(pop.isShowing()) {
                    // 隐藏窗口，如果设置了点击窗口外小时即不需要此方式隐藏
                    pop.dismiss();
                    // 显示当前时间

                    showDate(yearMain,monthMain,dayMain,hourMain,minuteMain,startDate);
                } else {
                    // 显示窗口
                    if (getActivity() instanceof AllAttendanceStatisticsActivity){
                        pop.showAtLocation(AllAttendanceStatisticsActivity.llContent, Gravity.CENTER,0,0);
                    }


                }
                btnCancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pop.dismiss();
                    }
                });
                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pop.dismiss();
                        // 显示当前时间
                        int day = datePicker.getDayOfMonth();
                        Log.e("日：",""+day);
                        int month = datePicker.getMonth();
                        Log.e("月",""+month);
                        int year = datePicker.getYear();
                        Log.e("年",""+year);
                        showDate(yearMain,monthMain,dayMain,hourMain,minuteMain,startDate);

                    }
                });
            }
        });
        selectEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                // 引入窗口配置文件
                View viewPop = inflater.inflate(R.layout.view_popupwindow_date_picker_dialog, null);
                DatePicker datePicker = (DatePicker) viewPop.findViewById(R.id.date_pick);
                btnCancle = (Button)viewPop.findViewById(R.id.btn_cancel);
                btnConfirm = (Button) viewPop.findViewById(R.id.btn_confirm);
                // 获取当前的时间

                // Calendar ca = Calendar.getInstance();
                Calendar ca = Calendar.getInstance();

                yearMain = ca.get(Calendar.YEAR);
                monthMain = ca.get(Calendar.MONTH);
                dayMain = ca.get(Calendar.DAY_OF_MONTH);
                hourMain = ca.get(Calendar.HOUR);
                minuteMain = ca.get(Calendar.MINUTE);
                Date d = new Date();
                d.setTime(System.currentTimeMillis());



                // 初始化DatePicker控件
                datePicker.init(yearMain, monthMain, dayMain, new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        yearMain = year;
                        monthMain = monthOfYear;
                        dayMain = dayOfMonth;

                    }
                });

                // 创建PopupWindow对象
                final PopupWindow pop = new PopupWindow(viewPop, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, false);
                pop.setAnimationStyle(R.style.mypopwindow_anim_style);
                ColorDrawable dw = new ColorDrawable(0xb0000000);
                pop.setBackgroundDrawable(dw);
                // 需要设置一下此参数，点击外边可消失
                pop.setBackgroundDrawable(new BitmapDrawable());
                //设置点击窗口外边窗口消失
                pop.setOutsideTouchable(true);
                // 设置此参数获得焦点，否则无法点击
                pop.setFocusable(true);


                if(pop.isShowing()) {
                    // 隐藏窗口，如果设置了点击窗口外小时即不需要此方式隐藏
                    pop.dismiss();
                    // 显示当前时间
                    showDate(yearMain,monthMain,dayMain,hourMain,minuteMain,endDate);
                } else {
                    // 显示窗口
                    if (getActivity() instanceof AllAttendanceStatisticsActivity){
                        pop.showAtLocation(AllAttendanceStatisticsActivity.llContent, Gravity.CENTER,0,0);
                    }

                }
                btnCancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pop.dismiss();
                    }
                });
                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pop.dismiss();
                        // 显示当前时间
                        showDate(yearMain,monthMain,dayMain,hourMain,minuteMain,endDate);

                    }
                });
            }
        });





        searchConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() instanceof AllAttendanceStatisticsActivity){
//                    userNameString = userName.getText().toString();
//                    userTypeString = userType.getText().toString();
//                    userDeptString = spinnerDepartment.getit
                    userStartDateString = startDate.getText().toString();
                    userEndDateString = endDate.getText().toString();
                    AllAttendanceStatisticsActivity activity = (AllAttendanceStatisticsActivity) getActivity();
                    activity.drawerLayout.closeDrawers();
//                    activity.swipRefresh.setRefreshing(true);
                    activity.requestSmartData(curUser.getOrgname(),userDeptString,userStartDateString,userEndDateString);

                }
            }
        });


    }


    private void setDeptData() {
        DepartmentNewBean departmentNewBean = new DepartmentNewBean();
        departmentNewBean.setDeptName("所有部门");
        departmentNewBean.setDeptPid("");
        orgId = mDepartments.get(1).getOrgid();
        orgName = mDepartments.get(1).getOrgname();
        departmentNewBean.setOrgid(orgId);
        departmentNewBean.setOrgname(orgName);
        mDepartments.add(0,departmentNewBean);
        setDateToView(mDepartments);
    }

    /**
     * 主线程接收部门数据
     */
    @SuppressLint("HandlerLeak")
    private Handler mUpdateGroupsListHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            mDepartments = (List<DepartmentNewBean>) msg.obj;
            DepartmentNewBean departmentNewBean = new DepartmentNewBean();
            departmentNewBean.setDeptName("所有部门");
            departmentNewBean.setDeptPid("");
            orgId = mDepartments.get(1).getOrgid();
            orgName = mDepartments.get(1).getOrgname();
            departmentNewBean.setOrgid(orgId);
            departmentNewBean.setOrgname(orgName);
            mDepartments.add(0,departmentNewBean);
            setDateToView(mDepartments);

        }
    };


    /**
     * 将显示部门数据
     * @param mList
     */
    private  void  setDateToView(final List<DepartmentNewBean> mList){
        DepartmentAdapter departmentAdapter = new DepartmentAdapter(getActivity(), mList);
        //传入的参数分别为 Context , 未选中项的textview , 数据源List
        spinnerDepartment.setAdapter(departmentAdapter);
        departmentAdapter.notifyDataSetChanged();
        spinnerDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String departmentId = mList.get(position).getOid();
                userDeptString = mList.get(position).getDeptName();
                String courtoaName = mList.get(position).getOrgname();
                Log.e("所属法院",""+courtoaName);
                List<UserNewBean> usersList = mList.get(position).getUserNewBeanList();
                mUsersList.clear();
                for (int i= 0; i< usersList.size();i++){
                    if (courtoaName.equals(usersList.get(i).getOrgname())){
                        mUsersList.add(usersList.get(i));
                    }
                }
                Log.e("所属法院人员",""+mUsersList.size());
                int ceshi = mUsersList.size();
                Log.e("部门人员数据",""+ceshi);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * 请求部门的数据
     */
    private class RefreshGroupTask extends AsyncTask<Void, Void, List<DepartmentNewBean>> {
        String mCourtoaName;

        RefreshGroupTask(String courtoaOid) {
            //法院Oid
            this.mCourtoaName = courtoaOid;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected List<DepartmentNewBean> doInBackground(Void... params) {
            Log.e("法院名称",""+mCourtoaName);
            List<DepartmentNewBean> departments = WebServiceUtil.getInstance().getDepartmentList(token,mCourtoaName);
            LitePal.saveAll(departments);
            return departments;
        }

        @Override
        protected void onPostExecute(List<DepartmentNewBean> departments) {
            if (departments != null) {
                Message msg = mUpdateGroupsListHandler.obtainMessage();
                msg.obj = departments;
                mUpdateGroupsListHandler.sendMessage(msg);
            }
        }

        @Override
        protected void onCancelled() {
        }
    }


    private void showDate(int year, int month, int day, int hour, int minute, EditText etView) {
        if (month<10){
            monStr = "0"+String.valueOf(month+1);
        }else {
            monStr = String.valueOf(month+1);
        }
        if (day<10){
            dayStr = "0"+String.valueOf(day);
        }else {
            dayStr = String.valueOf(day);
        }
        etView.setText( year+"-"+monStr+"-"+dayStr);
    }

}
