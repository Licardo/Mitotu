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
			holder.tvNickname = (TextView) convertView.findViewById(R.id.tv_name);
			holder.ivHeadPhoto = (CircleImageView) convertView.findViewById(R.id.iv_head_photo);
			holder.ivGender = (ImageView) convertView
					.findViewById(R.id.iv_gender);
            holder.tvAge = (TextView) convertView
					.findViewById(R.id.tv_age);
			holder.tvJob = (TextView) convertView.findViewById(R.id.tv_identity);
            holder.tvTime = (TextView) convertView
                    .findViewById(R.id.tv_date);
            holder.tvDate = (TextView) convertView
                    .findViewById(R.id.tv_tag_date);
            holder.tvDesCity = (TextView) convertView
                    .findViewById(R.id.tv_tag_des);
            holder.tvNum = (TextView) convertView
                    .findViewById(R.id.tv_tag_require);
            holder.tvFee = (TextView) convertView
                    .findViewById(R.id.tv_tag_price);
            holder.tvComment = (TextView) convertView
                    .findViewById(R.id.tv_introduce);
            holder.tvDistance = (TextView) convertView
                    .findViewById(R.id.tv_distance);
            holder.tvJoinCount = (TextView) convertView
                    .findViewById(R.id.tv_join_count);
            holder.tvCommentCount = (TextView) convertView
                    .findViewById(R.id.tv_comment_count);
            holder.layoutImg = (LinearLayout) convertView
                    .findViewById(R.id.layout_img);
            holder.ivLike = (ImageView) convertView
                    .findViewById(R.id.iv_like);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// 对ListView的Item中的控件的操作
		UrlImageViewHelper.setUrlDrawable(holder.ivHeadPhoto,
                mList.get(position).getHeadPhoto() + "&size=100x100",
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
        holder.tvDesCity.setText(mList.get(position).getDesCity());
        holder.tvDate.setText(mList.get(position).getStartDate());
        holder.tvNum.setText("需要"+mList.get(position).getNum()+"人");
        holder.tvFee.setText(mList.get(position).getFee());
        holder.tvComment.setText(mList.get(position).getComment());
        holder.tvJoinCount.setText("已报名"+mList.get(position).getJoinCount()+"人");
        holder.tvCommentCount.setText("评论"+mList.get(position).getReplyCount()+"人");
        if(mList.get(position).isLike()){
            holder.ivLike.setBackgroundResource(R.drawable.icon_like);
        }else{
            holder.ivLike.setBackgroundResource(R.drawable.icon_unlike);
        }
        holder.tvAge.setText(mList.get(position).getAge()+"岁");
        holder.tvJob.setText(mList.get(position).getJob());
        if(mList.get(position).getGender().equals("男")){
            holder.ivGender.setBackgroundResource(R.drawable.icon_man);
        }else{
            holder.ivGender.setBackgroundResource(R.drawable.icon_woman);
        }
        holder.tvDate.setText(mList.get(position).getStartDate());
        holder.tvTime.setText(mList.get(position).getPublishDate());
        for(PhotoInfo photoInfo:mList.get(position).getPicList()){
            
        }
        holder.tvDistance.setText(mList.get(position).getDistance()+"km");
		return convertView;
	}


	public final class ViewHolder {
        private TextView tvNickname = null;
        private CircleImageView ivHeadPhoto = null;
        private ImageView ivGender = null;
        private TextView tvAge = null;
        private TextView tvJob= null;
        private TextView tvTime= null;
        private TextView tvDate= null;
        private TextView tvDesCity= null;
        private TextView tvNum= null;
        private TextView tvFee= null;
        private TextView tvComment= null;
        private TextView tvDistance= null;
        private TextView tvJoinCount= null;
        private TextView tvCommentCount= null;
        private ImageView ivLike= null;
        private LinearLayout layoutImg= null;
	}
}
