package com.bs.lk.newoamvptest.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import static com.bs.lk.newoamvptest.common.util.URLRoot.BASE_URL_H5_ROOT_CLIENT_V3;
import static com.bs.lk.newoamvptest.common.util.URLRoot.BASE_URL_ROOT_SERVICE;

/**
 * Email：312607360@qq.com
 *
 * @author 温瑞壮
 * @date 2017/9/14
 * Description：
 */
public class NetUtils {

    public static final int HTTP_200 = 200;
    public static final String TAG_POST = "Post方式";
    public static final String  UPDATE_PATH  =  BASE_URL_ROOT_SERVICE + "/gz/document";

    public static final String  LOGIN_PATH   =  BASE_URL_H5_ROOT_CLIENT_V3 + "/bo_dev/login_login";

    /**EE
     * 描述：判断网络是否有效.
     *
     * @param context
     *            上下文
     * @return true, if is network available
     */
    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * 判断网络是否是wifi
     * @param context
     */
    public static boolean isWifiAvailable(Context context) {
        boolean isWifi = true;
        //判断有没有活动网络
        ConnectivityManager manager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo=manager.getActiveNetworkInfo();
        if (activeNetworkInfo==null )
        {//无网络
            isWifi = false;
        }else
        {
            //判断打开的是什么网络
            NetworkInfo wifiNetworkInfo=manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (wifiNetworkInfo!=null && wifiNetworkInfo.isConnected())
            {
                isWifi = true;
            }else{
                isWifi = false;
            }
        }
        return isWifi;
    }
    /**
     * 通过网络接口取
     * @return
     */
    private static String getNewMac() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!"wlan0".equalsIgnoreCase(nif.getName())) {
                    continue;
                }

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return null;
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }





    /**
     * Post方式请求
     */
    public static String requestByPost(String strJson,String path) throws Throwable   {
        // 请求的参数转换为byte数组  
        String  params = strJson;
        byte[]  postData =   params.getBytes();

        // 新建一个URL对象  

        URL url =   new   URL(path);

        // 打开一个HttpURLConnection连接  

        HttpURLConnection urlConn   = (HttpURLConnection) url.openConnection();
        urlConn.setRequestProperty("Access-Control-Allow-Origin", "*");
        urlConn.setRequestProperty("Access-Control-Allow-Methods", "POST");
        urlConn.setRequestProperty("Access-Control-Allow-Headers", "x-requested-with,content-type");
        urlConn.setRequestProperty("Access-Control-Allow-Credentials","true");

        // 取得sessionid.
//        String cookieval = urlConn.getHeaderField("set-cookie");
//        String sessionid = new String();
//        if(cookieval != null) {
//            sessionid = cookieval.substring(0, cookieval.indexOf(";"));
//        }
//        if(sessionid != null) {
//            urlConn.setRequestProperty("cookie", sessionid);
//        }



        //sessionid值格式：JSESSIONID=AD5F5C9EEB16C71EC3725DBF209F6178，是键值对，不是单指值

        // 设置连接超时时间  

        urlConn.setConnectTimeout(5 *   1000);

        // Post请求必须设置允许输出  

        urlConn.setDoOutput(true);

        // Post请求不能使用缓存  

        urlConn.setUseCaches(false);

        // 设置为Post请求  

        urlConn.setRequestMethod("POST");

        urlConn.setInstanceFollowRedirects(true);

        // 配置请求Content-Type  

        urlConn.setRequestProperty("Content-Type",    "application/x-www-form-urlencode");

        // 开始连接  

        urlConn.connect();

        // 发送请求参数  

        DataOutputStream dos =   new DataOutputStream(urlConn.getOutputStream());

        dos.write(postData);

        dos.flush();

        dos.close();

        // 判断请求是否成功  

        if  (urlConn.getResponseCode()  ==  HTTP_200)   {
            // 获取返回的数据  

            byte[]  data    =   readStream(urlConn.getInputStream());

            Log.i(TAG_POST, "Post请求方式成功，返回数据如下：");

            Log.i(TAG_POST, new String(data,"UTF-8"));
            return new String(data,"UTF-8");

        }   else  {

            Log.i(TAG_POST,"Post方式请求失败");

        }
        return null;
    }


    // 获取连接返回的数据
    private static byte[] readStream(InputStream inputStream) throws Exception {
        byte[] buffer = new byte[1024];
        int len = -1;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            baos.write(buffer, 0, len);
        }
        byte[] data = baos.toByteArray();
        inputStream.close();
        baos.close();
        return data;
    }





}
