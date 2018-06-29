package com.bs.lk.newoamvptest.dataparser;

import com.bs.lk.newoamvptest.bean.ViewComponent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by baggio on 2017/2/20.
 */

public class ViewComponentParser extends DataParser {
    public static final String VIEW = "view";
    public static final String INPUTNAME = "inputname";
    @Override
    public ViewComponent parser(JSONObject jsonObject) throws JSONException {
        ViewComponent component = new ViewComponent();
//        ComponentParser.parser(component,jsonObject);
        if (jsonObject.has(VIEW)) {
            component.setViewName(jsonObject.getString(VIEW));
        }
        if (jsonObject.has(INPUTNAME)) {
            component.setInputName(jsonObject.getString(INPUTNAME));
        }
        return component;
    }
}
