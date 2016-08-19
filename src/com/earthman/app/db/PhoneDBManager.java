package com.earthman.app.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.earthman.app.R;
import com.earthman.app.bean.CountryPhoneItem;
import com.earthman.app.utils.Constants;

/**
 * 作者：Zhou
 * 日期：2016-3-8 下午5:06:13
 * 描述：（）
 */
public class PhoneDBManager {

	private static final String TAG = "AddrDBManager";

	private final int BUFFER_SIZE = 1024;		
	private final static byte[] DB_LOCK = new byte[0];
	private Context context;
	private File file = null;
	private String dirPath;

	public PhoneDBManager(Context context) {
		this.context = context;
		this.dirPath = Constants.PHONE_DB_PATH;
	}

	// 获取数据库实例
	private SQLiteDatabase openDatabase() {
		try {
			Log.e(TAG, "open and return");
			file = new File(dirPath);
			if (!file.exists()) {
				Log.e(TAG, "file");
				InputStream is = context.getResources().openRawResource(
						R.raw.phone);
				if (is != null) {
					Log.e(TAG, "is null");
				} else {
				}
				FileOutputStream fos = new FileOutputStream(dirPath);
				if (is != null) {
					Log.e(TAG, "fosnull");
				} else {
				}
				byte[] buffer = new byte[BUFFER_SIZE];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
					fos.flush();
				}
				fos.close();
				is.close();
			}

			return SQLiteDatabase.openOrCreateDatabase(dirPath,
					null);
		} catch (FileNotFoundException e) {
			Log.e(TAG, "File not found");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(TAG, "IO exception");
			e.printStackTrace();
		} catch (Exception e) {
			Log.e(TAG, "exception " + e.toString());
		}
		return null;
	}
	
		
	public ArrayList<CountryPhoneItem> getCountryPhones() {
		SQLiteDatabase db = null;
		Cursor cursor = null;
		ArrayList<CountryPhoneItem> countryPhoneItems = new ArrayList<CountryPhoneItem>();
		synchronized (DB_LOCK) {
			try {
				db = openDatabase();
				if (db == null) {
					return null;
				}
				String sql = "select * from countryphone";
				cursor = db.rawQuery(sql, null);
				if (cursor != null && cursor.moveToFirst()) {
					do {
						CountryPhoneItem item = new CountryPhoneItem(cursor.getInt(cursor
								.getColumnIndex("id")),
								cursor.getString(cursor
										.getColumnIndex("countrycode")),
								cursor.getString(cursor
										.getColumnIndex("phonecode")));
						countryPhoneItems.add(item);
					} while (cursor.moveToNext());
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (cursor != null) {
					cursor.close();
				}
				if (db != null) {
					db.close();
				}
			}

		}
		return countryPhoneItems;
	}
}
