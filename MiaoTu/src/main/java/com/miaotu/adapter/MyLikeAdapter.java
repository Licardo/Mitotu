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
import com.miaotu.activity.BaseFragmentActivity;
import com.miaotu.activity.PersonCenterActivity;
import com.miaotu.model.BlackInfo;
import com.miaotu.util.Util;
import com.miaotu.view.CircleImageView;

import java.util.List;

/**
 * Created by Jayden on 2015/5/29.
 */
public class MyLikeAdapter extends BaseAdapter{

    private Context context;
    private List<BlackInfo> blackInfos;
    private LayoutInflater mLayoutInflater = null;

    public MyLikeAdapter(Context context, List<BlackInfo> blackInfos){
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
            view = mLayoutInflater.inflate(R.layout.item_mylikelist, null);
            holder = new ViewHolder();
            holder.ivPhoto = (CircleImageView) view.findViewById(R.id.iv_head_photo);
            holder.tvContent = (TextView) view.findViewById(R.id.tv_content);
            holder.tvName = (TextView) view.findViewById(R.id.tv_name);
            holder.ivFollow = (ImageView) view.findViewById(R.id.iv_follow);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        UrlImageViewHelper.setUrlDrawable(holder.ivPhoto,
                blackInfos.get(i).getHeadurl(),
                R.drawable.default_avatar);
        holder.tvContent.setText(blackInfos.get(i).getState());
        holder.tvName.setText(blackInfos.get(i).getNickname());
        if ("true".equals(blackInfos.get(i).getIsliked())){
            holder.ivFollow.setBackgroundResource(R.drawable.icon_guanzhu);
        }else {
            holder.ivFollow.setVisibility(View.GONE);
        }
        holder.ivPhoto.setTag(i);
        holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Util.isNetworkConnected(context)) {
                    return;
                }
                int pos = (int) view.getTag();
                Intent intent = new Intent(context, PersonCenterActivity.class);
                intent.putExtra("uid", blackInfos.get(pos).getUid());
//                context.startActivity(intent);
                ((BaseFragmentActivity)context).startActivityForResult(intent, 1001);
            }
        });
        return view;
    }

    public class ViewHolder{
        CircleImageView ivPhoto;
        TextView tvName;
        TextView tvContent;
        ImageView ivFollow;
    }
}
