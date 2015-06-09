package com.miaotu.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.miaotu.R;
import com.miaotu.model.JoinedListInfo;

import java.util.List;

/**
 * Created by Jayden on 2015/6/3.
 */
public class JoinedListAdapter extends BaseAdapter{

    private Context mcontext;
    private List<JoinedListInfo> joinedListInfoList;
    private LayoutInflater inflater;
    private boolean isTogether; //判断一起去还是妙旅团跳转过来

    public JoinedListAdapter(Context mcontext, List<JoinedListInfo> joinedListInfoList, boolean isTogether){
        this.mcontext = mcontext;
        this.joinedListInfoList = joinedListInfoList;
        inflater = LayoutInflater.from(mcontext);
        this.isTogether = isTogether;
    }

    @Override
    public int getCount() {
        return joinedListInfoList == null?0:joinedListInfoList.size();
    }

    @Override
    public Object getItem(int i) {
        return joinedListInfoList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(view == null){
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_join_list, null);
            holder.tvName = (TextView) view.findViewById(R.id.tv_name);
            holder.tvNickName = (TextView) view.findViewById(R.id.tv_nickname);
            holder.tvDate = (TextView) view.findViewById(R.id.tv_date);
            holder.tvIdentity = (TextView) view.findViewById(R.id.tv_identity);
            holder.tvPhone = (TextView) view.findViewById(R.id.tv_phone);
            holder.ivCall = (ImageView) view.findViewById(R.id.iv_call);
            holder.ivHeadPhoto = (ImageView) view.findViewById(R.id.iv_head_photo);
            holder.rlIdentity = (RelativeLayout) view.findViewById(R.id.rl_identity);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        JoinedListInfo info = joinedListInfoList.get(i);
        holder.tvName.setText(info.getUsername());
        holder.tvNickName.setText("(昵称："+info.getNickname()+")");
        holder.tvDate.setText(info.getCreated());
        holder.tvPhone.setText(info.getUserphone());
        holder.tvIdentity.setText(info.getUsercard());
        UrlImageViewHelper.setUrlDrawable(holder.ivHeadPhoto, info.getHeadurl(), R.drawable.icon_default_head);
        holder.ivCall.setTag(i);
        holder.ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag();
                Intent intent1 = new Intent();
                intent1.setAction("android.intent.action.DIAL");
                intent1.setData(Uri.parse("tel:"+joinedListInfoList.get(pos).getUserphone()));
                mcontext.startActivity(intent1);
            }
        });
        if (isTogether){
            holder.rlIdentity.setVisibility(View.GONE);
        }
        return view;
    }

    public class ViewHolder{
        TextView tvName;
        TextView tvNickName;
        TextView tvDate;
        TextView tvIdentity;
        TextView tvPhone;
        ImageView ivHeadPhoto;
        ImageView ivCall;
        RelativeLayout rlIdentity;
    }
}
