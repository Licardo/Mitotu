package com.miaotu.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.util.DateUtils;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.miaotu.R;
import com.miaotu.activity.PersonCenterActivity;
import com.miaotu.model.RemindLike;
import com.miaotu.model.RemindSys;
import com.miaotu.view.CircleImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Jayden on 2015/5/29.
 */
public class RemindSysListAdapter extends BaseAdapter{

    private Context context;
    private List<RemindSys> remindLikes;
    private LayoutInflater mLayoutInflater = null;

    public RemindSysListAdapter(Context context, List<RemindSys> remindLikes){
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
            view = mLayoutInflater.inflate(R.layout.item_remind_sys, null);
            holder = new ViewHolder();
            holder.ivIdot = (ImageView) view.findViewById(R.id.iv_idot);
            holder.tvDate = (TextView) view.findViewById(R.id.tv_date);
            holder.tvTitle = (TextView) view.findViewById(R.id.tv_title);
            holder.tvContent = (TextView) view.findViewById(R.id.tv_content);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        try {
            holder.tvDate.setText(DateUtils.getTimestampString(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(remindLikes.get(i).getCreated())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.tvTitle.setText(remindLikes.get(i).getTitle());
        holder.tvContent.setText(remindLikes.get(i).getContent().getContent());
        if ("1".equals(remindLikes.get(i).getStatus())){
            holder.ivIdot.setVisibility(View.INVISIBLE);
        }else {
            holder.ivIdot.setVisibility(View.VISIBLE);
        }
        return view;
    }

    public class ViewHolder{
        ImageView ivIdot;
        TextView tvTitle;
        TextView tvDate;
        TextView tvContent;
    }
}
