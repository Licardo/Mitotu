package com.miaotu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.miaotu.R;
import com.miaotu.imutil.CommonUtils;
import com.miaotu.imutil.Conversation;
import com.miaotu.jpush.MessageDatabaseHelper;
import com.miaotu.util.LogUtil;
import com.miaotu.util.StringUtil;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by ying on 2015/5/27.
 */
public class MessageFragment extends BaseFragment implements View.OnClickListener{
    private View root;
    private LinearLayout layoutSys,layoutLikeTour,layoutJoin,layoutChat,layoutLike,layoutComment;
    private TextView ivSysCount,ivLikeTourCount,ivJoinCount,ivChatCount,ivLikeCount,ivCommentCount;
    private TextView tvSysContent,tvLikeTourContent,tvJoinContent,tvChatContent,tvLikeContent,tvCommnetContent;
    private TextView tvSysDate,tvLikeTourDate,tvJoinDate,tvChatDate,tvLikeDate,tvCommentDate;
    private static MessageFragment messageFragment;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_message,
                container, false);
        findView();
        bindView();
        init();
        messageFragment = this;
        return root;
    }
    private void findView(){
        layoutSys = (LinearLayout) root.findViewById(R.id.layout_sys);
        layoutLikeTour = (LinearLayout) root.findViewById(R.id.layout_like_tour);
        layoutJoin = (LinearLayout) root.findViewById(R.id.layout_join);
        layoutChat = (LinearLayout) root.findViewById(R.id.layout_chat);
        layoutLike = (LinearLayout) root.findViewById(R.id.layout_like);
        layoutComment = (LinearLayout) root.findViewById(R.id.layout_comment);
        ivSysCount = (TextView) root.findViewById(R.id.iv_sys_count);
        ivLikeTourCount = (TextView) root.findViewById(R.id.iv_like_tour_msg);
        ivJoinCount = (TextView) root.findViewById(R.id.iv_join_count);
        ivChatCount = (TextView) root.findViewById(R.id.iv_chat_count);
        ivLikeCount = (TextView) root.findViewById(R.id.iv_like_count);
        ivCommentCount = (TextView) root.findViewById(R.id.iv_comment_count);
        tvSysContent = (TextView) root.findViewById(R.id.tv_sys_content);
        tvLikeTourContent = (TextView) root.findViewById(R.id.tv_like_tour_content);
        tvJoinContent = (TextView) root.findViewById(R.id.tv_join_content);
        tvChatContent = (TextView) root.findViewById(R.id.tv_chat_content);
        tvLikeContent = (TextView) root.findViewById(R.id.tv_like_content);
        tvCommnetContent = (TextView) root.findViewById(R.id.tv_comment_content);

        tvSysDate = (TextView) root.findViewById(R.id.tv_sys_date);
        tvLikeTourDate = (TextView) root.findViewById(R.id.tv_like_tour_date  );
        tvChatDate = (TextView) root.findViewById(R.id.tv_chat_date);
        tvJoinDate = (TextView) root.findViewById(R.id.tv_join_date);
        tvLikeDate = (TextView) root.findViewById(R.id.tv_like_date);
        tvCommentDate = (TextView) root.findViewById(R.id.tv_comment_date);

    }
    private void bindView(){
        layoutChat.setOnClickListener(this);
        layoutLike.setOnClickListener(this);
        layoutLikeTour.setOnClickListener(this);
        layoutJoin.setOnClickListener(this);
        layoutSys.setOnClickListener(this);
        layoutComment.setOnClickListener(this);
    }
    private void init(){
        refresh();
    }
    public static MessageFragment getInstance(){
        return messageFragment;
    }
    public void refresh(){
        //判断是否有im消息
        if( EMChatManager.getInstance().getUnreadMsgsCount()!=0){
            ivChatCount.setText(EMChatManager.getInstance().getUnreadMsgsCount() + "");
            ivChatCount.setVisibility(View.VISIBLE);
        }else{
            ivChatCount.setVisibility(View.GONE);
        }
        //判断是否有喜欢消息
        if(getLikeMessageNum()!=0){
            ivLikeCount.setText(getLikeMessageNum() + "");
            ivLikeCount.setVisibility(View.VISIBLE);
        }else{
            ivLikeCount.setVisibility(View.GONE);
        }
        //是否有喜欢线路消息
        if(!StringUtil.isEmpty(readPreference("tour_like_count"))&&Integer.parseInt(readPreference("tour_like_count"))>0){
            ivLikeTourCount.setText(readPreference("tour_like_count"));
            ivLikeTourCount.setVisibility(View.VISIBLE);
        }else{
            ivLikeTourCount.setVisibility(View.GONE);
        }
        //是否有参加线路消息
        if(!StringUtil.isEmpty(readPreference("tour_join_count"))&&Integer.parseInt(readPreference("tour_join_count"))>0){
            ivJoinCount.setText(readPreference("tour_join_count"));
            ivJoinCount.setVisibility(View.VISIBLE);
        }else{
            ivJoinCount.setVisibility(View.GONE);
        }
        //是否有回复线路消息
        if(!StringUtil.isEmpty(readPreference("tour_comment_count"))&&Integer.parseInt(readPreference("tour_comment_count"))>0){
            ivCommentCount.setText(readPreference("tour_comment_count"));
            ivCommentCount.setVisibility(View.VISIBLE);
        }else{
            ivCommentCount.setVisibility(View.GONE);
        }

        if(!StringUtil.isEmpty(readPreference("tour_like_name"))){
            tvLikeTourContent.setText("@" + readPreference("tour_like_name") + "喜欢您发布的线路了~");
        }
        if(!StringUtil.isEmpty(readPreference("tour_join_name"))){
            tvJoinContent.setText("有人报名了您发布的“"+readPreference("tour_join_name")+"”活动");
        }
        if(!StringUtil.isEmpty(readPreference("like_date"))){
            tvLikeContent.setText("又有新伙伴关注你啦！快去看看吧");
        }
        if(!StringUtil.isEmpty(readPreference("tour_comment_name"))){
            tvCommnetContent.setText(readPreference("tour_comment_name")+" 评论了您的线路。");
        }
        tvSysDate.setText(readPreference("sys_date"));
        tvSysContent.setText(readPreference("sys_name"));
        tvLikeTourDate.setText(readPreference("tour_like_date"));
        tvLikeDate.setText(readPreference("like_date"));
        tvJoinDate.setText(readPreference("tour_join_date"));
        tvChatContent.setText(readPreference("im_content"));
        tvChatDate.setText(readPreference("im_date"));
        tvCommentDate.setText(readPreference("tour_comment_date"));
    }
    private int getLikeMessageNum() {
        MessageDatabaseHelper helper = new MessageDatabaseHelper(getActivity());
        int num = helper.getAllLikeMessage().size();
        LogUtil.d("喜欢提醒个数"+num);
        return num;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.layout_chat:
                Intent intent = new Intent(getActivity(),MessageActivity.class);
                startActivity(intent);
                break;
            case R.id.layout_like:
                Intent intent1 = new Intent(getActivity(),RemindLikeActivity.class);
                startActivity(intent1);
                break;
            case R.id.layout_like_tour:
                Intent intent2 = new Intent(getActivity(),RemindLikeTourActivity.class);
                startActivity(intent2);
                break;
            case R.id.layout_join:
                Intent intent3 = new Intent(getActivity(),RemindJoinTourActivity.class);
                startActivity(intent3);
                break;
            case R.id.layout_sys:
                Intent intent4 = new Intent(getActivity(),SystemMsgListActivity.class);
                startActivity(intent4);
                break;
            case R.id.layout_comment:
                Intent intent5 = new Intent(getActivity(),RemindCommentTourActivity.class);
                startActivity(intent5);
                break;
        }
    }
}
