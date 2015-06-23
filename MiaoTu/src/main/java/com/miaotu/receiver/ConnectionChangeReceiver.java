package com.miaotu.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

public class ConnectionChangeReceiver extends BroadcastReceiver {
	private static final String TAG = ConnectionChangeReceiver.class
			.getSimpleName();

	@Override 
    public void onReceive(Context context, Intent intent) {  
        ConnectivityManager connectivityManager=  
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);  
        if (connectivityManager!=null) {  
            NetworkInfo [] networkInfos=connectivityManager.getAllNetworkInfo();  
            for (int i = 0; i < networkInfos.length; i++) {  
                State state=networkInfos[i].getState();  
                if (State.CONNECTED==state) {
                	SharedPreferences sharedPreferences = context.getSharedPreferences("COMMON",
            				Context.MODE_PRIVATE);
                	if (sharedPreferences.getString("login_status","").equals("in")) {
//                		XmppConnection.getInstance().closeConnection();
//                		ChatHelper.getInstance().loginOpenfire(context,
//                				sharedPreferences.getString("id",""), sharedPreferences.getString("token",""));
                		return;
                	}
                   
                }  
            }  
        }  
           
        //没有执行return,则说明当前无网络连接  
       // Toast.makeText(context,"网络已中断",1000).show();  
    }  


}
