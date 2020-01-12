package com.englishlearning.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.englishlearning.android.dataBase.WordDAO;
import com.englishlearning.android.wordfun.html2txt;
import com.englishlearning.android.wordfun.txt2word;

import java.io.IOException;
import java.util.ArrayList;

public class TXTActivity extends Activity {
    private static String TAG=TXTActivity.class.getSimpleName();
    private Button mbutton_add,mbutton_del_add,mbutton_html_add;
    private EditText mEditText;
    private TextView mTextView_test;

    private Handler uiHandler;
    private Menu mMenu;
    private Context context = this;
    private  WordDAO wDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_txt);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,      // 注意顺序
                R.layout.title);
        wDao = new WordDAO(context);
        mTextView_test = (TextView) findViewById(R.id.textView_test);

        //uiHandler在主线程中创建，所以自动绑定主线程
        uiHandler = new Handler();

        mEditText = (EditText) findViewById(R.id.editText);
        mbutton_add = (Button) findViewById(R.id.button_add);
        mbutton_del_add = (Button) findViewById(R.id.button_del_add);
        //mbutton_html_add = (Button) findViewById(R.id.button_html_add);

        mbutton_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt2word mtxtfun = null;
                try {
                    mtxtfun = new txt2word(mEditText.getText().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ArrayList<String> word = new ArrayList<>();
                for (int i = 0; i < mtxtfun.list.size(); i++) {
                    // System.out.println(mtxtfun.list.get(i).getKey() + ": " + mtxtfun.list.get(i).getValue());
                    //wDao.Insertword(mtxtfun.list.get(i).getKey());
                    word.add(mtxtfun.list.get(i).getKey());
                }

                Intent intent = new Intent();
                intent.setClass(TXTActivity.this, selectWord.class);    //参数一为当前Package的context，t当前Activity的context就是this，其他Package可能用到createPackageContex()参数二为你要打开的Activity的类名
                intent.putStringArrayListExtra("word", word);
                startActivityForResult(intent, 1);
                //finish();


//                               Toast.makeText(
//                                       getApplicationContext(),
//                                       R.string.txt_add_toast1+ mtxtfun.list.size()+R.string.txt_add_toast2,
//                                       Toast.LENGTH_SHORT).show();
            }
        });

        mbutton_del_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wDao.DeletetableTMP();
                Toast.makeText(getApplicationContext(), R.string.txt_del_add_toast, Toast.LENGTH_SHORT).show();

                txt2word mtxtfun = null;
                try {
                    mtxtfun = new txt2word(mEditText.getText().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < mtxtfun.list.size(); i++) {
                    // System.out.println(mtxtfun.list.get(i).getKey() + ": " + mtxtfun.list.get(i).getValue());
                    wDao.Insertword(mtxtfun.list.get(i).getKey());
                }

                Toast.makeText(getApplicationContext(), R.string.txt_add_toast1 + mtxtfun.list.size() + R.string.txt_add_toast2, Toast.LENGTH_SHORT).show();

            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add, menu);
        //getMenuInflater().inflate(R.menu.main, menu);
//        mMenu = menu;
//        mMenu.findItem(R.id.action_cart2).setVisible(false);
//        mMenu.findItem(R.id.action_cart3).setVisible(false);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.action_cart://监听菜单按钮
                finish();
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed()");
        finish();
        //super.onBackPressed();
    }
//    @Override protected void onActivityResult(int requestCode,int resultCode,Intent data){
//        Log.d("onActivityResult","hi");
//        switch (requestCode){
//            case 1:
//                if(resultCode==RESULT_OK){
//                    ArrayList<String> res=data.getStringArrayListExtra("word");
//                    for (int i = 0; i < res.size(); i++) {
//                        // System.out.println(mtxtfun.list.get(i).getKey() + ": " + mtxtfun.list.get(i).getValue());
//                        //wDao.Insertword(mtxtfun.list.get(i).getKey());
//                        wDao.Insertword(res.get(i));
//                        Log.d("onActivityResult",String.valueOf(res.size()));
//                    }
//                }
//                break;
//            default:
//        }
//    }

//    @Override
//    protected void onStop(){
//        Log.d(TAG, "onStop()");
//        super.onStop();
//    }


    class DownloadThread extends Thread {
        @Override
        public void run() {
            try{
                //ref: http://blog.csdn.net/iispring/article/details/47115879
                Log.d(TAG, "run: DownloadThread id"+ Thread.currentThread().getId());


                String url=mEditText.getText().toString();
                Log.d(TAG, "onClick: httml url"+url);

                final String html_txt= html2txt.getStringFromHtml(url);
                final txt2word mtxtfun = new txt2word(html_txt);
                final String liststring=txt2word.list2string();


                Log.d(TAG, "run: "+"ok");

                //update UI
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        wDao.DeletetableTMP();
                        System.out.println("Runnable thread id " + Thread.currentThread().getId());
                        TXTActivity.this.mTextView_test.setText(liststring);
                        for (int i = 0; i < mtxtfun.list.size(); i++) {
                            // System.out.println(mtxtfun.list.get(i).getKey() + ": " + mtxtfun.list.get(i).getValue());
                            wDao.Insertword(mtxtfun.list.get(i).getKey());
                        }
                    }
                };
                uiHandler.post(runnable);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
