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
 * 
 *         设置界面
 * 
 */
public class SettingActivity extends BaseActivity implements OnClickListener {
	private Button btnLeft,btnExit;
	private TextView tvTitle;
	private LinearLayout layoutAccountSafe, layoutMessageRemind,
			layoutBlackList, layoutUseHelp, layoutFeedback, layoutAboutMiao,
			layoutCheckUpdate; // 检查更新

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
		btnLeft = null;
		tvTitle = null;
	}

	private void findView() {
		btnLeft = (Button) findViewById(R.id.btn_left);
		tvTitle = (TextView) findViewById(R.id.tv_title);
//		layoutAccountSafe = (LinearLayout) findViewById(R.id.layout_account_safe);
//		layoutMessageRemind = (LinearLayout) findViewById(R.id.layout_message_remind);
//		layoutBlackList = (LinearLayout) findViewById(R.id.layout_blacklist);
//		layoutUseHelp = (LinearLayout) findViewById(R.id.layout_use_help);
//		layoutFeedback = (LinearLayout) findViewById(R.id.layout_feedback);
//		layoutAboutMiao = (LinearLayout) findViewById(R.id.layout_about_miao);
//		layoutCheckUpdate = (LinearLayout) findViewById(R.id.layout_check_update);
//		btnExit = (Button) findViewById(R.id.btn_exit);
	}

	private void bindView() {
		btnLeft.setVisibility(View.VISIBLE);
		tvTitle.setVisibility(View.VISIBLE);
		btnLeft.setOnClickListener(this);
		layoutAccountSafe.setOnClickListener(this);
		layoutMessageRemind.setOnClickListener(this);
		layoutBlackList.setOnClickListener(this);
		layoutUseHelp.setOnClickListener(this);
		layoutFeedback.setOnClickListener(this);
		layoutAboutMiao.setOnClickListener(this);
		layoutCheckUpdate.setOnClickListener(this);
		btnExit.setOnClickListener(this);
	}

	private void init() {
//		btnLeft.setBackgroundResource(R.drawable.arrow_white_left);
		tvTitle.setText("设置");
		if(!readPreference("login_state").equals("in")) {
			btnExit.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.btn_left:
//			SettingActivity.this.finish();
//			break;
//		case R.id.layout_check_update:
//			// 版本更新
//			UmengUpdateAgent.setUpdateOnlyWifi(false);
//			UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
//				@Override
//				public void onUpdateReturned(int updateStatus,
//						UpdateResponse updateInfo) {
//					switch (updateStatus) {
//					case UpdateStatus.Yes: // has update
//						UmengUpdateAgent.showUpdateDialog(SettingActivity.this,
//								updateInfo);
//						break;
//					case UpdateStatus.No: // has no update
//						Toast.makeText(SettingActivity.this, "没有更新",
//								Toast.LENGTH_SHORT).show();
//						break;
//					case UpdateStatus.NoneWifi: // none wifi
//						// Toast.makeText(SettingActivity.this,
//						// "没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT).show();
//						break;
//					case UpdateStatus.Timeout: // time out
//						Toast.makeText(SettingActivity.this, "超时",
//								Toast.LENGTH_SHORT).show();
//						break;
//					}
//				}
//			});
//			UmengUpdateAgent.forceUpdate(SettingActivity.this);
//			break;
//		case R.id.layout_feedback:
////			Intent feedIntent = new Intent(this, FeedbackActivity.class);
////			this.startActivity(feedIntent);
////            FeedbackAgent agent = new FeedbackAgent(SettingActivity.this);
////            agent.startFeedbackActivity();
//			break;
//		case R.id.layout_message_remind:
////			Intent msgIntent = new Intent(this, MsgRemindActivity.class);
////			this.startActivity(msgIntent);
//			break;
//		case R.id.layout_use_help:
////			Intent helpIntent = new Intent(this, HelpActivity.class);
////			this.startActivity(helpIntent);
//			break;
//		case R.id.layout_about_miao:
////			Intent aboutIntent = new Intent(this, AboutActivity.class);
////			this.startActivity(aboutIntent);
//			break;
//		case R.id.layout_account_safe:
////			if(!readPreference("login_state").equals("in")){
////				Intent loginIntent = new Intent(this,LoginActivity.class);
////				this.startActivity(loginIntent);
////				return;
////			}
////			Intent safeIntent = new Intent(this, AccountSafetyActivity.class);
////			this.startActivity(safeIntent);
////			break;
//		case R.id.layout_blacklist:
////			if(!readPreference("login_state").equals("in")){
////				Intent loginIntent = new Intent(this,LoginActivity.class);
////				this.startActivity(loginIntent);
////				return;
////			}
////			Intent blacklistIntent = new Intent(this, BlackListActivity.class);
////			this.startActivity(blacklistIntent);
////			break;
//		case R.id.btn_exit:
//			writePreference("login_state", "out");
//			writePreference("gender", "");
//			JPushInterface.stopPush(SettingActivity.this);
//			// XmppConnection.getInstance().closeConnection();
//			setResult(1);
//			Intent exitIntent = new Intent(JPushReceiver.ACTION_JPUSH_EXIT_MESSAGE_RECIEVE);
//			exitIntent.putExtra("exit_action","Done");
//			exitIntent.putExtras(new Bundle());
//			SettingActivity.this.sendOrderedBroadcast(exitIntent,null);
//			EMChatManager.getInstance().logout();// 此方法为同步方法
//			SettingActivity.this.finish();
//			break;

//		}

	}
}
