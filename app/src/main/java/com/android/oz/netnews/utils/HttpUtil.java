package com.android.oz.netnews.utils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by jonesleborn on 16/8/16.
 */
public class HttpUtil {

    private static HttpUtil util;

    private OkHttpClient client;

    private HttpUtil() {
        // 1.生成OKHttp
        client = new OkHttpClient();
    }

    public void doGet(String url, final HttpResponse httpResponse) {
        // 2.生成Request请求地址
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (httpResponse != null) {
                    httpResponse.onFail("请求失败");
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    // 如果响应不成功
                    // 记得使用对象之前一定要判断非空
                    if (httpResponse != null) {
                        httpResponse.onFail("响应失败");
                    }
                }

                // 拿到请求体
                String body = response.body().string();

                if (body != null) {
                    // 记得要交给httpResponse这个对象去处理
                    httpResponse.parse(body);
                }
            }
        });
    }


    // 单例模式
    // 保证多线程下面只有一个对象
    public static HttpUtil getInstace() {
        if (util == null) {

            // 静态方法应该使用类锁
            synchronized (HttpUtil.class) {
                if (util == null) {
                    // 双保险，确定util只有一个
                    util = new HttpUtil();
                }
            }
        }
        return util;
    }

}
