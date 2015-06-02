package com.miaotu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miaotu.R;
import com.miaotu.util.StringUtil;

/**
 * @author zhanglei
 * 
 * 账号与安全界面
 * 
 */
public class AccountSafetyActivity extends BaseActivity implements
		OnClickListener {
	private TextView tvTitle, tvLeft;
	private TextView tvIdentity,tvEmail,tvPhone,tvAccount;
//	private LinearLayout layoutModifyPwd,layoutBindPhone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_safety);
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
		tvIdentity = (TextView) findViewById(R.id.tv_identity);
		tvEmail = (TextView) findViewById(R.id.tv_email);
		tvAccount = (TextView) findViewById(R.id.tv_account);
		tvPhone = (TextView) findViewById(R.id.tv_phone);
//		layoutModifyPwd = (LinearLayout) findViewById(R.id.layout_modify_pwd);
//		layoutBindPhone = (LinearLayout) findViewById(R.id.layout_bind_phone);
	}

	private void bindView() {
		tvLeft.setVisibility(View.VISIBLE);
		tvTitle.setVisibility(View.VISIBLE);
		tvLeft.setOnClickListener(this);
//		layoutModifyPwd.setOnClickListener(this);
//		layoutBindPhone.setOnClickListener(this);
	}

	private void init() {
//		btnLeft.setBackgroundResource(R.drawable.arrow_white_left);
		tvTitle.setText("账号与安全");
		if("1".equals(readPreference("status"))){
			tvIdentity.setText("已认证");
		}
		if(!StringUtil.isBlank(readPreference("email"))){
			tvEmail.setText(readPreference("email"));
		}
		if(!StringUtil.isBlank(readPreference("phone"))){
			tvPhone.setText(readPreference("phone"));
			tvAccount.setText(readPreference("phone"));
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_left:
			AccountSafetyActivity.this.finish();
			break;
//		case R.id.layout_modify_pwd:
//			Intent intent1 = new Intent(this,ModifyPwdActivity.class);
//			this.startActivity(intent1);
//			break;
//		case R.id.layout_bind_phone:
//			Intent intent2 = new Intent(this,ModifyBindPhoneActivity.class);
//			this.startActivity(intent2);
//			break;
		}
	}
	
}
