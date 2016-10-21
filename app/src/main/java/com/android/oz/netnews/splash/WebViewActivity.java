package com.android.oz.netnews.splash;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.oz.netnews.R;
import com.android.oz.netnews.bean.LinksParams;

/**
 * Created by jonesleborn on 16/8/12.
 */
public class WebViewActivity extends Activity {
    private WebView wb_content;
    public static final String H5URL = "URL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        initView();
    }

    private void initView() {
        wb_content = (WebView) findViewById(R.id.wb_content);

        LinksParams serializableExtra = (LinksParams) getIntent().getSerializableExtra(H5URL);

        // 加载H5的url
        wb_content.loadUrl(serializableExtra.getLink_url());
        // wbview的属性
        wb_content.getSettings().setJavaScriptEnabled(true);

        wb_content.setWebViewClient(new WebViewClient() {
            // 当WebView的加载的网页发生跳转时，被调用的
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    // 点击返回之后发生的事件
    @Override
    public void onBackPressed() {
        if (wb_content.canGoBack()) {
            wb_content.goBack();
            return;
        }
        // 如果不能回退了，就停止程序
        finish();
    }
}
