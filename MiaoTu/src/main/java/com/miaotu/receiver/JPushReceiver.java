package com.miaotu.receiver;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miaotu.jpush.InviteMessage;
import com.miaotu.jpush.MessageCountDatabaseHelper;
import com.miaotu.jpush.MessageDatabaseHelper;
import com.miaotu.jpush.SystemMessage;
import com.miaotu.util.LogUtil;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class JPushReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";
	public static final String ACTION_JPUSH_INVITE_MESSAGE_RECIEVE = "action_jpush_invite_message_recieve";
	public static final String ACTION_JPUSH_SYS_MESSAGE_RECIEVE = "action_jpush_sys_message_recieve";
	public static final String ACTION_JPUSH_TOPIC_MESSAGE_RECIEVE = "action_jpush_topic_message_recieve";
	public static final String ACTION_UPDATE_MESSAGE_UI = "action_update_message_ui";
	public static final String ACTION_JPUSH_LOGIN_MESSAGE_RECIEVE = "action_jpush_login_message_recieve";
	public static final String ACTION_JPUSH_EXIT_MESSAGE_RECIEVE = "action_jpush_exit_message_recieve";
	public static final String ACTION_JPUSH_MY_INFO_MESSAGE_RECIEVE = "action_jpush_my_info_message_recieve";
	public static final String ACTION_JPUSH_INTEREST_MESSAGE_RECIEVE = "action_jpush_interest_message_recieve";
//	public static final String ACTION_TEST = "action_test";
	@Override
	public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
		Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
		
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...
                        
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
        	Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
        	processCustomMessage(context, bundle);
        
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
        	
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
            
        	//打开自定义的Activity
//        	Intent i = new Intent(context, TestActivity.class);
//        	i.putExtras(bundle);
//        	//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
//        	context.startActivity(i);
        	
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
        	
        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
        	boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
        	Log.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
        } else {
        	Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} 
			else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}
	
	//send msg to MainActivity
	private void processCustomMessage(Context context, Bundle bundle) {
		String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
		//PushMessage pushMessage = new PushMessage();
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		try {
			//pushMessage = mapper.readValue(message,PushMessage.class);
			InviteMessage inviteMessage = new InviteMessage();
			JSONObject rootJson = new JSONObject(message);
			if(rootJson.get("type").equals("invite")){
				//邀请消息
				inviteMessage = mapper.readValue(rootJson.getJSONObject("item").toString(), InviteMessage.class);
				inviteMessage.setMessageStatus("0");
				MessageDatabaseHelper helper = new MessageDatabaseHelper(context);
				long l = helper.saveInviteMessage(inviteMessage);
				LogUtil.d("插入收到的邀请消息"+l);
				Intent msgIntent = new Intent(ACTION_JPUSH_INVITE_MESSAGE_RECIEVE);
				msgIntent.putExtras(new Bundle());
				context.sendOrderedBroadcast(msgIntent,null);
			}else if(rootJson.get("type").equals("like")){
				//系统消息-喜欢
				SystemMessage systemMessage = new SystemMessage();
				systemMessage = mapper.readValue(rootJson.getJSONObject("item").toString(), SystemMessage.class);
				systemMessage.setMessageStatus("0");
				systemMessage.setMessageType("喜欢提醒");
				MessageDatabaseHelper helper = new MessageDatabaseHelper(context);
				long l = helper.saveSysMessage(systemMessage);
				LogUtil.d("插入收到的系统消息：喜欢"+l);
				Intent msgIntent = new Intent(ACTION_JPUSH_SYS_MESSAGE_RECIEVE);
				msgIntent.putExtras(new Bundle());
				context.sendOrderedBroadcast(msgIntent,null);
			}else if(rootJson.get("type").equals("order")){
                //系统消息-订单
                SystemMessage systemMessage = new SystemMessage();
                systemMessage = mapper.readValue(rootJson.getJSONObject("item").toString(), SystemMessage.class);
                systemMessage.setMessageStatus("0");
                systemMessage.setMessageType("订单通知");
                MessageDatabaseHelper helper = new MessageDatabaseHelper(context);
                long l = helper.saveSysMessage(systemMessage);
                LogUtil.d("插入收到的系统消息：订单"+l);
                Intent msgIntent = new Intent(ACTION_JPUSH_SYS_MESSAGE_RECIEVE);
                msgIntent.putExtras(new Bundle());
                context.sendOrderedBroadcast(msgIntent,null);
            }else if(rootJson.get("type").equals("topic")){
                //系统消息-社区回复
                MessageCountDatabaseHelper helper = new MessageCountDatabaseHelper(context);
                long l = helper.saveMessage("topic");
                LogUtil.d("插入收到的社区回复消息："+l);
                Intent msgIntent = new Intent(ACTION_JPUSH_TOPIC_MESSAGE_RECIEVE);
                msgIntent.putExtras(new Bundle());
                context.sendOrderedBroadcast(msgIntent,null);
            }
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		if (MainActivity.isForeground) {
//			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//			Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//			msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
//			if (!ExampleUtil.isEmpty(extras)) {
//				try {
//					JSONObject extraJson = new JSONObject(extras);
//					if (null != extraJson && extraJson.length() > 0) {
//						msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
//					}
//				} catch (JSONException e) {
//
//				}
//
//			}
//			context.sendBroadcast(msgIntent);
//		}
 catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
