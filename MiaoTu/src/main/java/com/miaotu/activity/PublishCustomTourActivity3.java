package com.miaotu.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.miaotu.R;
import com.miaotu.adapter.DateArrayAdapter;
import com.miaotu.adapter.DateNumericAdapter;
import com.miaotu.form.PublishCustomForm;
import com.miaotu.util.StringUtil;
import com.miaotu.view.WheelTwoColumnDialog;

import java.util.Calendar;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;


public class PublishCustomTourActivity3 extends BaseActivity implements OnClickListener{
    private TextView tvTitle,tvLeft,tvDel1,tvDel2;
    private EditText etTel,etId;
    private ImageView ivId1,ivId2;
    private Button btnNext;
    private PublishCustomForm customForm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_publish3);
        findView();
        bindView();
        init();
    }
    private void findView(){
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvLeft = (TextView) findViewById(R.id.tv_left);
        tvDel1 = (TextView) findViewById(R.id.tv_del1);
        tvDel2 = (TextView) findViewById(R.id.tv_del2);
        ivId1 = (ImageView) findViewById(R.id.iv_id_1);
        ivId2 = (ImageView) findViewById(R.id.iv_id_2);
        etTel = (EditText) findViewById(R.id.et_tel);
        etId = (EditText) findViewById(R.id.et_id);
        btnNext = (Button) findViewById(R.id.btn_next);
    };
    private void bindView(){
        tvLeft.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        ivId1.setOnClickListener(this);
        ivId2.setOnClickListener(this);
        tvDel1.setOnClickListener(this);
        tvDel2.setOnClickListener(this);

    };
    private void init(){
        tvTitle.setText("定制约游");
        customForm = new PublishCustomForm();
    };
    private boolean validate(){
        if(StringUtil.isEmpty(etTel.getText().toString())){
            showToastMsg("请输入目的城市！");
            return false;
        }
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_next:
                if(validate()){
                    Intent intent = new Intent();
                    intent.putExtra("form",customForm);
                    startActivityForResult(intent,3);
                }
                break;
            case R.id.tv_left:
                finish();
                break;
            case R.id.iv_id_1:
                //身份证正面
                break;
            case R.id.iv_id_2:
                //身份证反面
                break;
            case R.id.tv_del1:
                //身份证反面删除
                break;
            case R.id.tv_del2:
                //身份证正面删除
                break;
        }
    }
}
