package com.miaotu.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.miaotu.R;
import com.miaotu.adapter.DateArrayAdapter;
import com.miaotu.adapter.DateNumericAdapter;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.form.PublishCustomForm;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.model.ModifyPersonInfo;
import com.miaotu.result.BaseResult;
import com.miaotu.result.PhotoUploadResult;
import com.miaotu.util.LogUtil;
import com.miaotu.util.StringUtil;
import com.miaotu.view.WheelTwoColumnDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;


public class PublishCustomTourActivity3 extends BaseActivity implements OnClickListener{
    private TextView tvTitle,tvLeft,tvDel1,tvDel2;
    private EditText etTel,etId;
    private ImageView ivId1,ivId2;
    private Button btnNext;
    private PublishCustomForm customForm;
    private static final String IMAGE_FILE_LOCATION1 = Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/miaotu/temp1.jpg";
    private static final String IMAGE_FILE_LOCATION2 = Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/miaotu/temp2.jpg";
    Uri imageUri1 = Uri.parse(IMAGE_FILE_LOCATION1);//
    Uri imageUri2 = Uri.parse(IMAGE_FILE_LOCATION2);//
    List<File> imgs = new ArrayList<File>();
    File file1,file2;
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
        File myDir = new File(Environment
                .getExternalStorageDirectory().getAbsolutePath() + "/miaotu");
        myDir.mkdirs();
    };
    private boolean validate(){
        if(StringUtil.isEmpty(etTel.getText().toString())){
            showToastMsg("请输入您的手机号码！");
            return false;
        }
        if(!StringUtil.isPhoneNumber(etTel.getText().toString())){
            showToastMsg("请输入正确的手机号码！");
            return false;
        }
        if(StringUtil.isEmpty(etId.getText().toString())){
            showToastMsg("请输入身份证号码！");
            return false;
        }
        if(etId.getText().toString().length()<15){
            showToastMsg("请输入至少15位身份证号码！");
            return false;
        }
        if(file1==null){
            showToastMsg("请上传身份证正面照片！");
            return false;
        }
        if(file2==null){
            showToastMsg("请上传身份证反面照片！");
            return false;
        }
        return true;
    }
    public void chosePhoto(int requestCode) {
        File fos = null;
        try {
            if(requestCode==1){
                fos = new File(IMAGE_FILE_LOCATION1);
            }else{
                fos = new File(IMAGE_FILE_LOCATION2);
            }
            if(fos.exists()){
                fos.delete();
            }
            fos.deleteOnExit();
            fos.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(requestCode==1){
            imageUri1 = Uri.fromFile(fos);
        }else{
            imageUri2 = Uri.fromFile(fos);
        }
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        if(requestCode==1){
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri1);
        }else{
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri2);
        }
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
            startActivityForResult(intent,requestCode); // 如果requestCode=3，是修改头像
    }
    public static DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
            .showImageOnLoading(com.photoselector.R.drawable.ic_picture_loading)
            .showImageOnFail(com.photoselector.R.drawable.ic_picture_loadfailed)
            .cacheInMemory(false).cacheOnDisk(false).considerExifParams(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .imageScaleType(ImageScaleType.EXACTLY).build();
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            ImageLoader.getInstance().clearDiskCache();
            switch (requestCode) {
                case 1:
                    if (imageUri1 != null) {
                        file1 = new File(imageUri1.getPath());
                        LogUtil.d(file1.getAbsolutePath());
                        ImageLoader.getInstance().displayImage(Uri.fromFile(file1).toString(), ivId1, imageOptions);
                        tvDel1.setVisibility(View.VISIBLE);
                    }
                    break;
                case 2:
                    if (imageUri2 != null) {
                        file2 = new File(imageUri2.getPath());
                        LogUtil.d(file2.getAbsolutePath());
                        ImageLoader.getInstance().displayImage(imageUri2.toString(), ivId2, imageOptions);
                        tvDel2.setVisibility(View.VISIBLE);
                    }
                    break;
                default:
                    break;
            }
        }
        if(requestCode==3&&resultCode==1){
            setResult(1);
            finish();
        }
    }

    /**
     * 向服务器添加照片
     *
     * @param imgs
     */
    private void addPhoto(final List<File> imgs) {
        new BaseHttpAsyncTask<Void, Void, PhotoUploadResult>(this) {

            @Override
            protected void onCompleteTask(final PhotoUploadResult result) {
                if (result.getCode() == BaseResult.SUCCESS) {
                    customForm = (PublishCustomForm) getIntent().getSerializableExtra("form");
                    customForm.setPhone(etTel.getText().toString());
                    customForm.setIdCard(etId.getText().toString());
                    customForm.setIdPic(result.getPhotoList().get(0)+","+result.getPhotoList().get(1));
                    Intent intent = new Intent(PublishCustomTourActivity3.this,PublishCustomTourActivity4.class);
                    intent.putExtra("form",customForm);
                    startActivityForResult(intent,3);
                } else {
                    if (StringUtil.isBlank(result.getMsg())) {
                        showToastMsg("操作失败");
                    } else {
                        showToastMsg(result.getMsg());
                    }
                }
            }

            @Override
            protected PhotoUploadResult run(Void... params) {
                return HttpRequestUtil.getInstance().uploadPhoto(imgs);
            }

        }.execute();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_next:
                if(validate()){
                    List<File>imgs = new ArrayList<>();
                    imgs.add(file1);
                    imgs.add(file2);
                    addPhoto(imgs);

                }
                break;
            case R.id.tv_left:
                finish();
                break;
            case R.id.iv_id_1:
                //身份证正面
                chosePhoto(1);
                break;
            case R.id.iv_id_2:
                //身份证反面
                chosePhoto(2);
                break;
            case R.id.tv_del1:
                //身份证反面删除
                file1 = null;
                ivId1.setImageResource(R.drawable.bg_id_1);
                tvDel1.setVisibility(View.GONE);
                break;
            case R.id.tv_del2:
                //身份证正面删除
                file2 = null;
                tvDel2.setVisibility(View.GONE);
                ivId2.setImageResource(R.drawable.bg_id_2);
                break;
        }
    }
}
