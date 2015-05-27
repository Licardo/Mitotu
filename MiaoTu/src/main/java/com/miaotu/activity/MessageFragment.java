package com.miaotu.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.miaotu.R;

/**
 * Created by ying on 2015/5/27.
 */
public class MessageFragment extends BaseFragment {
    private View root;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_message,
                container, false);
        findView();
        bindView();
        init();
        return root;
    }
    private void findView(){}
    private void bindView(){}
    private void init(){}
}
