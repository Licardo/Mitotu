package com.miaotu.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import com.easemob.util.DateUtils;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.miaotu.R;
import com.miaotu.activity.BaseActivity;
import com.miaotu.activity.BaseFragmentActivity;
import com.miaotu.activity.JoinedListActivity;
import com.miaotu.activity.PersonCenterActivity;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.model.PhotoInfo;
import com.miaotu.model.Together;
import com.miaotu.result.BaseResult;
import com.miaotu.util.LogUtil;
import com.miaotu.util.StringUtil;
import com.miaotu.util.Util;
import com.miaotu.view.CircleImageView;
import com.photoselector.model.PhotoModel;
import com.photoselector.ui.PhotoPreviewActivity;
import com.photoselector.util.CommonUtils;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author Jayden
 *
 */
public class TogetherlistAdapter extends BaseAdapter {
	private LayoutInflater mLayoutInflater = null;
	private List<Together> mList = null;
	private Context mContext;
    private boolean isMine;         //是否是我的
    private boolean isOwner;       //是否是我发起的

	public TogetherlistAdapter(Context context, List<Together> list,boolean isMine, boolean isOwner) {
		mList = list;
		mContext = context;
		mLayoutInflater = LayoutInflater.from(context);
        this.isMine = isMine;
        this.isOwner = isOwner;
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
            holder.tvJoinList = (TextView) convertView
                    .findViewById(R.id.tv_join_list);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

        if(isMine){ //我的
            holder.tvDistance.setVisibility(View.GONE);
            holder.ivLike.setVisibility(View.GONE);
        }else{
            holder.tvDistance.setVisibility(View.VISIBLE);
            holder.ivLike.setVisibility(View.VISIBLE);
        }
        if(isOwner){        //我发起的
            holder.tvJoinList.setVisibility(View.VISIBLE);
            holder.tvJoinCount.setVisibility(View.GONE);
            holder.tvCommentCount.setVisibility(View.GONE);
        }
        holder.tvJoinList.setTag(position);
        holder.tvJoinList.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Util.isNetworkConnected(mContext)) {
                    ((BaseFragmentActivity)mContext).showToastMsg("当前未联网，请检查网络设置");
                    return;
                }
                int pos = (int) view.getTag();
                Intent intent = new Intent(mContext, JoinedListActivity.class);
                intent.putExtra("flag", "1");
                intent.putExtra("yid", mList.get(pos).getId());
                intent.putExtra("title", mList.get(pos).getStartDate() +
                        "一起去" + mList.get(pos).getDesCity());
                mContext.startActivity(intent);
            }
        });

		// 对ListView的Item中的控件的操作
		UrlImageViewHelper.setUrlDrawable(holder.ivHeadPhoto,
                mList.get(position).getHeadPhoto() + "100x100",
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
        holder.tvDesCity.setText(mList.get(position).getDesCity());
        try {
            holder.tvTime.setText(DateUtils.getTimestampString(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(mList.get(position).getPublishDate())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.tvNum.setText("需要"+mList.get(position).getNum()+"人");
        if ("0".equals(mList.get(position).getNum())){
            holder.tvNum.setText("人数不限");
        }
        holder.tvFee.setText(mList.get(position).getFee());
        holder.tvComment.setText(mList.get(position).getComment());
        holder.tvJoinCount.setText("已报名" + mList.get(position).getJoinCount() + "人");
        holder.tvCommentCount.setText("评论"+mList.get(position).getReplyCount()+"人");
        if(mList.get(position).isLike()){
            holder.ivLike.setBackgroundResource(R.drawable.icon_like);
        }else{
            holder.ivLike.setBackgroundResource(R.drawable.icon_unlike);
        }
        holder.ivLike.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                like(position);
            }
        });
        holder.tvAge.setText(mList.get(position).getAge()+"岁");
        if (StringUtil.isBlank(mList.get(position).getJob())){
            holder.tvJob.setVisibility(View.GONE);
        }
        holder.tvJob.setText(mList.get(position).getJob());
        if(mList.get(position).getGender().equals("男")){
            holder.ivGender.setBackgroundResource(R.drawable.icon_man);
        }else{
            holder.ivGender.setBackgroundResource(R.drawable.icon_woman);
        }
        holder.tvDate.setText(mList.get(position).getStartDate());
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
            if(i==2&&mList.get(position).getPicList().size()>3){
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
                        R.drawable.icon_default_image);
            }else{
                ImageView imageView = new ImageView(mContext);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        Util.dip2px(mContext, 80), Util.dip2px(mContext, 80));
                params.rightMargin = Util.dip2px(mContext, 10);
                imageView.setLayoutParams(params);
                holder.layoutImg.addView(imageView);
                UrlImageViewHelper.setUrlDrawable(imageView,
                        photoInfo.getUrl() + "240x240",
                        R.drawable.icon_default_image);
            }
        }
        holder.tvDistance.setText(mList.get(position).getDistance() + "km");
		return convertView;
	}
    private void like(final int position) {
        new BaseHttpAsyncTask<Void, Void, BaseResult>((BaseFragmentActivity)mContext, true) {
            @Override
            protected void onCompleteTask(BaseResult result) {
                if (result.getCode() == BaseResult.SUCCESS) {
                    if(mList.get(position).isLike()){
                        ((BaseFragmentActivity)mContext).showToastMsg("取消喜欢成功！");
                        mList.get(position).setIsLike(false);
                    }else{
                        ((BaseFragmentActivity)mContext).showToastMsg("喜欢成功！");
                        mList.get(position).setIsLike(true);
                    }
                    notifyDataSetChanged();
                } else {
                    if(StringUtil.isEmpty(result.getMsg())){
                        ((BaseFragmentActivity)mContext).showToastMsg("失败！");
                    }else{
                        ((BaseFragmentActivity)mContext).showToastMsg(result.getMsg());
                    }
                }
            }

            @Override
            protected BaseResult run(Void... params) {
                return HttpRequestUtil.getInstance().likeTogether(
                        ((BaseFragmentActivity)mContext).readPreference("token"),
                        mList.get(position).getId());
            }

        }.execute();
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
        private  TextView tvJoinList = null;
	}
}
