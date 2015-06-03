package com.miaotu.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.miaotu.R;

public class FunctionIntroduceActivity extends BaseActivity implements View.OnClickListener{

    private TextView tvLeft,tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_money_introduce);

        init();
    }

    private void init(){
        tvLeft = (TextView) findViewById(R.id.tv_left);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        WebView webView = (WebView) findViewById(R.id.webview);
        webView.loadUrl("http://m.miaotu.com/app/intro_miaotu.html");
        tvTitle.setText("功能说明");
        tvLeft.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_left:
                finish();
                break;
            default:
                break;
        }
    }
}
