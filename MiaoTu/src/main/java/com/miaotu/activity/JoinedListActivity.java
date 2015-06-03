package com.miaotu.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.miaotu.R;
import com.miaotu.adapter.JoinedListAdapter;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.model.JoinedListInfo;
import com.miaotu.result.BaseResult;
import com.miaotu.result.JoinedListResult;
import com.miaotu.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class JoinedListActivity extends BaseActivity implements View.OnClickListener{

    private TextView tvLeft,tvTitle;
    private ListView lvContent;
    private List<JoinedListInfo> joinedListInfoList;
    private JoinedListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joined_list);

        initView();
        init();
    }

    private void initView(){
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvLeft = (TextView) findViewById(R.id.tv_left);
        lvContent = (ListView) findViewById(R.id.lv_content);
    }
    private void init(){
        tvTitle.setText("");
        tvLeft.setOnClickListener(this);
        joinedListInfoList = new ArrayList<>();
        adapter = new JoinedListAdapter(this, joinedListInfoList);
        lvContent.setAdapter(adapter);
        String flag = getIntent().getStringExtra("flag");       //1：一起走的报名列表，2：妙旅团的报名列表
        if("1".equals(flag)){
            getTogetherList(getIntent().getStringExtra("yid"));
        }else {
            getCustomTourList(getIntent().getStringExtra("aid"));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_left:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 获取一起走报名列表
     * @param yid
     */
    private void getTogetherList(final String yid){
        new BaseHttpAsyncTask<Void, Void, JoinedListResult>(this, false){

            @Override
            protected void onCompleteTask(JoinedListResult joinedListResult) {
                if (joinedListResult.getCode() == BaseResult.SUCCESS){
                    joinedListInfoList.clear();
                    if(joinedListResult.getJoinedListInfoList() == null){
                        adapter.notifyDataSetChanged();
                        return;
                    }
                    joinedListInfoList.addAll(joinedListResult.getJoinedListInfoList());
                    adapter.notifyDataSetChanged();
                }else {
                    if (StringUtil.isBlank(joinedListResult.getMsg())){
                        showToastMsg("获取报名列表失败");
                    }else {
                        showToastMsg(joinedListResult.getMsg());
                    }
                }
            }

            @Override
            protected JoinedListResult run(Void... params) {
                return HttpRequestUtil.getInstance().getTogetherJoinedList(
                        readPreference("token"), yid, "100");
            }
        }.execute();
    }

    /**
     * 获取一起走报名列表
     * @param aid
     */
    private void getCustomTourList(final String aid){
        new BaseHttpAsyncTask<Void, Void, JoinedListResult>(this, false){

            @Override
            protected void onCompleteTask(JoinedListResult joinedListResult) {
                if (joinedListResult.getCode() == BaseResult.SUCCESS){
                    joinedListInfoList.clear();
                    if(joinedListResult.getJoinedListInfoList() == null){
                        adapter.notifyDataSetChanged();
                        return;
                    }
                    joinedListInfoList.addAll(joinedListResult.getJoinedListInfoList());
                    adapter.notifyDataSetChanged();
                }else {
                    if (StringUtil.isBlank(joinedListResult.getMsg())){
                        showToastMsg("获取报名列表失败");
                    }else {
                        showToastMsg(joinedListResult.getMsg());
                    }
                }
            }

            @Override
            protected JoinedListResult run(Void... params) {
                return HttpRequestUtil.getInstance().getCustomTourJoinedList(
                        readPreference("token"), aid, "100");
            }
        }.execute();
    }
}
