package com.englishlearning.android.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.englishlearning.android.R;
import com.englishlearning.android.dataBase.MyDatabaseHelper;

import java.io.IOException;
import java.util.Random;

public class practice extends Activity {

    private Button button_Back;
    private Button button_CheckAnswer;
    private TextView text_Question;
    private TextView text_Answer;

    private MyDatabaseHelper dbHelper;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pracitce);

        //取得各按钮控件
        final Button button_Back = (Button) findViewById(R.id.button_PracticeBackToMain);
        final Button button_CheckAnswer = (Button) findViewById(R.id.button_CheckAnswer);
        final Button button_Play = (Button) findViewById(R.id.button_Play);


        final TextView text_Question = (TextView) findViewById(R.id.text_Question);


        //为各按钮添加点击事件
        button_Back.setOnClickListener(new OnClickListenerBack());
        button_CheckAnswer.setOnClickListener(new OnClickListenerCheckAnswer());
        button_Play.setOnClickListener(new OnClickListenerPlay());


        dbHelper = new MyDatabaseHelper(this, "listenBook.db", null, 1);


        /*******挖空**********/
        Intent intentGetData = getIntent();
        String textInput = intentGetData.getStringExtra("textInput");//获取输入的文本

        String[] text = textInput.split("[ ]");
        int textLength = text.length;
        Random random = new Random();

        for(int count = 0;count < textLength/7; count++)
        {
            int randNum = random.nextInt(textLength);
            if(text[randNum] == "," || text[randNum] == "." || text[randNum] == "!" || text[randNum] == "?" || text[randNum] == ";" || text[randNum] == ":" ){}
            else text[randNum] = "***";
        }
        String textQuestion = String.join(" ",text);
        text_Question.setText(textQuestion);
        //把题目和答案存入数据库
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("textInput", textInput);
        values.put("question", textQuestion);
        db.insert("listenBook",null,values);

        /****************************/







    }


    //为Back按钮添加点击事件（点击返回主页面）
    private class OnClickListenerBack implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(practice.this, ListenMainActivity.class);
            startActivity(intent);
            practice.this.finish();

        }
    }

    //为CheckAnswer按钮添加点击事件（点击直接显示答案）
    private class OnClickListenerCheckAnswer implements View.OnClickListener{
        @Override
        public void onClick(View v){
            TextView text_Answer = (TextView) findViewById(R.id.text_Answer);
            Intent intentGetData = getIntent();
            String answer = intentGetData.getStringExtra("textInput");
            text_Answer.setText(answer);
        }
    }


    private class OnClickListenerPlay implements View.OnClickListener{
        @Override
        public void onClick(View v){
            MediaPlayer mp = new MediaPlayer();
            Intent intentGetData = getIntent();
            String query = intentGetData.getStringExtra("textInput");
            try {
                mp.setDataSource("http://dict.youdao.com/dictvoice?audio=" + query);
                mp.prepare();
                mp.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }




}




