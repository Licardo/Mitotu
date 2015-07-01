package com.miaotu.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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
public class TipDialog extends Dialog {
	private Context context;
	private Button cancel,confirm;
    private TextView tvContent,tvTip;

	public TipDialog(Activity context,int layoutId) {
		super(context, R.style.Dialog_Fullscreen_tip);
		LayoutInflater lay = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 		View v = lay.inflate(layoutId, null);
		v.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dismiss();
			}
		});
		setContentView(v);
		setCanceledOnTouchOutside(false);
        setCancelable(false);
//        // 设置dialog的宽高
        Window window = getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.height = WindowManager.LayoutParams.MATCH_PARENT;
        wl.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wl);

        setFeatureDrawableAlpha(Window.FEATURE_OPTIONS_PANEL, 0);
		this.context = context;
		// TODO Auto-generated constructor stub
	}
}
