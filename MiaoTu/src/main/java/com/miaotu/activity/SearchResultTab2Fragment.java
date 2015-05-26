package com.miaotu.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.miaotu.R;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.result.BaseResult;
import com.miaotu.result.SearchTourResult;
import com.miaotu.util.StringUtil;
import com.miaotu.util.Util;

public class SearchResultTab2Fragment extends BaseFragment implements View.OnClickListener {
private View root;
    private PullToRefreshListView lvPull;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_search_result_tab1, container, false);
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
            }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void init() {
        search();
    }
//搜索人
    public void search() {
        new BaseHttpAsyncTask<Void, Void, SearchTourResult>(getActivity(), true) {
            @Override
            protected void onCompleteTask(SearchTourResult result) {
                if(root==null){
                    return;
                }
                if (result.getCode() == BaseResult.SUCCESS) {
                } else {
                    if(StringUtil.isEmpty(result.getMsg())){
                        showToastMsg("获取搜索结果失败！");
                    }else{
                        showToastMsg(result.getMsg());
                    }
                }
            }

            @Override
            protected SearchTourResult run(Void... params) {
                return HttpRequestUtil.getInstance().searchTour(readPreference("token"), ((SearchActivity) getActivity()).getKey(), readPreference("latitude"), readPreference("longitude"));
            }

            @Override
            protected void finallyRun() {
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