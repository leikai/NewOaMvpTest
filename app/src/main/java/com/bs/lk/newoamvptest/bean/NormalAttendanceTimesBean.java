package com.bs.lk.newoamvptest.bean;

import java.io.Serializable;

/**
 * @author lk
 */
public class NormalAttendanceTimesBean implements Serializable{
    private String username;

    private String deptname;

    private String usertype;

    private String ua_user_id;

    private String ly_user_id;

    private String zccs;

    public void setUsername(String username){
        this.username = username;
    }
    public String getUsername(){
        return this.username;
    }
    public void setDeptname(String deptname){
        this.deptname = deptname;
    }
    public String getDeptname(){
        return this.deptname;
    }
    public void setUsertype(String usertype){
        this.usertype = usertype;
    }
    public String getUsertype(){
        return this.usertype;
    }
    public void setUa_user_id(String ua_user_id){
        this.ua_user_id = ua_user_id;
    }
    public String getUa_user_id(){
        return this.ua_user_id;
    }
    public void setLy_user_id(String ly_user_id){
        this.ly_user_id = ly_user_id;
    }
    public String getLy_user_id(){
        return this.ly_user_id;
    }
    public void setZccs(String zccs){
        this.zccs = zccs;
    }
    public String getZccs(){
        return this.zccs;
    }
}
