package com.miaotu.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miaotu.R;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.result.PersonInfoResult;
import com.miaotu.result.BaseResult;
import com.miaotu.util.StringUtil;
import com.miaotu.util.Util;
import com.miaotu.view.FlowLayout;

public class PersonCenterActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_username,tv_identity,tv_content_gender,tv_content_age,
            tv_content_address,tv_content_emotion,tv_content_job,tv_content_wantgo,
            tv_title,tv_left,tv_right,tv_top_emotion,tv_top_wantgo;
    private FlowLayout fl_tag;
    private ImageView iv_gender;
    private View view7;
    private LinearLayout ll_tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_center);

        initView();
        initData();
    }

    private void initView(){
        view7 = this.findViewById(R.id.view7);
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
    }

    /**
     * 初始化接口返回个人信息的控件
     * @param personInfoResult
     */
    private void initPersonInfoData(PersonInfoResult personInfoResult){
        tv_content_address.setText(personInfoResult.getPersonInfo().getAddress());
        tv_content_age.setText(personInfoResult.getPersonInfo().getAge());
        tv_content_emotion.setText(personInfoResult.getPersonInfo().getMaritalstatus());
        tv_top_emotion.setText(personInfoResult.getPersonInfo().getMaritalstatus());
        tv_content_gender.setText(personInfoResult.getPersonInfo().getGender());
        tv_content_job.setText(personInfoResult.getPersonInfo().getWork());
        tv_content_wantgo.setText(personInfoResult.getPersonInfo().getWantgo());
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
            }
        }
    }

    private void initData(){
        String token = readPreference("token");
        String uid = readPreference("uid");
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
            default:
                break;
        }
    }
}
