package com.informaticsproject.ureport;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "ureportDB";

	// Login table name
	private static final String TABLE_REPORTS = "ureports";
	public static final String KEY_ID = "id";
	public static final String KEY_EMAIL = "email";
	public static final String KEY_USERNAME = "username";
	public static final String KEY_CREATED_AT = "created_at";
	public static final String KEY_UPVOTES = "upvotes";
	public static final String KEY_TYPE = "usertype";
	public static final String KEY_SUCCESS = "success";
	public static final String KEY_PASSWORD = "password";
	public static final String KEY_TEXT = "text";
	public static final String KEY_DOCURL = "docurl";
	public static final String KEY_MEDIAURL = "mediaurl";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_REPORTS_TABLE = "CREATE TABLE " + TABLE_REPORTS + "("
				+ KEY_USERNAME + " TEXT," + KEY_TEXT + " TEXT," + KEY_MEDIAURL
				+ " TEXT," + KEY_DOCURL + " TEXT," + KEY_UPVOTES + " TEXT,"
				+ KEY_CREATED_AT + " TEXT," + KEY_ID + " TEXT" + ")";
		db.execSQL(CREATE_REPORTS_TABLE);

	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPORTS);

		// Create tables again
		onCreate(db);

	}

	public void addReport(String username, String timestamp, String docurl,
			String mediaurl, String text, String upvotes, String id) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(KEY_USERNAME, username);
		values.put(KEY_CREATED_AT, timestamp);
		values.put(KEY_DOCURL, docurl);
		values.put(KEY_MEDIAURL, mediaurl);
		values.put(KEY_TEXT, text);
		values.put(KEY_UPVOTES, upvotes);
		values.put(KEY_ID,  id);
		
		db.insert(TABLE_REPORTS, null, values);
		db.close(); // Closing database connection
	}

	
	public ArrayList<Ureport> getUReports() {
		Ureport report = null;
		String selectQuery = "SELECT  * FROM " + TABLE_REPORTS;
		ArrayList<Ureport> gotten = new ArrayList<Ureport>(getUserRowCount());
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Move to first row
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			report = new Ureport();
			report.username = cursor.getString(0);
			report.text = cursor.getString(1);
			report.mediaurl = cursor.getString(2);
			report.docurl = cursor.getString(3);
			report.upvote = cursor.getString(4);
			report.datetime = cursor.getString(5);
			report.id = cursor.getString(6);
			
			gotten.add(report);
		}
		cursor.close();
		db.close();
		// return user
		return gotten;
	}

	/**
	 * Getting user login status return true if rows are there in table
	 * */
	public int getUserRowCount() {
		String countQuery = "SELECT  * FROM " + TABLE_REPORTS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int rowCount = cursor.getCount();
		db.close();
		cursor.close();

		// return row count
		return rowCount;
	}

	/**
	 * Re crate database Delete all tables and create them again
	 * */
	public void resetTables() {
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Rows
		db.delete(TABLE_REPORTS, null, null);

		db.close();
	}

}
