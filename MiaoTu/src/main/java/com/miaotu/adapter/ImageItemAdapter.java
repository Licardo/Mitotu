package com.miaotu.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.miaotu.R;
import com.miaotu.model.PhotoInfo;
import com.miaotu.model.Together;
import com.miaotu.util.Util;
import com.miaotu.view.CircleImageView;

import java.util.List;

public class ImageItemAdapter extends BaseAdapter {
	private LayoutInflater mLayoutInflater = null;
	private List<PhotoInfo> mList = null;
	private Context mContext;

	public ImageItemAdapter(Context context, List<PhotoInfo> list) {
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
        convertView = new ImageView(mContext);
		GridView.LayoutParams params = new AbsListView.LayoutParams(Util.dip2px(mContext,100),Util.dip2px(mContext,100));
		convertView.setLayoutParams(params);
		// 对ListView的Item中的控件的操作
		UrlImageViewHelper.setUrlDrawable((ImageView)convertView,
                mList.get(position).getUrl() + "300x300",
                R.drawable.icon_default_head_photo);
		return convertView;
	}
}
