package com.miaotu.activity;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.miaotu.R;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.model.PersonInfoResult;
import com.miaotu.result.BaseResult;
import com.miaotu.util.StringUtil;
import com.miaotu.view.FlowLayout;

import static android.view.View.*;

public class PersonCenterActivity extends BaseActivity implements OnClickListener {

    private TextView tv_username,tv_identity,tv_content_gender,tv_content_age,
            tv_content_address,tv_content_emotion,tv_content_job,tv_content_wantgo,
            tv_left,tv_title,tv_right;
    private FlowLayout fl_tag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_center);

        initView();
        initData();
    }

    private void initView(){
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
        fl_tag = (FlowLayout) this.findViewById(R.id.fl_tag);

        View view = this.findViewById(R.id.title);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_left = (TextView) view.findViewById(R.id.tv_left);
        tv_right = (TextView) view.findViewById(R.id.tv_right);
        tv_right.setVisibility(GONE);
        tv_title.setText("个人主页");
//        tv_left.setOnClickListener(this);
    }

    /**
     * 初始化接口返回个人信息的控件
     * @param personInfoResult
     */
    private void initPersonInfoData(PersonInfoResult personInfoResult){
        tv_content_address.setText(personInfoResult.getAddress());
        tv_content_age.setText(personInfoResult.getAge());
        tv_content_emotion.setText(personInfoResult.getMaritalstatus());
        tv_content_gender.setText(personInfoResult.getGender());
        tv_content_job.setText(personInfoResult.getWork());
        tv_content_wantgo.setText(personInfoResult.getWantgo());
        tv_identity.setText(personInfoResult.getWork());
        tv_username.setText(personInfoResult.getNickname());

        personInfoResult.setTags("胭脂膏,爱搞笑");    //测试数据
        if(!StringUtil.isBlank(personInfoResult.getTags())){
            String[] tags = personInfoResult.getTags().split(",");
            for(String tag:tags){
                View view = LayoutInflater.from(this).inflate(R.layout.item_hottag, null);
                TextView tv_content = (TextView) view.findViewById(R.id.tv_tag);
                tv_content.setText(tag);
                fl_tag.addView(view);
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
