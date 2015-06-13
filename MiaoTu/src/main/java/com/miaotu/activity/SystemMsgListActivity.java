package com.miaotu.activity;

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
import com.miaotu.adapter.RemindLikeListAdapter;
import com.miaotu.adapter.RemindSysListAdapter;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.model.RemindLike;
import com.miaotu.model.RemindSys;
import com.miaotu.result.BaseResult;
import com.miaotu.result.RemindLikeResult;
import com.miaotu.result.RemindSysResult;
import com.miaotu.util.StringUtil;
import com.miaotu.util.Util;

import java.util.ArrayList;
import java.util.List;

public class SystemMsgListActivity extends BaseActivity implements View.OnClickListener{
private TextView tvTitle,tvLeft;
    private SwipeMenuListView lv;
    private List<RemindSys> remindSyses;
    private RemindSysListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_msg_list);
        findView();
        bindView();
        init();
    }
    private void findView(){
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvLeft = (TextView) findViewById(R.id.tv_left);
        lv = (SwipeMenuListView) findViewById(R.id.lv);
    }
    private void bindView(){
        tvLeft.setOnClickListener(this);
    }
    private void init(){
        tvTitle.setText("系统消息");
        writePreference("sys_count","0");
        MessageFragment.getInstance().refresh();
        remindSyses = new ArrayList<RemindSys>();
        adapter = new RemindSysListAdapter(this, remindSyses);
        lv.setAdapter(adapter);
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(SystemMsgListActivity.this);
                deleteItem.setBackground(new ColorDrawable(getResources().getColor(R.color.swipe_delete)));
//                deleteItem.setBackground(R.drawable.icon_msg_delete);
                deleteItem.setWidth(Util.dip2px(SystemMsgListActivity.this, 80));
                deleteItem.setTitle("删除");
                deleteItem.setTitleColor(getResources().getColor(R.color.white));
                deleteItem.setTitleSize(14);
                menu.addMenuItem(deleteItem);
            }
        };
        lv.setMenuCreator(creator);

        lv.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        //解除黑名单
                        delLike(position);
                        break;
                    default:
                        break;
                }

                return false;
            }
        });
        getRemindList();
    }
    /**
     * 获取关注提醒列表
     */
    private void getRemindList(){
        new BaseHttpAsyncTask<Void, Void, RemindSysResult>(this, true){

            @Override
            protected void onCompleteTask(RemindSysResult remindLikeResult) {
                if(remindLikeResult.getCode() == BaseResult.SUCCESS){
                    remindSyses.addAll(remindLikeResult.getRemindSyses());
                    adapter.notifyDataSetChanged();
                }else {
                    if(StringUtil.isBlank(remindLikeResult.getMsg())){
                        showToastMsg("获取关注列表失败");
                    }else {
                        showToastMsg(remindLikeResult.getMsg());
                    }
                }
            }

            @Override
            protected RemindSysResult run(Void... params) {
                return HttpRequestUtil.getInstance().getRemindSysList(readPreference("token"));
            }
        }.execute();
    }
    /**
     * 删除提醒
     * @param position
     */
    private void delLike(final int position){
        new BaseHttpAsyncTask<Void, Void, BaseResult>(this, true){

            @Override
            protected void onCompleteTask(BaseResult baseResult) {
                if(baseResult.getCode() == BaseResult.SUCCESS){
                    showToastMsg("操作成功");
                    remindSyses.remove(position);
                    adapter.notifyDataSetChanged();
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
                return HttpRequestUtil.getInstance().delLikeRemind(readPreference("token"), remindSyses.get(position).getId());
            }
        }.execute();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_left:
                finish();
                break;
        }
    }
}
