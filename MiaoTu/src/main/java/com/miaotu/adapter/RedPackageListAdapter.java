package com.miaotu.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.miaotu.R;
import com.miaotu.model.RedPackage;

/**
 * @author Jayden
 * 
 */
public class RedPackageListAdapter extends BaseAdapter {
	private LayoutInflater mLayoutInflater = null;
	private List<RedPackage> mList = null;
	private Context mContext;

	public RedPackageListAdapter(Context context,
			List<RedPackage> list) {
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
					R.layout.item_red_package, null);
			holder.mark = (TextView) convertView.findViewById(R.id.tv_mark);
			holder.money = (TextView) convertView.findViewById(R.id.tv_money);
			holder.time = (TextView) convertView.findViewById(R.id.tv_time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// 对ListView的Item中的控件的操作
		holder.mark.setText(mList.get(position).getMark());
		holder.time.setText(mList.get(position).getCreated());
		if("1".equals(mList.get(position).getType())) {
			holder.money.setText("+ " + mList.get(position).getMoney());
			holder.money.setTextColor(mContext.getResources().getColor(R.color.money_add));
		} else {
			holder.money.setText("- " + mList.get(position).getMoney());
			holder.money.setTextColor(mContext.getResources().getColor(R.color.money_minus));
		}
		return convertView;
	}

	public final class ViewHolder {
		public TextView mark = null;
		public TextView money = null;
		public TextView time = null;
	}
}
