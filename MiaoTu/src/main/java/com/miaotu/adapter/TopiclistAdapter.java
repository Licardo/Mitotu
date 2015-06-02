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

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.miaotu.R;
import com.miaotu.activity.BaseActivity;
import com.miaotu.activity.CustomTourDetailActivity;
import com.miaotu.activity.PersonCenterActivity;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.http.HttpRequestUtil;
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

    public TopiclistAdapter(Context context, List<Topic> list, String token) {
        mList = list;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.token = token;
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
//			holder.tvTitle = (TextView) convertView
//					.findViewById(R.id.tv_title);
            holder.tvContent = (TextView) convertView
                    .findViewById(R.id.tv_content);
            holder.layoutPhotos = (LinearLayout) convertView.findViewById(R.id.layout_photos);
            holder.tvMovementName = (TextView) convertView
                    .findViewById(R.id.tv_movement_name);
            holder.tvDistance = (TextView) convertView
                    .findViewById(R.id.tv_distance);
//            holder.tvDate = (TextView) convertView
//                    .findViewById(R.id.tv_date);
            holder.tvTopDate = (TextView) convertView
                    .findViewById(R.id.tv_top_date);
            holder.ivLike = (ImageView) convertView
                    .findViewById(R.id.iv_like);
            holder.llComment = (LinearLayout) convertView
                    .findViewById(R.id.ll_comment);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // 对ListView的Item中的控件的操作
        UrlImageViewHelper.setUrlDrawable(holder.ivHeadPhoto,
                mList.get(position).getHead_url() + "100x100",
                R.drawable.icon_default_head_photo);

        holder.ivHeadPhoto.setTag(position);
        holder.ivHeadPhoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag();
                Intent intent = new Intent(mContext, PersonCenterActivity.class);
                intent.putExtra("uid",mList.get(pos).getUid());
                mContext.startActivity(intent);
            }
        });
        holder.tvNickname.setText(mList.get(position).getNickname());
//        holder.tvTitle.setText(mList.get(position).getTitle());
        holder.tvContent.setText(mList.get(position).getContent());
        holder.tvTopDate.setText(mList.get(position).getCreated());
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
                    like(token, mList.get(pos).getUid(), false, (ImageView) view);   //添加喜欢
                    mList.get(pos).setIslike("true");
                } else {
                    like(token, mList.get(pos).getUid(), true, (ImageView) view);    //取消喜欢
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
                    R.drawable.icon_default_bbs_photo);
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
        if(StringUtil.isBlank(mList.get(position).getTitle())){
            holder.tvMovementName.setVisibility(View.GONE);
        }else {
            holder.tvMovementName.setVisibility(View.VISIBLE);
        }
        holder.tvMovementName.setText("@"+mList.get(position).getTitle());
        holder.tvMovementName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CustomTourDetailActivity.class);
                intent.putExtra("id", mList.get(position).getAid());
                mContext.startActivity(intent);
            }
        });
        if (!StringUtil.isBlank(mList.get(position).getDistance())){
            holder.tvDistance.setText(mList.get(position).getDistance() + "km");
        }else {
            holder.tvDistance.setVisibility(View.GONE);
        }
//        holder.tvDate.setText(mList.get(position).getCreated());
        return convertView;
    }


    public final class ViewHolder {
        private TextView tvNickname = null;
        private CircleImageView ivHeadPhoto = null;
        //        private TextView tvTitle = null;
        private TextView tvContent = null;
        private LinearLayout layoutPhotos = null;
        private TextView tvMovementName = null;
        private TextView tvDistance = null;
        //        private TextView tvDate = null;
        private TextView tvTopDate;
        private LinearLayout llComment;
        private ImageView ivLike;
    }


    private void like(final String token, final String touser, final boolean islike, final ImageView iv) {

        new BaseHttpAsyncTask<Void, Void, LikeResult>((Activity) mContext, false) {

            @Override
            protected void onCompleteTask(LikeResult baseResult) {
                if (baseResult.getCode() == BaseResult.SUCCESS) {
                    Toast.makeText(mContext, "操作成功", Toast.LENGTH_SHORT).show();
                    if (!islike) {
                        iv.setBackgroundResource(R.drawable.icon_friend_like);
                    } else {
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
            protected LikeResult run(Void... params) {
                return HttpRequestUtil.getInstance().like(token, touser);
            }
        }.execute();
    }
}
