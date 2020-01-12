package com.englishlearning.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.englishlearning.android.models.Eword;
import com.englishlearning.android.dataBase.SqlHelper;
import com.englishlearning.android.dataBase.WordDAO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class learnMeaning extends AppCompatActivity implements View.OnClickListener{

    private View view;
    private Activity activity;
    private Eword mWord = null;//存放单词数据的实例
    private ImageButton mAddWord;//添加单词的按钮
    private MediaPlayer mUK_MP = null;//播放英式发音
    private MediaPlayer mUS_MP = null;//播放美式发音
    private Intent startIntent;//开启查词后台服务的广播
    private Context context;

    private WordDAO wDao;
    private List<Eword> mEwordList;
    private List<String> mData = new ArrayList<>();

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.MOUNT_UNMOUNT_FILESYSTEMS"
    };

    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_result);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        verifyStoragePermissions(this);
        Button button=(Button) findViewById(R.id.btn_recite_next2);
        button.setOnClickListener(this);
        Button button2=(Button) findViewById(R.id.btn_recite_next1);
        button2.setOnClickListener(this);
        Button button3=(Button) findViewById(R.id.btn_recite_next3);
        button3.setOnClickListener(this);

        //init();
        Intent intent = getIntent();
        String isCollect=intent.getStringExtra("isCollect");
        SqlHelper helper=new SqlHelper();
        if(isCollect!=null &&isCollect.equals("1")){
            helper.setTableName("WordCollect",(MyApplication) getApplication());
        }else {
            helper.setTableName(((MyApplication) getApplication()).getBookId(),(MyApplication) getApplication());
        }
        wDao = new WordDAO(context);
        mEwordList = wDao.Queryword(-1);
        mData = getData();
        System.out.println(mData.size());
        handleInput(mData.get(0),0);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
        //return item.getItemId()==R.id.action_cart?true:false;
    }

    public  void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_recite_next3:
                handleInput(mData.get(i),1);
                break;
            case R.id.btn_recite_next2:
                if(++i < mEwordList.size())
                    handleInput(mData.get(i),0);
                else
                {
                    final CustomDialog dialog = new CustomDialog(this);
                    dialog.show();
                    dialog.setHintText("恭喜，您已完成所有单词的学习！");
                    dialog.setLeftButton("返回", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.setRightButton("重新学习", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            i = 0;
                            handleInput(mData.get(i),0);
                            dialog.dismiss();
                        }
                    });
                }
                break;
            case R.id.btn_recite_next1:
                wDao.addWordCollect(mEwordList.get(i));
                Toast.makeText(this, "单词已收藏", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }


    private List<String> getData() {
        List<String> data = new ArrayList<String>();
        for (int i = 0; i < mEwordList.size(); i++) {
            data.add("" + mEwordList.get(i).getWordSpell());
        }
        Log.d("swipe", "getData() OK");
        return data;
    }
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

    public void handleInput(String input, int mode) {
        Log.d("handleInput",input);
            mWord =WordDAO.getInstance(context).queryWord(input);
        switch(mode)
        {
            case 0:
                healData();
                showData0(mWord);
                break;
            case 1:
                healData();
                showData1(mWord);
                break;
        }
        return;
    }

    public void healData(){
        findViewById(R.id.uk).setVisibility(View.GONE);
        findViewById(R.id.tv_uk_phonetic).setVisibility(View.GONE);
        findViewById(R.id.im_uk_speech).setVisibility(View.GONE);
        findViewById(R.id.layout_speech_Phonetic).setVisibility(View.GONE);
        findViewById(R.id.us).setVisibility(View.GONE);
        findViewById(R.id.tv_us_phonetic).setVisibility(View.GONE);
        findViewById(R.id.im_us_speech).setVisibility(View.GONE);
        findViewById(R.id.layout_translation).setVisibility(View.GONE);
        findViewById(R.id.layout_explains).setVisibility(View.GONE);
        findViewById(R.id.layout_web).setVisibility(View.GONE);
    }

    public void showData0(Eword word) {
        //判断添加单词按钮的状态
        //关键字
        if (word.getQuery() != null) {
            TextView query = (TextView) findViewById(R.id.tv_query);
            query.setText(word.getQuery());
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
    }

    public void showData1(Eword word) {
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
}
