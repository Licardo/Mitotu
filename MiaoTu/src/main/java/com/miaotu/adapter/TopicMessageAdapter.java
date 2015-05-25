package com.miaotu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.miaotu.R;
import com.miaotu.model.TopicComment;
import com.miaotu.model.TopicMessage;
import com.miaotu.view.CircleImageView;

import java.util.List;


/**
 * @author Jayden
 *
 */
public class TopicMessageAdapter extends BaseAdapter {
	private LayoutInflater mLayoutInflater = null;
	private List<TopicMessage> mList = null;

	public TopicMessageAdapter(Context context, List<TopicMessage> list) {
		mList = list;
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
                //话题评论
                convertView = mLayoutInflater.inflate(
                        R.layout.item_topic_message, null);
                holder.ivDot = (ImageView) convertView.findViewById(R.id.iv_read_flg);
                holder.tvTitle = (TextView) convertView
                        .findViewById(R.id.tv_title);
                holder.tvDate = (TextView) convertView
                        .findViewById(R.id.tv_date);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
            if(mList.get(position).getStatus().equals("0")){
                //未读
                holder.ivDot.setVisibility(View.VISIBLE);
            }else{
                //已读
                holder.ivDot.setVisibility(View.GONE);
            }
            holder.tvTitle.setText(mList.get(position).getTitle());
            holder.tvDate.setText(mList.get(position).getDate());
		return convertView;
	}


	public final class ViewHolder {
        private ImageView ivDot = null;
        private TextView tvTitle = null;
        private TextView tvDate= null;
	}
}
