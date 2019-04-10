package com.bs.lk.newoamvptest.view.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.bg.BaseBackgroundFormat;
import com.bin.david.form.data.format.bg.BaseCellBackgroundFormat;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.TableData;
import com.bin.david.form.listener.OnColumnItemClickListener;
import com.bs.lk.newoamvptest.R;
import com.bs.lk.newoamvptest.bean.AbNormalAttendanceDetailsBean;
import com.bs.lk.newoamvptest.bean.AttendanceStatisticsBean;
import com.bs.lk.newoamvptest.bean.NormalAttendanceDetailsBean;
import com.bs.lk.newoamvptest.bean.NormalAttendanceTimesBean;
import com.bs.lk.newoamvptest.util.WebServiceUtil;
import com.geek.thread.GeekThreadManager;
import com.geek.thread.ThreadPriority;
import com.geek.thread.ThreadType;
import com.geek.thread.task.GeekRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * 异常考勤详情统计
 * @author lk
 */
public class AbNormalAttendanceActivity extends AppCompatActivity {
    private com.bin.david.form.core.SmartTable<AbNormalAttendanceDetailsBean> table;
    private List<AbNormalAttendanceDetailsBean> list = new ArrayList<>();
    private String jsonstr = "";
    private List<AbNormalAttendanceDetailsBean> templist;
    private String start_date;
    private String end_date;
    private String ly_user_id;
    private String kq_state;
    private TextView tvEmptyData;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_RESPONSE_EMPLOYEE:
                    templist = (ArrayList)msg.obj;
                    Column<String> username = new Column<>("姓名", "username");
                    username.setFixed(true);
                    Column<String> deptname = new Column<>("部门", "deptname");
                    Column<String> usertype = new Column<>("分类", "usertype");
                    Column<String> start_date = new Column<>("上班时间", "start_date");
                    Column<String> end_date = new Column<>("下班时间", "end_date");
                    Column<String> start_state = new Column<>("上班状态", "start_state");
                    Column<String> end_state = new Column<>("下班状态", "end_state");
                    Column<String> qjsc = new Column<>("请假（小时）", "qjsc");

