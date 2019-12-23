package com.example.worddict.https;


/**
 * 对获取单词和每日一句的网络请求的失败或成功进行相应的逻辑处理
 */

public interface HttpCallbackListener {

    void onFinish(String response);

    void onError(int resultCode);

}
