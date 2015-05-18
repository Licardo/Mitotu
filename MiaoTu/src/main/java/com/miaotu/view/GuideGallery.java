package com.miaotu.view;

import com.miaotu.R;
import com.miaotu.util.Util;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class GuideGallery extends ViewPager {
	//判断左右滑动
	private boolean left = false;  
    private boolean right = false;  
    private boolean isScrolling = false;  
    private int lastValue = -1;  
    //当前图像位置
    private int curPosition;
    //前一图像位置
    private int prePosition;
	private boolean isAutoPlay = false;
	//播放时间间隔（默认是5秒钟，最小不能小于100）
	private long timeInterval = 5000;
    
	private int imageSize;
	public LinearLayout bottomFrame;
	public GuideGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public void initPoints(){
		Context context = getContext();
		if(bottomFrame != null){
            bottomFrame.removeAllViews();
			bottomFrame.setOrientation(LinearLayout.HORIZONTAL);
			ImageView image = new ImageView(context);
			LinearLayout.LayoutParams lyParams = new LinearLayout.LayoutParams(Util.dip2px(context,5),Util.dip2px(context,5));
			//这里要dp转换成像素(px)
			int margin = Util.dip2px(context, 5);
			lyParams.setMargins(0, 0, margin, 0);
			image.setLayoutParams(lyParams);
			image.setImageDrawable(getResources().getDrawable(R.drawable.icon_orange_point));
			bottomFrame.addView(image);
			for(int i=1;i<getImageSize();i++){
				image = new ImageView(context);
				image.setLayoutParams(lyParams);
				image.setImageDrawable(getResources().getDrawable(R.drawable.icon_grey_point));
				bottomFrame.addView(image);
			}
			prePosition = 0;
		}
		this.setOnPageChangeListener(listener);
	}


	public int getImageSize() {
		return imageSize;
	}

	public void setImageSize(int imageSize) {
		this.imageSize = imageSize;
	}
	
	/** 
     * listener ,to get move direction . 
*/  
    public  OnPageChangeListener listener = new OnPageChangeListener() {  
        @Override  
        public void onPageScrollStateChanged(int arg0) {  
            if (arg0 == 1) {  
                isScrolling = true;  
            } else {  
                isScrolling = false;  
            }  
  
            if (arg0 == 2) {  
                right = left = false;  
            }  
  
        }  
  
        @Override  
        public void onPageScrolled(int arg0, float arg1, int arg2) {  
            if (isScrolling) {  
                if (lastValue > arg2) {  
                    // 递减，向右侧滑动  
                    right = true;  
                    left = false;
                    
                    //Toast.makeText(getContext(), "向右滑动"+arg0, Toast.LENGTH_SHORT).show();
                } else if (lastValue < arg2) {  
                    // 递减，向左侧滑动  
                    right = false;  
                    left = true;  
                    //Toast.makeText(getContext(), "向左滑动"+arg0, Toast.LENGTH_SHORT).show();
                } else if (lastValue == arg2) {  
                    right = left = false;  
                   // Toast.makeText(getContext(), "不滑动"+arg0, Toast.LENGTH_SHORT).show();
                }  
            }  
            lastValue = arg2;  
        }  
  
        @Override  
        public void onPageSelected(int arg0) {
        	arg0=arg0%imageSize;
        	if(bottomFrame != null){
        		ImageView preView = (ImageView) bottomFrame.getChildAt(prePosition);
        		curPosition = arg0;
        		ImageView curView = (ImageView) bottomFrame.getChildAt(curPosition);
        		preView.setImageResource(R.drawable.icon_grey_point);
        		curView.setImageResource(R.drawable.icon_orange_point);
        		prePosition = curPosition;
        	}
        	//Toast.makeText(getContext(), ""+arg0, Toast.LENGTH_SHORT).show();
        }  
    };  
      
    /** 
     * 得到是否向右侧滑动 
     * @return true 为右滑动 
*/  
    public boolean getMoveRight(){  
        return right;  
    }  
      
    /** 
     * 得到是否向左侧滑动 
     * @return true 为左做滑动 
*/  
    public boolean getMoveLeft(){  
        return left;  
    }  
      
	public boolean isAutoPlay() {
		return isAutoPlay;
	}
	public void setAutoPlay(boolean isAutoPlay) {
		this.isAutoPlay = isAutoPlay;
	}
	public long getTimeInterval() {
		return timeInterval;
	}
	public void setTimeInterval(long timeInterval) {
		this.timeInterval = timeInterval;
	}  
    
}
