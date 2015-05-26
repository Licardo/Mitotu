package com.miaotu.activity;

import android.annotation.SuppressLint;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.miaotu.R;
import com.miaotu.util.StringUtil;

public class SearchActivity extends BaseFragmentActivity implements View.OnClickListener{
private LinearLayout layoutSearch,layoutClear,layoutResult;
    private EditText etKey;
    private ImageView ivInit;
    private RadioGroup rgTab;
    private FragmentManager fragmentManager;
    private SearchResultTab1Fragment mTab01 ;
    private SearchResultTab2Fragment mTab02 ;
    private String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        findView();
        bindView();
        init();
    }
    private void findView(){
        layoutSearch = (LinearLayout) findViewById(R.id.layout_search);
        layoutClear = (LinearLayout) findViewById(R.id.layout_clear);
        layoutResult = (LinearLayout) findViewById(R.id.layout_result);
        etKey = (EditText) findViewById(R.id.et_key);
        ivInit = (ImageView) findViewById(R.id.iv_init);
        rgTab = (RadioGroup) findViewById(R.id.rg_search);

    }
    private void bindView(){
        layoutClear.setOnClickListener(this);
        layoutSearch.setOnClickListener(this);
        rgTab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rb_search1:
                        setTabSelection(0);
                        break;
                    case R.id.rb_search2:
                        setTabSelection(1);
                        break;
                }
            }
        });
    }
    private void init(){
        fragmentManager = getSupportFragmentManager();
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
                if (mTab01 == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    mTab01 = new SearchResultTab1Fragment();
                    transaction.add(R.id.layout_content, mTab01);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(mTab01);
                    mTab01.search();
                }
                break;
            case 1:
                if (mTab02 == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    mTab02 = new SearchResultTab2Fragment();
                    transaction.add(R.id.layout_content, mTab02);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(mTab02);
                    mTab02.search(true);
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.layout_search:
                //点击搜索
                if(StringUtil.isBlank(etKey.getText().toString())){
                    showToastMsg("请输入关键词！");
                    return;
                }
                key = StringUtil.trimAll(etKey.getText().toString());
                ivInit.setVisibility(View.GONE);
                layoutResult.setVisibility(View.VISIBLE);
                setTabSelection(0);
                rgTab.check(R.id.rb_search1);
                break;
            case R.id.layout_clear:
                //点击清除
                etKey.setText("");
                break;
        }
    }
}
