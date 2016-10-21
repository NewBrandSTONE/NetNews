package com.android.oz.netnews.utils;

import android.text.TextUtils;

/**
 * Created by jonesleborn on 16/8/16.
 */
public abstract class HttpResponse<T> {

    // 某一种类的类型
    private Class<T> tClass;

    // 告诉HttpResponse需要什么类型
    public HttpResponse(Class<T> tClass) {
        this.tClass = tClass;
    }

    public abstract void onSuccess(T t);

    public abstract void onFail(String msg);

    public void parse(String json) {

        if (TextUtils.isEmpty(json)) {
            // 如果json为空，说明解析失败
            onFail("Json 解析失败");
            return;
        }

        if (tClass == String.class) {
            // 说明此时我需要的结果是一个字符串类型的
            onSuccess((T) json);
            // 也就是不需要这个工具类进行转换了
            return;
        }

        T Object = JsonUtil.getJsonObject(json, tClass);

        if (Object != null) {
            onSuccess(Object);
        } else {
            onFail("json 解析失败");
        }

    }
}
