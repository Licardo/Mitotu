package com.miaotu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.miaotu.R;
import com.miaotu.adapter.TopicCommentsAdapter;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.model.LikeInfo;
import com.miaotu.model.PhotoInfo;
import com.miaotu.model.Topic;
import com.miaotu.model.TopicComment;
import com.miaotu.result.BaseResult;
import com.miaotu.result.LikeResult;
import com.miaotu.result.TopicCommentsListResult;
import com.miaotu.result.TopicResult;
import com.miaotu.util.LogUtil;
import com.miaotu.util.StringUtil;
import com.miaotu.util.Util;
import com.miaotu.view.CircleImageView;
import com.miaotu.view.DraggableGridView;
import com.miaotu.view.FlowLayout;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.photoselector.model.PhotoModel;
import com.photoselector.ui.PhotoPreviewActivity;
import com.photoselector.util.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hao on 2015/3/6.
 */
public class BBSTopicDetailActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvTitle;
    private TextView tvLeft;
    private PullToRefreshListView lvTopics;
    private List<TopicComment> commentList;
    private TopicCommentsAdapter adapter;
    private static int PAGECOUNT = 10;
    private int curPageCount = 0;
    private boolean isLoadMore = false;
    private View layoutMore;
    private Topic topic;
    private EditText etComment;
    private TextView tvPublishComment;
    private TextView tvTipComment;
    private int isLike; //1001喜欢,1002取消喜欢
    private int likecount;  //记录喜欢数量
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbs_topic_detail);
        findView();
        bindView();
        init();
    }

    private void findView() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvLeft = (TextView) findViewById(R.id.tv_left);
        lvTopics = (PullToRefreshListView) findViewById(R.id.lv_topic_comments);
        etComment = (EditText) findViewById(R.id.et_comment);
        tvPublishComment = (TextView) findViewById(R.id.tv_publish_comment);
        layoutMore = getLayoutInflater().inflate(R.layout.pull_to_refresh_more, null);
    }

    private void bindView() {
        tvLeft.setOnClickListener(this);
        tvTitle.setSingleLine(true);
        tvTitle.setEllipsize(TextUtils.TruncateAt.MIDDLE);
        tvTitle.setMaxEms(8);
        tvTitle.setText("动态正文");
        lvTopics.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(
                        BBSTopicDetailActivity.this, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
                                | DateUtils.FORMAT_ABBREV_ALL);

                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                getComments(false);

            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                loadMoreComments();
            }

        });
        lvTopics.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
