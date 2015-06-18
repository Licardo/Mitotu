package com.miaotu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miaotu.R;

public class IntroduceActivity extends BaseActivity implements View.OnClickListener{

    private TextView tvLeft,tvTitle;
    private LinearLayout ll_function,ll_red,ll_schedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce);

        init();
    }

    private void init(){
        tvLeft = (TextView) findViewById(R.id.tv_left);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ll_function = (LinearLayout) findViewById(R.id.ll_function);
        ll_red = (LinearLayout) findViewById(R.id.ll_red);
        ll_schedule = (LinearLayout) findViewById(R.id.ll_schedule);
        tvTitle.setText("妙途3.0介绍");
        tvLeft.setOnClickListener(this);
        ll_function.setOnClickListener(this);
        ll_red.setOnClickListener(this);
        ll_schedule.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_left:
                finish();
                break;
            case R.id.ll_function:
                Intent funIntent = new Intent(IntroduceActivity.this, FunctionIntroduceActivity.class);
                startActivity(funIntent);
                break;
            case R.id.ll_red:
                Intent redIntent = new Intent(IntroduceActivity.this, RedPackageIntroduceActivity.class);
                startActivity(redIntent);
                break;
            case R.id.ll_schedule:
                Intent guideIntent = new Intent(IntroduceActivity.this, FirstGuideActivity.class);
                guideIntent.putExtra("flag", true);
                startActivity(guideIntent);
                break;
            default:
                break;
        }
    }
}
