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

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class HomeShareActivity extends BaseActivity implements View.OnClickListener{

    private ImageView ivWeChat,ivFriends;
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
    }

    private void bindView(){
        ivWeChat.setOnClickListener(this);
        ivFriends.setOnClickListener(this);
    }

    private void initData(){
        url = getIntent().getStringExtra("url");
    }

    @Override
    public void onClick(View view) {
        ShareSDK.initSDK(this);
        switch (view.getId()){
            case R.id.iv_friend:
                WechatMoments.ShareParams wmsp = new WechatMoments.ShareParams();
                wmsp.setShareType(Platform.SHARE_IMAGE);
                wmsp.setTitle(url);
                wmsp.setImageUrl(url);
                break;
            case R.id.iv_wechat:
                Wechat.ShareParams wcsp = new Wechat.ShareParams();
                wcsp.setShareType(Platform.SHARE_IMAGE);
                wcsp.setTitle(url);
                wcsp.setImageUrl(url);
                break;
        }
        finish();
    }
}
