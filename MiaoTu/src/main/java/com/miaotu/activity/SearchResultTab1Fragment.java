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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.miaotu.R;
import com.miaotu.adapter.FirstPageImageAdapter;
import com.miaotu.adapter.TogetherlistAdapter;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.model.Banner;
import com.miaotu.model.CustomTour;
import com.miaotu.model.Together;
import com.miaotu.result.BaseResult;
import com.miaotu.result.SearchTourResult;
import com.miaotu.result.TogetherResult;
import com.miaotu.util.StringUtil;
import com.miaotu.util.Util;
import com.miaotu.view.CircleImageView;
import com.miaotu.view.GuideGallery;

import java.util.ArrayList;
import java.util.List;

public class SearchResultTab1Fragment extends BaseFragment implements View.OnClickListener {
private View root;
    private LinearLayout layoutTogether,layoutCustomTour,layoutTogetherList,layoutCustomTourList,layoutEmpty;
    private View gap;
    private TextView tvTogetherMore,tvCustomMore;
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
        layoutEmpty = (LinearLayout) root.findViewById(R.id.layout_empty);
        tvCustomMore = (TextView) root.findViewById(R.id.tv_custom_more);
        tvTogetherMore = (TextView) root.findViewById(R.id.tv_together_more);
        gap = root.findViewById(R.id.view_gap);
    }
    private void bindView() {
        tvCustomMore.setOnClickListener(this);
        tvTogetherMore.setOnClickListener(this);
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
                        layoutTogetherList.removeAllViews();
                        layoutCustomTourList.removeAllViews();
                    if(result.getSearchTour().getCustomTourList()!=null||result.getSearchTour().getTogetherList()!=null){
                        layoutEmpty.setVisibility(View.GONE);
                    }else {
                        layoutEmpty.setVisibility(View.VISIBLE);
                    }
                    if(result.getSearchTour().getTogetherList()!=null){
                        if(result.getSearchTour().getTogetherList().size()>=3){
                            tvTogetherMore.setVisibility(View.VISIBLE);
                        }else {
                            tvTogetherMore.setVisibility(View.GONE);
                        }
                        layoutTogether.setVisibility(View.VISIBLE);
                        gap.setVisibility(View.VISIBLE);
                        for(Together together:result.getSearchTour().getTogetherList()){
                            LinearLayout layoutItem = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.item_search_tour_result,null);
                            UrlImageViewHelper.setUrlDrawable((CircleImageView)(layoutItem.findViewById(R.id.iv_head_photo)),together.getHeadPhoto(),R.drawable.default_avatar);
                            ((TextView)layoutItem.findViewById(R.id.tv_name)).setText(together.getNickname());
                            ((TextView)layoutItem.findViewById(R.id.tv_time)).setText(together.getPublishDate());
                            ((TextView)layoutItem.findViewById(R.id.tv_distance)).setText(together.getDistance());
                            ((TextView)layoutItem.findViewById(R.id.tv_comment)).setText(together.getComment());
                            layoutItem.setTag(together.getId());
                            layoutItem.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(getActivity(),TogetherDetailActivity.class);
                                    intent.putExtra("id",(String)view.getTag());
                                    startActivity(intent);
                                }
                            });
                            layoutTogetherList.addView(layoutItem);
                        }
                    }else{
                        layoutTogether.setVisibility(View.GONE);
                        gap.setVisibility(View.GONE);
                    }
                    if(result.getSearchTour().getCustomTourList()!=null){
                        if(result.getSearchTour().getCustomTourList().size()>=3){
                            tvCustomMore.setVisibility(View.VISIBLE);
                        }else {
                            tvCustomMore.setVisibility(View.GONE);
                        }
                        layoutCustomTour.setVisibility(View.VISIBLE);
                        for(CustomTour customTour:result.getSearchTour().getCustomTourList()){
                            LinearLayout layoutItem = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.item_search_tour_result,null);
                            UrlImageViewHelper.setUrlDrawable((CircleImageView)(layoutItem.findViewById(R.id.iv_head_photo)),customTour.getHeadUrl(),R.drawable.default_avatar);
                            ((TextView)layoutItem.findViewById(R.id.tv_name)).setText(customTour.getNickname());
                            ((TextView)layoutItem.findViewById(R.id.tv_time)).setText(customTour.getCreated());
                            layoutItem.findViewById(R.id.tv_distance).setVisibility(View.GONE);
                            ((TextView)layoutItem.findViewById(R.id.tv_comment)).setText(customTour.getDescription());
                            layoutItem.setTag(customTour.getId());
                            layoutItem.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(getActivity(),CustomTourDetailActivity.class);
                                    intent.putExtra("id",(String)view.getTag());
                                    startActivity(intent);
                                }
                            });
                            layoutCustomTourList.addView(layoutItem);
                        }
                    }else {
                        layoutCustomTour.setVisibility(View.GONE);
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
            case R.id.tv_custom_more:
                break;
        }
    }
}