package com.englishlearning.android.https;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.englishlearning.android.dataBase.WordDAO;
import com.englishlearning.android.utils.SDUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.englishlearning.android.service.WordService.sWord;


/**
 * 获取音频的输入流以便存储在SQLite
 */

public class HttpWordVoiceAsyncTask extends AsyncTask<URL, Void, Void> {

    private int mresultCode;//服务器返回的状态码

    private static int flag = 0;//标记美式发音和英式发音是否都执行了

    private String voiceName = null;//音频的名字
    private Context context;
    public HttpWordVoiceAsyncTask(Context context,String name) {
        this.voiceName = name;
        this.context=context;
    }

    @Override
    protected Void doInBackground(URL... urls) {
        InputStream in = null;
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) urls[0].openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(8000);
            conn.setConnectTimeout(8000);
            mresultCode = conn.getResponseCode();
            if (mresultCode == HttpURLConnection.HTTP_OK) {
                in = conn.getInputStream();
                SDUtil.getHanderSD().saveToRecord(voiceName, in);
                if (voiceName.charAt(1) == 'k') {
                    sWord.setUk_speech(SDUtil.getHanderSD().
                            getRecordVoiceAddress(voiceName));
                    flag++;
                } else if (voiceName.charAt(1) == 's') {
                    sWord.setUs_speech(SDUtil.getHanderSD().
                            getRecordVoiceAddress(voiceName));
                    flag++;
                }
            } else {
                conn.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (mresultCode == HttpURLConnection.HTTP_OK) {
            if (flag == 2) {
                //把解析好的音频保存到数据库中
                WordDAO.getInstance(context).addWord(sWord);
            }
        } else {
            Toast.makeText(context, "获取音频失败：" + mresultCode, Toast.LENGTH_SHORT).show();
        }
    }
}
