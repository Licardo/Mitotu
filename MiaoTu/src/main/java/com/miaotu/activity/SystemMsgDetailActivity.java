package com.miaotu.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.miaotu.R;

public class SystemMsgDetailActivity extends BaseActivity implements View.OnClickListener{

    private TextView tvTitle,tvLeft;
    private TextView tvContent,tvDate,tvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_msg_detail);

        initView();
        bindView();
        initData();
    }

    private void initView(){
        tvContent = (TextView) this.findViewById(R.id.tv_content);
        tvDate = (TextView) this.findViewById(R.id.tv_date);
        tvLeft = (TextView) this.findViewById(R.id.tv_left);
        tvName = (TextView) this.findViewById(R.id.tv_name);
        tvTitle = (TextView) this.findViewById(R.id.tv_title);
    }

    private void bindView(){
        tvLeft.setOnClickListener(this);
    }

    private void initData(){
        tvTitle.setText("系统消息");
        tvName.setText(getIntent().getStringExtra("msgtitle"));
        tvDate.setText(getIntent().getStringExtra("msgdate"));
        tvContent.setText(getIntent().getStringExtra("msgcontent"));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_left:
                finish();
                break;
        }
    }
}
