package com.bs.lk.newoamvptest.dataparser;

import com.bs.lk.newoamvptest.bean.RoleBean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by baggio on 2017/2/8.
 */

public class RoleParser extends DataParser {
    public static final String ID = "id";
    public static final String ROLENAME = "roleName";
    public static final String GROUPNAME = "groupName";

    @Override
    public RoleBean parser(JSONObject jsonObject) throws JSONException {
        RoleBean role = new RoleBean();
        if (jsonObject.has(ID)) {
            role.setId(jsonObject.getString(ID));
        }
        if (jsonObject.has(ROLENAME)) {
            role.setRoleName(jsonObject.getString(ROLENAME));
        }
        if (jsonObject.has(GROUPNAME)) {
            role.setGroupName(jsonObject.getString(GROUPNAME));
        }
        return role;
    }
}
