package com.miaotu.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
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
import com.miaotu.util.StringUtil;
import com.miaotu.util.Util;



/**
 * @author ying
 * 
 */
public class AddBlackListDialog extends Dialog {
	private Context context;
	private TextView cancel,confirm;

	public AddBlackListDialog(Activity context, final String uid) {
		super(context, R.style.dialog_add_black_list);
		LayoutInflater lay = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = lay.inflate(R.layout.dialog_add_black_list, null);
        cancel = (TextView) v.findViewById(R.id.tv_cancel);
        confirm = (TextView) v.findViewById(R.id.tv_confirm);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //取消
                dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //拉黑
                dismiss();
                collect(uid);

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
        wl.width = Util.dip2px(context,220);
        window.setAttributes(wl);

        setFeatureDrawableAlpha(Window.FEATURE_OPTIONS_PANEL, 0);
		this.context = context;
		// TODO Auto-generated constructor stub
	}
    private void collect(final String uid) {
//        new BaseHttpAsyncTask<Void, Void, BaseResult>((BaseActivity)context, true) {
//            @Override
//            protected void onCompleteTask(BaseResult result) {
//                if (result.getCode() == BaseResult.SUCCESS) {
//                    ((BaseActivity)context).showToastMsg("拉黑成功！");
//                    ((BaseActivity)context).finish();
//                } else {
//                    if(StringUtil.isEmpty(result.getMsg())){
//                        ((BaseActivity)context).showToastMsg("拉黑失败！");
//                    }else{
//                        ((BaseActivity)context).showToastMsg(result.getMsg());
//                    }
//                }
//            }
//
//            @Override
//            protected BaseResult run(Void... params) {
//                return HttpRequestUtil.getInstance().addBlackList(
//                        ((BaseActivity) context).readPreference("token"),uid);
//            }
//
//
//            protected void finallyRun() {
//            };
//        }.execute();
    }

}
