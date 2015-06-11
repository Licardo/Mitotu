package com.miaotu.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.exceptions.EaseMobException;
import com.miaotu.R;
import com.miaotu.imutil.ChatAllHistoryAdapter;
import com.miaotu.imutil.ContactInfo;
import com.miaotu.imutil.Conversation;
import com.miaotu.imutil.IMDatabaseHelper;
import com.miaotu.receiver.JPushReceiver;
import com.miaotu.util.LogUtil;
import com.miaotu.util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;


public class MessageActivity extends BaseActivity implements
        OnItemClickListener {
    private SwipeMenuListView lvConversation;
    private ChatAllHistoryAdapter conversationAdapter;
    private List<EMConversation> mList = null;
    private List<ContactInfo> contactList = null;
    private List<Conversation> conversationList;

    private BroadcastReceiver receiver;

    private IntentFilter filter;
    private boolean bRegisterReceiver = false;

    private TextView sysMessageNum, inviteNum, visitNum;
    private RelativeLayout layoutRecentInvite, layoutSystemMessage, layoutRecentVisit;
    private boolean hidden;
    private TextView tvTitle,tvLeft;
    private RelativeLayout layoutInviteNum, layoutSysMsgNum, layoutVisitNum;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        findView();
        bindView();
        init();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver();
        lvConversation = null;
        conversationAdapter = null;
        if (mList != null) {
            mList.clear();
            mList = null;
        }
        tvTitle = null;

        receiver = null;

        filter = null;

        sysMessageNum = null;
        inviteNum = null;
        layoutRecentInvite = null;
        layoutSystemMessage = null;

    }

    private void findView() {
        lvConversation = (SwipeMenuListView) findViewById(R.id.lv_conversation);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvLeft = (TextView) findViewById(R.id.tv_left);
    }

    private void bindView() {
        tvTitle.setVisibility(View.VISIBLE);
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mList = new ArrayList<EMConversation>();
        contactList = new ArrayList<ContactInfo>();
        conversationList = new ArrayList<Conversation>();
        conversationAdapter = new ChatAllHistoryAdapter(this, 1, conversationList, mList);
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(MessageActivity.this);
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                deleteItem.setWidth(Util.dip2px(MessageActivity.this, 90));
                deleteItem.setTitle("删除");
                deleteItem.setTitleColor(Color.WHITE);
                deleteItem.setTitleSize(16);

                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        lvConversation.setMenuCreator(creator);
        lvConversation.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                EMChatManager.getInstance().deleteConversation(mList.get(position).getUserName());
                mList.remove(position);
                conversationAdapter.notifyDataSetChanged();
                return false;
            }
        });

        // set SwipeListener
        lvConversation.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });
        lvConversation.setAdapter(conversationAdapter);
        lvConversation.setOnItemClickListener(this);
    }

    private void init() {
        tvTitle.setText("聊天中心");
        receiver = new MyReceiver();
        if (!bRegisterReceiver) {
            bRegisterReceiver = true;
            registerReceiver();
        }
    }

    /**
     * 注册广播
     */
    public void registerReceiver() {
    }

    /**
     * 解除注册广播
     */
    public void unregisterReceiver() {
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            LogUtil.d("messagefragment收到广播");
            abortBroadcast();
            String action = intent.getAction();
            if (JPushReceiver.ACTION_UPDATE_MESSAGE_UI.equals(action)) {
            } else if (JPushReceiver.ACTION_JPUSH_INVITE_MESSAGE_RECIEVE
                    .equals(action)) {
                // 收到旅行邀请消息
                LogUtil.d("收到邀请消息广播");
            } else if (JPushReceiver.ACTION_JPUSH_SYS_MESSAGE_RECIEVE
                    .equals(action)) {
                LogUtil.d("收到系统消息广播");
            } else if (EMChatManager.getInstance().getNewMessageBroadcastAction()
                    .equals(action)) {
                //收到im消息

                //消息id
                String msgId = intent.getStringExtra("msgid");
                //发消息的人的username(userid)
                String msgFrom = intent.getStringExtra("from");
                //消息类型，文本，图片，语音消息等,这里返回的值为msg.type.ordinal()。
                //所以消息type实际为是enum类型
                int msgType = intent.getIntExtra("type", 0);
                //更方便的方法是通过msgId直接获取整个message
                EMMessage message = EMChatManager.getInstance().getMessage(msgId);
                ContactInfo contactInfo = new ContactInfo();
                contactInfo.setImId(message.getFrom());
                try {
                    LogUtil.d("messagefragment收到IM消息" + "new message id:" + msgId + " from:" + message
                            .getStringAttribute("nick_name") + " type:" + msgType);
                    contactInfo.setUid(message.getStringAttribute("uid"));
                    contactInfo.setNickName(message.getStringAttribute("nick_name"));
                    contactInfo.setHeadPhoto(message.getStringAttribute("headphoto"));
                    IMDatabaseHelper imDatabaseHelper = new IMDatabaseHelper(MessageActivity.this);
                    imDatabaseHelper.saveContactInfo(contactInfo);
                } catch (EaseMobException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
//                    getUserInfo(message.getFrom());
                    return;
                }
                refresh();
            }
        }


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // TODO Auto-generated method stub
        if(position<0){
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("id", mList.get(position).getUserName());
        if (mList.get(position).getIsGroup()) {
            intent.putExtra("chatType", 2);
            intent.putExtra("groupImId", mList.get(position).getUserName());
        } else {
            intent.putExtra("chatType", 1);
            intent.putExtra("name", conversationList.get(position).getContactInfo().getNickName());
            intent.putExtra("uid", conversationList.get(position).getContactInfo().getUid());
            intent.putExtra("headphoto", conversationList.get(position).getContactInfo().getHeadPhoto());
        }

        intent.setClass(MessageActivity.this, ChatsActivity.class);
        startActivity(intent);
    }


    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!bRegisterReceiver) {
            registerReceiver();
        }
        if (!hidden) {
            refresh();
        }
    }

    /**
     * 刷新页面
     */
    public void refresh() {
        if (mList == null) {
            mList = new ArrayList<EMConversation>();
        } else {
            mList.clear();
        }
        mList.addAll(loadConversationsWithRecentChat());
        if (contactList == null) {
            contactList = new ArrayList<ContactInfo>();
        } else {
            contactList.clear();
        }
        contactList.addAll(loadContactList());
        if (conversationList == null) {
            conversationList = new ArrayList<Conversation>();
        } else {
            conversationList.clear();
        }
        for (EMConversation emConversation : mList) {
            boolean find = false;
            //如果没有获取到对方传过来的用户信息，则不显示这一条（废弃）
//            try {
//                if(StringUtil.isEmpty(emConversation.getLastMessage().getStringAttribute("uid"))){
//                    mList.remove(emConversation);
//                    continue;
//                }
//            } catch (EaseMobException e) {
//                e.printStackTrace();
//                mList.remove(emConversation);
//                continue;
//            }
            for (ContactInfo contactInfo : contactList) {
                if (contactInfo.getImId().equals(emConversation.getUserName())) {
                    Conversation c = new Conversation();
                    c.setEmConversation(emConversation);
                    c.setContactInfo(contactInfo);
                    conversationList.add(c);
                    find = true;
                    break;
                }
            }
            if (!find) {
                Conversation c = new Conversation();
                c.setEmConversation(emConversation);
                c.setContactInfo(new ContactInfo());
                conversationList.add(c);
            }
        }
        conversationAdapter.notifyDataSetChanged();
    }

    /**
     * 获取所有会话
     *
     * @param
     * @return
     */
    private List<EMConversation> loadConversationsWithRecentChat() {
        // 获取所有会话，包括陌生人
        Hashtable<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();
        List<EMConversation> list = new ArrayList<EMConversation>();
        // 过滤掉messages seize为0的conversation
        for (EMConversation conversation : conversations.values()) {
            if (conversation.getAllMessages().size() != 0)
                list.add(conversation);
        }
        // 排序
        sortConversationByLastChatTime(list);
        return list;
    }

    private List<ContactInfo> loadContactList() {
        List<ContactInfo> list = new ArrayList<ContactInfo>();
        IMDatabaseHelper imDatabaseHelper = new IMDatabaseHelper(MessageActivity.this);
        list = imDatabaseHelper.getAllContactInfo();
        return list;
    }

    /**
     * 根据最后一条消息的时间排序
     *
     * @param
     */
    private void sortConversationByLastChatTime(List<EMConversation> conversationList) {
        Collections.sort(conversationList, new Comparator<EMConversation>() {
            @Override
            public int compare(final EMConversation con1, final EMConversation con2) {

                EMMessage con2LastMessage = con2.getLastMessage();
                EMMessage con1LastMessage = con1.getLastMessage();
                if (con2LastMessage.getMsgTime() == con1LastMessage.getMsgTime()) {
                    return 0;
                } else if (con2LastMessage.getMsgTime() > con1LastMessage.getMsgTime()) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }


}
