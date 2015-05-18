package com.miaotu.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.miaotu.R;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.model.RegisterInfo;
import com.miaotu.result.BaseResult;
import com.miaotu.util.StringUtil;

public class Register3Activity extends BaseActivity implements View.OnClickListener {
    private TextView tvLeft,tvTitle;
    private Button btnNext;
    private RegisterInfo registerInfo;
    private EditText etSMS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register3);
        findView();
        bindView();
        init();
    }
    private void findView(){
        tvLeft = (TextView) findViewById(R.id.tv_left);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        btnNext = (Button) findViewById(R.id.btn_next);
        etSMS = (EditText) findViewById(R.id.et_sms);
    }
    private void bindView(){
        tvLeft.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }
    private void init(){
        tvTitle.setText("验证手机号（3/3）");
        registerInfo = (RegisterInfo) getIntent().getSerializableExtra("registerInfo");
    }
private boolean validate(){
    if(StringUtil.isEmpty(etSMS.getText().toString())){
        showToastMsg("请输入验证码！");
        return false;
    }
    return true;
}
    /**
     * 注册
     * @param registerInfo
     */
    private void register(final RegisterInfo registerInfo) {
        new BaseHttpAsyncTask<Void, Void, BaseResult>(this, true) {
            @Override
            protected void onCompleteTask(BaseResult result) {
                if(tvLeft==null){
                    return;
                }
                if (result.getCode() == BaseResult.SUCCESS) {
                    showToastMsg("注册成功！");
                    Intent intent = new Intent(Register3Activity.this,MainActivity.class);
                    startActivity(intent);
                } else {
                    if(StringUtil.isEmpty(result.getMsg())){
                        showToastMsg("注册失败！");
                    }else{
                        showToastMsg(result.getMsg());
                    }
                }
            }

            @Override
            protected BaseResult run(Void... params) {
                return HttpRequestUtil.getInstance().register(registerInfo);
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
//                Intent nextIntent = new Intent(Register3Activity.this,Register3Activity.class);
//                startActivity(nextIntent);
                if(validate()){
                    //执行注册
                    registerInfo.setCode(etSMS.getText().toString());
                    register(registerInfo);
                }
                break;
        }
    }
}
