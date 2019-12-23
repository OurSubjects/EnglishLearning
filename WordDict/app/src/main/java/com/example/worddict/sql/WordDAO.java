package com.example.worddict.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.worddict.model.Eword;

import java.util.ArrayList;
import java.util.List;

public class WordDAO {

    public Context context;
    private SqlHelper helper;
    private static WordDAO mSearchRecordDao;
    public WordDAO(Context context) {
        helper = new SqlHelper();
        this.context = context;
    }
    /**
     * 获取单实例方法
     *
     * @return SearchRecordDao
     */
    public static WordDAO getInstance(Context context) {
        //双重检验锁
        if (mSearchRecordDao == null) {
            synchronized (WordDAO.class) {
                if (mSearchRecordDao == null) {
                    mSearchRecordDao = new WordDAO(context);
                }
            }
        }
        return mSearchRecordDao;
    }
    /**
     * TODO Read words to List<Eword>
     */
    public List<Eword> Queryword(int isOK) {
        Cursor cursor = null;
        if (isOK == -1) {
            cursor = helper.Query(context, SqlHelper.WORDLIST_TABLE_TMPWORD, null, null, null, null, null, null);
        } else {
            // cursor = helper.Query(context, SqlHelper.WORDLIST_TABLE_TMPWORD, null, "topicid=" + topicid + " AND " + "isOK=" + isOK, null, null, null, null);
            cursor = helper.Query(context, SqlHelper.WORDLIST_TABLE_TMPWORD, null, "is_ok=" + isOK, null, null, null, null);
        }// query("table",new String[]{"sid"})
        List<Eword> list = new ArrayList<Eword>();
        if (cursor.moveToFirst()) {
            do {
                // here add Eword one by one
                Eword bl = new Eword();
                //bl.setId(cursor.getInt(0));
                bl.setWordSpell(cursor.getString(0));
                bl.setUk_speech(cursor.getString(1));
                bl.setUs_phonetic(cursor.getString(2));
                bl.setTranslation(cursor.getString(3));
                bl.setUk_phonetic(cursor.getString(4));
                bl.setUs_phonetic(cursor.getString(5));
                bl.setExplainsAfterDeal(cursor.getString(6));
                bl.setWebsAfterDeal(cursor.getString(7));
                bl.setIsOK(-1);
                //bl.setWordMeaning(cursor.getString(2));
                //bl.setIsOK(cursor.getInt(3));
                list.add(bl);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    /**
     * 添加一条记录
     */
    public void addWord(Eword word) {
        //若单词存在记录,则更新它
        if (IsHaveWord(word.getQuery()) != false) {
            UpdateWordisOK(word,1);
            return;
        }
        //否则添加这个单词进历史记录
        ContentValues values = new ContentValues();
        //组装数据
        values.put("uk_speech", word.getUk_speech());
        values.put("us_speech", word.getUk_speech());
        values.put("spelling", word.getQuery());
        values.put("translation", word.getTranslation());
        values.put("uk_phonetic", word.getUk_phonetic());
        values.put("us_phonetic", word.getUs_phonetic());
        values.put("explains", word.getExplainsAfterDeal());
        values.put("webs", word.getWebsAfterDeal());
        values.put("is_ok",1);
        //插入数据
        helper.Insert(context,SqlHelper.WORDLIST_TABLE_TMPWORD,values);
    }
    /**
     * TODO Update Word's  isOK
     */
    public void UpdateWordisOK(Eword word, int isOK) {

        ContentValues values = new ContentValues();
        values.put("uk_speech", word.getUk_speech());
        values.put("us_speech", word.getUs_speech());
        values.put("spelling", word.getQuery());
        values.put("translation", word.getTranslation());
        values.put("uk_phonetic", word.getUk_phonetic());
        values.put("us_phonetic", word.getUs_phonetic());
        values.put("explains", word.connectExplains(word.getExplains()));
        values.put("webs", word.connectWebs(word.getWebs()));
        values.put("is_ok", isOK);

        helper.Update(context, SqlHelper.WORDLIST_TABLE_TMPWORD, values, " SPELLING ='" + word + "'", null);
    }

    /**
     * TODO add word  have spelling and isOK is 0.
     */
    public void Insertword(String word_spell) {
        ContentValues cv = new ContentValues();
        cv.put("SPELLING", word_spell);
        cv.put("isOK", 0);
        if (IsHaveWord(word_spell) == false)
            helper.Insert(context, SqlHelper.WORDLIST_TABLE_TMPWORD, cv);
    }
//    /**
//     * 在历史记录中更新某个单词
//     */
//    public void updateWord(Eword word) {
//        SQLiteDatabase db =
//                new MyDBHelper(MyApplication.getContext(), Table.DB, null, 1).getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put("uk_speech", word.getUk_speech());
//        values.put("us_speech", word.getUs_speech());
//        values.put("query", word.getQuery());
//        values.put("translation", word.getTranslation());
//        values.put("uk_phonetic", word.getUk_phonetic());
////        values.put("us_phonetic", word.getUs_phonetic());
////        values.put("explains", word.connectExplains(word.getExplains()));
////        values.put("webs", word.connectWebs(word.getWebs()));
//        db.update(Table.SEARCHRECORD, values, "query = ?", new String[]{word.getQuery()});
////        db.close();
//    }

    /**
     * TODO delete
     */
    public void Deleteword(String word_spell) {
        if (IsHaveWord(word_spell) == true)
            helper.Delete(context, SqlHelper.WORDLIST_TABLE_TMPWORD,
                    "SPELLING ='" + word_spell + "'", null);
    }
    /**
     * TODO delete table data
     */
    public void DeletetableTMP() {
        helper.Delete(context, SqlHelper.WORDLIST_TABLE_TMPWORD,
                null, null);
    }


    /**
     * TODO count all
     */
    public String CountDo() {
        Cursor cursor = null;
        cursor = helper.Query(context, SqlHelper.WORDLIST_TABLE_TMPWORD, null, null, null, null, null, null);
        int all = cursor.getCount();

        cursor = helper.Query(context, SqlHelper.WORDLIST_TABLE_TMPWORD, null, "isOK=" + 1, null, null, null, null);
        int isok = cursor.getCount();
        cursor.close();
        return isok + "/" + all;
        // return "11/12";
    }

    /**
     * TODO word only count one time
     */
    public boolean IsHaveWord(String word_spell) {
        Cursor cursor = null;
        cursor = helper.Query(context, SqlHelper.WORDLIST_TABLE_TMPWORD, null,
                "SPELLING= '" + word_spell + "'", null, null, null, null);
        int ishave = cursor.getCount();
        cursor.close();
        return ishave == 1 ? true : false;
    }
    /**
     * 判断单词是否有记录
     *
     * @param word
     * @return 若单词存在，返回单词对象,否则，返回null
     */
    public Eword queryWord(String word) {
        Cursor cursor = null;
        cursor = helper.Query(context, SqlHelper.WORDLIST_TABLE_TMPWORD, null,
                "SPELLING= '" + word + "'", null, null, null, null);
        Eword words = new Eword();
        if (cursor.moveToFirst()) {

            do {
                words.setQuery(cursor.getString(cursor.getColumnIndex("SPELLING")));
                words.setTranslation(cursor.getString(cursor.getColumnIndex("TRANSLATION")));
                words.setUk_speech(cursor.getString(cursor.getColumnIndex("UK_SPEECH")));
                words.setUk_phonetic(cursor.getString(cursor.getColumnIndex("UK_PHONETIC")));
                words.setUs_speech(cursor.getString(cursor.getColumnIndex("US_SPEECH")));
                words.setUs_phonetic(cursor.getString(cursor.getColumnIndex("US_PHONETIC")));
                words.setExplainsAfterDeal(cursor.getString(cursor.getColumnIndex("EXPLAINS")));
                words.setWebsAfterDeal(cursor.getString(cursor.getColumnIndex("WEBS")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        //db.close();
        return words;
    }
    /**
     * TODO TmpWord and MyWord SyncMyWordtoTmpWord
     * MyWord isOK=1,则TmpWord isOK=1
     * 在程序开始前
     */
    public void dataSyncMtoT() {

        //读取TmpWord，遍历里面的单词，比较与MyWord的不同
        Cursor cursor = null;
        cursor = helper.Query(context, SqlHelper.WORDLIST_TABLE_TMPWORD, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {

                String tmp_word_spell = cursor.getString(2);


                Cursor tmpc = null;
                //其中cursor.getString(2)为单词拼写也就是TmpWord的第三列单词拼写
                tmpc = helper.Query(context, SqlHelper.WORDLIST_TABLE_MYWORD, null,
                        "SPELLING= '" + tmp_word_spell + "'", null, null, null, null);
                int ishave = tmpc.getCount();

                if (ishave == 0){
                    //向MyWord中插入数据
                    ContentValues cv = new ContentValues();
                    cv.put("SPELLING", tmp_word_spell);
                    cv.put("isOK", 0);
                    helper.Insert(context, SqlHelper.WORDLIST_TABLE_MYWORD, cv);
                }
                if (ishave == 1) {
                    tmpc.moveToFirst();
                    //更新TmpWord中对应的单词的isOK为1，如果myword里isOK为1
                    if (tmpc.getInt(3) == 1) {
                        ContentValues cvupdate = new ContentValues();
                        cvupdate.put("isOK", 1);
                        helper.Update(context, SqlHelper.WORDLIST_TABLE_TMPWORD, cvupdate, " word_spell ='" + tmp_word_spell + "'", null);
                    }

                }


                tmpc.close();


            } while (cursor.moveToNext());
        }
        cursor.close();

    }

    /**
     * TODO TmpWord and MyWord TmpWordtoSyncMyWord
     * TmpWord isOK=? MyWord isOK=?
     * <p>
     * 在程序选择同步时
     */
    public void dataSyncTtoM() {
        Cursor cursor = null;

        cursor = helper.Query(context, SqlHelper.WORDLIST_TABLE_TMPWORD, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {

                String tmp_word_spell = cursor.getString(2);
                ContentValues cvupdate = new ContentValues();
                cvupdate.put("isOK", cursor.getInt(3));
                //更新myword上的is
                helper.Update(context, SqlHelper.WORDLIST_TABLE_MYWORD, cvupdate, " word_spell ='" + tmp_word_spell + "'", null);
            } while (cursor.moveToNext());
        }
        cursor.close();

    }

}
