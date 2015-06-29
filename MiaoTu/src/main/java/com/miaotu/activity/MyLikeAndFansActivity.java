package com.miaotu.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.miaotu.R;
import com.miaotu.util.StringUtil;
import com.miaotu.util.Util;

public class MyLikeAndFansActivity extends BaseFragmentActivity implements View.OnClickListener {
    private FragmentManager fragmentManager;
    private MyLikeFragment mTab01;
    private MyFansFragment mTab02;
    private int curPage;
    private RadioGroup radioGroup;
    private TextView tvLfet,tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_like_fans);

        findView();
        bindView();
        init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void bindView() {
        tvLfet.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.tab1:
                        setTabSelection(0);
                        break;
                    case R.id.tab2:
                        setTabSelection(1);
                        break;
                }
            }
        });
    }

    private void findView() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvLfet = (TextView) findViewById(R.id.tv_left);
        radioGroup = (RadioGroup) findViewById(R.id.rg_title);
    }

    private void init() {
        tvTitle.setText("关注与粉丝");
        fragmentManager = getSupportFragmentManager();
        setTabSelection(0);
        setResult(1);
    }


    @Override
    public void onClick(View view) {
        if (!Util.isNetworkConnected(MyLikeAndFansActivity.this)) {
            showToastMsg("当前未联网，请检查网络设置");
            return;
        }
        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 根据传入的index参数来设置选中的tab页。
     */
    @SuppressLint("NewApi")
    public void setTabSelection(int index) {
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (index) {
            case 0: //关注
                curPage = 0;
                if (mTab01 == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    mTab01 = new MyLikeFragment();
                    transaction.add(R.id.id_content, mTab01);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(mTab01);
                    mTab01.getLikeList();
                }
                break;
            case 1: //粉丝
                curPage = 1;
                if (mTab02 == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    mTab02 = new MyFansFragment();
                    transaction.add(R.id.id_content, mTab02);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(mTab02);
                    mTab02.getFansList();
                }
                break;
        }
//        transaction.commit();
        transaction.commitAllowingStateLoss(); //为了解决换量页面返回时报的错误，详情见http://blog.csdn.net/ranxiedao/article/details/8214936
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    @SuppressLint("NewApi")
    private void hideFragments(FragmentTransaction transaction) {
        if (mTab01 != null) {
            transaction.hide(mTab01);
        }
        if (mTab02 != null) {
            transaction.hide(mTab02);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3 && resultCode == 1) {
            (MyLikeAndFansActivity.this).writePreference("movement_city", data.getStringExtra("city"));
        }
        if (resultCode == 1001){
            mTab01.getLikeList();
        }
    }
}