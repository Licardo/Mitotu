package com.miaotu.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.miaotu.R;
import com.miaotu.util.Util;

public class FirstPageTab1Fragment extends BaseFragment implements View.OnClickListener {
private View root;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_first_page_tab1, container, false);
        findView();
        bindView();
        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void bindView() {
            }
    private void findView() {
    }

    private void init() {
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