package com.bs.lk.newoamvptest.view.activity;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.column.ColumnInfo;
import com.bin.david.form.data.format.bg.BaseBackgroundFormat;
import com.bin.david.form.data.format.bg.BaseCellBackgroundFormat;
import com.bin.david.form.data.format.bg.IBackgroundFormat;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.PageTableData;
import com.bin.david.form.data.table.TableData;
import com.bin.david.form.listener.OnColumnClickListener;
import com.bin.david.form.listener.OnColumnItemClickListener;
import com.bs.lk.newoamvptest.R;
import com.bs.lk.newoamvptest.bean.AttendanceStatisticsBean;
import com.bs.lk.newoamvptest.bean.NormalAttendanceTimesBean;
import com.bs.lk.newoamvptest.util.WebServiceUtil;
import com.geek.thread.GeekThreadManager;
import com.geek.thread.ThreadPriority;
import com.geek.thread.ThreadType;
import com.geek.thread.task.GeekRunnable;

import org.ksoap2.serialization.SoapSerializationEnvelope;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SmartTableActivity extends AppCompatActivity {
    private com.bin.david.form.core.SmartTable<NormalAttendanceTimesBean> table;
    private List<AttendanceStatisticsBean> list = new ArrayList<>();
    private String jsonstr = "";
    private List<NormalAttendanceTimesBean> templist;
    private String start_date;
    private String end_date;
    private String deptname;
    private String usertype;
    private String username;
    //统计数据异常，显示为空
    private TextView tvAbnormalData;


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_RESPONSE_EMPLOYEE:
                    templist = (ArrayList)msg.obj;
                    Column<String> username = new Column<>("姓名", "username");
                    Column<String> deptname = new Column<>("部门", "deptname");
                    Column<String> usertype = new Column<>("分类", "usertype");
                    Column<String> zccs = new Column<>("正常次数", "zccs");

                    String deptName = templist.get(0).getDeptname();
                    PageTableData<NormalAttendanceTimesBean> pageTableData = new PageTableData<NormalAttendanceTimesBean>("正常考勤统计表",templist,
                            username,deptname,usertype,zccs
                    );
                    pageTableData.setPageSize(15);
                    table.setTableData(pageTableData);

                    zccs.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
                        @Override
                        public void onClick(Column<String> column, String value, String s, int position) {
                            Log.e("wqe","column"+column+"value"+value+"s"+s+"position"+position);

                            Intent intent = new Intent(SmartTableActivity.this,NormalAttendanceActivity.class);
                            intent.putExtra("start_date",start_date);
                            intent.putExtra("end_date",end_date);
                            intent.putExtra("ly_user_id",templist.get(position).getLy_user_id());
                            startActivity(intent);
                        }
                    });


                    break;
                case SHOW_RESPONSE_EMPTY_DATA:
                    table.setVisibility(View.GONE);
                    tvAbnormalData.setVisibility(View.VISIBLE);
                    break;
                case SHOW_RESPONSE_ABNORMAL_DATA:
                    table.setVisibility(View.GONE);
                    tvAbnormalData.setText("网络问题，数据异常请重试！");
                    tvAbnormalData.setVisibility(View.VISIBLE);
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
        setContentView(R.layout.activity_smart_table);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        table = findViewById(R.id.normal_table);
        tvAbnormalData = findViewById(R.id.tv_abnormal);
        getData();





        table.getConfig().setContentCellBackgroundFormat(new BaseCellBackgroundFormat<CellInfo>() {
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
                if (cellInfo.row%2 == 0){
                    return 0;
                }
                return ContextCompat.getColor(SmartTableActivity.this,R.color.tableBackground);
            }
        });
        table.getConfig().setShowTableTitle(false);
        //设置表格主题字体样式
        table.getConfig().setContentStyle(new FontStyle().setTextSize(40));
        //设置表格标题字体样式
        table.getConfig().setColumnTitleStyle(new FontStyle().setTextSize(50));
        table.getConfig().setColumnTitleBackground(new BaseBackgroundFormat(getResources().getColor(R.color.tableBackgroundTitle)));
