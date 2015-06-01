package com.miaotu.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.miaotu.R;
import com.miaotu.model.PhotoInfo;
import com.miaotu.model.Together;
import com.miaotu.util.LogUtil;
import com.miaotu.util.StringUtil;
import com.miaotu.util.Util;
import com.miaotu.view.CircleImageView;
import com.photoselector.model.PhotoModel;
import com.photoselector.ui.PhotoPreviewActivity;
import com.photoselector.util.CommonUtils;

import org.w3c.dom.Text;

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
    private boolean isMine;

	public TogetherlistAdapter(Context context, List<Together> list,boolean isMine) {
		mList = list;
		mContext = context;
		mLayoutInflater = LayoutInflater.from(context);
        this.isMine = isMine;
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
            if(isMine){
                holder.tvDistance.setVisibility(View.GONE);
                holder.ivLike.setVisibility(View.GONE);
            }else{
                holder.tvDistance.setVisibility(View.VISIBLE);
                holder.ivLike.setVisibility(View.VISIBLE);
            }

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
        if(mList.get(position).getPicList().size()==0){
            holder.layoutImg.setVisibility(View.GONE);
        }else{
            holder.layoutImg.setVisibility(View.VISIBLE);
        }
        //添加图片
        int limit = 0;
        if(mList.get(position).getPicList().size()>3){
            limit=3;
        }else{
            limit = mList.get(position).getPicList().size();
        }
        if(limit==0){
            holder.layoutImg.setVisibility(View.GONE);
        }else{
            holder.layoutImg.setVisibility(View.VISIBLE);
        }
        holder.layoutImg.removeAllViews();
        for(int i=0;i<limit;i++) {
            PhotoInfo photoInfo = mList.get(position).getPicList().get(i);
            if(i==2){
                //添加图片个数textview
                ImageView imageView = new ImageView(mContext);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(Util.dip2px(mContext, 80), Util.dip2px(mContext, 80));
                imageView.setLayoutParams(params);
                TextView tvCount = new TextView(mContext);
                RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(Util.dip2px(mContext, 80), Util.dip2px(mContext, 20));
                params1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                tvCount.setGravity(Gravity.CENTER);
                tvCount.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
                tvCount.setLayoutParams(params1);
                tvCount.setText("共" + mList.get(position).getPicList().size() + "张图片");
                tvCount.setTextColor(mContext.getResources().getColor(R.color.white));
                tvCount.setBackgroundColor(mContext.getResources().getColor(R.color.transparen_black));
                RelativeLayout relativeLayout = new RelativeLayout(mContext);
                relativeLayout.addView(imageView);
                relativeLayout.addView(tvCount);
                holder.layoutImg.addView(relativeLayout);
                UrlImageViewHelper.setUrlDrawable(imageView,
                        photoInfo.getUrl() + "240x240",
                        R.drawable.default_avatar);
            }else{
                ImageView imageView = new ImageView(mContext);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Util.dip2px(mContext, 80), Util.dip2px(mContext, 80));
                params.rightMargin = Util.dip2px(mContext, 10);
                imageView.setLayoutParams(params);
                holder.layoutImg.addView(imageView);
                UrlImageViewHelper.setUrlDrawable(imageView,
                        photoInfo.getUrl() + "240x240",
                        R.drawable.default_avatar);
            }
        }
        holder.tvDistance.setText(mList.get(position).getDistance()+"km");

        holder.ivLike.setVisibility(View.GONE);
        holder.tvDistance.setVisibility(View.GONE);
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
