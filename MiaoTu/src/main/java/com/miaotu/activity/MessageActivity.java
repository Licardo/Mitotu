//package com.miaotu.activity;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.Hashtable;
//import java.util.List;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.pm.ApplicationInfo;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.baoyz.swipemenulistview.SwipeMenu;
//import com.baoyz.swipemenulistview.SwipeMenuCreator;
//import com.baoyz.swipemenulistview.SwipeMenuItem;
//import com.baoyz.swipemenulistview.SwipeMenuListView;
//import com.easemob.chat.EMChatManager;
//import com.easemob.chat.EMConversation;
//import com.easemob.chat.EMMessage;
//import com.easemob.exceptions.EaseMobException;
//import com.miaotu.R;
//import com.miaotu.async.BaseHttpAsyncTask;
//import com.miaotu.http.HttpRequestUtil;
//import com.miaotu.imutil.ChatAllHistoryAdapter;
//import com.miaotu.imutil.ContactInfo;
//import com.miaotu.imutil.Conversation;
//import com.miaotu.imutil.IMDatabaseHelper;
//import com.miaotu.jpush.MessageDatabaseHelper;
//import com.miaotu.model.User;
//import com.miaotu.receiver.JPushReceiver;
//import com.miaotu.result.BaseResult;
//import com.miaotu.result.UserInfoResult;
//import com.miaotu.util.LogUtil;
//import com.miaotu.util.Util;
//import com.umeng.analytics.MobclickAgent;
//
//
//public class MessageActivity extends BaseActivity implements
//        OnItemClickListener {
//    private SwipeMenuListView lvConversation;
//    private ChatAllHistoryAdapter conversationAdapter;
//    //	private ConversationListAdapter conversationAdapter;
//    private List<EMConversation> mList = null;
//    private List<ContactInfo> contactList = null;
//    private List<Conversation> conversationList;
////	private MyDatabaseHelper helper;
//
//    private BroadcastReceiver receiver;
//
//    private IntentFilter filter;
//    private boolean bRegisterReceiver = false;
//
//    private TextView sysMessageNum, inviteNum, visitNum;
//    private RelativeLayout layoutRecentInvite, layoutSystemMessage, layoutRecentVisit;
//    private boolean hidden;
//    private TextView tvTitle;
//    private RelativeLayout layoutInviteNum, layoutSysMsgNum, layoutVisitNum;
//    private View header;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View messageLayout = inflater.inflate(R.layout.fragment_message,
//                container, false);
//        header = inflater.inflate(R.layout.fragment_message_header,null);
//        findView(messageLayout);
//        bindView(messageLayout);
//        init();
//        return messageLayout;
//    }
//
//    @Override
//    public void onDestroy() {
//        // TODO Auto-generated method stub
//        super.onDestroy();
//        unregisterReceiver();
//        lvConversation = null;
//        conversationAdapter = null;
//        if (mList != null) {
//            mList.clear();
//            mList = null;
//        }
//        tvTitle = null;
//
//        receiver = null;
//
//        filter = null;
//
//        sysMessageNum = null;
//        inviteNum = null;
//        layoutRecentInvite = null;
//        layoutSystemMessage = null;
//
//    }
//
//    private void findView(View root) {
//        lvConversation = (SwipeMenuListView) root.findViewById(R.id.lv_conversation);
//        tvTitle = (TextView) root.findViewById(R.id.tv_title);
//        sysMessageNum = (TextView) header.findViewById(R.id.tv_sys_message_num);
//        inviteNum = (TextView) root.findViewById(R.id.tv_invite_num);
//        visitNum = (TextView) root.findViewById(R.id.tv_visit_num);
//        layoutRecentInvite = (RelativeLayout) root
//                .findViewById(R.id.layout_recent_invite);
//        layoutSystemMessage = (RelativeLayout) header
//                .findViewById(R.id.layout_sys_message);
//        layoutRecentVisit = (RelativeLayout) root
//                .findViewById(R.id.layout_recent_visit);
//        layoutVisitNum = (RelativeLayout) root
//                .findViewById(R.id.layout_visit_num);
//        layoutSysMsgNum = (RelativeLayout) header
//                .findViewById(R.id.layout_sys_meessage_num);
//        layoutInviteNum = (RelativeLayout) root
//                .findViewById(R.id.layout_invite_num);
//    }
//
//    private void bindView(View root) {
//        tvTitle.setVisibility(View.VISIBLE);
//        lvConversation.addHeaderView(header);
//        mList = new ArrayList<EMConversation>();
//        contactList = new ArrayList<ContactInfo>();
//        conversationList = new ArrayList<Conversation>();
//        conversationAdapter = new ChatAllHistoryAdapter(getActivity(), 1, conversationList, mList);
//        // step 1. create a MenuCreator
//        SwipeMenuCreator creator = new SwipeMenuCreator() {
//
//            @Override
//            public void create(SwipeMenu menu) {
////                // create "open" item
////                SwipeMenuItem openItem = new SwipeMenuItem(
////                        getActivity());
////                // set item background
////                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
////                        0xCE)));
////                // set item width
////                openItem.setWidth(Util.dip2px(getActivity(),90));
////                // set item title
////                openItem.setTitle("Open");
////                // set item title fontsize
////                openItem.setTitleSize(18);
////                // set item title font color
////                openItem.setTitleColor(Color.WHITE);
////                // add to menu
////                menu.addMenuItem(openItem);
//
//                // create "delete" item
//                SwipeMenuItem deleteItem = new SwipeMenuItem(
//                        getActivity());
//                // set item background
//                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
//                        0x3F, 0x25)));
//                // set item width
//                deleteItem.setWidth(Util.dip2px(getActivity(),90));
//                // set a icon
////                deleteItem.setIcon(R.drawable.ic_delete);
//                deleteItem.setTitle("删除");
//                deleteItem.setTitleColor(Color.WHITE);
//                deleteItem.setTitleSize(16);
//
//                // add to menu
//                menu.addMenuItem(deleteItem);
//            }
//        };
//        // set creator
//        lvConversation.setMenuCreator(creator);
//        lvConversation.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
////                ApplicationInfo item = mAppList.get(position);
////                switch (index) {
////                    case 0:
////                        // open
////                        open(item);
////                        break;
////                    case 1:
////                        // delete
//////					delete(item);
////                        mAppList.remove(position);
////                        mAdapter.notifyDataSetChanged();
////                        break;
////                }
//                EMChatManager.getInstance().deleteConversation(mList.get(position).getUserName());
//                mList.remove(position);
//                conversationAdapter.notifyDataSetChanged();
//                return false;
//            }
//        });
//
//        // set SwipeListener
//        lvConversation.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
//
//            @Override
//            public void onSwipeStart(int position) {
//                // swipe start
//            }
//
//            @Override
//            public void onSwipeEnd(int position) {
//                // swipe end
//            }
//        });
//        lvConversation.setAdapter(conversationAdapter);
//        lvConversation.setOnItemClickListener(this);
//        layoutRecentInvite.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                MobclickAgent.onEvent(getActivity(), "消息页面——最近的邀请");
//                Intent intent = new Intent(MessageFragment.this.getActivity(),
//                        RecentInviteActivity.class);
//                startActivity(intent);
//            }
//        });
//        layoutSystemMessage.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Intent intent = new Intent(getActivity(),
//                        SystemMessageActivity.class);
//                startActivity(intent);
//            }
//        });
//        layoutRecentVisit.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Intent intent = new Intent(getActivity(),
//                        RecentVisitActivity.class);
//                startActivity(intent);
//            }
//        });
//
//    }
//
//    private void init() {
//        tvTitle.setText("消息中心");
//        receiver = new MyReceiver();
//        if (!bRegisterReceiver) {
//            bRegisterReceiver = true;
//            registerReceiver();
//        }
//        getInviteMessageNum();
//        getSysMessageNum();
//    }
//
//    private int getInviteMessageNum() {
//        MessageDatabaseHelper helper = new MessageDatabaseHelper(getActivity());
//        int num = helper.getInviteMessageNum();
//        if (num > 0) {
//            inviteNum.setText(num + "");
//            layoutInviteNum.setVisibility(View.VISIBLE);
//        } else {
//            layoutInviteNum.setVisibility(View.INVISIBLE);
//        }
//        return num;
//    }
//
//    private int getSysMessageNum() {
//        MessageDatabaseHelper helper = new MessageDatabaseHelper(getActivity());
//        int num = helper.getSysMessageNum();
//        if (num > 0) {
//            sysMessageNum.setText(num + "");
//            layoutSysMsgNum.setVisibility(View.VISIBLE);
//        } else {
//            layoutSysMsgNum.setVisibility(View.INVISIBLE);
//        }
//        return num;
//    }
//
//    /**
//     * 注册广播
//     */
//    public void registerReceiver() {
//    }
//
//    /**
//     * 解除注册广播
//     */
//    public void unregisterReceiver() {
//    }
//
//    private class MyReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            // TODO Auto-generated method stub
//            LogUtil.d("messagefragment收到广播");
//            abortBroadcast();
//            String action = intent.getAction();
//            if (JPushReceiver.ACTION_UPDATE_MESSAGE_UI.equals(action)) {
//                getInviteMessageNum();
//                getSysMessageNum();
//            } else if (JPushReceiver.ACTION_JPUSH_INVITE_MESSAGE_RECIEVE
//                    .equals(action)) {
//                // 收到旅行邀请消息
//                LogUtil.d("收到邀请消息广播");
//                getInviteMessageNum();
//                if (!getUserVisibleHint()) {
////					ivRedDot.setVisibility(View.VISIBLE);
//                }
//            } else if (JPushReceiver.ACTION_JPUSH_SYS_MESSAGE_RECIEVE
//                    .equals(action)) {
//                LogUtil.d("收到系统消息广播");
//                getSysMessageNum();
//                if (!getUserVisibleHint()) {
////					ivRedDot.setVisibility(View.VISIBLE);
//                }
//            } else if (EMChatManager.getInstance().getNewMessageBroadcastAction()
//                    .equals(action)) {
//                //收到im消息
//
//                //消息id
//                String msgId = intent.getStringExtra("msgid");
//                //发消息的人的username(userid)
//                String msgFrom = intent.getStringExtra("from");
//                //消息类型，文本，图片，语音消息等,这里返回的值为msg.type.ordinal()。
//                //所以消息type实际为是enum类型
//                int msgType = intent.getIntExtra("type", 0);
//                //更方便的方法是通过msgId直接获取整个message
//                EMMessage message = EMChatManager.getInstance().getMessage(msgId);
//                ContactInfo contactInfo = new ContactInfo();
//                contactInfo.setImId(message.getFrom());
//                try {
//                    LogUtil.d("messagefragment收到IM消息" + "new message id:" + msgId + " from:" + message
//                            .getStringAttribute("nick_name") + " type:" + msgType);
//                    contactInfo.setUid(message.getStringAttribute("uid"));
//                    contactInfo.setNickName(message.getStringAttribute("nick_name"));
//                    contactInfo.setHeadPhoto(message.getStringAttribute("headphoto"));
//                    IMDatabaseHelper imDatabaseHelper = new IMDatabaseHelper(getActivity());
//                    imDatabaseHelper.saveContactInfo(contactInfo);
//                } catch (EaseMobException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                    getUserInfo(message.getFrom());
//                    return;
//                }
//                refresh();
//            }
//        }
//
//
//    }
//
//    //根据imid获取用户信息
//    private void getUserInfo(final String imid) {
//        new BaseHttpAsyncTask<Void, Void, UserInfoResult>(getActivity(), false) {
//            @Override
//            protected void onCompleteTask(UserInfoResult result) {
//                if(tvTitle==null){
//                    return;
//                }
//                if (result.getCode() == BaseResult.SUCCESS) {
//                    User user = new User();
//                    user = result.getUser();
//                    LogUtil.d("会话列表页面 从后台获取用户昵称  ： " + user.getNickname());
//                    ContactInfo contactInfo = new ContactInfo();
//                    contactInfo.setImId(imid);
//                    contactInfo.setUid(user.getId());
//                    contactInfo.setNickName(user.getNickname());
//                    contactInfo.setHeadPhoto(user.getAvatar().getUrl() + "&size=200x200");
//                    IMDatabaseHelper imDatabaseHelper = new IMDatabaseHelper(
//                            getActivity().getApplicationContext());
//                    imDatabaseHelper.saveContactInfo(contactInfo);
//
//                    //刷新
//                    refresh();
//
//                } else {
//                    //获取用户信息失败
//                }
//            }
//
//            @Override
//            protected UserInfoResult run(Void... params) {
//                return HttpRequestUtil.getInstance().getUserInfoByIMId(imid);
//            }
//
//            @Override
//            protected void onError() {
//                // TODO Auto-generated method stub
//
//            }
//        }.execute();
//    }
//
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position,
//                            long id) {
//        // TODO Auto-generated method stub
//        if(position<=0){
//            return;
//        }
//        Intent intent = new Intent();
//        intent.putExtra("id", mList.get(position-1).getUserName());
//        if (mList.get(position-1).getIsGroup()) {
//            intent.putExtra("chatType", 2);
//            intent.putExtra("groupImId", mList.get(position-1).getUserName());
//        } else {
//            intent.putExtra("chatType", 1);
//            intent.putExtra("name", conversationList.get(position-1).getContactInfo().getNickName());
//            intent.putExtra("uid", conversationList.get(position-1).getContactInfo().getUid());
//            intent.putExtra("headphoto", conversationList.get(position-1).getContactInfo().getHeadPhoto());
//        }
//
//        intent.setClass(getActivity(), ChatsActivity.class);
//        getActivity().startActivity(intent);
//    }
//
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//
//        if (isVisibleToUser) {
//            getInviteMessageNum();
//            getSysMessageNum();
//            if (!bRegisterReceiver) {
//                registerReceiver();
//            }
//        } else {
//            unregisterReceiver();
//        }
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        unregisterReceiver();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (!bRegisterReceiver) {
//            registerReceiver();
//        }
//        if (!hidden) {
//            refresh();
//        }
//        getInviteMessageNum();
//        getSysMessageNum();
//    }
//
//    /**
//     * 刷新页面
//     */
//    public void refresh() {
//        if (mList == null) {
//            mList = new ArrayList<EMConversation>();
//        } else {
//            mList.clear();
//        }
//        mList.addAll(loadConversationsWithRecentChat());
//        if (contactList == null) {
//            contactList = new ArrayList<ContactInfo>();
//        } else {
//            contactList.clear();
//        }
//        contactList.addAll(loadContactList());
//        if (conversationList == null) {
//            conversationList = new ArrayList<Conversation>();
//        } else {
//            conversationList.clear();
//        }
//        for (EMConversation emConversation : mList) {
//            boolean find = false;
//            //如果没有获取到对方传过来的用户信息，则不显示这一条（废弃）
////            try {
////                if(StringUtil.isEmpty(emConversation.getLastMessage().getStringAttribute("uid"))){
////                    mList.remove(emConversation);
////                    continue;
////                }
////            } catch (EaseMobException e) {
////                e.printStackTrace();
////                mList.remove(emConversation);
////                continue;
////            }
//            for (ContactInfo contactInfo : contactList) {
//                if (contactInfo.getImId().equals(emConversation.getUserName())) {
//                    Conversation c = new Conversation();
//                    c.setEmConversation(emConversation);
//                    c.setContactInfo(contactInfo);
//                    conversationList.add(c);
//                    find = true;
//                    break;
//                }
//            }
//            if (!find) {
//                Conversation c = new Conversation();
//                c.setEmConversation(emConversation);
//                c.setContactInfo(new ContactInfo());
//                conversationList.add(c);
//            }
//        }
//        conversationAdapter.notifyDataSetChanged();
//        getInviteMessageNum();
//        getSysMessageNum();
//    }
//
//    /**
//     * 获取所有会话
//     *
//     * @param
//     * @return
//     */
//    private List<EMConversation> loadConversationsWithRecentChat() {
//        // 获取所有会话，包括陌生人
//        Hashtable<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();
//        List<EMConversation> list = new ArrayList<EMConversation>();
//        // 过滤掉messages seize为0的conversation
//        for (EMConversation conversation : conversations.values()) {
//            if (conversation.getAllMessages().size() != 0)
//                list.add(conversation);
//        }
//        // 排序
//        sortConversationByLastChatTime(list);
//        return list;
//    }
//
//    private List<ContactInfo> loadContactList() {
//        List<ContactInfo> list = new ArrayList<ContactInfo>();
//        IMDatabaseHelper imDatabaseHelper = new IMDatabaseHelper(getActivity());
//        list = imDatabaseHelper.getAllContactInfo();
//        return list;
//    }
//
//    /**
//     * 根据最后一条消息的时间排序
//     *
//     * @param
//     */
//    private void sortConversationByLastChatTime(List<EMConversation> conversationList) {
//        Collections.sort(conversationList, new Comparator<EMConversation>() {
//            @Override
//            public int compare(final EMConversation con1, final EMConversation con2) {
//
//                EMMessage con2LastMessage = con2.getLastMessage();
//                EMMessage con1LastMessage = con1.getLastMessage();
//                if (con2LastMessage.getMsgTime() == con1LastMessage.getMsgTime()) {
//                    return 0;
//                } else if (con2LastMessage.getMsgTime() > con1LastMessage.getMsgTime()) {
//                    return 1;
//                } else {
//                    return -1;
//                }
//            }
//
//        });
//    }
//
//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        this.hidden = hidden;
//        if (!hidden) {
//            refresh();
//        }
//    }
//
//}
