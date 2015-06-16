package com.miaotu.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miaotu.R;
import com.miaotu.adapter.DateArrayAdapter;
import com.miaotu.adapter.DateNumericAdapter;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.form.PublishTogether;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.result.BaseResult;
import com.miaotu.result.PhotoUploadResult;
import com.miaotu.util.LogUtil;
import com.miaotu.util.StringUtil;
import com.miaotu.util.Util;
import com.miaotu.view.DraggableGridView;
import com.miaotu.view.FlowLayout;
import com.miaotu.view.FlowRadioGroup;
import com.miaotu.view.OnRearrangeListener;
import com.miaotu.view.WheelTwoColumnDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.photoselector.model.PhotoModel;
import com.photoselector.ui.PhotoPreviewActivity;
import com.photoselector.ui.PhotoSelectorActivity;
import com.photoselector.util.CommonUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;

public class PublishTogetherStep1Activity extends BaseActivity implements OnClickListener{
    DraggableGridView dgv;
    private ArrayList<PhotoModel> photoList;
    private List<File>files;
    private TextView tvStartDate,tvEndDate,tvPhotoNum;
    private EditText etDesCity,etOrigiCity;
    private RadioGroup rgFee;
    private FlowRadioGroup rgRequirement;
    private EditText etTag,tvGatherLocation;
    private FlowLayout layoutTags;
    private LinearLayout layoutNext;
    private Button btnTagAdd;
    private WheelTwoColumnDialog dialog;
    PublishTogether publishTogether;
    private TextView tvTitle,tvLeft;
    private TextView tvSelectWantGo,tvSelectOrigion,tvCount;
    public static DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
            .showImageOnLoading(com.photoselector.R.drawable.ic_picture_loading)
            .showImageOnFail(com.photoselector.R.drawable.ic_picture_loadfailed)
            .cacheInMemory(false).cacheOnDisk(true).considerExifParams(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .imageScaleType(ImageScaleType.EXACTLY).build();
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
        etDesCity = (EditText) findViewById(R.id.et_des_city);
        etOrigiCity = (EditText) findViewById(R.id.et_origin_city);
        tvGatherLocation = (EditText) findViewById(R.id.tv_gather_location);
        tvStartDate = (TextView) findViewById(R.id.tv_start_date);
        tvEndDate = (TextView) findViewById(R.id.tv_end_date);
        tvCount = (TextView) findViewById(R.id.tv_count);
        rgFee = (RadioGroup) findViewById(R.id.rg_fee);
        rgRequirement = (FlowRadioGroup) findViewById(R.id.rg_requirement);
        etTag = (EditText) findViewById(R.id.et_tag);
        layoutTags = (FlowLayout) findViewById(R.id.layout_tags);
        layoutNext = (LinearLayout) findViewById(R.id.layout_next);
        btnTagAdd = (Button) findViewById(R.id.btn_tag_add);
        tvPhotoNum = (TextView) findViewById(R.id.tv_photo_num);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvLeft = (TextView) findViewById(R.id.tv_left);
        tvSelectOrigion = (TextView) findViewById(R.id.tv_select_origin);
        tvSelectWantGo = (TextView) findViewById(R.id.tv_select_wantgo);
    }
    private void bindView(){
        tvLeft.setOnClickListener(this);
        tvStartDate.setOnClickListener(this);
        tvEndDate.setOnClickListener(this);
        layoutNext.setOnClickListener(this);
        btnTagAdd.setOnClickListener(this);
        tvSelectWantGo.setOnClickListener(this);
        tvSelectOrigion.setOnClickListener(this);
        tvCount.setOnClickListener(this);
        dgv.setOnRearrangeListener(new OnRearrangeListener() {
            public void onRearrange(int oldIndex, int newIndex) {
                photoList.add(newIndex, photoList.remove(oldIndex));
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
                    intent.putExtra("key_max", 9 - photoList.size());
                    startActivityForResult(intent, 3);
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
        tvTitle.setText("发起旅行");

        dgv.addView(imageView);
        photoList = new ArrayList<>();
        files = new ArrayList<>();
        tvStartDate.setText(Calendar.getInstance().get(Calendar.YEAR) + "-" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "-" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        tvEndDate.setText(Calendar.getInstance().get(Calendar.YEAR) + "-" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "-" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        publishTogether = new PublishTogether();
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
    private boolean validate(){
        if(StringUtil.isBlank(etDesCity.getText().toString())){
            showToastMsg("请选择目的城市！");
            return false;
        }
        if(StringUtil.isBlank(etOrigiCity.getText().toString())){
            showToastMsg("请选择出发城市！");
            return false;
        }
        return true;
    }
    private void next(){
        Intent intent = new Intent(PublishTogetherStep1Activity.this,PublishTogetherStep2Activity.class);

        publishTogether.setDesCity(etDesCity.getText().toString());
        publishTogether.setOriginCity(etOrigiCity.getText().toString());
        publishTogether.setOriginLocation(tvGatherLocation.getText().toString());
        publishTogether.setStartDate(tvStartDate.getText().toString());
        publishTogether.setEndDate(tvEndDate.getText().toString());
        publishTogether.setNumber(tvCount.getText().toString());
        publishTogether.setRequirement(((RadioButton) findViewById(rgRequirement.getCheckedRadioButtonId())).getText().toString());
        publishTogether.setFee(((RadioButton)findViewById(rgFee.getCheckedRadioButtonId())).getText().toString());
        String tags = "";
        for(int i=0;i<layoutTags.getChildCount();i++){
            tags+=(((TextView)layoutTags.getChildAt(i).findViewById(R.id.tv_tag)).getText().toString()+",");
        }
        if(layoutTags.getChildCount()!=0){
            tags.substring(0,tags.length()-1);
        }
        publishTogether.setTags(tags);

        intent.putExtra("publishTogether",publishTogether);
        startActivityForResult(intent,4);
    }
    private void uploadPhoto(){
        files.clear();
        for(PhotoModel photo:photoList){
            File file = new File(photo.getOriginalPath());
            files.add(file);
        }
        LogUtil.d("图片个数："+photoList.size());
        boolean gifFlg = false;
        for(File file:files){
            String ext = Util.getExtName(file);
            LogUtil.d("后缀名"+ext);
            if(ext.equals("gif")||ext.equals("GIF")){
                showToastMsg("暂时不支持上传gif动图(⊙o⊙)");
                gifFlg = true;
                break;
            }
        }
        if(gifFlg){
            return;
        }
        new BaseHttpAsyncTask<Void, Void, PhotoUploadResult>(PublishTogetherStep1Activity.this, true) {
            @Override
            protected void onCompleteTask(PhotoUploadResult result) {
                if(tvPhotoNum==null){
                    return;
                }
                if (result.getCode() == BaseResult.SUCCESS) {
                    String img = "";
                    for(String temp:result.getPhotoList()){
                        img+=(temp+",");
                    }
                    img.substring(0,img.length()-1);
                    publishTogether.setImg(img);
                    next();
                } else {
                    if (StringUtil.isEmpty(result.getMsg())) {
                        showToastMsg("图片上传失败！");
                    } else {
                        showToastMsg(result.getMsg());
                    }
                }
            }

            @Override
            protected PhotoUploadResult run(Void... params) {
                return HttpRequestUtil.getInstance().uploadPhoto(files);
            }
        }.execute();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1){
            if(requestCode==1){
                etDesCity.setText(data.getStringExtra("city"));
            }
            if(requestCode==2){
                etOrigiCity.setText(data.getStringExtra("city"));
            }
        }
        if(requestCode==3&&resultCode==RESULT_OK){

            //选择照片返回
            List<PhotoModel> selected = (ArrayList<PhotoModel>) data.getSerializableExtra("photos");
            for(int i=0;i<selected.size();i++){
                for(int j=0;j<photoList.size();j++){
                    if(selected.get(i).getOriginalPath().equals(photoList.get(j).getOriginalPath())){
                        //已经添加过的去除掉
                        selected.remove(i);
                        break;
                    }
                }
            }
            photoList.addAll(selected);
            dgv.removeAllViews();
            for (PhotoModel photo:photoList){
                ImageView imageView = new ImageView(this);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(params);
                ImageLoader.getInstance().displayImage(
                        "file://" + photo.getOriginalPath(), imageView, imageOptions);
                LogUtil.d("图片路径：" + photo.getOriginalPath());
                ImageView ivDel = new ImageView(this);
                RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(Util.dip2px(this,25),Util.dip2px(this,25));
                params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                params1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                ivDel.setLayoutParams(params1);
                ivDel.setTag(photo);
                ivDel.setBackgroundResource(R.drawable.icon_pic_del);
                ivDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dgv.removeView((View)view.getParent());
                        photoList.remove(view.getTag());
                        if(photoList.size()==8){
//                            dgv.getChildAt(dgv.getChildCount()-1).setVisibility(View.VISIBLE);
                            //添加图片的item
                            ImageView imageView = new ImageView(PublishTogetherStep1Activity.this);
                            AbsListView.LayoutParams params = new AbsListView.LayoutParams(Util.dip2px(PublishTogetherStep1Activity.this,70),Util.dip2px(PublishTogetherStep1Activity.this,70));
                            imageView.setLayoutParams(params);
                            imageView.setBackgroundResource(R.drawable.icon_pic_add);
                            dgv.addView(imageView);
                        }
                        tvPhotoNum.setText("你还可以插入"+(9-photoList.size())+"张图片");
                    }
                });

                RelativeLayout relativeLayout = new RelativeLayout(this);
                relativeLayout.addView(imageView);
                relativeLayout.addView(ivDel);

                dgv.addView(relativeLayout);
            }
            //添加图片的item
            ImageView imageView = new ImageView(this);
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(Util.dip2px(this,70),Util.dip2px(this,70));
            imageView.setLayoutParams(params);
            imageView.setBackgroundResource(R.drawable.icon_pic_add);
            tvPhotoNum.setText("你还可以插入"+(9-photoList.size())+"张图片");
            if(photoList.size()>8){
//                dgv.getChildAt(dgv.getChildCount()-1).setVisibility(View.GONE);
            }else{
                dgv.addView(imageView);
//                dgv.getChildAt(dgv.getChildCount()-1).setVisibility(View.VISIBLE);
            }
        }
        if(requestCode==4&&resultCode==1){
            finish();
        }

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_left:
                finish();
                break;
//            case R.id.tv_des_city:
//                //目的城市
//                Intent cityIntent = new Intent(PublishTogetherStep1Activity.this,CityListActivity.class);
//                startActivityForResult(cityIntent, 1);
//                break;
//            case R.id.tv_origin_city:
//                //出发城市
//                Intent city2Intent = new Intent(PublishTogetherStep1Activity.this,CityListActivity.class);
//                startActivityForResult(city2Intent, 2);
//                break;
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
                if(layoutTags.getChildCount()<6){
                    final View viewTemp = getLayoutInflater().inflate(R.layout.item_tag,null);
                    TextView tvTag = (TextView) viewTemp.findViewById(R.id.tv_tag);
                    ImageView ivTag = (ImageView) viewTemp.findViewById(R.id.iv_tag);
                    ivTag.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            layoutTags.removeView(viewTemp);
                        }
                    });
                    tvTag.setText(StringUtil.trimAll(etTag.getText().toString()));
                    etTag.setText("");
                    FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT,FlowLayout.LayoutParams.WRAP_CONTENT);
                    params.rightMargin = Util.dip2px(PublishTogetherStep1Activity.this,10);
                    params.bottomMargin = Util.dip2px(PublishTogetherStep1Activity.this,10);
                    viewTemp.setLayoutParams(params);
                    layoutTags.addView(viewTemp);
                    layoutTags.requestLayout();
                }else {
                    showToastMsg("最多只能添加6个线路标签");
                }
                break;
            case R.id.layout_next:
                //下一步
                if(validate()){
                    if(photoList.size()==0){
                        next();
                    }else{
                        uploadPhoto();
                    }
                }
                break;
            case R.id.tv_select_origin: //出发城市
                //出发城市
                Intent city2Intent = new Intent(PublishTogetherStep1Activity.this,CityListActivity.class);
                startActivityForResult(city2Intent, 2);
                break;
            case R.id.tv_select_wantgo: //目的地
                //目的城市
                Intent cityIntent = new Intent(PublishTogetherStep1Activity.this,CityListActivity.class);
                startActivityForResult(cityIntent, 1);
                break;
            case R.id.tv_count:
                getFriendsDialog();
                break;
        }
    }

    // 获取旅伴人数dialog
    private void getFriendsDialog() {
        // 为dialog的listview赋值
        LayoutInflater lay = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = lay.inflate(R.layout.dialog_age_layout, null);
        final WheelView wvDay = (WheelView) v.findViewById(R.id.wv_day);

        final String months[] = new String[]{"1", "2","3","4","5","6", "7","8","9","10",
                "11", "12","13","14","15","16", "17","18","19","20","不限"};
        wvDay.setViewAdapter(new DateArrayAdapter(this, months, 0));
        // 创建dialog
        dialog = new WheelTwoColumnDialog(this, R.style.Dialog_Fullscreen, v);
        dialog.setOnConfirmListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                int monthIndex = wvMonth.getCurrentItem();
                int dayIndex = wvDay.getCurrentItem();
                String currentCount = (dayIndex + 1) + "";
                if (dayIndex == 20){
                    currentCount = "不限";
                }
                tvCount.setText(currentCount);
                dialog.dismiss();
            }
        });
    }
}
