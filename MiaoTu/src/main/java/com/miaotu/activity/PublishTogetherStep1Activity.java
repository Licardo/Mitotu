package com.miaotu.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.miaotu.R;
import com.miaotu.adapter.DateArrayAdapter;
import com.miaotu.adapter.DateNumericAdapter;
import com.miaotu.util.StringUtil;
import com.miaotu.util.Util;
import com.miaotu.view.DraggableGridView;
import com.miaotu.view.FlowLayout;
import com.miaotu.view.FlowRadioGroup;
import com.miaotu.view.OnRearrangeListener;
import com.miaotu.view.WheelTwoColumnDialog;
import com.photoselector.model.PhotoModel;
import com.photoselector.ui.PhotoPreviewActivity;
import com.photoselector.ui.PhotoSelectorActivity;
import com.photoselector.util.CommonUtils;

import java.util.ArrayList;
import java.util.Calendar;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;

public class PublishTogetherStep1Activity extends BaseActivity implements OnClickListener{
    DraggableGridView dgv;
    private ArrayList<PhotoModel> photoList;
    private TextView tvDesCity,tvOriginCity,tvStartDate,tvEndDate;
    private RadioGroup rgCount,rgFee;
    private FlowRadioGroup rgRequirement;
    private EditText etTag,tvGatherLocation;
    private FlowLayout layoutTags;
    private LinearLayout layoutNext;
    private Button btnTagAdd;
    private WheelTwoColumnDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_together_step1);
        findView();
        bindView();
        init();
    }
    private void findView(){
        dgv = ((DraggableGridView)findViewById(R.id.dgv));
        tvDesCity = (TextView) findViewById(R.id.tv_des_city);
        tvOriginCity = (TextView) findViewById(R.id.tv_origin_city);
        tvGatherLocation = (EditText) findViewById(R.id.tv_gather_location);
        tvStartDate = (TextView) findViewById(R.id.tv_start_date);
        tvEndDate = (TextView) findViewById(R.id.tv_end_date);
        rgCount = (RadioGroup) findViewById(R.id.rg_count);
        rgFee = (RadioGroup) findViewById(R.id.rg_fee);
        rgRequirement = (FlowRadioGroup) findViewById(R.id.rg_requirement);
        etTag = (EditText) findViewById(R.id.et_tag);
        layoutTags = (FlowLayout) findViewById(R.id.layout_tags);
        layoutNext = (LinearLayout) findViewById(R.id.layout_next);
        btnTagAdd = (Button) findViewById(R.id.btn_tag_add);
    }
    private void bindView(){
        tvDesCity.setOnClickListener(this);
        tvOriginCity.setOnClickListener(this);
        tvStartDate.setOnClickListener(this);
        tvEndDate.setOnClickListener(this);
        layoutNext.setOnClickListener(this);
        btnTagAdd.setOnClickListener(this);
        dgv.setOnRearrangeListener(new OnRearrangeListener() {
            public void onRearrange(int oldIndex, int newIndex) {
                photoList.add(newIndex,photoList.remove(oldIndex));
//                String word = poem.remove(oldIndex);
//                if (oldIndex < newIndex)
//                    poem.add(newIndex, word);
//                else
//                    poem.add(newIndex, word);
            }
        });
        dgv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (arg2 == dgv.getChildCount() - 1) {
//                    showToastMsg("添加照片");
                    //添加照片
                    Intent intent = new Intent(PublishTogetherStep1Activity.this, PhotoSelectorActivity.class);
                    intent.putExtra("key_max",9-photoList.size());
                    startActivityForResult(intent, 1);
                } else {
                    //点击照片
                    /** 预览照片 */
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("photos", photoList);
                    bundle.putSerializable("position", arg2);
                    CommonUtils.launchActivity(PublishTogetherStep1Activity.this, PhotoPreviewActivity.class, bundle);
                }
//                dgv.removeViewAt(arg2);
//                poem.remove(arg2);
            }
        });
    }
    private void init(){
        ImageView imageView = new ImageView(this);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(Util.dip2px(this, 70),Util.dip2px(this,70));
        imageView.setLayoutParams(params);
        imageView.setBackgroundResource(R.drawable.icon_pic_add);

        dgv.addView(imageView);
        photoList = new ArrayList<>();

        tvStartDate.setText(Calendar.getInstance().get(Calendar.YEAR)+"-"+(Calendar.getInstance().get(Calendar.MONTH)+1)+"-"+Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        tvEndDate.setText(Calendar.getInstance().get(Calendar.YEAR)+"-"+(Calendar.getInstance().get(Calendar.MONTH)+1)+"-"+Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
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
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1){
            if(requestCode==1){
                tvDesCity.setText(data.getStringExtra("city"));
            }
            if(requestCode==2){
                tvOriginCity.setText(data.getStringExtra("city"));
            }
        }

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_des_city:
                //目的城市
                Intent cityIntent = new Intent(PublishTogetherStep1Activity.this,CityListActivity.class);
                startActivityForResult(cityIntent, 1);
                break;
            case R.id.tv_origin_city:
                //出发城市
                Intent city2Intent = new Intent(PublishTogetherStep1Activity.this,CityListActivity.class);
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
            case R.id.btn_tag_add:
                //添加标签
                if(StringUtil.isBlank(etTag.getText().toString())){
                    showToastMsg("请输入标签！");
                    return;
                }
                if(layoutTags.getChildCount()<7){
                    final View viewTemp = getLayoutInflater().inflate(R.layout.item_tag,null);
                    TextView tvTag = (TextView) viewTemp.findViewById(R.id.tv_tag);
                    ImageView ivTag = (ImageView) viewTemp.findViewById(R.id.iv_tag);
                    ivTag.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            layoutTags.removeView(viewTemp);
                        }
                    });
                    tvTag.setText(etTag.getText().toString());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
//                    params.rightMargin = Util.dip2px(PublishTogetherStep1Activity.this,10f);
                    params.setMargins(0,0,Util.dip2px(PublishTogetherStep1Activity.this,10f),0);
                    viewTemp.setLayoutParams(params);
                    layoutTags.addView(viewTemp);
                }
                break;
            case R.id.layout_next:
                //下一步
                break;
        }
    }
}
