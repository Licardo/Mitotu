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
import com.miaotu.model.PhotoInfo;
import com.miaotu.model.Together;
import com.miaotu.util.StringUtil;
import com.miaotu.util.Util;
import com.miaotu.view.CircleImageView;

import java.util.List;

/**
 * Created by Jayden on 2015/6/16.
 */
public class SearchResultTogetherAdapter extends BaseAdapter{

    private Context context;
    private List<Together> togetherList;
    private LayoutInflater inflater;

    public SearchResultTogetherAdapter(Context context, List<Together> togetherList){
        this.context = context;
        this.togetherList = togetherList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return togetherList == null?0:togetherList.size();
    }

    @Override
    public Object getItem(int i) {
        return togetherList.get(i);
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
            holder.tvDistance = (TextView) view.findViewById(R.id.tv_distance);
            holder.tvContent = (TextView) view.findViewById(R.id.tv_introduce);
            holder.ivHeadUrl = (CircleImageView) view.findViewById(R.id.iv_head_photo);
            holder.llImg = (LinearLayout) view.findViewById(R.id.layout_img);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        UrlImageViewHelper.setUrlDrawable(holder.ivHeadUrl, togetherList.get(position).getHeadPhoto(), R.drawable.default_avatar);
        holder.tvName.setText(togetherList.get(position).getNickname());
        holder.tvTime.setText(togetherList.get(position).getStartDate());
        if (StringUtil.isBlank(togetherList.get(position).getDistance())){
            togetherList.get(position).setDistance("0");
        }
        holder.tvDistance.setText("距您 "+togetherList.get(position).getDistance()+"km");
        holder.tvContent.setText(togetherList.get(position).getComment());
        int limit = 0;
        if(togetherList.get(position).getPicList().size()>3){
            limit=3;
        }else{
            limit = togetherList.get(position).getPicList().size();
        }
        if(limit==0){
            holder.llImg.setVisibility(View.GONE);
        }else{
            holder.llImg.setVisibility(View.VISIBLE);
        }
        holder.llImg.removeAllViews();
        for(int i=0;i<limit;i++) {
            PhotoInfo photoInfo = togetherList.get(position).getPicList().get(i);
            if(i==2&&togetherList.get(position).getPicList().size()>3){
                //添加图片个数textview
                ImageView imageView = new ImageView(context);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(Util.dip2px(context, 80), Util.dip2px(context, 80));
                imageView.setLayoutParams(params);
                TextView tvCount = new TextView(context);
                RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(Util.dip2px(context, 80), Util.dip2px(context, 20));
                params1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                tvCount.setGravity(Gravity.CENTER);
                tvCount.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
                tvCount.setLayoutParams(params1);
                tvCount.setText("共" + togetherList.get(position).getPicList().size() + "张图片");
                tvCount.setTextColor(context.getResources().getColor(R.color.white));
                tvCount.setBackgroundColor(context.getResources().getColor(R.color.transparen_black));
                RelativeLayout relativeLayout = new RelativeLayout(context);
                relativeLayout.addView(imageView);
                relativeLayout.addView(tvCount);
                holder.llImg.addView(relativeLayout);
                UrlImageViewHelper.setUrlDrawable(imageView,
                        photoInfo.getUrl() + "240x240",
                        R.drawable.icon_default_image);
            }else{
                ImageView imageView = new ImageView(context);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        Util.dip2px(context, 80), Util.dip2px(context, 80));
                params.rightMargin = Util.dip2px(context, 10);
                imageView.setLayoutParams(params);
                holder.llImg.addView(imageView);
                UrlImageViewHelper.setUrlDrawable(imageView,
                        photoInfo.getUrl() + "240x240",
                        R.drawable.icon_default_image);
            }
        }
        return view;
    }
    class ViewHolder{
        TextView tvName;
        TextView tvTime;
        TextView tvDistance;
        TextView tvContent;
        CircleImageView ivHeadUrl;
        LinearLayout llImg;
    }
}
