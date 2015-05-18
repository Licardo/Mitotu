package com.miaotu.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.StrictMode;
import android.provider.CallLog.Calls;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;


public class Util {

    // 获取可变UUID

    public String getMyUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    // 获取本机号

    public static String getNumber(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String phoneId = tm.getLine1Number();
        if (phoneId == null) {
            return "";
        }
        return phoneId;
    }

    // MD5加密，32位
    public static String MD5(String str) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);

        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    // 可逆的加密算法
    public String encryptmd5(String str) {
        char[] a = str.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 'l');
        }
        String s = new String(a);
        return s;
    }

    // 判断时间date1是否在时间date2之前
    public int isDateBefore(String date1, String date2) {
        return date1.compareTo(date2);
    }

    // 判断两个时间是否相差12小时以上
    public long getExceed(Date d1, Date d2) {
        long hours = 0;
        try {
            long diff = d1.getTime() - d2.getTime();
            hours = diff / (1000 * 60 * 60);
        } catch (Exception e) {
        }
        return hours;
    }

    // 删除指定文件夹下所有文件
    // param path 文件夹完整绝对路径
    public boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
                // delFolder(path + "/" + tempList[i]);//再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

    // 获取当前版名称
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    "cn.thumbworld.leihaowu", 0);
            versionName = packageInfo.versionName;
            if (StringUtil.isEmpty(versionName)) {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 获取版本号
     * @param context
     * @return
     */
    public static int getAppVersionCode(Context context){
        int versionCode = -1;
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    "cn.thumbworld.leihaowu", 0);
            versionCode = packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    public static void saveBitmap(String path, Bitmap mBitmap) {
        File file = new File(path);
        try {
            file.createNewFile();
            FileOutputStream fOut = null;
            fOut = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Drawable getPic(Context context, String imgUrl) {
        String fileName = imgUrl.substring(imgUrl.lastIndexOf("/") + 1);
        File file = new File(context.getCacheDir(), fileName);// 保存文件
        Drawable drawable = (BitmapDrawable) Drawable.createFromPath(file
                .toString());
        return drawable;
    }

    public static boolean isPicExists(Context context, String imgUrl) {
        String fileName = imgUrl.substring(imgUrl.lastIndexOf("/") + 1);
        File file = new File(context.getCacheDir(), fileName);// 保存文件
        if (!file.exists() && !file.isDirectory()) {
            return false;
        }
        return true;
    }

    /**
     * 获取机器imei号
     *
     * @param context
     * @return
     */
    public static String getImei(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        return imei;
    }

    /**
     * 返回号码上次拨打后通话时间
     *
     * @param activity
     * @param number
     * @return
     */
    @SuppressWarnings("deprecation")
    public static long getLastCallDuration(Activity activity, String number) {
        number = "10086";
        Log.d("Debug", Calls.CONTENT_URI.toString());
        Cursor cursor = activity.getContentResolver().query(Calls.CONTENT_URI,
                new String[] { Calls.DURATION, Calls.TYPE, Calls.DATE },
                Calls.NUMBER + "=?", new String[] { number },
                Calls.DEFAULT_SORT_ORDER);
        activity.startManagingCursor(cursor);
        boolean hasRecord = cursor.moveToLast();
        long outgoing = 0L;
        while (hasRecord) {
            int type = cursor.getInt(cursor.getColumnIndex(Calls.TYPE));
            long duration = cursor.getLong(cursor
                    .getColumnIndex(Calls.DURATION));
            switch (type) {
                case Calls.OUTGOING_TYPE:
                    outgoing = duration;
                    break;

                default:
                    break;
            }
            hasRecord = cursor.moveToPrevious();
        }
        // Log.d("DebugInfo", "上次通话 . 总通话时长 " + outgoing + "秒");
        return outgoing;
    }

    private static final int CHECK_INTERVAL = 1000 * 30;

    public static boolean isBetterLocation(Location location,
                                           Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > CHECK_INTERVAL;
        boolean isSignificantlyOlder = timeDelta < -CHECK_INTERVAL;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location,
        // use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must
            // be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation
                .getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and
        // accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate
                && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /** Checks whether two providers are the same */
    private static boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    /**
     * 保留N位小数
     *
     * @param src
     *            需要格式化的小数
     * @param n
     *            保留的位数
     * @return 格式化后的数字字符串
     */
    public static String keepNDouble(double src, int n) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(n);
        return nf.format(src);
    }

    @SuppressLint("SimpleDateFormat")
    public static String formatDate(Date date, String pattern) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat(pattern).format(date);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static boolean isHtml5Video(String str) {
        if (str == null) {
            return false;
        }
        if (str.trim().startsWith("<div")) {
            return true;
        }
        return false;
    }

    public static boolean isFlashVideo(String str) {
        if (str == null) {
            return false;
        }
        if (str.trim().startsWith("<embed")) {
            return true;
        }
        return false;
    }

    public static boolean isUrl(String str) {
        if (str == null) {
            return false;
        }
        if (str.trim().startsWith("http://")) {
            return true;
        }
        return false;
    }

    public static String getIpAddress(Context context) {
        URL infoUrl = null;
        InputStream inStream = null;
        try {
            // http://iframe.ip138.com/ic.asp
            // infoUrl = new URL("http://city.ip138.com/city0.asp");
            infoUrl = new URL("http://iframe.ip138.com/ic.asp");
            URLConnection connection = infoUrl.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.getInputStream();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inStream, "utf-8"));
                StringBuilder strber = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                    strber.append(line + "\n");
                inStream.close();
                // 从反馈的结果中提取出IP地址
                int start = strber.indexOf("[");
                int end = strber.indexOf("]", start + 1);
                line = strber.substring(start + 1, end);
                return line;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        try{

        ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable();
        }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取文件大小
     * */

    public static int getFileSize(String path) {

        File file = new File(path);

        if (file.exists() && file.isFile()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);

                return fis.available() / 1024;

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    if (fis != null)
                        fis.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        return 0;
    }

    /**
     * 获取文件后缀名
     *
     * @return
     */
    public static String getExtName(File file) {
        String fileName = file.getName();
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
    /**
     * get current time
     *
     * @return yyyy-mm-dd
     */
    public static String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
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
    /**
     * get current time
     *
     * @return yyyy-mm-dd
     */
    public static String getWholeTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sTime = format.format(new Date());
        System.out.println("CurrentTime:" + sTime);
        return sTime;
        // return String.format("%04d-%02d-%02d ", date.getYear() + 1900,
        // date.getMonth() + 1, date.getDate());

    }

    public static boolean isInLimitTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String time = sdf.format(new Date());
        try {
            Date currentTime = sdf.parse(time);
            Date endTime = sdf.parse("23:00:00");
            Date beginTime = sdf.parse("09:00:00");
            if(currentTime.after(beginTime) && currentTime.before(endTime)){
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * listview嵌套在其他可滑动layout中时，重新计算listview高度，不然listview只能显示一行
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        //((MarginLayoutParams) params).setMargins(10, 10, 10, 10); // 可删除

        listView.setLayoutParams(params);
    }

//    @SuppressLint("NewApi")
//    public static void enableStrictMode() {
//        if(Util.hasGingerbread())
//        {
//            StrictMode.ThreadPolicy.Builder threadPolicyBuilder =
//                    new StrictMode.ThreadPolicy.Builder()
//                            .detectAll()
//                            .penaltyLog();
//            StrictMode.VmPolicy.Builder vmPolicyBuilder =
//                    new StrictMode.VmPolicy.Builder()
//                            .detectAll()
//                            .penaltyLog();
//
//            if (Util.hasHoneycomb()) {
//                threadPolicyBuilder.penaltyFlashScreen();
//                vmPolicyBuilder
//                        .setClassInstanceLimit(ImageGridActivity.class, 1);
//            }
//            StrictMode.setThreadPolicy(threadPolicyBuilder.build());
//            StrictMode.setVmPolicy(vmPolicyBuilder.build());
//        }
//
//
//
//
//
//    }

    public static boolean hasFroyo() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.FROYO;

    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB_MR1;
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN;
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= 19;
    }

    public static List<Size> getResolutionList(Camera camera)
    {
        Parameters parameters = camera.getParameters();
        List<Size> previewSizes = parameters.getSupportedPreviewSizes();
        return previewSizes;
    }

    public static class ResolutionComparator implements Comparator<Size>{

        @Override
        public int compare(Size lhs, Size rhs) {
            if(lhs.height!=rhs.height)
                return lhs.height-rhs.height;
            else
                return lhs.width-rhs.width;
        }

    }



}
