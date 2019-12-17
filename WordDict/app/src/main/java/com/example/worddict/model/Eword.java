package com.example.worddict.model;

import java.util.List;

public class Eword {
    private int word_id;
    private String SPELLING;
   // private String word_meaning;
    private int isOK;
    //private String query; //关键字
    private String translation; //有道翻译
    private String uk_speech;//英式发音地址
    private String uk_phonetic;//英式英标
    private String us_speech;//美式发音地址
    private String us_phonetic;//美式英标
    private List<String> explains;//基本释义
    private String explainsAfterDeal;//经过拼接处理的基本释义
    private List<Web> webs;//网络释义
    private String websAfterDeal;//经过拼接处理的网络释义
    //private int order_number;//单词在单词本中的默认序号

    public Eword() {
        super();
    }

    public Eword(int word_id, String word_spell, String word_meaning, int isOK) {
        super();
        this.word_id = word_id;
        this.SPELLING = word_spell;
      //  this.word_meaning = word_meaning;
        this.isOK = isOK;
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
        return SPELLING;
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
        return word_id;
    }

    public void setQuery(String query) {
        this.SPELLING= query;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public void  setUk_speech(String uk_speech) {
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
        this.word_id = order_number;
    }

    public void setExplainsAfterDeal(String explainsAfterDeal) {
        this.explainsAfterDeal = explainsAfterDeal;
    }

    public void setWebsAfterDeal(String websAfterDeal) {
        this.websAfterDeal = websAfterDeal;
    }

    public int getId() {
        return word_id;
    }

    public void setId(int wid) {
        this.word_id = wid;
    }


    public String getWordSpell() {
        return SPELLING;
    }

    public void setWordSpell(String wspell) {
        this.SPELLING = wspell;
    }
//    public String getWordMeaning() {
//        return word_meaning;
//    }

//    public void setWordMeaning(String wmeaning) {
//        this.word_meaning = wmeaning;
//    }
    public int getIsOK() {
        return isOK;
    }

    public void setIsOK(int isok) {
        this.isOK = isok;
    }
}
