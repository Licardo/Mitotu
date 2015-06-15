package com.miaotu.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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
        WebSettings wSet = webView.getSettings();
        wSet.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JSInterface(), "native");

        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) { //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl(getIntent().getStringExtra("url"));
    }

    public final class JSInterface {

        @android.webkit.JavascriptInterface
        public void enterCustomTourDetail(String id){
            //线路详情
            Intent intent = new Intent(HomeBannerWebActivity.this, CustomTourDetailActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        }
    }

}
