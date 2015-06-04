package com.miaotu.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.miaotu.R;
import com.miaotu.adapter.RedPackageListAdapter;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.model.RedPackage;
import com.miaotu.result.BaseResult;
import com.miaotu.result.MoneyResult;
import com.miaotu.result.RedPackageListResult;
import com.miaotu.result.SymbolResult;
import com.miaotu.util.LogUtil;
import com.miaotu.util.StringUtil;
import com.miaotu.util.Util;

import cn.sharesdk.framework.ShareSDK;

/**
 * @author zhanglei
 * 红包界面
 */
public class RedPackageActivity extends BaseActivity implements OnClickListener {
    private TextView tvTitle, tvLeft;
    private TextView tvMoney;
    private List<RedPackage> mList;
    private RedPackageListAdapter adapter;
    private ListView lvLucky;
    private ImageView ivTellForture;
    private Dialog dialog;
    private TextView tvSymbolName, tvSymbolContent, tvSymbolStatement, tvFinishRemind;
    private ImageView ivCouponCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_package);
        findView();
        bindView();
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tvLeft = null;
        tvTitle = null;
    }

    private void findView() {
        tvLeft = (TextView) findViewById(R.id.tv_left);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvMoney = (TextView) findViewById(R.id.tv_money);
        ivTellForture = (ImageView) findViewById(R.id.iv_tell_forture);
        lvLucky = (ListView) findViewById(R.id.lv_lucky);
        ivCouponCode = (ImageView) findViewById(R.id.iv_coupon_code);
    }

    private void bindView() {
        tvLeft.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvLeft.setOnClickListener(this);
        ivCouponCode.setOnClickListener(this);
        ivTellForture.setOnClickListener(this);
        lvLucky.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
            }
        });
    }

    private void init() {
        if (!StringUtil.isBlank(readPreference("luckmoney"))) {
            Double luckyMoney = Double.parseDouble(readPreference("luckmoney"));
            DecimalFormat df = new DecimalFormat("0.00");
            writePreference("luckmoney", df.format(luckyMoney));
        } else {
            writePreference("luckmoney", "0.00");
        }
        tvMoney.setText(readPreference("luckmoney"));

        tvLeft.setBackgroundResource(R.drawable.arrow_left_grey);
        tvTitle.setText("我的红包");
        mList = new ArrayList<RedPackage>();
        adapter = new RedPackageListAdapter(this, mList);
        lvLucky.setAdapter(adapter);
        getLuckyList();
//		getRedPackage();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left:
                RedPackageActivity.this.finish();
                break;
            /*case R.id.btn_right:
                Intent intent = new Intent(this,RedPkStatementActivity.class);
                this.startActivity(intent);
                break;*/
            case R.id.iv_tell_forture:
                getSymbol();
                break;
            case R.id.iv_coupon_code:
                //兑换码兑换红包
                openCouponCodeDialog();
                break;
        }

    }

    /**
     * 获取红包
     */
    private void getLuckyList() {

        new BaseHttpAsyncTask<Void, Void, RedPackageListResult>(this,
                true) {
            @Override
            protected void onCompleteTask(RedPackageListResult result) {
                if (tvTitle == null) {
                    return;
                }
                if (result.getCode() == BaseResult.SUCCESS) {
                    mList.clear();
                    if (result.getRedPackageList() == null){
                        adapter.notifyDataSetChanged();
                        return;
                    }
                    mList.addAll(result.getRedPackageList());
                    adapter.notifyDataSetChanged();
//                    tvMoney.setText(result.getLuckMoney());
                } else {
                    if (StringUtil.isEmpty(result.getMsg())) {
                        showToastMsg("获取红包记录失败！");
                    } else {
                        showToastMsg(result.getMsg());
                    }
                }
            }

            @Override
            protected RedPackageListResult run(Void... params) {
                return HttpRequestUtil
                        .getInstance().
                                getLuckyList(readPreference("token"), "10");
            }

        }.execute();

    }

    //卜卦之后查看dialog
    private void openDialog() {
        LayoutInflater lay = (LayoutInflater) (this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        View v = lay.inflate(R.layout.dialog_tell_forture, null);
        tvSymbolName = (TextView) v.findViewById(R.id.tv_symbol_name);
        tvSymbolContent = (TextView) v.findViewById(R.id.tv_symbol_content);
        tvSymbolStatement = (TextView) v.findViewById(R.id.tv_symbol_statement);
        RelativeLayout layoutShare = (RelativeLayout) v.findViewById(R.id.layout_share);
        ImageView ivClose = (ImageView) v.findViewById(R.id.iv_close);
        dialog = new Dialog(this, R.style.dialog_search);
        ivClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        layoutShare.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showShare();
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        dialog.setContentView(v);
        dialog.setCanceledOnTouchOutside(false);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        lp.width = screenWidth; // 宽度
        lp.height = screenHeight; // 高度
        dialogWindow.setAttributes(lp);
        dialog.setFeatureDrawableAlpha(Window.FEATURE_OPTIONS_PANEL, 0);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //已经卜过卦之后的提示dialog
    private void finishDialog() {
        LayoutInflater lay = (LayoutInflater) (this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        View v = lay.inflate(R.layout.dialog_finish_tell_forture, null);
        tvFinishRemind = (TextView) v.findViewById(R.id.tv_finish_tell_forture);
        RelativeLayout layoutShare = (RelativeLayout) v.findViewById(R.id.layout_share);
        ImageView ivClose = (ImageView) v.findViewById(R.id.iv_close);
        dialog = new Dialog(this, R.style.dialog_search);
        ivClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        layoutShare.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showShare();
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        dialog.setContentView(v);
        dialog.setCanceledOnTouchOutside(false);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        lp.width = screenWidth; // 宽度
        lp.height = screenHeight; // 高度
        dialogWindow.setAttributes(lp);
        dialog.setFeatureDrawableAlpha(Window.FEATURE_OPTIONS_PANEL, 0);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * 每日一卦
     */
    private void getSymbol() {
        ivTellForture.setBackgroundResource(R.drawable.icon_tell_fortunes_grey);
        new BaseHttpAsyncTask<Void, Void, SymbolResult>(this,
                true) {
            @Override
            protected void onCompleteTask(SymbolResult result) {
                if (tvTitle == null) {
                    return;
                }
                if (result.getCode() == BaseResult.SUCCESS) {
                    openDialog();
                    tvSymbolName.setText(result.getSymbol().getTitle());
                    tvSymbolContent.setText(result.getSymbol().getContent());
                    SpannableString ss = new SpannableString("此卦象为" + result.getSymbol().getType() + "，得到妙途奖励红包￥" + result.getSymbol().getMoney() + "元");
                    ss.setSpan(new ForegroundColorSpan(RedPackageActivity.this.getResources().getColor(R.color.persimmon_red)), 16, ss.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ss.setSpan(new AbsoluteSizeSpan(Util.dip2px(RedPackageActivity.this, 20)), 16, ss.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tvSymbolStatement.setText(ss);
                    RedPackage rp = new RedPackage();
                    rp.setMoney(result.getSymbol().getMoney());
                    rp.setMark(result.getSymbol().getType() + " " + result.getSymbol().getTitle());
                    rp.setType("1");
                    rp.setId(readPreference("id"));
                    rp.setCreated(Util.getWholeTime());
                    mList.add(0, rp);
                    adapter.notifyDataSetChanged();
                    Double newMoney = Double.parseDouble(tvMoney.getText().toString()) + Double.parseDouble(result.getSymbol().getMoney());
                    DecimalFormat df = new DecimalFormat("0.00"); //保留两位小数
                    tvMoney.setText(df.format(newMoney) + "");
                    writePreference("luckmoney", newMoney + "");
                } else {
                    if (StringUtil.isEmpty(result.getMsg())) {
                        showToastMsg("获取红包金额失败！");
                    } else {
//                        showToastMsg(result.getMsg());
                        finishDialog();
                        tvFinishRemind.setText(result.getMsg());
                    }
                }
            }

            @Override
            protected SymbolResult run(Void... params) {
                return HttpRequestUtil
                        .getInstance().
                                getSymbol(readPreference("token"));
            }

        }.execute();

    }

    /**
     * 分享到sns社区平台
     */
    private void showShare() {
        /*String currentDate = (Util.getWholeTime()).substring(0,10); //获取当前日期，格式2015-01-11
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        oks.setTheme(OnekeyShareTheme.CLASSIC);
        // 关闭sso授权
        oks.disableSSOWhenAuthorize();
        // 分享时Notification的图标和文字
        oks.setNotification(R.drawable.ic_launcher,
                getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
//        oks.setTitle("快来围观我今天的运势！" + "\n http://m.miaotu.com/share/share_divine.php?user_id="+readPreference("id")+"&date="+currentDate);
        oks.setTitle("快来围观我今天的运势！");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://m.miaotu.com/share/share_divine.php?user_id="+readPreference("id")+"&date="+currentDate);
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我得到了妙途奖励的旅行红包啦，谁和我一起出去玩儿呢？" + "\n http://m.miaotu.com/share/share_divine.php?user_id="+readPreference("id")+"&date="+currentDate);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImageUrl("http://m.miaotu.com/share/image/share_divine.png");
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://m.miaotu.com/share/share_divine.php?user_id="+readPreference("id")+"&date="+currentDate);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我得到了妙途奖励的旅行红包啦，谁和我一起出去玩儿呢？" + "\n http://m.miaotu.com/share/share_divine.php?user_id="+readPreference("id")+"&date="+currentDate);
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://m.miaotu.com/share/share_divine.php?user_id="+readPreference("id")+"&date="+currentDate);
        // 启动分享GUI
        oks.show(this);*/
    }

    /**
     * 获取红包
     */
    private void getRedPackage() {

        new BaseHttpAsyncTask<Void, Void, MoneyResult>(this,
                true) {
            @Override
            protected void onCompleteTask(MoneyResult result) {
                if (tvTitle == null) {
                    return;
                }
                if (result.getCode() == BaseResult.SUCCESS) {
                    if ("0".equals(result.getMoney().getMoney())) {
                        tvMoney.setText("0.0");
                    } else {
                        tvMoney.setText(result.getMoney().getMoney());
                    }
                } else {
                    if (StringUtil.isEmpty(result.getMsg())) {
                        showToastMsg("获取红包金额失败！");
                    } else {
                        showToastMsg(result.getMsg());
                    }
                }
            }

            @Override
            protected MoneyResult run(Void... params) {
                return HttpRequestUtil
                        .getInstance().
                                getLuckyMoney(readPreference("token"));
            }

        }.execute();

    }

    //输入红包兑换码dialog
    private void openCouponCodeDialog() {
        LayoutInflater lay = (LayoutInflater) (this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        View v = lay.inflate(R.layout.dialog_coupon_code, null);
        final EditText etCouponCode = (EditText) v.findViewById(R.id.et_coupon_code);
        RelativeLayout layoutExchange = (RelativeLayout) v.findViewById(R.id.layout_exchange);
        ImageView ivClose = (ImageView) v.findViewById(R.id.iv_close);
        dialog = new Dialog(this, R.style.dialog_search);
        ivClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        layoutExchange.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                exchange(etCouponCode.getText().toString());
            }
        });
        dialog.setContentView(v);
        dialog.setCanceledOnTouchOutside(false);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        lp.width = screenWidth; // 宽度
        lp.height = screenHeight; // 高度
        dialogWindow.setAttributes(lp);
        dialog.setFeatureDrawableAlpha(Window.FEATURE_OPTIONS_PANEL, 0);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * 红包兑换
     * @param code
     */
    private void exchange(final String code) {
        if (StringUtil.isEmpty(code)) {
            showToastMsg("兑换码不能为空！");
            return;
        } else {
            new BaseHttpAsyncTask<Void, Void, BaseResult>(this,
                    true) {
                @Override
                protected void onCompleteTask(BaseResult result) {
                    if (tvTitle == null) {
                        return;
                    }
                    if (result.getCode() == BaseResult.SUCCESS) {
                        //兑换成功
                        showToastMsg("红包兑换成功");
                        getRedPackage();
                        getLuckyList();
                    /*if(MainSlidingActivity.getMainSlidingActivityInstance()!=null){
                        MainSlidingActivity.getMainSlidingActivityInstance().getStatistics();
                    }*/
                        dialog.dismiss();
                    } else {
                        if (StringUtil.isEmpty(result.getMsg())) {
                            showToastMsg("红包兑换失败！");
                        } else {
                            showToastMsg(result.getMsg());
                        }
                    }
                }

                @Override
                protected BaseResult run(Void... params) {
                    return HttpRequestUtil
                            .getInstance().
                                    exchangeCouponCode(readPreference("token"), code);
                }

            }.execute();
        }
    }
}
