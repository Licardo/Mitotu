package com.miaotu.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.miaotu.R;
import com.miaotu.activity.BaseFragment;
import com.miaotu.activity.BaseFragmentActivity;
import com.miaotu.activity.PersonCenterActivity;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.model.BlackInfo;
import com.miaotu.result.BaseResult;
import com.miaotu.util.StringUtil;
import com.miaotu.view.CircleImageView;

import java.util.List;

/**
 * Created by Jayden on 2015/5/29.
 */
public class MyFansAdapter extends BaseAdapter{

    private Context context;
    private List<BlackInfo> blackInfos;
    private LayoutInflater mLayoutInflater = null;
    private String token;

    public MyFansAdapter(Context context, List<BlackInfo> blackInfos, String token){
        this.context = context;
        this.blackInfos = blackInfos;
        mLayoutInflater = LayoutInflater.from(context);
        this.token = token;
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
        UrlImageViewHelper.setUrlDrawable(holder.ivPhoto, blackInfos.get(i).getHeadurl(), R.drawable.icon_default_head_photo);
        holder.tvContent.setText(blackInfos.get(i).getState());
        holder.tvName.setText(blackInfos.get(i).getNickname());
        if ("true".equals(blackInfos.get(i).getIslike())){
            holder.ivFollow.setBackgroundResource(R.drawable.icon_guanzhu);
        }
        holder.ivFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                like(i, (ImageView) view);
            }
        });
        holder.ivPhoto.setTag(i);
        holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag();
                Intent intent = new Intent(context, PersonCenterActivity.class);
                intent.putExtra("uid", blackInfos.get(pos).getUid());
                context.startActivity(intent);
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

    /**
     * 添加/取消喜欢接口
     */
    private void like(final int position, final ImageView view) {

        new BaseHttpAsyncTask<Void, Void, BaseResult>((Activity) context, false) {

            @Override
            protected void onCompleteTask(BaseResult baseResult) {
                if (baseResult.getCode() == BaseResult.SUCCESS) {
                    if ("true".equals(blackInfos.get(position).getIslike())){    //取消关注
                        view.setBackgroundResource(R.drawable.mine_guanzhu);
                        blackInfos.get(position).setIslike("false");
                        ((BaseFragmentActivity)context).writePreference("followcount",
                                (Integer.parseInt(((BaseFragmentActivity)context).
                                        readPreference("followcount")) - 1) + "");
                    }else {     //关注
                        view.setBackgroundResource(R.drawable.icon_guanzhu);
                        blackInfos.get(position).setIslike("true");
                        ((BaseFragmentActivity)context).writePreference("followcount",
                                (Integer.parseInt(((BaseFragmentActivity)context).
                                        readPreference("followcount")) + 1) + "");
                    }
                } else {
                    if (StringUtil.isBlank(baseResult.getMsg())) {
                        Toast.makeText(context, "操作失败", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, baseResult.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            protected BaseResult run(Void... params) {
                return HttpRequestUtil.getInstance().like(token,
                        blackInfos.get(position).getUid());
            }
        }.execute();
    }
}
