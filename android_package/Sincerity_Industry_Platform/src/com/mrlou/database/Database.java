package com.mrlou.database;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.mrlou.util.BaseHelper;
import com.mrlou.util.Constant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class Database extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "Sincerity.db";
	public static final String TABLE_NAME = "data";
	public static final String AD_TABLE_NAME = "ad_data";
	public static final String ID = "id";
	public static final String DATA = "data";
	private static final String CITYNAME = "cityName";
	private static final String CID = "cityid";
	
	private static final int ID_ENTER = 1; 
	private static final int ID_RQEUEST_AD = 2;
	private SQLiteDatabase wdb = null;
	private SQLiteDatabase rdb = null;

	
	public Database(Context context) {
		super(context, DATABASE_NAME, null, 1);
		wdb = getWritableDatabase();
		rdb = getReadableDatabase();
	}

	
	public void addValue(int id, byte[] value) {
		ContentValues values = new ContentValues();
		values.put(ID, id);
		values.put(DATA, value);
		wdb.insert(AD_TABLE_NAME, null, values);
	}

	
	public void delValue(int id) {
		String where = ID + " = ?";
		String str = "" + id;
		String[] whereValue = { str };
		wdb.delete(TABLE_NAME, where, whereValue);
	}


	public void setValue(int id, byte[] value) {
		String where = ID + " = ?";
		String str = "" + id;
		String[] whereValue = { str };
		ContentValues values = new ContentValues();
		values.put(ID, id);
		values.put(DATA, value);
		wdb.update(AD_TABLE_NAME, values, where, whereValue);
	}

	
	
	public byte[] getValue(int id) {
		String where = ID + " = ?";
		String str = "" + id;
		String[] whereValue = { str };
		Cursor cursor = rdb.query(AD_TABLE_NAME, null, where, whereValue, null,
				null, null);
		cursor.moveToFirst();
		byte[] data = null;
		if (cursor.getCount() > 0) {
			data = cursor.getBlob(cursor.getColumnIndex(DATA));
		}
		cursor.close();
		cursor = null;
		return data;
	}

	public boolean isContain(int id) {
		String where = ID + " = ?";
		String str = "" + id;
		String[] whereValue = { str };
		Cursor cursor = rdb.query(AD_TABLE_NAME, null, where, whereValue, null,
				null, null);
		cursor.moveToFirst(); 
		boolean contain = false;
		if (cursor.getCount() > 0) {
			contain = true;
		}
		cursor.close();
		cursor = null;
		return contain;
	}

	
	public void addTable(String table, ContentValues values) {
		wdb.insert(table, null, values);
	}

	
	public void deleteTable(String table, String Column, String value) {
		String where = Column + " = ?";
		String[] whereValue = { value };
		wdb.delete(table, where, whereValue);
	}

	
	public void updataTable(String table, String Column, String value,
			ContentValues values) {
		String where = Column + " = ?";
		String[] whereValue = { value };
		wdb.update(table, values, where, whereValue);
	}

	
	public boolean isCheckValue(String table, String Column, String value) {
		String[] str = { value };
		Cursor cursor = rdb.query(table, null, Column + " = ?", str, null,
				null, null);
		boolean check = false;
		if (cursor.getCount() > 0) {
			check = true;
		}
		cursor.close();
		cursor = null;
		return check;
	}

	
	public Cursor selectTable(String table, String Column, String value) {
		Cursor cursor = null;
		if (Column != null || value != null) {
			String str[] = { value };
			cursor = rdb.query(table, null, Column + " = ?", str, null, null,
					null);
		} else {
			cursor = rdb.query(table, null, null, null, null, null, null);
		}
		return cursor;
	}

	
	public void createTabel(String table, String SQL) {
		wdb.execSQL(SQL);
	}

	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table if not exists " + AD_TABLE_NAME + "(" + ID
				+ " varchar primary key," + DATA + " BLOB)");
		String sql = "CREATE TABLE " + TABLE_NAME + " ( " + ID + " TEXT, "
				+ CID + " INTERGER," + CITYNAME + " TEXT);";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	
	@Override
	public void close() {
		rdb.close();
		wdb.close();
	}

	
	public void saveAdStatus(boolean isCheck) {
		byte[] data = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeBoolean(isCheck);
			data = baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			BaseHelper.safeCloseOutputStream(dos);
			BaseHelper.safeCloseOutputStream(baos);
		}
		if (isContain(ID_RQEUEST_AD)) {
			this.setValue(ID_RQEUEST_AD, data); 
		} else {
			this.addValue(ID_RQEUEST_AD, data); 
		}
	}


	public boolean loadAdStatus() {
		boolean checkStatus = false;
		if (isContain(ID_RQEUEST_AD)) {
			byte[] data;
			data = this.getValue(ID_RQEUEST_AD);
			if (data != null) {
				ByteArrayInputStream bais = new ByteArrayInputStream(data);
				DataInputStream dis = new DataInputStream(bais);
				try {
					checkStatus = dis.readBoolean();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					BaseHelper.safeCloseInputStream(dis);
					BaseHelper.safeCloseInputStream(bais);
				}
			}
		}
		return checkStatus;
	}

	
	public void saveEnterStatus(boolean isEnter) {
		byte[] data = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeBoolean(isEnter);
			data = baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			BaseHelper.safeCloseOutputStream(dos);
			BaseHelper.safeCloseOutputStream(baos);
		}
		if (isContain(ID_ENTER)) {
			this.setValue(ID_ENTER, data); // �޸ļ�¼
		} else {
			this.addValue(ID_ENTER, data); // ��Ӽ�¼
		}
	}
	
	
	public void loadEnterStatus() {
		if (isContain(ID_ENTER)) {
			byte[] data;
			data = this.getValue(ID_ENTER);
			if (data != null) {
				ByteArrayInputStream bais = new ByteArrayInputStream(data);
				DataInputStream dis = new DataInputStream(bais);
				try {
					Constant.isInitEnter = dis.readBoolean();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					BaseHelper.safeCloseInputStream(dis);
					BaseHelper.safeCloseInputStream(bais);
				}
			}
		}
	}
	
	
	public void clearDatabase() {

	}

}