                    String deptName = templist.get(0).getDeptname();
                    TableData<AbNormalAttendanceDetailsBean> tableData = new TableData<AbNormalAttendanceDetailsBean>("异常考勤详情统计表",templist,
                            username,deptname,usertype,start_date,end_date,start_state,end_state,qjsc
                    );
                    table.setTableData(tableData);
                    break;
                case SHOW_RESPONSE_EMPTY_DATA:
                    table.setVisibility(View.GONE);
                    tvEmptyData.setVisibility(View.VISIBLE);
                    break;
                case SHOW_RESPONSE_ABNORMAL_DATA:
                    table.setVisibility(View.GONE);
                    tvEmptyData.setText("网络问题，数据异常请重试！");
                    tvEmptyData.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }

    };

    public static final int SHOW_RESPONSE_EMPLOYEE = 0;
    public static final int SHOW_RESPONSE_EMPTY_DATA = 1;
    public static final int SHOW_RESPONSE_ABNORMAL_DATA = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ab_normal_attendance);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        table = findViewById(R.id.abnormal_attendance_table);
        tvEmptyData = findViewById(R.id.tv_abnormal);
        getData();
        table.getConfig().setContentCellBackgroundFormat(new BaseCellBackgroundFormat<CellInfo>() {
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
                if (cellInfo.row%2 == 0){
                    return ContextCompat.getColor(AbNormalAttendanceActivity.this,R.color.tableBackground);
                }
                return 0;
            }
        });
        table.getConfig().setShowTableTitle(false);
        //设置表格主题字体样式
        table.getConfig().setContentStyle(new FontStyle().setTextSize(40));
        //设置表格标题字体样式
        table.getConfig().setColumnTitleStyle(new FontStyle().setTextSize(50));
        table.getConfig().setColumnTitleBackground(new BaseBackgroundFormat(getResources().getColor(R.color.tableBackgroundTitle)));
        table.getConfig().setMinTableWidth(1080);

    }
    private void getData() {
        try {

            JSONObject jsonobject = JSONObject.parseObject(jsonstr);
            Intent intent = getIntent();
            start_date = intent.getStringExtra("start_date");
            Log.e("start_date",""+start_date);
            end_date = intent.getStringExtra("end_date");
            Log.e("end_date",""+end_date);
            ly_user_id = intent.getStringExtra("ly_user_id");
            Log.e("ly_user_id",""+ly_user_id);
            kq_state = intent.getStringExtra("kq_state");
            Log.e("ly_user_id",""+ly_user_id);
            String sql ="SELECT * FROM HR_KQJL               		  "+
                    "	 WHERE                           		  "+
                    "		 (                           		  "+
                    "			 WORKING_STATE = '"+kq_state+"'   "+
                    "			 OR WORK_STATE = '"+kq_state+"'   "+
                    "		 )                           		  "+
                    "	 AND KQ_DATE >= '"+start_date+"'          "+
                    "	 AND KQ_DATE <= '"+end_date+"'            "+
                    "	 AND LY_USER_ID = '"+ly_user_id+"'        ";
            GeekThreadManager.getInstance().execute(new GeekRunnable(ThreadPriority.NORMAL) {
                @Override
                public void run() {
                    String resultStr = WebServiceUtil.getInstance().queryBODatasBySQL(sql.toString());
                    com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(resultStr);
                    if ("SQL查询成功".equals(jsonObject.getString("msg"))){
                        if (!"0".equals(jsonObject.getString("count"))) {
                            StringBuffer sb = new StringBuffer();
                            JSONArray jsonarray = JSONArray.parseArray(jsonObject.getString("data"));
                            sb.append("[");
                            for (int i = 0; i < jsonarray.size(); i++) {
                                String kqtj = jsonarray.getString(i).substring(1,jsonarray.getString(i).lastIndexOf("]"));
                                String[] kqtjlist = kqtj.split(",");
                                sb.append("{");
                                sb.append("\"oid\":"+kqtjlist[0]+",");
                                sb.append("\"ly_user_id\":"+kqtjlist[1]+",");
                                sb.append("\"username\":"+kqtjlist[2]+",");
                                sb.append("\"deptname\":"+kqtjlist[3]+",");
                                sb.append("\"orgname\":"+kqtjlist[4]+",");
                                sb.append("\"start_date\":"+kqtjlist[5]+",");
                                sb.append("\"end_date\":"+kqtjlist[6]+",");
                                sb.append("\"start_state\":"+kqtjlist[7]+",");
                                sb.append("\"end_state\":"+kqtjlist[8]+",");
                                sb.append("\"ua_user_id\":"+kqtjlist[9]+",");
                                sb.append("\"kqdate\":"+kqtjlist[10]+",");
                                sb.append("\"usertype\":"+kqtjlist[11]+",");
                                String qjsc = kqtjlist[12];
                                if (qjsc == null || "".equals(qjsc) || "null".equals(qjsc)) {
                                    qjsc = "0";
                                }
                                sb.append("\"qjsc\":"+qjsc);
                                if (i == (jsonarray.size()-1)) {
                                    sb.append("}");
                                }else{
                                    sb.append("},");
                                }
                            }
                            sb.append("]");
                            List<AbNormalAttendanceDetailsBean> resps = (List) JSONArray.parseArray(sb.toString(), AbNormalAttendanceDetailsBean.class);
                            Message message = new Message();
                            message.what = SHOW_RESPONSE_EMPLOYEE;
                            message.obj = resps;
                            handler.sendMessage(message);

                        }else{
                            Message message = new Message();
                            message.what = SHOW_RESPONSE_EMPTY_DATA;
                            message.obj = "0";
                            handler.sendMessage(message);
                        }
                    }else {
                        Message message = new Message();
                        message.what = SHOW_RESPONSE_ABNORMAL_DATA;
                        message.obj = "查询失败";
                        handler.sendMessage(message);
                    }

                }
            }, ThreadType.NORMAL_THREAD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
