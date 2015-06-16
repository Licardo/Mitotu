package com.miaotu.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.miaotu.R;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.form.PublishCustomForm;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.result.BaseResult;
import com.miaotu.result.PhotoUploadResult;
import com.miaotu.util.LogUtil;
import com.miaotu.util.StringUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class PublishCustomTourActivity4 extends BaseActivity implements OnClickListener{
    private TextView tvTitle,tvLeft;
    private Button btnNext;
    private PublishCustomForm customForm;
    private RadioGroup rgDaoyou,rgDriver,rgZiyouxing,rgAboard,rgActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_publish4);
        findView();
        bindView();
        init();
    }
    private void findView(){
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvLeft = (TextView) findViewById(R.id.tv_left);
        btnNext = (Button) findViewById(R.id.btn_next);
        rgAboard = (RadioGroup) findViewById(R.id.rg_aboard);
        rgActivity = (RadioGroup) findViewById(R.id.rg_activity);
        rgDaoyou = (RadioGroup) findViewById(R.id.rg_daoyou);
        rgDriver = (RadioGroup) findViewById(R.id.rg_driver);
        rgZiyouxing = (RadioGroup) findViewById(R.id.rg_ziyouxing);
    };
    private void bindView(){
        tvLeft.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    };
    private void init(){
        tvTitle.setText("定制约游");
    };
    private void publish(){
        new BaseHttpAsyncTask<Void, Void, BaseResult>(PublishCustomTourActivity4.this, true) {
            @Override
            protected void onCompleteTask(BaseResult result) {
                if(tvLeft==null){
                    return;
                }
                if (result.getCode() == BaseResult.SUCCESS) {
                    setResult(1);
                    Intent intent = new Intent(PublishCustomTourActivity4.this,PublishCustomTourActivity5.class);
                    startActivity(intent);
                    finish();
                } else {
                    if (StringUtil.isEmpty(result.getMsg())) {
                        showToastMsg("发布失败！");
                    } else {
                        showToastMsg(result.getMsg());
                    }
                }
            }

            @Override
            protected BaseResult run(Void... params) {
                return HttpRequestUtil.getInstance().publishCustom(customForm);
            }
        }.execute();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_next:
                customForm = (PublishCustomForm) getIntent().getSerializableExtra("form");
                if(rgZiyouxing.getCheckedRadioButtonId()==R.id.rb_ziyouxing1){
                    customForm.setIsFreepath("1");
                }else{
                    customForm.setIsFreepath("0");
                }
                if(rgDriver.getCheckedRadioButtonId()==R.id.rb_driver1){
                    customForm.setIsDriver("1");
                }else{
                    customForm.setIsDriver("0");
                }
                if(rgDaoyou.getCheckedRadioButtonId()==R.id.rb_daoyou1){
                    customForm.setIsTourist("1");
                }else{
                    customForm.setIsTourist("0");
                }
                if(rgActivity.getCheckedRadioButtonId()==R.id.rb_activity1){
                    customForm.setIsManyTourist("1");
                }else{
                    customForm.setIsManyTourist("0");
                }
                if(rgAboard.getCheckedRadioButtonId()==R.id.rb_aboard1){
                    customForm.setIsOutbound("1");
                }else{
                    customForm.setIsOutbound("0");
                }
                customForm.setToken(readPreference("token"));
                publish();
                break;
            case R.id.tv_left:
                finish();
                break;
        }
    }
}
