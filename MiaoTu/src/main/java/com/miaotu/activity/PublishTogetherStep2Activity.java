package com.miaotu.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.miaotu.R;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.form.PublishTogether;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.result.BaseResult;
import com.miaotu.result.PublishTogetherResult;
import com.miaotu.util.LogUtil;
import com.miaotu.util.StringUtil;
import com.miaotu.util.Util;
import com.photoselector.model.PhotoModel;

import java.io.File;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class PublishTogetherStep2Activity extends BaseActivity implements OnClickListener{
    PublishTogether publishTogether;
    private EditText etComment;
    private TextView tvPublish;
    private TextView tvTitle,tvLeft;
    private RadioGroup rgShare;
    private CheckBox rbSelected;
    private CheckBox rbCircle,rbWechat,rbWeibo,rbZone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_together_step2);
        findView();
        bindView();
        init();
    }
private void findView(){
    etComment = (EditText) findViewById(R.id.et_comment);
    tvPublish = (TextView) findViewById(R.id.tv_publish);
    tvLeft = (TextView) findViewById(R.id.tv_left);
    tvTitle = (TextView) findViewById(R.id.tv_title);
    rgShare = (RadioGroup) findViewById(R.id.rg_share);
    rbCircle = (CheckBox)findViewById(R.id.rb_share_circle);
    rbWechat = (CheckBox)findViewById(R.id.rb_share_wechat);
    rbWeibo = (CheckBox)findViewById(R.id.rb_share_weibo);
    rbZone = (CheckBox)findViewById(R.id.rb_share_zone);
}
private void bindView(){
    tvPublish.setOnClickListener(this);
    tvLeft.setOnClickListener(this);
    rbSelected = rbCircle;
    rbCircle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
                if (rbSelected == null || rbSelected.getId() == rbCircle.getId()) {
                    rbCircle.setChecked(true);
                    rbSelected = rbCircle;
                } else {
                    if (rbSelected.getId() != rbCircle.getId()) {
                        rbSelected.setChecked(false);
                        rbCircle.setChecked(true);
                        rbSelected = rbCircle;
                    }
                }
            } else {
                if (rbSelected.getId() == rbCircle.getId()) {
                    rbSelected = null;
                    rbCircle.setChecked(false);
                }
            }
        }
    });
    rbWechat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b){
                if (rbSelected == null || rbSelected.getId() == rbWechat.getId()){
                    rbWechat.setChecked(true);
                    rbSelected = rbWechat;
                }else {
                    if (rbSelected.getId() != rbWechat.getId()){
                        rbSelected.setChecked(false);
                        rbWechat.setChecked(true);
                        rbSelected = rbWechat;
                    }
                }
            }else {
                if (rbSelected.getId() == rbWechat.getId()){
                    rbSelected = null;
                    rbWechat.setChecked(false);
                }
            }
        }
    });
    rbWeibo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b){
                if (rbSelected == null || rbSelected.getId() == rbWeibo.getId()){
                    rbWeibo.setChecked(true);
                    rbSelected = rbWeibo;
                }else {
                    if (rbSelected.getId() != rbWeibo.getId()){
                        rbSelected.setChecked(false);
                        rbWeibo.setChecked(true);
                        rbSelected = rbWeibo;
                    }
                }
            }else {
                if (rbSelected.getId() == rbWeibo.getId()){
                    rbSelected = null;
                    rbWeibo.setChecked(false);
                }
            }
        }
    });
    rbZone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b){
                if (rbSelected == null || rbSelected.getId() == rbZone.getId()){
                    rbZone.setChecked(true);
                    rbSelected = rbZone;
                }else {
                    if (rbSelected.getId() != rbZone.getId()){
                        rbSelected.setChecked(false);
                        rbZone.setChecked(true);
                        rbSelected = rbZone;
                    }
                }
            }else {
                if (rbSelected.getId() == rbZone.getId()){
                    rbSelected = null;
                    rbZone.setChecked(false);
                }
            }
        }
    });
}
private void init(){
    publishTogether = (PublishTogether) getIntent().getSerializableExtra("publishTogether");
    tvTitle.setText("发起旅行");
}
private void publish(){
        new BaseHttpAsyncTask<Void, Void, PublishTogetherResult>(PublishTogetherStep2Activity.this, true) {
            @Override
            protected void onCompleteTask(PublishTogetherResult result) {
                if(tvPublish==null){
                    return;
                }
                if (result.getCode() == BaseResult.SUCCESS) {
                    showToastMsg("发布成功！");
                    String hearurl = "";
                    if (publishTogether.getImg() != null &&
                            publishTogether.getImg().split(",").length > 0){
                        hearurl = publishTogether.getImg().split(",")[0];
                    }
//                    rbSelected = (RadioButton)findViewById(rgShare.getCheckedRadioButtonId());
                    if (rbSelected != null){
                        share(rbSelected.getId(),result.getTogether().getId(),
                             result.getTogether().getComment(),hearurl);
                    }

                    setResult(1);
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
            protected PublishTogetherResult run(Void... params) {
                return HttpRequestUtil.getInstance().publishTogether(publishTogether);
            }
        }.execute();
}
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_publish:
                if(!StringUtil.isBlank(etComment.getText().toString())){
                    publishTogether.setRemark(etComment.getText().toString());
                    publishTogether.setToken(readPreference("token"));
                    publish();
                }else{
                    showToastMsg("请输入旅行推荐语！");
                }
                break;
            case R.id.tv_left:
                finish();
        }
    }

    /**
     * 分享
     * @param id
     * @param yid
     * @param remark
     * @param headurl
     */
    private void share(int id, String yid, String remark, String headurl){
        ShareSDK.initSDK(this);
        switch (id){
            case R.id.rb_share_circle:
                WechatMoments.ShareParams wmsp = new WechatMoments.ShareParams();
                if (StringUtil.isBlank(headurl)){
                    wmsp.setShareType(Platform.SHARE_TEXT);
                }else {
                    wmsp.setShareType(Platform.SHARE_WEBPAGE);
                    wmsp.setImageUrl(headurl + "200×200");
                    wmsp.setUrl("http://m.miaotu.com/ShareLine/?yid=" + yid);
                }
                wmsp.setTitle(remark + "\n http://m.miaotu.com/ShareLine/?yid=" + yid);
                wmsp.setText(remark + "\n http://m.miaotu.com/ShareLine/?yid=" + yid);
                Platform wechatmoments = ShareSDK.getPlatform(WechatMoments.NAME);
                wechatmoments.setPlatformActionListener(new PlatFormListener());
                wechatmoments.share(wmsp);
            break;
            case R.id.rb_share_wechat:
                Wechat.ShareParams wcsp = new Wechat.ShareParams();
                if (StringUtil.isBlank(headurl)){
                    wcsp.setShareType(Platform.SHARE_TEXT);
                }else {
                    wcsp.setShareType(Platform.SHARE_WEBPAGE);
                    wcsp.setImageUrl(headurl + "200x200");
                    wcsp.setUrl("http://m.miaotu.com/ShareLine/?yid=" + yid);
                }
                wcsp.setTitle(remark + "\n http://m.miaotu.com/ShareLine/?yid=" + yid);
                wcsp.setText(remark + "\n http://m.miaotu.com/ShareLine/?yid=" + yid);
                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                wechat.setPlatformActionListener(new PlatFormListener());
                wechat.share(wcsp);
                break;
            case R.id.rb_share_weibo:
                SinaWeibo.ShareParams wbsp = new SinaWeibo.ShareParams();
                wbsp.setText(remark + "\n http://m.miaotu.com/ShareLine/?yid=" + yid);
                if (!StringUtil.isBlank(headurl)){
                    wbsp.setImageUrl(headurl + "200x200");
                }
                Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                weibo.setPlatformActionListener(new PlatFormListener());
                weibo.share(wbsp);
                break;
            case R.id.rb_share_zone:
                QZone.ShareParams qqsp = new QZone.ShareParams();
                qqsp.setTitle(remark + "\n http://m.miaotu.com/ShareLine/?yid=" + yid);
                qqsp.setTitleUrl("http://m.miaotu.com/ShareLine/?yid=" + yid); // 标题的超链接
                qqsp.setText(remark + "\n http://m.miaotu.com/ShareLine/?yid=" + yid);
                qqsp.setImageUrl(headurl + "200x200");
                qqsp.setSite(getString(R.string.app_name));
                qqsp.setSiteUrl("http://m.miaotu.com/ShareLine/?yid=" + yid);

                Platform qzone = ShareSDK.getPlatform (QZone.NAME);
                qzone. setPlatformActionListener (new PlatFormListener()); // 设置分享事件回调
                // 执行图文分享
                qzone.share(qqsp);
            break;
        }
    }
    class PlatFormListener implements PlatformActionListener{

        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            showToastMsg("分享成功");
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            showToastMsg("分享失败");
        }

        @Override
        public void onCancel(Platform platform, int i) {
            showToastMsg("取消分享");
        }
    }
}
