package com.miaotu.view;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.miaotu.R;
import com.miaotu.adapter.PopMovementListAdapter;
import com.miaotu.model.Movement;
import com.miaotu.util.StringUtil;

import java.util.List;

/**
 * 自定义popupWindow
 *
 */
public class ChoseTopicMovementPopupWindow extends PopupWindow {
    private View conentView;
    private View parent;

    public ChoseTopicMovementPopupWindow(final Activity context, int width, final View v, final List<Movement>movementList) {
    	parent = v;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.layout_movement_pop, null);
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        ListView lvMovments = (ListView) conentView.findViewById(R.id.lv_movement_pop);
        lvMovments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(!StringUtil.isEmpty(movementList.get(i).getId())){
                    ((TextView)v.findViewById(R.id.tv_movement_name)).setText(movementList.get(i).getName());
                    v.findViewById(R.id.tv_movement_name).setTag(movementList.get(i).getId());
                }else{
                    ((TextView)v.findViewById(R.id.tv_movement_name)).setText("");
                    v.findViewById(R.id.tv_movement_name).setTag("");
                }
                ChoseTopicMovementPopupWindow.this.dismiss();
            }
        });

        PopMovementListAdapter adapter = new PopMovementListAdapter(context,movementList);
        lvMovments.setAdapter(adapter);

        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(width);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
 
    }
 
    /**
     * 显示popupWindow
     */
    public void showPopupWindow() {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAsDropDown(parent, 0, 0);
        } else {
            this.dismiss();
        }
    }
}