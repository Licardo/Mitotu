package com.miaotu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.miaotu.R;
import com.miaotu.model.BlackInfo;
import com.miaotu.result.BlackResult;
import com.miaotu.view.CircleImageView;

import java.util.List;

/**
 * Created by Jayden on 2015/5/29.
 */
public class BlackListAdapter extends BaseAdapter{

    private Context context;
    private List<BlackInfo> blackInfos;
    private LayoutInflater mLayoutInflater = null;

    public BlackListAdapter(Context context, List<BlackInfo> blackInfos){
        this.context = context;
        this.blackInfos = blackInfos;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return blackInfos == null?0:blackInfos.size();
    }

    @Override
    public Object getItem(int i) {
        return blackInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(view == null){
            view = mLayoutInflater.inflate(R.layout.item_blacklist, null);
            holder = new ViewHolder();
            holder.ivPhoto = (CircleImageView) view.findViewById(R.id.iv_userhead);
            holder.tvDate = (TextView) view.findViewById(R.id.tv_time);
            holder.tvName = (TextView) view.findViewById(R.id.tv_name);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        UrlImageViewHelper.setUrlDrawable(holder.ivPhoto, blackInfos.get(i).getHeadurl(), R.drawable.icon_default_head_photo);
        holder.tvDate.setText(blackInfos.get(i).getCreated());
        holder.tvName.setText(blackInfos.get(i).getNickname());
        return view;
    }

    public class ViewHolder{
        CircleImageView ivPhoto;
        TextView tvName;
        TextView tvDate;
    }
}
