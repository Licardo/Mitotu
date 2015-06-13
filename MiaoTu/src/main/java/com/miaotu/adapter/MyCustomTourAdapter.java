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
import com.miaotu.activity.JoinedListActivity;
import com.miaotu.model.CustomTour;
import com.miaotu.model.CustomTourInfo;
import com.miaotu.util.Util;
import com.miaotu.view.CircleImageView;
import com.miaotu.view.FlowLayout;

import java.util.List;

/**
 * Created by Jayden on 2015/5/30.
 */
public class MyCustomTourAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private List<CustomTour> customTourInfoList;
    private Context mContext;

    public MyCustomTourAdapter(Context mContext, List<CustomTour> customTourInfoList){
        this.mContext = mContext;
        this.customTourInfoList = customTourInfoList;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return customTourInfoList==null?0:customTourInfoList.size();
    }

    @Override
    public Object getItem(int i) {
        return customTourInfoList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(view == null){
            view = inflater.inflate(R.layout.item_custom_tour, null);
            holder = new ViewHolder();
            holder.tvDate = (TextView) view.findViewById(R.id.tv_date);
            holder.tvEndDate = (TextView) view.findViewById(R.id.tv_end_time);
            holder.tvJoin = (TextView) view.findViewById(R.id.tv_join_list);
            holder.tvName = (TextView) view.findViewById(R.id.tv_username);
            holder.tvPrice = (TextView) view.findViewById(R.id.tv_price);
            holder.tvTitle = (TextView) view.findViewById(R.id.tv_title);
            holder.tvCount = (TextView) view.findViewById(R.id.tv_count);
            holder.ivBackground = (ImageView) view.findViewById(R.id.iv_background);
            holder.ivLike = (ImageView) view.findViewById(R.id.iv_like);
            holder.ivHeadPhoto = (CircleImageView) view.findViewById(R.id.iv_head_photo);
            holder.flTags = (FlowLayout) view.findViewById(R.id.fl_tag);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }

        holder.tvCount.setVisibility(View.GONE);
        holder.ivLike.setVisibility(View.GONE);
        holder.tvJoin.setVisibility(View.VISIBLE);
        CustomTour info = customTourInfoList.get(i);
        holder.tvDate.setText(info.getStartDate()+"-"+info.getEndDate());
        holder.tvTitle.setText(info.getTitle());
        holder.tvPrice.setText(info.getMtPrice());
        holder.tvName.setText(info.getNickname());
        holder.tvEndDate.setText(info.getEndDate()+"截止报名");
        UrlImageViewHelper.setUrlDrawable(holder.ivBackground,
                info.getPicUrl(), R.drawable.icon_default_background);
        UrlImageViewHelper.setUrlDrawable(holder.ivHeadPhoto,
                info.getHeadUrl(), R.drawable.default_avatar);
        holder.tvJoin.setTag(i);
        holder.tvJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //查看报名列表
                int pos = (int) view.getTag();
                Intent intent = new Intent(mContext, JoinedListActivity.class);
                intent.putExtra("flag", "2");
                intent.putExtra("aid", customTourInfoList.get(pos).getId());
                intent.putExtra("title", customTourInfoList.get(pos).getTitle());
                mContext.startActivity(intent);
            }
        });
        String[] tags = null;
        if(info.getTags()!=null) {
            tags = info.getTags().split(",");

            for (String tag : tags) {
                TextView tvTag = new TextView(mContext);
                tvTag.setText(tag);
                FlowLayout.LayoutParams layoutParams = new FlowLayout.
                        LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT,
                        FlowLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.rightMargin = Util.dip2px(mContext, 10);
                tvTag.setTextSize(10);
                tvTag.setLayoutParams(layoutParams);
                holder.flTags.addView(tvTag);
            }
        }else {
            holder.flTags.setVisibility(View.GONE);
        }
        return view;
    }

    public class ViewHolder{
        TextView tvName;
        TextView tvTitle;
        TextView tvDate;
        TextView tvPrice;
        TextView tvEndDate;
        TextView tvJoin;
        TextView tvCount;
        ImageView ivBackground;
        ImageView ivLike;
        CircleImageView ivHeadPhoto;
        FlowLayout flTags;
    }
}
