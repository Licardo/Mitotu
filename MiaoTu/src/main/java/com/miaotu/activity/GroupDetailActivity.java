package com.miaotu.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
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
import com.miaotu.view.CircleImageView;

import java.util.ArrayList;
import java.util.List;

public class GroupDetailActivity extends BaseActivity implements OnClickListener {

	private TextView tvLeft;
	private TextView tvRight;
	private TextView tvTitle;
    private TextView tvGroupName,tvGroupNo,btnDismiss,tvAnnoncement;
    private ListView lvGroupMember;
    private String gid;
    private GroupUserAdapter adapter;
    private List<GroupUserInfo> groupUserInfos;
    private TextView tvGroupCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_detail);
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
        tvRight = (TextView) findViewById(R.id.tv_right);
		tvTitle = (TextView) findViewById(R.id.tv_title);

        tvGroupName = (TextView) findViewById(R.id.tv_group_name);
        tvGroupNo = (TextView) findViewById(R.id.tv_group_no);
        btnDismiss = (TextView) findViewById(R.id.btn_dismiss);
        tvAnnoncement = (TextView) findViewById(R.id.tv_annoncement);
        lvGroupMember = (ListView) findViewById(R.id.lv_group_member);
        tvGroupCount = (TextView) findViewById(R.id.tv_groupcount);
	}

	private void bindView() {
        tvRight.setVisibility(View.GONE);
		tvTitle.setVisibility(View.VISIBLE);
        tvLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        btnDismiss.setOnClickListener(this);
	}

	private void initView() {
		tvTitle.setText("群资料");
        tvRight.setText("管理");
	}
    private void init(){
        groupUserInfos = new ArrayList<>();
        adapter = new GroupUserAdapter(this, groupUserInfos);
        lvGroupMember.setAdapter(adapter);
        gid = getIntent().getStringExtra("gid");
        getGroupDetail();
        getGroupUserList();
    }
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_left:
			GroupDetailActivity.this.finish();
			break;
        case R.id.tv_right:
            Intent intent = new Intent(GroupDetailActivity.this,GroupEditActivity.class);
            intent.putExtra("gid",gid);
            intent.putExtra("name",tvGroupName.getText().toString());
            intent.putExtra("notice",tvAnnoncement.getText().toString());
            startActivityForResult(intent, 1);
        break;
        case R.id.btn_dismiss:
            //解散团聊
            break;
		}
	}

    /**
     * 获取团资料详情
     */
    private void getGroupDetail() {
        new BaseHttpAsyncTask<Void, Void, GroupDetailResult>(this, false) {
            @Override
            protected void onCompleteTask(GroupDetailResult result) {
                if(result.getCode()== BaseResult.SUCCESS){
                    writeDetail(result);
                }else{
                    if(!StringUtil.isEmpty(result.getMsg())){
                        showToastMsg(result.getMsg());
                    }else{
                        showToastMsg("获取群聊信息失败！");
                    }
                }
            }

            @Override
            protected GroupDetailResult run(Void... params) {
                return HttpRequestUtil.getInstance().getGroupDetail(
                        readPreference("token"), gid);
            }

        }.execute();
    }
    private void writeDetail(GroupDetailResult result) {
        if (result.getInfolist() == null){
            return;
        }
        GroupDetailInfo info = result.getInfolist();
        tvGroupName.setText(info.getName());
        tvGroupNo.setText("群号：" + info.getGid());
        tvAnnoncement.setText(info.getNotice());
        if(info.getIsowner().equals("true")){
            tvRight.setText("管理");
            tvRight.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case 1:
                if(resultCode==1){
                    getGroupDetail();
                    getGroupUserList();
                }
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
                    groupUserInfos.clear();
                    if (result.getGroupUserInfoList() == null){
                        adapter.notifyDataSetChanged();
                        tvGroupCount.setText("群成员(0)");
                        return;
                    }
                    groupUserInfos.addAll(result.getGroupUserInfoList());
                    adapter.notifyDataSetChanged();
                    tvGroupCount.setText("群成员(" + result.getGroupUserInfoList().size()+")");
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
}
