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

import com.miaotu.R;

/**
 * Created by ying on 2015/5/27.
 */
public class MessageFragment extends BaseFragment implements View.OnClickListener{
    private View root;
    private LinearLayout layoutSys,layoutLikeTour,layoutJoin,layoutChat,layoutLike;
    private ImageView ivSysCount,ivLikeTourCount,ivJoinCount,ivChatCount,ivLikeCount;
    private TextView tvSysContent,tvLikeTourContent,tvJoinContent,tvChatContent,tvLikeContent;
    private TextView tvSysDate,tvLikeTourDate,tvJoinDate,tvChatDate,tvLikeDate;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_message,
                container, false);
        findView();
        bindView();
        init();
        return root;
    }
    private void findView(){
        layoutSys = (LinearLayout) root.findViewById(R.id.layout_sys);
        layoutLikeTour = (LinearLayout) root.findViewById(R.id.layout_like_tour);
        layoutJoin = (LinearLayout) root.findViewById(R.id.layout_join);
        layoutChat = (LinearLayout) root.findViewById(R.id.layout_chat);
        layoutLike = (LinearLayout) root.findViewById(R.id.layout_like);
        ivSysCount = (ImageView) root.findViewById(R.id.iv_sys_count);
        ivLikeTourCount = (ImageView) root.findViewById(R.id.iv_like_tour_msg);
        ivJoinCount = (ImageView) root.findViewById(R.id.iv_join_count);
        ivChatCount = (ImageView) root.findViewById(R.id.iv_chat_count);
        ivLikeCount = (ImageView) root.findViewById(R.id.iv_like_count);
        tvSysContent = (TextView) root.findViewById(R.id.tv_sys_content);
        tvLikeTourContent = (TextView) root.findViewById(R.id.tv_like_tour_content);
        tvJoinContent = (TextView) root.findViewById(R.id.tv_join_content);
        tvChatContent = (TextView) root.findViewById(R.id.tv_chat_content);
        tvLikeContent = (TextView) root.findViewById(R.id.tv_like_content);

        tvSysDate = (TextView) root.findViewById(R.id.tv_sys_date);
        tvLikeTourDate = (TextView) root.findViewById(R.id.tv_like_tour_date  );
        tvChatDate = (TextView) root.findViewById(R.id.tv_chat_date);
        tvJoinDate = (TextView) root.findViewById(R.id.tv_join_date);
        tvLikeDate = (TextView) root.findViewById(R.id.tv_like_date);

    }
    private void bindView(){
        layoutChat.setOnClickListener(this);
    }
    private void init(){}

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.layout_chat:
                Intent intent = new Intent(getActivity(),MessageActivity.class);
                startActivity(intent);
                break;
        }
    }
}
