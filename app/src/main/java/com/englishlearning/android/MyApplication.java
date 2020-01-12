package com.englishlearning.android;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

import com.englishlearning.android.dataBase.MyDatabaseHelper;

/**
 * 管理程序内一些全局的状态信息
 */

public class MyApplication extends Application {

    private static Context context;
    private final MyDatabaseHelper dbHelper = new MyDatabaseHelper(this, "English.db", null, 1);//不可变的对象
    private SQLiteDatabase db; //SQLiteDatabase对象

    private SharedPreferences sharedPreferences;
    private String bookTitle;
    private String bookId;
    private int bookImage;
    private int wordNum;
    private boolean flag;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
        setDb(dbHelper.getWritableDatabase()); //用于创建和升级数据库
        bookTitle=sharedPreferences.getString("bookTitle","");
        bookId=sharedPreferences.getString("bookId","");
        bookImage=sharedPreferences.getInt("bookImage",0);
        wordNum=sharedPreferences.getInt("wordNum",0);
        flag=false;
    }

    public static Context getContext() {
        return context;
    }
    public SQLiteDatabase getDb() {
        return db;
    }

    public void setDb(SQLiteDatabase db) {
        this.db = db;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public int getBookImage() {
        return bookImage;
    }

    public void setBookImage(int bookImage) {
        this.bookImage = bookImage;
    }

    public int getWordNum() {
        return wordNum;
    }

    public void setWordNum(int wordNum) {
        this.wordNum = wordNum;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }
}

