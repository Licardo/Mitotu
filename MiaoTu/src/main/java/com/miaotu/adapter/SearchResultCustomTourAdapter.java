package com.miaotu.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.miaotu.R;
import com.miaotu.model.CustomTour;
import com.miaotu.model.PhotoInfo;
import com.miaotu.model.Together;
import com.miaotu.util.StringUtil;
import com.miaotu.util.Util;
import com.miaotu.view.CircleImageView;

import java.util.List;

/**
 * Created by Jayden on 2015/6/16.
 */
public class SearchResultCustomTourAdapter extends BaseAdapter{

    private Context context;
    private List<CustomTour> customTours;
    private LayoutInflater inflater;

    public SearchResultCustomTourAdapter(Context context, List<CustomTour> customTours){
        this.context = context;
        this.customTours = customTours;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return customTours == null?0:customTours.size();
    }

    @Override
    public Object getItem(int i) {
        return customTours.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        ViewHolder holder = null;
        if (view == null){
            view = inflater.inflate(R.layout.item_search_result_together, null);
            holder = new ViewHolder();
            holder.tvName = (TextView) view.findViewById(R.id.tv_name);
            holder.tvTime = (TextView) view.findViewById(R.id.tv_time);
            holder.tvContent = (TextView) view.findViewById(R.id.tv_introduce);
            holder.ivHeadUrl = (CircleImageView) view.findViewById(R.id.iv_head_photo);
            holder.ivImg = (ImageView) view.findViewById(R.id.iv_image);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        UrlImageViewHelper.setUrlDrawable(holder.ivHeadUrl, customTours.get(position).getHeadUrl(), R.drawable.default_avatar);
        UrlImageViewHelper.setUrlDrawable(holder.ivImg, customTours.get(position).getPicUrl(), R.drawable.icon_default_big_pic);
        holder.tvName.setText(customTours.get(position).getNickname());
        holder.tvTime.setText(customTours.get(position).getStartDate());
        holder.tvContent.setText(customTours.get(position).getTitle());
        return view;
    }
    class ViewHolder{
        TextView tvName;
        TextView tvTime;
        TextView tvContent;
        CircleImageView ivHeadUrl;
        ImageView ivImg;
    }
}
