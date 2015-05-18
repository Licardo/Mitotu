package com.miaotu.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.miaotu.R;
import com.miaotu.activity.BaseActivity;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.result.BaseResult;
import com.miaotu.util.Util;


/**
 * @author ying
 * 
 */
public class AddBlackAndReportDialog extends Dialog {
	private Context context;
	private TextView cancel,reason1,reason2,reason3,reason4,reason5,reason6;

	public AddBlackAndReportDialog(Activity context, final String uid) {
		super(context, R.style.dialog_add_black_list);
		LayoutInflater lay = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = lay.inflate(R.layout.dialog_add_black_and_report, null);
        cancel = (TextView) v.findViewById(R.id.tv_cancel);
        reason2 = (TextView) v.findViewById(R.id.reason2);
        reason3 = (TextView) v.findViewById(R.id.reason3);
        reason4 = (TextView) v.findViewById(R.id.reason4);
        reason5 = (TextView) v.findViewById(R.id.reason5);
        reason6 = (TextView) v.findViewById(R.id.reason6);
        reason1 = (TextView) v.findViewById(R.id.reason1);
        reason1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collect(uid,((TextView)view).getText().toString());
            }
        });
        reason2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collect(uid,((TextView)view).getText().toString());
            }
        });
        reason3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collect(uid,((TextView)view).getText().toString());
            }
        });
        reason4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collect(uid,((TextView)view).getText().toString());
            }
        });
        reason5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collect(uid,((TextView)view).getText().toString());
            }
        });
        reason6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collect(uid,((TextView)view).getText().toString());
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //取消
                dismiss();
            }
        });
		setContentView(v);
		setCanceledOnTouchOutside(false);
        setCancelable(false);
//        // 设置dialog的宽高
        Window window = getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
//        wl.x = Util.dip2px(context,3);
//        wl.y = Util.dip2px(context,50);
        wl.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wl.width = Util.dip2px(context, 220);
        window.setAttributes(wl);

        setFeatureDrawableAlpha(Window.FEATURE_OPTIONS_PANEL, 0);
		this.context = context;
		// TODO Auto-generated constructor stub
	}
    private void collect(final String uid,final String reason) {
//        new BaseHttpAsyncTask<Void, Void, BaseResult>((BaseActivity)context, true) {
//            @Override
//            protected void onCompleteTask(BaseResult result) {
//                if (result.getCode() == BaseResult.SUCCESS) {
//                    ((BaseActivity)context).showToastMsg("举报成功！");
//                    ((BaseActivity)context).finish();
//                } else {
//                    ((BaseActivity)context).showToastMsg("举报失败！");
//                }
//            }
//
//            @Override
//            protected BaseResult run(Void... params) {
//                return HttpRequestUtil.getInstance().addBlackListAndReport(
//                        ((BaseActivity) context).readPreference("token"),uid,reason);
//            }
//
//
//            protected void finallyRun() {
//            };
//        }.execute();
    }

}
