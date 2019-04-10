package com.bs.lk.newoamvptest.bean;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * 查询远程数据库考勤数据
 * @author lk
 */
public class AttendanceDataBean extends LitePalSupport implements Serializable {
    private String oid;

    private String creattime;

    private String orgid;

    private String orgname;

    private String deptid;

    private String deptname;

    private String userid;

    private String username;

    private String userloginname;

    private String updateuserid;

    private String updateusername;

    private String updateuserloginname;

    private String updatetime;

    private String wfid;

    private String wfinsid;

    private String isend;

    private String xh;

    private String ssignintimte;

    private String ssignbacktime;

    private String date;

    private String state;

    private String xsignintimte;

    private String xsignbacktime;

    private String ambdkwz;

    private String bz;

    private String qk;

    private String zt;

    private String amedkwz;

    private String pmbdkwz;

    private String pmedkwz;

    private String ambstate;

    private String amestate;

    private String pmbstate;

    private String pmestate;

    public void setOid(String oid){
        this.oid = oid;
    }
    public String getOid(){
        return this.oid;
    }
    public void setCreattime(String creattime){
        this.creattime = creattime;
    }
    public String getCreattime(){
        return this.creattime;
    }
    public void setOrgid(String orgid){
        this.orgid = orgid;
    }
    public String getOrgid(){
        return this.orgid;
    }
    public void setOrgname(String orgname){
        this.orgname = orgname;
    }
    public String getOrgname(){
        return this.orgname;
    }
    public void setDeptid(String deptid){
        this.deptid = deptid;
    }
    public String getDeptid(){
        return this.deptid;
    }
    public void setDeptname(String deptname){
        this.deptname = deptname;
    }
    public String getDeptname(){
        return this.deptname;
    }
    public void setUserid(String userid){
        this.userid = userid;
    }
    public String getUserid(){
        return this.userid;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public String getUsername(){
        return this.username;
    }
    public void setUserloginname(String userloginname){
        this.userloginname = userloginname;
    }
    public String getUserloginname(){
        return this.userloginname;
    }
    public void setUpdateuserid(String updateuserid){
        this.updateuserid = updateuserid;
    }
    public String getUpdateuserid(){
        return this.updateuserid;
    }
    public void setUpdateusername(String updateusername){
        this.updateusername = updateusername;
    }
    public String getUpdateusername(){
        return this.updateusername;
    }
    public void setUpdateuserloginname(String updateuserloginname){
        this.updateuserloginname = updateuserloginname;
    }
    public String getUpdateuserloginname(){
        return this.updateuserloginname;
    }
    public void setUpdatetime(String updatetime){
        this.updatetime = updatetime;
    }
    public String getUpdatetime(){
        return this.updatetime;
    }
    public void setWfid(String wfid){
        this.wfid = wfid;
    }
    public String getWfid(){
        return this.wfid;
    }
    public void setWfinsid(String wfinsid){
        this.wfinsid = wfinsid;
    }
    public String getWfinsid(){
        return this.wfinsid;
    }
    public void setIsend(String isend){
        this.isend = isend;
    }
    public String getIsend(){
        return this.isend;
    }
    public void setXh(String xh){
        this.xh = xh;
    }
    public String getXh(){
        return this.xh;
    }
    public void setSsignintimte(String ssignintimte){
        this.ssignintimte = ssignintimte;
    }
    public String getSsignintimte(){
        return this.ssignintimte;
    }
    public void setSsignbacktime(String ssignbacktime){
        this.ssignbacktime = ssignbacktime;
    }
    public String getSsignbacktime(){
        return this.ssignbacktime;
    }
    public void setDate(String date){
        this.date = date;
    }
    public String getDate(){
        return this.date;
    }
    public void setState(String state){
        this.state = state;
    }
    public String getState(){
        return this.state;
    }
    public void setXsignintimte(String xsignintimte){
        this.xsignintimte = xsignintimte;
    }
    public String getXsignintimte(){
        return this.xsignintimte;
    }
    public void setXsignbacktime(String xsignbacktime){
        this.xsignbacktime = xsignbacktime;
    }
    public String getXsignbacktime(){
        return this.xsignbacktime;
    }
    public void setAmbdkwz(String ambdkwz){
        this.ambdkwz = ambdkwz;
    }
    public String getAmbdkwz(){
        return this.ambdkwz;
    }
    public void setBz(String bz){
        this.bz = bz;
    }
    public String getBz(){
        return this.bz;
    }
    public void setQk(String qk){
        this.qk = qk;
    }
    public String getQk(){
        return this.qk;
    }
    public void setZt(String zt){
        this.zt = zt;
    }
    public String getZt(){
        return this.zt;
    }
    public void setAmedkwz(String amedkwz){
        this.amedkwz = amedkwz;
    }
    public String getAmedkwz(){
        return this.amedkwz;
    }
    public void setPmbdkwz(String pmbdkwz){
        this.pmbdkwz = pmbdkwz;
    }
    public String getPmbdkwz(){
        return this.pmbdkwz;
    }
    public void setPmedkwz(String pmedkwz){
        this.pmedkwz = pmedkwz;
    }
    public String getPmedkwz(){
        return this.pmedkwz;
    }
    public void setAmbstate(String ambstate){
        this.ambstate = ambstate;
    }
    public String getAmbstate(){
        return this.ambstate;
    }
    public void setAmestate(String amestate){
        this.amestate = amestate;
    }
    public String getAmestate(){
        return this.amestate;
    }
    public void setPmbstate(String pmbstate){
        this.pmbstate = pmbstate;
    }
    public String getPmbstate(){
        return this.pmbstate;
    }
    public void setPmestate(String pmestate){
        this.pmestate = pmestate;
    }
    public String getPmestate(){
        return this.pmestate;
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        AttendanceDataBean other = (AttendanceDataBean)obj;
        if (other.getDate() == null){
            return false;
        }
        if (date == null){
            if (other.date != null)
                return false;
        }else if (!date.equals(other.date))
            return false;
        if (other.getDate() == null){
            return false;
        }
        if (oid == null){
            if (other.oid != null)
                return false;
        }else if (!oid.equals(other.userid))
            return false;
        return true;

    }
}
