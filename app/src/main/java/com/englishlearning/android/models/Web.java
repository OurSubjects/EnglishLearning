package com.englishlearning.android.models;

import java.util.List;

/**
 * 网络释义
 */

public class Web {

    private String key;//关键字
    private List<String> values;//释义

    public Web() {
        this.key = null;
        this.values = null;
    }

    public List<String> getValues() {
        return values;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return key + ":" + connectValues() + '\n';
    }

    /**
     * 对values中的释义进行拼接
     */
    public String connectValues() {
        StringBuffer sb = new StringBuffer();
        //网络释义值不为空且至少有一个值时进行拼接
        if (values != null && values.size() > 0) {
            for (int i = 0; i < values.size(); i++) {
                sb.append(values.get(i)).append(",");
            }
            //把最后的逗号去掉后再转成字符串
            return sb.deleteCharAt(sb.length() - 1).toString();
        }
        return "无";
    }
}
