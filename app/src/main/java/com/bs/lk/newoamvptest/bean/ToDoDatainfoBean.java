package com.bs.lk.newoamvptest.bean;

import org.litepal.crud.LitePalSupport;

public class ToDoDatainfoBean extends LitePalSupport {
    private String actorname;

    private String actoroid;

    private String actorusername;

    private String createtime;

    private String emailstate;

    private String flowid;

    private String flowname;

    private String flowstepid;

    private int flowstepno;

    private String oid;

    private String ownername;

    private String owneroid;

    private String ownerusername;

    private String smsstate;

    private String taskstate;

    private String wechartstate;

    private String wfinsid;

    private String wftitle;

    public void setActorname(String actorname){
        this.actorname = actorname;
    }
    public String getActorname(){
        return this.actorname;
    }
    public void setActoroid(String actoroid){
        this.actoroid = actoroid;
    }
    public String getActoroid(){
        return this.actoroid;
    }
    public void setActorusername(String actorusername){
        this.actorusername = actorusername;
    }
    public String getActorusername(){
        return this.actorusername;
    }
    public void setCreatetime(String createtime){
        this.createtime = createtime;
    }
    public String getCreatetime(){
        return this.createtime;
    }
    public void setEmailstate(String emailstate){
        this.emailstate = emailstate;
    }
    public String getEmailstate(){
        return this.emailstate;
    }
    public void setFlowid(String flowid){
        this.flowid = flowid;
    }
    public String getFlowid(){
        return this.flowid;
    }
    public void setFlowname(String flowname){
        this.flowname = flowname;
    }
    public String getFlowname(){
        return this.flowname;
    }
    public void setFlowstepid(String flowstepid){
        this.flowstepid = flowstepid;
    }
    public String getFlowstepid(){
        return this.flowstepid;
    }
    public void setFlowstepno(int flowstepno){
        this.flowstepno = flowstepno;
    }
    public int getFlowstepno(){
        return this.flowstepno;
    }
    public void setOid(String oid){
        this.oid = oid;
    }
    public String getOid(){
        return this.oid;
    }
    public void setOwnername(String ownername){
        this.ownername = ownername;
    }
    public String getOwnername(){
        return this.ownername;
    }
    public void setOwneroid(String owneroid){
        this.owneroid = owneroid;
    }
    public String getOwneroid(){
        return this.owneroid;
    }
    public void setOwnerusername(String ownerusername){
        this.ownerusername = ownerusername;
    }
    public String getOwnerusername(){
        return this.ownerusername;
    }
    public void setSmsstate(String smsstate){
        this.smsstate = smsstate;
    }
    public String getSmsstate(){
        return this.smsstate;
    }
    public void setTaskstate(String taskstate){
        this.taskstate = taskstate;
    }
    public String getTaskstate(){
        return this.taskstate;
    }
    public void setWechartstate(String wechartstate){
        this.wechartstate = wechartstate;
    }
    public String getWechartstate(){
        return this.wechartstate;
    }
    public void setWfinsid(String wfinsid){
        this.wfinsid = wfinsid;
    }
    public String getWfinsid(){
        return this.wfinsid;
    }
    public void setWftitle(String wftitle){
        this.wftitle = wftitle;
    }
    public String getWftitle(){
        return this.wftitle;
    }


    @Override
    public boolean equals(Object object){
        if(this == object)
            return true;
        if(object == null)
            return false;
        if(this.getClass() != object.getClass())
            return false;
        final ToDoDatainfoBean toDoDatainfoBean = (ToDoDatainfoBean) object;
        if (!this.getOid().equals(toDoDatainfoBean.getOid())){
            return false;
        }
        return true;
    }




}
