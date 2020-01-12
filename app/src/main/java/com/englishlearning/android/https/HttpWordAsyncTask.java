package com.englishlearning.android.https;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * 处理查词请求的异步任务
 */

public class HttpWordAsyncTask extends AsyncTask<URL, Void, String> {
    private int mresultCode;//服务器返回的状态码
    private HttpCallbackListener mhttpCallbackListenerForWord;

    public HttpWordAsyncTask(HttpCallbackListener listener) {
        mhttpCallbackListenerForWord = listener;
    }


    @Override
    protected String doInBackground(URL... urls) {
        StringBuffer request;
        String line;
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) urls[0].openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(8000);
            conn.setConnectTimeout(8000);
            mresultCode = conn.getResponseCode();
            //若请求成功，则进行数据读取
            if (mresultCode == HttpURLConnection.HTTP_OK) {
                InputStream in = conn.getInputStream();
                BufferedReader bffReader = new BufferedReader(new InputStreamReader(in));
                request = new StringBuffer();
                while ((line = bffReader.readLine()) != null) {
                    request.append(line);
                }
                return request.toString();
            }
            //若请求失败，关闭连接
            else {
                conn.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null)
                conn.disconnect();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        //成功响应请求
        if (result != null) {
            mhttpCallbackListenerForWord.onFinish(result);
        }
        //网络操作失败
        else {
            mhttpCallbackListenerForWord.onError(mresultCode);
        }
    }
}

