package com.example.worddict;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.worddict.config.Broadcast;

/**
 * 欢迎页
 */
public class WelcomeActivity extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getStoragePermissions(WelcomeActivity.this);
        //发送APP启动广播
        Intent intent = new Intent(Broadcast.APPSTART);
        sendBroadcast(intent);
    }

    /**
     * 动态申请权限
     *
     * @param activity
     */
    public void getStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            } else {
                startMainActivity();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(WelcomeActivity.this, "必须同意所有权限才能使用本程序,请手动开启权限！", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    //用户同意了权限，开启主活动
                    startMainActivity();
                } else {
                    Toast.makeText(WelcomeActivity.this, "发生未知错误", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
        }
    }


    private void startMainActivity() {
        //延迟执行一个任务
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent mainIntent = new Intent(WelcomeActivity.this, MainActivity.class);
                WelcomeActivity.this.startActivity(mainIntent);
                WelcomeActivity.this.finish();
            }
        }, 2000);
    }
}
