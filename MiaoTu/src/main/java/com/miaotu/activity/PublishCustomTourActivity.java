package com.miaotu.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.miaotu.R;


public class PublishCustomTourActivity extends BaseActivity {
private WebView webView;
    private TextView tvTitle;
    Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_tour);
        webView = (WebView) findViewById(R.id.webview);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText("发起定制");
        WebSettings wSet = webView.getSettings();
        wSet.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JSInterface(), "native");

        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) { //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return true;
            }
        });


                webView.loadUrl("http://m.miaotu.com/App/creat");
    }
    /**
     * js调用java的接口
     * @author ying
     *
     */
    public final class JSInterface {
        //JavaScript脚本代码可以调用的函数onClick()处理
        @android.webkit.JavascriptInterface
        public void click(String action) {
            showToastMsg("点击了"+action);
        }
        @android.webkit.JavascriptInterface
        public void setTitle(final String title) {
            mHandler.post(new Runnable() {

                public void run() {

                    // Code in here
                    tvTitle.setText(title);

                }

            });
        }
        @android.webkit.JavascriptInterface
        public void showTip(final String text) {
            mHandler.post(new Runnable() {

                public void run() {

                    // Code in here
                    showToastMsg(text);

                }

            });
        }
        @android.webkit.JavascriptInterface
        public String getToken() {
            return readPreference("token");
        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) &&   webView .canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
