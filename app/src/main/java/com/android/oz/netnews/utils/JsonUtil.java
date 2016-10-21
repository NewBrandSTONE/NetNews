package com.android.oz.netnews.utils;

import android.text.TextUtils;

import com.google.gson.Gson;

/**
 * Created by jonesleborn on 16/8/11.
 */
public class JsonUtil {

    private static Gson gson;

    public static <T> T getJsonObject(String content, Class<T> clz) {
        // 如果content为空，则返回空
        if (TextUtils.isEmpty(content)) {
            return null;
        }

        if (gson == null) {
            gson = new Gson();
        }

        return gson.fromJson(content, clz);
    }
}
