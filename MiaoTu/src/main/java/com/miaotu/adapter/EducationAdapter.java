package com.miaotu.adapter;

import com.miaotu.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;

/**
 * Adapter for countries
 */
public class EducationAdapter extends AbstractWheelTextAdapter {
	private String edus[] = new String[] { "本科", "硕士", "博士", "大专", "大专以下" };
	/**
	 * Constructor
	 */
	public EducationAdapter(Context context) {
		super(context, R.layout.dialog_word_design, NO_RESOURCE);

		setItemTextResource(R.id.country_name);
	}

	@Override
	public View getItem(int index, View cachedView, ViewGroup parent) {
		View view = super.getItem(index, cachedView, parent);
		return view;
	}

	@Override
	public int getItemsCount() {
		return edus.length;
	}

	@Override
	protected CharSequence getItemText(int index) {
		return edus[index];
	}

	public String[] getEdus() {
		return edus;
	}

	public void setEdus(String[] edus) {
		this.edus = edus;
	}
	
}
