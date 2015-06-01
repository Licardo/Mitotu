package com.miaotu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.miaotu.R;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.result.LikeResult;
import com.miaotu.result.PersonInfoResult;
import com.miaotu.result.BaseResult;
import com.miaotu.util.MD5;
import com.miaotu.util.StringUtil;
import com.miaotu.util.Util;
import com.miaotu.view.CircleImageView;
import com.miaotu.view.FlowLayout;

public class PersonCenterActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_username,tv_identity,tv_content_gender,tv_content_age,
            tv_content_address,tv_content_emotion,tv_content_job,tv_content_wantgo,
            tv_title,tv_left,tv_right,tv_top_emotion,tv_top_wantgo;
    private FlowLayout fl_tag;
    private ImageView iv_gender;
    private View view7,view_bottom;
    private LinearLayout ll_tag;
    private RelativeLayout rl_gender,rl_age,rl_address,rl_emotion,rl_job,rl_wantgo;
    private CircleImageView ci_userhead;
    private TextView tv_start,tv_sign,tv_like,tv_trends,tv_tip_trends;
    private RelativeLayout rl_follow,rl_chating,rl_bottom;
    private TextView tv_follow;
    private ImageView iv_follow;
    private PersonInfoResult result;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_center);

        initView();
        initData();
    }

    private void initView(){
        tv_follow = (TextView) this.findViewById(R.id.tv_follow);
        iv_follow = (ImageView) this.findViewById(R.id.iv_follow);
        tv_start = (TextView) this.findViewById(R.id.tv_start);
        tv_sign = (TextView) this.findViewById(R.id.tv_sign);
        tv_like = (TextView) this.findViewById(R.id.tv_like);
        tv_trends = (TextView) this.findViewById(R.id.tv_trends);
        tv_tip_trends = (TextView) this.findViewById(R.id.tv_tip_trends);
        rl_follow = (RelativeLayout) this.findViewById(R.id.rl_follow);
        rl_chating = (RelativeLayout) this.findViewById(R.id.rl_chating);
        rl_bottom = (RelativeLayout) this.findViewById(R.id.rl_bottom);
        ci_userhead = (CircleImageView) this.findViewById(R.id.ci_userhead);
        rl_gender = (RelativeLayout) this.findViewById(R.id.rl_gender);
        rl_age = (RelativeLayout) this.findViewById(R.id.rl_age);
        rl_address = (RelativeLayout) this.findViewById(R.id.rl_address);
        rl_emotion = (RelativeLayout) this.findViewById(R.id.rl_emotion);
        rl_job = (RelativeLayout) this.findViewById(R.id.rl_job);
        rl_wantgo = (RelativeLayout) this.findViewById(R.id.rl_wantgo);
        view7 = this.findViewById(R.id.view7);
        view_bottom = this.findViewById(R.id.view_bottom);
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_left = (TextView) this.findViewById(R.id.tv_left);
        tv_right = (TextView) this.findViewById(R.id.tv_right);
        tv_content_address = (TextView) this.findViewById(R.id.tv_content_address);
        tv_identity = (TextView) this.findViewById(R.id.tv_identity);
        tv_content_gender = (TextView) this.findViewById(R.id.tv_content_gender);
        tv_content_age = (TextView) this.findViewById(R.id.tv_content_age);
        tv_content_emotion = (TextView) this.findViewById(R.id.tv_content_emotion);
        tv_content_job = (TextView) this.findViewById(R.id.tv_content_job);
        tv_content_wantgo = (TextView) this.findViewById(R.id.tv_content_wantgo);
        tv_username = (TextView) this.findViewById(R.id.tv_username);
        tv_top_emotion = (TextView) this.findViewById(R.id.tv_top_emotion);
        tv_top_wantgo = (TextView) this.findViewById(R.id.tv_top_wantgo);
        iv_gender = (ImageView) this.findViewById(R.id.iv_gender);
        fl_tag = (FlowLayout) this.findViewById(R.id.fl_tag);
        ll_tag = (LinearLayout) this.findViewById(R.id.ll_tag);

        tv_right.setVisibility(View.GONE);
        tv_title.setText("个人主页");
        tv_left.setOnClickListener(this);
        rl_chating.setOnClickListener(this);
        rl_follow.setOnClickListener(this);
    }

    /**
     * 初始化接口返回个人信息的控件
     * @param personInfoResult
     */
    private void initPersonInfoData(PersonInfoResult personInfoResult){
        if("true".equals(personInfoResult.getPersonInfo().getIslike())){    //是否关注此人
            changeBtnFollow(true);
        }
        UrlImageViewHelper.setUrlDrawable(ci_userhead, personInfoResult.getPersonInfo().getHeadurl(), R.drawable.icon_default_head_photo);
        tv_top_emotion.setText(personInfoResult.getPersonInfo().getMaritalstatus());
        if(!StringUtil.isBlank(personInfoResult.getPersonInfo().getAddress())){
            rl_address.setVisibility(View.VISIBLE);
            tv_content_address.setText(personInfoResult.getPersonInfo().getAddress());
        }
        if(!StringUtil.isBlank(personInfoResult.getPersonInfo().getAge())){
            rl_age.setVisibility(View.VISIBLE);
            tv_content_age.setText(personInfoResult.getPersonInfo().getAge());
        }
        if(!StringUtil.isBlank(personInfoResult.getPersonInfo().getMaritalstatus())){
            rl_emotion.setVisibility(View.VISIBLE);
            tv_content_emotion.setText(personInfoResult.getPersonInfo().getMaritalstatus());
        }
        if(!StringUtil.isBlank(personInfoResult.getPersonInfo().getGender())){
            rl_gender.setVisibility(View.VISIBLE);
            tv_content_gender.setText(personInfoResult.getPersonInfo().getGender());
        }
        if(!StringUtil.isBlank(personInfoResult.getPersonInfo().getWork())){
            rl_job.setVisibility(View.VISIBLE);
            tv_content_job.setText(personInfoResult.getPersonInfo().getWork());
        }
        if(!StringUtil.isBlank(personInfoResult.getPersonInfo().getWantgo())){
            rl_wantgo.setVisibility(View.VISIBLE);
            tv_content_wantgo.setText(personInfoResult.getPersonInfo().getWantgo());
        }
        tv_top_wantgo.setText(personInfoResult.getPersonInfo().getWantgo());
        tv_identity.setText(personInfoResult.getPersonInfo().getWork());
        tv_username.setText(personInfoResult.getPersonInfo().getNickname());
        if("男".equals(personInfoResult.getPersonInfo().getGender())){
            iv_gender.setBackgroundResource(R.drawable.mine_boy);
        }

//        personInfoResult.getPersonInfo().setTags("胭脂膏,爱搞笑,硬币,硬笔, 硬逼, 影壁, yingbi, 逮屁, 特使");    //测试数据
        if(!StringUtil.isBlank(personInfoResult.getPersonInfo().getTags())){
            String[] tags = personInfoResult.getPersonInfo().getTags().split(",");
            int count = 0;
            for(String tag:tags){
                View view = LayoutInflater.from(this).inflate(R.layout.item_hottag, null);
                TextView tv_content = (TextView) view.findViewById(R.id.tv_tag);
                tv_content.setText(tag);
                FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT,
                        FlowLayout.LayoutParams.WRAP_CONTENT);
                params.bottomMargin = Util.dip2px(PersonCenterActivity.this,10);
                params.rightMargin = Util.dip2px(PersonCenterActivity.this, 10);
                view.setLayoutParams(params);
                fl_tag.addView(view);
                fl_tag.requestLayout();
                count++;
            }
            if(count == 0){
                ll_tag.setVisibility(View.GONE);
                view_bottom.setVisibility(View.GONE);
            }
        }else {
            view_bottom.setVisibility(View.GONE);
            ll_tag.setVisibility(View.GONE);
        }
    }

    private void initData(){
        token = readPreference("token");
        String id = readPreference("uid");
        String uid = getIntent().getStringExtra("uid");
        if(id.equals(uid)){
            showMyPage(true);
        }
        readPersonInfo(token, uid);
    }

    /**
     * 获取用户信息
     * @param token
     * @param uid
     */
    private void readPersonInfo(final String token, final String uid){

        new BaseHttpAsyncTask<Void, Void, PersonInfoResult>(this, true){

            @Override
            protected void onCompleteTask(PersonInfoResult personInfoResult) {
                if(personInfoResult.getCode() == BaseResult.SUCCESS){
                    result = personInfoResult;
                    initPersonInfoData(personInfoResult);
                }else{
                    if(StringUtil.isEmpty(personInfoResult.getMsg())){
                        showToastMsg("获取用户信息失败");
                    }else{
                        showToastMsg(personInfoResult.getMsg());
                    }
                }
            }

            @Override
            protected PersonInfoResult run(Void... params) {
                return HttpRequestUtil.getInstance().getPersonInfo(token, uid);
            }
        }.execute();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_left:
                finish();
                break;
            case R.id.rl_chating:
                Intent chatIntent = new Intent(PersonCenterActivity.this, ChatsActivity.class);
                chatIntent.putExtra("chatType", ChatsActivity.CHATTYPE_SINGLE);
                chatIntent.putExtra("id", MD5.md5(result.getPersonInfo().getUid()));
                chatIntent.putExtra("uid", result.getPersonInfo().getUid());
                chatIntent.putExtra("name", result.getPersonInfo().getNickname());
                chatIntent.putExtra("headphoto", result.getPersonInfo().getHeadurl());
                startActivity(chatIntent);
                break;
            case R.id.rl_follow:
                like(token, result.getPersonInfo().getUid());
                break;
            default:
                break;
        }
    }

    /**
     * 自己看自己的个人信息
     * @param isShow
     */
    private void showMyPage(boolean isShow){
        if(isShow){
            rl_bottom.setVisibility(View.GONE);
            tv_start.setText("我发起的约游");
            tv_sign.setText("我报名的约游");
            tv_like.setText("我喜欢的约游");
            tv_trends.setText("我发布的动态");
            tv_tip_trends.setText("发布的动态");
        }
    }

    /**
     * 切换关注/取消关注按钮
     * @param isfollow
     */
    private void changeBtnFollow(boolean isfollow){
        if(isfollow){
            iv_follow.setVisibility(View.GONE);
            tv_follow.setText("取消关注");
            tv_follow.setTextColor(getResources().getColor(R.color.grey64));
        }else{
            iv_follow.setVisibility(View.VISIBLE);
            tv_follow.setText("关注");
            tv_follow.setTextColor(getResources().getColor(R.color.text_orange));
        }
    }

    /**
     * 添加/取消喜欢接口
     * @param token
     * @param touser
     */
    private void like(final String token, final String touser) {

        new BaseHttpAsyncTask<Void, Void, LikeResult>(this, false) {

            @Override
            protected void onCompleteTask(LikeResult baseResult) {
                if (baseResult.getCode() == BaseResult.SUCCESS) {
                    if("true".equals(result.getPersonInfo().getIslike())){
                        changeBtnFollow(false);
                        result.getPersonInfo().setIslike("false");
                    }else{
                        changeBtnFollow(true);
                        result.getPersonInfo().setIslike("true");
                    }
                    showToastMsg("操作成功");
                } else {
                    if (StringUtil.isBlank(baseResult.getMsg())) {
                        showToastMsg("操作失败");
                    } else {
                        showToastMsg(baseResult.getMsg());
                    }
                }
            }

            @Override
            protected LikeResult run(Void... params) {
                return HttpRequestUtil.getInstance().like(token, touser);
            }
        }.execute();
    }
}
