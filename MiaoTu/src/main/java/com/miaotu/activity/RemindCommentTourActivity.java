package com.miaotu.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.miaotu.R;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.result.BaseResult;
import com.miaotu.util.StringUtil;

public class RemindCommentTourActivity extends BaseFragmentActivity implements View.OnClickListener{
    private TextView tvTitle,tvLeft,tvRight;
    private FragmentManager fragmentManager;
    private RemindCommentTogetherFragment mTab01;
    private RemindCommentCustomFragment mTab02;
    private RadioGroup radioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind_comment_tour);
        findView();
        bindView();
        init();
    }
    private void findView(){
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvLeft = (TextView) findViewById(R.id.tv_left);
        tvRight = (TextView) findViewById(R.id.tv_right);
        radioGroup = (RadioGroup) findViewById(R.id.rg_title);
    }
    private void bindView(){
        tvLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
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
    private void init(){
        writePreference("tour_comment_count","0");
        MessageFragment.getInstance().refresh();
        tvTitle.setText("评论提醒");
        tvRight.setText("清空");
        fragmentManager = getSupportFragmentManager();
        setTabSelection(0);
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
                    mTab01 = new RemindCommentTogetherFragment();
                    transaction.add(R.id.id_content, mTab01);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(mTab01);
                }
                break;
            case 1:
                if (mTab02 == null) {
                // 如果MessageFragment为空，则创建一个并添加到界面上
                mTab02 = new RemindCommentCustomFragment();
                transaction.add(R.id.id_content, mTab02);
            } else {
                // 如果MessageFragment不为空，则直接将它显示出来
                transaction.show(mTab02);
            }
                break;
        }
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
    /**
     * 清空提醒
     */
    private void delAllCommentTogether(){
        new BaseHttpAsyncTask<Void, Void, BaseResult>(this, true){

            @Override
            protected void onCompleteTask(BaseResult baseResult) {
                if(baseResult.getCode() == BaseResult.SUCCESS){
                    showToastMsg("操作成功");
                    mTab01.getRemindList();
                }else {
                    if(StringUtil.isBlank(baseResult.getMsg())){
                        showToastMsg("删除失败");
                    }else {
                        showToastMsg(baseResult.getMsg());
                    }
                }
            }

            @Override
            protected BaseResult run(Void... params) {
                return HttpRequestUtil.getInstance().delAllCommentTogetherRemind(readPreference("token"));
            }
        }.execute();
    }
    /**
     * 清空提醒
     */
    private void delAllCommentCustom(){
        new BaseHttpAsyncTask<Void, Void, BaseResult>(this, true){

            @Override
            protected void onCompleteTask(BaseResult baseResult) {
                if(baseResult.getCode() == BaseResult.SUCCESS){
                    showToastMsg("操作成功");
                    mTab02.getRemindList();
                }else {
                    if(StringUtil.isBlank(baseResult.getMsg())){
                        showToastMsg("删除失败");
                    }else {
                        showToastMsg(baseResult.getMsg());
                    }
                }
            }

            @Override
            protected BaseResult run(Void... params) {
                return HttpRequestUtil.getInstance().delAllCommentCustomTourRemind(readPreference("token"));
            }
        }.execute();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_left:
                finish();
                break;
            case R.id.tv_right:
                if(radioGroup.getCheckedRadioButtonId()==R.id.tab1){
                    delAllCommentTogether();
                }else if(radioGroup.getCheckedRadioButtonId()==R.id.tab2){
                    delAllCommentCustom();
                }
                break;
        }
    }
}
