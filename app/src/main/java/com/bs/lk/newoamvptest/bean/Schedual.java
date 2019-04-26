package com.bs.lk.newoamvptest.bean;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * Created by 殇冰无恨 on 2017/11/16.
 */

public class Schedual extends LitePalSupport implements Serializable {
    private String mContent;
    private String mStartTime;
    private String mEndTime;
    private Boolean isAllday;

    public Schedual() {
    }

    public Schedual(String mContent, String mStartTime, String mEndTime, Boolean isAllday) {
        this.mContent = mContent;
        this.mStartTime = mStartTime;
        this.mEndTime = mEndTime;
        this.isAllday = isAllday;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String mContent) {
        this.mContent = mContent;
    }

    public String getStartTime() {
        return mStartTime;
    }

    public void setStartTime(String mStartTime) {
        this.mStartTime = mStartTime;
    }

    public String getEndTime() {
        return mEndTime;
    }

    public void setEndTime(String mEndTime) {
        this.mEndTime = mEndTime;
    }

    public Boolean getIsAllday() {
        return isAllday;
    }

    public void setIsAllday(Boolean allday) {
        isAllday = allday;
    }





}
