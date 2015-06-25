package com.miaotu.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.miaotu.R;
import com.miaotu.model.Together;

public class JoinTogetherStep1 extends BaseActivity implements View.OnClickListener{
private TextView tvTitle,tvLable,tvLeft;
    private Together together;
    private Button btnNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_together_step1);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvLeft = (TextView) findViewById(R.id.tv_left);
        tvLable = (TextView) findViewById(R.id.tv_lable);
        btnNext = (Button) findViewById(R.id.btn_next);

        tvLeft.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        tvTitle.setText("报名");
        together = (Together) getIntent().getSerializableExtra("together");
        if (together == null){
            return;
        }
        SpannableStringBuilder style=new SpannableStringBuilder("您将报名由"+" "+together.getNickname()+" 发起的约游线路，请仔细阅读下文。");
        style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.grey64)), 0, 5, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.fbab4a)), 6, 6+together.getNickname().length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.grey64)), 7+together.getNickname().length(),together.getNickname().length()+23, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tvLable.setText(style);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1&&resultCode==1){
            setResult(1);
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_left:
                finish();
                break;
            case R.id.btn_next:
                Intent intent = new Intent(JoinTogetherStep1.this,JoinTogetherStep2.class);
                intent.putExtra("together",together);
                startActivityForResult(intent, 1);
                break;
        }
    }
}
