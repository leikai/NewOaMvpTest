package com.bs.lk.newoamvptest.util;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
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

public class WebServiceUtil {

    public static String BASE_SERVER_URL = "http://111.53.181.200:6688/mserver/services/awsSvc?wsdl"; // http
    public static String BASE_CABINET_URL ="http://111.53.181.200:8087/Cloud/CloudAppPort?wsdl";//云柜


    public static String BASE_DOWNLOAD_URL = "http://111.53.181.200:6688/mserver";

    private static final boolean IS_HTTP = true;
    public static String BASE_SERVER_URL_TOKEN = "http://111.53.181.200:8087/mserver/services/bwsSvc?wsdl"; // http

    private static final String NAMESPACE_TOKEN = "http://bws.courtoa.zt.com";//新OA整体命名空间
    private final String NAMESPACECABINET = "http://action.cloud/";//云柜命名空间
    private final String NAMESPACECABINETORIGINAL = "http://tempuri.org/";
    private final String NAMESPACE = "http://maws.courtoa.zt.com";//新闻模块命名空间



    private String mHost = "111.53.181.200";//223.12.199.236
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
                        BASE_SERVER_URL = "http://" + mHost + ":"+mPort+"/mserver/services/awsSvc?wsdl";
                        BASE_DOWNLOAD_URL = "http://" + mHost + ":"+mPort+"/mserver";//6688
                    } else {
                        BASE_DOWNLOAD_URL = "https://" + mHost + ":"+mPort+"/mserver";//7445
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
            ht = new HttpTransportSE(BASE_SERVER_URL);
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
     * @param qdeptid
     * @return
     */
    public List<UserNewBean> getNewUserList(String token, String qdeptid) {
        List<UserNewBean> users = null;
        Hashtable<String, Object> hash = new Hashtable<>();
        String methodName = "getUserList";
        SoapObject request = new SoapObject(NAMESPACE_TOKEN, methodName);
        JSONObject json = new JSONObject();
        try {
            if (!TextUtils.isEmpty(token)) {
                json.put("token", token);
            }
            if (!TextUtils.isEmpty(qdeptid)) {
                json.put("qdeptid", qdeptid);
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



    public String GetQRCode(SoapParams params, SoapClient.ISoapUtilCallback iSoapUtilCallback) {
        try {
            String dataDynamicCode = null;
            String methodName = "GetQRCode";
            SoapObject request = new SoapObject(NAMESPACECABINET, methodName);
            // 传递参数
            LinkedHashMap<String, Object> paramsList = params.getParamsList();
            Iterator<Map.Entry<String, Object>> iter = paramsList.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Object> entry = iter.next();
                Log.e("wrz", "请求参数--->Key:" + entry.getKey() + ",Value:" + entry.getValue());
                request.addProperty(entry.getKey(), entry.getValue());
            }
            //生成调用WebService方法的SOAP请求信息，并制定SOAP的版本
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            //envelope.bodyOut = request;
            // 等价于envelope.bodyOut=rpc;
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransportSE = new HttpTransportSE(BASE_CABINET_URL, 8 * 1000);
//            //-------------------------------ceshi------------------------//
//            HttpTransportSE httpTransportSE = new HttpTransportSE("http://192.168.2.110:8080/Cloud/CloudAppPort?wsdl", 8 * 1000);
//            //--------------------------------------//
//            httpTransportSE.call(null, envelope);
            //-------------------------------ceshi------------------------//
            httpTransportSE.call(NAMESPACECABINET + methodName, envelope);
            //--------------------------------------//
            iSoapUtilCallback.onSuccess(envelope);
            Log.e("wrz", "返回结果--->" + envelope.getResponse().toString());

        } catch (Exception e) {
            iSoapUtilCallback.onFailure(e);
            Log.e("wrz error", params.getParamsList().get("action") + "报错--->" + e.toString());
        }
        return null;
    }

    public String GetEmployeeDeposit(SoapParams params, SoapClient.ISoapUtilCallback iSoapUtilCallback) {
        try {
            String dataDynamicCode = null;
            String methodName = "GetEmployeeDeposit";
            SoapObject request = new SoapObject(NAMESPACECABINET, methodName);
            // 传递参数
            LinkedHashMap<String, Object> paramsList = params.getParamsList();
            Iterator<Map.Entry<String, Object>> iter = paramsList.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Object> entry = iter.next();
                Log.e("wrz", "请求参数--->Key:" + entry.getKey() + ",Value:" + entry.getValue());
                request.addProperty(entry.getKey(), entry.getValue());
            }
            //生成调用WebService方法的SOAP请求信息，并制定SOAP的版本
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            //envelope.bodyOut = request;
            // 等价于envelope.bodyOut=rpc;
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransportSE = new HttpTransportSE(BASE_CABINET_URL, 8 * 1000);
            //            httpTransportSE.debug = true;
            //            ArrayList<HeaderProperty> headerPropertyArrayList = new ArrayList<HeaderProperty>();
            //            headerPropertyArrayList.add(new HeaderProperty("Connection", "close"));

            httpTransportSE.call(NAMESPACECABINETORIGINAL + methodName, envelope);


            //            httpTransportSE.call(null, envelope, headerPropertyArrayList);
            //            System.setProperty("http.keepAlive", "false");
            // SoapObject bodyIn = (SoapObject) envelope.bodyIn;
            // result = bodyIn.getProperty(0).toString();
            //            LogUtil.i("SoapClient", envelope.getResponse().toString());
            SoapObject bodyIn = (SoapObject) envelope.bodyIn;
            String result = bodyIn.getProperty(0).toString();
            Log.e("result",""+result);
            iSoapUtilCallback.onSuccess(envelope);
            Log.e("wrz", "返回结果--->" + envelope.getResponse().toString());



        } catch (Exception e) {
            iSoapUtilCallback.onFailure(e);
            Log.e("wrz error", params.getParamsList().get("action") + "报错--->" + e.toString());

        }
        return null;
    }

    public String GetEmployeePickup(SoapParams params, SoapClient.ISoapUtilCallback iSoapUtilCallback) {
        try {
            String dataDynamicCode = null;
            String methodName = "GetEmployeePickup";
            SoapObject request = new SoapObject(NAMESPACECABINET, methodName);
            // 传递参数
            LinkedHashMap<String, Object> paramsList = params.getParamsList();
            Iterator<Map.Entry<String, Object>> iter = paramsList.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Object> entry = iter.next();
                Log.e("wrz", "请求参数--->Key:" + entry.getKey() + ",Value:" + entry.getValue());
                request.addProperty(entry.getKey(), entry.getValue());
            }
            //生成调用WebService方法的SOAP请求信息，并制定SOAP的版本
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            //envelope.bodyOut = request;
            // 等价于envelope.bodyOut=rpc;
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransportSE = new HttpTransportSE(BASE_CABINET_URL, 8 * 1000);
            httpTransportSE.call(NAMESPACECABINET + methodName, envelope);
            iSoapUtilCallback.onSuccess(envelope);
            Log.e("wrz", "返回结果--->" + envelope.getResponse().toString());
        } catch (Exception e) {
            iSoapUtilCallback.onFailure(e);
            Log.e("wrz error", params.getParamsList().get("action") + "报错--->" + e.toString());

        }
        return null;
    }
    public String getWebInfos1(SoapParams params, SoapClient.ISoapUtilCallback iSoapUtilCallback) {
        try {
            String dataDynamicCode = null;
            String methodName = "getWebInfos1";
            SoapObject request = new SoapObject(NAMESPACE, methodName);
            // 传递参数
            LinkedHashMap<String, Object> paramsList = params.getParamsList();
            Iterator<Map.Entry<String, Object>> iter = paramsList.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Object> entry = iter.next();
                Log.e("wrz", "请求参数--->Key:" + entry.getKey() + ",Value:" + entry.getValue());
                request.addProperty(entry.getKey(), entry.getValue());
            }
            //生成调用WebService方法的SOAP请求信息，并制定SOAP的版本
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            //envelope.bodyOut = request;
            // 等价于envelope.bodyOut=rpc;
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
            HttpTransportSE httpTransportSE = new HttpTransportSE(BASE_SERVER_URL, 8 * 1000);
            //            httpTransportSE.debug = true;
            //            ArrayList<HeaderProperty> headerPropertyArrayList = new ArrayList<HeaderProperty>();
            //            headerPropertyArrayList.add(new HeaderProperty("Connection", "close"));

            httpTransportSE.call(NAMESPACE + methodName, envelope);


            //            httpTransportSE.call(null, envelope, headerPropertyArrayList);
            //            System.setProperty("http.keepAlive", "false");
            // SoapObject bodyIn = (SoapObject) envelope.bodyIn;
            // result = bodyIn.getProperty(0).toString();
            //            LogUtil.i("SoapClient", envelope.getResponse().toString());
            iSoapUtilCallback.onSuccess(envelope);
            Log.e("wrz", "返回结果--->" + envelope.getResponse().toString());



        } catch (Exception e) {
            iSoapUtilCallback.onFailure(e);
            Log.e("wrz error", params.getParamsList().get("action") + "报错--->" + e.toString());

        }
        return null;
    }

    //banner请求
    public String getWebImgs(String nums){
        String dataWebImgs = null;
        String methodName = "getWebImgs";
        SoapObject request = new SoapObject(NAMESPACE, methodName);
        request.addProperty("jsonstr", nums);//json.toString()
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP){
            ht = new HttpTransportSE(BASE_SERVER_URL);
            Log.e("我是ht1","hhhhhhhhhhhhhhhhhhhhhhhhhhhh");
        }else {
            ht = new HttpsTransportSE(mHost, mPort,
                    WS_OPS, TIMEOUT);
            Log.e("我是ht2","tttttttttttttttttttttttttttttttt");
        }try{
            String resultStr = null;
            ht.call(NAMESPACE + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;
                resultStr = result.getPropertyAsString("return");
            }
            if (resultIsNotEmpty(resultStr)) {
                dataWebImgs = resultStr;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return dataWebImgs;
    }

    public String getWebInfos(SoapParams params, SoapClient.ISoapUtilCallback iSoapUtilCallback) {
        try {
            String dataDynamicCode = null;
            String methodName = "getWebInfos";
            SoapObject request = new SoapObject(NAMESPACE, methodName);
            // 传递参数
            LinkedHashMap<String, Object> paramsList = params.getParamsList();
            Iterator<Map.Entry<String, Object>> iter = paramsList.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Object> entry = iter.next();
                Log.e("wrz", "请求参数--->Key:" + entry.getKey() + ",Value:" + entry.getValue());
                request.addProperty(entry.getKey(), entry.getValue());
            }
            //生成调用WebService方法的SOAP请求信息，并制定SOAP的版本
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            //envelope.bodyOut = request;
            // 等价于envelope.bodyOut=rpc;
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
            HttpTransportSE httpTransportSE = new HttpTransportSE(BASE_SERVER_URL, 8 * 1000);
            //            httpTransportSE.debug = true;
            //            ArrayList<HeaderProperty> headerPropertyArrayList = new ArrayList<HeaderProperty>();
            //            headerPropertyArrayList.add(new HeaderProperty("Connection", "close"));

            httpTransportSE.call(NAMESPACE + methodName, envelope);


            //            httpTransportSE.call(null, envelope, headerPropertyArrayList);
            //            System.setProperty("http.keepAlive", "false");
            // SoapObject bodyIn = (SoapObject) envelope.bodyIn;
            // result = bodyIn.getProperty(0).toString();
            //            LogUtil.i("SoapClient", envelope.getResponse().toString());
            iSoapUtilCallback.onSuccess(envelope);
            Log.e("wrz", "返回结果--->" + envelope.getResponse().toString());



        } catch (Exception e) {
            iSoapUtilCallback.onFailure(e);
            Log.e("wrz error", params.getParamsList().get("action") + "报错--->" + e.toString());

        }
        return null;
    }
    private  boolean resultIsNotEmpty(String resultStr) {
        return !TextUtils.isEmpty(resultStr) && !resultStr.equals("anyType{}");
    }
}
