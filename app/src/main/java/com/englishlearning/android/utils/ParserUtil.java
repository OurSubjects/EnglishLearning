package com.englishlearning.android.utils;


import com.englishlearning.android.models.Eword;
import com.englishlearning.android.models.Web;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 解析数据
 */
public class ParserUtil {

    public static Eword ParseJSONForWord(String jsonData) {
        JSONObject jo;//把服务器返回的全部数据装入jo中
        Eword words = null;
        try {
            jo = new JSONObject(jsonData);
            //根据服务器的errorCode判断结果是否正常
            if (jo.getInt("errorCode") == 0) {
                //结果正常，进行解析
                words = new Eword();
                //关键字
                if (jo.has("query")) {
                    words.setQuery(jo.getString("query"));
                }
                //翻译
                if (jo.has("translation")) {
                    //先去除没用的部分
                    String tra = jo.getString("translation");
                    String s = tra.substring(2, tra.length() - 2);
                    //如果没有基本释义和网络释义，而且关键字和有道翻译相同，则为无效查询
                    if (!jo.has("basic") && !jo.has("web")) {
                        if (words.getQuery().equals(s)) {
                            return null;
                        }
                    }
                    words.setTranslation(s);

                }
                //基本词典
                if (jo.has("basic")) {
                    //创建基本词典的JSONObject对象
                    JSONObject joBasic = jo.getJSONObject("basic");
                    //英式英标
                    if (joBasic.has("uk-phonetic")) {
                        words.setUk_phonetic(joBasic.getString("uk-phonetic"));
                    }
                    //英式发音
                    if (joBasic.has("uk-speech")) {
                        words.setUk_speech(joBasic.getString("uk-speech"));
                    }
                    //美式英标
                    if (joBasic.has("us-phonetic")) {
                        words.setUs_phonetic(joBasic.getString("us-phonetic"));
                    }
                    //美式发音
                    if (joBasic.has("us-speech")) {
                        words.setUs_speech(joBasic.getString("us-speech"));
                    }
                    //基本释义
                    if (joBasic.has("explains")) {
                        JSONArray ja = joBasic.getJSONArray("explains");
                        words.setExplains(new ArrayList<String>());
                        for (int i = 0; i < ja.length(); i++) {
                            words.getExplains().add(ja.getString(i));
                        }
                        //对基本释义进行拼接处理
                        String s = words.connectExplains(words.getExplains());
                        words.setExplainsAfterDeal(s);
                    }
                }
                //网络释义
                if (jo.has("web")) {
                    //创建网络释义的JSONArray数组
                    JSONArray jaWeb = jo.getJSONArray("web");
                    JSONArray jaWebValues;
                    words.setWebs(new ArrayList<Web>());
                    //获取网络释义中的每一个web对象的数据
                    for (int i = 0; i < jaWeb.length(); i++) {
                        JSONObject joWeb = jaWeb.getJSONObject(i);
                        Web web = new Web();
                        //获取key
                        web.setKey(joWeb.getString("key"));
                        web.setValues(new ArrayList<String>());
                        jaWebValues = joWeb.getJSONArray("value");
                        //遍历values
                        for (int j = 0; j < jaWebValues.length(); j++) {
                            web.getValues().add(jaWebValues.getString(j));
                        }
                        //最后把web对象传到words对象的webs集合中
                        words.getWebs().add(web);
                    }
                    //对网络释义进行拼接处理
                    String s = words.connectWebs(words.getWebs());
                    words.setWebsAfterDeal(s);
                }
            } else {
                //结果不正常，提示用户
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return words;
    }


}
