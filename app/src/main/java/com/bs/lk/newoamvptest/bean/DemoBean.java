package com.bs.lk.newoamvptest.bean;

/**
 *   name; 姓名
 *   deptname; 部门
 *   daysTime; 考勤天数
 *   missTime; 缺卡次数
 *   leaveTime;请假次数
 *   lateTime; 迟到次数
 *   leaveEarlyTime; 早退次数
 */
public class DemoBean {
    private String name;
    private String deptname;
    private String daysTime;
    private String missTime;
    private String leaveTime;
    private String lateTime;
    private String leaveEarlyTime;

    public DemoBean() {
    }

    public DemoBean(String name, String deptname, String daysTime, String missTime, String leaveTime, String lateTime, String leaveEarlyTime) {
        this.name = name;
        this.deptname = deptname;
        this.daysTime = daysTime;
        this.missTime = missTime;
        this.leaveTime = leaveTime;
        this.lateTime = lateTime;
        this.leaveEarlyTime = leaveEarlyTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeptname() {
        return deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }

    public String getDaysTime() {
        return daysTime;
    }

    public void setDaysTime(String daysTime) {
        this.daysTime = daysTime;
    }

    public String getMissTime() {
        return missTime;
    }

    public void setMissTime(String missTime) {
        this.missTime = missTime;
    }

    public String getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(String leaveTime) {
        this.leaveTime = leaveTime;
    }

    public String getLateTime() {
        return lateTime;
    }

    public void setLateTime(String lateTime) {
        this.lateTime = lateTime;
    }

    public String getLeaveEarlyTime() {
        return leaveEarlyTime;
    }

    public void setLeaveEarlyTime(String leaveEarlyTime) {
        this.leaveEarlyTime = leaveEarlyTime;
    }
}
