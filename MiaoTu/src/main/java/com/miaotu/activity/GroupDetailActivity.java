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

import com.miaotu.R;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.result.BaseResult;
import com.miaotu.util.StringUtil;
import com.miaotu.view.CircleImageView;

import java.util.ArrayList;
import java.util.List;

public class GroupDetailActivity extends BaseActivity implements OnClickListener {

	private Button btnLeft;
	private Button btnRight;
	private TextView tvTitle;
    private TextView tvGroupName,tvGroupNo,btnDismiss,tvAnnoncement;

    private CircleImageView ivHead1,ivHead2,ivHead3,ivHead4,ivHead5,ivHead6,ivHead7,ivHead8,ivHead9;
    private List<CircleImageView> headPhotoViewList;
    private ListView lvGroupMember;
    private String groupImId;

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
		btnLeft = null;
		tvTitle = null;
	}

	private void findView() {
		btnLeft = (Button) findViewById(R.id.btn_left);
		tvTitle = (TextView) findViewById(R.id.tv_title);

        tvGroupName = (TextView) findViewById(R.id.tv_group_name);
        tvGroupNo = (TextView) findViewById(R.id.tv_group_no);
        btnDismiss = (TextView) findViewById(R.id.btn_dismiss);
        tvAnnoncement = (TextView) findViewById(R.id.tv_annoncement);
        lvGroupMember = (ListView) findViewById(R.id.lv_group_member);
        ivHead1 = (CircleImageView) findViewById(R.id.iv_head_photo1);
        ivHead2 = (CircleImageView) findViewById(R.id.iv_head_photo2);
        ivHead3 = (CircleImageView) findViewById(R.id.iv_head_photo3);
        ivHead4 = (CircleImageView) findViewById(R.id.iv_head_photo4);
        ivHead5 = (CircleImageView) findViewById(R.id.iv_head_photo5);
        ivHead6 = (CircleImageView) findViewById(R.id.iv_head_photo6);
        ivHead7 = (CircleImageView) findViewById(R.id.iv_head_photo7);
        ivHead8 = (CircleImageView) findViewById(R.id.iv_head_photo8);
        ivHead9 = (CircleImageView) findViewById(R.id.iv_head_photo9);
        headPhotoViewList = new ArrayList<CircleImageView>();
        headPhotoViewList.add(ivHead1);
        headPhotoViewList.add(ivHead2);
        headPhotoViewList.add(ivHead3);
        headPhotoViewList.add(ivHead4);
        headPhotoViewList.add(ivHead5);
        headPhotoViewList.add(ivHead6);
        headPhotoViewList.add(ivHead7);
        headPhotoViewList.add(ivHead8);
        headPhotoViewList.add(ivHead9);
	}

	private void bindView() {
		btnLeft.setVisibility(View.VISIBLE);
		tvTitle.setVisibility(View.VISIBLE);
		btnLeft.setOnClickListener(this);
		btnRight.setOnClickListener(this);
        btnDismiss.setOnClickListener(this);
	}

	private void initView() {
		tvTitle.setText("团聊");
	}
    private void init(){
        groupImId = getIntent().getStringExtra("groupImId");
        getDetail();
    }
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_left:
			GroupDetailActivity.this.finish();
			break;
            case R.id.tv_right:
//			    Intent intent = new Intent(GroupDetailActivity.this,GroupEditActivity.class);
//                intent.putExtra("groupImId",groupImId);
//                startActivityForResult(intent, 1);
			break;
        case R.id.btn_dismiss:
            //解散团聊
            break;
		}
	}
    private void getDetail() {
//        new BaseHttpAsyncTask<Void, Void, GroupDetailResult>(this, true) {
//            @Override
//            protected void onCompleteTask(GroupDetailResult result) {
//                if(tvTitle==null){
//                    return;
//                }
//                if(result.getCode()== BaseResult.SUCCESS){
//                    writeDetail(result);
//                }else{
//                    if(!StringUtil.isEmpty(result.getMsg())){
//                        showToastMsg(result.getMsg());
//                    }else{
//                        showToastMsg("获取群聊信息失败！");
//                    }
//                }
//            }
//
//            @Override
//            protected GroupDetailResult run(Void... params) {
//                return HttpRequestUtil.getInstance().getGroupDetail(
//                        readPreference("token"), groupImId);
//            }
//
//            @Override
//            protected void onError() {
//            }
//
//        }.execute();
    }
//    private void writeDetail(GroupDetailResult result){
//        tvGroupName.setText(result.getGroup().getName());
//        tvGroupNo.setText("团号"+result.getGroup().getId());
//        tvAnnoncement.setText(result.getGroup().getNotice());
//        if(result.getGroup().getIdentity().equals("owner")){
//            btnDismiss.setVisibility(View.VISIBLE);
//            btnRight.setText("管理");
//            btnRight.setVisibility(View.VISIBLE);
//            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) btnRight.getLayoutParams();
//            params.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
//            params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
//            btnRight.setLayoutParams(params);
//        }else{
//            btnDismiss.setVisibility(View.GONE);
//        }
//        int i = 0;
//        for(Member m:result.getGroup().getMemberList()){
//            if(i>=9){
//                break;
//            }
//            UrlImageViewHelper.setUrlDrawable(headPhotoViewList.get(i),m.getPhoto().getUrl()+"&size=100x100");
//            headPhotoViewList.get(i).setVisibility(View.VISIBLE);
//            i++;
//        }
//        memberList.clear();
//        memberList.addAll(result.getGroup().getMemberList());
//        groupDetailListAdapter.notifyDataSetChanged();
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case 1:
                if(resultCode==1){
                    getDetail();
                }
                break;
        }
    }
}
