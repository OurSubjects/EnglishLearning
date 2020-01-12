package com.englishlearning.android.utils;


import android.content.Context;

import com.englishlearning.android.https.HttpCallbackListener;
import com.englishlearning.android.https.HttpWordAsyncTask;
import com.englishlearning.android.https.HttpWordVoiceAsyncTask;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * 获取网络请求的工具类
 */
public class HttpUtil {

    private URL murl;
    private static HttpUtil httpUtil;

    /**
     * 私有化构造器
     */
    private HttpUtil() {

    }

    /**
     * //获得HttpUtil的单实例方法
     *
     * @return HttpUtil
     */
    public static HttpUtil getHttp() {
        //双重检验锁
        if (httpUtil == null) {
            synchronized (HttpUtil.class) {
                if (null == httpUtil) {
                    httpUtil = new HttpUtil();
                }
            }
        }
        return httpUtil;
    }

    /**
     * 发送查询单词的请求到异步任务中处理耗时操作
     *
     * @param path
     * @param input
     * @param listen
     */
    public void sendHttpRequestForWord(String path, String input, HttpCallbackListener listen) {
        if (null != input) {
            StringBuilder sb = new StringBuilder();
            sb.append(path).append(input);
            try {
                murl = new URL(sb.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        new HttpWordAsyncTask(listen).execute(murl);
    }



    /**
     * 发送获取网络音频请求到一部任务中
     *
     * @param path 音频的路径
     * @param name 音频的名字
     */
    public void sendHttpRequestForVoice(Context context, String name, String path) {
        StringBuilder sb = new StringBuilder();
        sb.append(path);
        try {
            murl = new URL(sb.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        new HttpWordVoiceAsyncTask(context,name).execute(murl);
    }


}
