package com.miaotu.jpush;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.miaotu.util.LogUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MessageCountDatabaseHelper {
	public DBOpenHelper messageDbHelper;
	public static Context context;
	public static final String DATABASE_NAME = "message_count";

	public static final String TABLE_NAME = "t_message_count";

	public MessageCountDatabaseHelper(Context context) {
		this.context = context;
		copyDataBase(context ,DATABASE_NAME);
		messageDbHelper = new DBOpenHelper(context, DATABASE_NAME, null, 2);
		
	}
	
	/**
	 * 保存消息数量
	 * 
	 * @param
	 * @return
	 */
	public long saveMessage(String type) {
		long l = -1;
		try {
            int count = getMessageNum(type);
			SQLiteDatabase db = messageDbHelper.getWritableDatabase();
			ContentValues cv = new ContentValues();
			
			cv.put("type", type);
			cv.put("count", count+1);
			// 插入ContentValues中的数据
			l = db.replace(TABLE_NAME, null, cv);
			db.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return l;
		
	}
	/**
	 * 获取消息数量
	 * @return
	 */
	public int getMessageNum(String type){
		int l = 0;
		SQLiteDatabase db = messageDbHelper.getWritableDatabase();
		String sql = "select * from "+TABLE_NAME+" where type=? ";
		Cursor cursor = db.rawQuery(sql, new String[]{type});
		while (cursor.moveToNext()) {
			l=cursor.getInt(cursor.getColumnIndex("count"));
		}
		if (!(cursor.isClosed() || cursor == null)) {
			cursor.close();
		}
		db.close();
		return l;
	}
	/**
	 * 重置消息数量
	 * @return
	 */
	public long resetMessageNo(String type){
            long l = -1;
            try {
                SQLiteDatabase db = messageDbHelper.getWritableDatabase();
                ContentValues cv = new ContentValues();

                cv.put("type", type);
                cv.put("count", 0);
                // 插入ContentValues中的数据
                l = db.replace(TABLE_NAME, null, cv);
                db.close();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return l;

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
				final File file = context.getDatabasePath(DATABASE_NAME);
				file.delete();
				copyDataBase(context, DATABASE_NAME);
				onCreate(db);
			}
		}

	}

}
