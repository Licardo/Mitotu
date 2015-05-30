package com.miaotu.jpush;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.miaotu.util.LogUtil;

public class MessageDatabaseHelper {
	public DBOpenHelper messageDbHelper;
	public static Context context;
	public static final String DATABASE_CHAT_NAME = "message";

	public static final String TABLE_INVITE_MESSAGE = "invite_message";
	public static final String TABLE_SYS_MESSAGE = "sys_message";
	public static final String TABLE_CHAT_MSG_COUNT = "msg_count";

	public MessageDatabaseHelper(Context context) {
		this.context = context;
		copyDataBase(context ,DATABASE_CHAT_NAME);
		messageDbHelper = new DBOpenHelper(context, DATABASE_CHAT_NAME, null, 2);
		
	}
	
	/**
	 * 保存邀请信息
	 * 
	 * @param
	 * @return
	 */
	public long saveInviteMessage(InviteMessage inviteMessage) {
		long l = -1;
		try {
			SQLiteDatabase db = messageDbHelper.getWritableDatabase();
			ContentValues cv = new ContentValues();
			
			cv.put("invite_status", inviteMessage.getInviteStatus());
			cv.put("invite_content", inviteMessage.getInviteContent());
			cv.put("movement_end_date", inviteMessage.getMovementEndDate());
			cv.put("movement_city", inviteMessage.getMovementCity());
			cv.put("movement_name", inviteMessage.getMovementName());
			cv.put("movement_id", inviteMessage.getMovementId());
			cv.put("user_age", inviteMessage.getUserAge());
			cv.put("user_id", inviteMessage.getUserId());
			cv.put("user_name", inviteMessage.getUserName());
			cv.put("user_name", inviteMessage.getUserName());
			cv.put("message_date", inviteMessage.getMessageDate());
			cv.put("message_status", inviteMessage.getMessageStatus());
			cv.put("user_gender", inviteMessage.getUserGender());

			// 插入ContentValues中的数据
			l = db.insert(TABLE_INVITE_MESSAGE, null, cv);
			db.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return l;

	}
	/**
	 * 获取未读邀请信息数量
	 * @return
	 */
	public int getInviteMessageNum(){
		int l = 0;
		SQLiteDatabase db = messageDbHelper.getWritableDatabase();
		String sql = "select * from "+TABLE_INVITE_MESSAGE+" where message_status=? ";
		Cursor cursor = db.rawQuery(sql, new String[]{"0"});
		while (cursor.moveToNext()) {
			l++;
		}
		if (!(cursor.isClosed() || cursor == null)) {
			cursor.close();
		}
		 db.close();
		return l;
	}
	/**
	 * 获取所有邀请消息
	 * @return
	 */
	public List<InviteMessage> getAllInviteMessage(){
		List<InviteMessage>list = new ArrayList<InviteMessage>();
		SQLiteDatabase db = messageDbHelper.getWritableDatabase();
		String sql = "select * from "+TABLE_INVITE_MESSAGE+" order by message_date desc";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			InviteMessage inviteMessage = new InviteMessage();
			inviteMessage.setInviteContent(cursor.getString(cursor
					.getColumnIndex("invite_content")));
			inviteMessage.setInviteId(cursor.getInt(cursor
					.getColumnIndex("invite_id")));
			inviteMessage.setInviteStatus(cursor.getString(cursor
					.getColumnIndex("invite_status")));
			inviteMessage.setMessageDate(cursor.getString(cursor
					.getColumnIndex("message_date")));
			inviteMessage.setMessageStatus(cursor.getString(cursor
					.getColumnIndex("message_status")));
			inviteMessage.setMovementCity(cursor.getString(cursor
					.getColumnIndex("movement_city")));
			inviteMessage.setMovementEndDate(cursor.getString(cursor
					.getColumnIndex("movement_end_date")));
			inviteMessage.setMovementId(cursor.getString(cursor
					.getColumnIndex("movement_id")));
			inviteMessage.setMovementName(cursor.getString(cursor
					.getColumnIndex("movement_name")));
			inviteMessage.setUserAge(cursor.getString(cursor
					.getColumnIndex("user_age")));
			inviteMessage.setUserId(cursor.getString(cursor
					.getColumnIndex("user_id")));
			inviteMessage.setUserName(cursor.getString(cursor
					.getColumnIndex("user_name")));
			inviteMessage.setUserGender(cursor.getString(cursor
					.getColumnIndex("user_gender")));
			list.add(inviteMessage);
		}
		if (!(cursor == null||cursor.isClosed())) {
			cursor.close();
		}
		db.close();
		return list;
	}
	/**
	 * 重置邀请新消息数量
	 * @return
	 */
	public int resetInviteMessageNo(){
		int i = 0;
		SQLiteDatabase db = messageDbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("message_status", "1");
		i  = db.update(TABLE_INVITE_MESSAGE, cv, "message_status = ?", new String[]{"0"});
		return i ;
	}
	/**
	 * 更新邀请消息的状态为已经同意或者拒绝
	 * @return
	 */
	public int resetInviteMessageStatus(String messageId,String status){
		int i = 0;
		SQLiteDatabase db = messageDbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("invite_status", status);
		i  = db.update(TABLE_INVITE_MESSAGE, cv, "invite_id = ?", new String[]{messageId});
		return i ;
	}
	/**
	 * 保存系统消息
	 * 
	 * @param chatInfo
	 * @return
	 */
	public long saveSysMessage(SystemMessage systemMessage) {
		long l = -1;
		try {
			SQLiteDatabase db = messageDbHelper.getWritableDatabase();
			ContentValues cv = new ContentValues();
			
			cv.put("message_status", systemMessage.getMessageStatus());
			cv.put("message_type", systemMessage.getMessageType());
			cv.put("user_interest_count", systemMessage.getUserInterestCount());
			cv.put("user_gender", systemMessage.getUserGender());
			cv.put("user_age", systemMessage.getUserAge());
			cv.put("user_id", systemMessage.getUserId());
			cv.put("user_name", systemMessage.getUserName());
			cv.put("message_content", systemMessage.getMessageContent());
			cv.put("message_date", systemMessage.getMessageDate());
			// 插入ContentValues中的数据
			l = db.insert(TABLE_SYS_MESSAGE, null, cv);
			db.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return l;
		
	}
	/**
	 * 获取未读系统消息数量
	 * @return
	 */
	public int getSysMessageNum(){
		int l = 0;
		SQLiteDatabase db = messageDbHelper.getWritableDatabase();
		String sql = "select * from "+TABLE_SYS_MESSAGE+" where message_status=? ";
		Cursor cursor = db.rawQuery(sql, new String[]{"0"});
		while (cursor.moveToNext()) {
			l++;
		}
		if (!(cursor.isClosed() || cursor == null)) {
			cursor.close();
		}
		db.close();
		return l;
	}
	/**
	 * 获取所有邀请消息
	 * @return
	 */
	public List<SystemMessage> getAllSysMessage(){
		List<SystemMessage>list = new ArrayList<SystemMessage>();
		SQLiteDatabase db = messageDbHelper.getWritableDatabase();
		String sql = "select * from "+TABLE_SYS_MESSAGE+" order by message_date desc";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			SystemMessage systemMessage = new SystemMessage();
			systemMessage.setMessageContent(cursor.getString(cursor
					.getColumnIndex("message_content")));
			systemMessage.setMessageDate(cursor.getString(cursor
					.getColumnIndex("message_date")));
			systemMessage.setMessageId(cursor.getInt(cursor
					.getColumnIndex("sys_message_id")));
			systemMessage.setMessageStatus(cursor.getString(cursor
					.getColumnIndex("message_status")));
			systemMessage.setMessageType(cursor.getString(cursor
					.getColumnIndex("message_type")));
			systemMessage.setUserAge(cursor.getString(cursor
					.getColumnIndex("user_age")));
			systemMessage.setUserGender(cursor.getString(cursor
					.getColumnIndex("user_gender")));
			systemMessage.setUserId(cursor.getString(cursor
					.getColumnIndex("user_id")));
			systemMessage.setUserInterestCount(cursor.getString(cursor
					.getColumnIndex("user_interest_count")));
			systemMessage.setUserName(cursor.getString(cursor
					.getColumnIndex("user_name")));
			list.add(systemMessage);
		}
		if (!(cursor == null||cursor.isClosed())) {
			cursor.close();
		}
		db.close();
		return list;
	}
	/**
	 * 重置邀请新消息数量
	 * @return
	 */
	public int resetSysMessageNo(){
		int i = 0;
		SQLiteDatabase db = messageDbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("message_status", "1");
		i  = db.update(TABLE_SYS_MESSAGE, cv, "message_status = ?", new String[]{"0"});
		return i ;
	}
	/**
	 * 删除推送消息
	 * @return
	 */
	public void deletePushMessage(){
		try {
			SQLiteDatabase db = messageDbHelper.getWritableDatabase();
			db.execSQL("DELETE FROM "+TABLE_INVITE_MESSAGE);
			db.execSQL("DELETE FROM "+TABLE_SYS_MESSAGE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * copy数据库
	 */
	public static void copyDataBase(Context context ,String dbName) {
		if(context == null)
			return;
		OutputStream os = null;
		File dbFile = context.getDatabasePath(dbName);
		if (dbFile.exists()) {
			return;
		}
		File dirDatabase = new File(dbFile.getParent());
		dirDatabase.mkdirs();
		try {
			dbFile.createNewFile();
			LogUtil.d("拷贝数据库");
			os = new FileOutputStream(dbFile.getAbsolutePath());
			InputStream open = context.getAssets().open(dbName);
			byte[] b = new byte[1024 * 512];
			int len;
			while ((len = open.read(b)) > 0) {
				os.write(b, 0, len);
			}
			os.flush();
			open.close();

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} finally {
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * SQLiteOpenHelper
	 * 
	 * @author yurf
	 * 
	 */
	private static class DBOpenHelper extends SQLiteOpenHelper {

		public DBOpenHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);

		}

		@Override
		public void onCreate(SQLiteDatabase db) {

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			if (oldVersion == 1) {
				final File file = context.getDatabasePath(DATABASE_CHAT_NAME);
				file.delete();
				copyDataBase(context, DATABASE_CHAT_NAME);
				onCreate(db);
			}
		}

	}

}
