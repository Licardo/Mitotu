package com.miaotu.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.miaotu.R;
import com.miaotu.adapter.ImageItemAdapter;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.model.PersonInfo;
import com.miaotu.model.PhotoInfo;
import com.miaotu.model.RegisterInfo;
import com.miaotu.model.Together;
import com.miaotu.model.TogetherReply;
import com.miaotu.result.BaseResult;
import com.miaotu.result.LoginResult;
import com.miaotu.result.TogetherDetailResult;
import com.miaotu.util.LogUtil;
import com.miaotu.util.MD5;
import com.miaotu.util.StringUtil;
import com.miaotu.util.Util;
import com.miaotu.view.CircleImageView;
import com.miaotu.view.FlowLayout;
import com.miaotu.view.GuideGallery;
import com.miaotu.view.MyGridView;
import com.photoselector.model.PhotoModel;
import com.photoselector.ui.PhotoPreviewActivity;
import com.photoselector.util.CommonUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

public class TogetherDetailActivity extends BaseActivity implements View.OnClickListener{
private String id;
private Together together;
    private TextView tvLeft,tvTitle,tvName,tvAge,tvJob,tvComment;
    private CircleImageView ivHeadPhoto;
    private ImageView ivGender,ivLike;
    private FlowLayout personalTag,togetherTag;
    private MyGridView gvPhotos;
    private ImageItemAdapter adapter;
    private TextView tvJoined,tvLiked,tvStartDate,tvEndDate,tvDesCity,tvOrginCity,tvFee,tvRequirement;
    private RelativeLayout layoutJoined,layoutLiked,layoutPublishComment;
    private LinearLayout layoutJoinedPhotos,layoutInterestPhotos,layoutInterestPart,layoutJoinedPart;
    private ImageView ivJoinedTriangle,ivInterestTriangle,ivChat,ivGroupChat,ivShare;
    private LinearLayout layoutLike,layoutComment,layoutJoin,layoutMenu,layoutCommentList;
    private LinearLayout layoutJoinMore,layoutLikeMore,layoutAll;
    private boolean islike;
    private EditText etComment;
    private TextView tvPublishComment;
    private TogetherDetailResult togetherDetailResult;
    final ArrayList<PhotoModel> photoList = new ArrayList<PhotoModel>();
    private View ivLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_together_detail);

        findView();
        bindView();
        init();
    }
    private void findView(){
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvLeft = (TextView) findViewById(R.id.tv_left);
        ivHeadPhoto = (CircleImageView) findViewById(R.id.iv_head_photo);
        tvName = (TextView) findViewById(R.id.tv_name);
        ivGender = (ImageView) findViewById(R.id.iv_gender);
        tvAge = (TextView) findViewById(R.id.tv_age);
        tvJob = (TextView) findViewById(R.id.tv_job);
        tvComment = (TextView) findViewById(R.id.tv_comment);
        personalTag = (FlowLayout) findViewById(R.id.fl_personal_tag);
        gvPhotos = (MyGridView) findViewById(R.id.gv_photos);
        tvJoined = (TextView) findViewById(R.id.tv_joined);
        tvLiked = (TextView) findViewById(R.id.tv_interest);
        tvStartDate = (TextView) findViewById(R.id.iv_start_date);
        tvEndDate = (TextView) findViewById(R.id.tv_end_date);
        tvDesCity = (TextView) findViewById(R.id.tv_des_city);
        tvOrginCity = (TextView) findViewById(R.id.tv_origin_city);
        tvFee = (TextView) findViewById(R.id.tv_fee);
        togetherTag = (FlowLayout) findViewById(R.id.fl_together_tag);
        tvRequirement = (TextView) findViewById(R.id.tv_requirement);
        layoutJoined = (RelativeLayout) findViewById(R.id.layout_joined);
        layoutLiked = (RelativeLayout) findViewById(R.id.layout_interest);
        layoutJoinedPhotos = (LinearLayout) findViewById(R.id.layout_joined_photos);
        layoutInterestPhotos = (LinearLayout) findViewById(R.id.layout_interest_photos);
        ivJoinedTriangle = (ImageView) findViewById(R.id.iv_joined_triangle);
        ivInterestTriangle = (ImageView) findViewById(R.id.iv_interest_triangle);
        layoutInterestPart = (LinearLayout) findViewById(R.id.layout_interest_part);
        layoutJoinedPart = (LinearLayout) findViewById(R.id.layout_joined_part);
        layoutLike = (LinearLayout) findViewById(R.id.layout_like);
        layoutComment = (LinearLayout) findViewById(R.id.layout_comment);
        layoutJoin = (LinearLayout) findViewById(R.id.layout_join);
        ivLike = (ImageView) findViewById(R.id.iv_like);
        layoutMenu = (LinearLayout) findViewById(R.id.layout_menu);
        layoutPublishComment = (RelativeLayout) findViewById(R.id.layout_publish_comment);
        etComment = (EditText) findViewById(R.id.et_comment);
        tvPublishComment = (TextView) findViewById(R.id.tv_publish_comment);
        layoutCommentList = (LinearLayout) findViewById(R.id.layout_comment_list);
        layoutJoinMore = (LinearLayout) findViewById(R.id.layout_joined_more);
        layoutLikeMore = (LinearLayout) findViewById(R.id.layout_interest_more);
        layoutAll = (LinearLayout) findViewById(R.id.layout_all);

        ivChat = (ImageView) findViewById(R.id.iv_chat);
        ivGroupChat = (ImageView) findViewById(R.id.iv_group_chat);
        ivShare = (ImageView) findViewById(R.id.iv_share);

        ivLine = findViewById(R.id.iv_line);
    }
    private void bindView(){
        layoutJoinMore.setOnClickListener(this);
        layoutLikeMore.setOnClickListener(this);
        layoutLiked.setOnClickListener(this);
        layoutJoined.setOnClickListener(this);
        layoutLike.setOnClickListener(this);
        layoutComment.setOnClickListener(this);
        layoutJoin.setOnClickListener(this);
        tvPublishComment.setOnClickListener(this);
        tvLeft.setOnClickListener(this);
        ivHeadPhoto.setOnClickListener(this);
        ivChat.setOnClickListener(this);
        ivGroupChat.setOnClickListener(this);
        ivShare.setOnClickListener(this);
        layoutAll.setOnClickListener(this);
        gvPhotos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //点击照片
                /** 预览照片 */
                Bundle bundle = new Bundle();
                bundle.putSerializable("photos", photoList);
                bundle.putSerializable("position", i);
                CommonUtils.launchActivity(TogetherDetailActivity.this, PhotoPreviewActivity.class, bundle);
            }
        });
    }
    private void init(){
        id=getIntent().getStringExtra("id");
        tvTitle.setText("约游详情");

        getDetail();
    }
    //获取一起去详情
    private void getDetail() {
        new BaseHttpAsyncTask<Void, Void, TogetherDetailResult>(TogetherDetailActivity.this, true) {
            @Override
            protected void onCompleteTask(TogetherDetailResult result) {
                if(tvTitle==null){
                    return;
                }
                if (result.getCode() == BaseResult.SUCCESS) {
                    togetherDetailResult = result;
                    writeDetail(result);
                } else {
                   if(StringUtil.isEmpty(result.getMsg())){
                       showToastMsg("获取约游详情失败");
                   }else{
                       showToastMsg(result.getMsg());
                   }
                }
            }

            @Override
            protected TogetherDetailResult run(Void... params) {
                return HttpRequestUtil.getInstance().getTogetherDetail(readPreference("token"),id);
            }

        }.execute();
    }
    private void writeDetail(TogetherDetailResult result){
        together = result.getTogether();
        UrlImageViewHelper.setUrlDrawable(ivHeadPhoto, result.getTogether().getHeadPhoto() + "", R.drawable.default_avatar);
        tvName.setText(result.getTogether().getNickname());
        if(result.getTogether().getGender().equals("男")){
            ivGender.setBackgroundResource(R.drawable.icon_man);
        }else{
            ivGender.setBackgroundResource(R.drawable.icon_woman);
        }
        tvAge.setText(result.getTogether().getAge()+"岁");
        tvJob.setText(result.getTogether().getJob());
        tvComment.setText(result.getTogether().getComment());
        if(!StringUtil.isEmpty(result.getTogether().getUserTag())){
            String[] tags = result.getTogether().getUserTag().split(",");
            personalTag.removeAllViews();
            for(String tag:tags){
                TextView textView = new TextView(this);
                textView.setTextColor(getResources().getColor(R.color.b4b4b4));
                textView.setText(tag);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                textView.setGravity(Gravity.CENTER);
                textView.setBackgroundResource(R.drawable.bg_hottag);
                FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.rightMargin = Util.dip2px(this,10);
                params.bottomMargin = Util.dip2px(this,5);
                textView.setLayoutParams(params);
                textView.setPadding(Util.dip2px(this, 10), Util.dip2px(this, 4), Util.dip2px(this, 10), Util.dip2px(this, 4));
                personalTag.addView(textView);
            }
        }
        photoList.clear();
        for (int i = 0; i < result.getTogether().getPicList().size(); i++) {
            PhotoInfo photoInfo = result.getTogether().getPicList().get(i);
            PhotoModel photoModel = new PhotoModel();
            photoModel.setOriginalPath(photoInfo.getUrl());
            photoList.add(photoModel);
        }
        adapter= new ImageItemAdapter(this,result.getTogether().getPicList());
        gvPhotos.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        if (result.getTogether().getPicList() == null ||
                result.getTogether().getPicList().size() < 1){
            ivLine.setVisibility(View.GONE);
        }else {
            ivLine.setVisibility(View.VISIBLE);
        }

        tvJoined.setText("报名 " + result.getTogether().getJoinCount());
        tvLiked.setText("喜欢 " + result.getTogether().getLikeCount());
        tvJoined.setTextColor(getResources().getColor(R.color.grey));

        tvStartDate.setText(result.getTogether().getStartDate());
        tvEndDate.setText(result.getTogether().getEndDate());
        tvDesCity.setText(result.getTogether().getDesCity());
        tvOrginCity.setText(result.getTogether().getOriginCity()+" "+result.getTogether().getOriginLocation());
        tvFee.setText(result.getTogether().getFee());
        tvRequirement.setText(result.getTogether().getRequirement());
        if(!StringUtil.isEmpty(result.getTogether().getTags())){
            String[] tags1 = result.getTogether().getTags().split(",");
            togetherTag.removeAllViews();
            for(String tag:tags1){
                TextView textView = new TextView(this);
                textView.setTextColor(getResources().getColor(R.color.b4b4b4));
                textView.setText(tag);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                textView.setGravity(Gravity.CENTER);
                textView.setBackgroundResource(R.drawable.bg_hottag);
                FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.rightMargin = Util.dip2px(this,10);
                params.bottomMargin = Util.dip2px(this,5);
                textView.setLayoutParams(params);
                textView.setPadding(Util.dip2px(this, 10), Util.dip2px(this, 4), Util.dip2px(this, 10), Util.dip2px(this, 4));
                togetherTag.addView(textView);
            }
        }
        islike=result.getTogether().isLike();
        if(islike){
            ivLike.setBackgroundResource(R.drawable.icon_like);
        }else{
            ivLike.setBackgroundResource(R.drawable.icon_unlike);
        }
        layoutJoinedPhotos.removeAllViews();
        if(result.getTogether().getJoinList()!=null) {
            for (int i = 0; i < result.getTogether().getJoinList().size(); i++) {
                PersonInfo m = result.getTogether().getJoinList().get(i);
                CircleImageView imageView = new CircleImageView(
                        this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        Util.dip2px(this, 24),
                        Util.dip2px(this, 24));
                if (i == 0) {
                    params.leftMargin = Util.dip2px(
                            this, 10);
                }
                params.rightMargin = Util.dip2px(
                        this, 10);
                imageView.setLayoutParams(params);
                imageView.setTag(m.getUid());
                imageView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if(!Util.isNetworkConnected(TogetherDetailActivity.this)) {
                            showToastMsg("当前未联网，请检查网络设置");
                            return;
                        }
                        MobclickAgent.onEvent(TogetherDetailActivity.this, "线路详情页-感兴趣的人");
                        Intent intent = new Intent(
                                TogetherDetailActivity.this,
                                PersonCenterActivity.class);
                        intent.putExtra("uid", (String) v.getTag());
                        startActivity(intent);
                    }
                });
                layoutJoinedPhotos.addView(imageView);
                UrlImageViewHelper.setUrlDrawable(
                        imageView,
                        m.getHeadurl() + "100x100", R.drawable.icon_default_head_photo);
            }
        }
        layoutInterestPhotos.removeAllViews();
        if(result.getTogether().getLikeList()!=null){
            for (int i = 0; i < result.getTogether().getLikeList().size(); i++) {
                PersonInfo m = result.getTogether().getLikeList().get(i);
                CircleImageView imageView = new CircleImageView(
                        this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        Util.dip2px(this, 24),
                        Util.dip2px(this, 24));
                if (i == 0) {
                    params.leftMargin = Util.dip2px(
                            this, 10);
                }
                params.rightMargin = Util.dip2px(
                        this, 10);
                imageView.setLayoutParams(params);
                imageView.setTag(m.getUid());
                imageView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if(!Util.isNetworkConnected(TogetherDetailActivity.this)) {
                            showToastMsg("当前未联网，请检查网络设置");
                            return;
                        }
                        MobclickAgent.onEvent(TogetherDetailActivity.this, "线路详情页-感兴趣的人");
                        Intent intent = new Intent(
                                TogetherDetailActivity.this,
                                PersonCenterActivity.class);
                        intent.putExtra("uid", (String) v.getTag());
                        startActivity(intent);
                    }
                });
                layoutInterestPhotos.addView(imageView);
                UrlImageViewHelper.setUrlDrawable(
                        imageView,
                        m.getHeadurl() + "100x100", R.drawable.icon_default_head_photo);
            }
        }
        layoutCommentList.removeAllViews();
        if(result.getTogether().getReplyList()!=null){
            for(TogetherReply reply:result.getTogether().getReplyList()){
                TextView textView = new TextView(this);
                SpannableStringBuilder style=new SpannableStringBuilder(reply.getNickname()+" "+reply.getContent());
                style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.b4b4b4)), 0, reply.getNickname().length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.grey64)), reply.getNickname().length(), reply.getNickname().length() + reply.getContent().length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                textView.setText(style);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.bottomMargin = Util.dip2px(this,5);
                textView.setLayoutParams(params);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                textView.setLineSpacing(0f,1.4f);
                layoutCommentList.addView(textView);
            }
        }

    }


    private void like() {
        new BaseHttpAsyncTask<Void, Void, BaseResult>(this, true) {
            @Override
            protected void onCompleteTask(BaseResult result) {
                if(tvLiked==null){
                    return;
                }
                if (result.getCode() == BaseResult.SUCCESS) {
                    if(islike){
                        ivLike.setBackgroundResource(R.drawable.icon_unlike);
                        showToastMsg("取消喜欢成功！");
                        islike=false;
                        for(PersonInfo p:togetherDetailResult.getTogether().getLikeList()){
                            if(p.getUid().equals(readPreference("uid"))){
                                togetherDetailResult.getTogether().getLikeList().remove(p);
                                break;
                            }
                        }
                        togetherDetailResult.getTogether().setIsLike(false);
                        String strcount = tvLiked.getText().toString().substring(2).trim();
                        int count = Integer.parseInt(strcount);
                        count-=1;
                        if (count < 0){
                            count = 0;
                        }
                        togetherDetailResult.getTogether().setLikeCount(count + "");
                        setResult(1002);
                    }else{
                        showToastMsg("喜欢成功！");
                        ivLike.setBackgroundResource(R.drawable.icon_like);
                        islike=true;
                        PersonInfo personInfo = new PersonInfo();
                        personInfo.setUid(readPreference("uid"));
                        personInfo.setHeadurl(readPreference("headphoto"));
                        if(togetherDetailResult.getTogether().getLikeList()==null){
                            togetherDetailResult.getTogether().setLikeList(new ArrayList<PersonInfo>());
                        }
                        togetherDetailResult.getTogether().getLikeList().add(0, personInfo);
                        togetherDetailResult.getTogether().setIsLike(true);
                        String strcount = tvLiked.getText().toString().substring(2).trim();
                        int count = Integer.parseInt(strcount);
                        count+=1;
                        togetherDetailResult.getTogether().setLikeCount(count + "");
                        setResult(1001);
                    }
                    writeDetail(togetherDetailResult);
                } else {
                    if(StringUtil.isEmpty(result.getMsg())){
                        showToastMsg("失败！");
                    }else{
                        showToastMsg(result.getMsg());
                    }
                }
            }

            @Override
            protected BaseResult run(Void... params) {
                return HttpRequestUtil.getInstance().likeTogether(readPreference("token"),id);
            }

        }.execute();
    }

    /**
     * 发表评论
     */
    private void publishComment() {
        new BaseHttpAsyncTask<Void, Void, BaseResult>(this, true) {
            @Override
            protected void onCompleteTask(BaseResult result) {
                if(tvLiked==null){
                    return;
                }
                if (result.getCode() == BaseResult.SUCCESS) {
                        showToastMsg("评论发表成功！");
                    layoutMenu.setVisibility(View.VISIBLE);
                    layoutPublishComment.setVisibility(View.GONE);
                    TogetherReply reply1 = new TogetherReply();
                    reply1.setNickname(readPreference("name"));
                    reply1.setContent(StringUtil.trimAll(etComment.getText().toString()));
                    etComment.setText("");
                    if(togetherDetailResult.getTogether().getReplyList()==null){
                        togetherDetailResult.getTogether().setReplyList(new ArrayList<TogetherReply>());
                    }
                    togetherDetailResult.getTogether().getReplyList().add(0, reply1);
                    layoutCommentList.removeAllViews();
                    if(togetherDetailResult.getTogether().getReplyList()!=null){
                        for(TogetherReply reply:togetherDetailResult.getTogether().getReplyList()){
                            TextView textView = new TextView(TogetherDetailActivity.this);
                            SpannableStringBuilder style=new SpannableStringBuilder(reply.getNickname()+" "+reply.getContent());
                            style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.b4b4b4)), 0, reply.getNickname().length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                            style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.grey64)), reply.getNickname().length(),(reply.getNickname()+" "+reply.getContent()).length()-1 , Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                            textView.setText(style);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            params.bottomMargin = Util.dip2px(TogetherDetailActivity.this,5);
                            textView.setLayoutParams(params);
                            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                            textView.setLineSpacing(0f,1.4f);
                            layoutCommentList.addView(textView);
                        }
                    }
                } else {
                    if(StringUtil.isEmpty(result.getMsg())){
                        showToastMsg("评论发表失败！");
                    }else{
                        showToastMsg(result.getMsg());
                    }
                }
            }

            @Override
            protected BaseResult run(Void... params) {
                return HttpRequestUtil.getInstance().publishTogetherComment(readPreference("token"), id, StringUtil.trimAll(etComment.getText().toString()));
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
//        oks.setNotification(R.drawable.ic_launcher,
//                getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(togetherDetailResult.getTogether().getComment() + "\n http://m.miaotu.com/ShareLine/?yid=" + togetherDetailResult.getTogether().getId());
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://m.miaotu.com/ShareLine/?yid=" + togetherDetailResult.getTogether().getId());
        // text是分享文本，所有平台都需要这个字段
        oks.setText(togetherDetailResult.getTogether().getComment() + "\n http://m.miaotu.com/ShareLine/?yid=" + togetherDetailResult.getTogether().getId());
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        if (togetherDetailResult.getTogether().getPicList() != null &&
                togetherDetailResult.getTogether().getPicList().size() > 0){

            oks.setImageUrl(togetherDetailResult.getTogether().getPicList().get(0).getUrl()
                    + "200x200");
        }
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://m.miaotu.com/ShareLine/?yid=" + togetherDetailResult.getTogether().getId());
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(togetherDetailResult.getTogether().getComment() + "\n http://m.miaotu.com/ShareLine/?yid=" + togetherDetailResult.getTogether().getId());
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://m.miaotu.com/ShareLine/?yid=" +togetherDetailResult.getTogether().getId());

        // 启动分享GUI
        oks.show(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1&&resultCode==1){
            togetherDetailResult.getTogether().setIsAddGroup(true);
            if(togetherDetailResult.getTogether().getJoinList()==null){
                togetherDetailResult.getTogether().setJoinList(new ArrayList<PersonInfo>());
            }
            PersonInfo personInfo = new PersonInfo();
            personInfo.setHeadurl(readPreference("headphoto"));
            personInfo.setUid(readPreference("uid"));
            togetherDetailResult.getTogether().getJoinList().add(0, personInfo);
            togetherDetailResult.getTogether().setJoinCount(
                    togetherDetailResult.getTogether().getJoinList().size()+"");
            writeDetail(togetherDetailResult);
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.tv_left:
                finish();
                break;
            case R.id.layout_joined:
                // 已参加的
                layoutJoinedPhotos.setVisibility(View.VISIBLE);
                layoutInterestPhotos.setVisibility(View.GONE);
                ivJoinedTriangle.setVisibility(View.VISIBLE);
                ivInterestTriangle.setVisibility(View.INVISIBLE);
                tvJoined.setTextColor(getResources().getColor(R.color.text_color));
                tvLiked.setTextColor(getResources().getColor(R.color.invite_status_refuse_me));
                layoutInterestPart.setVisibility(View.GONE);
                layoutJoinedPart.setVisibility(View.VISIBLE);
                break;
            case R.id.layout_interest:
                // 感兴趣的
                layoutJoinedPhotos.setVisibility(View.GONE);
                layoutInterestPhotos.setVisibility(View.VISIBLE);
                ivJoinedTriangle.setVisibility(View.INVISIBLE);
                ivInterestTriangle.setVisibility(View.VISIBLE);
                tvLiked.setTextColor(getResources().getColor(R.color.text_color));
                tvJoined.setTextColor(getResources().getColor(R.color.invite_status_refuse_me));
                layoutJoinedPart.setVisibility(View.GONE);
                layoutInterestPart.setVisibility(View.VISIBLE);
                break;
            case R.id.layout_like:
                // 感兴趣
                like();
                break;
//            case R.id.layout_joined_more:
//                // 参加列表
//                Intent intent = new Intent(TogetherDetailActivity.this, JoinedListActivity.class);
//                intent.putExtra("flag", "1");
//                intent.putExtra("yid", togetherDetailResult.getTogether().getId());
//                intent.putExtra("title", togetherDetailResult.getTogether().getStartDate() +
//                        "一起去" + togetherDetailResult.getTogether().getDesCity());
//                startActivity(intent);
//                break;
//            case R.id.layout_interest_more:
//                // 感兴趣列表
//                Intent intent1 = new Intent(TogetherDetailActivity.this, JoinedListActivity.class);
//                intent1.putExtra("flag", "1");
//                intent1.putExtra("yid", togetherDetailResult.getTogether().getId());
//                intent1.putExtra("title", togetherDetailResult.getTogether().getStartDate() +
//                        "一起去" + togetherDetailResult.getTogether().getDesCity());
//                startActivity(intent1);
//                break;
            case R.id.iv_head_photo:
                // 个人中心
                if(!Util.isNetworkConnected(TogetherDetailActivity.this)) {
                    showToastMsg("当前未联网，请检查网络设置");
                    return;
                }
                Intent userIntent = new Intent(TogetherDetailActivity.this,PersonCenterActivity.class);
                userIntent.putExtra("uid", togetherDetailResult.getTogether().getUid());
                startActivity(userIntent);
                break;
            case R.id.iv_chat:
                //私聊
                if(togetherDetailResult.getTogether().getUid().equals(readPreference("uid"))){
                    showToastMsg("不能和自己聊天！");
                    break;
                }
                Intent chatIntent = new Intent(TogetherDetailActivity.this, ChatsActivity.class);
                chatIntent.putExtra("chatType", ChatsActivity.CHATTYPE_SINGLE);
                chatIntent.putExtra("id", MD5.md5(togetherDetailResult.getTogether().getUid()));
                chatIntent.putExtra("uid", togetherDetailResult.getTogether().getUid());
                chatIntent.putExtra("name", togetherDetailResult.getTogether().getNickname());
                chatIntent.putExtra("headphoto", togetherDetailResult.getTogether().getHeadPhoto());
                startActivity(chatIntent);
                break;
            case R.id.iv_group_chat:
                //群聊
                if(togetherDetailResult.getTogether().isAddGroup()){
                    Intent groupChatIntent = new Intent(TogetherDetailActivity.this, ChatsActivity.class);
                    groupChatIntent.putExtra("groupImId", togetherDetailResult.getTogether().getGroupId());
                    groupChatIntent.putExtra("groupName", togetherDetailResult.getTogether().getGroupname());
                    groupChatIntent.putExtra("chatType", 2);
                    startActivity(groupChatIntent);
                }else{
                    showToastMsg("您还未报名此次活动，不能参与群聊！");
                }
                break;
            case R.id.iv_share:
                //分享
                showShare();
                break;
            case R.id.layout_all:
                //隐藏评论框
                layoutMenu.setVisibility(View.VISIBLE);
                layoutPublishComment.setVisibility(View.GONE);
                break;
            case R.id.layout_join:
                // 参加
                Intent joinIntent = new Intent(TogetherDetailActivity.this,JoinTogetherStep1.class);
                joinIntent.putExtra("together",together);
                startActivityForResult(joinIntent, 1);
                break;
            case R.id.layout_comment:
                // 评论
                layoutMenu.setVisibility(View.GONE);
                layoutPublishComment.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_publish_comment:
                //发表评论
                if(StringUtil.isBlank(etComment.getText().toString())){
                    layoutMenu.setVisibility(View.VISIBLE);
                    layoutPublishComment.setVisibility(View.GONE);
                }else{
                    publishComment();
                }
                break;
        }
    }
}
