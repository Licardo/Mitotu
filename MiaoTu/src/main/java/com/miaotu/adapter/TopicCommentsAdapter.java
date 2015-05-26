package com.miaotu.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.miaotu.R;
import com.miaotu.model.PhotoInfo;
import com.miaotu.model.Topic;
import com.miaotu.model.TopicComment;
import com.miaotu.util.StringUtil;
import com.miaotu.util.Util;
import com.miaotu.view.CircleImageView;

import java.util.List;


/**
 * @author Jayden
 *
 */
public class TopicCommentsAdapter extends BaseAdapter {
	private LayoutInflater mLayoutInflater = null;
	private List<TopicComment> mList = null;
	private Context mContext;

	public TopicCommentsAdapter(Context context, List<TopicComment> list) {
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
                        R.layout.item_topic_comment, null);
                holder.tvNickname = (TextView) convertView.findViewById(R.id.tv_nickname);
                holder.ivHeadPhoto = (CircleImageView) convertView.findViewById(R.id.iv_head_photo);
                holder.tvContent = (TextView) convertView
                        .findViewById(R.id.tv_content);
                holder.tvDate = (TextView) convertView
                        .findViewById(R.id.tv_date);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// 对ListView的Item中的控件的操作
            //话题评论
            UrlImageViewHelper.setUrlDrawable(holder.ivHeadPhoto,
                    mList.get(position).getHeadurl(),
                    R.drawable.icon_default_head_photo);
            holder.ivHeadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(mContext, UserHomeActivity.class);
//                intent.putExtra("userId",mList.get(position).getUid());
//                mContext.startActivity(intent);
            }
        });
            holder.tvNickname.setText(mList.get(position).getNickname());
            holder.tvContent.setText(mList.get(position).getContent());
            holder.tvDate.setText(mList.get(position).getCreated());
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
