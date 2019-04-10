package com.bs.lk.newoamvptest.util;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bs.lk.newoamvptest.bean.DepartmentBean;
import com.bs.lk.newoamvptest.bean.DepartmentNewBean;
import com.bs.lk.newoamvptest.bean.UserNewBean;
import com.bs.lk.newoamvptest.cache.CSharedPreferences;
import com.bs.lk.newoamvptest.dataparser.DataParser;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.HttpsTransportSE;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.bs.lk.newoamvptest.common.util.URLRoot.BASE_URL_ROOT_SERVICE;

public class WebServiceUtil {

    private static final boolean IS_HTTP = true;


    public static String BASE_SERVER_URL_TOKEN = BASE_URL_ROOT_SERVICE + "/mserver/services/bwsSvc?wsdl";
    /**
     * 新OA整体命名空间
     */
    private static final String NAMESPACE_TOKEN = "http://bws.courtoa.zt.com";



    private String mHost = "111.53.181.200";
    private int mPort = 7443;
    private final String WS_OPS = "/mserver/services/awsSvc?wsdl";
    private final int TIMEOUT = 20000;

    private static WebServiceUtil mInstance;


    private WebServiceUtil() {

    }

    public synchronized static WebServiceUtil getInstance() {
        if (mInstance == null) {
            mInstance = new WebServiceUtil();
            mInstance.initServerInfo();
        }
        return mInstance;
    }

