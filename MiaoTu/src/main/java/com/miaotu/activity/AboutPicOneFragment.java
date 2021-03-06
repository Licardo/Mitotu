package com.miaotu.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.miaotu.R;

public class AboutPicOneFragment extends BaseFragment {
private ImageView ivAboutPic;
private View root;

@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	root = inflater.inflate(R.layout.fragment_about, container, false);
	findView();
	init();
	return root;
}

@Override
public void onDestroy() {
	super.onDestroy();
//    BitmapDrawable bd = (BitmapDrawable)ivAboutPic.getBackground();
//    ivAboutPic.setBackgroundResource(0);//别忘了把背景设为null，避免onDraw刷新背景时候出现used a recycled bitmap错误
//    bd.setCallback(null);
//    bd.getBitmap().recycle();
}

private void findView() {
	ivAboutPic = (ImageView) root.findViewById(R.id.iv_about);
}

@SuppressLint("NewApi")
private void init() {
    ivAboutPic.setBackgroundResource(R.drawable.bg_guide_1);
}
}
