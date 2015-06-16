package com.miaotu.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.miaotu.R;
import com.miaotu.adapter.RemindLikeTogetherListAdapter;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.model.RemindLikeTogether;
import com.miaotu.result.BaseResult;
import com.miaotu.result.RemindLikeTogetherResult;
import com.miaotu.util.StringUtil;
import com.miaotu.util.Util;

import java.util.ArrayList;
import java.util.List;

public class RemindCommentTogetherFragment extends BaseFragment implements View.OnClickListener{
    private SwipeMenuListView lv;
    private List<RemindLikeTogether> remindLikes;
    private RemindLikeTogetherListAdapter adapter;
    private View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_remind_comment_together,null);
        findView();
        bindView();
        init();
        return root;
    }

    private void findView(){
        lv = (SwipeMenuListView) root.findViewById(R.id.lv);
    }
    private void bindView(){
    }
    private void init(){
        remindLikes = new ArrayList<RemindLikeTogether>();
        adapter = new RemindLikeTogetherListAdapter(getActivity(), remindLikes);
        lv.setAdapter(adapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity());
                deleteItem.setBackground(new ColorDrawable(getResources().getColor(R.color.swipe_delete)));
//                deleteItem.setBackground(R.drawable.icon_msg_delete);
                deleteItem.setWidth(Util.dip2px(getActivity(), 80));
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
     * 获取评论提醒列表
     */
    public void getRemindList(){
        new BaseHttpAsyncTask<Void, Void, RemindLikeTogetherResult>(getActivity(), true){

            @Override
            protected void onCompleteTask(RemindLikeTogetherResult remindLikeResult) {
                if(remindLikeResult.getCode() == BaseResult.SUCCESS){
                    remindLikes.clear();
                    remindLikes.addAll(remindLikeResult.getRemindLikeTogethers());
                    adapter.notifyDataSetChanged();
                }else {
                    if(StringUtil.isBlank(remindLikeResult.getMsg())){
                        showToastMsg("获取列表失败");
                    }else {
                        showToastMsg(remindLikeResult.getMsg());
                    }
                }
            }

            @Override
            protected RemindLikeTogetherResult run(Void... params) {
                return HttpRequestUtil.getInstance().getRemindCommentTogetherList(readPreference("token"));
            }
        }.execute();
    }
    /**
     * 删除提醒
     * @param position
     */
    private void delLike(final int position){
        new BaseHttpAsyncTask<Void, Void, BaseResult>(getActivity(), true){

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
    @Override
    public void onClick(View view) {
        switch (view.getId()){
        }
    }
}
