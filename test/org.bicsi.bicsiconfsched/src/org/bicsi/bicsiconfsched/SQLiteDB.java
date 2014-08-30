package org.bicsi.bicsiconfsched;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.database.Cursor;

public class SQLiteDB {
	
	public static final String KEY_ID = "_id";
	 public static final String KEY_SCHEDULEDATE = "scheduleDate";
	 public static final String KEY_SESSIONNAME = "sessionName";
	 public static final String KEY_SESSIONTIME = "sessionTime";
	 

	 private static final String TAG = "DbAdapter";
	 private DatabaseHelper DBHelper;
	 private SQLiteDatabase db;

	 private static final String DATABASE_NAME = "SQLiteDB";
	 private static final String SQLITE_TABLE = "sample";
	 private static final int DATABASE_VERSION = 1;

	 private final Context context;

	 private static final String DATABASE_CREATE =
	  "create table sample (_id text primary key, scheduleDate text not null, sessionName text not null, sessionTime text not null);";

	 private static class DatabaseHelper extends SQLiteOpenHelper {

	  DatabaseHelper(Context context) {
	   super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  }


	  @Override
	  public void onCreate(SQLiteDatabase db) {
	   try {
	   				Log.w(TAG, DATABASE_CREATE);
					db.execSQL(DATABASE_CREATE);
	   			} catch (SQLException e) {
	   				e.printStackTrace();
	   			}
	  }

	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	   Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
	     + newVersion + ", which will destroy all old data");
	   db.execSQL("DROP TABLE IF EXISTS sample ");
	   onCreate(db);
	  }
	 }

	 /*public SQLiteDB(Context ctx) {
	  this.context = ctx;
	 }*/
	 
	 public SQLiteDB(Context ctx) {
			
	 		this.context = ctx;
	 		DBHelper = new DatabaseHelper(context);
	 	}

	//---open SQLite DB---
	 public SQLiteDB open() throws SQLException {
	  /*mDbHelper = new DatabaseHelper(mCtx);*/
	  db = DBHelper.getWritableDatabase();
	  return this;
	 }

	//---close SQLite DB---
	 public void close() {
	  /*if (mDbHelper != null) {
	   mDbHelper.close();
	  }*/
	  DBHelper.close();
	 }

	 //---insert data into SQLite DB---
	 	public long insert(String id, String scheduleDate, String sessionName, String sessionTime) {
		
	 		ContentValues initialValues = new ContentValues();
	 		initialValues.put(KEY_ID, id);
	 		initialValues.put(KEY_SCHEDULEDATE, scheduleDate);
	 		initialValues.put(KEY_SESSIONNAME, sessionName);
	 		initialValues.put(KEY_SESSIONTIME, sessionTime);
			
	 		return db.insert(SQLITE_TABLE, null, initialValues);
	 	}

	 //---Delete All Data from table in SQLite DB---
	 	public void deleteAll() {
		
	 		db.delete(SQLITE_TABLE, null, null);
	 	}

	 /*public Cursor fetchCountriesByName(String inputText) throws SQLException {
	  Log.w(TAG, inputText);
	  Cursor mCursor = null;
	  if (inputText == null  ||  inputText.length () == 0)  {
	   mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID,
	     KEY_CODE, KEY_NAME, KEY_CONTINENT, KEY_REGION}, 
	     null, null, null, null, null);

	  }
	  else {
	   mCursor = mDb.query(true, SQLITE_TABLE, new String[] {KEY_ROWID,
	     KEY_CODE, KEY_NAME, KEY_CONTINENT, KEY_REGION}, 
	     KEY_NAME + " like '%" + inputText + "%'", null,
	     null, null, null, null);
	  }
	  if (mCursor != null) {
	   mCursor.moveToFirst();
	  }
	  return mCursor;

	 }*/
	 
	 public Cursor fetchScheduleByDate(String inputText) throws SQLException {
	   Log.w(TAG, inputText);
	   Cursor mCursor = null;
	   if (inputText == null  ||  inputText.length () == 0)  {
	    mCursor = db.query(SQLITE_TABLE, new String[] {KEY_ID,
	      KEY_SCHEDULEDATE, KEY_SESSIONNAME, KEY_SESSIONTIME}, 
	      null, null, null, null, null);

	   }
	   else {
	    mCursor = db.query(true, SQLITE_TABLE, new String[] {KEY_ID,
	    		KEY_SCHEDULEDATE, KEY_SESSIONNAME, KEY_SESSIONTIME}, 
	    		KEY_SCHEDULEDATE + " like '%" + inputText + "%'", null,
	      null, null, null, null);
	   }
	   if (mCursor != null) {
	    mCursor.moveToFirst();
	   }
	   return mCursor;

	  }

	 public Cursor fetchAllSchedules() {

	  Cursor mCursor = db.query(SQLITE_TABLE, new String[] {KEY_ID,
			  KEY_SCHEDULEDATE, KEY_SESSIONNAME, KEY_SESSIONTIME}, 
	    null, null, null, null, null);

	  if (mCursor != null) {
	   mCursor.moveToFirst();
	  }
	  return mCursor;
	 }
	
	

}
