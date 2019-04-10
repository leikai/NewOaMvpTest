package com.bs.lk.newoamvptest.bean;

import org.litepal.crud.LitePalSupport;

/**
 * id
 * date 打卡日期
 * morningworkClockTime 上午上班打卡时间
 * morningworkClockAddress 上午上班打卡地点
 * morningworkClocState 上午上班打卡状态
 * morningworkfinishClockTime  上午下班打卡时间
 * morningworkfinishClockAddress  上午下班打卡地点
 * morningworkfinishClocState  上午下班打卡状态
 * afternoonworkClockTime 下午上班打卡时间
 * afternoonworkClockAddress  下午上班打卡地点
 * afternoonworkClocState  下午上班打卡状态
 * afternoonworkfinishClockTime  下午下班打卡时间
 * afternoonworkfinishClockAddress  下午下班打卡地点
 * afternoonworkfinishClocState  下午下班打卡状态
 *
 * 考勤数据表
 * @author lk
 */
public class AttendanceMsgBean extends LitePalSupport {
    private int id;
    private String date;
    private String morningworkClockTime;
    private String morningworkClockAddress;
    private String morningworkClocState;
    private String morningworkfinishClockTime;
    private String morningworkfinishClockAddress;
    private String morningworkfinishClocState;
    private String afternoonworkClockTime;
    private String afternoonworkClockAddress;
    private String afternoonworkClocState;
    private String afternoonworkfinishClockTime;
    private String afternoonworkfinishClockAddress;
    private String afternoonworkfinishClocState;

    public AttendanceMsgBean() {
    }

    public AttendanceMsgBean(int id, String date, String morningworkClockTime, String morningworkClockAddress, String morningworkClocState, String morningworkfinishClockTime, String morningworkfinishClockAddress, String morningworkfinishClocState, String afternoonworkClockTime, String afternoonworkClockAddress, String afternoonworkClocState, String afternoonworkfinishClockTime, String afternoonworkfinishClockAddress, String afternoonworkfinishClocState) {
        this.id = id;
        this.date = date;
        this.morningworkClockTime = morningworkClockTime;
        this.morningworkClockAddress = morningworkClockAddress;
        this.morningworkClocState = morningworkClocState;
        this.morningworkfinishClockTime = morningworkfinishClockTime;
        this.morningworkfinishClockAddress = morningworkfinishClockAddress;
        this.morningworkfinishClocState = morningworkfinishClocState;
        this.afternoonworkClockTime = afternoonworkClockTime;
        this.afternoonworkClockAddress = afternoonworkClockAddress;
        this.afternoonworkClocState = afternoonworkClocState;
        this.afternoonworkfinishClockTime = afternoonworkfinishClockTime;
        this.afternoonworkfinishClockAddress = afternoonworkfinishClockAddress;
        this.afternoonworkfinishClocState = afternoonworkfinishClocState;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMorningworkClockTime() {
        return morningworkClockTime;
    }

    public void setMorningworkClockTime(String morningworkClockTime) {
        this.morningworkClockTime = morningworkClockTime;
    }

    public String getMorningworkClockAddress() {
        return morningworkClockAddress;
    }

    public void setMorningworkClockAddress(String morningworkClockAddress) {
        this.morningworkClockAddress = morningworkClockAddress;
    }

    public String getMorningworkClocState() {
        return morningworkClocState;
    }

    public void setMorningworkClocState(String morningworkClocState) {
        this.morningworkClocState = morningworkClocState;
    }

    public String getMorningworkfinishClockTime() {
        return morningworkfinishClockTime;
    }

    public void setMorningworkfinishClockTime(String morningworkfinishClockTime) {
        this.morningworkfinishClockTime = morningworkfinishClockTime;
    }

    public String getMorningworkfinishClockAddress() {
        return morningworkfinishClockAddress;
    }

    public void setMorningworkfinishClockAddress(String morningworkfinishClockAddress) {
        this.morningworkfinishClockAddress = morningworkfinishClockAddress;
    }

    public String getMorningworkfinishClocState() {
        return morningworkfinishClocState;
    }

    public void setMorningworkfinishClocState(String morningworkfinishClocState) {
        this.morningworkfinishClocState = morningworkfinishClocState;
    }

    public String getAfternoonworkClockTime() {
        return afternoonworkClockTime;
    }

    public void setAfternoonworkClockTime(String afternoonworkClockTime) {
        this.afternoonworkClockTime = afternoonworkClockTime;
    }

    public String getAfternoonworkClockAddress() {
        return afternoonworkClockAddress;
    }

    public void setAfternoonworkClockAddress(String afternoonworkClockAddress) {
        this.afternoonworkClockAddress = afternoonworkClockAddress;
    }

    public String getAfternoonworkClocState() {
        return afternoonworkClocState;
    }

    public void setAfternoonworkClocState(String afternoonworkClocState) {
        this.afternoonworkClocState = afternoonworkClocState;
    }

    public String getAfternoonworkfinishClockTime() {
        return afternoonworkfinishClockTime;
    }

    public void setAfternoonworkfinishClockTime(String afternoonworkfinishClockTime) {
        this.afternoonworkfinishClockTime = afternoonworkfinishClockTime;
    }

    public String getAfternoonworkfinishClockAddress() {
        return afternoonworkfinishClockAddress;
    }

    public void setAfternoonworkfinishClockAddress(String afternoonworkfinishClockAddress) {
        this.afternoonworkfinishClockAddress = afternoonworkfinishClockAddress;
    }

    public String getAfternoonworkfinishClocState() {
        return afternoonworkfinishClocState;
    }

    public void setAfternoonworkfinishClocState(String afternoonworkfinishClocState) {
        this.afternoonworkfinishClocState = afternoonworkfinishClocState;
    }

    @Override
    public String toString() {
        return "AttendanceMsgBean{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", morningworkClockTime='" + morningworkClockTime + '\'' +
                ", morningworkClockAddress='" + morningworkClockAddress + '\'' +
                ", morningworkClocState='" + morningworkClocState + '\'' +
                ", morningworkfinishClockTime='" + morningworkfinishClockTime + '\'' +
                ", morningworkfinishClockAddress='" + morningworkfinishClockAddress + '\'' +
                ", morningworkfinishClocState='" + morningworkfinishClocState + '\'' +
                ", afternoonworkClockTime='" + afternoonworkClockTime + '\'' +
                ", afternoonworkClockAddress='" + afternoonworkClockAddress + '\'' +
                ", afternoonworkClocState='" + afternoonworkClocState + '\'' +
                ", afternoonworkfinishClockTime='" + afternoonworkfinishClockTime + '\'' +
                ", afternoonworkfinishClockAddress='" + afternoonworkfinishClockAddress + '\'' +
                ", afternoonworkfinishClocState='" + afternoonworkfinishClocState + '\'' +
                '}';
    }
}
