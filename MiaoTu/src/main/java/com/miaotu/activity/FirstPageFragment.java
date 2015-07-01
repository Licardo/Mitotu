package com.miaotu.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.miaotu.R;
import com.miaotu.util.StringUtil;
import com.miaotu.util.Util;

public class FirstPageFragment extends BaseFragment implements View.OnClickListener {
private View root;
    private FragmentManager fragmentManager;
    private FirstPageTab1Fragment mTab01 ;
    private FirstPageTab2Fragment mTab02 ;
    private int curPage;
    private ImageView ivPublish;
    private RadioGroup radioGroup;
    private LinearLayout layoutSearch;
    private TextView tvRight;
    private static FirstPageFragment instance;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_first_page, container, false);
        instance = FirstPageFragment.this;
        findView();
        bindView();
        init();
        return root;
    }
    public static FirstPageFragment getInstance(){
        return instance;
    }
    public void refreshCity(){
        try{
            if (tvRight == null){
                return;
            }
            if(!StringUtil.isEmpty(readPreference("selected_city"))){
                if(readPreference("selected_city").length()>4){
                    tvRight.setText(readPreference("selected_city").substring(0,3)+"...");
                }else{
                    tvRight.setText(readPreference("selected_city"));
                }
            }else
//            if(!StringUtil.isEmpty(readPreference("located_city"))){
//                if(readPreference("located_city").length()>4){
//                    tvRight.setText(readPreference("located_city").substring(0,3)+"...");
//                }else{
//                    tvRight.setText(readPreference("located_city"));
//                }
//            }else
            {
                tvRight.setText("定位中...");
            }
            mTab02.getTogether(false);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void bindView() {
        ivPublish.setOnClickListener(this);
        layoutSearch.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.tab1:
                        setTabSelection(0);
                        tvRight.setVisibility(View.VISIBLE);
                        break;
                    case R.id.tab2:
                        setTabSelection(1);
                        tvRight.setVisibility(View.GONE);
                        break;
                }
            }
        });
            }
    private void findView() {
        ivPublish = (ImageView) root.findViewById(R.id.iv_publish);
        radioGroup = (RadioGroup) root.findViewById(R.id.rg_title);
        layoutSearch = (LinearLayout) root.findViewById(R.id.layout_search);
        tvRight = (TextView) root.findViewById(R.id.tv_right);
    }

    private void init() {
        fragmentManager = getChildFragmentManager();
//        if(!StringUtil.isEmpty(readPreference("selected_city"))){
//            if(readPreference("selected_city").length()>4){
//                tvRight.setText(readPreference("selected_city").substring(0,3)+"...");
//            }else{
//                tvRight.setText(readPreference("selected_city"));
//            }
//        }else
        tvRight.setTextColor(getResources().getColor(R.color.grey64));
        if(!StringUtil.isEmpty(readPreference("selected_city"))){
            if(readPreference("selected_city").length()>4){
                tvRight.setText(readPreference("selected_city").substring(0,3)+"...");
            }else{
                tvRight.setText(readPreference("selected_city"));
            }
        }else{
                tvRight.setText("定位中...");
        }
        setTabSelection(0);
    }


    @Override
    public void onClick(View view) {
        if(!Util.isNetworkConnected(getActivity())) {
            showToastMsg("当前未联网，请检查网络设置");
            return;
        }
        switch (view.getId()) {
            case R.id.iv_publish:
                //发布新的旅行
                if(radioGroup.getCheckedRadioButtonId()==R.id.tab1){
                    Intent publishTogetherIntent = new Intent(getActivity(),PublishTogetherStep1Activity.class);
                    startActivity(publishTogetherIntent);
                }else if(radioGroup.getCheckedRadioButtonId()==R.id.tab2){
                    Intent publishCustomIntent = new Intent(getActivity(),PublishCustomTourActivity.class);
                    startActivity(publishCustomIntent);
                }
                break;
            case R.id.layout_search:
                Intent intent = new Intent(getActivity(),SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_right:
                Intent intent1 = new Intent(getActivity(),CityListActivity.class);
                startActivityForResult(intent1,1);
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
            case 0:
                curPage = 0;
                if (mTab01 == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    mTab01 = new FirstPageTab1Fragment();
                    transaction.add(R.id.id_content, mTab01);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(mTab01);
                }
                break;
            case 1:
                curPage = 1;
                if(StringUtil.isEmpty(readPreference("tip2"))){
                    writePreference("tip2","1");
                    MainActivity.getInstance().showTip("2");
                }
                if (mTab02 == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    mTab02 = new FirstPageTab2Fragment();
                    transaction.add(R.id.id_content, mTab02);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(mTab02);
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
        if (requestCode == 1 && resultCode == 1) {
            ((MainActivity) getActivity()).writePreference("selected_city", data.getStringExtra("city"));
            if(readPreference("selected_city").length()>4){
                tvRight.setText(readPreference("selected_city").substring(0,4)+"...");
            }else{
                tvRight.setText(readPreference("selected_city"));
            }
        }
        switch (resultCode) {
            case 1001:  //喜欢一起去
                mTab01.modifyLikeView(requestCode, true);
                break;
            case 1002:  //不喜欢一起去
                mTab01.modifyLikeView(requestCode, false);
                break;
            case 1003:  //喜欢秒旅团
                mTab02.modifyLikeView(requestCode, true);
                break;
            case 1004:  //不喜欢秒旅团
                mTab02.modifyLikeView(requestCode, false);
                break;
        }
    }
}