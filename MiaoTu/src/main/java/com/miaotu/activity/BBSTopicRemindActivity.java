package com.miaotu.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMMessage;
import com.easemob.chat.OnMessageNotifyListener;
import com.easemob.chat.OnNotificationClickListener;
import com.easemob.exceptions.EaseMobException;
import com.miaotu.R;
import com.miaotu.imutil.CommonUtils;

import cn.jpush.android.api.JPushInterface;

public class BBSTopicRemindActivity extends BaseActivity implements View.OnClickListener{

    private ImageView iv_msgremind, iv_receptmsg;
    private boolean isSelected1,isSelected2;
    private TextView tvLeft, tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message_remind);

        initView();
        initData();
    }

    private void initView(){
        iv_msgremind = (ImageView) this.findViewById(R.id.iv_msgremind);
        iv_receptmsg = (ImageView) this.findViewById(R.id.iv_receptmsg);
        tvLeft = (TextView) this.findViewById(R.id.tv_left);
        tvTitle = (TextView) this.findViewById(R.id.tv_title);
        iv_msgremind.setOnClickListener(this);
        iv_receptmsg.setOnClickListener(this);
        tvLeft.setOnClickListener(this);
        tvTitle.setText("新消息通知提醒");
    }

    private void initData(){
        isSelected1 = true;
        if ("off".equals(readPreference("msgnotification"))){
            isSelected2 = true;
        }else {
            isSelected2 = false;
        }
        if ("off".equals(readPreference("msgdetail"))){
            isSelected1 = true;
        }else {
            isSelected1 = false;
        }
        onClick(iv_receptmsg);
        onClick(iv_msgremind);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_msgremind:
                if(!isSelected1){
                    writePreference("msgdetail","on");
                    iv_msgremind.setBackgroundResource(R.drawable.icon_open);
                    EMChatOptions chatOptions = EMChatManager.getInstance().getChatOptions();
                    chatOptions.setNotifyBySoundAndVibrate(false);
                    chatOptions.setNotificationEnable(false);
                    chatOptions.setShowNotificationInBackgroud(true);
                    chatOptions.setNotifyText(new OnMessageNotifyListener() {
                        @Override
                        public String onNewMessageNotify(EMMessage emMessage) {
                            String text = "";
                            try {
                                text = emMessage
                                        .getStringAttribute("nick_name");
                            } catch (EaseMobException e) {
                                e.printStackTrace();
                            }
                            String ticker = CommonUtils.getMessageDigest(emMessage, BBSTopicRemindActivity.this);
                            if (emMessage.getType() == EMMessage.Type.TXT)
                                ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
                            text += ":" + ticker;
                            return text;
                        }

                        @Override
                        public String onLatestMessageNotify(EMMessage emMessage, int i, int i2) {
                            return null;
                        }

                        @Override
                        public String onSetNotificationTitle(EMMessage emMessage) {
                            return null;
                        }

                        @Override
                        public int onSetSmallIcon(EMMessage emMessage) {
                            return 0;
                        }
                    });
                    chatOptions.setOnNotificationClickListener(new OnNotificationClickListener() {
                        @Override
                        public Intent onNotificationClick(EMMessage emMessage) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("msg", "true");
                            return intent;
                        }
                    });
                }else {
                    writePreference("msgdetail","off");
                    iv_msgremind.setBackgroundResource(R.drawable.icon_close);
                    EMChatOptions chatOptions = EMChatManager.getInstance().getChatOptions();
                    chatOptions.setNotifyBySoundAndVibrate(false);
                    chatOptions.setNotificationEnable(false);
                    chatOptions.setShowNotificationInBackgroud(true);
                    chatOptions.setNotifyText(new OnMessageNotifyListener() {
                        @Override
                        public String onNewMessageNotify(EMMessage emMessage) {
                            String text = "妙途发来一条消息";
                            return text;
                        }

                        @Override
                        public String onLatestMessageNotify(EMMessage emMessage, int i, int i2) {
                            return null;
                        }

                        @Override
                        public String onSetNotificationTitle(EMMessage emMessage) {
                            return null;
                        }

                        @Override
                        public int onSetSmallIcon(EMMessage emMessage) {
                            return 0;
                        }
                    });
                    chatOptions.setOnNotificationClickListener(new OnNotificationClickListener() {
                        @Override
                        public Intent onNotificationClick(EMMessage emMessage) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("msg", "true");
                            return intent;
                        }
                    });
                }
                isSelected1 = !isSelected1;
                break;
            case R.id.iv_receptmsg:
                if(!isSelected2){
                    iv_receptmsg.setBackgroundResource(R.drawable.icon_open);
                    writePreference("msgnotification", "on");
                }else {
                    iv_receptmsg.setBackgroundResource(R.drawable.icon_close);
                    writePreference("msgnotification", "off");
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
