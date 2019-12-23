package com.example.worddict.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.worddict.config.Broadcast;
import com.example.worddict.config.HttpPath;
import com.example.worddict.https.HttpCallbackListener;
import com.example.worddict.model.Eword;
import com.example.worddict.sql.WordDAO;
import com.example.worddict.utils.HttpUtil;
import com.example.worddict.utils.ParserUtil;


public class WordService extends Service {

    public static Eword sWord;
    private Context context = this;
    public WordService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("input");
        //发起查词网络请求并根据结果判断处理
        HttpUtil.getHttp().sendHttpRequestForWord(HttpPath.PATH_WORD, input, mhttpCallbackListenForWord);

        return super.onStartCommand(intent, flags, startId);
    }

    private HttpCallbackListener mhttpCallbackListenForWord = new HttpCallbackListener() {

        @Override
        public void onFinish(String response) {
            //解析数据
            sWord = ParserUtil.ParseJSONForWord(response);
            //若有数据
            if (sWord != null) {
                Toast.makeText(context, "正在联网查词", Toast.LENGTH_SHORT).show();
                //保存数据
                Log.d("WordDao addWords",sWord.getWordSpell());
                WordDAO.getInstance(context).addWord(sWord);
                if(sWord.getExplains()==null) {
                    Toast.makeText(context, "存在单词无法查询:"+sWord.getWordSpell(), Toast.LENGTH_SHORT).show();

                }
                //进行音频解析和保存
                String uk_voiceName = "uk_" + sWord.getQuery() + ".mp3";
                String us_voiceName = "us_" + sWord.getQuery() + ".mp3";
                //英式发音
                if (sWord.getUk_speech() != null) {
                    HttpUtil.getHttp().sendHttpRequestForVoice(context,uk_voiceName, sWord.getUk_speech());
                }
                //美式发音
                if (sWord.getUs_speech() != null) {
                    HttpUtil.getHttp().sendHttpRequestForVoice(context,us_voiceName, sWord.getUs_speech());
                }
                //发送广播提示成功完成查询单词
                Intent intent = new Intent(Broadcast.WORDSERVICECOMPLETE);
                sendBroadcast(intent);
            } else {
                //提示用户无查询结果

                Intent intent = new Intent(Broadcast.WORDSERVICEISNULL);
                sendBroadcast(intent);
            }
        }

        @Override
        public void onError(int resultCode) {
            Toast.makeText(context, "获取单词错误：" + resultCode, Toast.LENGTH_SHORT).show();
        }
    };
}
