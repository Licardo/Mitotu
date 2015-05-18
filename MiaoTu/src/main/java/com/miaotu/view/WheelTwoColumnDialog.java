package com.miaotu.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.miaotu.R;

public class WheelTwoColumnDialog extends Dialog{
	Button btnConfirm;
	public WheelTwoColumnDialog(Activity context,int theme,View v) {
		super(context,theme);
		setCanceledOnTouchOutside(true);
		btnConfirm =(Button) v.findViewById(R.id.btn_confirm);
		setContentView(v);
		
		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.BOTTOM);
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
		int screenHeigh = dm.heightPixels;
		lp.width = screenWidth; // 宽度
		lp.height = (int) (screenHeigh * (0.3)); // 高度
		dialogWindow.setAttributes(lp);
		dialogWindow.setWindowAnimations(R.style.dialog_style); // 添加动画
		setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
			}
		});
		show();
	}
	public void setOnConfirmListener(View.OnClickListener confirmListener ) {
		btnConfirm.setOnClickListener(confirmListener);
	}
}
