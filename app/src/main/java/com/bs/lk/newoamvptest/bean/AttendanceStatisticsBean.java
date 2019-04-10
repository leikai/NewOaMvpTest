package com.bs.lk.newoamvptest.bean;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

@SmartTable(name = "考勤统计表")
public class AttendanceStatisticsBean {
    @SmartColumn(id = 1,name = "姓名")
    private String name;
    @SmartColumn(id = 2,name = "部门")
    private String department;
    @SmartColumn(id = 3,name = "分类")
    private String userType;
    @SmartColumn(id = 4,name = "迟到")
    private String late;
    @SmartColumn(id = 5,name = "早退")
    private String earlyRetreat;
    @SmartColumn(id = 6,name = "旷工")
    private String absenteeism;
    @SmartColumn(id = 7,name = "请假")
    private String leave;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getLate() {
        return late;
    }

    public void setLate(String late) {
        this.late = late;
    }

    public String getEarlyRetreat() {
        return earlyRetreat;
    }

    public void setEarlyRetreat(String earlyRetreat) {
        this.earlyRetreat = earlyRetreat;
    }

    public String getAbsenteeism() {
        return absenteeism;
    }

    public void setAbsenteeism(String absenteeism) {
        this.absenteeism = absenteeism;
    }

    public String getLeave() {
        return leave;
    }

    public void setLeave(String leave) {
        this.leave = leave;
    }
}
