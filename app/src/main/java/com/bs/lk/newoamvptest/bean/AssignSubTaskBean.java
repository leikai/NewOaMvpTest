package com.bs.lk.newoamvptest.bean;

/**
 * Created by baggio on 2017/2/24.
 */

public class AssignSubTaskBean {
    private String mETime;//结束日期
    private String mSTime;//开始日期
    private String mContent;//任务内容
    private String mWsMan;//执行人姓名
    private String mWsManId;//执行人courtoaid的值
    private int mWsManOAId;//执行人id的值

    public String getETime() {
        return mETime;
    }

    public void setETime(String mETime) {
        this.mETime = mETime;
    }

    public String getSTime() {
        return mSTime;
    }

    public void setSTime(String mSTime) {
        this.mSTime = mSTime;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String mContent) {
        this.mContent = mContent;
    }

    public String getWsMan() {
        return mWsMan;
    }

    public void setWsMan(String mWsMan) {
        this.mWsMan = mWsMan;
    }

    public String getWsManId() {
        return mWsManId;
    }

    public void setWsManId(String mWsManId) {
        this.mWsManId = mWsManId;
    }

    public int getWsManOAId() {
        return mWsManOAId;
    }

    public void setWsManOAId(int mWsManOAId) {
        this.mWsManOAId = mWsManOAId;
    }
}
