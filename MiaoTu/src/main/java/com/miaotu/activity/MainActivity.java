package com.miaotu.activity;


import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.OnMessageNotifyListener;
import com.easemob.chat.OnNotificationClickListener;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.DateUtils;
import com.miaotu.R;
import com.miaotu.imutil.CommonUtils;
import com.miaotu.imutil.ContactInfo;
import com.miaotu.imutil.IMDatabaseHelper;
import com.miaotu.jpush.MessageDatabaseHelper;
import com.miaotu.receiver.ConnectionChangeReceiver;
import com.miaotu.receiver.JPushReceiver;
import com.miaotu.util.LogUtil;
import com.miaotu.util.MD5;
import com.miaotu.util.StringUtil;
import com.miaotu.util.Util;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;

public class MainActivity extends BaseFragmentActivity implements
        OnClickListener {
    private static MainActivity instance;
    private FirstPageFragment mTab01 ;
    private BBSTopicListFragment mTab02 ;
    private MessageFragment mTab03 ;
    private FourthPageFragment mTab04 ;

    private FragmentManager fragmentManager;

    private LinearLayout mTabBtnFirst;
    private LinearLayout mTabBtnCommunity;
    private LinearLayout mTabBtnMessage;
    private LinearLayout mTabBtnMine;
    private TextView ivMsgFlg;

    ConnectionChangeReceiver mConnectionChangeReceiver = new ConnectionChangeReceiver();
    private boolean inMsgFlag = false; //标识是否当前在消息页面

    private int curPage = 0;
    private int desPage = 0;

    public static MainActivity getInstance() {
        return instance;
    }

    public static void setInstance(MainActivity instance) {
        MainActivity.instance = instance;
    }

    private NewMessageBroadcastReceiver msgReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        instance = this;
        //以下四行 判断网络连接
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mConnectionChangeReceiver, filter);

        MobclickAgent.openActivityDurationTrack(false);
//		MobclickAgent.setDebugMode(true);
        UmengUpdateAgent.update(this);
//		getDeviceInfo(this);
        MobclickAgent.updateOnlineConfig(this);
        setContentView(R.layout.activity_main);
        initView();
        fragmentManager = getSupportFragmentManager();
        //恢复activity
