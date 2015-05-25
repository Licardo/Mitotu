package com.miaotu.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.miaotu.R;

import java.util.List;

public class FirstPageImageAdapter extends PagerAdapter implements OnClickListener {

	private Context context;
	private List<String> imagePathes;
	private boolean clickAble = false;//是否可以点击
    private int viewId;	//轮播控件中imageView的id
    private int layoutId;	//包含轮播控件中ImageView的布局文件Id
    private LayoutInflater mInflater;
	public FirstPageImageAdapter(Context context, List<String> imagePathes, int layoutId, int viewId){
		this.imagePathes = imagePathes;
		this.context = context;
		this.layoutId = layoutId;
		this.viewId = viewId;
	}
	public FirstPageImageAdapter(Context context, List<String> imagePathes, boolean clickAble, int layoutId, int viewId){
		this.imagePathes = imagePathes;
		this.context = context;
		this.clickAble = clickAble;
		this.viewId = viewId;
		this.layoutId = layoutId;
	}
	@Override
	public int getCount() {
		return imagePathes.size()*10000;
	}

	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		//super.destroyItem(container, position, object);
		/*position=(position+1)%imageList.size();
		try{
		container.removeView(imageList.get(position));
		}catch(Exception e){};*/
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		int positionOritional = position;
		position = position%imagePathes.size();
		mInflater = LayoutInflater.from(context);
		LinearLayout linearLayout =  (LinearLayout) mInflater.inflate(layoutId, null);
		ImageView imageItem = (ImageView) linearLayout.findViewById(viewId);
		imageItem.setTag(position);
		UrlImageViewHelper.setUrlDrawable(imageItem, imagePathes.get(position),R.drawable.default_avatar);
		if(clickAble){
			imageItem.setOnClickListener(this);
		}
		linearLayout.removeAllViews();
		container.addView(imageItem);
		return imageItem;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	@Override
	public void onClick(View view) {
//		ImageView imageItem = (ImageView)view;
//		Integer position = (Integer)imageItem.getTag();
//        Intent intent = new Intent(context, MovementDetailActivity.class);
//        intent.putExtra("id",activityList.get(position));
//        context.startActivity(intent);
	}
	

}
