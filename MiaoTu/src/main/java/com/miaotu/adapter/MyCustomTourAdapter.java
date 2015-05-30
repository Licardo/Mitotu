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
import com.miaotu.model.CustomTourInfo;
import com.miaotu.view.CircleImageView;

import java.util.List;

/**
 * Created by Jayden on 2015/5/30.
 */
public class MyCustomTourAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private List<CustomTourInfo> customTourInfoList;
    private Context mContext;

    public MyCustomTourAdapter(Context mContext, List<CustomTourInfo> customTourInfoList){
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
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }

        holder.tvCount.setVisibility(View.GONE);
        holder.ivLike.setVisibility(View.GONE);
        holder.tvJoin.setVisibility(View.VISIBLE);
        CustomTourInfo info = customTourInfoList.get(i);
        holder.tvDate.setText(info.getStartdate()+"-"+info.getEnddate());
        holder.tvTitle.setText(info.getTitle());
        holder.tvPrice.setText(info.getMtprice());
        holder.tvName.setText(info.getNickname());
        holder.tvEndDate.setText(info.getEnddate());
        UrlImageViewHelper.setUrlDrawable(holder.ivBackground, info.getPicurl(), R.drawable.bg_choose_login);
        UrlImageViewHelper.setUrlDrawable(holder.ivHeadPhoto, info.getHeadurl(), R.drawable.icon_default_head);
        holder.tvJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //查看报名列表
            }
        });
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
    }
}