//                showToastMsg("滑动到底了");
                if (!isLoadMore && commentList.size() == curPageCount) {
                    loadMoreComments();
                }
            }

        });
        etComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //有内容时候发送按钮变色
                if (!StringUtil.isBlank(etComment.getText().toString())) {
                    tvPublishComment.setBackgroundColor(getResources().getColor(R.color.invite_status_invite_me));
                    tvPublishComment.setTextColor(getResources().getColor(R.color.white));
                } else {
                    tvPublishComment.setBackgroundColor(getResources().getColor(R.color.white));
                    tvPublishComment.setTextColor(getResources().getColor(R.color.grey64));
                }
            }
        });

        tvPublishComment.setOnClickListener(this);

    }

    private void init() {
        topic = (Topic) getIntent().getSerializableExtra("topic");

        tvTitle.setVisibility(View.VISIBLE);
        tvLeft.setVisibility(View.VISIBLE);
        tvLeft.setBackgroundResource(R.drawable.icon_back);
        commentList = new ArrayList<>();
        adapter = new TopicCommentsAdapter(BBSTopicDetailActivity.this, commentList);
        lvTopics.setAdapter(adapter);
        if (topic != null) {
            LayoutInflater inflater = LayoutInflater.from(this);
            view = inflater.inflate(R.layout.item_topic_detail, null);
//            tvTitle.setText(topic.getTitle());
            tvTipComment = (TextView) view.findViewById(R.id.tv_tip_comment);
            ((TextView) view.findViewById(R.id.tv_nickname)).setText(topic.getNickname());
            UrlImageViewHelper.setUrlDrawable((CircleImageView) view.findViewById(R.id.iv_head_photo),
                    topic.getHead_url() + "100x100",
                    R.drawable.default_avatar);
            view.findViewById(R.id.iv_head_photo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!Util.isNetworkConnected(BBSTopicDetailActivity.this)) {
                        showToastMsg("当前未联网，请检查网络设置");
                        return;
                    }
                    Intent intent = new Intent(BBSTopicDetailActivity.this, PersonCenterActivity.class);
                    intent.putExtra("uid", topic.getUid());
//                    startActivity(intent);
                    startActivityForResult(intent, 1001);
                }
            });
            ((TextView) view
                    .findViewById(R.id.tv_title)).setText("标题");
            ((TextView) view
                    .findViewById(R.id.tv_content)).setText(topic.getContent());
            LinearLayout layoutPhotos = (LinearLayout) view.findViewById(R.id.layout_photos);
            layoutPhotos.removeAllViews();
            int j = 0;
            LinearLayout layoutTemp = new LinearLayout(this);
            layoutTemp.setOrientation(LinearLayout.HORIZONTAL);
            layoutPhotos.addView(layoutTemp);
            final ArrayList<PhotoModel> photoList = new ArrayList<>();
            for (int i = 0; i < topic.getPiclist().size(); i++) {
                PhotoInfo photoInfo = topic.getPiclist().get(i);
                PhotoModel photoModel = new PhotoModel();
                photoModel.setOriginalPath(photoInfo.getUrl());
                photoList.add(photoModel);
                ImageView imageView = new ImageView(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Util.dip2px(this, 70), Util.dip2px(this, 70));
                params.leftMargin = Util.dip2px(this, 10);
                imageView.setLayoutParams(params);
                UrlImageViewHelper.setUrlDrawable(imageView,
                        photoInfo.getUrl() + "210x210",
                        R.drawable.icon_default_image);
                imageView.setTag(i);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //点击照片
                        /** 预览照片 */
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("photos", photoList);
                        bundle.putSerializable("position", (int) view.getTag());
                        CommonUtils.launchActivity(BBSTopicDetailActivity.this, PhotoPreviewActivity.class, bundle);
                    }
                });
                if (j == 3) {
                    layoutTemp = new LinearLayout(this);
                    LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    params1.topMargin = Util.dip2px(this, 10);
                    layoutTemp.setLayoutParams(params1);
                    layoutTemp.setOrientation(LinearLayout.HORIZONTAL);
                    layoutPhotos.addView(layoutTemp);
                    j = 0;
                }
                layoutTemp.addView(imageView);
                j++;
            }
            if (topic.getPiclist().size() == 0) {
                layoutPhotos.setVisibility(View.GONE);
            }
            if(StringUtil.isBlank(topic.getTitle())){
                view.findViewById(R.id.tv_movement_name).setVisibility(View.GONE);
            }
            ((TextView) view
                    .findViewById(R.id.tv_movement_name)).setText("@" + topic.getTitle());
            view.findViewById(R.id.tv_movement_name).setOnClickListener(this);
            if (!StringUtil.isBlank(topic.getDistance())){
                ((TextView) view
                        .findViewById(R.id.tv_comment_count)).setText(topic.getDistance()+"km");
            }
            ((TextView) view
                    .findViewById(R.id.tv_top_date)).setText(topic.getCreated());
            ImageView ivLike = (ImageView) view.findViewById(R.id.iv_like);


            if ("false".equals(topic.getIslike())) {
                ivLike.setBackgroundResource(R.drawable.icon_friend_dislike);
                int pos = isExist(topic);
                if (pos != -1){
                    topic.getLikelist().remove(pos);
                }
            } else {
                ivLike.setBackgroundResource(R.drawable.icon_friend_like);
                if (isExist(topic) == -1){
                    LikeInfo info = new LikeInfo();
                    info.setUid(readPreference("uid"));
                    info.setHeadurl(readPreference("headphoto"));
                    likecount = topic.getLikelist().size();
                    topic.getLikelist().add(info);
                }
            }
            ivLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (topic == null){
                        topic = new Topic();
                    }
                    if ("false".equals(topic.getIslike())) {
                        likeState(topic.getSid(), false, (ImageView) v);   //添加喜欢
                        topic.setIslike("true");
                    } else {
                        likeState(topic.getSid(), true, (ImageView) v);    //取消喜欢
                        topic.setIslike("false");
                    }
                }
            });

            if(topic.getLikelist().size() > 0){
                for(int i=0;i<topic.getLikelist().size();i++) {
                    modifyLikelist(view, i, true);
                }
            }else{
                view.findViewById(R.id.ll_likeuser).setVisibility(View.GONE);
            }
            likecount = topic.getLikelist().size();
            lvTopics.getRefreshableView().addHeaderView(view);
            getComments(false);
        } else {
            String sid = getIntent().getStringExtra("sid");
            getDetail(true, sid);
        }
    }

    private void getComments(boolean isShow) {
        new BaseHttpAsyncTask<Void, Void, TopicCommentsListResult>(BBSTopicDetailActivity.this, isShow) {
            @Override
            protected void onCompleteTask(TopicCommentsListResult result) {
                if (commentList == null) {
                    return;
                }
                if (result.getCode() == BaseResult.SUCCESS) {
                    commentList.clear();
                    if(result.getComment() == null){
                        return;
                    }
                    commentList.addAll(result.getComment());
                    adapter.notifyDataSetChanged();
                    if (commentList.size() < 1){
                        if (tvTipComment != null){
                            tvTipComment.setVisibility(View.GONE);
                        }
                    }

                    if (lvTopics.getRefreshableView().getFooterViewsCount() == 1 && commentList.size() == PAGECOUNT) {
                        lvTopics.getRefreshableView().addFooterView(layoutMore);
                    }
                } else {
                    if (StringUtil.isEmpty(result.getMsg())) {
                        showToastMsg("获取话题列表失败！");
                    } else {
                        showToastMsg(result.getMsg());
                    }
                }
            }

            @Override
            protected TopicCommentsListResult run(Void... params) {
                curPageCount = PAGECOUNT;
                return HttpRequestUtil.getInstance().getTopicComments(topic.getSid(),
                        readPreference("token"), curPageCount+"");
            }

            @Override
            protected void finallyRun() {
                if (commentList == null) {
                    return;
                }
                lvTopics.onRefreshComplete();
            }
        }.execute();
    }

    private void loadMoreComments() {
        new BaseHttpAsyncTask<Void, Void, TopicCommentsListResult>(BBSTopicDetailActivity.this, false) {
            @Override
            protected void onCompleteTask(TopicCommentsListResult result) {
                if (commentList == null) {
                    return;
                }
                if (result.getCode() == BaseResult.SUCCESS) {
                    commentList.clear();
                    commentList.addAll(result.getComment());
                    adapter.notifyDataSetChanged();
                    if (commentList.size() != curPageCount) {
                        lvTopics.getRefreshableView().removeFooterView(layoutMore);
                    }
                } else {
                    if (StringUtil.isEmpty(result.getMsg())) {
                        showToastMsg("获取话题列表失败！");
                    } else {
                        showToastMsg(result.getMsg());
                    }
                }
            }

            @Override
            protected TopicCommentsListResult run(Void... params) {
                isLoadMore = true;
                curPageCount += PAGECOUNT;
                return HttpRequestUtil.getInstance().getTopicComments(topic.getSid(),
                        readPreference("token"), curPageCount + "");
            }

//            @Override
//            protected void onError() {
//                // TODO Auto-generated method stub
//
//            }

            @Override
            protected void finallyRun() {
                if (commentList == null) {
                    return;
                }
                lvTopics.onRefreshComplete();
                isLoadMore = false;
            }
        }.execute();
    }

    /**
     * 获取妙友动态详情
     * @param isShow
     * @param sid
     */
    private void getDetail(boolean isShow, final String sid) {
        new BaseHttpAsyncTask<Void, Void, TopicResult>(BBSTopicDetailActivity.this, isShow) {
            @Override
            protected void onCompleteTask(TopicResult result) {
                if (commentList == null) {
                    return;
                }
                if (result.getCode() == BaseResult.SUCCESS) {
                    topic = result.getTopic();
                    /*tvTitle.setText(topic.getTitle());*/
                    LayoutInflater inflater = LayoutInflater.from(BBSTopicDetailActivity.this);
                    view = inflater.inflate(R.layout.item_topic_detail, null);
                    tvTipComment = (TextView) view.findViewById(R.id.tv_tip_comment);
                    ((TextView) view.findViewById(R.id.tv_nickname)).setText(topic.getNickname());
                    UrlImageViewHelper.setUrlDrawable((CircleImageView) view.findViewById(R.id.iv_head_photo),
                            topic.getHead_url() + "100x100",
                            R.drawable.default_avatar);
                    view.findViewById(R.id.iv_head_photo).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(!Util.isNetworkConnected(BBSTopicDetailActivity.this)) {
                                showToastMsg("当前未联网，请检查网络设置");
                                return;
                            }
                            Intent intent = new Intent(BBSTopicDetailActivity.this, PersonCenterActivity.class);
                            intent.putExtra("uid",topic.getUid());
                            startActivityForResult(intent, 1001);
                        }
                    });
                    /*((TextView) view
                            .findViewById(R.id.tv_title)).setText(topic.getTitle());*/
                    ((TextView) view
                            .findViewById(R.id.tv_content)).setText(topic.getContent());
                    LinearLayout layoutPhotos = (LinearLayout) view.findViewById(R.id.layout_photos);
                    layoutPhotos.removeAllViews();
                    int j = 0;
                    LinearLayout layoutTemp = new LinearLayout(BBSTopicDetailActivity.this);
                    layoutTemp.setOrientation(LinearLayout.HORIZONTAL);
                    layoutPhotos.addView(layoutTemp);
                    final ArrayList<PhotoModel> photoList = new ArrayList<>();
                    for (int i = 0; i < topic.getPiclist().size(); i++) {
                        PhotoInfo photoInfo = topic.getPiclist().get(i);
                        PhotoModel photoModel = new PhotoModel();
                        photoModel.setOriginalPath(photoInfo.getUrl());
                        photoList.add(photoModel);
                        ImageView imageView = new ImageView(BBSTopicDetailActivity.this);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                Util.dip2px(BBSTopicDetailActivity.this, 70),
                                Util.dip2px(BBSTopicDetailActivity.this, 70));
                        params.leftMargin = Util.dip2px(BBSTopicDetailActivity.this, 10);
                        imageView.setLayoutParams(params);
                        UrlImageViewHelper.setUrlDrawable(imageView,
                                photoInfo.getUrl() + "210x210",
                                R.drawable.icon_default_image);
                        imageView.setTag(i);
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //点击照片
                                /** 预览照片 */
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("photos", photoList);
                                bundle.putSerializable("position", (int) view.getTag());
                                CommonUtils.launchActivity(BBSTopicDetailActivity.this, PhotoPreviewActivity.class, bundle);
                            }
                        });
                        if (j == 3) {
                            layoutTemp = new LinearLayout(BBSTopicDetailActivity.this);
                            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            params1.topMargin = Util.dip2px(BBSTopicDetailActivity.this, 10);
                            layoutTemp.setLayoutParams(params1);
                            layoutTemp.setOrientation(LinearLayout.HORIZONTAL);
                            layoutPhotos.addView(layoutTemp);
                            j = 0;
                        }
                        layoutTemp.addView(imageView);
                        j++;
                    }
                    if (topic.getPiclist().size() == 0) {
                        layoutPhotos.setVisibility(View.GONE);
                    }
                    if(StringUtil.isBlank(topic.getTitle())){
                        view.findViewById(R.id.tv_movement_name).setVisibility(View.GONE);
                    }
                    ((TextView) view
                            .findViewById(R.id.tv_movement_name)).setText("@" + topic.getTitle());
                    view.findViewById(R.id.tv_movement_name).setOnClickListener(BBSTopicDetailActivity.this);
                    if (StringUtil.isBlank(topic.getDistance())){
                        ((TextView) view
                                .findViewById(R.id.tv_comment_count)).setVisibility(View.GONE);
                    }
                    ((TextView) view
                            .findViewById(R.id.tv_comment_count)).setText(topic.getDistance()+"km");
                    ((TextView) view
                            .findViewById(R.id.tv_date)).setText(topic.getCreated());
                    ImageView ivLike = (ImageView) view.findViewById(R.id.iv_like);
                    if ("false".equals(topic.getIslike())) {
                        ivLike.setBackgroundResource(R.drawable.icon_friend_dislike);
                        int pos = isExist(topic);
                        if (pos != -1){
                            topic.getLikelist().remove(pos);
                        }
                    } else {
                        ivLike.setBackgroundResource(R.drawable.icon_friend_like);
                        if (isExist(topic) == -1){
                            LikeInfo info = new LikeInfo();
                            info.setUid(readPreference("uid"));
                            info.setHeadurl(readPreference("headphoto"));
                            likecount = topic.getLikelist().size();
                            topic.getLikelist().add(info);
                        }
                    }
                    ivLike.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (topic == null){
                                topic = new Topic();
                            }
                            if ("false".equals(topic.getIslike())) {
                                likeState(topic.getSid(), false, (ImageView) v);   //添加喜欢
                                topic.setIslike("true");
                            } else {
                                likeState(topic.getSid(), true, (ImageView) v);    //取消喜欢
                                topic.setIslike("false");
                            }
                        }
                    });

                    if(topic.getLikelist().size() > 0){
                        for(int i=0;i<topic.getLikelist().size();i++){
                            modifyLikelist(view, i, true);
                        }
                    }else{
                        view.findViewById(R.id.ll_likeuser).setVisibility(View.GONE);
                    }
                    likecount = topic.getLikelist().size();
                    lvTopics.getRefreshableView().addHeaderView(view);
                    getComments(false);

                } else {
                    if (StringUtil.isEmpty(result.getMsg())) {
                        showToastMsg("获取话题失败！");
                    } else {
                        showToastMsg(result.getMsg());
                    }
                }
            }

            @Override
            protected TopicResult run(Void... params) {
                return HttpRequestUtil.getInstance().getTopicDetail(sid,
                        readPreference("token"));
            }

            @Override
            protected void finallyRun() {
                if (commentList == null) {
                    return;
                }
                lvTopics.onRefreshComplete();
            }
        }.execute();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_publish_comment:
                if (!StringUtil.isBlank(etComment.getText().toString())) {
                    publishComment();
                }
                break;
            case R.id.tv_movement_name:
                Intent intent = new Intent(BBSTopicDetailActivity.this,CustomTourDetailActivity.class);
                intent.putExtra("id",topic.getAid());
                startActivity(intent);
                break;
            case R.id.tv_left:
                setResult(isLike);
                this.finish();
                break;
        }
    }

    private void publishComment() {
        //发表评论
        if (!readPreference("login_status").equals("in")) {
            Intent intent = new Intent(BBSTopicDetailActivity.this, LoginActivity.class);
            startActivity(intent);
            return;
        } else {
            new BaseHttpAsyncTask<Void, Void, BaseResult>(BBSTopicDetailActivity.this, true) {
                @Override
                protected void onCompleteTask(BaseResult result) {
                    if (commentList == null) {
                        return;
                    }
                    if (result.getCode() == BaseResult.SUCCESS) {
                        showToastMsg("评论成功！");
                        etComment.setText("");
                        getComments(false);
                    } else {
                        if (StringUtil.isEmpty(result.getMsg())) {
                            showToastMsg("评论话题失败！");
                        } else {
                            showToastMsg(result.getMsg());
                        }
                    }
                }

                @Override
                protected BaseResult run(Void... params) {
                    return HttpRequestUtil.getInstance().publishComment(readPreference("token"), etComment.getText().toString(), topic.getSid());
                }

//                @Override
//                protected void onError() {
//                    // TODO Auto-generated method stub
//
//                }
            }.execute();
        }
    }

    /**
     * 添加/取消喜欢妙友状态的接口
     * @param sid
     * @param islike
     * @param iv
     */
    private void likeState(final String sid, final boolean islike, final ImageView iv) {

        new BaseHttpAsyncTask<Void, Void, BaseResult>(this, true) {

            @Override
            protected void onCompleteTask(BaseResult baseResult) {
                if (baseResult.getCode() == BaseResult.SUCCESS) {
                    if(!islike){
                        showToastMsg("喜欢成功");
                        iv.setBackgroundResource(R.drawable.icon_friend_like);
                        isLike = 1001;

                        topic.setIslike("true");
                        LikeInfo info = new LikeInfo();
                        info.setUid(readPreference("uid"));
                        info.setHeadurl(readPreference("headphoto"));
                        likecount = topic.getLikelist().size();
                        topic.getLikelist().add(info);
                        modifyLikelist(view, likecount, true);
                        view.findViewById(R.id.ll_likeuser).setVisibility(View.VISIBLE);

                    }else {
                        showToastMsg("取消喜欢成功");
                        iv.setBackgroundResource(R.drawable.icon_friend_dislike);
                        isLike = 1002;

                        int i = 0;

                        for (LikeInfo info:topic.getLikelist()){
                            if(readPreference("uid").equals(info.getUid())){
                                topic.getLikelist().remove(info);
                                //移除头像
                                modifyLikelist(view, i, false);
                                break;
                            }
                            i++;
                        }
                        if (topic == null || topic.getLikelist().size() == 0){
                            view.findViewById(R.id.ll_likeuser).setVisibility(View.GONE);
                        }
                    }
                    setResult(isLike);
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
                return HttpRequestUtil.getInstance().likeState(readPreference("token"), sid);
            }
        }.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 添加到喜欢的列表中
     * @param view
     * @param position
     */
    private void modifyLikelist(View view, final int position, boolean flag){
        LinearLayout llLikePhoto = (LinearLayout) view.findViewById(R.id.ll_likephoto);
        if (flag) {

            CircleImageView imageView = new CircleImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Util.dip2px(this, 30), Util.dip2px(this, 30));
            params.rightMargin = Util.dip2px(this, 10);
            imageView.setLayoutParams(params);
            UrlImageViewHelper.setUrlDrawable(imageView,
                    topic.getLikelist().get(position).getHeadurl() + "100×100",
                    R.drawable.default_avatar);
            imageView.setTag(position);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!Util.isNetworkConnected(BBSTopicDetailActivity.this)) {
                        showToastMsg("当前未联网，请检查网络设置");
                        return;
                    }
                    int pos = (int) view.getTag();
                    Intent intent = new Intent();
                    intent.setClass(BBSTopicDetailActivity.this, PersonCenterActivity.class);
                    intent.putExtra("uid", topic.getLikelist().get(pos).getUid());
                    startActivity(intent);
                }
            });
            llLikePhoto.addView(imageView);
        }else { //取消头像
            llLikePhoto.removeViewAt(position);
        }
    }

    /**
     * 我的头像是否存在
     * @return
     */
    private int isExist(Topic topic){
        int position = -1;
        int i = 0;
        for (LikeInfo info:topic.getLikelist()){
            if (readPreference("uid").equals(info.getUid())){
                position = i;
                break;
            }
            i++;
        }
       return position;
    }
}
