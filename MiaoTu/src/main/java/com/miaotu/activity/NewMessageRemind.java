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

    private ImageView iv_msgremind, iv_receptmsg;
    private boolean isSelected1,isSelected2;
    private TextView tvLeft, tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message_remind);

        initView();
    }

    private void initView(){
        iv_msgremind = (ImageView) this.findViewById(R.id.iv_msgremind);
        iv_receptmsg = (ImageView) this.findViewById(R.id.iv_receptmsg);
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
                if(!isSelected1){
                    iv_msgremind.setBackgroundResource(R.drawable.icon_open);
                }else {
                    iv_msgremind.setBackgroundResource(R.drawable.icon_close);
                }
                isSelected1 = !isSelected1;
                break;
            case R.id.iv_receptmsg:
                if(!isSelected2){
                    iv_msgremind.setBackgroundResource(R.drawable.icon_open);
                }else {
                    iv_msgremind.setBackgroundResource(R.drawable.icon_close);
                }
                isSelected2 = !isSelected2;
                break;
            case R.id.tv_left:
                finish();
                break;
            default:
                break;
        }
    }
}
