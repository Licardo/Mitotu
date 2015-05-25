package com.miaotu.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.miaotu.R;
import com.miaotu.adapter.PhotoPagerAdapter;
import com.miaotu.http.HttpRequestUtil;

public class PhotoViewPagerActivity extends BaseActivity {
	private ViewPager viewPager;
	private List<View> viewList;
	private PhotoPagerAdapter adapter;
	private List<String> photoList;
	private int position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_view_pager);
		findView();
		bindView();
		init();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		viewPager = null;
		if (viewList != null) {
			viewList.clear();
			viewList = null;
		}
		adapter = null;
		if(photoList!=null){
			photoList.clear();
			photoList=null;
		}
	}

	private void findView() {
		// TODO Auto-generated method stub
		viewPager = (ViewPager) findViewById(R.id.viewpager);
	}

	private void bindView() {
		// TODO Auto-generated method stub

	}

	@SuppressLint("NewApi")
	private void init() {
		// TODO Auto-generated method stub
		photoList =  (List<String>) getIntent().getSerializableExtra("photoList");
		position = getIntent().getIntExtra("position", 0);
		viewList = new ArrayList<View>();

		for (String id : photoList) {
			ImageView ivTemp = new ImageView(this);
			LayoutParams params = new LayoutParams();
			WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
			Point size = new Point();
			wm.getDefaultDisplay().getSize(size);
			int width = size.x;
			params.width = LayoutParams.MATCH_PARENT;
			params.height = LayoutParams.WRAP_CONTENT;
			ivTemp.setLayoutParams(params);
			ivTemp.setScaleType(ScaleType.FIT_CENTER);
			UrlImageViewHelper.setUrlDrawable(ivTemp,
					id,R.drawable.meet_head_bg);
			ivTemp.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					PhotoViewPagerActivity.this.finish();
				}
			});
			viewList.add(ivTemp);
		}
		adapter = new PhotoPagerAdapter(viewList);
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(position, false);

	}
}
