package com.miaotu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.miaotu.R;


public class PublishCustomTourActivity5 extends BaseActivity {
    private WebView webView;
    private TextView tvTitle,tvLeft;
    Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_publish5);
        webView = (WebView) findViewById(R.id.webview);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvLeft = (TextView) findViewById(R.id.tv_left);
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvTitle.setText("定制约游");
        WebSettings wSet = webView.getSettings();
        wSet.setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) { //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl("http://m.miaotu.com/App/creatRes");
    }
}