//        if (savedInstanceState == null) {
////            FragmentTransaction t = fragmentManager.beginTransaction();
////            mFrag = new SampleListFragment();
////            t.replace(R.id.menu_frame, mFrag);
////            t.commit();
//            setTabSelection(0);
//        } else {
////            mFrag = (BaseFragment)fragmentManager.findFragmentById(R.id.menu_frame);
//        }

        ImageLoader.getInstance().init(
                new ImageLoaderConfiguration.Builder(getApplicationContext())
                        .memoryCacheExtraOptions(480, 800)//设置缓存图片时候的宽高最大值，默认为屏幕宽高
//                        .discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75)//设置硬盘缓存，默认格式jpeg，压缩质量70
                        .threadPoolSize(5)  //设置线程池的最高线程数量
                        .threadPriority(Thread.NORM_PRIORITY)//设置线程优先级
                        .denyCacheImageMultipleSizesInMemory()//自动缩放
                        .memoryCache(new UsingFreqLimitedMemoryCache(4 * 1024 * 1024))//设置缓存大小，UsingFrgLimitMemoryCache类可以扩展
                        .imageDownloader(new BaseImageDownloader(this, 5000, 30000)).build());

        if (readPreference("login_state").equals("in")) {
            JPushInterface.setDebugMode(true);
            JPushInterface.setAliasAndTags(this, MD5.md5(readPreference("uid")), null);
            JPushInterface.init(getApplicationContext());
            JPushInterface.resumePush(getApplicationContext());

            initImReceiver();
            initTopicMessageReceiver();
//			JPushInterface.setDebugMode(true);

        }
        //注册一个监听连接状态的listener
        EMChatManager.getInstance().addConnectionListener(new MyConnectionListener());

        EMChatOptions chatOptions = EMChatManager.getInstance().getChatOptions();
        chatOptions.setNotifyBySoundAndVibrate(false);
        chatOptions.setNotificationEnable(false);
        chatOptions.setShowNotificationInBackgroud(true);
        chatOptions.setNotifyText(new OnMessageNotifyListener() {
            @Override
            public String onNewMessageNotify(EMMessage emMessage) {
                String text = "";
                try {
                    text= emMessage
                    .getStringAttribute("nick_name");
                } catch (EaseMobException e) {
                    e.printStackTrace();
                }
                String ticker = CommonUtils.getMessageDigest(emMessage, MainActivity.this);
                if(emMessage.getType() == EMMessage.Type.TXT)
                    ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
                text +=":"+ticker;
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
//                if(MainActivity.getInstance()!=null){
//                    MainActivity.getInstance().setTabSelection(3);
//                }
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("msg","true");
//                PendingIntent contentIntent = PendingIntent.getActivity(MainActivity.this, 0,  intent, PendingIntent.FLAG_CANCEL_CURRENT);
                return intent;
            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(!StringUtil.isEmpty(intent.getStringExtra("msg"))){
            setTabSelection(2);
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(mConnectionChangeReceiver);
        // 注销广播接收者
        try {
            unregisterReceiver(msgReceiver);
        } catch (Exception e) {
        }
        try {
            unregisterReceiver(ackMessageReceiver);
        } catch (Exception e) {
        }

        mTabBtnFirst = null;
        mTabBtnCommunity = null;
        mTabBtnMessage = null;
        mTabBtnMine = null;

    }


//    /**
//     * 刷新首页信息
//     * */
//    public void refreshFirstPageInfo(){
//        if (StringUtil.isEmpty(readPreference("movement_city"))) {
//            writePreference("movement_city", readPreference("located_city"));
//        }
//        mTab01.getFirstPageInfo(false);
//    }

    /**
     * 根据传入的index参数来设置选中的tab页。
     */
    @SuppressLint("NewApi")
    public void setTabSelection(int index) {
        // 重置按钮
        resetTabBtn();
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (index) {
            case 0:
                curPage = 0;
                // 当点击了消息tab时，改变控件的图片和文字颜色
                ((ImageView) mTabBtnFirst.findViewById(R.id.btn_tab_first))
                        .setImageResource(R.drawable.icon_tab_first_selected);
                ((TextView) mTabBtnFirst
                        .findViewById(R.id.tv_tab_first))
                        .setTextColor(getResources().getColor(
                                R.color.text_orange));
                if (mTab01 == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    mTab01 = new FirstPageFragment();
                    transaction.add(R.id.id_content, mTab01);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(mTab01);
                }
                break;
            case 1:
                curPage = 1;
                // 当点击了消息tab时，改变控件的图片和文字颜色
                ((ImageView) mTabBtnCommunity.findViewById(R.id.btn_tab_community))
                        .setImageResource(R.drawable.icon_tab_community_selected);
                ((TextView) mTabBtnCommunity
                        .findViewById(R.id.tv_tab_community))
                        .setTextColor(getResources().getColor(
                                R.color.text_orange));
                if (mTab02 == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    mTab02 = new BBSTopicListFragment();
                    transaction.add(R.id.id_content, mTab02);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(mTab02);
                }
                break;
            case 2:
                curPage = 2;
                ((TextView) mTabBtnMessage
                        .findViewById(R.id.tv_tab_message))
                        .setTextColor(getResources().getColor(
                                R.color.text_orange));
                // 当点击了动态tab时，改变控件的图片和文字颜色
                ((ImageView) mTabBtnMessage.findViewById(R.id.btn_tab_message))
                        .setImageResource(R.drawable.icon_tab_message_selected);
                ivMsgFlg.setVisibility(View.INVISIBLE);
                if (mTab03 == null) {
                    // 如果NewsFragment为空，则创建一个并添加到界面上
                    mTab03 = new MessageFragment();
                    transaction.add(R.id.id_content, mTab03);
                } else {
                    // 如果NewsFragment不为空，则直接将它显示出来
                    transaction.show(mTab03);
                    mTab03.refresh();
                }
                break;
            case 3:
                curPage = 3;
                // 当点击了设置tab时，改变控件的图片和文字颜色
                ((ImageView) mTabBtnMine.findViewById(R.id.btn_tab_mine))
                        .setImageResource(R.drawable.icon_tab_mine_selected);
                ((TextView) mTabBtnMine
                        .findViewById(R.id.tv_tab_mine))
                        .setTextColor(getResources().getColor(
                                R.color.text_orange));
                if (mTab04 == null) {
                    // 如果SettingFragment为空，则创建一个并添加到界面上
                    mTab04 = new FourthPageFragment();
                    transaction.add(R.id.id_content, mTab04);
                } else {
                    // 如果SettingFragment不为空，则直接将它显示出来
                    transaction.show(mTab04);
                }
                break;
        }
//        transaction.commit();
        transaction.commitAllowingStateLoss(); //为了解决换量页面返回时报的错误，详情见http://blog.csdn.net/ranxiedao/article/details/8214936
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    @SuppressLint("NewApi")
    private void hideFragments(FragmentTransaction transaction) {
        if (mTab01 != null) {
            transaction.hide(mTab01);
        }
        if (mTab02 != null) {
            transaction.hide(mTab02);
        }
        if (mTab03 != null) {
            transaction.hide(mTab03);
        }
        if (mTab04 != null) {
            transaction.hide(mTab04);
        }

    }

    protected void resetTabBtn() {
        ((ImageView) mTabBtnFirst.findViewById(R.id.btn_tab_first))
                .setImageResource(R.drawable.icon_tab_first_unselected);
        ((ImageView) mTabBtnCommunity
                .findViewById(R.id.btn_tab_community))
                .setImageResource(R.drawable.icon_tab_community_unselected);
        ((ImageView) mTabBtnMessage.findViewById(R.id.btn_tab_message))
                .setImageResource(R.drawable.icon_tab_message_unselected);
        ((ImageView) mTabBtnMine.findViewById(R.id.btn_tab_mine))
                .setImageResource(R.drawable.icon_tab_mine_unselected);
        ((TextView) mTabBtnFirst.findViewById(R.id.tv_tab_first))
                .setTextColor(getResources().getColor(R.color.line_grey));
        ((TextView) mTabBtnCommunity
                .findViewById(R.id.tv_tab_community))
                .setTextColor(getResources().getColor(R.color.line_grey));
        ((TextView) mTabBtnMessage.findViewById(R.id.tv_tab_message))
                .setTextColor(getResources().getColor(R.color.line_grey));
        ((TextView) mTabBtnMine.findViewById(R.id.tv_tab_mine))
                .setTextColor(getResources().getColor(R.color.line_grey));
    }

    private void initView() {

        mTabBtnFirst = (LinearLayout) findViewById(R.id.layout_tab_first);
        mTabBtnCommunity = (LinearLayout) findViewById(R.id.layout_tab_community);
        mTabBtnMessage = (LinearLayout) findViewById(R.id.layout_tab_message);
        mTabBtnMine = (LinearLayout) findViewById(R.id.layout_tab_mine);
        ivMsgFlg = (TextView) findViewById(R.id.icon_msg_flg);

        mTabBtnFirst.setOnClickListener(this);
        mTabBtnCommunity.setOnClickListener(this);
        mTabBtnMessage.setOnClickListener(this);
        mTabBtnMine.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (!readPreference("login_state").equals("in")
                && (v.getId() == R.id.layout_tab_message || v.getId() == R.id.layout_tab_mine)) {
            if (v.getId() == R.id.layout_tab_message) {
                desPage = 2;
            } else if (v.getId() == R.id.layout_tab_mine) {
                desPage = 3;
            }
            /*Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivityForResult(intent, 1);
            return;*/
        }
        resetTabBtn();
        switch (v.getId()) {
            case R.id.layout_tab_first:
                inMsgFlag = false;
                setTabSelection(0);
                break;
            case R.id.layout_tab_community:
                inMsgFlag = false;
                setTabSelection(1);
                break;
            case R.id.layout_tab_message:
                inMsgFlag = true;
                setTabSelection(2);
                break;
            case R.id.layout_tab_mine:
                inMsgFlag = false;
                setTabSelection(3);
                break;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
            setTabSelection(curPage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent arg2) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, arg2);
        if (requestCode == 1) {
            // 登陆之后
            if (resultCode == 1) {
                setTabSelection(desPage);
                JPushInterface.setAliasAndTags(this, readPreference("phone"), null);

                JPushInterface.init(getApplicationContext());
                JPushInterface.resumePush(getApplicationContext());

                initImReceiver();
                initTopicMessageReceiver();
            }
        }
        if (requestCode == 9) {
            // 注销之后
            if (resultCode == 1) {
                // 注销成功
                JPushInterface.stopPush(this);
                // 注销广播接收者
                try {
                    unregisterReceiver(topicReceiver);
                } catch (Exception e) {
                }
                try {
                    unregisterReceiver(msgReceiver);
                } catch (Exception e) {
                }
                try {
                    unregisterReceiver(ackMessageReceiver);
                } catch (Exception e) {
                }
            }
        }

    }

    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click(); // 调用双击退出函数
        }
        return false;
    }

    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
            System.exit(0);
        }
    }
    //社区回复提醒0
    private TopicMessageBroadcastReceiver topicReceiver;
    private void initTopicMessageReceiver() {
        topicReceiver = new TopicMessageBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(JPushReceiver.ACTION_JPUSH_TOPIC_MESSAGE_RECIEVE);
        registerReceiver(topicReceiver, intentFilter);
    }
    private class TopicMessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            abortBroadcast();
            //收到社区回复提醒
            if(mTab02 != null){
//                mTab02.refreshMessage();
            }
        }
    }
    private void initImReceiver() {
        //注册im的Receiver
        msgReceiver = new NewMessageBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
        intentFilter.setPriority(3);
        registerReceiver(msgReceiver, intentFilter);
        // 注册一个ack回执消息的BroadcastReceiver
        IntentFilter ackMessageIntentFilter = new IntentFilter(EMChatManager.getInstance().getAckMessageBroadcastAction());
        ackMessageIntentFilter.setPriority(3);
        registerReceiver(ackMessageReceiver, ackMessageIntentFilter);
        EMChat.getInstance().setAppInited();
        //注册系统消息的Receiver
        IntentFilter sysIntentFilter = new IntentFilter(JPushReceiver.ACTION_JPUSH_SYS_MESSAGE_RECIEVE);
        sysIntentFilter.setPriority(3);
        registerReceiver(sysMessageReceiver, sysIntentFilter);
    }

    private int getInviteMessageNum() {
        MessageDatabaseHelper helper = new MessageDatabaseHelper(this);
        int num = helper.getInviteMessageNum();
        return num;
    }

    private class NewMessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            abortBroadcast();
            String action = intent.getAction();
            LogUtil.d("Main收到IM消息");

            // 收到im消息
            // 消息id
            String msgId = intent.getStringExtra("msgid");
            // 发消息的人的username(userid)
            String msgFrom = intent.getStringExtra("from");
            // 消息类型，文本，图片，语音消息等,这里返回的值为msg.type.ordinal()。
            // 所以消息type实际为是enum类型
            int msgType = intent.getIntExtra("type", 0);
            // 更方便的方法是通过msgId直接获取整个message
            EMMessage message = EMChatManager.getInstance().getMessage(
                    msgId);
            ContactInfo contactInfo = new ContactInfo();
            contactInfo.setImId(message.getFrom());
            try {
                contactInfo.setUid(message.getStringAttribute("uid"));
                contactInfo.setNickName(message
                        .getStringAttribute("nick_name"));
                contactInfo.setHeadPhoto(message
                        .getStringAttribute("headphoto"));
                LogUtil.d("main收到IM消息" + "from:" + message
                        .getStringAttribute("nick_name")
                        + " type:" + msgType+"headphoto"+message
                        .getStringAttribute("headphoto"));
                IMDatabaseHelper imDatabaseHelper = new IMDatabaseHelper(
                        getApplicationContext());
                imDatabaseHelper.saveContactInfo(contactInfo);
            } catch (EaseMobException e) {
                // TODO Auto-generated catch block
                //如果拿不到对方传过来的昵称和头像，就去服务端获取//废弃
                e.printStackTrace();
                return;
            }
            String ticker = CommonUtils.getMessageDigest(message, MainActivity.this);
            if(message.getType() == EMMessage.Type.TXT)
                ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
            writePreference("im_content", ticker);

            writePreference("im_date",DateUtils.getTimestampString(new Date(message.getMsgTime())));
            remindUser("imMsg");

            return;
