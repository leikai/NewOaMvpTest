package com.bs.lk.newoamvptest.bean;

import android.text.TextUtils;

import java.io.Serializable;

public class UserNewBean implements Serializable {
    private String deptid;

    private String deptname;

    private String deptoaid;

    private String empEmail;

    private String empMobilephone;

    private String empid;

    private String empname;

    private String note;

    private String oaid;

    private String oid;

    private String roleid;

    private String userName;

    private String userPassword;

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
    public void setDeptoaid(String deptoaid){
        this.deptoaid = deptoaid;
    }
    public String getDeptoaid(){
        return this.deptoaid;
    }
    public void setEmpEmail(String empEmail){
        this.empEmail = empEmail;
    }
    public String getEmpEmail(){
        return this.empEmail;
    }
    public void setEmpMobilephone(String empMobilephone){
        this.empMobilephone = empMobilephone;
    }
    public String getEmpMobilephone(){
        return this.empMobilephone;
    }
    public void setEmpid(String empid){
        this.empid = empid;
    }
    public String getEmpid(){
        return this.empid;
    }
    public void setEmpname(String empname){
        this.empname = empname;
    }
    public String getEmpname(){
        return this.empname;
    }
    public void setNote(String note){
        this.note = note;
    }
    public String getNote(){
        return this.note;
    }
    public void setOaid(String oaid){
        this.oaid = oaid;
    }
    public String getOaid(){
        return this.oaid;
    }
    public void setOid(String oid){
        this.oid = oid;
    }
    public String getOid(){
        return this.oid;
    }
    public void setRoleid(String roleid){
        this.roleid = roleid;
    }
    public String getRoleid(){
        return this.roleid;
    }
    public void setUserName(String userName){
        this.userName = userName;
    }
    public String getUserName(){
        return this.userName;
    }
    public void setUserPassword(String userPassword){
        this.userPassword = userPassword;
    }
    public String getUserPassword(){
        return this.userPassword;
    }


    public void copyUserInfo(UserNewBean user) {
        setOid(user.getOid());
        if (!TextUtils.isEmpty(user.getDeptid())) {
            setDeptid(user.getDeptid());
        }
        if (!TextUtils.isEmpty(user.getDeptname())) {
            setDeptname(user.getDeptname());
        }
        if (!TextUtils.isEmpty(user.getDeptoaid())) {
            setDeptoaid(user.getDeptoaid());
        }
        if (!TextUtils.isEmpty(user.getEmpEmail())) {
            setEmpEmail(user.getEmpEmail());
        }
        if (!TextUtils.isEmpty(user.getEmpMobilephone())) {
            setEmpMobilephone(user.getEmpMobilephone());
        }
        if (!TextUtils.isEmpty(user.getEmpid())) {
            setEmpid(user.getEmpid());
        }
        if (!TextUtils.isEmpty(user.getEmpname())) {
            setEmpname(user.getEmpname());
        }
        if (!TextUtils.isEmpty(user.getNote())) {
            setNote(user.getNote());
        }
        if (!TextUtils.isEmpty(user.getOaid())) {
            setOaid(user.getOaid());
        }
        if (!TextUtils.isEmpty(user.getRoleid())) {
            setRoleid(user.getRoleid());
        }
        if (!TextUtils.isEmpty(user.getUserName())) {
            setUserName(user.getUserName());
        }
        if (!TextUtils.isEmpty(user.getUserPassword())) {
            setUserPassword(user.getUserPassword());
        }
    }
}
