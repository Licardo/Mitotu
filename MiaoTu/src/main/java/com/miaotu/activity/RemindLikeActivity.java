package com.miaotu.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.miaotu.R;
import com.miaotu.adapter.BlackListAdapter;
import com.miaotu.adapter.RemindLikeListAdapter;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.jpush.MessageDatabaseHelper;
import com.miaotu.model.BlackInfo;
import com.miaotu.model.RemindLike;
import com.miaotu.result.BaseResult;
import com.miaotu.result.BlackResult;
import com.miaotu.result.RemindLikeResult;
import com.miaotu.util.StringUtil;
import com.miaotu.util.Util;

import java.util.ArrayList;
import java.util.List;

public class RemindLikeActivity extends BaseActivity implements View.OnClickListener{
    private TextView tvTitle,tvLeft,tvRight;
    private SwipeMenuListView lv;
    private List<RemindLike> remindLikes;
    private RemindLikeListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind_like);
        findView();
        bindView();
        init();
    }
    private void findView(){
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvLeft = (TextView) findViewById(R.id.tv_left);
        tvRight = (TextView) findViewById(R.id.tv_right);
        lv = (SwipeMenuListView) findViewById(R.id.lv);
    }
    private void bindView(){
        tvLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
    }
    private void init(){
        MessageDatabaseHelper helper = new MessageDatabaseHelper(this);
        helper.resetAllLikeMessage();
        MessageFragment.getInstance().refresh();
        tvTitle.setText("关注提醒");
        tvRight.setText("清空");
        remindLikes = new ArrayList<RemindLike>();
        adapter = new RemindLikeListAdapter(this, remindLikes);
        lv.setAdapter(adapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(RemindLikeActivity.this);
                deleteItem.setBackground(new ColorDrawable(getResources().getColor(R.color.swipe_delete)));
//                deleteItem.setBackground(R.drawable.icon_msg_delete);
                deleteItem.setWidth(Util.dip2px(RemindLikeActivity.this, 80));
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
        new BaseHttpAsyncTask<Void, Void, RemindLikeResult>(this, true){

            @Override
            protected void onCompleteTask(RemindLikeResult remindLikeResult) {
                if(remindLikeResult.getCode() == BaseResult.SUCCESS){
                    remindLikes.addAll(remindLikeResult.getRemindLikes());
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
            protected RemindLikeResult run(Void... params) {
                return HttpRequestUtil.getInstance().getRemindLikeList(readPreference("token"));
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
                    remindLikes.remove(position);
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
                return HttpRequestUtil.getInstance().delLikeRemind(readPreference("token"), remindLikes.get(position).getId());
            }
        }.execute();
    }
    /**
     * 清空提醒
     */
    private void delAllLike(){
        new BaseHttpAsyncTask<Void, Void, BaseResult>(this, true){

            @Override
            protected void onCompleteTask(BaseResult baseResult) {
                if(baseResult.getCode() == BaseResult.SUCCESS){
                    showToastMsg("操作成功");
                    remindLikes.clear();
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
                return HttpRequestUtil.getInstance().delAllLikeRemind(readPreference("token"));
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
                delAllLike();
                break;
        }
    }
}