//
//            ///以下代码废弃
//            if (JPushReceiver.ACTION_JPUSH_INVITE_MESSAGE_RECIEVE
//                    .equals(action)) {
//                // 收到旅行邀请消息
//                LogUtil.d("Main收到邀请消息广播");
//            } else if (JPushReceiver.ACTION_JPUSH_SYS_MESSAGE_RECIEVE
//                    .equals(action)) {
//                LogUtil.d("Main收到系统消息广播");
//            } else if (EMChatManager.getInstance()
//                    .getNewMessageBroadcastAction().equals(action)) {
//            }
//
        }
    }

    /**
     * 消息回执BroadcastReceiver
     */
    private BroadcastReceiver ackMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            abortBroadcast();

            String msgid = intent.getStringExtra("msgid");
            String from = intent.getStringExtra("from");

            EMConversation conversation = EMChatManager.getInstance().getConversation(from);
            if (conversation != null) {
                // 把message设为已读
                EMMessage msg = conversation.getMessage(msgid);

                if (msg != null) {

                    // 2014-11-5 修复在某些机器上，在聊天页面对方发送已读回执时不立即显示已读的bug
                    if (ChatsActivity.activityInstance != null) {
                        if (msg.getChatType() == ChatType.Chat) {
                            if (from.equals(ChatsActivity.activityInstance.getToChatUsername()))
                                return;
                        }
                    }

                    msg.isAcked = true;
                }
            }

        }
    };
    /**
     * 系统消息BroadcastReceiver
     */
    private BroadcastReceiver sysMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            abortBroadcast();
            remindUser("sysMsg");
        }
    };

    private int getLikeMessageNum() {
        MessageDatabaseHelper helper = new MessageDatabaseHelper(this);
        int num = helper.getAllLikeMessage().size();
        return num;
    }

    /**
     * 用声音和振动提醒用户，设置消息页面下标
     */
    private void remindUser(String type){
        if ("open".equals(readPreference("no_disturb")) && Util.isInLimitTime()) {
            // 如果开启免打扰并且当前时间是23:00以后09:00之前，没有声音或震动提示
        } else {
            if(type.equals("imMsg")){
                if ("close".equals(readPreference("privacy_remind_msg"))) { //im消息
                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    Notification notification = new Notification();
                    notification.vibrate = new long[]{0, 200, 100, 200, 100, 200};    // 数组是以毫秒为单位的暂停、震动、暂停……时间
                    notificationManager.notify(R.string.app_name, notification);
                } else {
                    MediaPlayer player = MediaPlayer.create(MainActivity.this, R.raw.ring);
                    player.start();
                }
            }else
            if(type.equals("sysMsg")){
                if ("close".equals(readPreference("each_attention_msg"))) { //系统消息
                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    Notification notification = new Notification();
                    notification.vibrate = new long[]{0, 200, 100, 200, 100, 200};    // 数组是以毫秒为单位的暂停、震动、暂停……时间
                    notificationManager.notify(R.string.app_name, notification);
                } else {
                    MediaPlayer player = MediaPlayer.create(MainActivity.this, R.raw.ring);
                    player.start();
                }
            }

        }
        if (inMsgFlag) {
            if(mTab03 != null){
                mTab03.refresh();
            }
        } else {
            int count = 0;
            count += getInviteMessageNum();
            count += getLikeMessageNum();
            count += EMChatManager.getInstance().getUnreadMsgsCount();
            count +=Integer.parseInt(readPreference("tour_like_count"));
            count +=Integer.parseInt(readPreference("tour_join_count"));
            if (count > 99) {
                ivMsgFlg.setText("99+");
            } else {
                ivMsgFlg.setText("" + count);
            }
            ivMsgFlg.setVisibility(View.VISIBLE);
        }
    }

    //实现ConnectionListener接口
    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
        }

        @Override
        public void onDisconnected(final int error) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (error == EMError.CONNECTION_CONFLICT) {
                        // 显示帐号在其他设备登陆
                        showToastMsg("您的账户已经在其他设备登陆，请重新登陆！");
                        writePreference("login_state", "out");
                    } else {
                        //"连接不到聊天服务器"
                    }
                }
            });
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }
}