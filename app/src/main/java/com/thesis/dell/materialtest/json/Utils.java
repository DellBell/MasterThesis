package com.thesis.dell.materialtest.json;

import org.json.JSONObject;

/**
 * Created by Administrator on 22.09.2015.
 */
public class Utils {
    public static boolean contains(JSONObject jsonObject, String key) {
        return jsonObject != null && jsonObject.has(key) && !jsonObject.isNull(key) ? true : false;
    }
}
