package com.miaotu.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.miaotu.R;
import com.miaotu.adapter.BlackListAdapter;
import com.miaotu.adapter.MyLikeAdapter;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.model.BlackInfo;
import com.miaotu.result.BaseResult;
import com.miaotu.result.BlackResult;
import com.miaotu.util.StringUtil;
import com.miaotu.util.Util;

import java.util.ArrayList;
import java.util.List;

public class MyLikeFragment extends BaseFragment implements View.OnClickListener{

    private SwipeMenuListView lvBlackList;
    private List<BlackInfo> blackInfoList;
    private MyLikeAdapter adapter;
    private View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        root = inflater.inflate(R.layout.fragment_my_like, container, false);
        initView();
        initData();
        return root;
    }

    private void initView(){
        lvBlackList = (SwipeMenuListView) root.findViewById(R.id.lv_blacklist);
    }

    private void initData(){
        blackInfoList = new ArrayList<>();
        adapter = new MyLikeAdapter(this.getActivity(), blackInfoList);
        lvBlackList.setAdapter(adapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(MyLikeFragment.this.getActivity());
                deleteItem.setBackground(new ColorDrawable(getResources().getColor(R.color.swipe_delete)));
//                deleteItem.setBackground(R.drawable.icon_msg_delete);
                deleteItem.setWidth(Util.dip2px(MyLikeFragment.this.getActivity(), 80));
                deleteItem.setTitle("取消关注");
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
                        //取消关注
                        like(position);
                        break;
                    default:
                        break;
                }

                return false;
            }
        });

        getLikeList();
    }

    /**
     * 获取关注列表
     */
    public void getLikeList(){
        new BaseHttpAsyncTask<Void, Void, BlackResult>(this.getActivity(), true){

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
                return HttpRequestUtil.getInstance().getLikeList(readPreference("token"),
                        readPreference("uid"));
            }
        }.execute();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            default:
                break;
        }
    }

    /**
     * 添加/取消喜欢接口
     */
    private void like(final int position) {

        new BaseHttpAsyncTask<Void, Void, BaseResult>(MyLikeFragment.this.getActivity(), false) {

            @Override
            protected void onCompleteTask(BaseResult baseResult) {
                if (baseResult.getCode() == BaseResult.SUCCESS) {
                    blackInfoList.remove(position);
                    adapter.notifyDataSetChanged();
                    writePreference("followcount",
                            (Integer.parseInt(readPreference("followcount"))-1)+"");
                } else {
                    if (StringUtil.isBlank(baseResult.getMsg())) {
                        showToastMsg("操作失败");
                    } else {
                        showToastMsg(baseResult.getMsg());
                    }
                }
            }

            @Override
            protected BaseResult run(Void... params) {
                return HttpRequestUtil.getInstance().like(readPreference("token"),
                        blackInfoList.get(position).getUid());
            }
        }.execute();
    }
}
