package com.miaotu.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.miaotu.R;
import com.miaotu.util.StringUtil;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

public class HomeBannerWebActivity extends BaseActivity implements View.OnClickListener{

    private WebView webView;
    private TextView tvLeft;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_banner_web);

        initView();
        initData();
    }

    private void initView(){
        webView = (WebView) findViewById(R.id.webview);
        tvLeft = (TextView) findViewById(R.id.tv_left);
    }

    private void initData(){
        tvLeft.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_left:
                finish();
                break;
        }
    }

    public final class JSInterface {

        @android.webkit.JavascriptInterface
        public void enterCustomTourDetail(String id){
            //线路详情
            Intent intent = new Intent(HomeBannerWebActivity.this, CustomTourDetailActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        }

        @android.webkit.JavascriptInterface
        public void sharePage(String photourl, String pageurl, String text){
            ShareSDK.initSDK(HomeBannerWebActivity.this);
            OnekeyShare oks = new OnekeyShare();
            oks.setTheme(OnekeyShareTheme.CLASSIC);
            // 关闭sso授权
            oks.disableSSOWhenAuthorize();
            // 分享时Notification的图标和文字
//        oks.setNotification(R.drawable.ic_launcher,
//                getString(R.string.app_name));
            // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
            oks.setTitle(text + "\n " + pageurl);
            // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
            oks.setTitleUrl(pageurl);
            // text是分享文本，所有平台都需要这个字段
            oks.setText(text + "\n" + pageurl);
            // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
            if (!StringUtil.isBlank(photourl)){

                oks.setImageUrl(photourl);
            }
            // url仅在微信（包括好友和朋友圈）中使用
            oks.setUrl(pageurl);
            // comment是我对这条分享的评论，仅在人人网和QQ空间使用
            oks.setComment(text + "\n" + pageurl);
            // site是分享此内容的网站名称，仅在QQ空间使用
            oks.setSite(getString(R.string.app_name));
            // siteUrl是分享此内容的网站地址，仅在QQ空间使用
            oks.setSiteUrl(pageurl);

            // 启动分享GUI
            oks.show(HomeBannerWebActivity.this);
        }
    }

}
