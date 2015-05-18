package com.miaotu.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.umeng.analytics.MobclickAgent;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

public class BaseFragmentActivity extends FragmentActivity {
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	public static final String NAME_COMMON = "COMMON";

	/**
	 * 显示toast提示信息
	 * 
	 * @param msg
	 *            提示信息
	 */
	public void showToastMsg(String msg) {
		showToastMsg(msg, Toast.LENGTH_SHORT);
	}

	/**
	 * 显示toast提示信息
	 * 
	 * @param msg
	 *            提示信息
	 * @param duration
	 *            显示时间长短
	 */
	protected void showToastMsg(String msg, int duration) {
		Toast.makeText(this, msg, duration).show();
	}

	/**
	 * get current time
	 * 
	 * @return yyyy-mm-dd
	 */
	public static String getCurrentTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sTime = format.format(new Date());
		System.out.println("CurrentTime:" + sTime);
		return sTime;
		// return String.format("%04d-%02d-%02d ", date.getYear() + 1900,
		// date.getMonth() + 1, date.getDate());

	}

	/**
	 * get current time
	 * 
	 * @return yyyy-mm-dd
	 */
	public static String getNameByTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		String sTime = format.format(new Date());
		System.out.println("CurrentTime:" + sTime);
		return sTime;
		// return String.format("%04d-%02d-%02d ", date.getYear() + 1900,
		// date.getMonth() + 1, date.getDate());

	}

	// *********************** 数据存储方法 ****************************
	/**
	 * SharedPreferences工具方法,用来读取一个值 如果没有读取到，会返回""
	 * 
	 * @param key
	 * @return
	 */
	public String readPreference(String key) {
		SharedPreferences sharedPreferences = getSharedPreferences(NAME_COMMON,
				MODE_PRIVATE);
		String value = sharedPreferences.getString(key, "");
		return value;
	}

	/**
	 * SharedPreferences工具方法,用来写入一个值
	 * 
	 * @param key
	 * @param value
	 */
	public void writePreference(String key, String value) {
		SharedPreferences sharedPreferences = getSharedPreferences(NAME_COMMON,
				MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * 删除 SharedPreferences
	 * 
	 * @param key
	 * @param value
	 */
	public void deletePreference(String key) {
		SharedPreferences sharedPreferences = getSharedPreferences(NAME_COMMON,
				MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.remove(key);
		editor.commit();
	}

	/**
	 * SharedPreferences工具方法,用来读取一个值 如果没有读取到，会返回""
	 * 
	 * @param key
	 * @return
	 */
	public String readPreference(String xmlname, String key) {
		SharedPreferences sharedPreferences = getSharedPreferences(xmlname,
				MODE_PRIVATE);
		String value = sharedPreferences.getString(key, "");
		return value;
	}

	/**
	 * SharedPreferences工具方法,用来写入一个值进自定义文件名
	 * 
	 * @param key
	 * @param value
	 * @param xmlname
	 *            自定义xml的名字
	 */
	public void writePreference(String xmlname, String key, String value) {
		SharedPreferences sharedPreferences = getSharedPreferences(xmlname,
				MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * 删除信息
	 * 
	 * @param xmlname
	 *            指定的信息文件
	 * @param key
	 *            特定的key值
	 */
	public void deletePreference(String xmlname, String key) {
		SharedPreferences sharedPreferences = getSharedPreferences(xmlname,
				MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.remove(key);
		editor.commit();
	}

	/**
	 * 删除特定信息
	 * 
	 * @param xmlname
	 *            指定的信息文件
	 */
	public void deleteAllPreference(String xmlname) {
		SharedPreferences sharedPreferences = getSharedPreferences(xmlname,
				MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.clear();
		editor.commit();
	}
}
