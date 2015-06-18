package com.miaotu.app;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Application;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.easemob.chat.EMChat;
import com.miaotu.activity.CityListActivity;
import com.miaotu.activity.FirstPageFragment;
import com.miaotu.util.LogUtil;

public class App extends Application implements
		AMapLocationListener {
    public static final String NAME_COMMON = "COMMON";
	public static float fDensity;
	public static float screenW;
	public static float screenH;
	public static App instance;
	private LocationManagerProxy mLocationManagerProxy;

	/**
	 * 临时会话有效期
	 */
	private String tempChatTime = "";

	public static App getInstance() {
		return instance;
	}

	/**
	 * 是否允许访问手机联系人
	 */
	private boolean bToContact;
	private SharedPreferences preferences;
	private Toast toast;
	public static String packageName = "";

	@Override
	public void onCreate() {
		super.onCreate();
		preferences = getSharedPreferences(NAME_COMMON, MODE_PRIVATE);
		initIm();
		fDensity = getResources().getDisplayMetrics().density;
		screenW = getResources().getDisplayMetrics().widthPixels;
		screenH = getResources().getDisplayMetrics().heightPixels;
		instance = this;
		packageName = getPackageName();
	}


	/**
	 * Show Toast
	 * 
	 * @param msg
	 *            the message to show
	 */
	private void showTextToast(String msg) {
		if (toast == null) {
			toast = Toast.makeText(getApplicationContext(), msg,
					Toast.LENGTH_SHORT);
		} else {
			toast.setText(msg);
		}
		toast.show();
	}

	/**
	 * Show Toast
	 * 
	 * @param resId
	 *            the message resources id in string.xml
	 */
	private void showTextToast(int resId) {
		showTextToast(getString(resId));
	}

	/**
	 * Show Toast
	 * 
	 * @param msg
	 */
	public static void showToast(String msg) {
		if (instance != null)
			instance.showTextToast(msg);
	}

	/**
	 * Show Toast
	 * 
	 * @param resId
	 */
	public static void showToast(int resId) {
		if (instance != null)
			instance.showTextToast(resId);
	}

	/**
	 * Check network available
	 * 
	 * @return
	 */
	public static boolean checkNetwork() {
		NetworkInfo info = ((ConnectivityManager) instance
				.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo();

		return (info != null) && (info.isConnected());
	}

	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			String str = (String) msg.obj;
			showTextToast(str);
		};
	};

	public void showToastAsyn(String str) {
		Message msg = new Message();
		msg.obj = str;
		handler.sendMessage(msg);
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		System.exit(0);
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		manager.killBackgroundProcesses(getPackageName());
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		try {
			// final ActivityManager am =
			// (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
			// am.restartPackage(getPackageName());
			ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
			manager.killBackgroundProcesses(getPackageName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取设备号
	 * 
	 * @return
	 */
	public String getDeviceNo() {
		String uniqueId = "";
		try {
			final TelephonyManager tm = (TelephonyManager) getBaseContext()
					.getSystemService(Context.TELEPHONY_SERVICE);

			final String tmDevice, tmSerial, tmPhone, androidId;
			tmDevice = "" + tm.getDeviceId();
			tmSerial = "" + tm.getSimSerialNumber();
			androidId = ""
					+ android.provider.Settings.Secure.getString(
							getContentResolver(),
							android.provider.Settings.Secure.ANDROID_ID);

			UUID deviceUuid = new UUID(androidId.hashCode(),
					((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
			uniqueId = deviceUuid.toString();
		} catch (Exception e) {
			uniqueId = "";
			e.printStackTrace();
		}
		return uniqueId;
	}

	/**
	 * 判断当前应用程序处于前台还是后台
	 * 
	 * @param
	 * 
	 * @return
	 */
	public static boolean isApplicationBroughtToBackground() {
		if (instance == null)
			return false;
		ActivityManager am = (ActivityManager) instance
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (!topActivity.getPackageName().equals(instance.getPackageName())) {
				return true;
			}
		}
		return false;

	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isBackground(Context context) {

		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(context.getPackageName())) {
				if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}

	public static void cancelNotification() {
		try {
			NotificationManager manger = (NotificationManager) instance
					.getSystemService(NOTIFICATION_SERVICE);
			manger.cancelAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getTimeStamp() {
		return new java.util.Date().getTime() + "";
	}
	
	
	private void initIm(){
		int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        
        // 如果使用到百度地图或者类似启动remote service的第三方库，这个if判断不能少
        if (processAppName == null ||!processAppName.equalsIgnoreCase("com.miaotu")) {
            // workaround for baidu location sdk
            // 百度定位sdk，定位服务运行在一个单独的进程，每次定位服务启动的时候，都会调用application::onCreate
            // 创建新的进程。
            // 但环信的sdk只需要在主进程中初始化一次。 这个特殊处理是，如果从pid 找不到对应的processInfo
            // processName，
            // 则此application::onCreate 是被service 调用的，直接返回

            LogUtil.e("app","enter the service process!");
            return;
        }
        
       //初始化环信SDK
       LogUtil.d("Application", "Initialize EMChat SDK");
       EMChat.getInstance().init(this);

		mLocationManagerProxy = LocationManagerProxy.getInstance(this);
		// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
		// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用removeUpdates()方法来取消定位请求
		// 在定位结束后，在合适的生命周期调用destroy()方法
		// 其中如果间隔时间为-1，则定位只定一次,
		// 在单次定位情况下，定位无论成功与否，都无需调用removeUpdates()方法移除请求，定位sdk内部会移除
		mLocationManagerProxy.requestLocationData(
				LocationProviderProxy.AMapNetwork, -1, 15, this);
	}
	private String getAppName(int pID) {
		String processName = null;
		ActivityManager am = (ActivityManager) this
				.getSystemService(ACTIVITY_SERVICE);
		List l = am.getRunningAppProcesses();
		Iterator i = l.iterator();
		PackageManager pm = this.getPackageManager();
		while (i.hasNext()) {
			RunningAppProcessInfo info = (RunningAppProcessInfo) (i
					.next());
			try {
				if (info.pid == pID) {
					CharSequence c = pm.getApplicationLabel(pm
							.getApplicationInfo(info.processName,PackageManager.GET_META_DATA));
					processName = info.processName;
					return processName;
				}
			} catch (Exception e) {
			}
		}
		return processName;
	}

	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (amapLocation != null
				&& amapLocation.getAMapException().getErrorCode() == 0) {
			// 定位成功回调信息，设置相关消息
//			mLocationLatlngTextView.setText(amapLocation.getLatitude() + "  "
//					+ amapLocation.getLongitude());

//			mLocationDesTextView.setText(amapLocation.getAddress());

//			mLocationCountryTextView.setText(amapLocation.getCountry());
//			if (amapLocation.getProvince() == null) {
//				mLocationProvinceTextView.setText("null");
//			} else {
//				mLocationProvinceTextView.setText(amapLocation.getProvince());
//			}
//			mLocationCityTextView.setText(amapLocation.getCity());
//
//			showToast("纬度：" + amapLocation.getLatitude() + " 经度 "
//					+ amapLocation.getLongitude() + " 城市 " + amapLocation.getCity());
			writePreference("latitude", amapLocation.getLatitude() + "");
			writePreference("longitude",amapLocation.getLongitude() +"");
			writePreference("latitude","30.312021");
			writePreference("longitude","120.255116");
			writePreference("located_city",amapLocation.getCity() +"");
//			writePreference("selected_city",amapLocation.getCity() +"");
			FirstPageFragment.getInstance().refreshCity();
			CityListActivity.getInstance().refreshCity();
//			mLocationCountyTextView.setText(amapLocation.getDistrict());
//			mLocationRoadTextView.setText(amapLocation.getRoad());
//			mLocationPOITextView.setText(amapLocation.getPoiName());
//			mLocationCityCodeTextView.setText(amapLocation.getCityCode());
//			mLocationAreaCodeTextView.setText(amapLocation.getAdCode());
		} else {
			Log.e("AmapErr", "Location ERR:" + amapLocation.getAMapException().getErrorCode());
		}

	}

	@Override
	public void onLocationChanged(Location location) {

	}

	@Override
	public void onStatusChanged(String s, int i, Bundle bundle) {

	}

	@Override
	public void onProviderEnabled(String s) {

	}

	@Override
	public void onProviderDisabled(String s) {

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
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * 删除 SharedPreferences
	 *
	 * @param key
	 */
	public void deletePreference(String key) {
		SharedPreferences sharedPreferences = getSharedPreferences(NAME_COMMON,
				MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
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
		SharedPreferences.Editor editor = sharedPreferences.edit();
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
		SharedPreferences.Editor editor = sharedPreferences.edit();
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
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.clear();
		editor.commit();
	}
}
