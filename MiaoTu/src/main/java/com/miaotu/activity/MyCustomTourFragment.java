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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.miaotu.R;
import com.miaotu.adapter.CustomTourlistAdapter;
import com.miaotu.adapter.TogetherlistAdapter;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.model.CustomTour;
import com.miaotu.result.BaseResult;
import com.miaotu.result.MyCustomTourResult;
import com.miaotu.util.StringUtil;
import com.miaotu.util.Util;

import java.util.ArrayList;
import java.util.List;

public class MyCustomTourFragment extends BaseFragment implements View.OnClickListener {
    private View root;
    private PullToRefreshListView lvPull;
    private View head;
    private CustomTourlistAdapter adapter;
    private List<CustomTour> mList;
    private int page = 1;
    private final int PAGECOUNT = 12;
    private boolean isLoadMore = false;
    private View layoutMore;
    private String type,uid;
    private boolean isOwner;    //我的动态orTA的动态
    private boolean isMineCustomTour;   //是否是我发布的妙旅团

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_my_customtour, container, false);
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
                Intent intent = new Intent(getActivity(),
                        CustomTourDetailActivity.class);
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
                getCustomTour(type, uid, false);

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
        if (isOwner && "owner".equals(type)){
            isMineCustomTour = true;
        }
        mList = new ArrayList<>();
        adapter = new CustomTourlistAdapter(getActivity(), mList, isMineCustomTour);
        lvPull.setAdapter(adapter);
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        wm.getDefaultDisplay().getSize(size);
        int width = size.x;

        View emptyview = LayoutInflater.from(this.getActivity()).
                inflate(R.layout.activity_empty, null);
        TextView tvContent1 = (TextView) emptyview.findViewById(R.id.tv_content1);
        TextView tvContent2 = (TextView) emptyview.findViewById(R.id.tv_content2);
        TextView tvTip1 = (TextView) emptyview.findViewById(R.id.tv_tip1);
        TextView tvTip2 = (TextView) emptyview.findViewById(R.id.tv_tip2);
        Button btnSearch = (Button) emptyview.findViewById(R.id.btn_search);
        btnSearch.setVisibility(View.GONE);
        if (isOwner) {  //我的动态
            if ("join".equals(type)) {
                tvContent1.setVisibility(View.GONE);
                tvTip2.setVisibility(View.VISIBLE);
                tvTip1.setText("你还没有报名“妙旅团”哦");
                tvTip2.setText("有好多好玩线路等着你！");
                lvPull.setEmptyView(emptyview);
            } else if ("like".equals(type)) {

            }else {
                tvContent2.setVisibility(View.VISIBLE);
                tvTip2.setVisibility(View.VISIBLE);
                tvContent1.setText("一个人旅行是瞎逛");
                tvContent2.setText("一群人旅行是狂欢");
                tvTip1.setText("良辰美景你忍心独自欣赏？");
                tvTip2.setText("快去“妙旅团”发起旅行吧");
            }
        }else { //TA的动态
            if ("join".equals(type)){ //ta报名的
                tvContent1.setVisibility(View.GONE);
                tvTip1.setText("TA还没有报名“妙旅团”哦");
            }else if ("like".equals(type)){ //ta喜欢的

            }else { //ta发起的
                tvContent1.setVisibility(View.GONE);
                tvTip1.setText("TA还没有发起“妙旅团”哦");
            }
        }
        lvPull.setEmptyView(emptyview);

        getCustomTour(type, uid, true);
    }

    //获取一起去
    private void getCustomTour(final String type, final String uid, final boolean isShow) {
        new BaseHttpAsyncTask<Void, Void, MyCustomTourResult>(getActivity(), isShow) {
            @Override
            protected void onCompleteTask(MyCustomTourResult result) {
                if (root == null) {
                    return;
                }
                if (result.getCode() == BaseResult.SUCCESS) {
                    mList.clear();
                    if(result.getCustomTourInfolist() == null){
                        adapter.notifyDataSetChanged();
                        return;
                    }
                    mList.addAll(result.getCustomTourInfolist());
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
            protected MyCustomTourResult run(Void... params) {
                page = 1;
                return HttpRequestUtil.getInstance().getOwnerCustomerTour(readPreference("token"),
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
        new BaseHttpAsyncTask<Void, Void, MyCustomTourResult>(getActivity(), isShow) {
            @Override
            protected void onCompleteTask(MyCustomTourResult result) {
                if (root == null) {
                    return;
                }
                if (result.getCode() == BaseResult.SUCCESS) {
                    if (result.getCustomTourInfolist() == null) {
                        return;
                    }
                    mList.addAll(result.getCustomTourInfolist());
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
            protected MyCustomTourResult run(Void... params) {
                isLoadMore = true;
                page += 1;
                return HttpRequestUtil.getInstance().getOwnerCustomerTour(readPreference("token"),
                        uid, type, page * PAGECOUNT + "");
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