package com.miaotu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.miaotu.R;
import com.miaotu.model.CustomTour;

import java.util.List;


/**
 * @author zhanglei
 * 
 */
public class PopMovementListAdapter extends BaseAdapter {
	private LayoutInflater mLayoutInflater = null;
	private List<CustomTour> mList = null;
	public PopMovementListAdapter(Context context, List<CustomTour> list) {
		mList = list;
		mLayoutInflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.mList != null ? this.mList.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return this.mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mLayoutInflater.inflate(
					R.layout.item_movement_pop, null);
			holder.name = (TextView) convertView
					.findViewById(R.id.tv_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// 对ListView的Item中的控件的操作
		holder.name.setText(mList.get(position).getNickname());
		return convertView;
	}
	

	
	public final class ViewHolder {
		public TextView name = null;
	}
}
