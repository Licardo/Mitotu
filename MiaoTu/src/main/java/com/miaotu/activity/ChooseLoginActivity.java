package com.miaotu.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.miaotu.R;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.model.RegisterInfo;
import com.miaotu.result.BaseResult;
import com.miaotu.result.LoginResult;
import com.miaotu.util.LogUtil;
import com.miaotu.util.MD5;
import com.miaotu.util.StringUtil;
import com.miaotu.util.Util;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;

public class ChooseLoginActivity extends BaseActivity implements View.OnClickListener,PlatformActionListener {
private Button btnWechatRegister,btnOtherRegister,btnLogin;
    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_login);
        findView();
        bindView();
        init();
    }
    private void findView(){
        btnOtherRegister = (Button) findViewById(R.id.btn_register_other);
        btnWechatRegister = (Button) findViewById(R.id.btn_wechat_register);
        btnLogin = (Button) findViewById(R.id.btn_login);
    }
    private void bindView(){
        btnWechatRegister.setOnClickListener(this);
        btnOtherRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }
    private void init(){
        ShareSDK.initSDK(this);
    }
    private void showDialog(){
        // 为dialog的listview赋值
        LayoutInflater lay = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = lay.inflate(R.layout.dialog_choose_register_way, null);
        final Button btnCancel = (Button) v.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        final ImageView ivWeibo = (ImageView) v.findViewById(R.id.iv_weibo);
        ivWeibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //微信登录
                //测试时，需要打包签名；sample测试时，用项目里面的demokey.keystore
                //打包签名apk,然后才能产生微信的登录
                Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                authorize(weibo);
            }
        });
        final ImageView ivQQ = (ImageView) v.findViewById(R.id.iv_qq);
        ivQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Platform qq = ShareSDK.getPlatform(QZone.NAME);
                authorize(qq);
            }
        });
        final ImageView ivRegister = (ImageView) v.findViewById(R.id.iv_register);
        ivRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(ChooseLoginActivity.this,Register1Activity.class);
                startActivity(registerIntent);
            }
        });
        // 创建dialog
        dialog = new Dialog(this,R.style.Dialog_Fullscreen);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(v);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.height = Util.dip2px(this, 190);
        wl.width = Util.dip2px(this, 250);
        window.setAttributes(wl);
        dialog.setFeatureDrawableAlpha(Window.FEATURE_OPTIONS_PANEL, 0);
        dialog.show();
    }
    //执行授权,获取用户信息
    //文档：http://wiki.mob.com/Android_%E8%8E%B7%E5%8F%96%E7%94%A8%E6%88%B7%E8%B5%84%E6%96%99
    private void authorize(Platform plat) {
        if (plat == null) {
            return;
        }

        plat.setPlatformActionListener(this);
        // true不使用SSO授权，false使用SSO授权
        plat.SSOSetting(false);
        plat.showUser(null);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_register_other:
                showDialog();
                break;
            case R.id.btn_wechat_register:
                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                authorize(wechat);
                break;
            case R.id.btn_login:
                Intent loginIntent = new Intent(ChooseLoginActivity.this,LoginActivity.class);
                startActivity(loginIntent);
                break;
        }
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> res) {
        if(platform.getName().equals(QZone.NAME)){
            try{
                String info = "qq登陆信息：";
                //qq登陆
                info+=" id:"+platform.getDb().getUserId();
                info+=" 头像地址："+res.get("figureurl_qq_1").toString();
                info+=" 昵称："+res.get("nickname").toString();
                info+=" 城市："+res.get("city").toString();
                info+=" 省份："+res.get("province").toString();
                info+=" 性别："+res.get("gender").toString();
                LogUtil.d(info);
                final RegisterInfo registerInfo = new RegisterInfo();
                registerInfo.setOpenid(platform.getDb().getUserId());
                registerInfo.setHeadurl(res.get("figureurl_qq_1").toString());
                registerInfo.setNickname(res.get("nickname").toString());
                registerInfo.setCity(res.get("city").toString());
                registerInfo.setProvince(res.get("province").toString());
                registerInfo.setGender(res.get("gender").toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        login(registerInfo);
                    }
                });
            }catch (Exception e){e.printStackTrace();}
        }
        //微博
        if (platform.getName().equals(SinaWeibo.NAME)) {
            PlatformDb platDB = platform.getDb();//获取数平台数据DB
            //通过DB获取各种数据
            platDB.getUserId();
            platDB.getToken();
            platDB.getUserGender();
            platDB.getUserIcon();
            platDB.getUserId();
            platDB.getUserName();
            LogUtil.d("微博信息    " + platDB.getToken() + "," + platDB.getUserGender() + "," + platDB.getUserIcon()
                    + "," + platDB.getUserId() + "," + platDB.getUserName());
            final RegisterInfo registerInfo = new RegisterInfo();
            registerInfo.setUsid(platDB.getUserId());
            if(platDB.getUserGender().equals("m")){
                registerInfo.setGender("男");
            }
            if(platDB.getUserGender().equals("w")){
                registerInfo.setGender("女");
            }
            registerInfo.setNickname(platDB.getUserName());
            registerInfo.setHeadurl(platDB.getUserIcon());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    login(registerInfo);
                }
            });

        }
        //微信
        if(platform.getName().equals(Wechat.NAME)){
            PlatformDb platDB = platform.getDb();//获取数平台数据DB
            //通过DB获取各种数据
            platDB.getUserId();
            platDB.getToken();
            platDB.getUserGender();
            platDB.getUserIcon();
            platDB.getUserId();
            platDB.getUserName();
            LogUtil.d("微信信息    " + platDB.getToken() + "," + platDB.getUserGender() + "," + platDB.getUserIcon()
                    + "," + platDB.getUserId() + "," + platDB.getUserName());
            final RegisterInfo registerInfo = new RegisterInfo();
            registerInfo.setUnionid(platDB.getUserId());
            if(platDB.getUserGender().equals("m")){
                registerInfo.setGender("男");
            }
            if(platDB.getUserGender().equals("w")){
                registerInfo.setGender("女");
            }
            registerInfo.setNickname(platDB.getUserName());
            registerInfo.setHeadurl(platDB.getUserIcon());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    login(registerInfo);
                }
            });
        }
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        LogUtil.d("登陆失败！");
    }

    @Override
    public void onCancel(Platform platform, int i) {
        LogUtil.d("登陆取消！");
    }
    //登陆
    private void login(final RegisterInfo registerInfo) {
        new BaseHttpAsyncTask<Void, Void, LoginResult>(ChooseLoginActivity.this, true) {
            @Override
            protected void onCompleteTask(LoginResult result) {
                if(btnLogin==null){
                    return;
                }
                if (result.getCode() == BaseResult.SUCCESS) {
                    showToastMsg("登陆成功！");
                    writePreference("uid", result.getLogin().getUid());
                    writePreference("id", result.getLogin().getId());
                    writePreference("token",result.getLogin().getToken());
                    writePreference("name",result.getLogin().getName());
                    writePreference("age",result.getLogin().getAge());
                    writePreference("gender",result.getLogin().getGender());
                    writePreference("headphoto",result.getLogin().getHeadPhoto());
                    writePreference("job",result.getLogin().getJob());
                    writePreference("login_state","in");
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
                    Intent intent = new Intent(ChooseLoginActivity.this,MainActivity.class);
                    startActivity(intent);
                } else {
                    //注册流程
                    register(registerInfo);
                }
            }

            @Override
            protected LoginResult run(Void... params) {
                return HttpRequestUtil.getInstance().login(registerInfo);
            }

        }.execute();
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

    /**
     * 注册
     * @param registerInfo
     */
    private void register(final RegisterInfo registerInfo) {
        new BaseHttpAsyncTask<Void, Void, BaseResult>(ChooseLoginActivity.this, true) {
            @Override
            protected void onCompleteTask(BaseResult result) {
                if(btnLogin==null){
                    return;
                }
                if (result.getCode() == BaseResult.SUCCESS) {
                    showToastMsg("注册成功！");
                } else {
                    if(StringUtil.isEmpty(result.getMsg())){
                        showToastMsg("注册失败！");
                    }else{
                        showToastMsg(result.getMsg());
                    }
                }
            }

            @Override
            protected BaseResult run(Void... params) {
                return HttpRequestUtil.getInstance().register(registerInfo);
            }

        }.execute();
    }
}
