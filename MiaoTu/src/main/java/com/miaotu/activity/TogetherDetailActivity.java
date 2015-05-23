package com.miaotu.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
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
import com.miaotu.model.RegisterInfo;
import com.miaotu.result.BaseResult;
import com.miaotu.result.LoginResult;
import com.miaotu.result.TogetherDetailResult;
import com.miaotu.util.StringUtil;
import com.miaotu.util.Util;
import com.miaotu.view.CircleImageView;
import com.miaotu.view.FlowLayout;
import com.miaotu.view.MyGridView;

public class TogetherDetailActivity extends BaseActivity implements View.OnClickListener{
private String id;
    private TextView tvTitle,tvName,tvAge,tvJob,tvComment;
    private CircleImageView ivHeadPhoto;
    private ImageView ivGender;
    private FlowLayout personalTag,togetherTag;
    private MyGridView gvPhotos;
    private ImageItemAdapter adapter;
    private TextView tvJoined,tvLiked,tvStartDate,tvEndDate,tvDesCity,tvOrginCity,tvFee,tvRequirement;
    private RelativeLayout layoutJoined,layoutLiked;
    private LinearLayout layoutJoinedPhotos,layoutInterestPhotos,layoutInterestPart,layoutJoinedPart;
    private ImageView ivJoinedTriangle,ivInterestTriangle;
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
    }
    private void bindView(){
        layoutLiked.setOnClickListener(this);
        layoutJoined.setOnClickListener(this);
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
//            for (int i = 0; i < detail.getInterestUsers().size(); i++) {
//                Member m = detail.getInterestUsers().get(i);
//                CircleImageView imageView = new CircleImageView(
//                        MovementDetailActivity.this);
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                        Util.dip2px(MovementDetailActivity.this, 24),
//                        Util.dip2px(MovementDetailActivity.this, 24));
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
//                        MobclickAgent.onEvent(MovementDetailActivity.this, "线路详情页-感兴趣的人");
//                        Intent intent = new Intent(
//                                MovementDetailActivity.this,
//                                UserHomeActivity.class);
//                        intent.putExtra("userId", (String) v.getTag());
//                        startActivity(intent);
//                    }
//                });
//                layoutInterestPhotos.addView(imageView);
//                UrlImageViewHelper.setUrlDrawable(
//                        imageView,
//                        m.getPhoto().getUrl() + "&size=100x100", R.drawable.icon_default_head_photo);
//            }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.tv_left:
                finish();
                break;
            case R.id.layout_joined:
                // 已参加
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
                // 感兴趣
                layoutJoinedPhotos.setVisibility(View.GONE);
                layoutInterestPhotos.setVisibility(View.VISIBLE);
                ivJoinedTriangle.setVisibility(View.INVISIBLE);
                ivInterestTriangle.setVisibility(View.VISIBLE);
                tvLiked.setTextColor(getResources().getColor(R.color.text_color));
                tvJoined.setTextColor(getResources().getColor(R.color.invite_status_refuse_me));
                layoutJoinedPart.setVisibility(View.GONE);
                layoutInterestPart.setVisibility(View.VISIBLE);
                break;
        }
    }
}
