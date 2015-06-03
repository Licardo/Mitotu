package com.miaotu.activity;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.miaotu.R;
import com.miaotu.adapter.BlackListAdapter;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.model.BlackInfo;
import com.miaotu.result.BaseResult;
import com.miaotu.result.BlackResult;
import com.miaotu.util.StringUtil;
import com.miaotu.util.Util;

import java.util.ArrayList;
import java.util.List;

public class BlackListActivity extends BaseActivity implements View.OnClickListener{

    private TextView tvTitle,tvLeft;
    private SwipeMenuListView lvBlackList;
    private List<BlackInfo> blackInfoList;
    private BlackListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_list);

        initView();
        initData();
    }

    private void initView(){
        tvLeft = (TextView) this.findViewById(R.id.tv_left);
        tvTitle = (TextView) this.findViewById(R.id.tv_title);
        lvBlackList = (SwipeMenuListView) this.findViewById(R.id.lv_blacklist);
    }

    private void initData(){
        tvTitle.setText("黑名单");
        tvLeft.setOnClickListener(this);
        blackInfoList = new ArrayList<BlackInfo>();
        adapter = new BlackListAdapter(this, blackInfoList);
        lvBlackList.setAdapter(adapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(BlackListActivity.this);
                deleteItem.setBackground(new ColorDrawable(getResources().getColor(R.color.swipe_delete)));
//                deleteItem.setBackground(R.drawable.icon_msg_delete);
                deleteItem.setWidth(Util.dip2px(BlackListActivity.this, 80));
                deleteItem.setTitle("解除");
                deleteItem.setTitleColor(getResources().getColor(R.color.white));
                deleteItem.setTitleSize(14);
                menu.addMenuItem(deleteItem);
            }
        };
        lvBlackList.setMenuCreator(creator);

        lvBlackList.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        //解除黑名单
                        setBlackList(position);
                        break;
                    default:
                        break;
                }

                return false;
            }
        });

        String uid = readPreference("uid");
        getBlackList(uid);
    }

    /**
     * 获取黑名单
     * @param uid
     */
    private void getBlackList(final String uid){
        new BaseHttpAsyncTask<Void, Void, BlackResult>(this, false){

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
                return HttpRequestUtil.getInstance().getBlackList(readPreference("token"), uid);
            }
        }.execute();
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
     * 加入/解除黑名单
     */
    private void setBlackList(final int position){
        new BaseHttpAsyncTask<Void, Void, BaseResult>(this, false){

            @Override
            protected void onCompleteTask(BaseResult baseResult) {
                if(baseResult.getCode() == BaseResult.SUCCESS){
                    showToastMsg("操作成功");
                    blackInfoList.remove(position);
                    adapter.notifyDataSetChanged();
                }else {
                    if(StringUtil.isBlank(baseResult.getMsg())){
                        showToastMsg("解除黑名单失败");
                    }else {
                        showToastMsg(baseResult.getMsg());
                    }
                }
            }

            @Override
            protected BaseResult run(Void... params) {
                return HttpRequestUtil.getInstance().setBlackList(readPreference("token"),
                        blackInfoList.get(position).getUid());
            }
        }.execute();
    }
}
