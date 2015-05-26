package com.miaotu.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.miaotu.R;
import com.miaotu.adapter.FirstPageImageAdapter;
import com.miaotu.adapter.TogetherlistAdapter;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.model.Banner;
import com.miaotu.model.Together;
import com.miaotu.result.BaseResult;
import com.miaotu.result.SearchTourResult;
import com.miaotu.result.TogetherResult;
import com.miaotu.util.StringUtil;
import com.miaotu.util.Util;
import com.miaotu.view.GuideGallery;

import java.util.ArrayList;
import java.util.List;

public class SearchResultTab1Fragment extends BaseFragment implements View.OnClickListener {
private View root;
    private LinearLayout layoutTogether,layoutCustomTour,layoutTogetherList,layoutCustomTourList;
    private View gap;
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
        layoutTogether = (LinearLayout) root.findViewById(R.id.layout_together);
        layoutTogetherList = (LinearLayout) root.findViewById(R.id.layout_together_list);
        layoutCustomTour = (LinearLayout) root.findViewById(R.id.layout_custom_tour);
        layoutCustomTourList = (LinearLayout) root.findViewById(R.id.layout_custom_tour_list);
        gap = root.findViewById(R.id.view_gap);
    }
    private void bindView() {
            }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void init() {
        search();
    }
//搜索线路
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