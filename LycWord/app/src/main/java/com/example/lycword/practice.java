package com.example.lycword;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.io.IOException;

public class practice extends Activity {

    private Button button_Back;
    private Button button_CheckAnswer;
    private TextView text_Question;
    private TextView text_Answer;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pracitce);



        //取得各按钮控件
        final Button button_Back = (Button) findViewById(R.id.button_Back);
        final Button button_CheckAnswer = (Button) findViewById(R.id.button_CheckAnswer);
        final Button button_Play = (Button) findViewById(R.id.button_Play);


        final TextView text_Question = (TextView) findViewById(R.id.text_Question);
        //final TextView text_Answer = (TextView) findViewById(R.id.text_Answer);


        //为各按钮添加点击事件
        button_Back.setOnClickListener(new OnClickListenerBack());
        button_CheckAnswer.setOnClickListener(new OnClickListenerCheckAnswer());
        button_Play.setOnClickListener(new OnClickListenerPlay());


        Intent intentGetData = getIntent();
        String textInput = intentGetData.getStringExtra("textInput");
        text_Question.setText(textInput);


        //text_Question.setText("Hey! I'm the Question!!!");


    }


    //为Back按钮添加点击事件（点击返回主页面）
    private class OnClickListenerBack implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(practice.this, MainActivity.class);
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




