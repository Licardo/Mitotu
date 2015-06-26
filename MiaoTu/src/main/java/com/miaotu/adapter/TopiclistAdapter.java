package com.miaotu.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.util.DateUtils;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.miaotu.R;
import com.miaotu.activity.BaseActivity;
import com.miaotu.activity.BaseFragmentActivity;
import com.miaotu.activity.CustomTourDetailActivity;
import com.miaotu.activity.PersonCenterActivity;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.model.LikeInfo;
import com.miaotu.model.PhotoInfo;
import com.miaotu.model.Topic;
import com.miaotu.result.BaseResult;
import com.miaotu.result.LikeResult;
import com.miaotu.util.StringUtil;
import com.miaotu.util.Util;
import com.miaotu.view.CircleImageView;
import com.photoselector.model.PhotoModel;
import com.photoselector.ui.PhotoPreviewActivity;
import com.photoselector.util.CommonUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Jayden
 */
public class TopiclistAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater = null;
    private List<Topic> mList = null;
    private Context mContext;
    private String token;
    private boolean flag;

    public TopiclistAdapter(Context context, List<Topic> list, String token, boolean flag) {
        mList = list;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.token = token;
        this.flag = flag;
    }

    @Override
    public int getCount() {
        return this.mList != null ? this.mList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return this.mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mLayoutInflater.inflate(
                    R.layout.item_topic, null);
            holder.tvNickname = (TextView) convertView.findViewById(R.id.tv_nickname);
            holder.ivHeadPhoto = (CircleImageView) convertView.findViewById(R.id.iv_head_photo);
            holder.tvContent = (TextView) convertView
                    .findViewById(R.id.tv_content);
            holder.layoutPhotos = (LinearLayout) convertView.findViewById(R.id.layout_photos);
            holder.tvMovementName = (TextView) convertView
                    .findViewById(R.id.tv_movement_name);
            holder.tvDistance = (TextView) convertView
                    .findViewById(R.id.tv_distance);
            holder.tvLike = (TextView) convertView
                    .findViewById(R.id.tv_like);
            holder.tvComment = (TextView) convertView
                    .findViewById(R.id.tv_comment);
            holder.tvTopDate = (TextView) convertView
                    .findViewById(R.id.tv_top_date);
            holder.ivLike = (ImageView) convertView
                    .findViewById(R.id.iv_like);
            holder.llComment = (LinearLayout) convertView
                    .findViewById(R.id.ll_comment);
            holder.llCount = (LinearLayout) convertView
                    .findViewById(R.id.ll_count);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // 对ListView的Item中的控件的操作
        UrlImageViewHelper.setUrlDrawable(holder.ivHeadPhoto,
                mList.get(position).getHead_url() + "100x100",
                R.drawable.default_avatar);

        holder.ivHeadPhoto.setTag(position);
        holder.ivHeadPhoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Util.isNetworkConnected(mContext)) {
                    return;
                }
                int pos = (int) view.getTag();
                Intent intent = new Intent(mContext, PersonCenterActivity.class);
                intent.putExtra("uid", mList.get(pos).getUid());
                mContext.startActivity(intent);
            }
        });
        holder.tvNickname.setText(mList.get(position).getNickname());
