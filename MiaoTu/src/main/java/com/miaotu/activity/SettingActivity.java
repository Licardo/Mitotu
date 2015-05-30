package com.miaotu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;

import com.easemob.chat.EMChatManager;
import com.miaotu.R;
import com.miaotu.receiver.JPushReceiver;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

/**
 * @author zhanglei
 *         <p/>
 *         设置界面
 */
public class SettingActivity extends BaseActivity implements OnClickListener {
    private Button btnExit;
    private TextView tvLeft, tvTitle;
    /*private LinearLayout layout_account_safe, layoutMessageRemind,
            layoutBlackList, layoutUseHelp, layoutFeedback, layoutAboutMiao,
            layoutCheckUpdate; // 检查更新*/
    private LinearLayout layout_account_safe, layout_use_msg,
            layout_call, layout_introduce, layout_feedback,layout_blacklist;

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
//		layoutCheckUpdate.setOnClickListener(this);
        btnExit.setOnClickListener(this);
    }

    private void init() {
//		btnLeft.setBackgroundResource(R.drawable.arrow_white_left);
        tvTitle.setText("设置");
        if (!readPreference("login_state").equals("in")) {
            btnExit.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                SettingActivity.this.finish();
                break;
        /*case R.id.layout_check_update:
			// 版本更新
			UmengUpdateAgent.setUpdateOnlyWifi(false);
			UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
				@Override
				public void onUpdateReturned(int updateStatus,
						UpdateResponse updateInfo) {
					switch (updateStatus) {
					case UpdateStatus.Yes: // has update
						UmengUpdateAgent.showUpdateDialog(SettingActivity.this,
								updateInfo);
						break;
					case UpdateStatus.No: // has no update
						Toast.makeText(SettingActivity.this, "没有更新",
								Toast.LENGTH_SHORT).show();
						break;
					case UpdateStatus.NoneWifi: // none wifi
						// Toast.makeText(SettingActivity.this,
						// "没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT).show();
						break;
					case UpdateStatus.Timeout: // time out
						Toast.makeText(SettingActivity.this, "超时",
								Toast.LENGTH_SHORT).show();
						break;
					}
				}
			});
			UmengUpdateAgent.forceUpdate(SettingActivity.this);
			break;*/
            case R.id.layout_feedback:        //帮助与反馈
                Intent feedIntent = new Intent(this, FeedBackActivity.class);
                this.startActivity(feedIntent);
                break;
            case R.id.layout_use_msg:        //新消息通知提醒
                Intent msgIntent = new Intent(this, NewMessageRemind.class);
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
            case R.id.layout_account_safe:
                if (!readPreference("login_state").equals("in")) {
                    Intent loginIntent = new Intent(this, LoginActivity.class);
                    this.startActivity(loginIntent);
                    return;
                }
                Intent safeIntent = new Intent(this, AccountSafetyActivity.class);
                this.startActivity(safeIntent);
                break;
            case R.id.layout_blacklist:
			if(!readPreference("login_state").equals("in")){
				Intent loginIntent = new Intent(this,LoginActivity.class);
				this.startActivity(loginIntent);
				return;
			}
			Intent blacklistIntent = new Intent(this, BlackListActivity.class);
			this.startActivity(blacklistIntent);
			break;
            case R.id.btn_exit:
                writePreference("login_state", "out");
                writePreference("gender", "");
                JPushInterface.stopPush(SettingActivity.this);
                // XmppConnection.getInstance().closeConnection();
                setResult(1);
                Intent exitIntent = new Intent(JPushReceiver.ACTION_JPUSH_EXIT_MESSAGE_RECIEVE);
                exitIntent.putExtra("exit_action", "Done");
                exitIntent.putExtras(new Bundle());
                SettingActivity.this.sendOrderedBroadcast(exitIntent, null);
                EMChatManager.getInstance().logout();// 此方法为同步方法
                MainActivity.getInstance().finish();
                SettingActivity.this.finish();
                Intent loginIntent = new Intent(SettingActivity.this,ChooseLoginActivity.class);
                startActivity(loginIntent);
                break;
        }

    }
}
