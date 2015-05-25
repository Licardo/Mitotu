package com.miaotu.adapter;

import com.miaotu.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;

/**
 * Adapter for countries
 */
public class HeightAdapter extends AbstractWheelTextAdapter {
	private String heights[] = new String[] { "150", "151", "152", "153",
			"154", "155", "156", "157", "158", "159", "160", "161", "162",
			"163", "164", "165", "166", "167", "168", "169", "170", "171",
			"172", "173", "174", "175", "176", "177", "178", "179", "180",
			"181", "182", "183", "184", "185", "186", "187", "188", "189",
			"190", "191", "192", "193", "194", "195", "196", "197", "198",
			"199", "200", "201", "202", "203", "204", "205", "206", "207",
			"208", "209", "210", };

	/**
	 * Constructor
	 */
	public HeightAdapter(Context context) {
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
		return heights.length;
	}

	@Override
	protected CharSequence getItemText(int index) {
		return heights[index];
	}

	public String[] getHeights() {
		return heights;
	}

	public void setHeights(String[] heights) {
		this.heights = heights;
	}
}
