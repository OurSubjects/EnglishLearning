package com.englishlearning.android.dataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String CREATE_BOOK ="create table Book ("
            +"bookId text primary key,"
            +"bookImage integer,"
            +"wordNum integer,"
            +"bookTitle text)";
    public static final String CREATE_WORDCOLLECT="create table WordCollect("
            + " SPELLING text not null," +
            "UK_SPEECH text ," +
            " US_SPEECH text ," +
            " TRANSLATION text not null," +
            " UK_PHONETIC text ," +
            " US_PHONETIC text ," +
            " EXPLAINS text not null," +
            " WEBS text ," +
            " IS_OK text "+
            ");";
    public static final String CREATE_TABLE_LISTEN = "create table listenBook (" +
            "id integer primary key autoincrement, " +
            "textInput text, " +
            "question text )";
    private Context mContext;
    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    //如果数据库不存在会调用，否则不调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOK );
        db.execSQL(CREATE_WORDCOLLECT);
        db.execSQL(CREATE_TABLE_LISTEN);
        Log.d("database","创建初始表成功");
    }

    //用于升级数据库(比如增加了表,因为从数据库存在不会调用onCreate方法),只需在构造方法中传递版本大于之前版本就会调用这个方法
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
