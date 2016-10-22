package com.android.oz.netnews.main.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.oz.netnews.R;
import com.android.oz.netnews.bean.DocDetail;
import com.android.oz.netnews.bean.DocDetailImg;
import com.android.oz.netnews.constant.FileConstant;
import com.android.oz.netnews.constant.MyConstance;
import com.android.oz.netnews.utils.HttpResponse;
import com.android.oz.netnews.utils.HttpUtil;
import com.android.oz.netnews.utils.JsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * @author O.z Young
 * @date 16/10/22
 * @desc 新闻详细界面显示内容
 */

public class NewDetailActivity extends Activity {

    public static final String DOC_ID = "docId";

    private EditText editText;

    private RelativeLayout share_feed;
    private TextView news_article_send;

    private String docId;

    private static final int INIT = 1;

    private WebView webview;

    private InnerHandler handler;

    private DocDetail docDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_news_detail);

        // 获取docID
        docId = getIntent().getStringExtra(DOC_ID);

        initView();

        initLisenter();

        getDate(docId);

        handler = new InnerHandler(this);
    }

    @SuppressLint("JavascriptInterface")
    private void initView() {
        editText = (EditText) findViewById(R.id.edit_text);
        share_feed = (RelativeLayout) findViewById(R.id.share_feed);
        news_article_send = (TextView) findViewById(R.id.news_article_send);
        webview = (WebView) findViewById(R.id.webview);

        // 开启javascript
        webview.getSettings().setJavaScriptEnabled(true);

        // webView与java代码交互
        webview.addJavascriptInterface(this, "demo");
    }

    private void initLisenter() {
        // 焦点监听的回调
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.v("Oz", "是否有焦点" + hasFocus);
                // 如果有焦点的话将回帖数隐藏
                if (hasFocus) {
                    share_feed.setVisibility(GONE);
                    news_article_send.setVisibility(View.VISIBLE);
                } else {
                    share_feed.setVisibility(VISIBLE);
                    news_article_send.setVisibility(GONE);
                }
            }
        });
    }

    @JavascriptInterface
    public void showImage() {
        Intent intent = MyConstance.getNewIntent(this, PhotoDetailActivity.class);
        intent.putExtra(PhotoDetailActivity.IMAGES, docDetail);
        startActivity(intent);
    }

    public void getDate(final String id) {
        HttpUtil httpUtil = HttpUtil.getInstace();
        String url = FileConstant.getDetailUrl(id);
        httpUtil.doGet(url, new HttpResponse<String>(String.class) {
            @Override
            public void onSuccess(String o) {
                try {
                    Log.v("Oz", o);
                    JSONObject object = new JSONObject(o);
                    JSONObject js_id = object.optJSONObject(id);
                    docDetail = JsonUtil.getJsonObject(js_id.toString(), DocDetail.class);

                    if (docDetail != null) {
                        String body = docDetail.getBody();
                        List<DocDetailImg> imgList = docDetail.getImg();
                        if (imgList != null) {
                            for (int i = 0; i < imgList.size(); i++) {
                                DocDetailImg docDetailImg = imgList.get(i);
                                String src = docDetailImg.getSrc();
                                String path = "<!--IMG#" + i + "-->";
                                String imgTag = "<img style='width:100%'src='" + src + "' onclick=\"show()\">";
                                body = body.replaceFirst(path, imgTag);
                            }
                        }

                        // 标题头
                        String titleHTML = "<p><span style='font-size:18px;'><strong>" + docDetail.getTitle() + "</strong></span></p>";

                        // "&nbsp&nbsp" + doc.getPtime() + doc.getSource()

                        titleHTML = titleHTML + "<p><span style='color:#666666;'>" + docDetail.getSource() + "&nbsp&nbsp" + docDetail.getPtime() + "</span></p>";

                        body = titleHTML + body;

                        body = "<html><head><script type='text/javascript'>function show(){window.demo.showImage()} </script></head><body>" + body + "</body></html>";

                        Message message = handler.obtainMessage();
                        message.obj = body;
                        message.what = INIT;
                        handler.sendMessage(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    // 有可能加载到403forbidden
                    Message message = handler.obtainMessage();
                    message.obj = o;
                    message.what = INIT;
                    handler.sendMessage(message);
                }
            }

            @Override
            public void onFail(String msg) {
                Log.v("Oz", "加载失败");


            }
        });
    }

    public void setWebView(String html) {
        // 加载html的方法
        webview.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
    }

    public static class InnerHandler extends android.os.Handler {
        WeakReference<NewDetailActivity> activity;

        public InnerHandler(NewDetailActivity activity) {
            this.activity = new WeakReference<NewDetailActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            NewDetailActivity newActivity = activity.get();

            if (newActivity == null) {
                return;
            }

            switch (msg.what) {
                case INIT:
                    String html = (String) msg.obj;
                    newActivity.setWebView(html);
                    break;
            }
        }
    }

}