//        holder.tvTitle.setText(mList.get(position).getTitle());
        holder.tvContent.setText(mList.get(position).getContent());
        try {
            holder.tvTopDate.setText(DateUtils.getTimestampString(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(mList.get(position).getCreated())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if ("false".equals(mList.get(position).getIslike())) {
            holder.ivLike.setBackgroundResource(R.drawable.icon_friend_dislike);
        } else {
            holder.ivLike.setBackgroundResource(R.drawable.icon_friend_like);
        }
        holder.ivLike.setTag(position);
        holder.ivLike.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag();
                if ("false".equals(mList.get(pos).getIslike())) {
                    likeState(token, mList.get(pos).getSid(), false, (ImageView) view);   //添加喜欢
                    mList.get(pos).setIslike("true");
                } else {
                    likeState(token, mList.get(pos).getSid(), true, (ImageView) view);    //取消喜欢
                    mList.get(pos).setIslike("false");
                }
            }
        });
        //添加图片
        int limit = 0;
        if (mList.get(position).getPiclist().size() > 3) {
            limit = 3;
        } else {
            limit = mList.get(position).getPiclist().size();
        }
        if (limit == 0) {
            holder.layoutPhotos.setVisibility(View.GONE);
        } else {
            holder.layoutPhotos.setVisibility(View.VISIBLE);
        }
        holder.layoutPhotos.removeAllViews();

        final ArrayList<PhotoModel> photoList = new ArrayList<>();
        for (PhotoInfo photoInfo : mList.get(position).getPiclist()) {
            PhotoModel photoModel = new PhotoModel();
            photoModel.setOriginalPath(photoInfo.getUrl());
            photoList.add(photoModel);
        }

        for (int i = 0; i < limit; i++) {
            PhotoInfo photoInfo = mList.get(position).getPiclist().get(i);
            ImageView imageView = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Util.dip2px(mContext, 70), Util.dip2px(mContext, 70));
            params.leftMargin = Util.dip2px(mContext, 10);
            imageView.setLayoutParams(params);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            holder.layoutPhotos.addView(imageView);
            UrlImageViewHelper.setUrlDrawable(imageView,
                    photoInfo.getUrl() + "210x210",
                    R.drawable.icon_default_image);
            final int p = i;
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    //点击照片
                    /** 预览照片 */
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("photos", photoList);
                    bundle.putSerializable("position", p);
                    CommonUtils.launchActivity(mContext, PhotoPreviewActivity.class, bundle);
                }
            });
        }
        if (StringUtil.isBlank(mList.get(position).getTitle())) {
            holder.tvMovementName.setVisibility(View.GONE);
        } else {
            holder.tvMovementName.setVisibility(View.VISIBLE);
        }
        holder.tvMovementName.setText("@" + mList.get(position).getTitle());
        holder.tvMovementName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CustomTourDetailActivity.class);
                intent.putExtra("id", mList.get(position).getAid());
                mContext.startActivity(intent);
            }
        });
        if (!StringUtil.isBlank(mList.get(position).getDistance())) {
            holder.tvDistance.setText(mList.get(position).getDistance() + "km");
        } else {
            holder.tvDistance.setVisibility(View.GONE);
        }
        holder.tvLike.setText("喜欢 " + mList.get(position).getStatelikecount() + "人");
        holder.tvComment.setText("评论 " + mList.get(position).getStatereplycount());
        //true 显示我的/ta的动态, false 显示妙友
        if (flag) {
            holder.tvDistance.setVisibility(View.GONE);
            holder.ivLike.setVisibility(View.GONE);
            holder.llComment.setVisibility(View.GONE);
            holder.llCount.setVisibility(View.VISIBLE);
        }
//        holder.tvDate.setText(mList.get(position).getCreated());
        return convertView;
    }


    public final class ViewHolder {
        private TextView tvNickname = null;
        private CircleImageView ivHeadPhoto = null;
        private TextView tvContent = null;
        private LinearLayout layoutPhotos = null;
        private TextView tvMovementName = null;
        private TextView tvDistance = null;
        private TextView tvLike = null;
        private TextView tvComment = null;
        private TextView tvTopDate;
        private LinearLayout llComment;
        private LinearLayout llCount;
        private ImageView ivLike;
    }


    private void likeState(final String token, final String sid, final boolean islike, final ImageView iv) {

        new BaseHttpAsyncTask<Void, Void, BaseResult>((Activity) mContext, false) {

            @Override
            protected void onCompleteTask(BaseResult baseResult) {
                if (baseResult.getCode() == BaseResult.SUCCESS) {
                    if (!islike) {
                        Toast.makeText(mContext, "喜欢成功", Toast.LENGTH_SHORT).show();
                        iv.setBackgroundResource(R.drawable.icon_friend_like);
                    } else {
                        Toast.makeText(mContext, "取消喜欢成功", Toast.LENGTH_SHORT).show();
                        iv.setBackgroundResource(R.drawable.icon_friend_dislike);
                    }
                } else {
                    if (StringUtil.isBlank(baseResult.getMsg())) {
                        Toast.makeText(mContext, "操作失败", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, baseResult.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            protected BaseResult run(Void... params) {
                return HttpRequestUtil.getInstance().likeState(token, sid);
            }
        }.execute();
    }
}
