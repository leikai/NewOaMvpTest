package com.bs.lk.newoamvptest.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * version 3.0 获取session的实体类
 * @author lk
 */
public class SessionGsonFormatBean {

    /**
     * result : success
     * Access-Control-Allow-Headers : x-requested-with,content-type
     * Access-Control-Allow-Origin : *
     * sessionid : 886D5A3F3FAC64FFA88469B4D9B11CBA
     * Access-Control-Allow-Methods : POST
     * Access-Control-Allow-Credentials : true
     */

    private String result;
    @JSONField(name = "Access-Control-Allow-Headers")
    private String AccessControlAllowHeaders;
    @JSONField(name = "Access-Control-Allow-Origin")
    private String AccessControlAllowOrigin;
    private String sessionid;
    @JSONField(name = "Access-Control-Allow-Methods")
    private String AccessControlAllowMethods;
    @JSONField(name = "Access-Control-Allow-Credentials")
    private String AccessControlAllowCredentials;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getAccessControlAllowHeaders() {
        return AccessControlAllowHeaders;
    }

    public void setAccessControlAllowHeaders(String AccessControlAllowHeaders) {
        this.AccessControlAllowHeaders = AccessControlAllowHeaders;
    }

    public String getAccessControlAllowOrigin() {
        return AccessControlAllowOrigin;
    }

    public void setAccessControlAllowOrigin(String AccessControlAllowOrigin) {
        this.AccessControlAllowOrigin = AccessControlAllowOrigin;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public String getAccessControlAllowMethods() {
        return AccessControlAllowMethods;
    }

    public void setAccessControlAllowMethods(String AccessControlAllowMethods) {
        this.AccessControlAllowMethods = AccessControlAllowMethods;
    }

    public String getAccessControlAllowCredentials() {
        return AccessControlAllowCredentials;
    }

    public void setAccessControlAllowCredentials(String AccessControlAllowCredentials) {
        this.AccessControlAllowCredentials = AccessControlAllowCredentials;
    }
}
