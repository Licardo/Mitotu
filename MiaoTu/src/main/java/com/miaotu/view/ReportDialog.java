package com.miaotu.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.miaotu.R;
import com.miaotu.util.Util;


/**
 * @author ying
 * 
 */
public class ReportDialog extends Dialog {
	private Context context;
	private TextView addBlackList,addBackListAndReport;

	public ReportDialog(final Activity context, final String uid) {
		super(context, R.style.dialog_report);
		LayoutInflater lay = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = lay.inflate(R.layout.dialog_report, null);
		addBlackList = (TextView) v.findViewById(R.id.tv_add_black_list);
        addBackListAndReport = (TextView) v.findViewById(R.id.tv_and_report);
        addBlackList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //拉黑菜单
                dismiss();
                AddBlackListDialog addBlackListDialog = new AddBlackListDialog(context,uid);
                addBlackListDialog.show();
            }
        });
        addBackListAndReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //举报菜单
                AddBlackAndReportDialog addBlackAndReportDialog = new AddBlackAndReportDialog(context,uid);
                addBlackAndReportDialog.show();
                dismiss();
            }
        });
		setContentView(v);
		setCanceledOnTouchOutside(true);
        // 设置dialog的宽高
        Window window = getWindow();
        window.setGravity(Gravity.RIGHT | Gravity.TOP);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = Util.dip2px(context,3);
        wl.y = Util.dip2px(context,50);
        wl.height = Util.dip2px(context,73f);
        wl.width = Util.dip2px(context,90);
        window.setAttributes(wl);

        setFeatureDrawableAlpha(Window.FEATURE_OPTIONS_PANEL, 0);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

}
