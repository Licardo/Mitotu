package com.miaotu.adapter;

import com.miaotu.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;

/**
	 * Adapter for countries
	 */
	public class ProvinceAdapter extends AbstractWheelTextAdapter {
		private String countries[] =
				new String[] {"安徽省", "北京市", "福建省", "甘肃省",
                        "广东省", "广西壮族自治区", "贵州省", "海南省", "河北省", "河南省", "黑龙江省", "湖北省", "湖南省", "吉林省", "江苏省",
                        "江西省", "辽宁省", "内蒙古自治区", "宁夏回族自治区", "青海省", "山东省", "山西省", "陕西省", "上海市", "四川省", "天津市",
                        "西藏自治区", "新疆自治区", "云南省", "浙江省", "重庆市"};
		/**
		 * Constructor
		 */
		public ProvinceAdapter(Context context) {
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
			return countries.length;
		}

		@Override
		protected CharSequence getItemText(int index) {
			return countries[index];
		}
	}
	