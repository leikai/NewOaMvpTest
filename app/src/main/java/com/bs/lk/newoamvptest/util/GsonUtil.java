package com.bs.lk.newoamvptest.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class GsonUtil {
    private static Gson GSON = new Gson();

    /**
     * JSON转Bean
     * */
    public static <T> T getBeanFromJson(String json, Class<?> clazz){
        return (T) GSON.fromJson(json, clazz);
    }

    /**
     * JSON转List<T>
     */
    public static <T> List<T> getListFromJson(String json, TypeToken<List<T>> tt) {
        return GSON.fromJson(json, tt.getType());
    }

    /**
     * 根据key从json中取出value
     * */
    public static String getJsonFromKey(String json, String key) {
        try {
            return getValue(new JSONObject(json), key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getValue(JSONObject json, String key) {
        if (json == null) {
            return "";
        }
        String str = null;
        try {
            str = json.getString(key);
        } catch (JSONException e) {
            str = "";
        }
        return str;
    }

    public static String getMessage(String json) {
        return getJsonFromKey(json, "message");
    }

    public static Integer getCode(String json) {
        String code = getJsonFromKey(json, "code");
        return Integer.valueOf(code);
    }

    public static String getData(String json) {
        return getJsonFromKey(json, "data");
    }
}
