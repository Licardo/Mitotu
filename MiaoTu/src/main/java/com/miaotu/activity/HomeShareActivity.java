package com.miaotu.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.miaotu.R;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class HomeShareActivity extends BaseActivity implements View.OnClickListener{

    private ImageView ivWeChat,ivFriends,ivClose;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home_share);

        initView();
        bindView();
        initData();
    }

    private void initView(){
        ivFriends = (ImageView) findViewById(R.id.iv_friend);
        ivWeChat = (ImageView) findViewById(R.id.iv_wechat);
        ivClose = (ImageView) findViewById(R.id.iv_close);
    }

    private void bindView(){
        ivWeChat.setOnClickListener(this);
        ivFriends.setOnClickListener(this);
        ivClose.setOnClickListener(this);
    }

    private void initData(){
        url = getIntent().getStringExtra("url");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_friend:
                ShareSDK.initSDK(this);
                WechatMoments.ShareParams wmsp = new WechatMoments.ShareParams();
                wmsp.setShareType(Platform.SHARE_IMAGE);
                wmsp.setTitle(url);
                wmsp.setImageUrl(url);
                Platform wechatmoments = ShareSDK.getPlatform(WechatMoments.NAME);
                wechatmoments.setPlatformActionListener(new PlatFormListener());
                wechatmoments.share(wmsp);
                finish();
                break;
            case R.id.iv_wechat:
                ShareSDK.initSDK(this);
                Wechat.ShareParams wcsp = new Wechat.ShareParams();
                wcsp.setShareType(Platform.SHARE_IMAGE);
                wcsp.setTitle(url);
                wcsp.setImageUrl(url);
                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                wechat.setPlatformActionListener(new PlatFormListener());
                wechat.share(wcsp);
                finish();
                break;
            case R.id.iv_close:
                finish();
                break;
        }
    }
    class PlatFormListener implements PlatformActionListener {

        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            showToastMsg("分享成功");
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            showToastMsg("分享失败");
        }

        @Override
        public void onCancel(Platform platform, int i) {
            showToastMsg("取消分享");
        }
    }
}
