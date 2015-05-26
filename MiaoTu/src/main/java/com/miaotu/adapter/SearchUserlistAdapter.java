package com.miaotu.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.miaotu.R;
import com.miaotu.activity.PersonCenterActivity;
import com.miaotu.activity.SearchActivity;
import com.miaotu.model.CustomTour;
import com.miaotu.model.PersonInfo;
import com.miaotu.util.LogUtil;
import com.miaotu.util.Util;
import com.miaotu.view.CircleImageView;
import com.miaotu.view.FlowLayout;

import java.util.List;


/**
 * @author Jayden
 *
 */
public class SearchUserlistAdapter extends BaseAdapter {
	private LayoutInflater mLayoutInflater = null;
	private List<PersonInfo> mList = null;
	private Context mContext;

	public SearchUserlistAdapter(Context context, List<PersonInfo> list) {
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

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    @Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mLayoutInflater.inflate(
					R.layout.item_search_user, null);
			holder.tvNickname = (TextView) convertView.findViewById(R.id.tv_username);
			holder.ivHeadPhoto = (CircleImageView) convertView.findViewById(R.id.iv_head_photo);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// 对ListView的Item中的控件的操作
		UrlImageViewHelper.setUrlDrawable(holder.ivHeadPhoto,
				mList.get(position).getHeadurl() + "100x100",
				R.drawable.icon_default_head_photo);
        holder.ivHeadPhoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PersonCenterActivity.class);
                intent.putExtra("uid", mList.get(position).getUid());
                mContext.startActivity(intent);
            }
        });
		String key = ((SearchActivity)mContext).getKey();
        SpannableStringBuilder style=new SpannableStringBuilder(mList.get(position).getNickname());
        style.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.grey64)), 0, mList.get(position).getNickname().length() - 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        style.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.text_orange)), mList.get(position).getNickname().indexOf(key), mList.get(position).getNickname().indexOf(key) + key.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        holder.tvNickname.setText(style);
        return convertView;
	}


	public final class ViewHolder {
        private TextView tvNickname = null;
        private CircleImageView ivHeadPhoto = null;
	}
}
