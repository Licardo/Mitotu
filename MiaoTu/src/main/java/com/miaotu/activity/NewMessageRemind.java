package com.miaotu.activity;

import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.miaotu.R;

public class NewMessageRemind extends BaseActivity implements View.OnClickListener{

    private ImageView iv_msgremind;
    private boolean isSelected;
    private TextView tvLeft, tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message_remind);

        initView();
    }

    private void initView(){
        iv_msgremind = (ImageView) this.findViewById(R.id.iv_msgremind);
        tvLeft = (TextView) this.findViewById(R.id.tv_left);
        tvTitle = (TextView) this.findViewById(R.id.tv_title);
        iv_msgremind.setOnClickListener(this);
        tvLeft.setOnClickListener(this);
        tvTitle.setText("新消息通知提醒");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_msgremind:
                if(isSelected){
                    iv_msgremind.setBackgroundResource(R.drawable.icon_open);
                }else {
                    iv_msgremind.setBackgroundResource(R.drawable.icon_close);
                }
                isSelected = !isSelected;
                break;
            case R.id.tv_left:
                finish();
                break;
            default:
                break;
        }
    }
}
