package com.englishlearning.android.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.englishlearning.android.R;
import com.englishlearning.android.dataBase.MyDatabaseHelper;
import com.englishlearning.android.models.historyClass;

import java.util.ArrayList;
import java.util.List;

public class listen_history extends Activity {

    private Button button_HistoryBackToMain;
    private ListView listview_ListenHistory;

    List<historyClass> listenHistory;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listen_history);

        listenHistory = new ArrayList<historyClass>();


        final Button button_HistoryBackToMain = (Button) findViewById(R.id.button_HistoryBackToMain);

        button_HistoryBackToMain.setOnClickListener(new OnClickListenerHistoryBackToMain());

        MyDatabaseHelper getData = new MyDatabaseHelper(this, "listenBook.db", null, 1);
        SQLiteDatabase db = getData.getWritableDatabase();
        Cursor cursor = db.query("listenBook",null,null,null,null,null,null);
        while (cursor.moveToNext()){
            String _id = cursor.getString(cursor.getColumnIndex("id"));
            String textInput = cursor.getString(cursor.getColumnIndex("textInput"));

            historyClass h = new historyClass(_id, textInput);
            listenHistory.add(h);
        }

        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
        for(historyClass h:listenHistory){
            TextView textView = new TextView(this);
            textView.setText(h.toString());
            textView.setTextSize(18);
            textView.setMaxLines(2);
            ll.addView(textView);
        }






    }
//点击返回按钮返回听力模块主页面
    private class OnClickListenerHistoryBackToMain implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            Intent intent = new Intent(listen_history.this, ListenMainActivity.class);
            startActivity(intent);
            listen_history.this.finish();
        }
    }


}