//        table.getConfig().setColumnTitleBackground(new BaseBackgroundFormat(R.color.tableBackgroundTitle));
        table.getConfig().setMinTableWidth(1080);


    }

    private void getData() {
        try {
//            if (!TextUtils.isEmpty(jsonstr)) {
                StringBuffer sbsql = new StringBuffer();
                sbsql.append("SELECT USERNAME,DEPTNAME,USERTYPE,UA_USER_ID,LY_USER_ID,COUNT (1) zc FROM HR_KQJL WHERE  ((WORKING_STATE = '正常' AND WORK_STATE = '正常') OR (WORKING_STATE = '休息' AND WORK_STATE = '休息') OR (WORKING_STATE = '正常' AND WORK_STATE = '休息') OR (WORKING_STATE = '休息' AND WORK_STATE = '正常')) ");
                JSONObject jsonobject = JSONObject.parseObject(jsonstr);

                start_date = "2018-06-01";
                end_date = "2018-07-30";
                deptname = "";
                usertype = "";
                username = "";
                if (!TextUtils.isEmpty(start_date) && !TextUtils.isEmpty(end_date)) {
                    sbsql.append(" AND KQ_DATE >= '"+start_date+"' AND KQ_DATE <= '"+end_date+"' ");
                }
                if (!TextUtils.isEmpty(deptname)) {
                    sbsql.append(" AND DEPTNAME like '%"+deptname+"%' ");
                }
                if (!TextUtils.isEmpty(usertype)) {
                    sbsql.append(" AND USERTYPE like '%"+usertype+"%' ");
                }
                if (!TextUtils.isEmpty(username)) {
                    sbsql.append(" AND USERNAME like '%"+username+"%' ");
                }
                sbsql.append(" AND LY_USER_ID NOT IN ( SELECT LY_USER_ID FROM HR_KQJL WHERE (WORKING_STATE NOT IN ('正常', '休息') OR WORK_STATE NOT IN ('正常', '休息')) ");
                if (!TextUtils.isEmpty(start_date) && !TextUtils.isEmpty(end_date)) {
                    sbsql.append(" AND KQ_DATE >= '"+start_date+"' AND KQ_DATE <= '"+end_date+"' ");
                }
                if (!TextUtils.isEmpty(deptname)) {
                    sbsql.append(" AND DEPTNAME like '%"+deptname+"%' ");
                }
                if (!TextUtils.isEmpty(usertype)) {
                    sbsql.append(" AND USERTYPE like '%"+usertype+"%' ");
                }
                sbsql.append(" GROUP BY LY_USER_ID) GROUP BY LY_USER_ID,USERNAME,DEPTNAME,USERTYPE,UA_USER_ID,LY_USER_ID ");
                GeekThreadManager.getInstance().execute(new GeekRunnable(ThreadPriority.NORMAL) {
                    @Override
                    public void run() {
                        String resultStr = WebServiceUtil.getInstance().queryBODatasBySQL(sbsql.toString());
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
                                    sb.append("\"username\":"+kqtjlist[0]+",");
                                    sb.append("\"deptname\":"+kqtjlist[1]+",");
                                    sb.append("\"usertype\":"+kqtjlist[2]+",");
                                    sb.append("\"ua_user_id\":"+kqtjlist[3]+",");
                                    sb.append("\"ly_user_id\":"+kqtjlist[4]+",");
                                    sb.append("\"zccs\":"+kqtjlist[5]);
                                    if (i == (jsonarray.size()-1)) {
                                        sb.append("}");
                                    }else{
                                        sb.append("},");
                                    }
                                }
                                sb.append("]");
                                List<NormalAttendanceTimesBean> resps = (List) JSONArray.parseArray(sb.toString(), NormalAttendanceTimesBean.class);
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


        try {
            if (!TextUtils.isEmpty(jsonstr)) {
                StringBuffer sbsql = new StringBuffer();
                sbsql.append(" SELECT DISTINCT qj_count,kg_count,cd_count,zt_count,USERNAME,DEPTNAME,USERTYPE,UA_USER_ID,tj.LY_USER_ID,kqqjsc FROM (SELECT * FROM ( SELECT LY_USER_ID,ISNULL((SELECT COUNT (1) qj FROM HR_KQJL WHERE ( WORKING_STATE = '请假' OR WORK_STATE = '请假') AND LY_USER_ID = hk.LY_USER_ID ");
                JSONObject jsonobject = JSONObject.parseObject(jsonstr);
                String start_date = jsonobject.getString("start_date");
                String end_date = jsonobject.getString("end_date");
                String usertype = jsonobject.getString("usertype");
                String deptname = jsonobject.getString("deptname");
                String username = jsonobject.getString("username");
                if (!TextUtils.isEmpty(start_date) && !TextUtils.isEmpty(end_date)) {
                    sbsql.append(" AND KQ_DATE >= '"+start_date+"' AND KQ_DATE <= '"+end_date+"' ");
                }
                if (!TextUtils.isEmpty(deptname)) {
                    sbsql.append(" AND DEPTNAME like '%"+deptname+"%' ");
                }
                if (!TextUtils.isEmpty(usertype)) {
                    sbsql.append(" AND USERTYPE like '%"+usertype+"%' ");
                }
                if (!TextUtils.isEmpty(username)) {
                    sbsql.append(" AND USERNAME like '%"+username+"%' ");
                }
                sbsql.append(" GROUP BY LY_USER_ID),0) qj_count,ISNULL((SELECT COUNT (1) qj FROM HR_KQJL WHERE ( WORKING_STATE = '旷工' OR WORK_STATE = '旷工') AND LY_USER_ID = hk.LY_USER_ID ");
                if (!TextUtils.isEmpty(start_date) && !TextUtils.isEmpty(end_date)) {
                    sbsql.append(" AND KQ_DATE >= '"+start_date+"' AND KQ_DATE <= '"+end_date+"' ");
                }
                if (!TextUtils.isEmpty(deptname)) {
                    sbsql.append(" AND DEPTNAME like '%"+deptname+"%' ");
                }
                if (!TextUtils.isEmpty(usertype)) {
                    sbsql.append(" AND USERTYPE like '%"+usertype+"%' ");
                }
                if (!TextUtils.isEmpty(username)) {
                    sbsql.append(" AND USERNAME like '%"+username+"%' ");
                }
                sbsql.append(" GROUP BY LY_USER_ID ),0) kg_count,ISNULL((SELECT COUNT (1) qj FROM HR_KQJL WHERE (WORKING_STATE = '迟到' OR WORK_STATE = '迟到')AND LY_USER_ID = hk.LY_USER_ID ");
                if (!TextUtils.isEmpty(start_date) && !TextUtils.isEmpty(end_date)) {
                    sbsql.append(" AND KQ_DATE >= '"+start_date+"' AND KQ_DATE <= '"+end_date+"' ");
                }
                if (!TextUtils.isEmpty(deptname)) {
                    sbsql.append(" AND DEPTNAME like '%"+deptname+"%' ");
                }
                if (!TextUtils.isEmpty(usertype)) {
                    sbsql.append(" AND USERTYPE like '%"+usertype+"%' ");
                }
                if (!TextUtils.isEmpty(username)) {
                    sbsql.append(" AND USERNAME like '%"+username+"%' ");
                }
                sbsql.append(" GROUP BY LY_USER_ID),0) cd_count,ISNULL((SELECT COUNT (1) qj FROM HR_KQJL WHERE WORK_STATE = '早退' AND LY_USER_ID = hk.LY_USER_ID ");
                if (!TextUtils.isEmpty(start_date) && !TextUtils.isEmpty(end_date)) {
                    sbsql.append(" AND KQ_DATE >= '"+start_date+"' AND KQ_DATE <= '"+end_date+"' ");
                }
                if (!TextUtils.isEmpty(deptname)) {
                    sbsql.append(" AND DEPTNAME like '%"+deptname+"%' ");
                }
                if (!TextUtils.isEmpty(usertype)) {
                    sbsql.append(" AND USERTYPE like '%"+usertype+"%' ");
                }
                if (!TextUtils.isEmpty(username)) {
                    sbsql.append(" AND USERNAME like '%"+username+"%' ");
                }
                sbsql.append(" GROUP BY LY_USER_ID),0) zt_count,ISNULL((SELECT SUM (CAST(QJSC AS DECIMAL(18, 2))) qjsc FROM HR_KQJL WHERE (WORKING_STATE = '请假' OR WORK_STATE = '请假')AND LY_USER_ID = hk.LY_USER_ID ");
                if (!TextUtils.isEmpty(start_date) && !TextUtils.isEmpty(end_date)) {
                    sbsql.append(" AND KQ_DATE >= '"+start_date+"' AND KQ_DATE <= '"+end_date+"' ");
                }
                if (!TextUtils.isEmpty(deptname)) {
                    sbsql.append(" AND DEPTNAME like '%"+deptname+"%' ");
                }
                if (!TextUtils.isEmpty(usertype)) {
                    sbsql.append(" AND USERTYPE like '%"+usertype+"%' ");
                }
                if (!TextUtils.isEmpty(username)) {
                    sbsql.append(" AND USERNAME like '%"+username+"%' ");
                }
                sbsql.append(" GROUP BY LY_USER_ID),0) kqqjsc FROM HR_KQJL hk WHERE 1=1 ");
                if (!TextUtils.isEmpty(start_date) && !TextUtils.isEmpty(end_date)) {
                    sbsql.append(" AND KQ_DATE >= '"+start_date+"' AND KQ_DATE <= '"+end_date+"' ");
                }
                if (!TextUtils.isEmpty(deptname)) {
                    sbsql.append(" AND DEPTNAME like '%"+deptname+"%' ");
                }
                if (!TextUtils.isEmpty(usertype)) {
                    sbsql.append(" AND USERTYPE like '%"+usertype+"%' ");
                }
                if (!TextUtils.isEmpty(username)) {
                    sbsql.append(" AND USERNAME like '%"+username+"%' ");
                }
                sbsql.append(" GROUP BY LY_USER_ID) a WHERE a.qj_count != 0 OR a.kg_count != 0 OR a.cd_count != 0 OR a.zt_count != 0 ) tj LEFT JOIN HR_KQJL kq ON tj.LY_USER_ID = kq.LY_USER_ID ORDER BY tj.qj_count DESC,tj.kg_count DESC,tj.cd_count DESC,tj.zt_count DESC ");

                GeekThreadManager.getInstance().execute(new GeekRunnable(ThreadPriority.NORMAL) {
                    @Override
                    public void run() {
                        String resultStr = WebServiceUtil.getInstance().queryBODatasBySQL(sbsql.toString());
                        com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(resultStr);
                        if (!"0".equals(jsonobject.getString("count"))) {
                            StringBuffer sb = new StringBuffer();
                            JSONArray jsonarray = JSONArray.parseArray(jsonobject.getString("data"));
                            sb.append("[");
                            for (int i = 0; i < jsonarray.size(); i++) {
                                String kqtj = jsonarray.getString(i).substring(1,jsonarray.getString(i).lastIndexOf("]"));
                                String[] kqtjlist = kqtj.split(",");
                                sb.append("{");
                                sb.append("\"qjcount\":"+kqtjlist[0]+",");
                                sb.append("\"kgcount\":"+kqtjlist[1]+",");
                                sb.append("\"cdcount\":"+kqtjlist[2]+",");
                                sb.append("\"ztcount\":"+kqtjlist[3]+",");
                                sb.append("\"username\":"+kqtjlist[4]+",");
                                sb.append("\"deptname\":"+kqtjlist[5]+",");
                                sb.append("\"usertype\":"+kqtjlist[6]+",");
                                sb.append("\"ua_user_id\":"+kqtjlist[7]+",");
                                sb.append("\"ly_user_id\":"+kqtjlist[8]+",");
                                sb.append("\"qjsc\":"+kqtjlist[9]);
                                if (i == (jsonarray.size()-1)) {
                                    sb.append("}");
                                }else{
                                    sb.append("},");
                                }
                            }
                            sb.append("]");

                            List templist = (List) JSONArray.parseArray(sb.toString(), Object.class);

                        }else{

                        }
                    }
                },ThreadType.NORMAL_THREAD);



            }
        } catch (Exception e) {
            e.printStackTrace();
        }






    }

}
