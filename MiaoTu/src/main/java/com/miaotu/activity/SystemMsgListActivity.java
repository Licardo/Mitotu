package com.miaotu.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.miaotu.R;

public class SystemMsgListActivity extends BaseActivity implements View.OnClickListener{
private TextView tvTitle,tvLeft;
    private SwipeMenuListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_msg_list);
        findView();
        bindView();
        init();
    }
    private void findView(){
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvLeft = (TextView) findViewById(R.id.tv_left);
        lv = (SwipeMenuListView) findViewById(R.id.lv);
    }
    private void bindView(){
        tvLeft.setOnClickListener(this);
    }
    private void init(){
        tvTitle.setText("系统消息");
        writePreference("sys_count","0");
        MessageFragment.getInstance().refresh();
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
