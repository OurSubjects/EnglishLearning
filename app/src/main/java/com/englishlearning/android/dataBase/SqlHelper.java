package com.englishlearning.android.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.englishlearning.android.MyApplication;

public class SqlHelper {

	public static String WORDLIST_TABLE_TMPWORD = "TmpWord";
	public static String WORDLIST_TABLE_MYWORD = "MyWord";
	public static MyApplication app;


	public void setTableName(String tableName,MyApplication app){
		WORDLIST_TABLE_TMPWORD=tableName;
		this.app=app;
	}

	public void Insert(Context context, String table, ContentValues values) {
		SQLiteDatabase db = app.getDb();
		try {
			db.insert(table, null, values);
			Log.i("wordroid=", "Insert");
		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	public void CreateTable(Context context, String table) {
		SQLiteDatabase db = app.getDb();
		String sql = "CREATE TABLE " + table
				+ " ( " +
				" SPELLING text not null," +
				"UK_SPEECH text ," +
				" US_SPEECH text ," +

				" TRANSLATION text not null," +
				" UK_PHONETIC text ," +
				" US_PHONETIC text ," +
				" EXPLAINS text not null," +
				" WEBS text ," +
				" IS_OK text "+
				" FLAG text "+
				");";
		try {
			db.execSQL(sql);
			Log.i("wordroid=", sql);
		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	public void Update(Context context, String table, ContentValues values, String whereClause, String[] whereArgs) {
		SQLiteDatabase db = app.getDb();
		try {
			db.update(table, values, whereClause, whereArgs);
			Log.i("wordroid=", "Update");
		} catch (Exception e) {
			e.getStackTrace();
		}
	}


	public Cursor Query(Context context, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having,
                        String orderBy) {
		SQLiteDatabase db = app.getDb();
		Cursor cursor = null;
		try {
			cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
			Log.i("wordroid=", "query");
			Log.i("countofcursor=", "" + cursor.getCount());
		} catch (Exception e) {
			e.getStackTrace();
		}
		return cursor;

	}

	public void Delete(Context context, String table, String whereClause, String[] whereArgs) {
		SQLiteDatabase db = app.getDb();
		try {
			db.delete(table, whereClause, whereArgs);
			Log.i("wordroid=", "delete");
		} catch (Exception e) {
			e.getStackTrace();
		}
	}
}
