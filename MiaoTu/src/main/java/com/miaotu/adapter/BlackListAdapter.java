package com.miaotu.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.easemob.util.DateUtils;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.miaotu.R;
import com.miaotu.activity.BaseActivity;
import com.miaotu.activity.PersonCenterActivity;
import com.miaotu.model.BlackInfo;
import com.miaotu.result.BlackResult;
import com.miaotu.util.Util;
import com.miaotu.view.CircleImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
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
        UrlImageViewHelper.setUrlDrawable(holder.ivPhoto,
                blackInfos.get(i).getHeadurl(), R.drawable.default_avatar);
        try {
            holder.tvDate.setText(DateUtils.getTimestampString(
                    new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(blackInfos.get(i).getCreated())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.tvName.setText(blackInfos.get(i).getNickname());
        holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Util.isNetworkConnected(context)) {
                    ((BaseActivity)context).showToastMsg("当前未联网，请检查网络设置");
                    return;
                }
                Intent intent = new Intent(context, PersonCenterActivity.class);
                intent.putExtra("uid", blackInfos.get(i).getUid());
                context.startActivity(intent);
            }
        });
        return view;
    }

    public class ViewHolder{
        CircleImageView ivPhoto;
        TextView tvName;
        TextView tvDate;
    }
}
