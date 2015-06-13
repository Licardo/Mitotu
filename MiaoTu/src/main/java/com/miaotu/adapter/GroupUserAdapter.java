package com.miaotu.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.miaotu.R;
import com.miaotu.activity.PersonCenterActivity;
import com.miaotu.model.GroupUserInfo;
import com.miaotu.view.CircleImageView;

import java.util.List;

/**
 * Created by Jayden on 2015/5/29.
 */
public class GroupUserAdapter extends BaseAdapter{

    private List<GroupUserInfo> groupInfos;
    private LayoutInflater mLayoutInflater = null;
    private Context mContext;

    public GroupUserAdapter(Context context, List<GroupUserInfo> groupInfos){
        this.groupInfos = groupInfos;
        mLayoutInflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return groupInfos == null?0:groupInfos.size();
    }

    @Override
    public Object getItem(int i) {
        return groupInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(view == null){
            view = mLayoutInflater.inflate(R.layout.item_group_user, null);
            holder = new ViewHolder();
            holder.ivPhoto = (CircleImageView) view.findViewById(R.id.iv_head_photo);
            holder.tvName = (TextView) view.findViewById(R.id.tv_name);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        UrlImageViewHelper.setUrlDrawable(holder.ivPhoto, groupInfos.get(i).getHeadurl(),
                R.drawable.icon_default_head_photo);
        holder.tvName.setText(groupInfos.get(i).getNickname());
        holder.ivPhoto.setTag(i);
        holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag();
                Intent intent = new Intent(mContext, PersonCenterActivity.class);
                intent.putExtra("uid", groupInfos.get(pos).getUid());
                mContext.startActivity(intent);
            }
        });
        return view;
    }

    public class ViewHolder{
        CircleImageView ivPhoto;
        TextView tvName;
    }
}
