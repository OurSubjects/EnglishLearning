package com.example.worddict;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.worddict.sql.SqlHelper;
import com.example.worddict.sql.WordDAO;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText editText;
    private WordDAO wDao;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.MOUNT_UNMOUNT_FILESYSTEMS"

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this);
        SqlHelper helper=new SqlHelper();
//        helper.DeleteTable(this, SqlHelper.WORDLIST_TABLE_TMPWORD);
//        helper.CreateTable(this,SqlHelper.WORDLIST_TABLE_TMPWORD);
        Button button=(Button) findViewById(R.id.button);
        Button button2=(Button) findViewById(R.id.button_word);
        button.setOnClickListener(this);
        button2.setOnClickListener(this);

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
            case R.id.button:
//                String inputText=editText.getText().toString();
//                Toast.makeText(MainActivity.this,inputText,
//                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, TXTActivity.class);    //参数一为当前Package的context，t当前Activity的context就是this，其他Package可能用到createPackageContex()参数二为你要打开的Activity的类名
                startActivityForResult(intent,2);

                break;
            case R.id.button_word:
                Intent intent2 = new Intent();
                intent2.setClass(MainActivity.this, wordlist.class);    //参数一为当前Package的context，t当前Activity的context就是this，其他Package可能用到createPackageContex()参数二为你要打开的Activity的类名
                startActivityForResult(intent2,3);

            default:
                break;
        }
    }
    @Override protected void onActivityResult(int requestCode,int resultCode,Intent data){
        Log.d("onActivityResult","hi");
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    ArrayList<String> res=data.getStringArrayListExtra("word");
                    for (int i = 0; i < res.size(); i++) {
                        // System.out.println(mtxtfun.list.get(i).getKey() + ": " + mtxtfun.list.get(i).getValue());
                        //wDao.Insertword(mtxtfun.list.get(i).getKey());
                        wDao.Insertword(res.get(i));
                        Log.d("onActivityResult",String.valueOf(res.size()));
                    }
                }
                break;
            default:
        }
    }
}
