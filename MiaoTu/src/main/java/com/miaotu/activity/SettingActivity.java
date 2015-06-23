package com.miaotu.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;

import com.easemob.chat.EMChatManager;
import com.miaotu.R;
import com.miaotu.receiver.JPushReceiver;

/**
 * @author zhanglei
 *         <p/>
 *         设置界面
 */
public class SettingActivity extends BaseActivity implements OnClickListener {
    private Button btnExit;
    private TextView tvLeft, tvTitle;
    private LinearLayout layout_account_safe, layout_use_msg,
            layout_call, layout_introduce, layout_feedback,
            layout_blacklist,ll_comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        findView();
        bindView();
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tvLeft = null;
        tvTitle = null;
    }

    private void findView() {
        tvLeft = (TextView) findViewById(R.id.tv_left);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        layout_account_safe = (LinearLayout) findViewById(R.id.layout_account_safe);
        layout_use_msg = (LinearLayout) findViewById(R.id.layout_use_msg);
        layout_call = (LinearLayout) findViewById(R.id.layout_call);
        layout_introduce = (LinearLayout) findViewById(R.id.layout_introduce);
        layout_feedback = (LinearLayout) findViewById(R.id.layout_feedback);
        layout_blacklist = (LinearLayout) findViewById(R.id.layout_blacklist);
        ll_comment = (LinearLayout) findViewById(R.id.ll_comment);
//		layoutCheckUpdate = (LinearLayout) findViewById(R.id.layout_check_update);
        btnExit = (Button) findViewById(R.id.btn_exit);
    }

    private void bindView() {
        tvLeft.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvLeft.setOnClickListener(this);
        layout_account_safe.setOnClickListener(this);
        layout_use_msg.setOnClickListener(this);
        layout_call.setOnClickListener(this);
        layout_introduce.setOnClickListener(this);
        layout_feedback.setOnClickListener(this);
        layout_blacklist.setOnClickListener(this);
		ll_comment.setOnClickListener(this);
        btnExit.setOnClickListener(this);
    }

    private void init() {
//		btnLeft.setBackgroundResource(R.drawable.arrow_white_left);
        tvTitle.setText("设置");
        if (!readPreference("login_status").equals("in")) {
            btnExit.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left:
                SettingActivity.this.finish();
                break;
            case R.id.layout_feedback:        //帮助与反馈
                Intent feedIntent = new Intent(this, FeedBackActivity.class);
                this.startActivity(feedIntent);
                break;
            case R.id.layout_use_msg:        //新消息通知提醒
                Intent msgIntent = new Intent(this, BBSTopicRemindActivity.class);
                this.startActivity(msgIntent);
                break;
            case R.id.layout_call:        //联系我们
                Intent helpIntent = new Intent(this, AboutActivity.class);
                this.startActivity(helpIntent);
                break;
            case R.id.layout_introduce:        //妙途3.0介绍
                Intent aboutIntent = new Intent(this, IntroduceActivity.class);
                this.startActivity(aboutIntent);
                break;
            case R.id.layout_account_safe:      //账号与安全
                if (!readPreference("login_status").equals("in")) {
                    Intent loginIntent = new Intent(this, LoginActivity.class);
                    this.startActivity(loginIntent);
                    return;
                }
                Intent safeIntent = new Intent(this, AccountSafetyActivity.class);
                this.startActivity(safeIntent);
                break;
            case R.id.layout_blacklist:
			if(!readPreference("login_status").equals("in")){
				Intent loginIntent = new Intent(this,LoginActivity.class);
				this.startActivity(loginIntent);
				return;
			}
			Intent blacklistIntent = new Intent(this, BlackListActivity.class);
			this.startActivity(blacklistIntent);
			break;
            case R.id.btn_exit:
                writePreference("login_status", "out");
                writePreference("gender", "");
                writePreference("tour_join_count","0");
                writePreference("tour_like_name","");
                writePreference("tour_join_name","");
                writePreference("like_date","");
                writePreference("tour_like_date","");
                writePreference("tour_join_date","");
                writePreference("im_content","");
                writePreference("im_date","");
                writePreference("sys_date","");
                writePreference("sys_name","");
                writePreference("sys_count","");
                writePreference("tour_comment_date","");
                writePreference("tour_comment_name","");
                writePreference("tour_comment_count","0");
                writePreference("fanscount","0");
                writePreference("followcount","0");
                writePreference("luckmoney", "0");
                writePreference("headphoto","");
                writePreference("name","");
                writePreference("age","0");
                writePreference("address","");
                writePreference("emotion","");
                writePreference("wantgo","");
                writePreference("tags","");
                JPushInterface.stopPush(SettingActivity.this);
                // XmppConnection.getInstance().closeConnection();
                setResult(1);
                Intent exitIntent = new Intent(JPushReceiver.ACTION_JPUSH_EXIT_MESSAGE_RECIEVE);
                exitIntent.putExtra("exit_action", "Done");
                exitIntent.putExtras(new Bundle());
                SettingActivity.this.sendOrderedBroadcast(exitIntent, null);

                ShareSDK.initSDK(getApplicationContext());
                Platform qzone = ShareSDK.getPlatform(this, QZone.NAME);
                if(qzone.isValid()) {
                        qzone.removeAccount();
                    }
                Platform wechat = ShareSDK.getPlatform(this, Wechat.NAME);
                if(wechat.isValid()) {
                    wechat.removeAccount();
                    }
                Platform weibo = ShareSDK.getPlatform(this, SinaWeibo.NAME);
                if(weibo.isValid()) {
                    weibo.removeAccount();
                    }
                EMChatManager.getInstance().logout();// 此方法为同步方法
                MainActivity.getInstance().finish();
                SettingActivity.this.finish();
                Intent loginIntent = new Intent(SettingActivity.this,ChooseLoginActivity.class);
                startActivity(loginIntent);
                break;
            case R.id.ll_comment:
                Uri uri = Uri.parse("market://details?id="+getPackageName());
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }

    }
}
