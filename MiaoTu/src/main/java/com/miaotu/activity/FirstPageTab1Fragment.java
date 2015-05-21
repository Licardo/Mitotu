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
import com.miaotu.result.BaseResult;
import com.miaotu.result.LoginResult;
import com.miaotu.util.StringUtil;
import com.miaotu.util.Util;

public class FirstPageTab1Fragment extends BaseFragment implements View.OnClickListener {
private View root;
    private PullToRefreshListView lvPull;
    private View head;
    private TogetherlistAdapter adapter;
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
//                getVisitors(false);

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
//                if (!isLoadMore&&visitorList.size()==offset) {
//                    loadMore();
//                }
            }

        });
            }

    private void init() {
        lvPull.getRefreshableView().addHeaderView(head);
//        adapter = new TogetherlistAdapter();
        lvPull.setAdapter(adapter);
        getTogether(true);
    }
//获取一起去
    private void getTogether(final boolean isShow) {
        new BaseHttpAsyncTask<Void, Void, BaseResult>(getActivity(), isShow) {
            @Override
            protected void onCompleteTask(BaseResult result) {
                if(root==null){
                    return;
                }
                if (result.getCode() == BaseResult.SUCCESS) {
                } else {
                    if(StringUtil.isEmpty(result.getMsg())){
                        showToastMsg("获取约游列表失败！");
                    }else{
                        showToastMsg(result.getMsg());
                    }
                }
            }

            @Override
            protected BaseResult run(Void... params) {
                return HttpRequestUtil.getInstance().getTogetherList(readPreference("token"),"1","2");
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