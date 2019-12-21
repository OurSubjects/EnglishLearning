package com.example.lycword;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import static java.lang.System.exit;

public class MainActivity extends AppCompatActivity {

    private Button button_Cancel;
    private Button button_Clear;

    private boolean flag = false; //设置标记，false表示正在播放

    private TextView textView;

    private static final String YOUDAO_URL = "https://openapi.youdao.com/ttsapi";
    private static final String APP_SECRET = "417c6e6303beb890";
    private static final String APP_KEY = "lycWord";




    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


/***********按钮事件begin***********/

//取得各按钮组件
        final Button button_Cancel = (Button) findViewById(R.id.button_Cancel);
        final Button button_Clear = (Button) findViewById(R.id.button_Clear);
        final Button button_OK = (Button) findViewById(R.id.button_OK);

        button_Cancel.setOnClickListener(new OnClickListenerCancel());
        button_Clear.setOnClickListener(new OnClickListenerClear());
        button_OK.setOnClickListener(new OnClickListenerOK());


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

//为cancel按钮设置点击事件
        private class OnClickListenerCancel implements View.OnClickListener
        {
            @Override
            public void onClick(View v)
            {
                exit(0);
            }
        }


//为OK按钮设置点击事件（点击跳转到练习模式，同时获取editText中输入的文本）
        private class OnClickListenerOK implements View.OnClickListener
        {
            @Override
            public void onClick(View v)
            {
                EditText edit_Txtinput = findViewById(R.id.edit_TXTinput);
                Intent intentPutData = new Intent(MainActivity.this, practice.class);
                intentPutData.putExtra("textInput", edit_Txtinput.getText().toString().trim());
                startActivity(intentPutData);
                finish();
            }
        }

        //为Clear按钮设置点击事件（点击清除输入的内容）
        private class OnClickListenerClear implements View.OnClickListener
        {
            @Override
            public void onClick(View v)
            {
                EditText edit_TXTinput = (EditText) findViewById(R.id.edit_TXTinput);
                edit_TXTinput.setText("");
            }
        }



    /***********按钮事件end***********/



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
