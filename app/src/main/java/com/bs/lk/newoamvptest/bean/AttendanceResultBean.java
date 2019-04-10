package com.bs.lk.newoamvptest.bean;

/**
 * 实体类：考勤反馈结果
 * @author lk
 */
public class AttendanceResultBean {
    private String msg;

    private boolean success;

    public void setMsg(String msg){
        this.msg = msg;
    }
    public String getMsg(){
        return this.msg;
    }
    public void setSuccess(boolean success){
        this.success = success;
    }
    public boolean getSuccess(){
        return this.success;
    }

}