    //网络请求的地址
    public void initServerInfo() {
        try {
            JSONObject json = CSharedPreferences.getInstance().getServerInfo();
            if (json != null) {
                if (json.has("port")) {
                    mPort = json.getInt("port");
                }
                if (json.has("host")) {
                    mHost = json.getString("host");
                    if (IS_HTTP) {
                        BASE_SERVER_URL_TOKEN = "http://" + mHost + ":"+mPort+"/mserver/services/awsSvc?wsdl";
                    } else {
                    }
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 登录获取
     *
     * @param user
     * @return
     */
    public  String createSession(UserNewBean user) {
        String methodName = "createSession";
        SoapObject request = new SoapObject(NAMESPACE_TOKEN, methodName);
        JSONObject json = new JSONObject();
        try {
            json.put("userPwd", user.getUserPassword());
            json.put("userLoginName", user.getUserName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL_TOKEN);
        } else {
//            ht = new HttpsTransportSE(mHost, mPort,
//                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE_TOKEN + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;

                String resultStr = result.getPropertyAsString("return");
                if (resultIsNotEmpty(resultStr)) {
                    return resultStr;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * 登录获取
     *
     * @param
     * @return
     */
    public  String createSession(String userLoginName, String userHand) {
        String methodName = "createSession";
        SoapObject request = new SoapObject(NAMESPACE_TOKEN, methodName);
        JSONObject json = new JSONObject();
        try {
            json.put("userLoginName", userLoginName);
            json.put("userHand", userHand);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL_TOKEN);
        } else {
//            ht = new HttpsTransportSE(mHost, mPort,
//                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE_TOKEN + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;

                String resultStr = result.getPropertyAsString("return");
                if (resultIsNotEmpty(resultStr)) {
                    return resultStr;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取个人详情页
     *  @param username
     * @param userOid
     * @return
     */
    public UserNewBean getUserInfo(String username, String userOid) {
        UserNewBean user = null;
        String methodName = "getUserInfo";
        SoapObject request = new SoapObject(NAMESPACE_TOKEN, methodName);
        JSONObject json = new JSONObject();
        try {
            if (username != null){
                json.put("userName", username);
            }
            if (userOid != null){
                json.put("userOid", userOid);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL_TOKEN);
        } else {
            ht = new HttpsTransportSE(mHost, mPort,
                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE_TOKEN + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;
                String resultStr = result.getPropertyAsString("return");
                Log.e("resultStr",""+resultStr);
                user = JSON.parseObject(resultStr,UserNewBean.class);
                user.copyUserInfo(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * 获取待办列表
     *
     * @param token
     * @return
     */
    public String getTaskList(String token) {
//        String serviceUrl = BASE_SERVER_URL + AWSSVC;
        String methodName = "getTaskList";
        SoapObject request = new SoapObject(NAMESPACE_TOKEN, methodName);
        JSONObject json = new JSONObject();
        try {
            json.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL_TOKEN);
        } else {
            ht = new HttpsTransportSE(mHost, mPort,
                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE_TOKEN + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;

                String resultStr = result.getPropertyAsString("return");
                if (resultIsNotEmpty(resultStr)) {
                    return resultStr;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return null;
    }


    /**
     * 修改后的通讯录
     * @param token
     * @param qorgid
     * @return
     */
    public List<DepartmentNewBean> getDepartmentList(String token, String qorgid) {
        List<DepartmentNewBean> departments = null;
        String methodName = "getDepartmentList";
        SoapObject request = new SoapObject(NAMESPACE_TOKEN, methodName);
        JSONObject json = new JSONObject();
        try {
            if (!TextUtils.isEmpty(qorgid)) {
                json.put("token", token);
                json.put("qorgid", qorgid);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL_TOKEN);
        } else {
            ht = new HttpsTransportSE(mHost, mPort,
                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE_TOKEN + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;
                String resultStr = result.getPropertyAsString("return");
                if (resultIsNotEmpty(resultStr)) {


                    List<DepartmentNewBean> resps=new ArrayList<DepartmentNewBean>(com.alibaba.fastjson.JSONArray.parseArray(resultStr,DepartmentNewBean.class));
                    Log.e("部门数据",""+resps.get(0));

                    return resps;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return departments;
    }



    public boolean getUserDepartmentInfo(UserNewBean user) {
        String methodName = "getUserDepartmentInfo";
        SoapObject request = new SoapObject(NAMESPACE_TOKEN, methodName);
        JSONObject json = new JSONObject();
        try {
            json.put("userId", user.getOid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL_TOKEN);
        } else {
            ht = new HttpsTransportSE(mHost, mPort,
                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE_TOKEN + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;
                String resultStr = result.getPropertyAsString("return");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * 新OA通讯录人员信息
     * @param token
     * @param qusername
     * @return
     */
    public List<UserNewBean> getNewUserList(String token, String qusername,String qdeptid) {
        List<UserNewBean> users = null;
        Hashtable<String, Object> hash = new Hashtable<>();
        String methodName = "getUserList";
        SoapObject request = new SoapObject(NAMESPACE_TOKEN, methodName);
        JSONObject json = new JSONObject();
        try {
            if (!TextUtils.isEmpty(token)) {
                json.put("token", token);
            }
            if (!TextUtils.isEmpty(qusername)) {
                json.put("qusername", qusername);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL_TOKEN,3600*1000);
        } else {
            ht = new HttpsTransportSE(mHost, mPort,
                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE_TOKEN + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;
                String resultStr = result.getPropertyAsString("return");
                if (resultIsNotEmpty(resultStr)) {

                    List<UserNewBean> resps=new ArrayList<UserNewBean>(com.alibaba.fastjson.JSONArray.parseArray(resultStr,UserNewBean.class));
                    Log.e("人员数据",""+resps.get(0));

                    return resps;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 新OA通讯录人员信息
     * @param token
     * @param qdeptid
     * @return
     */
    public List<UserNewBean> getNewUserList(String token,  String qorgid,   String qdeptid, String qusername) {
        List<UserNewBean> users = null;
        Hashtable<String, Object> hash = new Hashtable<>();
        String methodName = "getUserList";
        SoapObject request = new SoapObject(NAMESPACE_TOKEN, methodName);
        JSONObject json = new JSONObject();
        try {
            if (!TextUtils.isEmpty(token)) {
                json.put("token", token);
            }
            if (!TextUtils.isEmpty(qorgid)) {
                json.put("qorgid", qorgid);
            }
            if (!TextUtils.isEmpty(qdeptid)) {
                json.put("qdeptid", qdeptid);
            }
            if (!TextUtils.isEmpty(qusername)) {
                json.put("qusername", qusername);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL_TOKEN,3600*1000);
        } else {
            ht = new HttpsTransportSE(mHost, mPort,
                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE_TOKEN + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;
                String resultStr = result.getPropertyAsString("return");
                if (resultIsNotEmpty(resultStr)) {

                    List<UserNewBean> resps=new ArrayList<UserNewBean>(com.alibaba.fastjson.JSONArray.parseArray(resultStr,UserNewBean.class));
                    Log.e("人员数据",""+resps.get(0));

                    return resps;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 考勤统计
     *
     * @param sql
     * @return
     */
    public  String queryBODatasBySQL(String sql) {
        String methodName = "queryBODatasBySQL";
        SoapObject request = new SoapObject(NAMESPACE_TOKEN, methodName);
//        JSONObject json = new JSONObject();
//        try {
//            json.put("userPwd", user.getUserPassword());
//            json.put("userLoginName", user.getUserName());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        request.addProperty("jsonstr", sql.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL_TOKEN);
        } else {
//            ht = new HttpsTransportSE(mHost, mPort,
//                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE_TOKEN + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;
                String resultStr = result.getPropertyAsString("return");
                if (resultIsNotEmpty(resultStr)) {
                    return resultStr;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * 帐号切换
     *
     * @param
     * @return
     */
    public  String getUsersByRelation(String relation) {
        String methodName = "getUsersByRelation";
        SoapObject request = new SoapObject(NAMESPACE_TOKEN, methodName);
        JSONObject json = new JSONObject();
        try {
            json.put("relation", relation);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL_TOKEN);
        } else {
//            ht = new HttpsTransportSE(mHost, mPort,
//                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE_TOKEN + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;

                String resultStr = result.getPropertyAsString("return");
                if (resultIsNotEmpty(resultStr)) {
                    return resultStr;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 修改手势密码
     *
     * @param
     * @return
     */
    public  String changeUserPWD(String userLoginName, String userHand) {
        String methodName = "changeUserPWD";
        SoapObject request = new SoapObject(NAMESPACE_TOKEN, methodName);
        JSONObject json = new JSONObject();
        try {
            json.put("userLoginName", userLoginName);
            json.put("userHand", userHand);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL_TOKEN);
        } else {
//            ht = new HttpsTransportSE(mHost, mPort,
//                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE_TOKEN + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;

                String resultStr = result.getPropertyAsString("return");
                if (resultIsNotEmpty(resultStr)) {
                    return resultStr;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 考勤打卡
     *
     * @param
     * @return
     */
    public  String updateSign(String ssignintimte,String ambdkwz,String ambstate,
                              String ssignbacktime, String amedkwz,String amestate,
                              String xsignintimte,String pmbdkwz, String pmbstate,
                              String xsignbacktime,String pmedkwz,String pmestate,
                              String oid, String orgid) {
        String methodName = "updateSign";
        SoapObject request = new SoapObject(NAMESPACE_TOKEN, methodName);
        JSONObject json = new JSONObject();
        try {
            if (ssignintimte!=null|ambdkwz!=null|ambstate!=null){
                //上午上班打卡时间
                json.put("ssignintimte",ssignintimte);
                //上午上班打卡位置
                json.put("ambdkwz", ambdkwz);
                //上午上班打卡状态
                json.put("ambstate", ambstate);
                json.put("val", "1");

            }else if (ssignbacktime!=null|amedkwz!=null|amestate!=null){
                //上午下班打卡时间
                json.put("ssignbacktime", ssignbacktime);
                //上午下班打卡位置
                json.put("amedkwz", amedkwz);
                //上午下班打卡状态
                json.put("amestate", amestate);
                json.put("val", "2");

            }else if (xsignintimte!=null|pmbdkwz!=null|pmbstate!=null){
                //下午上班打卡时间
                json.put("xsignintimte", xsignintimte);
                //下午上班打卡位置
                json.put("pmbdkwz", pmbdkwz);
                //下午上班打卡状态
                json.put("pmbstate", pmbstate);
                json.put("val", "3");
            }else if (xsignbacktime!=null|pmedkwz!=null|pmestate!=null){
                //下午下班打卡时间
                json.put("xsignbacktime", xsignbacktime);
                //下午下班打卡位置
                json.put("pmedkwz", pmedkwz);
                //下午下班打卡状态
                json.put("pmestate", pmestate);
                json.put("val", "4");
            }
            //登录人的oid
            json.put("userid", oid);
            json.put("orgid", orgid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL_TOKEN);
        } else {
//            ht = new HttpsTransportSE(mHost, mPort,
//                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE_TOKEN + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;
                String resultStr = result.getPropertyAsString("return");
                if (resultIsNotEmpty(resultStr)) {
                    return resultStr;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 查询该单位打卡时间及班次
     *
     * @param
     * @return
     */
    public  String getTheShift(String orgid) {
        String methodName = "getTheShift";
        SoapObject request = new SoapObject(NAMESPACE_TOKEN, methodName);
        JSONObject json = new JSONObject();
        try {
            //登录人的oid
            json.put("orgid", orgid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL_TOKEN);
        } else {
//            ht = new HttpsTransportSE(mHost, mPort,
//                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE_TOKEN + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;
                String resultStr = result.getPropertyAsString("return");
                if (resultIsNotEmpty(resultStr)) {
                    return resultStr;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 查询考勤打卡记录
     *
     * @param
     * @return
     */
    public  String getOrgSign(String time,String userid) {
        String methodName = "getOrgSign";
        SoapObject request = new SoapObject(NAMESPACE_TOKEN, methodName);
        JSONObject json = new JSONObject();
        try {
            //登录人的oid
            json.put("time", time);
            json.put("userid", userid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL_TOKEN);
        } else {
//            ht = new HttpsTransportSE(mHost, mPort,
//                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE_TOKEN + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;
                String resultStr = result.getPropertyAsString("return");
                if (resultIsNotEmpty(resultStr)) {
                    return resultStr;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private  boolean resultIsNotEmpty(String resultStr) {
        return !TextUtils.isEmpty(resultStr) && !resultStr.equals("anyType{}");
    }
}
