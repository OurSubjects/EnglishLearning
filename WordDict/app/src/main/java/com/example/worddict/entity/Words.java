package com.example.worddict.entity;


import java.io.Serializable;
import java.util.List;

/**
 * 单词实体类
 */
public class Words implements Serializable {
    private String query; //关键字
    private String translation; //有道翻译
    private String uk_speech;//英式发音地址
    private String uk_phonetic;//英式英标
    private String us_speech;//美式发音地址
    private String us_phonetic;//美式英标
    private List<String> explains;//基本释义
    private String explainsAfterDeal;//经过拼接处理的基本释义
    private List<Web> webs;//网络释义
    private String websAfterDeal;//经过拼接处理的网络释义
    private int order_number;//单词在单词本中的默认序号

    public Words() {

        this.query = null;
        this.translation = null;
        this.uk_speech = null;
        this.uk_phonetic = null;
        this.us_speech = null;
        this.us_phonetic = null;
        this.explains = null;
        this.webs = null;
        this.websAfterDeal = null;
        this.explainsAfterDeal = null;

    }


    /**
     * 对网络释义进行拼接并
     * 返回拼接后的字符串
     *
     * @param webs
     * @return
     */
    public String connectWebs(List<Web> webs) {
        StringBuffer sb = new StringBuffer();
        //基本释义值不为空且至少有一个值时进行拼接
        if (webs != null && webs.size() > 0) {
            for (int i = 0; i < webs.size(); i++) {
                sb.append(webs.get(i).toString());
            }
            //把最后的换行符去掉后再转成字符串
            return sb.toString();
        }
        return null;
    }

    /**
     * 对基本释义explains进行拼接
     * 返回拼接后的字符串
     *
     * @param explains
     * @return String
     */
    public String connectExplains(List<String> explains) {
        StringBuffer sb = new StringBuffer();
        //基本释义值不为空且至少有一个值时进行拼接
        if (explains != null && explains.size() > 0) {
            for (int i = 0; i < explains.size(); i++) {
                sb.append(explains.get(i)).append('\n');
            }
            //把最后的换行符去掉后再转成字符串
            return sb.deleteCharAt(sb.length() - 1).toString();
        }
        return null;
    }

    public String getQuery() {
        return query;
    }

    public String getTranslation() {
        return translation;
    }

    public String getUk_speech() {
        return uk_speech;
    }

    public String getUk_phonetic() {
        return uk_phonetic;
    }

    public String getUs_speech() {
        return us_speech;
    }

    public String getUs_phonetic() {
        return us_phonetic;
    }

    public List<String> getExplains() {
        return explains;
    }

    public List<Web> getWebs() {
        return webs;
    }

    public String getExplainsAfterDeal() {
        return explainsAfterDeal;
    }

    public String getWebsAfterDeal() {
        return websAfterDeal;
    }

    public int getOrder_number() {
        return order_number;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public void setUk_speech(String uk_speech) {
        this.uk_speech = uk_speech;
    }

    public void setUk_phonetic(String uk_phonetic) {
        this.uk_phonetic = uk_phonetic;
    }

    public void setUs_speech(String us_speech) {
        this.us_speech = us_speech;
    }

    public void setUs_phonetic(String us_phonetic) {
        this.us_phonetic = us_phonetic;
    }

    public void setExplains(List<String> explains) {
        this.explains = explains;
    }

    public void setWebs(List<Web> webs) {
        this.webs = webs;
    }

    public void setOrder_number(int order_number) {
        this.order_number = order_number;
    }

    public void setExplainsAfterDeal(String explainsAfterDeal) {
        this.explainsAfterDeal = explainsAfterDeal;
    }

    public void setWebsAfterDeal(String websAfterDeal) {
        this.websAfterDeal = websAfterDeal;
    }
}
