package com.miaotu.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.miaotu.R;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.result.BaseResult;
import com.miaotu.result.EveryDayResult;
import com.miaotu.util.StringUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EveryDayPicActivity extends BaseActivity implements View.OnClickListener{
private ImageView ivPic,ivShare,ivDownload;
    private TextView tvDate1,tvDate2;
    private Button btnJump;
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 取消状态栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_every_day_pic);
        findView();
        bindView();
        init();
    }
    private void findView(){
        ivPic = (ImageView) findViewById(R.id.iv_pic);
        ivShare = (ImageView) findViewById(R.id.iv_share);
        ivDownload = (ImageView) findViewById(R.id.iv_download);
        tvDate1 = (TextView) findViewById(R.id.tv_date1);
        tvDate2 = (TextView) findViewById(R.id.tv_date2);
        btnJump = (Button) findViewById(R.id.btn_jump);
    }
    private void bindView(){
        ivPic.setOnClickListener(this);
        ivShare.setOnClickListener(this);
        ivDownload.setOnClickListener(this);
        btnJump.setOnClickListener(this);
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void init(){
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        wm.getDefaultDisplay().getSize(size);
        int width = size.x;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ivPic.getLayoutParams();
        params.height = width;
        ivPic.setLayoutParams(params);
        getDay();
    }
    private void getDay(){
        new BaseHttpAsyncTask<Void, Void, EveryDayResult>(this, true) {
            @Override
            protected void onCompleteTask(EveryDayResult result) {
                if(tvDate1==null){
                    return;
                }
                if (result.getCode() == BaseResult.SUCCESS) {
                    UrlImageViewHelper.setUrlDrawable(ivPic, result.getEveryDayInfo().getPicUrl() + "800x800", R.drawable.default_avatar);
                    url = result.getEveryDayInfo().getPicUrl();
                    tvDate1.setText(result.getEveryDayInfo().getDate1());
                    tvDate2.setText(result.getEveryDayInfo().getDate2());
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
                    String sysDatetime = fmt.format(calendar.getTime());
                    writePreference("everyday",sysDatetime);
                } else {
                    if(StringUtil.isEmpty(result.getMsg())){
                        showToastMsg("每日一图获取失败！");
                    }else{
                        showToastMsg(result.getMsg());
                    }
                }
            }

            @Override
            protected EveryDayResult run(Void... params) {
                return HttpRequestUtil.getInstance().getEveryDay();
            }

        }.execute();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_pic:
                break;
            case R.id.iv_share:
                break;
            case R.id.iv_download:
                UrlImageViewHelper.loadUrlDrawable(EveryDayPicActivity.this, url, new UrlImageViewCallback() {
                    @Override
                    public void onLoaded(ImageView imageView, Bitmap bitmap, String s, boolean b) {
                        MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "title", "description");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            Intent mediaScanIntent = new Intent(
                                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                            mediaScanIntent.setData(Uri.fromFile(Environment.getExternalStorageDirectory()));
                            sendBroadcast(mediaScanIntent);
                        } else {
                            sendBroadcast(new Intent(
                                    Intent.ACTION_MEDIA_MOUNTED,
                                    Uri.parse("file://"
                                            + Environment.getExternalStorageDirectory())));
                        }
                        showToastMsg("保存成功！");
                    }
                });
                break;
            case R.id.btn_jump:
                Intent intent = new Intent(EveryDayPicActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
