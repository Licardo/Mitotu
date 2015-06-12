package com.miaotu.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.miaotu.R;
import com.miaotu.activity.PersonCenterActivity;
import com.miaotu.activity.TogetherDetailActivity;
import com.miaotu.model.RemindLike;
import com.miaotu.model.RemindLikeTogether;
import com.miaotu.view.CircleImageView;

import java.util.List;

/**
 * Created by Jayden on 2015/5/29.
 */
public class RemindLikeTogetherListAdapter extends BaseAdapter{

    private Context context;
    private List<RemindLikeTogether> remindLikes;
    private LayoutInflater mLayoutInflater = null;

    public RemindLikeTogetherListAdapter(Context context, List<RemindLikeTogether> remindLikes){
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(view == null){
            view = mLayoutInflater.inflate(R.layout.item_remind_like_tour, null);
            holder = new ViewHolder();
            holder.ivPhoto = (CircleImageView) view.findViewById(R.id.iv_head_photo);
            holder.tvDate = (TextView) view.findViewById(R.id.tv_time);
            holder.tvName = (TextView) view.findViewById(R.id.tv_name);
            holder.ivPic = (ImageView) view.findViewById(R.id.iv_right);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        UrlImageViewHelper.setUrlDrawable(holder.ivPhoto, remindLikes.get(i).getRemindLikeTogetherInfo().getHeadUrl(), R.drawable.icon_default_head_photo);
        holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PersonCenterActivity.class);
                intent.putExtra("uid", remindLikes.get(i).getRemindLikeTogetherInfo().getUid());
                context.startActivity(intent);
            }
        });
        UrlImageViewHelper.setUrlDrawable(holder.ivPic, remindLikes.get(i).getRemindLikeTogetherInfo().getPicUrl(), R.drawable.icon_default_head_photo);
        holder.ivPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,
                        TogetherDetailActivity.class);
                intent.putExtra("id", remindLikes.get(i).getRemindLikeTogetherInfo().getYid());
                context.startActivity(intent);
            }
        });
        holder.tvDate.setText(remindLikes.get(i).getCreated());
        holder.tvName.setText(remindLikes.get(i).getRemindLikeTogetherInfo().getNickname());
        return view;
    }

    public class ViewHolder{
        CircleImageView ivPhoto;
        ImageView ivPic;
        TextView tvName;
        TextView tvDate;
    }
}
