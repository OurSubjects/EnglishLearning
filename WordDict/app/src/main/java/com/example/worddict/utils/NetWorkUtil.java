package com.example.worddict.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import com.example.worddict.application.MyApplication;

public class NetWorkUtil {

    /**
     * 判断是否有可用网络
     *
     * @return true, 有网络，false,无网络
     */
    public static boolean hasNetwork() {
        //判断是否有网络
        ConnectivityManager connectivityManager = (ConnectivityManager) MyApplication.getContext().
                getSystemService(Context.CONNECTIVITY_SERVICE);
//        assert connectionManager != null;
//        NetworkInfo info = connectionManager.getActiveNetworkInfo();
//        if (info != null && info.isAvailable()) {
//            return true;
//        } else {
//            return false;
//        }
        //ConnectivityManager connectivityManager = (ConnectivityManager)MyApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        //新版本调用方法获取网络状态
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = connectivityManager.getAllNetworks();
            NetworkInfo networkInfo;
            for (Network mNetwork : networks) {
                networkInfo = connectivityManager.getNetworkInfo(mNetwork);
                if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                    return true;
                }
            }
        }else {
            //否则调用旧版本方法
            if (connectivityManager != null) {
                NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
                if (info != null) {
                    for (NetworkInfo anInfo : info) {
                        if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                            Log.d("Network",
                                    "NETWORKNAME: " + anInfo.getTypeName());
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
