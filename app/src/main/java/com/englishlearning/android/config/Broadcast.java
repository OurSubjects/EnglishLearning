package com.englishlearning.android.config;

/**
 * 自定义广播
 */

public class Broadcast {
    //成功完成查询单词
    public static final String WORDSERVICECOMPLETE
            = "cn.dictionary.app.dictionary.WORDSERVICECOMPLETE";

    //查询单词失败，无查询结果
    public static final String WORDSERVICEISNULL
            = "cn.dictionary.app.dictionary.WORDSERVICEISNULL";

    //APP启动
    public static final String APPSTART
            = "cn.dictionary.app.dictionary.APPSTART";

    //开启通知栏查词广播
    public static final String STARY_NOTIFICATION
            = "cn.dictionary.app.dictionary.STARY_NOTIFICATION";

    //每日一句获取成功广播
    public static final String DAILYSENTENCESERVICE_COMPLETE
            = "cn.dictionary.app.DailySentenceService.COMPLETE";
}
