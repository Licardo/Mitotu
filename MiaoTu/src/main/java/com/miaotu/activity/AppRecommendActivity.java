package com.miaotu.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.DownloadListener;
import android.webkit.WebView;

import com.miaotu.R;
import com.miaotu.http.HttpRequestUtil;

/**
 * Created by ying on 2015/2/10.
 */
public class AppRecommendActivity extends BaseActivity{
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_recommend);
        webView = (WebView) findViewById(R.id.webview);
        webView.loadUrl(HttpRequestUtil.getServer()+"configs/exchange");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String s, String s2, String s3, String s4, long l) {
                Uri uri = Uri.parse(s);
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });
    }
}
