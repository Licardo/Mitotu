package com.miaotu.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.miaotu.R;
import com.miaotu.activity.BaseFragmentActivity;
import com.miaotu.activity.JoinedListActivity;
import com.miaotu.activity.PersonCenterActivity;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.model.CustomTour;
import com.miaotu.model.PhotoInfo;
import com.miaotu.model.Together;
import com.miaotu.result.BaseResult;
import com.miaotu.util.StringUtil;
import com.miaotu.util.Util;
import com.miaotu.view.CircleImageView;
import com.miaotu.view.FlowLayout;

import java.util.List;


/**
 * @author Jayden
 *
 */
public class CustomTourlistAdapter extends BaseAdapter {
	private LayoutInflater mLayoutInflater = null;
	private List<CustomTour> mList = null;
	private Context mContext;
    private boolean isMine;

	public CustomTourlistAdapter(Context context, List<CustomTour> list, boolean isMine) {
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

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    @Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mLayoutInflater.inflate(
					R.layout.item_custom_tour, null);
			holder.tvNickname = (TextView) convertView.findViewById(R.id.tv_username);
			holder.ivHeadPhoto = (CircleImageView) convertView.findViewById(R.id.iv_head_photo);
			holder.ivSummary = (ImageView) convertView
					.findViewById(R.id.iv_background);
            holder.tvEndTime = (TextView) convertView
					.findViewById(R.id.tv_age);
            holder.tvPrice = (TextView) convertView
                    .findViewById(R.id.tv_price);
            holder.tvDate = (TextView) convertView
                    .findViewById(R.id.tv_date);
            holder.tvCount = (TextView) convertView
                    .findViewById(R.id.tv_count);
            holder.tvJoinList = (TextView) convertView
                    .findViewById(R.id.tv_join_list);
            holder.ivLike = (ImageView) convertView
                    .findViewById(R.id.iv_like);
            holder.layoutTag = (FlowLayout) convertView
                    .findViewById(R.id.fl_tag);
            holder.tvEndTime = (TextView) convertView
                    .findViewById(R.id.tv_end_time);
            holder.tvTitle = (TextView) convertView
                    .findViewById(R.id.tv_title);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// 对ListView的Item中的控件的操作
		UrlImageViewHelper.setUrlDrawable(holder.ivHeadPhoto,
                mList.get(position).getHeadUrl() + "100x100",
                R.drawable.icon_default_head_photo);
        holder.ivHeadPhoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PersonCenterActivity.class);
                intent.putExtra("uid", mList.get(position).getUid());
                mContext.startActivity(intent);
            }
        });
        holder.tvNickname.setText(mList.get(position).getNickname());
        holder.tvDate.setText(mList.get(position).getStartDate());
        if(isMine){
            holder.tvCount.setVisibility(View.GONE);
            holder.tvJoinList.setVisibility(View.VISIBLE);
        }else{
            holder.tvCount.setVisibility(View.VISIBLE);
            holder.tvJoinList.setVisibility(View.GONE);
            holder.tvCount.setText("已报名"+mList.get(position).getJoinCount()+"人"+" 评论"+mList.get(position).getLikeCount()+"人");
        }
        holder.tvJoinList.setTag(position);
        holder.tvJoinList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //查看报名列表
                int pos = (int) view.getTag();
                Intent intent = new Intent(mContext, JoinedListActivity.class);
                intent.putExtra("flag", "2");
                intent.putExtra("aid", mList.get(pos).getId());
                intent.putExtra("title", mList.get(pos).getTitle());
                mContext.startActivity(intent);
            }
        });
        if(mList.get(position).isLike()){
            holder.ivLike.setBackgroundResource(R.drawable.icon_bglike);
        }else{
            holder.ivLike.setBackgroundResource(R.drawable.icon_bgfdislike);
        }
        holder.ivLike.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                like(position);
            }
        });
        if(mList.get(position).getTags()!=null&&mList.get(position).getTags().length()!=0){
            String[]tags=mList.get(position).getTags().split(",");
            for(String tag:tags){
                TextView textView = new TextView(mContext);
                textView.setText(tag);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                textView.setTextColor(mContext.getResources().getColor(R.color.grey64));
                FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.rightMargin = Util.dip2px(mContext,10);
                params.bottomMargin = Util.dip2px(mContext,10);
                textView.setLayoutParams(params);
                holder.layoutTag.addView(textView);
            }
        }
        holder.tvDate.setText(mList.get(position).getStartDate()+"-"+mList.get(position).getEndDate());
        holder.tvEndTime.setText(mList.get(position).getEndTime()+"截止报名");
        holder.tvTitle.setText(mList.get(position).getTitle());
        holder.tvPrice.setText(mList.get(position).getMtPrice()+"元");
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        wm.getDefaultDisplay().getSize(size);
        int width = size.x;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width,width*404/678);
        holder.ivSummary.setLayoutParams(params);
        UrlImageViewHelper.setUrlDrawable(holder.ivSummary,mList.get(position).getPicUrl()+"678x404",R.drawable.default_avatar);
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
                return HttpRequestUtil.getInstance().likeCustomTour(((BaseFragmentActivity) mContext).readPreference("token"), mList.get(position).getId());
            }

        }.execute();
    }
	public final class ViewHolder {
        private TextView tvNickname = null;
        private TextView tvEndTime = null;
        private TextView tvTitle = null;
        private TextView tvPrice = null;
        private TextView tvDate = null;
        private TextView tvCount = null;
        private TextView tvJoinList = null;
        private CircleImageView ivHeadPhoto = null;
        private ImageView ivSummary = null;
        private ImageView ivLike = null;
        private FlowLayout layoutTag = null;
	}
}
