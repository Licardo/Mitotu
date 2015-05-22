package com.miaotu.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.miaotu.R;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.model.RegisterInfo;
import com.miaotu.result.BaseResult;
import com.miaotu.result.LoginResult;
import com.miaotu.util.LogUtil;
import com.miaotu.util.StringUtil;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;

public class LoginActivity extends BaseActivity implements View.OnClickListener,PlatformActionListener {
private TextView tvLeft,tvTitle;
    private Button btnLogin;
    private EditText etTel,etPassword;
    private ImageView ivWeibo,ivQQ,ivWechat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findView();
        bindView();
        init();
    }
private void findView(){
    tvLeft = (TextView) findViewById(R.id.tv_left);
    tvTitle = (TextView) findViewById(R.id.tv_title);
    btnLogin = (Button) findViewById(R.id.btn_login);
    etTel = (EditText) findViewById(R.id.et_tel);
    etPassword = (EditText) findViewById(R.id.et_password);
    ivWeibo = (ImageView) findViewById(R.id.iv_weibo);
    ivQQ = (ImageView) findViewById(R.id.iv_qq);
    ivWechat = (ImageView) findViewById(R.id.iv_wechat);
}
private void bindView(){
    tvLeft.setOnClickListener(this);
    btnLogin.setOnClickListener(this);
    ivWeibo.setOnClickListener(this);
    ivQQ.setOnClickListener(this);
    ivWechat.setOnClickListener(this);
}
private void init(){
    tvTitle.setText("登录");

}
    //登陆
    private void login(final RegisterInfo registerInfo, final boolean isTel) {
        new BaseHttpAsyncTask<Void, Void, LoginResult>(this, true) {
            @Override
            protected void onCompleteTask(LoginResult result) {
                if(btnLogin==null){
                    return;
                }
                if (result.getCode() == BaseResult.SUCCESS) {
                    showToastMsg("登陆成功！");
                    writePreference("uid", result.getLogin().getUid());
                    writePreference("token",result.getLogin().getToken());
                    writePreference("name",result.getLogin().getName());
                    writePreference("age",result.getLogin().getAge());
                    writePreference("gender",result.getLogin().getGender());
                    writePreference("headphoto",result.getLogin().getHeadPhoto());
                    writePreference("job",result.getLogin().getJob());
                    writePreference("login_state","in");
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                } else {
                    if(isTel){
                        if(StringUtil.isEmpty(result.getMsg())){
                            showToastMsg("登陆失败！");
                        }else{
                            showToastMsg(result.getMsg());
                        }
                    }else{
                        register(registerInfo);
                    }
                }
            }

            @Override
            protected LoginResult run(Void... params) {
                return HttpRequestUtil.getInstance().login(registerInfo);
            }

        }.execute();
    }
    /**
     * 注册
     * @param registerInfo
     */
    private void register(final RegisterInfo registerInfo) {
        new BaseHttpAsyncTask<Void, Void, BaseResult>(this, true) {
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
    private boolean validate(){
        if(StringUtil.isBlank(etTel.getText().toString())){
            showToastMsg("请输入您的手机号！");
            return false;
        }
        if(!StringUtil.isPhoneNumber(etTel.getText().toString())){
            showToastMsg("请输入正确的手机号码！");
        }
        if(StringUtil.isBlank(etPassword.getText().toString())){
            showToastMsg("请输入您的登录密码！");
            return false;
        }
        return true;
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
            case R.id.tv_left:
                finish();
                break;
            case R.id.btn_login:
                if(validate()){
                    final RegisterInfo registerInfo = new RegisterInfo();
                    registerInfo.setPhone(etTel.getText().toString());
                    registerInfo.setPassword(etPassword.getText().toString());
                    login(registerInfo,true);
                }
                break;
            case R.id.iv_qq:
                Platform qq = ShareSDK.getPlatform(QZone.NAME);
                authorize(qq);
                break;
            case R.id.iv_weibo:
                Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                authorize(weibo);
                break;
            case R.id.iv_wechat:
                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                authorize(wechat);
                break;
        }
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> res) {
        LogUtil.d("登陆成功！");

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
                        login(registerInfo,false);
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
                    login(registerInfo,false);
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
                    login(registerInfo,false);
                }
            });
        }
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {

    }

    @Override
    public void onCancel(Platform platform, int i) {

    }
}
