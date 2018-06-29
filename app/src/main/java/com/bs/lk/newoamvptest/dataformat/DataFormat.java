package com.bs.lk.newoamvptest.dataformat;

import android.text.TextUtils;

import com.bs.lk.newoamvptest.bean.AssignSubTaskBean;
import com.bs.lk.newoamvptest.bean.AssignTaskBean;
import com.bs.lk.newoamvptest.bean.DepartmentBean;
import com.bs.lk.newoamvptest.bean.RoleBean;
import com.bs.lk.newoamvptest.bean.TaskBean;
import com.bs.lk.newoamvptest.bean.UserNewBean;
import com.bs.lk.newoamvptest.dataparser.AssignTaskParser;
import com.bs.lk.newoamvptest.dataparser.DepartmentParser;
import com.bs.lk.newoamvptest.dataparser.RoleParser;
import com.bs.lk.newoamvptest.dataparser.TaskParser;
import com.bs.lk.newoamvptest.dataparser.UserNewParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by baggio on 2017/2/6.
 */

public class DataFormat {
    public static JSONObject formatUserNew(UserNewBean user) throws JSONException {
        JSONObject json = new JSONObject();
        if (!TextUtils.isEmpty(user.getOid())) {
            json.put(UserNewParser.OID, user.getOid());
        }
        if (!TextUtils.isEmpty(user.getDeptid())) {
            json.put(UserNewParser.DEPTID, user.getDeptid());
        }
        if (!TextUtils.isEmpty(user.getDeptname())) {
            json.put(UserNewParser.DEPTNAME, user.getDeptname());
        }
        if (!TextUtils.isEmpty(user.getDeptoaid())) {
            json.put(UserNewParser.DEPTOAID, user.getDeptoaid());
        }
        if (!TextUtils.isEmpty(user.getEmpEmail())) {
            json.put(UserNewParser.EMPEMAIL, user.getEmpEmail());
        }
        if (!TextUtils.isEmpty(user.getEmpMobilephone())) {
            json.put(UserNewParser.EMPMOBILEPHONE, user.getEmpMobilephone());
        }
        if (!TextUtils.isEmpty(user.getEmpid())) {
            json.put(UserNewParser.EMPID, user.getEmpid());
        }
        if (!TextUtils.isEmpty(user.getEmpname())) {
            json.put(UserNewParser.EMPNAME, user.getEmpname());
        }
        if (!TextUtils.isEmpty(user.getNote())) {
            json.put(UserNewParser.NOTE, user.getNote());
        }
        if (!TextUtils.isEmpty(user.getOaid())) {
            json.put(UserNewParser.OAID, user.getOaid());
        }
        if (!TextUtils.isEmpty(user.getRoleid())) {
            json.put(UserNewParser.ROLEID, user.getRoleid());
        }
        if (!TextUtils.isEmpty(user.getUserName())) {
            json.put(UserNewParser.USERNAME, user.getUserName());
        }
        if (!TextUtils.isEmpty(user.getUserPassword())) {
            json.put(UserNewParser.USERPASSWORD, user.getUserName());
        }
        return json;
    }

    public static JSONObject formatDepartment(DepartmentBean department) throws JSONException {
        JSONObject json = new JSONObject();
        if (!TextUtils.isEmpty(department.getId())) {
            json.put(DepartmentParser.ID, department.getId());
        }
        if (!TextUtils.isEmpty(department.getDepartmentFullId())) {
            json.put(DepartmentParser.DEPARTMENTFULLID, department.getDepartmentFullId());
        }
        if (!TextUtils.isEmpty(department.getDepartmentName())) {
            json.put(DepartmentParser.DEPARTMENTNAME, department.getDepartmentName());
        }
        if (!TextUtils.isEmpty(department.getDepartmentFullName())) {
            json.put(DepartmentParser.DEPARTMENTFULLNAME, department.getDepartmentFullName());
        }
        if (!TextUtils.isEmpty(department.getDepartmentNo())) {
            json.put(DepartmentParser.DEPARTMENTNO, department.getDepartmentNo());
        }
        if (!TextUtils.isEmpty(department.getDepartmentZone())) {
            json.put(DepartmentParser.DEPARTMENTZONE, department.getDepartmentZone());
        }
        if (!TextUtils.isEmpty(department.getParentDepartmentId())) {
            json.put(DepartmentParser.PARENTDEPARTMENTID, department.getParentDepartmentId());
        }
        if (!TextUtils.isEmpty(department.getLayer())) {
            json.put(DepartmentParser.LAYER, department.getLayer());
        }
        if (!TextUtils.isEmpty(department.getExtend1())) {
            json.put(DepartmentParser.EXTEND1, department.getExtend1());
        }
        if (!TextUtils.isEmpty(department.getExtend2())) {
            json.put(DepartmentParser.EXTEND2, department.getExtend2());
        }
        return json;
    }

    public static JSONObject formatRole(RoleBean role) throws JSONException {
        JSONObject json = new JSONObject();
        if (!TextUtils.isEmpty(role.getId())) {
            json.put(RoleParser.ID, role.getId());
        }
        if (!TextUtils.isEmpty(role.getRoleName())) {
            json.put(RoleParser.ROLENAME, role.getRoleName());
        }
        if (!TextUtils.isEmpty(role.getGroupName())) {
            json.put(RoleParser.GROUPNAME, role.getGroupName());
        }
        return json;
    }

