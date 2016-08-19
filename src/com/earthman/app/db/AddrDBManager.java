package com.earthman.app.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.litepal.util.LogUtil;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.earthman.app.R;
import com.earthman.app.bean.AddrItem;
import com.earthman.app.utils.Constants;

public class AddrDBManager {
	private static final String TAG = "AddrDBManager";

	private final int BUFFER_SIZE = 1024;		
	private final static byte[] DB_LOCK = new byte[0];
	private Context context;
	private File file = null;
	private String dirPath;

	public AddrDBManager(Context context) {
		this.context = context;
		this.dirPath = Constants.ADDR_DB_PATH;
	}

	// 获取数据库实例
	private SQLiteDatabase openDatabase() {
		try {
			Log.e(TAG, "open and return");
			file = new File(dirPath);
			if (!file.exists()) {
				Log.e(TAG, "file");
				InputStream is = context.getResources().openRawResource(
						R.raw.addr);
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

	/**
	 * parentId = 0:获取所有省份信息
	 * 
	 * parentId = addr_id:获取某一省份的所有城市信息或某一城市的所有县区信息 <br/>
	 * 
	 * @param [parentId]-[] <br/>
	 */
	public ArrayList<AddrItem> getAddrs(int parentId) {
		SQLiteDatabase db = null;
		Cursor cursor = null;
		ArrayList<AddrItem> addrItems = new ArrayList<AddrItem>();
		synchronized (DB_LOCK) {
			try {
				db = openDatabase();
				if (db == null) {
					return null;
				}
				String sql = "select * from ADDR where PARENT_ID = " + parentId;
				cursor = db.rawQuery(sql, null);
				if (cursor != null && cursor.moveToFirst()) {
					do {
						AddrItem item = new AddrItem(cursor.getInt(cursor
								.getColumnIndex("ADDR_ID")),
								cursor.getInt(cursor
										.getColumnIndex("ADDR_CODE")),
								cursor.getString(cursor
										.getColumnIndex("ADDR_NAME")),
								cursor.getInt(cursor
										.getColumnIndex("PARENT_ID")));
						if (!"市辖区".equals(item.getAddrName())) {
							addrItems.add(item);
						}
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
		LogUtil.d("com.njhn.weetmall", "查询数据库：" + addrItems.size() + "  "
				+ addrItems.toString());
		return addrItems;
	}

	public ArrayList<AddrItem> getAllCitys() {
		SQLiteDatabase db = null;
		Cursor cursor = null;
		ArrayList<AddrItem> addrItems = new ArrayList<AddrItem>();
		synchronized (DB_LOCK) {
			try {
				db = openDatabase();
				if (db == null) {
					return null;
				}
				String sql = "select * from addr where parent_id in(select addr_id from addr where parent_id=0)";
				cursor = db.rawQuery(sql, null);
				if (cursor != null && cursor.moveToFirst()) {
					do {
						AddrItem item = new AddrItem(cursor.getInt(cursor
								.getColumnIndex("ADDR_ID")),
								cursor.getInt(cursor
										.getColumnIndex("ADDR_CODE")),
								cursor.getString(cursor
										.getColumnIndex("ADDR_NAME")),
								cursor.getInt(cursor
										.getColumnIndex("PARENT_ID")));
						addrItems.add(item);
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
		LogUtil.d("com.njhn.weetmall", "查询数据库：" + addrItems.size() + "  "
				+ addrItems.toString());
		return addrItems;
	}

	/**
	 * 根据地区码查询对应的地区名称 如420000|420100|420111
	 */
	public String getAddrName(String addrCode) {
		if (TextUtils.isEmpty(addrCode)) {
			return null;
		}
		StringBuffer stringBuffer = new StringBuffer();
		SQLiteDatabase db = null;
		Cursor cursor = null;
		synchronized (DB_LOCK) {
			try {
				db = openDatabase();
				if (db == null) {
					return null;
				}
				String sql = "select * from ADDR where ADDR_CODE = " + addrCode;
				LogUtil.d("com.njhn.weetmall", "sql = " + sql);
				cursor = db.rawQuery(sql, null);
				if (cursor != null && cursor.moveToFirst()) {
					do {
						stringBuffer.append(cursor.getString(cursor
								.getColumnIndex("ADDR_NAME")));
						stringBuffer.append(" ");
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
		return stringBuffer.toString();
	}

	/**
	 * 根据地区ID查询对应的地区码 如420000|420100|420111
	 * 
	 * @param addrId
	 *            地区ID
	 * @return 地区码
	 */
	public String getAddrCode(int addrId) {
		String addrCode = "";
		SQLiteDatabase db = null;
		Cursor cursor = null;
		synchronized (DB_LOCK) {
			try {
				db = openDatabase();
				if (db == null) {
					return null;
				}
				String sql = "select ADDR_CODE from ADDR where ADDR_ID="
						+ addrId;
				LogUtil.d("com.njhn.weetmall", "sql = " + sql);
				cursor = db.rawQuery(sql, null);
				if (cursor != null && cursor.moveToFirst()) {
					do {
						addrCode = cursor.getString(cursor
								.getColumnIndex("ADDR_CODE"));
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
		return addrCode;
	}
	
	/**
	 * 根据地区名称查询对应的addID
	 * @param addrName 地区名称
	 * @return 地区码
	 */
	public String getAddrName(int addrID) {		
		SQLiteDatabase db = null;
		Cursor cursor = null;
		String addrName = null;
		synchronized (DB_LOCK) {
			try {
				db = openDatabase();
				if (db == null) {
					return null;
				}
				String sql = "select ADDR_NAME from ADDR where ADDR_ID='" + addrID + "'";
				LogUtil.d("com.njhn.weetmall", "sql = " + sql);
				cursor = db.rawQuery(sql, null);
				if (cursor != null && cursor.moveToFirst()) {
					do {
						addrName = cursor.getString(cursor.getColumnIndex("ADDR_NAME"));
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
		return addrName;
	}
	
		
	/**
	 * 通过定位到的区域编码，获取parentId
	 * 
	 * @param [areaCodeStr]-[定位到的区域编码] <br/>
	 * @param [参数2]-[参数2说明] <br/>
	 */
	public int getParentId(String areaCodeStr) {
		SQLiteDatabase db = null;
		Cursor cursor = null;
		int parentId = -1;
		synchronized (DB_LOCK) {
			try {
				db = openDatabase();
				if (db == null) {
					return parentId;
				}
				String sql = "select * from ADDR where ADDR_CODE = "
						+ areaCodeStr;
				cursor = db.rawQuery(sql, null);
				if (cursor != null && cursor.moveToFirst()) {
						parentId = cursor.getInt(cursor
								.getColumnIndex("PARENT_ID"));
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
		return parentId;
	}
	
	/**
	 * 通过定位到的区域编码，获取parentId
	 * 
	 * @param [areaCodeStr]-[定位到的区域编码] <br/>
	 * @param [参数2]-[参数2说明] <br/>
	 */
	public int getAddrId(String areaCodeStr) {
		SQLiteDatabase db = null;
		Cursor cursor = null;
		int parentId = -1;
		synchronized (DB_LOCK) {
			try {
				db = openDatabase();
				if (db == null) {
					return parentId;
				}
				String sql = "select * from ADDR where ADDR_CODE = "
						+ areaCodeStr;
				cursor = db.rawQuery(sql, null);
				if (cursor != null && cursor.moveToFirst()) {
						parentId = cursor.getInt(cursor
								.getColumnIndex("ADDR_ID"));
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
		return parentId;
	}
	
	
	/**
	 * 通过定位到的区域编码，获取parentId
	 * 
	 * @param [areaCodeStr]-[定位到的区域编码] <br/>
	 * @param [参数2]-[参数2说明] <br/>
	 */
	public AddrItem getAddrItem(int addrID) {
		SQLiteDatabase db = null;
		Cursor cursor = null;
		AddrItem addrItem = null;
		synchronized (DB_LOCK) {
			try {
				db = openDatabase();
				if (db == null) {
					return addrItem;
				}
				String sql = "select * from ADDR where ADDR_ID = "
						+ addrID;
				cursor = db.rawQuery(sql, null);
				if (cursor != null && cursor.moveToFirst()) {
					addrItem = new AddrItem(cursor.getInt(cursor
							.getColumnIndex("ADDR_ID")),
							cursor.getInt(cursor
									.getColumnIndex("ADDR_CODE")),
							cursor.getString(cursor
									.getColumnIndex("ADDR_NAME")),
							cursor.getInt(cursor
									.getColumnIndex("PARENT_ID")));
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
		return addrItem;
	}
}