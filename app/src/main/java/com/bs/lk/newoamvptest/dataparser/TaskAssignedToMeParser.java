package com.bs.lk.newoamvptest.dataparser;

import com.bs.lk.newoamvptest.bean.TaskAssignedToMeBean;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by baggio on 2017/2/27.
 */

public class TaskAssignedToMeParser extends DataParser {
    public static final String OID = "oid";
    public static final String TITLE = "title";
    public static final String NOTE = "note";

    @Override
    public TaskAssignedToMeBean parser(JSONObject jsonObject) throws JSONException {
        TaskAssignedToMeBean task = new TaskAssignedToMeBean();
        if (jsonObject.has(OID)) {
            task.setOId(jsonObject.getString(OID));
        }
        if (jsonObject.has(TITLE)) {
            task.setTitle(jsonObject.getString(TITLE));
        }
        if (jsonObject.has(NOTE)) {
            task.setNote(jsonObject.getString(NOTE));
        }
        return task;
    }
}
