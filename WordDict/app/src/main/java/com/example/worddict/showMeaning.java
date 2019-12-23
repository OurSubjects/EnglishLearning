package com.example.worddict;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.worddict.model.Eword;
import com.example.worddict.service.WordService;
import com.example.worddict.sql.WordDAO;
import com.example.worddict.utils.NetWorkUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static com.example.worddict.service.WordService.sWord;

public class showMeaning extends AppCompatActivity {

    private View view;
    private Activity activity;
    private Eword mWord = null;//存放单词数据的实例
    private ImageButton mAddWord;//添加单词的按钮
    private MediaPlayer mUK_MP = null;//播放英式发音
    private MediaPlayer mUS_MP = null;//播放美式发音
    private WordServiceCompleteReceiver mWordServiceCompleteReceiver;//查询单词完毕的广播
    private WordServiceisNullReceiver mWordServiceisNullReceiver;//查询不到输入的词汇
    private Intent startIntent;//开启查词后台服务的广播
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_result);
        //view = inflater.inflate(R.layout.fragment_result, container, false);
       Intent intent=getIntent();
       String data=intent.getStringExtra("word");
       System.out.print(data);
        Log.d("showMeaning",data);
        handleInput(data);

    }
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.fragment_result, container, false);
//        initView();
//        initEvent();
//        return view;
//    }
    /**
     * 初始化UI控件
     */
    private void initView() {
        mAddWord = (ImageButton) view.findViewById(R.id.im_addWord);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        //mAddWord.setOnClickListener(this);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }
    public void handleInput(String input) {
        Log.d("handleInput",input);
        //input = deleteContinuousSpace(input);
        //查询记录中是否存在此单词
        boolean wr = WordDAO.getInstance(context).IsHaveWord(input);
        if (wr == false) {
            //有时候服务器返回查询值的与输入值不相等，例如小写变成大写，这里进行判断处理
            wr = WordDAO.getInstance(context).IsHaveWord(input.toUpperCase());
            if (wr == false) {
                //有时候服务器返回查询值的与输入值不相等，例如小写变成大写，这里进行判断处理
                wr = WordDAO.getInstance(context).IsHaveWord(input.toLowerCase());
            }
        }
        if (wr != false) {
            //本地有记录，直接显示
            mWord =WordDAO.getInstance(context).queryWord(input);
            showData(mWord);
            //赋值给mWord，以便用户对它进行加入生词本和从生词本中删除等操作

            return;
        }
        //查询单词本中是否存在此单词
//        Words wwb = WordBookDao.getInstance().queryWord(input);
//        if (wwb == null) {
//            wwb = SearchRecordDao.getInstance().queryWord(input.toUpperCase());
//            if (wwb == null) {
//                wwb = SearchRecordDao.getInstance().queryWord(input.toLowerCase());
//            }
//        }
//        if (wwb != null) {
//            //单词本中存在，直接显示
//            showData(wwb);
//            //赋值给mWord，以便用户对它进行加入生词本和从生词本中删除等操作
//            mWord = wwb;
//            WordUtil.saveWordToRecord(wwb);
//            return;
//        }
        //单词本和搜索记录中都不存在，则进行联网查词
        if (NetWorkUtil.hasNetwork()) {
            //对用户输入的数据进行转码
            try {
                input = URLEncoder.encode(input, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            startIntent = new Intent(activity, WordService.class);
            startIntent.putExtra("input", input);
            activity.startService(startIntent);
        } else {
            Toast.makeText(activity, "网络不可用!", Toast.LENGTH_SHORT).show();
        }
    }

    public void showData(Eword word) {
        //判断添加单词按钮的状态

        //关键字
        if (word.getQuery() != null) {
            TextView query = (TextView) findViewById(R.id.tv_query);
            query.setText(word.getQuery());
        }
        //有道翻译
        if (word.getTranslation() != null) {
            findViewById(R.id.layout_translation).setVisibility(View.VISIBLE);
            TextView translation = (TextView) findViewById(R.id.tv_translation);
            translation.setText(word.getTranslation());
        } else {
            findViewById(R.id.layout_translation).setVisibility(View.GONE);
        }
        //英式音标
        if (word.getUk_phonetic() != null) {
            findViewById(R.id.layout_speech_Phonetic).setVisibility(View.VISIBLE);
            findViewById(R.id.uk).setVisibility(View.VISIBLE);
            TextView uk_phonetic = (TextView) findViewById(R.id.tv_uk_phonetic);
            uk_phonetic.setVisibility(View.VISIBLE);
            uk_phonetic.setText(word.getUk_phonetic());
        } else {
            findViewById(R.id.uk).setVisibility(View.GONE);
            findViewById(R.id.tv_uk_phonetic).setVisibility(View.GONE);
        }
        //英式发音
        if (word.getUk_speech() != null) {
            findViewById(R.id.layout_speech_Phonetic).setVisibility(View.VISIBLE);
            final ImageButton uk_speech = (ImageButton) findViewById(R.id.im_uk_speech);
            uk_speech.setVisibility(View.VISIBLE);
            mUK_MP = new MediaPlayer();
            try {
                mUK_MP.setDataSource(word.getUk_speech());
                mUK_MP.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            uk_speech.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mUK_MP.start();
                }
            });
            uk_speech.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            uk_speech.setBackgroundResource(R.drawable.im_voice_pressed);
                            break;
                        case MotionEvent.ACTION_UP:
                            uk_speech.setBackgroundResource(R.drawable.im_voice_normal);
                            break;
                    }
                    return false;
                }
            });
        } else {
            findViewById(R.id.im_uk_speech).setVisibility(View.GONE);
            findViewById(R.id.layout_speech_Phonetic).setVisibility(View.GONE);
        }
        //美式音标
        if (word.getUs_phonetic() != null) {
            findViewById(R.id.layout_speech_Phonetic).setVisibility(View.VISIBLE);
           findViewById(R.id.us).setVisibility(View.VISIBLE);
            TextView us_phonetic = (TextView) findViewById(R.id.tv_us_phonetic);
            us_phonetic.setVisibility(View.VISIBLE);
            us_phonetic.setText(word.getUs_phonetic());
        } else {
            findViewById(R.id.us).setVisibility(View.GONE);
            findViewById(R.id.tv_us_phonetic).setVisibility(View.GONE);
        }
        //美式发音
        if ((word.getUs_speech() != null) && (word.getUs_phonetic() != null)) {
            findViewById(R.id.layout_speech_Phonetic).setVisibility(View.VISIBLE);
            final ImageButton us_speech = (ImageButton) findViewById(R.id.im_us_speech);
            us_speech.setVisibility(View.VISIBLE);
            mUS_MP = new MediaPlayer();
            try {
                mUS_MP.setDataSource(word.getUs_speech());
                mUS_MP.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            us_speech.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mUS_MP.start();
                }
            });
            us_speech.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            us_speech.setBackgroundResource(R.drawable.im_voice_pressed);
                            break;
                        case MotionEvent.ACTION_UP:
                            us_speech.setBackgroundResource(R.drawable.im_voice_normal);
                            break;
                    }
                    return false;
                }
            });
        } else {
            findViewById(R.id.im_us_speech).setVisibility(View.GONE);
        }
        //基本释义
        if (word.getExplainsAfterDeal() != null) {
            findViewById(R.id.layout_explains).setVisibility(View.VISIBLE);
            TextView explains = (TextView) findViewById(R.id.tv_explains);
            //传入拼接处理后的基本释义
            explains.setText(word.getExplainsAfterDeal());
        } else {
            findViewById(R.id.layout_explains).setVisibility(View.GONE);
        }
        //网络释义
        if (word.getWebsAfterDeal() != null) {
            findViewById(R.id.layout_web).setVisibility(View.VISIBLE);
            TextView webs = (TextView) findViewById(R.id.tv_web);
            //传入拼接处理后的网络释义
            webs.setText(word.getWebsAfterDeal());
        } else {
            findViewById(R.id.layout_web).setVisibility(View.GONE);
        }

    }
    /**
     * 查询单词完毕的广播
     */
    class WordServiceCompleteReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            activity.stopService(startIntent);
            mWord = sWord;
            showData(mWord);
        }
    }

    /**
     * 查询不到输入的词汇的广播
     */
    class WordServiceisNullReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            activity.stopService(startIntent);
            TextView tv = (TextView) view.findViewById(R.id.tv_query);
            tv.setText("找不到该词汇！");
        }
    }

}
