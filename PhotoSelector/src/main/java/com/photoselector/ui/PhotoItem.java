package com.photoselector.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.photoselector.R;
import com.photoselector.model.PhotoModel;

/**
 * @author Aizaz AZ
 *
 */

public class PhotoItem extends LinearLayout implements OnCheckedChangeListener,
		OnLongClickListener {

	private ImageView ivPhoto;
	private CheckBox cbPhoto;
	private onPhotoItemCheckedListener listener;
	private PhotoModel photo;
	private boolean isCheckAll;
	private onItemClickListener l;
	private int position;

	public static DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.ic_picture_loading)
			.showImageOnFail(R.drawable.ic_picture_loadfailed)
			.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
			.bitmapConfig(Bitmap.Config.RGB_565).build();

	private PhotoItem(Context context) {
		super(context);
	}

	public PhotoItem(Context context, onPhotoItemCheckedListener listener) {
		this(context);
		LayoutInflater.from(context).inflate(R.layout.layout_photoitem, this,
				true);
		this.listener = listener;

		setOnLongClickListener(this);

		ivPhoto = (ImageView) findViewById(R.id.iv_photo_lpsi);
		cbPhoto = (CheckBox) findViewById(R.id.cb_photo_lpsi);

		cbPhoto.setOnCheckedChangeListener(this); // CheckBox选中状态改变监听器
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        boolean flg = true;//记录是否超出个数限制，没超过为true
		if (!isCheckAll) {
			flg = listener.onCheckedChanged(photo, buttonView, isChecked); // 调用主界面回调函数
		}
		if(flg){// 让图片变暗或者变亮
            if (isChecked) {
                setDrawingable();
                ivPhoto.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
            } else {
                ivPhoto.clearColorFilter();
            }
            photo.setChecked(isChecked);

        }else {
            buttonView.setChecked(!isChecked);
        }
	}

	/** 设置路径下的图片对应的缩略图 */
	public void setImageDrawable(final PhotoModel photo) {
		this.photo = photo;
		// You may need this setting form some custom ROM(s)
		/*
		 * new Handler().postDelayed(new Runnable() {
		 * 
		 * @Override public void run() { ImageLoader.getInstance().displayImage(
		 * "file://" + photo.getOriginalPath(), ivPhoto); } }, new
		 * Random().nextInt(10));
		 */

		ImageLoader.getInstance().displayImage(
				"file://" + photo.getOriginalPath(), ivPhoto, imageOptions);
	}

	private void setDrawingable() {
		ivPhoto.setDrawingCacheEnabled(true);
		ivPhoto.buildDrawingCache();
	}

	@Override
	public void setSelected(boolean selected) {
		if (photo == null) {
			return;
		}
		isCheckAll = true;
		cbPhoto.setChecked(selected);
		isCheckAll = false;
	}

	public void setOnClickListener(onItemClickListener l, int position) {
		this.l = l;
		this.position = position;
	}

	// @Override
	// public void
	// onClick(View v) {
	// if (l != null)
	// l.onItemClick(position);
	// }

	/** 图片Item选中事件监听器 */
	public static interface onPhotoItemCheckedListener {
		public boolean onCheckedChanged(PhotoModel photoModel,//返回值标明是否可以设置此项为选中
				CompoundButton buttonView, boolean isChecked);
	}

	/** 图片点击事件 */
	public interface onItemClickListener {
		public void onItemClick(int position);
	}

	@Override
	public boolean onLongClick(View v) {
		if (l != null)
			l.onItemClick(position);
		return true;
	}

}
