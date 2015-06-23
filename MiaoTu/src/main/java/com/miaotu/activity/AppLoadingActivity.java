package com.miaotu.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.miaotu.R;
import com.miaotu.util.LogUtil;
import com.miaotu.util.MD5;
import com.miaotu.util.StringUtil;
import com.miaotu.util.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author ZhangLei
 *
 */
public class AppLoadingActivity extends BaseActivity {
    /** Called when the activity is first created. */
	private static final int sleepTime = 1888;

    @Override
    public void onCreate(Bundle savedInstanceState) {
     // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        // 取消状态栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_loading);

        if(StringUtil.isBlank(readPreference("isFirst"))) {
            writePreference("isFirst", "first");
            Intent i = new Intent(this,FirstGuideActivity.class);
            this.startActivity(i);
            AppLoadingActivity.this.finish();
            return;
        }

        // 三秒钟之后进入login
//        ImageView ivLoadingMiao = (ImageView) this.findViewById(R.id.iv_loading_miao);
//        ImageView ivLoadingLv = (ImageView) this.findViewById(R.id.iv_loading_lv);
//        ImageView ivLoadingTu = (ImageView) this.findViewById(R.id.iv_loading_tu);
        
        // 从浅到深,从百分之10到百分之百
//        AlphaAnimation animation = new AlphaAnimation(0.1f, 1.0f);
//        animation.setDuration(2000);
//        ivLoadingMiao.setAnimation(animation);
//        ivLoadingLv.setAnimation(animation);
//        ivLoadingTu.setAnimation(animation);


        // 给animation设置监听器
//        animation.setAnimationListener(new AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//                // TODO Auto-generated method stub
//            }
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//                // TODO Auto-generated method stub
//            }
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                // TODO Auto-generated method stub
//                // 三秒之后跳出
//            	animationFlg = true;
//            	finishLoading();
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        init();
    }

    private void init(){
//    	 if(!readPreference("login_state").equals("in")){
             new Thread(new Runnable() {
                 public void run() {
                         try {
                             Thread.sleep(sleepTime);
                         } catch (InterruptedException e) {
                             e.printStackTrace();
                         }
                     finishLoading();
                 }
             }).start();
    		 return;
//    	 }
//
    }
    class LoadIMInfoThread extends Thread{
        @Override
        public void run() {
            super.run();
            LogUtil.d("main", "登录聊天服务器成功！");
            long start = System.currentTimeMillis();
            EMChatManager.getInstance().loadAllConversations();
            try {
                EMGroupManager.getInstance().getGroupsFromServer();
            } catch (EaseMobException e) {
                e.printStackTrace();
            }
            EMGroupManager.getInstance().loadAllGroups();

//            long costTime = System.currentTimeMillis() - start;
//            //等待sleeptime时长
//            if (sleepTime - costTime > 0) {
//                try {
//                    Thread.sleep(sleepTime - costTime);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
        }
    }
    private void finishLoading(){
         if(readPreference("login_status").equals("in")){
             EMChatManager.getInstance().login(MD5.md5(readPreference("uid")), readPreference("token"),
                     new EMCallBack() {//回调
                         @Override
                         public void onSuccess() {
//                                    runOnUiThread(new Runnable() {
//                                        public void run() {
//
//                                        }
//                                    });
                             new LoadIMInfoThread().start();
                         }

                         @Override
                         public void onProgress(int progress, String status) {

                         }

                         @Override
                         public void onError(int code, String message) {
                             Log.d("main", "登录聊天服务器失败！");
                         }

                     });

             Calendar calendar = Calendar.getInstance();
             SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
             String sysDatetime = fmt.format(calendar.getTime());
             if(readPreference("everyday").equals(sysDatetime)){
                 Intent intent = new Intent(AppLoadingActivity.this,MainActivity.class);
                 startActivity(intent);
             }else{
                 Intent intent = new Intent(AppLoadingActivity.this,EveryDayPicActivity.class);
                 startActivity(intent);
             }
             finish();
    	}else{
             Intent it = new Intent(AppLoadingActivity.this, ChooseLoginActivity.class);
             startActivity(it);
             finish();
         }
    }

}

