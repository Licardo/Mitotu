package com.miaotu.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import com.miaotu.R;

public class HomeBannerWebActivity extends BaseActivity {

    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_banner_web);

        initView();
        initData();
    }

    private void initView(){
        webView = (WebView) findViewById(R.id.webview);
    }

    private void initData(){
        webView.loadUrl(getIntent().getStringExtra("url"));
    }

}
