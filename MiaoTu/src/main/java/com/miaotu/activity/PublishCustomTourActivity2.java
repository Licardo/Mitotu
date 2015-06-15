package com.miaotu.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
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


public class PublishCustomTourActivity2 extends BaseActivity implements OnClickListener{
    private TextView tvTitle,tvLeft,tvStartDate,tvEndDate;
    private EditText etOriginLocaiton,etDesCity,etOriginCity;
    private TextView tvWantgo,tvOrigin;
    private Button btnNext;
    private PublishCustomForm customForm;
    private WheelTwoColumnDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_publish2);
        findView();
        bindView();
        init();
    }
    private void findView(){
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvLeft = (TextView) findViewById(R.id.tv_left);
        etDesCity = (EditText) findViewById(R.id.et_des_city);
        etOriginCity = (EditText) findViewById(R.id.et_origin_city);
        tvStartDate = (TextView) findViewById(R.id.tv_start_date);
        tvEndDate = (TextView) findViewById(R.id.tv_end_date);
        etOriginLocaiton = (EditText) findViewById(R.id.tv_gather_location);
        btnNext = (Button) findViewById(R.id.btn_next);
        tvWantgo = (TextView) findViewById(R.id.tv_select_wantgo);
        tvOrigin = (TextView) findViewById(R.id.tv_select_origin);
    };
    private void bindView(){
        tvLeft.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        tvOrigin.setOnClickListener(this);
        tvWantgo.setOnClickListener(this);
        tvStartDate.setOnClickListener(this);
        tvEndDate.setOnClickListener(this);
    };
    private void init(){
        tvTitle.setText("定制约游");
        tvStartDate.setText(Calendar.getInstance().get(Calendar.YEAR) + "-" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "-" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        tvEndDate.setText(Calendar.getInstance().get(Calendar.YEAR) + "-" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "-" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        customForm = new PublishCustomForm();
    };
    private boolean validate(){
        if(StringUtil.isEmpty(etDesCity.getText().toString())){
            showToastMsg("请输入目的城市！");
            return false;
        }
        if(StringUtil.isEmpty(etOriginCity.getText().toString())){
            showToastMsg("请输入出发城市！");
            return false;
        }
        return true;
    }
    // 获取生日dialog
    private void getDateDialog(final View parent) {
        // 为dialog的listview赋值
        LayoutInflater lay = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = lay.inflate(R.layout.dialog_birthday_layout, null);
        Calendar calendar = Calendar.getInstance();

        final WheelView wvMonth = (WheelView) v.findViewById(R.id.wv_month);
        final WheelView wvYear = (WheelView) v.findViewById(R.id.wv_year);
        final WheelView wvDay = (WheelView) v.findViewById(R.id.wv_day);

        OnWheelChangedListener listener = new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updateDays(wvYear, wvMonth, wvDay);
            }
        };

        // month
        int curMonth = calendar.get(Calendar.MONTH);
        final String months[] = new String[]{"一月", "二月", "三月", "四月", "五月",
                "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"};
        wvMonth.setViewAdapter(new DateArrayAdapter(this, months, curMonth));
        wvMonth.setCurrentItem(curMonth);
        wvMonth.addChangingListener(listener);

        // year
        int curYear = calendar.get(Calendar.YEAR);
        wvYear.setViewAdapter(new DateNumericAdapter(this, curYear, curYear +10, 0));
        wvYear.setCurrentItem(0);
        wvYear.addChangingListener(listener);
        // day
        updateDays(wvYear, wvMonth, wvDay);
        wvDay.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) - 1);
        // 创建dialog
        dialog = new WheelTwoColumnDialog(this, R.style.Dialog_Fullscreen, v);
        dialog.setOnConfirmListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                int monthIndex = wvMonth.getCurrentItem();
                int dayIndex = wvDay.getCurrentItem();
                int yearIndex = wvYear.getCurrentItem();
                String currentYear = (yearIndex + Calendar.getInstance().get(Calendar.YEAR)) + ""; // 获取出生年份
                String currentMonth = (monthIndex + 1) + ""; // 获取出生月份
                if (currentMonth.length() == 1) {
                    currentMonth = "0" + currentMonth;
                }
                String currentDay = (dayIndex + 1) + ""; // 获取出生日子
                if (currentDay.length() == 1) {
                    currentDay = "0" + currentDay;
                }
                ((TextView)parent).setText(currentYear + "-" + currentMonth + "-"
                        + currentDay);
                dialog.dismiss();
            }
        });
    }
    /**
     * Updates day wheel. Sets max days according to selected month and year
     */
    void updateDays(WheelView year, WheelView month, WheelView day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,
                calendar.get(Calendar.YEAR) + year.getCurrentItem());
        calendar.set(Calendar.MONTH, month.getCurrentItem());

        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        day.setViewAdapter(new DateNumericAdapter(this, 1, maxDays, calendar
                .get(Calendar.DAY_OF_MONTH) - 1));
        int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
        day.setCurrentItem(curDay - 1, true);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==3&&resultCode==1){
            setResult(1);
            finish();
        }
        if(resultCode==1){
            if(requestCode==1){
                etDesCity.setText(data.getStringExtra("city"));
            }
            if(requestCode==2){
                etOriginCity.setText(data.getStringExtra("city"));
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_next:
                if(validate()){
                    customForm.setDesCity(etDesCity.getText().toString());
                    customForm.setOriginCity(etOriginCity.getText().toString());
                    customForm.setOriginLocation(etOriginLocaiton.getText().toString());
                    customForm.setStartDate(tvStartDate.getText().toString());
                    customForm.setEndDate(tvEndDate.getText().toString());
                    Intent intent = new Intent(PublishCustomTourActivity2.this,PublishCustomTourActivity3.class);
                    intent.putExtra("form",customForm);
                    startActivityForResult(intent,3);
                }
                break;
            case R.id.tv_left:
                finish();
                break;
            case R.id.tv_select_wantgo:
                //目的城市
                Intent cityIntent = new Intent(PublishCustomTourActivity2.this,CityListActivity.class);
                startActivityForResult(cityIntent, 1);
                break;
            case R.id.tv_select_origin:
                //出发城市
                Intent city2Intent = new Intent(PublishCustomTourActivity2.this,CityListActivity.class);
                startActivityForResult(city2Intent, 2);
                break;
            case R.id.tv_start_date:
                //出发日期
                getDateDialog(view);
                break;
            case R.id.tv_end_date:
                getDateDialog(view);
                //结束日期
                break;
        }
    }
}
