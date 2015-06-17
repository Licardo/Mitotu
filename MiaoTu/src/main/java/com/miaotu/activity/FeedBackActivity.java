package com.miaotu.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.miaotu.R;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.result.BaseResult;
import com.miaotu.util.StringUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FeedBackActivity extends BaseActivity implements View.OnClickListener{

    private TextView tvCall,tvLeft,tvTitle;
    private EditText etContent,etEmail;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        initView();
        bindVew();
    }

    private void initView(){
        tvLeft = (TextView) this.findViewById(R.id.tv_left);
        tvTitle = (TextView) this.findViewById(R.id.tv_title);
        etContent = (EditText) this.findViewById(R.id.et_content);
        etEmail = (EditText) this.findViewById(R.id.et_email);
        btnSubmit = (Button) this.findViewById(R.id.btn_submit);
        tvCall = (TextView) this.findViewById(R.id.tv_call);
        SpannableString sp = new SpannableString("联系妙途");
        sp.setSpan(new URLSpan(""), 0, sp.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_href)),
                0, sp.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tvCall.setText(sp);
        tvTitle.setText("帮助与反馈");
    }

    private void bindVew(){
        btnSubmit.setOnClickListener(this);
        tvCall.setOnClickListener(this);
        tvLeft.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_call:
                Intent aboutIntent = new Intent(FeedBackActivity.this, AboutActivity.class);
                startActivity(aboutIntent);
                break;
            case R.id.btn_submit:
                String email = etEmail.getText().toString().trim();
                String content = etContent.getText().toString().trim();
                if (StringUtil.isBlank(email)){
                    showToastMsg("请输入邮箱");
                    return;
                }
                if(!isEmail(email)){
                    showToastMsg("邮箱格式不正确");
                    return;
                }
                feedBack(email,content);
                break;
            case R.id.tv_left:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 意见反馈
     * @param email
     * @param content
     */
    private void feedBack(final String email, final String content){
        new BaseHttpAsyncTask<Void,Void,BaseResult>(this, false){

            @Override
            protected void onCompleteTask(BaseResult baseResult) {
                if (baseResult.getCode() == BaseResult.SUCCESS){
                    showToastMsg(baseResult.getMsg());
                    etEmail.setText("");
                    etContent.setText("");
                }else {
                    if (StringUtil.isBlank(baseResult.getMsg())){
                        showToastMsg("发送失败");
                    }else {
                        showToastMsg(baseResult.getMsg());
                    }
                }
            }

            @Override
            protected BaseResult run(Void... params) {
                return HttpRequestUtil.getInstance().feedBack(email, content);
            }
        }.execute();
    }

    //判断email格式是否正确
    public boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }
}
