package com.miaotu.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.miaotu.R;


public class CustomTourDetailActivity extends BaseActivity {
private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_tour);
        webView = (WebView) findViewById(R.id.webview);
        WebSettings wSet = webView.getSettings();
        wSet.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JSInterface(), "native");
        webView.loadUrl("http://m.miaotu.com/pages/detail.html"+"&id="+getIntent().getStringExtra("id"));
    }
    /**
     * js调用java的接口ueq
     * @author ying
     *
     */
    public final class JSInterface {
        //JavaScript脚本代码可以调用的函数onClick()处理
        @android.webkit.JavascriptInterface
        public void click(String action) {
            showToastMsg("点击了"+action);
        }
    }
}
