package com.miaotu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.miaotu.adapter.TopicCommentsAdapter;
import com.miaotu.adapter.TopicMessageAdapter;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.model.PhotoInfo;
import com.miaotu.model.Topic;
import com.miaotu.model.TopicComment;
import com.miaotu.model.TopicMessage;
import com.miaotu.result.BaseResult;
import com.miaotu.result.TopicCommentsListResult;
import com.miaotu.result.TopicMessageListResult;
import com.miaotu.util.LogUtil;
import com.miaotu.util.StringUtil;
import com.miaotu.util.Util;
import com.miaotu.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.photoselector.model.PhotoModel;
import com.photoselector.ui.PhotoPreviewActivity;
import com.photoselector.util.CommonUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ying on 2015/3/6.
 */
public class BBSMessageActivity extends BaseActivity implements View.OnClickListener{
    private TextView tvTitle;
    private TextView tvLeft;
    private PullToRefreshListView lvTopicMessage;
    private List<TopicMessage> mList;
    private TopicMessageAdapter adapter;
    private static int PAGECOUNT=15;
    private int curPageCount = 0;
    private boolean isLoadMore = false;
    private View layoutMore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbs_topic_message);
        layoutMore = getLayoutInflater().inflate(R.layout.pull_to_refresh_more, null);
        findView();
        bindView();
        init();
    }

    private void findView() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvLeft = (TextView) findViewById(R.id.tv_left);
        lvTopicMessage = (PullToRefreshListView) findViewById(R.id.lv_topic_message);
    }

    private void bindView() {
        lvTopicMessage.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(
                        BBSMessageActivity.this, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
                                | DateUtils.FORMAT_ABBREV_ALL);

                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                getMessages(false);

            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
//                loadMore();
            }

        });
        lvTopicMessage.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
//                showToastMsg("滑动到底了");
                if (!isLoadMore&&mList.size()==curPageCount) {
                    loadMoreComments();
                }
            }

        });
        lvTopicMessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(BBSMessageActivity.this, BBSTopicDetailActivity.class);
                intent.putExtra("sid", mList.get(i - 1).getSid());
                startActivity(intent);
                //设置已读状态
                read(false, mList.get(i - 1).getSmid(), i - 1);
            }
        });
        tvLeft.setOnClickListener(this);
    }

    private void init() {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("话题回复");
        tvLeft.setVisibility(View.VISIBLE);
        tvLeft.setBackgroundResource(R.drawable.icon_back);
        mList=new ArrayList<>();
        adapter = new TopicMessageAdapter(BBSMessageActivity.this,mList);
        lvTopicMessage.setAdapter(adapter);
        getMessages(false);
        
    }
    private void getMessages(boolean isShow) {
        new BaseHttpAsyncTask<Void, Void, TopicMessageListResult>(BBSMessageActivity.this, isShow) {
            @Override
            protected void onCompleteTask(TopicMessageListResult result) {
                    if(mList==null){
                        return;
                    }
                if (result.getCode() == BaseResult.SUCCESS) {
                    mList.clear();
                    mList.addAll(result.getMessages());
                    TopicMessage mes = new TopicMessage();
                    mes.setContent("你好啊");
                    mes.setCreated(new Date().toString());
                    mes.setNickname("四小美");
                    mes.setRemark("hi");
                    mes.setStatus("0");
                    mes.setSid("1");
                    mes.setSmid("1");
                    mList.add(mes);
                    adapter.notifyDataSetChanged();
//                    showToastMsg("lastvisibale:"+lvTopicMessage.getRefreshableView().getLastVisiblePosition()+"  count: "+lvTopicMessage.getRefreshableView().getCount()+" first:"+lvTopicMessage.getRefreshableView().getFirstVisiblePosition());
                    if(lvTopicMessage.getRefreshableView().getFooterViewsCount()==1&&mList.size()==PAGECOUNT){
                        lvTopicMessage.getRefreshableView().addFooterView(layoutMore);
                    }
                } else {
                    if (StringUtil.isEmpty(result.getMsg())) {
                        showToastMsg("获取话题失败！");
                    } else {
                        showToastMsg(result.getMsg());
                    }
                }
            }

            @Override
            protected TopicMessageListResult run(Void... params) {
                curPageCount=PAGECOUNT;
				return HttpRequestUtil.getInstance().getTopicMessage(readPreference("token"), curPageCount + "");
            }

//            @Override
//            protected void onError() {
//                // TODO Auto-generated method stub
//
//            }

            @Override
            protected void finallyRun() {

                if(mList==null){
                    return;
                }
                lvTopicMessage.onRefreshComplete();
            }
        }.execute();
    }
    private void loadMoreComments() {
        new BaseHttpAsyncTask<Void, Void, TopicMessageListResult>(BBSMessageActivity.this, false) {
            @Override
            protected void onCompleteTask(TopicMessageListResult result) {
                    if(mList==null){
                        return;
                    }
                if (result.getCode() == BaseResult.SUCCESS) {
                    mList.clear();
                    mList.addAll(result.getMessages());
                    adapter.notifyDataSetChanged();
                    if(mList.size()!=curPageCount){
                        lvTopicMessage.getRefreshableView().removeFooterView(layoutMore);
                    }
                } else {
                    if (StringUtil.isEmpty(result.getMsg())) {
                        showToastMsg("获取话题失败！");
                    } else {
                        showToastMsg(result.getMsg());
                    }
                }
            }

            @Override
            protected TopicMessageListResult run(Void... params) {
                isLoadMore = true;
                curPageCount+=PAGECOUNT;
                return HttpRequestUtil.getInstance().getTopicMessage(readPreference("token"), curPageCount + "");
            }

//            @Override
//            protected void onError() {
//                // TODO Auto-generated method stub
//
//            }

            @Override
            protected void finallyRun() {
                if(mList==null){
                    return;
                }
                lvTopicMessage.onRefreshComplete();
                isLoadMore = false;
            }
        }.execute();
    }
    //标记消息为已读
        private void read(boolean isShow,final String meesageId,final int position) {
        new BaseHttpAsyncTask<Void, Void, BaseResult>(BBSMessageActivity.this, isShow) {
            @Override
            protected void onCompleteTask(BaseResult result) {
                    if(mList==null){
                        return;
                    }
                if (result.getCode() == BaseResult.SUCCESS) {
                    //标记成功
                    mList.get(position).setStatus("1");
                    adapter.notifyDataSetChanged();
                } else {
                    if (StringUtil.isEmpty(result.getMsg())) {
                    } else {
                    }
                }
            }

            @Override
            protected BaseResult run(Void... params) {
                curPageCount=PAGECOUNT;
                return HttpRequestUtil.getInstance().readTopicMessage(readPreference("token"), meesageId);
            }

//            @Override
//            protected void onError() {
//                // TODO Auto-generated method stub
//
//            }
        }.execute();
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
