package com.miaotu.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.miaotu.R;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.model.ModifyPersonInfo;
import com.miaotu.result.BaseResult;
import com.miaotu.util.LogUtil;
import com.miaotu.util.StringUtil;
import com.miaotu.util.Util;
import com.miaotu.view.FlowLayout;

import java.util.ArrayList;
import java.util.List;

public class EditUserInfoActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_left, tv_title, tv_right;
    private Button btn_add;
    private EditText et_tag;
    private FlowLayout fl_tags;
    private ModifyPersonInfo userinfo;
    private EditText et_nickname,et_gender,et_age,et_address,et_emotion,et_job,et_wantgo;
    private List<String> alltags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);

        initView();
        initData();
    }

    private void initView(){
        et_wantgo = (EditText) this.findViewById(R.id.et_wantgo);
        et_job = (EditText) this.findViewById(R.id.et_job);
        et_address = (EditText) this.findViewById(R.id.et_address);
        et_emotion = (EditText) this.findViewById(R.id.et_emotion);
        et_age = (EditText) this.findViewById(R.id.et_age);
        et_gender = (EditText) this.findViewById(R.id.et_gender);
        et_nickname = (EditText) this.findViewById(R.id.et_nickname);
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_left = (TextView) this.findViewById(R.id.tv_left);
        tv_right = (TextView) this.findViewById(R.id.tv_right);
        btn_add = (Button) this.findViewById(R.id.btn_add);
        et_tag = (EditText) this.findViewById(R.id.et_tag);
        fl_tags = (FlowLayout) this.findViewById(R.id.fl_tags);
        tv_right.setText("完成");
        tv_title.setText("编辑个人资料");
        tv_left.setOnClickListener(this);
        tv_right.setOnClickListener(this);
        btn_add.setOnClickListener(this);
    }

    private void initData(){
        userinfo = new ModifyPersonInfo();
        alltags = new ArrayList<String>();
    }

    private int position;

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.tv_left:
                finish();
                break;
            case R.id.btn_add:
                String content = et_tag.getText().toString().trim();
                if(!StringUtil.isBlank(content)){
                    final View tagview = LayoutInflater.from(EditUserInfoActivity.this).inflate(
                            R.layout.item_tag, null);
                    TextView tv_tag = (TextView) tagview.findViewById(R.id.tv_tag);
                    final ImageView iv_del = (ImageView) tagview.findViewById(R.id.iv_tag);
                    tv_tag.setText(content);
                    tagview.setTag(position);
                    alltags.add(content);
                    tagview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            fl_tags.removeView(tagview);
                            fl_tags.requestLayout();
                            int pos = (int) tagview.getTag();
                            if(pos < alltags.size()){
                                alltags.remove(pos);
                            }
                        }
                    });
                    FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT,
                            FlowLayout.LayoutParams.WRAP_CONTENT);
                    params.bottomMargin = Util.dip2px(EditUserInfoActivity.this, 10);
                    params.rightMargin = Util.dip2px(EditUserInfoActivity.this, 10);
                    tagview.setLayoutParams(params);
                    fl_tags.addView(tagview);
                    fl_tags.requestLayout();
                    et_tag.setText("");
                    position++;
                }
                break;
            case R.id.tv_right:
                String token = readPreference("token");
                userinfo.setToken(token);
                userinfo.setNickname(et_nickname.getText().toString().trim());
                userinfo.setGender(et_gender.getText().toString().trim());
                userinfo.setAge(et_age.getText().toString().trim());
                userinfo.setAddress(et_address.getText().toString().trim());
                userinfo.setMarital_status(et_emotion.getText().toString().trim());
                userinfo.setWork(et_job.getText().toString().trim());
                userinfo.setWant_go(et_wantgo.getText().toString().trim());
                String contenttag = "";
                for(String tag:alltags){
                    contenttag += tag + ",";
                }
                if(!StringUtil.isBlank(contenttag)){
                    userinfo.setTags(contenttag.substring(0, contenttag.length()-1));
                }
                modifyUserInfo(userinfo);
                LogUtil.e("Error", userinfo.getTags());
                break;
            default:
                break;
        }
    }

    /**
     * 修改用户信息
     */
    private void modifyUserInfo(final ModifyPersonInfo info){
        new BaseHttpAsyncTask<Void, Void, BaseResult>(this, true){

            @Override
            protected void onCompleteTask(BaseResult baseResult) {
                if(baseResult.getCode() == BaseResult.SUCCESS){
                    showToastMsg("修改成功");
                }else{
                    if(StringUtil.isBlank(baseResult.getMsg())){
                        showToastMsg("修改信息失败");
                    }else{
                        showToastMsg(baseResult.getMsg());
                    }
                }
            }

            @Override
            protected BaseResult run(Void... params) {
                return HttpRequestUtil.getInstance().modifyPersonInfo(info);
            }
        }.execute();
    }

}
