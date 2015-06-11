package com.miaotu.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.miaotu.R;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.form.PublishTogether;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.result.BaseResult;
import com.miaotu.util.LogUtil;
import com.miaotu.util.StringUtil;
import com.miaotu.util.Util;
import com.photoselector.model.PhotoModel;

import java.io.File;

public class PublishTogetherStep2Activity extends BaseActivity implements OnClickListener{
    PublishTogether publishTogether;
    private EditText etComment;
    private TextView tvPublish;
    private TextView tvTitle,tvLeft;
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
}
private void bindView(){
    tvPublish.setOnClickListener(this);
    tvLeft.setOnClickListener(this);
}
private void init(){
    publishTogether = (PublishTogether) getIntent().getSerializableExtra("publishTogether");
    tvTitle.setText("发起旅行");
}
private void publish(){
        new BaseHttpAsyncTask<Void, Void, BaseResult>(PublishTogetherStep2Activity.this, true) {
            @Override
            protected void onCompleteTask(BaseResult result) {
                if(tvPublish==null){
                    return;
                }
                if (result.getCode() == BaseResult.SUCCESS) {
                    showToastMsg("发布成功！");
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
            protected BaseResult run(Void... params) {
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
}
