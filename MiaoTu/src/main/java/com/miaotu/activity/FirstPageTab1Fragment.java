package com.miaotu.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.miaotu.R;
import com.miaotu.adapter.TogetherlistAdapter;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.model.RegisterInfo;
import com.miaotu.model.Together;
import com.miaotu.result.BaseResult;
import com.miaotu.result.LoginResult;
import com.miaotu.result.TogetherResult;
import com.miaotu.util.StringUtil;
import com.miaotu.util.Util;

import java.util.ArrayList;
import java.util.List;

public class FirstPageTab1Fragment extends BaseFragment implements View.OnClickListener {
private View root;
    private PullToRefreshListView lvPull;
    private View head;
    private TogetherlistAdapter adapter;
    private List<Together> mList;
    private int page=1;
    private final int PAGECOUNT = 12;
    private boolean isLoadMore = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_first_page_tab1, container, false);
        head = inflater.inflate(R.layout.together_head,null);
        findView();
        bindView();
        init();
        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void findView() {
        lvPull = (PullToRefreshListView) root.findViewById(R.id.lv_pull);
    }
    private void bindView() {
        lvPull.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
//                Intent intent = new Intent(RecentVisitActivity.this,
//                        UserHomeActivity.class);
//                intent.putExtra("userId", visitorList.get(position - 1)
//                        .getUserId());
//                startActivityForResult(intent, 1);
            }
        });
        lvPull.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(
                        getActivity(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
                                | DateUtils.FORMAT_ABBREV_ALL);

                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                getTogether(false);

            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
//                loadMore();
            }

        });
        lvPull.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
//                showToastMsg("滑动到底了");
                if (!isLoadMore&&mList.size()==page*PAGECOUNT) {
                    loadMore(false);
                }
            }

        });
            }

    private void init() {
        lvPull.getRefreshableView().addHeaderView(head);
        mList = new ArrayList<>();
        adapter = new TogetherlistAdapter(getActivity(),mList,false);
        lvPull.setAdapter(adapter);
        getTogether(true);
    }
//获取一起去
    private void getTogether(final boolean isShow) {
        new BaseHttpAsyncTask<Void, Void, TogetherResult>(getActivity(), isShow) {
            @Override
            protected void onCompleteTask(TogetherResult result) {
                if(root==null){
                    return;
                }
                if (result.getCode() == BaseResult.SUCCESS) {
                    mList.clear();
                    mList.addAll(result.getTogetherList());
                    adapter.notifyDataSetChanged();
                    if(lvPull.getRefreshableView().getFooterViewsCount()==1&&mList.size()==PAGECOUNT*page){
                        lvPull.getRefreshableView().addFooterView(head);
                    }
                } else {
                    if(StringUtil.isEmpty(result.getMsg())){
                        showToastMsg("获取约游列表失败！");
                    }else{
                        showToastMsg(result.getMsg());
                    }
                }
            }

            @Override
            protected TogetherResult run(Void... params) {
                page=1;
                return HttpRequestUtil.getInstance().getTogetherList(readPreference("token"),page+"",PAGECOUNT+"",readPreference("latitude"),readPreference("longitude"));
            }

            @Override
            protected void finallyRun() {
                super.finallyRun();
                lvPull.onRefreshComplete();
            }
        }.execute();
    }
    //获取一起去
    private void loadMore(final boolean isShow) {
        new BaseHttpAsyncTask<Void, Void, TogetherResult>(getActivity(), isShow) {
            @Override
            protected void onCompleteTask(TogetherResult result) {
                if(root==null){
                    return;
                }
                if (result.getCode() == BaseResult.SUCCESS) {
//                    mList.clear();
                    mList.addAll(result.getTogetherList());
                    adapter.notifyDataSetChanged();
                    if(mList.size()!=PAGECOUNT*page){
                        lvPull.getRefreshableView().removeFooterView(head);
                    }
                } else {
                    if(StringUtil.isEmpty(result.getMsg())){
                        showToastMsg("获取约游列表失败！");
                    }else{
                        showToastMsg(result.getMsg());
                    }
                }
            }

            @Override
            protected TogetherResult run(Void... params) {
                isLoadMore = true;
                page+=1;
                return HttpRequestUtil.getInstance().getTogetherList(readPreference("token"),page+"",PAGECOUNT+"",readPreference("latitude"),readPreference("longitude"));
            }

            @Override
            protected void finallyRun() {
                isLoadMore=false;
                super.finallyRun();
            }
        }.execute();
    }
    @Override
    public void onClick(View view) {
        if(!Util.isNetworkConnected(getActivity())) {
            showToastMsg("当前未联网，请检查网络设置");
            return;
        }
        switch (view.getId()) {

        }
    }
}