package com.miaotu.imutil;

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

public class IMDatabaseHelper {
	public DBOpenHelper messageDbHelper;
	public static Context context;
	public static final String DATABASE_IM = "im";

	public static final String TABLE_CONTACT_INFO = "contact_info";

	public IMDatabaseHelper(Context context) {
		this.context = context;
		copyDataBase(context ,DATABASE_IM);
		messageDbHelper = new DBOpenHelper(context, DATABASE_IM, null, 1);
		
	}
	/**
	 * 插入或者更新联系人信息
	 * @param contactInfo
	 * @return
	 */
	public long saveContactInfo(ContactInfo contactInfo) {
		long l = -1;
		try {
			SQLiteDatabase db = messageDbHelper.getWritableDatabase();
			ContentValues cv = new ContentValues();
			
			cv.put("im_id", contactInfo.getImId());
			cv.put("uid", contactInfo.getUid());
			cv.put("nick_name", contactInfo.getNickName());
			cv.put("head_photo", contactInfo.getHeadPhoto());
			// 插入ContentValues中的数据
			l = db.replace(TABLE_CONTACT_INFO, null, cv);
			db.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return l;

	}

    /**
     * 查询单个联系人
     * @param id
     * @return
     */
    public ContactInfo getContactInfo(String id) {
        SQLiteDatabase db = messageDbHelper.getWritableDatabase();
        String sql = "select * from "+TABLE_CONTACT_INFO+" where im_id='"+id+"'";
        Cursor cursor = db.rawQuery(sql, null);
        ContactInfo contactInfo = new ContactInfo();
        while (cursor.moveToNext()) {
            contactInfo.setImId(cursor.getString(cursor
                    .getColumnIndex("im_id")));
            contactInfo.setUid(cursor.getString(cursor
                    .getColumnIndex("uid")));
            contactInfo.setNickName(cursor.getString(cursor
                    .getColumnIndex("nick_name")));
            contactInfo.setHeadPhoto(cursor.getString(cursor
                    .getColumnIndex("head_photo")));
        }
        if (!(cursor == null||cursor.isClosed())) {
            cursor.close();
        }
        db.close();
        return contactInfo;

    }
	/**
	 * 获取所有联系人信息
	 * @return
	 */
	public List<ContactInfo> getAllContactInfo(){
		List<ContactInfo>list = new ArrayList<ContactInfo>();
		SQLiteDatabase db = messageDbHelper.getWritableDatabase();
		String sql = "select * from "+TABLE_CONTACT_INFO;
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			ContactInfo contactInfo = new ContactInfo();
			contactInfo.setImId(cursor.getString(cursor
					.getColumnIndex("im_id")));
			contactInfo.setUid(cursor.getString(cursor
					.getColumnIndex("uid")));
			contactInfo.setNickName(cursor.getString(cursor
					.getColumnIndex("nick_name")));
			contactInfo.setHeadPhoto(cursor.getString(cursor
					.getColumnIndex("head_photo")));
			list.add(contactInfo);
		}
		if (!(cursor == null||cursor.isClosed())) {
			cursor.close();
		}
		db.close();
		return list;
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
				final File file = context.getDatabasePath(DATABASE_IM);
				file.delete();
				copyDataBase(context, DATABASE_IM);
				onCreate(db);
			}
		}

	}

}
