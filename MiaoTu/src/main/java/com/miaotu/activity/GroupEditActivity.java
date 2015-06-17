package com.miaotu.activity;


import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.miaotu.R;
import com.miaotu.adapter.GroupUserAdapter;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.model.GroupDetailInfo;
import com.miaotu.model.GroupUserInfo;
import com.miaotu.result.BaseResult;
import com.miaotu.result.GroupDetailResult;
import com.miaotu.result.GroupUserListResult;
import com.miaotu.util.StringUtil;
import com.miaotu.util.Util;

import java.util.ArrayList;
import java.util.List;

public class GroupEditActivity extends BaseActivity implements OnClickListener {

	private TextView tvLeft;
	private TextView tvTitle;
    private EditText etGroupName,etAnnoncement;

    private SwipeMenuListView lvGroupMember;
    private GroupUserAdapter adapter;
    private List<GroupUserInfo> memberList;
    private String gid;
    private TextView tvGroupCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_edit);
		findView();
		bindView();
		initView();
        init();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
        tvLeft = null;
		tvTitle = null;
	}

	private void findView() {
        tvLeft = (TextView) findViewById(R.id.tv_left);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvGroupCount = (TextView) findViewById(R.id.tv_groupcount);

        etGroupName = (EditText) findViewById(R.id.et_groupname);
        etAnnoncement = (EditText) findViewById(R.id.et_groupnotice);
        lvGroupMember = (SwipeMenuListView) findViewById(R.id.iv_group_member);
	}

	private void bindView() {
		tvLeft.setVisibility(View.VISIBLE);
		tvTitle.setVisibility(View.VISIBLE);
		tvLeft.setOnClickListener(this);
	}

	private void initView() {
		tvTitle.setText("管理");
	}
    private void init(){
        writeDetail();
        gid = getIntent().getStringExtra("gid");
        memberList = new ArrayList<>();
        adapter = new GroupUserAdapter(this,memberList);
        lvGroupMember.setAdapter(adapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(GroupEditActivity.this);
                deleteItem.setBackground(new ColorDrawable(getResources().getColor(R.color.swipe_delete)));
                deleteItem.setWidth(Util.dip2px(GroupEditActivity.this, 80));
                deleteItem.setTitle("移除");
                deleteItem.setTitleColor(getResources().getColor(R.color.white));
                deleteItem.setTitleSize(14);
                menu.addMenuItem(deleteItem);
            }
        };
        lvGroupMember.setMenuCreator(creator);

        lvGroupMember.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        removeGroupUser(position);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        getGroupUserList();
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		    case R.id.tv_left:
                if (getIntent().getStringExtra("name").equals(
                        etGroupName.getText().toString().trim())&&
                        getIntent().getStringExtra("notice").equals(
                                etAnnoncement.getText().toString().trim())){
                    finish();
                    return;
                }
                editGroupDetail(etGroupName.getText().toString().trim(),
                        etAnnoncement.getText().toString().trim());
//			    GroupEditActivity.this.finish();
			    break;
		}
	}
    /**
     * 获取群用户信息
     */
    private void getGroupUserList(){
        new BaseHttpAsyncTask<Void, Void, GroupUserListResult>(this, true) {
            @Override
            protected void onCompleteTask(GroupUserListResult result) {
                if(result.getCode()== BaseResult.SUCCESS){
                    memberList.clear();
                    if (result.getGroupUserInfoList() == null){
                        adapter.notifyDataSetChanged();
                        tvGroupCount.setText("群成员(0)");
                        return;
                    }
                    memberList.addAll(result.getGroupUserInfoList());
                    adapter.notifyDataSetChanged();
                    tvGroupCount.setText("群成员(" + result.getGroupUserInfoList().size() + ")");
                }else{
                    if(!StringUtil.isEmpty(result.getMsg())){
                        showToastMsg(result.getMsg());
                    }else{
                        showToastMsg("获取群用户失败！");
                    }
                }
            }

            @Override
            protected GroupUserListResult run(Void... params) {
                return HttpRequestUtil.getInstance().getGroupList(
                        readPreference("token"), gid);
            }
        }.execute();
    }

    private void writeDetail() {
        etGroupName.setText(getIntent().getStringExtra("name"));
        etAnnoncement.setText(getIntent().getStringExtra("notice"));
    }

    /**
     *群组详细信息编辑
     * @param name
     * @param annoncement
     */
    private void editGroupDetail(final String name,final String annoncement){
        new BaseHttpAsyncTask<Void, Void, BaseResult>(this, true) {
            @Override
            protected void onCompleteTask(BaseResult result) {
                if(result.getCode()== BaseResult.SUCCESS){
                    showToastMsg("编辑成功");
                    setResult(1);
                    GroupEditActivity.this.finish();
                }else{
                    if(!StringUtil.isEmpty(result.getMsg())){
                        showToastMsg(result.getMsg());
                    }else{
                        showToastMsg("修改群聊信息失败！");
                    }
                }
            }

            @Override
            protected BaseResult run(Void... params) {
                return HttpRequestUtil.getInstance().editGroupDetail(
                        readPreference("token"), gid,name,annoncement);
            }

        }.execute();
    }

    /**
     *从群组中移除用户
     * @param position
     */
    private void removeGroupUser(final int position){
        new BaseHttpAsyncTask<Void, Void, BaseResult>(this, true) {
            @Override
            protected void onCompleteTask(BaseResult result) {
                if(result.getCode()== BaseResult.SUCCESS){
                    showToastMsg("编辑成功");
                    memberList.remove(position);
                    setResult(1);
                }else{
                    if(!StringUtil.isEmpty(result.getMsg())){
                        showToastMsg(result.getMsg());
                    }else{
                        showToastMsg("修改群聊信息失败！");
                    }
                }
            }

            @Override
            protected BaseResult run(Void... params) {
                return HttpRequestUtil.getInstance().removeGroupUser(
                        readPreference("token"), gid,
                        memberList.get(position).getUid());
            }

        }.execute();
    }
}
