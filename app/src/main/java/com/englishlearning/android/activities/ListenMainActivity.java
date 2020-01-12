package com.englishlearning.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.englishlearning.android.R;
import com.englishlearning.android.dataBase.MyDatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class ListenMainActivity extends AppCompatActivity {

    private Button button_Cancel;
    private Button button_Clear;

    private boolean flag = false; //设置标记，false表示正在播放

    private TextView textView;

    private static final String YOUDAO_URL = "https://openapi.youdao.com/ttsapi";
    private static final String APP_SECRET = "417c6e6303beb890";
    private static final String APP_KEY = "lycWord";

    private MyDatabaseHelper dbHelper;




    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar() !=null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_listen_main);


/***********按钮事件begin***********/

//取得各按钮组件
        final ImageView button_Cancel = (ImageView) findViewById(R.id.button_Cancel);
        final Button button_Clear = (Button) findViewById(R.id.button_Clear);
        final Button button_OK = (Button) findViewById(R.id.button_OK);
        final Button button_History = (Button) findViewById(R.id.button_History);

        button_Cancel.setOnClickListener(new OnClickListenerCancel());
        button_Clear.setOnClickListener(new OnClickListenerClear());
        button_OK.setOnClickListener(new OnClickListenerOK());
        button_History.setOnClickListener(new OnClickListenerHistory());


        //final ListView listview_History = (ListView) findViewById(R.id.listview_History);


        dbHelper = new MyDatabaseHelper(this, "listenBook.db", null, 1);



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

//为cancel按钮设置点击事件,点击返回上一级页面
        private class OnClickListenerCancel implements View.OnClickListener
        {
            @Override
            public void onClick(View v){
                finish();
            }
        }


//为OK按钮设置点击事件（点击跳转到练习模式，同时获取editText中输入的文本）
        private class OnClickListenerOK implements View.OnClickListener
        {
            @Override
            public void onClick(View v)
            {
                EditText edit_Txtinput = findViewById(R.id.edit_TXTinput);
                Intent intentPutData = new Intent(ListenMainActivity.this, practice.class);
                intentPutData.putExtra("textInput", edit_Txtinput.getText().toString().trim());
                dbHelper.getWritableDatabase();
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

        //为History按钮设置点击事件（点击显示历史记录）
        private class OnClickListenerHistory implements View.OnClickListener
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(ListenMainActivity.this, listen_history.class);
                startActivity(intent);
                ListenMainActivity.this.finish();
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
