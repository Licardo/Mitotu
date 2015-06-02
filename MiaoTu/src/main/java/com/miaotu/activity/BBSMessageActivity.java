package com.miaotu.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.miaotu.R;
import com.miaotu.adapter.TopicCommentsAdapter;
import com.miaotu.adapter.TopicMessageAdapter;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.model.PhotoInfo;
import com.miaotu.model.Topic;
import com.miaotu.model.TopicComment;
import com.miaotu.model.TopicMessage;
import com.miaotu.result.BaseResult;
import com.miaotu.result.MessageResult;
import com.miaotu.result.TopicCommentsListResult;
import com.miaotu.result.TopicMessageListResult;
import com.miaotu.util.LogUtil;
import com.miaotu.util.StringUtil;
import com.miaotu.util.Util;
import com.miaotu.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.photoselector.model.PhotoModel;
import com.photoselector.ui.PhotoPreviewActivity;
import com.photoselector.util.CommonUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ying on 2015/3/6.
 */
public class BBSMessageActivity extends BaseActivity implements View.OnClickListener{
    private TextView tvTitle;
    private TextView tvLeft,tvRight;
    private SwipeMenuListView lvTopicMessage;
    private List<TopicMessage> mList;
    private TopicMessageAdapter adapter;
    private static int PAGECOUNT=15;
    private int curPageCount = 0;
    private boolean isLoadMore = false;
    private View layoutMore;
    private String type = "state";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbs_topic_message);
        layoutMore = getLayoutInflater().inflate(R.layout.pull_to_refresh_more, null);
        findView();
        bindView();
        init();
    }

    private void findView() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvLeft = (TextView) findViewById(R.id.tv_left);
        tvRight = (TextView) findViewById(R.id.tv_right);
        lvTopicMessage = (SwipeMenuListView) findViewById(R.id.lv_topic_message);
    }

    private void bindView() {
        tvRight.setOnClickListener(this);
        tvLeft.setOnClickListener(this);
        lvTopicMessage.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index){
                    case 0:
                        deleteMessage(readPreference("token"), mList.get(position).getSmid(), position);
                        break;
                    default:
                        break;
                }

                return false;
            }
        });
        lvTopicMessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(BBSMessageActivity.this, BBSTopicDetailActivity.class);
                intent.putExtra("sid", mList.get(i).getSid());
                startActivity(intent);
                //设置已读状态
                read(false, mList.get(i).getSmid(), i);
            }
        });
        tvLeft.setOnClickListener(this);
    }

    private void init() {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("话题回复");
        tvRight.setText("清空");
        tvLeft.setVisibility(View.VISIBLE);
        tvLeft.setBackgroundResource(R.drawable.icon_back);
        mList=new ArrayList<>();
        adapter = new TopicMessageAdapter(BBSMessageActivity.this,mList);
        lvTopicMessage.setAdapter(adapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(BBSMessageActivity.this);
                deleteItem.setBackground(new ColorDrawable(getResources().getColor(R.color.swipe_delete)));
//                deleteItem.setBackground(R.drawable.icon_msg_delete);
                deleteItem.setWidth(Util.dip2px(BBSMessageActivity.this, 80));
                deleteItem.setTitle("删除");
                deleteItem.setTitleColor(getResources().getColor(R.color.white));
                deleteItem.setTitleSize(14);
                menu.addMenuItem(deleteItem);
            }
        };
        lvTopicMessage.setMenuCreator(creator);

        getMessages(type, false);
    }

    /**
     * 获取消息接口
     * @param isShow
     */
    private void getMessages(final String type, boolean isShow) {
        new BaseHttpAsyncTask<Void, Void, TopicMessageListResult>(BBSMessageActivity.this, isShow) {
            @Override
            protected void onCompleteTask(TopicMessageListResult result) {
                    if(mList==null){
                        return;
                    }
                if (result.getCode() == BaseResult.SUCCESS) {
                    mList.clear();
                    mList.addAll(result.getMessages());
//                    TopicMessage mes = new TopicMessage();
//                    mes.setContent("你好啊");
//                    mes.setCreated(new Date().toString());
//                    mes.setNickname("四小美");
//                    mes.setRemark("hi");
//                    mes.setStatus("0");
//                    mes.setSid("1");
//                    mes.setSmid("1");
//                    mList.add(mes);
                    adapter.notifyDataSetChanged();
//                    showToastMsg("lastvisibale:"+lvTopicMessage.getRefreshableView().getLastVisiblePosition()+"  count: "+lvTopicMessage.getRefreshableView().getCount()+" first:"+lvTopicMessage.getRefreshableView().getFirstVisiblePosition());
                } else {
                    if (StringUtil.isEmpty(result.getMsg())) {
                        showToastMsg("获取话题失败！");
                    } else {
                        showToastMsg(result.getMsg());
                    }
                }
            }

            @Override
            protected TopicMessageListResult run(Void... params) {
                curPageCount=PAGECOUNT;
				return HttpRequestUtil.getInstance().getTopicMessage(readPreference("token"), curPageCount + "",type);
            }

            @Override
            protected void finallyRun() {

                if(mList==null){
                    return;
                }
            }
        }.execute();
    }

    /**
     * 加载更多消息
     */
    private void loadMoreComments() {
        new BaseHttpAsyncTask<Void, Void, TopicMessageListResult>(BBSMessageActivity.this, false) {
            @Override
            protected void onCompleteTask(TopicMessageListResult result) {
                    if(mList==null){
                        return;
                    }
                if (result.getCode() == BaseResult.SUCCESS) {
                    mList.clear();
                    mList.addAll(result.getMessages());
                    adapter.notifyDataSetChanged();
                    if(mList.size()!=curPageCount){
                    }
                } else {
                    if (StringUtil.isEmpty(result.getMsg())) {
                        showToastMsg("获取话题失败！");
                    } else {
                        showToastMsg(result.getMsg());
                    }
                }
            }

            @Override
            protected TopicMessageListResult run(Void... params) {
                isLoadMore = true;
                curPageCount+=PAGECOUNT;
                return HttpRequestUtil.getInstance().getTopicMessage(readPreference("token"), curPageCount + "", type);
            }

            @Override
            protected void finallyRun() {
                if(mList==null){
                    return;
                }
                isLoadMore = false;
            }
        }.execute();
    }
    //标记消息为已读
    private void read(boolean isShow,final String meesageId,final int position) {
        new BaseHttpAsyncTask<Void, Void, MessageResult>(BBSMessageActivity.this, isShow) {
            @Override
            protected void onCompleteTask(MessageResult result) {
                    if(mList==null){
                        return;
                    }
                if (result.getCode() == BaseResult.SUCCESS) {
                    //标记成功
                    if("1".equals(result.getMessage().getStatus())){
                        adapter.notifyDataSetChanged();
                    }
//                    mList.get(position).setStatus("1");
                } else {
                    if (StringUtil.isEmpty(result.getMsg())) {
                        showToastMsg("读取失败");
                    } else {
                        showToastMsg(result.getMsg());
                    }
                }
            }

            @Override
            protected MessageResult run(Void... params) {
                curPageCount=PAGECOUNT;
                return HttpRequestUtil.getInstance().readTopicMessage(readPreference("token"), meesageId);
            }
        }.execute();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_left:
                finish();
                break;
            case R.id.tv_right: //清空
                final Dialog dialog = new AlertDialog.Builder(BBSMessageActivity.this).create();
                dialog.setCancelable(true);
                dialog.show();
                dialog.setContentView(R.layout.dialog_message_empty);
                Button btnCancle = (Button) dialog.findViewById(R.id.btn_cancel);
                Button btnConfirm = (Button) dialog.findViewById(R.id.btn_confirm);
                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        emptyMessages(readPreference("token"));
                        dialog.dismiss();
                    }
                });
                btnCancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                break;
        }
    }

    /**
     * 清空消息
     * @param token
     */
    private void emptyMessages(final String token){
        new BaseHttpAsyncTask<Void, Void, BaseResult>(this, false){

            @Override
            protected void onCompleteTask(BaseResult baseResult) {
                if(baseResult.getCode() == BaseResult.SUCCESS){
                    showToastMsg("操作成功");
                    mList.clear();
                    adapter.notifyDataSetChanged();
                }else {
                    if(StringUtil.isBlank(baseResult.getMsg())){
                        showToastMsg("操作失败");
                    }else{
                        showToastMsg(baseResult.getMsg());
                    }
                }
            }

            @Override
            protected BaseResult run(Void... params) {
                return HttpRequestUtil.getInstance().emptyTopicMessage(token, type);
            }
        }.execute();
    }

    /**
     * 删除消息
     * @param token
     * @param smid
     */
    private void deleteMessage(final String token, final String smid, final int postion){
        new BaseHttpAsyncTask<Void, Void, MessageResult>(BBSMessageActivity.this, false){

            @Override
            protected void onCompleteTask(MessageResult baseResult) {
                if(baseResult.getCode() == 100){
                    if("-1".equals(baseResult.getMessage().getStatus())){
                        mList.remove(postion);
                        adapter.notifyDataSetChanged();
                    }
                    showToastMsg("操作成功");
                }else {
                    if(StringUtil.isBlank(baseResult.getMsg())){
                        showToastMsg("操作失败");
                    }else {
                        showToastMsg(baseResult.getMsg());
                    }
                }
            }

            @Override
            protected MessageResult run(Void... params) {
                return HttpRequestUtil.getInstance().deleteTopicMessage(token, smid);
            }
        }.execute();
    }
}
