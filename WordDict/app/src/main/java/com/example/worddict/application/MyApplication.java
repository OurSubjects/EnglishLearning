package com.example.worddict.application;

import android.app.Application;
import android.content.Context;

/**
 * 管理程序内一些全局的状态信息
 */

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
