package com.miaotu.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.miaotu.R;
import com.miaotu.adapter.CustomTourlistAdapter;
import com.miaotu.adapter.SearchUserlistAdapter;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.model.CustomTour;
import com.miaotu.model.PersonInfo;
import com.miaotu.result.BaseResult;
import com.miaotu.result.SearchTourResult;
import com.miaotu.result.SearchUserResult;
import com.miaotu.util.StringUtil;
import com.miaotu.util.Util;

import java.util.ArrayList;
import java.util.List;

public class SearchResultTab2Fragment extends BaseFragment implements View.OnClickListener {
private View root;
    private PullToRefreshListView lvPull;
    private SearchUserlistAdapter adapter;
    private List<PersonInfo> mList;
    private int page=1;
    private final int PAGECOUNT = 12;
    private boolean isLoadMore = false;
    private View layoutMore,layoutEmpty;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_search_result_tab2, container, false);
        layoutMore = inflater.inflate(R.layout.pull_to_refresh_more, null);
        layoutEmpty = inflater.inflate(R.layout.layout_empty_search_user, null);
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
                Intent intent = new Intent(getActivity(),
                        PersonCenterActivity.class);
                intent.putExtra("uid", mList.get(position - 1).getUid());
                startActivityForResult(intent, 1);
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
                search(false);

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
                    loadMore();
                }
            }

        });
            }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void init() {
        mList = new ArrayList<>();
        adapter = new SearchUserlistAdapter(getActivity(),mList);
        lvPull.setAdapter(adapter);
        lvPull.setEmptyView(layoutEmpty);
        search(true);
    }
//搜索人
    public void search(boolean isShow) {
        new BaseHttpAsyncTask<Void, Void, SearchUserResult>(getActivity(), isShow) {
            @Override
            protected void onCompleteTask(SearchUserResult result) {
                if(root==null){
                    return;
                }
                if (result.getCode() == BaseResult.SUCCESS) {
                    mList.clear();
                    if(result.getPersonInfo()==null){
                        return;
                    }
                    mList.addAll(result.getPersonInfo());
                    adapter.notifyDataSetChanged();
                    if(lvPull.getRefreshableView().getFooterViewsCount()==1&&mList.size()==PAGECOUNT*page){
                        lvPull.getRefreshableView().addFooterView(layoutMore);
                    }
                } else {
                    if(StringUtil.isEmpty(result.getMsg())){
                        showToastMsg("获取搜索结果失败！");
                    }else{
                        showToastMsg(result.getMsg());
                    }
                }
            }

            @Override
            protected SearchUserResult run(Void... params) {
                page=1;
                return HttpRequestUtil.getInstance().searchUser(readPreference("token"), ((SearchActivity) getActivity()).getKey(), "1", "12");
            }

            @Override
            protected void finallyRun() {
                super.finallyRun();
                lvPull.onRefreshComplete();
            }
        }.execute();
    }
    //获取更多
    public void loadMore() {
        new BaseHttpAsyncTask<Void, Void, SearchUserResult>(getActivity(), false) {
            @Override
            protected void onCompleteTask(SearchUserResult result) {
                if(root==null){
                    return;
                }
                if (result.getCode() == BaseResult.SUCCESS) {
                    mList.addAll(result.getPersonInfo());
                    adapter.notifyDataSetChanged();
                } else {
                    if(StringUtil.isEmpty(result.getMsg())){
                        showToastMsg("获取搜索结果失败！");
                    }else{
                        showToastMsg(result.getMsg());
                    }
                }
            }

            @Override
            protected SearchUserResult run(Void... params) {
                isLoadMore = true;
                page+=1;
                return HttpRequestUtil.getInstance().searchUser(readPreference("token"), ((SearchActivity) getActivity()).getKey(), "1", "12");
            }

            @Override
            protected void finallyRun() {
                super.finallyRun();
                isLoadMore=false;
                if(mList.size()!=PAGECOUNT*page){
                    lvPull.getRefreshableView().removeFooterView(layoutMore);
                }
                lvPull.onRefreshComplete();
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