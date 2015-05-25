package com.miaotu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.miaotu.R;
import com.miaotu.adapter.TopiclistAdapter;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.jpush.MessageCountDatabaseHelper;
import com.miaotu.model.Topic;
import com.miaotu.result.BaseResult;
import com.miaotu.result.MemberListResult;
import com.miaotu.result.TopicListResult;
import com.miaotu.util.StringUtil;
import com.miaotu.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ying on 2015/3/6.
 */
public class BBSTopicListFragment extends BaseFragment implements View.OnClickListener{
    private View root;
    private TextView tvTitle;
    private Button btnRight,btnLeft;
    private PullToRefreshListView lvTopics;
    private View layoutMore;
    private List<Topic> topicList;
    private TopiclistAdapter adapter;
    private static int PAGECOUNT=8;
    private int curPageCount = 0;
    private boolean isLoadMore = false;
    private ImageView ivDot;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.activity_bbs_topic_list,
                container, false);
        layoutMore = inflater.inflate(R.layout.pull_to_refresh_more,null);
        findView();
        bindView();
        init();
        return root;
    }

    private void findView() {
        tvTitle = (TextView) root.findViewById(R.id.tv_title);
        btnRight = (Button) root.findViewById(R.id.btn_right);
        btnLeft = (Button) root.findViewById(R.id.btn_left);
        lvTopics = (PullToRefreshListView) root.findViewById(R.id.lv_topics);
        ivDot = (ImageView) root.findViewById(R.id.iv_msg_flg);
    }

    private void bindView() {
        lvTopics.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(
                        getActivity(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
                                | DateUtils.FORMAT_ABBREV_ALL);

                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                getTopics(false);

            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                loadMore();
            }

        });

        lvTopics.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
//                showToastMsg("滑动到底了");
                if (!isLoadMore&&curPageCount==topicList.size()) {
                    loadMore();
                }
//                lvTopics.setRefreshing(true);
            }

        });
        lvTopics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //从1开始 跳转到帖子详情页
                if (i > 0) {
                    Intent intent = new Intent(getActivity(), BBSTopicDetailActivity.class);
                    intent.putExtra("topic", topicList.get(i - 1));
                    startActivity(intent);
                }
            }
        });
//
        btnRight.setOnClickListener(this);
        btnLeft.setOnClickListener(this);
    }

    private void init() {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("社区");
        btnRight.setText("写话题");
        btnRight.setVisibility(View.VISIBLE);
        ViewGroup.LayoutParams params = btnRight.getLayoutParams();
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        btnRight.setLayoutParams(params);
        btnLeft.setBackgroundResource(R.drawable.icon_topic_message);
        ViewGroup.LayoutParams params1 = btnLeft.getLayoutParams();
        params1.width =  Util.dip2px(getActivity(),24);
        params1.height = Util.dip2px(getActivity(),24);
        btnLeft.setLayoutParams(params1);
        btnLeft.setVisibility(View.VISIBLE);
        topicList=new ArrayList<>();
        adapter = new TopiclistAdapter(getActivity(),topicList);
        lvTopics.setAdapter(adapter);

//        getMessageCount();
        getTopics(true);
        
    }
    //刷新左上角提示
    public void refreshMessage(){
        try{
            ivDot.setVisibility(View.VISIBLE);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //刷新社区页面
    private void refresh(){
        try{
            getTopics(false);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
//    private void getMessageCount(){
//        MessageCountDatabaseHelper helper = new MessageCountDatabaseHelper(getActivity());
//        if(helper.getMessageNum("topic")>0){
//            ivDot.setVisibility(View.VISIBLE);
//        }else{
//            ivDot.setVisibility(View.GONE);
//        }
//    }
    private void getTopics(boolean isShow) {
        new BaseHttpAsyncTask<Void, Void, TopicListResult>(getActivity(), isShow) {
            @Override
            protected void onCompleteTask(TopicListResult result) {
                if(topicList==null){
                    return;
                }
                if (result.getCode() == BaseResult.SUCCESS) {
                    topicList.clear();
                    topicList.addAll(result.getTopics());
                    adapter.notifyDataSetChanged();
//                    showToastMsg("lastvisibale:"+lvTopics.getRefreshableView().getLastVisiblePosition()+"  count: "+lvTopics.getRefreshableView().getCount()+" first:"+lvTopics.getRefreshableView().getFirstVisiblePosition());
                    if(lvTopics.getRefreshableView().getFooterViewsCount()==1&&topicList.size()==PAGECOUNT){
                        lvTopics.getRefreshableView().addFooterView(layoutMore);
                    }
                } else {
                    if (StringUtil.isEmpty(result.getMsg())) {
                        showToastMsg("获取话题列表失败！");
                    } else {
                        showToastMsg(result.getMsg());
                    }
                }
            }

            @Override
            protected TopicListResult run(Void... params) {
                curPageCount=PAGECOUNT;
				return HttpRequestUtil.getInstance().getTopicList(curPageCount+"",""); //第二个参数表示活动id，主要用于线路详情中查看线路话题
            }

            @Override
            protected void finallyRun() {
                if(topicList==null){
                    return;
                }
                lvTopics.onRefreshComplete();
            }
        }.execute();
    }
    private void loadMore() {
        new BaseHttpAsyncTask<Void, Void, TopicListResult>(getActivity(), false) {
            @Override
            protected void onCompleteTask(TopicListResult result) {
                if(topicList==null){
                    return;
                }
                if (result.getCode() == BaseResult.SUCCESS) {
                    topicList.clear();
                    topicList.addAll(result.getTopics());
                    adapter.notifyDataSetChanged();
                    if(topicList.size()!=curPageCount){
                        lvTopics.getRefreshableView().removeFooterView(layoutMore);
                    }
                } else {
                    if (StringUtil.isEmpty(result.getMsg())) {
                        showToastMsg("获取话题列表失败！");
                    } else {
                        showToastMsg(result.getMsg());
                    }
                }
            }

            @Override
            protected TopicListResult run(Void... params) {
                isLoadMore = true;
                curPageCount+=PAGECOUNT;
                return HttpRequestUtil.getInstance().getTopicList(curPageCount+"","");
            }

            @Override
            protected void finallyRun() {
                if(topicList==null){
                    return;
                }
                lvTopics.onRefreshComplete();
                isLoadMore = false;
            }
        }.execute();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_right:
                //发表新话题
                Intent intent = new Intent(getActivity(),BBSPublishTopicActivity.class);
                startActivityForResult(intent,1);
                break;
            case R.id.btn_left:
                //社区提醒
                MessageCountDatabaseHelper helper = new MessageCountDatabaseHelper(getActivity());
                long l = helper.resetMessageNo("topic");
                ivDot.setVisibility(View.GONE);
                Intent intent1 = new Intent(getActivity(),BBSMessageActivity.class);
                startActivity(intent1);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1&&resultCode==1){
            //发表帖子成功
            refresh();
        }
    }
}
