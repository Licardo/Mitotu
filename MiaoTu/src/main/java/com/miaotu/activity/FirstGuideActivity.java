package com.miaotu.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miaotu.R;
import com.miaotu.util.LogUtil;
import com.miaotu.view.ControlScrollViewPager;
import com.miaotu.view.LoadingDlg;

public class FirstGuideActivity extends BaseFragmentActivity implements OnClickListener {

	private ControlScrollViewPager mViewPager;
	private FragmentPagerAdapter mAdapter;
	private List<Fragment> mFragments = new ArrayList<Fragment>();
    private static FirstGuideActivity firstGuideActivity;
	private boolean isScrolling;	//是否滑动
	private boolean isFromIntroduce;//是否从设置介绍界面过来

    public static FirstGuideActivity getFirstGuideActivity() {
        return firstGuideActivity;
    }

    public void setFirstGuideActivity(FirstGuideActivity firstGuideActivity) {
        this.firstGuideActivity = firstGuideActivity;
    }

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        firstGuideActivity = this;
		setContentView(R.layout.activity_first_guide);
		mViewPager = (ControlScrollViewPager) findViewById(R.id.id_viewpager);
		mViewPager.setOffscreenPageLimit(4);
		findView();
		bindView();
		initView();

		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

			@Override
			public int getCount() {
				return mFragments.size();
			}

			@Override
			public Fragment getItem(int arg0) {
				return mFragments.get(arg0);
			}
		};

		mViewPager.setAdapter(mAdapter);

		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(final int position) {

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
					if (arg0 == 3 && arg1 == 0 && arg2 == 0 && isScrolling){
						if (isFromIntroduce){
							finish();
						}else {
							Intent intent = new Intent(FirstGuideActivity.this, AppLoadingActivity.class);
							startActivity(intent);
							finish();
						}
					}
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				if (arg0 == 1){
					isScrolling = true;
				}else {
					isScrolling = false;
				}
			}
		});

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mViewPager = null;
		mAdapter = null;
		if (mFragments != null) {
			mFragments.clear();
			mFragments = null;
		}
	}

	private void findView() {
	}

	private void bindView() {
	}

	private void initView() {
		isFromIntroduce = getIntent().getBooleanExtra("flag", false);
		AboutPicOneFragment tab01 = new AboutPicOneFragment();
		AboutPicTwoFragment tab02 = new AboutPicTwoFragment();
		AboutPicThreeFragment tab03 = new AboutPicThreeFragment();
		AboutPicFourFragment tab04 = new AboutPicFourFragment();
		mFragments.add(tab01);
		mFragments.add(tab02);
		mFragments.add(tab03);
		mFragments.add(tab04);
	}
	

	@Override
	public void onClick(View v) {

	}

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == 1) {
//            if(resultCode == 2){ //登陆成功
//                FirstGuideActivity.this.finish();
//            }
//        }
//        if(requestCode == 2) {
//            if(resultCode == 2){ //注册成功
//                FirstGuideActivity.this.finish();
//            }
//        }
//    }

    public ControlScrollViewPager getViewPager() {
		return mViewPager;
	}

}
