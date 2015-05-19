package com.miaotu.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.miaotu.R;
import com.miaotu.model.PhotoInfo;
import com.miaotu.model.Together;
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
 *
 */
public class TogetherlistAdapter extends BaseAdapter {
	private LayoutInflater mLayoutInflater = null;
	private List<Together> mList = null;
	private Context mContext;

	public TogetherlistAdapter(Context context, List<Together> list) {
		mList = list;
		mContext = context;
		mLayoutInflater = LayoutInflater.from(context);

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
					R.layout.item_together, null);
			holder.tvNickname = (TextView) convertView.findViewById(R.id.tv_nickname);
			holder.ivHeadPhoto = (CircleImageView) convertView.findViewById(R.id.iv_head_photo);
			holder.tvTitle = (TextView) convertView
					.findViewById(R.id.tv_title);
            holder.tvContent = (TextView) convertView
					.findViewById(R.id.tv_content);
			holder.layoutPhotos = (LinearLayout) convertView.findViewById(R.id.layout_photos);
            holder.tvMovementName = (TextView) convertView
                    .findViewById(R.id.tv_movement_name);
            holder.tvCommentCounts = (TextView) convertView
                    .findViewById(R.id.tv_comment_count);
            holder.tvDate = (TextView) convertView
                    .findViewById(R.id.tv_date);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// 对ListView的Item中的控件的操作
		UrlImageViewHelper.setUrlDrawable(holder.ivHeadPhoto,
                mList.get(position).getHeadPhoto().getUrl() + "&size=100x100",
                R.drawable.icon_default_head_photo);
        holder.ivHeadPhoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(mContext, UserHomeActivity.class);
//                intent.putExtra("userId",mList.get(position).getUid());
//                mContext.startActivity(intent);
            }
        });
        holder.tvNickname.setText(mList.get(position).getNickname());
        holder.tvTitle.setText(mList.get(position).getTitle());
        holder.tvContent.setText(mList.get(position).getContent());
        //添加图片
        int limit = 0;
        if(mList.get(position).getPics().size()>3){
            limit=3;
        }else{
            limit = mList.get(position).getPics().size();
        }
        if(limit==0){
            holder.layoutPhotos.setVisibility(View.GONE);
        }else{
            holder.layoutPhotos.setVisibility(View.VISIBLE);
        }
        holder.layoutPhotos.removeAllViews();

        final ArrayList<PhotoModel> photoList = new ArrayList<>();
        for(PhotoInfo photoInfo:mList.get(position).getPics()){
            PhotoModel photoModel = new PhotoModel();
            photoModel.setOriginalPath(photoInfo.getUrl());
            photoList.add(photoModel);
        }

        for(int i=0;i<limit;i++){
            PhotoInfo photoInfo = mList.get(position).getPics().get(i);
            ImageView imageView = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Util.dip2px(mContext,70),Util.dip2px(mContext,70));
            params.leftMargin = Util.dip2px(mContext,10);
            imageView.setLayoutParams(params);
            holder.layoutPhotos.addView(imageView);
            UrlImageViewHelper.setUrlDrawable(imageView,
                    photoInfo.getUrl() + "&size=210x210",
                    R.drawable.default_avatar);
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
        if(StringUtil.isBlank(mList.get(position).getMovementTitle())){
            holder.tvMovementName.setVisibility(View.GONE);
        }else {
            holder.tvMovementName.setVisibility(View.VISIBLE);
        }
        holder.tvMovementName.setText("@"+mList.get(position).getMovementTitle());
        holder.tvMovementName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(mContext, MovementDetailActivity.class);
//                intent.putExtra("id",mList.get(position).getMovementId());
//                mContext.startActivity(intent);
            }
        });
        holder.tvCommentCounts.setText(mList.get(position).getCommentCount());
        holder.tvDate.setText(mList.get(position).getDate());
		return convertView;
	}


	public final class ViewHolder {
        private TextView tvNickname = null;
        private CircleImageView ivHeadPhoto = null;
        private TextView tvTitle = null;
        private TextView tvContent = null;
        private LinearLayout layoutPhotos= null;
        private TextView tvMovementName= null;
        private TextView tvCommentCounts= null;
        private TextView tvDate= null;
	}
}
