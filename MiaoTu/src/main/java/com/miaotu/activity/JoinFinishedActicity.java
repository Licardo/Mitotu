package com.miaotu.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.fasterxml.jackson.databind.deser.Deserializers;
import com.miaotu.R;
import com.miaotu.util.MD5;

import javax.xml.transform.Templates;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

public class JoinFinishedActicity extends BaseActivity {

    private TextView tvLeft,tvTilte;
    private WebView webView;
    private String uid,nickname,hearurl,gid,gname,remark;
    Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_finished_acticity);

        initView();
        initData();
    }

    private void initView(){
        tvLeft = (TextView) findViewById(R.id.tv_left);
        tvTilte = (TextView) findViewById(R.id.tv_title);
        webView = (WebView) findViewById(R.id.webview);
    }

    private void initData(){
        uid = getIntent().getStringExtra("uid");
        nickname = getIntent().getStringExtra("nickname");
        hearurl = getIntent().getStringExtra("headurl");
        gid = getIntent().getStringExtra("gid");
        gname = getIntent().getStringExtra("groupname");
        remark = getIntent().getStringExtra("remark");
        WebSettings wSet = webView.getSettings();
        wSet.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JSInterface(), "native");

        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) { //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl("http://m.miaotu.com/App/joinRes/?uid="+uid+"&nickname="+nickname+"&headurl="+hearurl+"&gid="+gid+"&groupname="+gname+"&remark="+remark);
    }

    /**
     * js调用java的接口
     * @author ying
     *
     */
    public final class JSInterface {
        @android.webkit.JavascriptInterface
        public void chat(String uid,String name,String headphoto) {
            //私聊
            if(uid.equals(readPreference("uid"))){
                showToastMsg("不能和自己聊天！");
                return ;
            }
            Intent chatIntent = new Intent(JoinFinishedActicity.this, ChatsActivity.class);
            chatIntent.putExtra("chatType", ChatsActivity.CHATTYPE_SINGLE);
            chatIntent.putExtra("id", MD5.md5(uid));
            chatIntent.putExtra("uid", uid);
            chatIntent.putExtra("name", name);
            chatIntent.putExtra("headphoto", headphoto);
            startActivity(chatIntent);
        }

        @android.webkit.JavascriptInterface
        public void groupChat(String groupId,String groupName) {
            //群聊

            Intent groupChatIntent = new Intent(JoinFinishedActicity.this, ChatsActivity.class);
            groupChatIntent.putExtra("groupImId", groupId);
            groupChatIntent.putExtra("groupName", groupName);
            groupChatIntent.putExtra("chatType", 2);
            startActivity(groupChatIntent);
        }

        @android.webkit.JavascriptInterface
        public void share(String remark) {
            ShareSDK.initSDK(JoinFinishedActicity.this);
            OnekeyShare oks = new OnekeyShare();
            oks.setTheme(OnekeyShareTheme.CLASSIC);
            // 关闭sso授权
            oks.disableSSOWhenAuthorize();
            // 分享时Notification的图标和文字
//        oks.setNotification(R.drawable.ic_launcher,
//                getString(R.string.app_name));
            // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
            oks.setTitle(remark + "\n http://m.miaotu.com/ShareLine/custom/?aid=" + getIntent().getStringExtra("id"));
            // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
            oks.setTitleUrl("http://m.miaotu.com/ShareLine/custom/?aid=" + getIntent().getStringExtra("id"));
            // text是分享文本，所有平台都需要这个字段
            oks.setText(remark + "\n http://m.miaotu.com/ShareLine/custom/?aid=" + getIntent().getStringExtra("id"));
            // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数

            oks.setImageUrl(getIntent().getStringExtra("picurl")
                    + "200x200");
            // url仅在微信（包括好友和朋友圈）中使用
            oks.setUrl("http://m.miaotu.com/ShareLine/custom/?aid=" + getIntent().getStringExtra("id"));
            // comment是我对这条分享的评论，仅在人人网和QQ空间使用
            oks.setComment(remark + "\n http://m.miaotu.com/ShareLine/custom/?aid=" + getIntent().getStringExtra("id"));
            // site是分享此内容的网站名称，仅在QQ空间使用
            oks.setSite(getString(R.string.app_name));
            // siteUrl是分享此内容的网站地址，仅在QQ空间使用
            oks.setSiteUrl("http://m.miaotu.com/ShareLine/custom/?aid=" +getIntent().getStringExtra("id"));

            // 启动分享GUI
            oks.show(JoinFinishedActicity.this);
        }
    }
}
