package com.android.oz.netnews.splash;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.android.oz.netnews.Interface.IRingTextView;
import com.android.oz.netnews.R;
import com.android.oz.netnews.constant.FileConstant;
import com.android.oz.netnews.main.activity.IndexActivity;
import com.android.oz.netnews.bean.Adert;
import com.android.oz.netnews.bean.Ads;
import com.android.oz.netnews.constant.MyConstance;
import com.android.oz.netnews.helper.MD5Helper;
import com.android.oz.netnews.service.AdsIntentService;
import com.android.oz.netnews.utils.HttpResponse;
import com.android.oz.netnews.utils.HttpUtil;
import com.android.oz.netnews.utils.ImgUtil;
import com.android.oz.netnews.utils.JsonUtil;
import com.android.oz.netnews.utils.SharedPreferenceUtil;
import com.android.oz.netnews.widget.RingTextView;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private ImageView iv_logo;
    private RingTextView rtv_ring;
    private static final int REFRESH_PROGRESS = 1;
    private static final int GOTO_HOME = 2;

    // 等待的秒数 2秒  1秒刷新 4次
    private int totalWaitSec = 2 * 4;
    private MyRunnerbale runnerbale = new MyRunnerbale();

    private Handler handler = new Handler() {
        private int index = 0;

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case REFRESH_PROGRESS:
                    if (index > totalWaitSec) {
                        // 等够时间了
                        gotoMain();
                        handler.removeCallbacks(runnerbale);
                    } else {
                        // 还需要继续刷新
                        rtv_ring.setProgress(totalWaitSec, index);
                    }
                    index++;
                    break;
                case GOTO_HOME:
                    gotoMain();
                    break;
            }
        }
    };

    private void gotoMain() {
        Intent intent = MyConstance.getNewIntent(MainActivity.this, IndexActivity.class);
        startActivity(intent);
        //设置activity跳转动画的
        overridePendingTransition(R.anim.activity_enter, 0);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 设置全屏
        initScreenLayout();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        // 获取服务器的响应数据
        getData();
        // 设置图片
        getImag();

    }

    private void initView() {
        iv_logo = (ImageView) findViewById(R.id.iv_logo);
        rtv_ring = (RingTextView) findViewById(R.id.rtv_ring);
        rtv_ring.setiRingTextViewClick(new IRingTextView() {
            @Override
            public void setOnclick(RingTextView ring) {
                gotoMain();
                handler.removeCallbacks(runnerbale);
            }
        });
    }

    // 通过缓存获取图片
    public void getImag() {
        String cachData = SharedPreferenceUtil.getString(MainActivity.this, "cachData");
        if (!TextUtils.isEmpty(cachData)) {
            // 有图片才显示跳转
            handler.post(runnerbale);
            Ads ads = JsonUtil.getJsonObject(cachData, Ads.class);
            if (ads != null) {
                // 获取图片的地址
                List<Adert> adertList = ads.getAds();
                if (adertList != null) {
                    // 需要取出当前显示图片的角标
                    int imgIndex = SharedPreferenceUtil.getInt(MainActivity.this, "imgIndex");
                    imgIndex = imgIndex % adertList.size();
                    final Adert adert = adertList.get(imgIndex);
                    List<String> res_url = adert.getRes_url();
                    String img_url = res_url.get(0);
                    if (!TextUtils.isEmpty(img_url)) {
                        String img_url_md5 = MD5Helper.toMD5(img_url);
                        Bitmap bitmap = ImgUtil.getImageByName(img_url_md5);
                        if (bitmap != null) {
                            iv_logo.setImageBitmap(bitmap);
                            imgIndex++;
                            SharedPreferenceUtil.saveInt(MainActivity.this, "imgIndex", imgIndex);

                            // 添加点击事件
                            iv_logo.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    //删除这个任务
                                    handler.removeCallbacks(runnerbale);

                                    Intent intent = MyConstance.getNewIntent(MainActivity.this, WebViewActivity.class);
                                    // 将跳转的url放入intent中
                                    intent.putExtra(WebViewActivity.H5URL, adert.getAction_params());
                                    startActivity(intent);

                                    //设置activity跳转动画的
                                    overridePendingTransition(R.anim.activity_enter, 0);
                                    finish();
                                }
                            });
                        }
                    }
                }
            }
        }
    }


    private void getData() {

        // 先判断sp中是否有缓存
        String cachData = SharedPreferenceUtil.getString(MainActivity.this, "cachData");
        if (TextUtils.isEmpty(cachData)) {
            getAds2();
        } else {
            // 如果缓存不是空的话，判断一下这个缓存是否过期
            long last_success = SharedPreferenceUtil.getLong(MainActivity.this, "last_success");
            long now_success = System.currentTimeMillis();
            long wait_success = now_success - last_success;
            int next_req = SharedPreferenceUtil.getInt(MainActivity.this, "next_req");
            if (wait_success > next_req * 60 * 60 * 1000) {
                // 说明缓存已经过时了，需要重新加载
                getAds2();
            }
        }
    }


    private void getAds2() {
        HttpUtil util = HttpUtil.getInstace();
        util.doGet(FileConstant.ads_url, new HttpResponse<String>(String.class) {
            @Override
            public void onSuccess(String body) {

                // 因为后面需要将json数据缓存起来，所以
                // 我们传入一个String的字符串告诉HttpUtil不需要转换这个JSon
                // 在这里做转换
                Ads ads = JsonUtil.getJsonObject(body, Ads.class);

                if (ads != null) {
                    // 说明解析成功了
                    // 将广告的json缓存
                    SharedPreferenceUtil.saveString(MainActivity.this, "cachData", body);
                    // 缓存上次成功获取连接的时间
                    SharedPreferenceUtil.saveLong(MainActivity.this, "last_success", System.currentTimeMillis());
                    // 缓存下一次需要发送请求的时间
                    SharedPreferenceUtil.saveInt(MainActivity.this, "next_req", ads.getNext_req());
                }
                // 启动IntentService
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, AdsIntentService.class);
                intent.putExtra("data", ads);
                startService(intent);
            }

            @Override
            public void onFail(String msg) {
                Log.v("Oz", msg);
            }
        });
    }


    private void getAds() {
        // 1.获取okhttpclient对象
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(FileConstant.ads_url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful())
                    throw new IOException("respon处理异常->" + response.code());

                // 获取到的数据内容
                String data = response.body().string();

                if (!TextUtils.isEmpty(data)) {
                    Ads ads = JsonUtil.getJsonObject(data, Ads.class);
                    if (ads != null) {
                        // 说明解析成功了
                        // 将广告的json缓存
                        SharedPreferenceUtil.saveString(MainActivity.this, "cachData", data);
                        // 缓存上次成功获取连接的时间
                        SharedPreferenceUtil.saveLong(MainActivity.this, "last_success", System.currentTimeMillis());
                        // 缓存下一次需要发送请求的时间
                        SharedPreferenceUtil.saveInt(MainActivity.this, "next_req", ads.getNext_req());
                    }
                    // 启动IntentService
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, AdsIntentService.class);
                    intent.putExtra("data", ads);
                    startService(intent);
                }
            }
        });
    }

    /**
     * 设置全屏
     */
    private void initScreenLayout() {
        // 隐藏App的标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 将整个广告页设置成全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private static int tatol = (2 * 2000) / 250;


    class MyRunnerbale implements Runnable {
        @Override
        public void run() {
            handler.sendEmptyMessage(REFRESH_PROGRESS);
            handler.postDelayed(runnerbale, 250);
        }
    }


}
