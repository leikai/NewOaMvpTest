package com.bs.lk.newoamvptest.bean;

import java.util.List;

/**
 * 通知
 */
public class NotificationAnnouncementBean {
    private String oid;

    private String orgid;

    private String deptname;

    private String username;

    private String title;

    private String content;

    private String spusername;

    private String spuserid;

    private String sendtime;

    private String filepath;

    private String noticestate;

    private String copystate;

    private String titlestrong;

    private String titlered;

    private String type;

    private String createtime;

    private List<BoTzggImages> boTzggImages ;

    public void setOid(String oid){
        this.oid = oid;
    }
    public String getOid(){
        return this.oid;
    }
    public void setOrgid(String orgid){
        this.orgid = orgid;
    }
    public String getOrgid(){
        return this.orgid;
    }
    public void setDeptname(String deptname){
        this.deptname = deptname;
    }
    public String getDeptname(){
        return this.deptname;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public String getUsername(){
        return this.username;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }
    public void setContent(String content){
        this.content = content;
    }
    public String getContent(){
        return this.content;
    }
    public void setSpusername(String spusername){
        this.spusername = spusername;
    }
    public String getSpusername(){
        return this.spusername;
    }
    public void setSpuserid(String spuserid){
        this.spuserid = spuserid;
    }
    public String getSpuserid(){
        return this.spuserid;
    }
    public void setSendtime(String sendtime){
        this.sendtime = sendtime;
    }
    public String getSendtime(){
        return this.sendtime;
    }
    public void setFilepath(String filepath){
        this.filepath = filepath;
    }
    public String getFilepath(){
        return this.filepath;
    }
    public void setNoticestate(String noticestate){
        this.noticestate = noticestate;
    }
    public String getNoticestate(){
        return this.noticestate;
    }
    public void setCopystate(String copystate){
        this.copystate = copystate;
    }
    public String getCopystate(){
        return this.copystate;
    }
    public void setTitlestrong(String titlestrong){
        this.titlestrong = titlestrong;
    }
    public String getTitlestrong(){
        return this.titlestrong;
    }
    public void setTitlered(String titlered){
        this.titlered = titlered;
    }
    public String getTitlered(){
        return this.titlered;
    }
    public void setType(String type){
        this.type = type;
    }
    public String getType(){
        return this.type;
    }
    public void setCreatetime(String createtime){
        this.createtime = createtime;
    }
    public String getCreatetime(){
        return this.createtime;
    }
    public void setBoTzggImages(List<BoTzggImages> boTzggImages){
        this.boTzggImages = boTzggImages;
    }
    public List<BoTzggImages> getBoTzggImages(){
        return this.boTzggImages;
    }
}
