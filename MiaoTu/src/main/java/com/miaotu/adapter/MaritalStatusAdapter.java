package com.miaotu.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.miaotu.R;

import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;

/**
 * Adapter for countries
 */
public class MaritalStatusAdapter extends AbstractWheelTextAdapter {
	private String maritalStatuses[] = new String[45];

	/**
	 * Constructor
	 */
	public MaritalStatusAdapter(Context context) {
		super(context, R.layout.dialog_word_design, NO_RESOURCE);

		setItemTextResource(R.id.country_name);
		for (int i=0;i<45;i++){
			maritalStatuses[i]=""+(i+15);
		}
	}

	@Override
	public View getItem(int index, View cachedView, ViewGroup parent) {
		View view = super.getItem(index, cachedView, parent);
		return view;
	}

	@Override
	public int getItemsCount() {
		return maritalStatuses.length;
	}

	@Override
	protected CharSequence getItemText(int index) {
		return maritalStatuses[index];
	}

	public String[] getMaritalStatuses() {
		return maritalStatuses;
	}

	public void setMaritalStatuses(String[] maritalStatuses) {
		this.maritalStatuses = maritalStatuses;
	}

}
