package com.miaotu.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.miaotu.R;
import com.miaotu.adapter.TogetherlistAdapter;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.model.Together;
import com.miaotu.result.BaseResult;
import com.miaotu.result.MyTogetherResult;
import com.miaotu.util.StringUtil;
import com.miaotu.util.Util;

import java.util.ArrayList;
import java.util.List;

public class MyTogetherFragment extends BaseFragment implements View.OnClickListener {
    private View root;
    private PullToRefreshListView lvPull;
    private View head;
    private TogetherlistAdapter adapter;
    private List<Together> mList;
    private int page = 1;
    private final int PAGECOUNT = 12;
    private boolean isLoadMore = false;
    private View layoutMore;
    private String type,uid;
    private boolean isOwner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_my_together, container, false);
        layoutMore = inflater.inflate(R.layout.pull_to_refresh_more, null);
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
                        TogetherDetailActivity.class);
                intent.putExtra("id", mList.get(position - 1).getId());
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
                getTogether(type, uid, false);

            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                loadMore(false);
            }

        });
        lvPull.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
//                showToastMsg("滑动到底了");
                if (!isLoadMore && mList.size() == page * PAGECOUNT) {
                    loadMore(false);
                }
            }

        });
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void init() {
        if(getArguments() !=null){
            type = getArguments().getString("type");
            uid = getArguments().getString("uid");
            isOwner = getArguments().getBoolean("isOwner");
        }
        mList = new ArrayList<>();
        adapter = new TogetherlistAdapter(getActivity(), mList, true, isOwner);
        lvPull.setAdapter(adapter);
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        wm.getDefaultDisplay().getSize(size);
        int width = size.x;
        getTogether(type, uid, true);
    }

    //获取一起去
    private void getTogether(final String type, final String uid, final boolean isShow) {
        new BaseHttpAsyncTask<Void, Void, MyTogetherResult>(getActivity(), isShow) {
            @Override
            protected void onCompleteTask(MyTogetherResult result) {
                if (root == null) {
                    return;
                }
                if (result.getCode() == BaseResult.SUCCESS) {
                    mList.clear();
                    if(result.getDateTourInfoList() == null){
                        adapter.notifyDataSetChanged();
                        return;
                    }
                    mList.addAll(result.getDateTourInfoList());
                    adapter.notifyDataSetChanged();
                    if (lvPull.getRefreshableView().getFooterViewsCount() == 1 && mList.size() == PAGECOUNT * page) {
                        lvPull.getRefreshableView().addFooterView(layoutMore);
                    }
                } else {
                    if (StringUtil.isEmpty(result.getMsg())) {
                        showToastMsg("获取约游列表失败！");
                    } else {
                        showToastMsg(result.getMsg());
                    }
                }
            }

            @Override
            protected MyTogetherResult run(Void... params) {
                page = 1;
                return HttpRequestUtil.getInstance().getMyTogetherList(readPreference("token"),
                        uid, type, page * PAGECOUNT + "");
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
        new BaseHttpAsyncTask<Void, Void, MyTogetherResult>(getActivity(), isShow) {
            @Override
            protected void onCompleteTask(MyTogetherResult result) {
                if (root == null) {
                    return;
                }
                if (result.getCode() == BaseResult.SUCCESS) {
                    if (result.getDateTourInfoList() == null) {
                        return;
                    }
                    mList.addAll(result.getDateTourInfoList());
                    adapter.notifyDataSetChanged();

                } else {
                    if (StringUtil.isEmpty(result.getMsg())) {
                        showToastMsg("获取约游列表失败！");
                    } else {
                        showToastMsg(result.getMsg());
                    }
                }
            }

            @Override
            protected MyTogetherResult run(Void... params) {
                isLoadMore = true;
                page += 1;
                return HttpRequestUtil.getInstance().getMyTogetherList(readPreference("token"),
                        uid, type, page*PAGECOUNT+"");
            }

            @Override
            protected void finallyRun() {
                isLoadMore = false;
                if (mList.size() != PAGECOUNT * page) {
                    lvPull.getRefreshableView().removeFooterView(layoutMore);
                }
                super.finallyRun();
            }
        }.execute();
    }

    @Override
    public void onClick(View view) {
        if (!Util.isNetworkConnected(getActivity())) {
            showToastMsg("当前未联网，请检查网络设置");
            return;
        }
        switch (view.getId()) {

        }
    }
}