    public static JSONObject formatTask(TaskBean task) throws JSONException {
        JSONObject json = new JSONObject();
        json.put(TaskParser.TASKID, task.getTaskId());
        if (!TextUtils.isEmpty(task.getActivityDefId())) {
            json.put(TaskParser.ACTIVITYDEFID, task.getActivityDefId());
        }
        if (!TextUtils.isEmpty(task.getBeginTime())) {
            json.put(TaskParser.BEGINTIME, task.getBeginTime());
        }
        if (!TextUtils.isEmpty(task.getBindUrl())) {
            json.put(TaskParser.BINDURL, task.getBindUrl());
        }
        if (!TextUtils.isEmpty(task.getOwner())) {
            json.put(TaskParser.OWNER, task.getOwner());
        }
        if (!TextUtils.isEmpty(task.getOwnerName())) {
            json.put(TaskParser.OWNERNAME, task.getOwnerName());
        }
        if (!TextUtils.isEmpty(task.getProcessDefId())) {
            json.put(TaskParser.PROCESSDEFID, task.getProcessDefId());
        }
        if (!TextUtils.isEmpty(task.getProcessGroup())) {
            json.put(TaskParser.PROCESSGROUP, task.getProcessGroup());
        }
        if (!TextUtils.isEmpty(task.getReadTime())) {
            json.put(TaskParser.READTIME, task.getReadTime());
        }
        if (!TextUtils.isEmpty(task.getStepname())) {
            json.put(TaskParser.STEPNAME, task.getStepname());
        }
        if (!TextUtils.isEmpty(task.getTitle())) {
            json.put(TaskParser.TITLE, task.getTitle());
        }
        if (!TextUtils.isEmpty(task.getWftitle())) {
            json.put(TaskParser.WFTITLE, task.getWftitle());
        }
        json.put(TaskParser.PROCESSINSTANCEID, task.getProcessInstanceId());
        json.put(TaskParser.PRIORITY, String.valueOf(task.getPriority()));
        json.put(TaskParser.STATUS, String.valueOf(task.getStatus()));
        json.put(TaskParser.ISREAD, task.isRead() ? true : false);
        return json;
    }

    public static JSONObject formatAssignTask(AssignTaskBean assignTask) throws JSONException {
        JSONObject json = new JSONObject();
        if (!TextUtils.isEmpty(assignTask.getWTitle())) {
            json.put(AssignTaskParser.WTITLE, assignTask.getWTitle());
        }
        if (!TextUtils.isEmpty(assignTask.getNote())) {
            json.put(AssignTaskParser.NOTE, assignTask.getNote());
        }
        if (!TextUtils.isEmpty(assignTask.getSTime())) {
            json.put(AssignTaskParser.STIME, assignTask.getSTime());
        }
        if (!TextUtils.isEmpty(assignTask.getETime())) {
            json.put(AssignTaskParser.ETIME, assignTask.getETime());
        }
        if (!TextUtils.isEmpty(assignTask.getAlertTime())) {
            json.put(AssignTaskParser.ALERTTIME, assignTask.getAlertTime());
        }
        if (!TextUtils.isEmpty(assignTask.getWContent())) {
            json.put(AssignTaskParser.WCONTENT, assignTask.getWContent());
        }
        if (!TextUtils.isEmpty(assignTask.getWsMan())) {
            json.put(AssignTaskParser.WSMAN, assignTask.getWsMan());
        }
        if (!TextUtils.isEmpty(assignTask.getWsManId())) {
            json.put(AssignTaskParser.WSMANID, assignTask.getWsManId());
        }
        json.put(AssignTaskParser.WSMANOAID, assignTask.getWsManOAId());
        if (assignTask.getDetails() != null) {
            JSONArray jsonArray = new JSONArray();
            for (AssignSubTaskBean subTask : assignTask.getDetails()) {
                JSONObject jsonObject = formatAssignSubTask(subTask);
                jsonArray.put(jsonObject);
            }
            json.put(AssignTaskParser.DETAILS, jsonArray);
        }
        return json;
    }

    public static JSONObject formatAssignSubTask(AssignSubTaskBean assignSubTask) throws
            JSONException {
        JSONObject json = new JSONObject();
        if (!TextUtils.isEmpty(assignSubTask.getSTime())) {
            json.put(AssignTaskParser.STIME, assignSubTask.getSTime());
        }
        if (!TextUtils.isEmpty(assignSubTask.getETime())) {
            json.put(AssignTaskParser.ETIME, assignSubTask.getETime());
        }
        if (!TextUtils.isEmpty(assignSubTask.getContent())) {
            json.put(AssignTaskParser.WCONTENT, assignSubTask.getContent());
        }
        if (!TextUtils.isEmpty(assignSubTask.getWsMan())) {
            json.put(AssignTaskParser.WSMAN, assignSubTask.getWsMan());
        }
        if (!TextUtils.isEmpty(assignSubTask.getWsManId())) {
            json.put(AssignTaskParser.WSMANID, assignSubTask.getWsManId());
        }
        json.put(AssignTaskParser.WSMANOAID, assignSubTask.getWsManOAId());
        return json;
    }
}
