package com.englishlearning.android.config;

/**
 * 建表语句和相关的表名
 */

public class Table {

    //数据库名
    public static final String DB = "Dictionary.db";

    //单词本表名
    public static final String WORDBOOK = "wordbook";

    //搜索记录表名
    public static final String SEARCHRECORD = "searchRecord";

    //每日一句表名
    public static final String DAILYSENTENCE = "dailySentence";

    //建立WordBook表
    public static final String CREATE_WORDBOOK = "create table wordbook ("
            + "query text,"
            + "translation text,"
            + "uk_speech text,"
            + "uk_phonetic text,"
            + "us_speech text,"
            + "us_phonetic text,"
            + "explains text,"
            + "webs text,"
            + "order_number integer primary key autoincrement)";

    //建立dailySentence表
    public static final String CREATE_DAILYSENTENCE = "create table dailySentence ("
            + "dateline text primary key,"
            + "voicePath text,"
            + "englishContent text,"
            + "chineseContent text,"
            + "picturePath text)";

    //建立searchRecord表
    public static final String CREATE_SEARCHRECORD = "create table searchRecord ("
            + "query text primary key,"
            + "translation text,"
            + "uk_speech text,"
            + "uk_phonetic text,"
            + "us_speech text,"
            + "us_phonetic text,"
            + "explains text,"
            + "webs text)";
}
