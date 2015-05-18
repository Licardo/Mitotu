package com.photoselector.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.photoselector.R;
import com.photoselector.model.PhotoModel;
import com.polites.GestureImageView;

public class PhotoPreview extends LinearLayout implements OnClickListener {

	private DonutProgress pbLoading;
	private GestureImageView ivContent;
	private OnClickListener l;

	public PhotoPreview(Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.view_photopreview, this, true);

		pbLoading = (DonutProgress) findViewById(R.id.pb_loading_vpp);
		ivContent = (GestureImageView) findViewById(R.id.iv_content_vpp);
		ivContent.setOnClickListener(this);
	}

	public PhotoPreview(Context context, AttributeSet attrs, int defStyle) {
		this(context);
	}

	public PhotoPreview(Context context, AttributeSet attrs) {
		this(context);
	}

	public void loadImage(PhotoModel photoModel) {
        if(photoModel.getOriginalPath().startsWith("http")){
            //如果是网络图片，不拼接file前缀
		    loadImage(photoModel.getOriginalPath());
        }else{
            //如果是本地图片，拼接file前缀
		    loadImage("file://" + photoModel.getOriginalPath());
        }
	}

	private void loadImage(String path) {
//		ImageLoader.getInstance().loadImage(path, new SimpleImageLoadingListener() {
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                ivContent.setImageBitmap(loadedImage);
//                pbLoading.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                ivContent.setImageDrawable(getResources().getDrawable(R.drawable.ic_picture_loadfailed));
//                pbLoading.setVisibility(View.GONE);
//            }
//
//        });
        DisplayImageOptions options;
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageOnFail(R.drawable.icon_photo_faild)
                .build();
        ImageLoader.getInstance().displayImage(path, ivContent, options, new SimpleImageLoadingListener(){
            @Override
            public void onLoadingStarted(String s, View view) {
                pbLoading.setProgress(0);
                pbLoading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                pbLoading.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                pbLoading.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        },new ImageLoadingProgressListener() {
                @Override
                public void onProgressUpdate(String imageUri, View view, int current,int total) {
                    //在这里更新 ProgressBar的进度信息
                    Log.d("progress",""+Math.round(100.0f * current / total));
                    Log.d("progress","current:"+current +"  total:"+total);
                    pbLoading.setProgress(Math.round(100.0f * current / total));
                }
            });
        }

	@Override
	public void setOnClickListener(OnClickListener l) {
		this.l = l;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.iv_content_vpp && l != null)
			l.onClick(ivContent);
	};

}
