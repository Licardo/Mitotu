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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.miaotu.R;
import com.miaotu.adapter.MaritalStatusAdapter;
import com.miaotu.model.RegisterInfo;
import com.miaotu.util.StringUtil;
import com.miaotu.view.WheelTwoColumnDialog;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;

public class Register1Activity extends BaseActivity implements View.OnClickListener{
private TextView tvLeft,tvTitle;
    private Button btnNext;
    private EditText etName;
    private TextView tvCity,tvAge;
    private RadioGroup rgGender;
    private RadioButton rbMan,rbWoman;
    private RegisterInfo registerInfo;
    private WheelTwoColumnDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);
        findView();
        bindView();
        init();
    }
    private void findView(){
        tvLeft = (TextView) findViewById(R.id.tv_left);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        btnNext = (Button) findViewById(R.id.btn_next);
        etName = (EditText) findViewById(R.id.et_name);
        tvCity = (TextView) findViewById(R.id.tv_city);
        tvAge = (TextView) findViewById(R.id.tv_age);
        rgGender = (RadioGroup) findViewById(R.id.rg_gender);
        rbMan = (RadioButton) findViewById(R.id.rb_man);
        rbWoman = (RadioButton) findViewById(R.id.rb_woman);
    }
    private void bindView(){
        tvLeft.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        tvCity.setOnClickListener(this);
        tvAge.setOnClickListener(this);
    }
    private void init(){
        tvTitle.setText("创建个人资料（1/3）");
        registerInfo = new RegisterInfo();
    }
    private boolean validate(){
        if(StringUtil.isBlank(etName.getText().toString())){
            showToastMsg("请输入您的妙途用户名！");
            return false;
        }
        if(rgGender.getCheckedRadioButtonId()==-1){
            showToastMsg("请选择您的性别！");
            return false;
        }
        if(StringUtil.isEmpty(tvCity.getText().toString())){
            showToastMsg("请选择您的城市！");
            return false;
        }
        if(StringUtil.isEmpty(tvAge.getText().toString())){
            showToastMsg("请选择您的年龄！");
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode==1){
            tvCity.setText(data.getStringExtra("city"));
        }
    }
    // 获取情感状态dialog
    private void getMaritalStatusDialog(final View parent) {
        // 为dialog的listview赋值
        LayoutInflater lay = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = lay.inflate(R.layout.dialog_marital_status_layout, null);
        final WheelView wvMaritalStatus = (WheelView) v
                .findViewById(R.id.wv_marital_status);
        wvMaritalStatus.setVisibleItems(5);
        final MaritalStatusAdapter maritalStatusAdapter = new MaritalStatusAdapter(
                this);
        wvMaritalStatus.setViewAdapter(maritalStatusAdapter);

        wvMaritalStatus.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
            }
        });

//        wvMaritalStatus.addScrollingListener(new OnWheelScrollListener() {
//            @Override
//            public void onScrollingStarted(WheelView wheel) {
//                scrolling = true;
//            }
//
//            @Override
//            public void onScrollingFinished(WheelView wheel) {
//                scrolling = false;
//            }
//        });

        wvMaritalStatus.setCurrentItem(1);
        // 创建dialog
        dialog = new WheelTwoColumnDialog(this, R.style.Dialog_Fullscreen, v);
        dialog.setOnConfirmListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String curMaritalStatus = maritalStatusAdapter
                        .getMaritalStatuses()[wvMaritalStatus.getCurrentItem()]; // 获取当前选中的情感状态
                tvAge.setText(curMaritalStatus);
                dialog.dismiss();
            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_left:
                finish();
                break;
            case R.id.btn_next:
                if(validate()){
                    registerInfo.setNickname(etName.getText().toString());
                    if(rgGender.getCheckedRadioButtonId()==rbMan.getId()){
                        registerInfo.setGender("男");
                    }else if(rgGender.getCheckedRadioButtonId()==rbWoman.getId()){
                        registerInfo.setGender("女");
                    }
                    registerInfo.setCity(tvCity.getText().toString());
                    registerInfo.setAge(tvAge.getText().toString());
                    Intent nextIntent = new Intent(Register1Activity.this,Register2Activity.class);
                    nextIntent.putExtra("registerInfo",registerInfo);
                    startActivity(nextIntent);
                }
                break;
            case R.id.tv_city:
                Intent cityIntent = new Intent(Register1Activity.this,CityListActivity.class);
                startActivityForResult(cityIntent, 1);
                break;
            case R.id.tv_age:
                getMaritalStatusDialog(tvAge);
                break;
        }
    }
}
