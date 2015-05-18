package com.miaotu.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.miaotu.R;

/**
 * 自定义等待对话框 
 * @author sunfuyong
 *
 */
public class LoadingDlg extends Dialog {

	Context context;

	public LoadingDlg(Context context) {
		this(context,R.style.loading_dialog);
		
	}

	public LoadingDlg(Context context, int theme) {
		super(context, theme);
		this.context = context;
		setCanceledOnTouchOutside(false);
		setCancelable(false);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_loading);
	}
	
	public static LoadingDlg show(Context context){
		LoadingDlg dlg = new LoadingDlg(context);
		dlg.show();
		return dlg;
	}

}
