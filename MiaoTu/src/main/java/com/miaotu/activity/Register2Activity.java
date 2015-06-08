package com.miaotu.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.miaotu.R;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.model.RegisterInfo;
import com.miaotu.result.BaseResult;
import com.miaotu.util.StringUtil;
import com.miaotu.util.Util;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;

public class Register2Activity extends BaseActivity implements View.OnClickListener{
    private TextView tvLeft,tvTitle;
    private Button btnNext;
    private RegisterInfo registerInfo;
    private EditText etTel,etPassword,etPasswordRe;
    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        findView();
        bindView();
        init();
    }
    private void findView(){
        tvLeft = (TextView) findViewById(R.id.tv_left);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        btnNext = (Button) findViewById(R.id.btn_next);
        etTel = (EditText) findViewById(R.id.et_tel);
        etPassword = (EditText) findViewById(R.id.et_password);
        etPasswordRe = (EditText) findViewById(R.id.et_password_re);
    }
    private void bindView(){
        tvLeft.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }
    private void init(){
        tvTitle.setText("设置账号和密码（2/3）");
        registerInfo = (RegisterInfo) getIntent().getSerializableExtra("registerInfo");
    }
private boolean validate(){
    if(StringUtil.isEmpty(etTel.getText().toString())){
        showToastMsg("请输入手机号！");
        return false;
    }
    if(!StringUtil.isPhoneNumber(etTel.getText().toString())){
        showToastMsg("请输入正确的手机号！");
        return false;
    }
    if(StringUtil.isEmpty(etPassword.getText().toString())){
        showToastMsg("请输入密码！");
        return false;
    }
    if(etPassword.getText().toString().length()<6){
        showToastMsg("请输入6-8位密码！");
        return false;
    }
    if(StringUtil.isEmpty(etPasswordRe.getText().toString())){
        showToastMsg("请重复输入密码！");
        return false;
    }
    if(!etPasswordRe.getText().toString().equals(etPassword.getText().toString())){
        showToastMsg("两次密码输入不一致，请重新输入！");
        return false;
    }
    return true;
}
    private void showDialog(){
        // 为dialog的listview赋值
        LayoutInflater lay = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = lay.inflate(R.layout.dialog_tel_confirm, null);
        final TextView tvTel = (TextView) v.findViewById(R.id.tv_tel);
        tvTel.setText(etTel.getText().toString());
        final Button btnCancel = (Button) v.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        final Button btnConfirm = (Button)v.findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSms(etTel.getText().toString());
            }
        });
        // 创建dialog
        dialog = new Dialog(this,R.style.Dialog_Fullscreen);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(v);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.height = Util.dip2px(this, 178);
        wl.width = Util.dip2px(this, 240);
        window.setAttributes(wl);
        dialog.setFeatureDrawableAlpha(Window.FEATURE_OPTIONS_PANEL, 0);
        dialog.show();
    }
    private void getSms(final String tel) {
        new BaseHttpAsyncTask<Void, Void, BaseResult>(this, true) {
            @Override
            protected void onCompleteTask(BaseResult result) {
                if(tvLeft==null){
                    return;
                }
                if (result.getCode() == BaseResult.SUCCESS) {
                    registerInfo.setPhone(etTel.getText().toString());
                    registerInfo.setPassword(etPassword.getText().toString());
                    Intent nextIntent = new Intent(Register2Activity.this,Register3Activity.class);
                    nextIntent.putExtra("registerInfo",registerInfo);
                    startActivity(nextIntent);
                } else {
                    if(StringUtil.isEmpty(result.getMsg())){
                        showToastMsg("获取验证码失败！");
                    }else{
                        showToastMsg(result.getMsg());
                    }
                }
            }

            @Override
            protected BaseResult run(Void... params) {
                return HttpRequestUtil.getInstance().getSms(etTel.getText().toString());
            }

        }.execute();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_left:
                finish();
                break;
            case R.id.btn_next:
                if(validate()){
                    showDialog();
                }
                break;
        }
    }
}
