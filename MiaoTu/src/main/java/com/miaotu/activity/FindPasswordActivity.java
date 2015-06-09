package com.miaotu.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.miaotu.R;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.model.RegisterInfo;
import com.miaotu.result.BaseResult;
import com.miaotu.result.LoginResult;
import com.miaotu.util.LogUtil;
import com.miaotu.util.MD5;
import com.miaotu.util.StringUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FindPasswordActivity extends BaseActivity implements View.OnClickListener{
private TextView tvLeft,tvTitle,tvSendValidate;
    private EditText etTel,etPassword,etValidate;
    private Button btnNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
        findView();
        bindView();
        init();
    }
private void findView (){
    tvLeft = (TextView) findViewById(R.id.tv_left);
    tvTitle = (TextView) findViewById(R.id.tv_title);
    tvSendValidate = (TextView) findViewById(R.id.tv_send_validate);
    etTel = (EditText) findViewById(R.id.et_tel);
    etPassword = (EditText) findViewById(R.id.et_password);
    etValidate = (EditText) findViewById(R.id.et_validate);
    btnNext = (Button) findViewById(R.id.btn_login);
}
private void bindView (){
    tvSendValidate.setOnClickListener(this);
    tvLeft.setOnClickListener(this);
    btnNext.setOnClickListener(this);
}
private void init (){
    tvTitle.setText("忘记密码");
}
    private void getSms(final String tel) {
        new BaseHttpAsyncTask<Void, Void, BaseResult>(this, true) {
            @Override
            protected void onCompleteTask(BaseResult result) {
                if(tvLeft==null){
                    return;
                }
                if (result.getCode() == BaseResult.SUCCESS) {
                    showToastMsg("验证码已经发送");
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
                return HttpRequestUtil.getInstance().getFindSms(etTel.getText().toString());
            }

        }.execute();
    }
    //找回密码
    private void findPassword(final RegisterInfo registerInfo) {
        new BaseHttpAsyncTask<Void, Void, BaseResult>(this, true) {
            @Override
            protected void onCompleteTask(BaseResult result) {
                if(tvLeft==null){
                    return;
                }
                if (result.getCode() == BaseResult.SUCCESS) {
                    login(registerInfo);
                } else {
                    if(StringUtil.isEmpty(result.getMsg())){
                        showToastMsg("密码找回失败！");
                    }else{
                        showToastMsg(result.getMsg());
                    }
                }
            }

            @Override
            protected BaseResult run(Void... params) {
                return HttpRequestUtil.getInstance().findPassword(registerInfo);
            }

        }.execute();
    }
    //登陆
    private void login(final RegisterInfo registerInfo) {
        new BaseHttpAsyncTask<Void, Void, LoginResult>(this, true) {
            @Override
            protected void onCompleteTask(LoginResult result) {
                if(tvLeft==null){
                    return;
                }
                if (result.getCode() == BaseResult.SUCCESS) {
                    showToastMsg("密码重置成功！");
                    writePreference("uid", result.getLogin().getUid());
                    writePreference("id", result.getLogin().getId());
                    writePreference("token",result.getLogin().getToken());
                    writePreference("name",result.getLogin().getName());
                    writePreference("age",result.getLogin().getAge());
                    writePreference("gender",result.getLogin().getGender());
                    writePreference("headphoto",result.getLogin().getHeadPhoto());
                    writePreference("job",result.getLogin().getJob());
                    writePreference("fanscount", result.getLogin().getFanscount());
                    writePreference("followcount", result.getLogin().getFollowcount());
                    writePreference("wxid", result.getLogin().getWxunionid());
                    writePreference("qqid", result.getLogin().getQqopenid());
                    writePreference("sinaid", result.getLogin().getSinauid());
                    writePreference("luckmoney", result.getLogin().getLuckymoney());
                    writePreference("status", result.getLogin().getStatus());   //1身份证验证 0未验证
                    writePreference("email", result.getLogin().getEmail());
                    writePreference("phone", result.getLogin().getPhone());
                    writePreference("login_state","in");

                    EMChatManager.getInstance().login(MD5.md5(readPreference("uid")), readPreference("token"),
                            new EMCallBack() {//回调
                                @Override
                                public void onSuccess() {
//                                    runOnUiThread(new Runnable() {
//                                        public void run() {
//
//                                        }
//                                    });
                                    new LoadIMInfoThread().start();
                                }

                                @Override
                                public void onProgress(int progress, String status) {

                                }

                                @Override
                                public void onError(int code, String message) {
                                    Log.d("main", "登录聊天服务器失败！");
                                }

                            });
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
                    String sysDatetime = fmt.format(calendar.getTime());
//                    if(readPreference("everyday").equals(sysDatetime)){
//                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
//                        startActivity(intent);
//                    }else{
                    Intent intent = new Intent(FindPasswordActivity.this,EveryDayPicActivity.class);
                    startActivity(intent);
//                    }
                    setResult(1);
                    finish();
                } else {
                    if(StringUtil.isEmpty(result.getMsg())){
                        showToastMsg("登录失败！");
                    }else{
                        showToastMsg(result.getMsg());
                    }
                }
            }

            @Override
            protected LoginResult run(Void... params) {
                return HttpRequestUtil.getInstance().login(registerInfo);
            }

        }.execute();
    }
    class LoadIMInfoThread extends Thread{
        @Override
        public void run() {
            super.run();
            LogUtil.d("main", "登录聊天服务器成功！");
            long start = System.currentTimeMillis();
            EMChatManager.getInstance().loadAllConversations();
            try {
                EMGroupManager.getInstance().getGroupsFromServer();
            } catch (EaseMobException e) {
                e.printStackTrace();
            }
            EMGroupManager.getInstance().loadAllGroups();

//            long costTime = System.currentTimeMillis() - start;
//            //等待sleeptime时长
//            if (sleepTime - costTime > 0) {
//                try {
//                    Thread.sleep(sleepTime - costTime);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_left:
                finish();
                break;
            case R.id.tv_send_validate:
                if(StringUtil.isEmpty(etTel.getText().toString())){
                    showToastMsg("请输入手机号！");
                    break;
                }
                if(!StringUtil.isPhoneNumber(etTel.getText().toString())){
                    showToastMsg("请输入合法的手机号！");
                    break;
                }
                if(StringUtil.isEmpty(etPassword.getText().toString())){
                    showToastMsg("请输入新密码");
                    break;
                }
                if(etPassword.getText().toString().length()<6){
                    showToastMsg("请输入至少6位密码");
                    break;
                }
                getSms(etTel.getText().toString());
                break;
            case R.id.btn_login:
                if(StringUtil.isEmpty(etTel.getText().toString())){
                    showToastMsg("请输入手机号！");
                    break;
                }
                if(!StringUtil.isPhoneNumber(etTel.getText().toString())){
                    showToastMsg("请输入合法的手机号！");
                    break;
                }
                if(StringUtil.isEmpty(etPassword.getText().toString())){
                    showToastMsg("请输入新密码");
                    break;
                }
                if(etPassword.getText().toString().length()<6){
                    showToastMsg("请输入至少6位密码");
                    break;
                }
                if(StringUtil.isEmpty(etValidate.getText().toString())){
                    showToastMsg("请输入验证码");
                    break;
                }
                RegisterInfo registerInfo = new RegisterInfo();
                registerInfo.setPhone(etTel.getText().toString());
                registerInfo.setPassword(etPassword.getText().toString());
                registerInfo.setCode(etValidate.getText().toString());
                findPassword(registerInfo);
                break;
        }

    }

}
