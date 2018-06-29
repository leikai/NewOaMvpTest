package com.bs.lk.newoamvptest.dataparser;

import com.bs.lk.newoamvptest.bean.UserNewBean;

import org.json.JSONException;
import org.json.JSONObject;

public class UserNewParser extends DataParser {
    public static final String OID = "oid";
    public static final String DEPTID = "deptid";
    public static final String DEPTNAME = "deptname";
    public static final String DEPTOAID = "deptoaid";
    public static final String EMPEMAIL = "empEmail";
    public static final String EMPMOBILEPHONE = "empMobilephone";
    public static final String EMPID = "empid";
    public static final String EMPNAME = "empname";
    public static final String NOTE = "note";
    public static final String OAID = "oaid";
    public static final String ROLEID = "roleid";
    public static final String USERNAME = "userName";
    public static final String USERPASSWORD = "userPassword";





    @Override
    public UserNewBean parser(JSONObject jsonObject) throws JSONException {
        UserNewBean user = new UserNewBean();
//        user.setId(jsonObject.getInt(ID));
        if (jsonObject.has(OID)) {
            user.setOid(jsonObject.getString(OID));
        }
        if (jsonObject.has(DEPTID)) {
            user.setDeptid(jsonObject.getString(DEPTID));
        }
        if (jsonObject.has(DEPTNAME)) {
            user.setDeptname(jsonObject.getString(DEPTNAME));
        }
        if (jsonObject.has(DEPTOAID)) {
            user.setDeptoaid(jsonObject.getString(DEPTOAID));
        }
        if (jsonObject.has(EMPEMAIL)) {
            user.setEmpEmail(jsonObject.getString(EMPEMAIL));
        }
        if (jsonObject.has(EMPMOBILEPHONE)) {
            user.setEmpMobilephone(jsonObject.getString(EMPMOBILEPHONE));
        }
        if (jsonObject.has(EMPID)) {
            user.setEmpid(jsonObject.getString(EMPID));
        }




        if (jsonObject.has(EMPNAME)) {
            user.setEmpname(jsonObject.getString(EMPNAME));
        }


        if (jsonObject.has(NOTE)) {
            user.setNote(jsonObject.getString(NOTE));
        }
        if (jsonObject.has(OAID)) {
            user.setOaid(jsonObject.getString(OAID));
        }
        if (jsonObject.has(ROLEID)) {
            user.setRoleid(jsonObject.getString(ROLEID));
        }
        if (jsonObject.has(USERNAME)) {
            user.setUserName(jsonObject.getString(USERNAME));
        }
        if (jsonObject.has(USERPASSWORD)) {
            user.setUserPassword(jsonObject.getString(USERPASSWORD));
        }

        return user;
    }

}
