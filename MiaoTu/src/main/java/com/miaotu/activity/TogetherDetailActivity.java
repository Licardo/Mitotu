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
import com.miaotu.model.RegisterInfo;
import com.miaotu.model.Together;
import com.miaotu.model.TogetherReply;
import com.miaotu.result.BaseResult;
import com.miaotu.result.LoginResult;
import com.miaotu.result.TogetherDetailResult;
import com.miaotu.util.StringUtil;
import com.miaotu.util.Util;
import com.miaotu.view.CircleImageView;
import com.miaotu.view.FlowLayout;
import com.miaotu.view.GuideGallery;
import com.miaotu.view.MyGridView;
import com.umeng.analytics.MobclickAgent;

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
    private ImageView ivJoinedTriangle,ivInterestTriangle;
    private LinearLayout layoutLike,layoutComment,layoutJoin,layoutMenu,layoutCommentList;
    private boolean islike;
    private EditText etComment;
    private TextView tvPublishComment;

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

    }
    private void bindView(){
        layoutLiked.setOnClickListener(this);
        layoutJoined.setOnClickListener(this);
        layoutLike.setOnClickListener(this);
        layoutComment.setOnClickListener(this);
        layoutJoin.setOnClickListener(this);
        tvPublishComment.setOnClickListener(this);
        tvLeft.setOnClickListener(this);
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
        String[] tags = result.getTogether().getTags().split(",");
        for(String tag:tags){
            TextView textView = new TextView(this);
            textView.setTextColor(getResources().getColor(R.color.b4b4b4));
            textView.setText(tag);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundResource(R.drawable.bg_hottag);
            FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.rightMargin = Util.dip2px(this,10);
            textView.setLayoutParams(params);
            textView.setPadding(Util.dip2px(this, 10), Util.dip2px(this, 4), Util.dip2px(this, 10), Util.dip2px(this, 4));
            personalTag.addView(textView);
        }
        adapter= new ImageItemAdapter(this,result.getTogether().getPicList());
        gvPhotos.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        tvJoined.setText("报名 " + result.getTogether().getJoinCount());
        tvLiked.setText("喜欢 " + result.getTogether().getLikeCount());
        tvJoined.setTextColor(getResources().getColor(R.color.grey));

        tvStartDate.setText(result.getTogether().getStartDate());
        tvEndDate.setText(result.getTogether().getEndDate());
        tvDesCity.setText(result.getTogether().getDesCity());
        tvOrginCity.setText(result.getTogether().getOriginCity()+" "+result.getTogether().getOriginLocation());
        tvFee.setText(result.getTogether().getFee());
        tvRequirement.setText(result.getTogether().getRequirement());

        String[] tags1 = result.getTogether().getUserTag().split(",");
        for(String tag:tags1){
            TextView textView = new TextView(this);
            textView.setTextColor(getResources().getColor(R.color.b4b4b4));
            textView.setText(tag);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundResource(R.drawable.bg_hottag);
            FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.rightMargin = Util.dip2px(this,10);
            textView.setLayoutParams(params);
            textView.setPadding(Util.dip2px(this, 10), Util.dip2px(this, 4), Util.dip2px(this, 10), Util.dip2px(this, 4));
            togetherTag.addView(textView);
        }
        islike=result.getTogether().isLike();
        if(islike){
            ivLike.setBackgroundResource(R.drawable.icon_like);
        }else{
            ivLike.setBackgroundResource(R.drawable.icon_unlike);
        }
        if(result.getTogether().getLikeList()!=null) {
//            for (int i = 0; i < detail.getJoinUsers().size(); i++) {
//                Member m = detail.getJoinUsers().get(i);
//                CircleImageView imageView = new CircleImageView(
//                        MovementDetailActivity.this);
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                        Util.dip2px(this, 24),
//                        Util.dip2px(this, 24));
//                if (i == 0) {
//                    params.leftMargin = Util.dip2px(
//                            MovementDetailActivity.this, 10);
//                }
//                params.rightMargin = Util.dip2px(
//                        MovementDetailActivity.this, 10);
//                imageView.setLayoutParams(params);
//                imageView.setTag(m.getUserId());
//                imageView.setOnClickListener(new OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        // TODO Auto-generated method stub
//                        MobclickAgent.onEvent(MovementDetailActivity.this, "线路详情页-参加的人");
//                        Intent intent = new Intent(
//                                MovementDetailActivity.this,
//                                UserHomeActivity.class);
//                        intent.putExtra("userId", (String) v.getTag());
//                        startActivity(intent);
//                    }
//                });
//                layoutJoinedPhotos.addView(imageView);
//                UrlImageViewHelper.setUrlDrawable(
//                        imageView,
//
//                        m.getPhoto().getUrl() + "&size=100x100", R.drawable.icon_default_head_photo);
//            }
        }
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
                    }else{
                        showToastMsg("喜欢成功！");
                        ivLike.setBackgroundResource(R.drawable.icon_like);
                        islike=true;
                    }
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
            case R.id.layout_join:
                // 参加
                Intent joinIntent = new Intent(TogetherDetailActivity.this,JoinTogetherStep1.class);
                joinIntent.putExtra("together",together);
                startActivity(joinIntent);
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
