package com.miaotu.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.miaotu.R;
import com.miaotu.adapter.MyFansAdapter;
import com.miaotu.adapter.MyLikeAdapter;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.model.BlackInfo;
import com.miaotu.result.BaseResult;
import com.miaotu.result.BlackResult;
import com.miaotu.util.StringUtil;
import com.miaotu.util.Util;

import java.util.ArrayList;
import java.util.List;

public class MyFansFragment extends BaseFragment implements View.OnClickListener{

    private SwipeMenuListView lvBlackList;
    private List<BlackInfo> blackInfoList;
    private MyFansAdapter adapter;
    private View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        root = inflater.inflate(R.layout.fragment_my_like, container, false);
        initView();
        initData();
        return root;
    }

    private void initView(){
        lvBlackList = (SwipeMenuListView) root.findViewById(R.id.lv_blacklist);
    }

    private void initData(){
        blackInfoList = new ArrayList<>();
        adapter = new MyFansAdapter(this.getActivity(), blackInfoList, readPreference("token"));
        lvBlackList.setAdapter(adapter);

        getFansList();
    }

    /**
     * 获取关注列表
     */
    private void getFansList(){
        new BaseHttpAsyncTask<Void, Void, BlackResult>(this.getActivity(), false){

            @Override
            protected void onCompleteTask(BlackResult blackResult) {
                if(blackResult.getCode() == BaseResult.SUCCESS){
                    blackInfoList.clear();
                    if(blackResult.getBlackInfos() == null){
                        adapter.notifyDataSetChanged();
                        return;
                    }
                    blackInfoList.addAll(blackResult.getBlackInfos());
                    adapter.notifyDataSetChanged();
                }else {
                    if(StringUtil.isBlank(blackResult.getMsg())){
                        showToastMsg("获取黑名单失败");
                    }else {
                        showToastMsg(blackResult.getMsg());
                    }
                }
            }

            @Override
            protected BlackResult run(Void... params) {
                return HttpRequestUtil.getInstance().getFansList(readPreference("token"),
                        readPreference("uid"));
            }
        }.execute();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            default:
                break;
        }
    }
}
