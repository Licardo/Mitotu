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
import com.miaotu.model.PhotoInfo;
import com.miaotu.result.BaseResult;
import com.miaotu.result.EveryDayResult;
import com.miaotu.util.StringUtil;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.photoselector.model.PhotoModel;
import com.photoselector.ui.PhotoPreviewActivity;
import com.photoselector.util.CommonUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

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
    /**
     * 分享到sns社区平台
     */
    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        oks.setTheme(OnekeyShareTheme.CLASSIC);
        // 关闭sso授权
        oks.disableSSOWhenAuthorize();
        // 分享时Notification的图标和文字
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用

//        oks.setTitle(togetherDetailResult.getTogether().getComment() + "\n http://m.miaotu.com/journey/detail.php?id=" + togetherDetailResult.getTogether().getId());
//        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//        oks.setTitleUrl("http://m.miaotu.com/journey/detail.php?id=" + togetherDetailResult.getTogether().getId());
        // text是分享文本，所有平台都需要这个字段
        oks.setText("每日一图");
//        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImageUrl(url);
//        // url仅在微信（包括好友和朋友圈）中使用
//        oks.setUrl("http://m.miaotu.com/journey/detail.php?id=" + togetherDetailResult.getTogether().getId());
//        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment(togetherDetailResult.getTogether().getComment() + "\n http://m.miaotu.com/journey/detail.php?id=" + togetherDetailResult.getTogether().getId());
//        // site是分享此内容的网站名称，仅在QQ空间使用
//        oks.setSite(getString(R.string.app_name));
//        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//        oks.setSiteUrl("http://m.miaotu.com/journey/detail.php?id=" +togetherDetailResult.getTogether().getId());

        // 启动分享GUI
        oks.show(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_pic:
                ImageLoader.getInstance().init(
                        new ImageLoaderConfiguration.Builder(getApplicationContext())
                                .memoryCacheExtraOptions(480, 800)//设置缓存图片时候的宽高最大值，默认为屏幕宽高
//                        .discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75)//设置硬盘缓存，默认格式jpeg，压缩质量70
                                .threadPoolSize(5)  //设置线程池的最高线程数量
                                .threadPriority(Thread.NORM_PRIORITY)//设置线程优先级
                                .denyCacheImageMultipleSizesInMemory()//自动缩放
                                .memoryCache(new UsingFreqLimitedMemoryCache(4 * 1024 * 1024))//设置缓存大小，UsingFrgLimitMemoryCache类可以扩展
                                .imageDownloader(new BaseImageDownloader(this, 5000, 30000)).build());
                PhotoModel photoModel = new PhotoModel();
                photoModel.setOriginalPath(url);
                ArrayList<PhotoModel> photoList = new ArrayList<>();
                photoList.add(photoModel);
                //点击照片
                /** 预览照片 */
                Bundle bundle = new Bundle();
                bundle.putSerializable("photos", photoList);
                bundle.putSerializable("position", 0);
                CommonUtils.launchActivity(EveryDayPicActivity.this, PhotoPreviewActivity.class, bundle);
                break;
            case R.id.iv_share:
                Intent shareintent = new Intent(EveryDayPicActivity.this, HomeShareActivity.class);
                shareintent.putExtra("url", url);
                startActivity(shareintent);
//                showShare();
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
