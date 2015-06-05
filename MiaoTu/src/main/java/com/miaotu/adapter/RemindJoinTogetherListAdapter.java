package com.miaotu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.miaotu.R;
import com.miaotu.model.RemindLikeTogether;
import com.miaotu.view.CircleImageView;

import java.util.List;

/**
 * Created by Jayden on 2015/5/29.
 */
public class RemindJoinTogetherListAdapter extends BaseAdapter{

    private Context context;
    private List<RemindLikeTogether> remindLikes;
    private LayoutInflater mLayoutInflater = null;

    public RemindJoinTogetherListAdapter(Context context, List<RemindLikeTogether> remindLikes){
        this.context = context;
        this.remindLikes = remindLikes;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return remindLikes == null?0:remindLikes.size();
    }

    @Override
    public Object getItem(int i) {
        return remindLikes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(view == null){
            view = mLayoutInflater.inflate(R.layout.item_remind_join_tour, null);
            holder = new ViewHolder();
            holder.ivPhoto = (CircleImageView) view.findViewById(R.id.iv_head_photo);
            holder.tvDate = (TextView) view.findViewById(R.id.tv_time);
            holder.tvName = (TextView) view.findViewById(R.id.tv_name);
            holder.tvContent = (TextView) view.findViewById(R.id.tv_status);
            holder.ivPic = (ImageView) view.findViewById(R.id.iv_right);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        UrlImageViewHelper.setUrlDrawable(holder.ivPhoto, remindLikes.get(i).getRemindLikeTogetherInfo().getHeadUrl(), R.drawable.icon_default_head_photo);
        holder.ivPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        holder.tvDate.setText(remindLikes.get(i).getCreated());
        holder.tvName.setText(remindLikes.get(i).getRemindLikeTogetherInfo().getNickname());
        holder.tvContent.setText(remindLikes.get(i).getRemindLikeTogetherInfo().getContent());
        return view;
    }

    public class ViewHolder{
        CircleImageView ivPhoto;
        ImageView ivPic;
        TextView tvName;
        TextView tvContent;
        TextView tvDate;
    }
}
