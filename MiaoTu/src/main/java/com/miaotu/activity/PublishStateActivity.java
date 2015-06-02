package com.miaotu.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.miaotu.R;
import com.miaotu.adapter.TopiclistAdapter;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.form.MFriendsInfo;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.model.Topic;
import com.miaotu.result.BaseResult;
import com.miaotu.result.TopicListResult;
import com.miaotu.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class PublishStateActivity extends BaseActivity implements View.OnClickListener{

    private TextView tvLeft, tvTitle;
    private PullToRefreshListView lvTopics;
    private View layoutMore;
    private List<Topic> topicList;
    private TopiclistAdapter adapter;
    private static int PAGECOUNT=10;
    private int curPageCount = 0;
    private boolean isLoadMore = false;
    private String token,uid,title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_state);

        findView();
        bindView();
        init();
    }

    private void findView(){
        layoutMore = LayoutInflater.from(this).inflate(R.layout.pull_to_refresh_more, null);
        tvLeft = (TextView) this.findViewById(R.id.tv_left);
        tvTitle = (TextView) this.findViewById(R.id.tv_title);
        lvTopics = (PullToRefreshListView) this.findViewById(R.id.lv_topics);
    }

    private void bindView() {
        lvTopics.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(
                        PublishStateActivity.this, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
                                | DateUtils.FORMAT_ABBREV_ALL);

                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

                getTopics(token, uid, false);

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
                if (!isLoadMore && curPageCount == topicList.size()) {
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
                    Intent intent = new Intent(PublishStateActivity.this, BBSTopicDetailActivity.class);
                    intent.putExtra("topic", topicList.get(i - 1));
                    startActivity(intent);
                }
            }
        });
        tvLeft.setOnClickListener(this);
    }

    private void init(){
        topicList=new ArrayList<>();
        token = readPreference("token");
        uid = getIntent().getStringExtra("uid");
        title = getIntent().getStringExtra("title");
        tvTitle.setText(title);
        adapter = new TopiclistAdapter(this,topicList, token);
        lvTopics.setAdapter(adapter);
        getTopics(token, uid, true);
    }

    /**
     * 获取妙友动态
     * @param isShow
     */
    private void getTopics(final String token, final String uid, boolean isShow) {
        new BaseHttpAsyncTask<Void, Void, TopicListResult>(this, isShow) {
            @Override
            protected void onCompleteTask(TopicListResult result) {
                if(topicList==null){
                    return;
                }
                if (result.getCode() == BaseResult.SUCCESS) {
                    topicList.clear();
                    if(result.getTopics() == null){
                        adapter.notifyDataSetChanged();
                        lvTopics.getRefreshableView().removeFooterView(layoutMore);
                        return;
                    }
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
//                info.setNum(curPageCount+"");
                return HttpRequestUtil.getInstance().getPublishStates(token, uid, curPageCount+"");
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
        new BaseHttpAsyncTask<Void, Void, TopicListResult>(this, false) {
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
                return HttpRequestUtil.getInstance().getPublishStates(token, uid, curPageCount + "");
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
            case R.id.tv_left:
                finish();
                break;
            default:
                break;
        }
    }
}
