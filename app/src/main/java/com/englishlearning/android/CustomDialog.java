package com.englishlearning.android;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by admin on 2018/7/26.
 */

public class CustomDialog extends Dialog {

    /**
     * 提示
     */
    protected TextView hintTv;

    /**
     * 左边按钮
     */
    protected TextView doubleLeftBtn;

    /**
     * 右边按钮
     */
    protected TextView doubleRightBtn;

    public CustomDialog(Context context) {
        super(context, R.style.CustomDialogStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(false);  // 是否可以撤销
        setContentView(R.layout.dialog_custom);
        hintTv = (TextView) findViewById(R.id.tv_content);
        doubleLeftBtn = (TextView) findViewById(R.id.tv_cancle);
        doubleRightBtn = (TextView) findViewById(R.id.tv_choose_job);
        setCanceledOnTouchOutside(true);//点击弹框外的区域关闭
    }

    /**
     * 设置右键文字和点击事件
     *
     * @param rightStr      文字
     * @param clickListener 点击事件
     */
    public void setRightButton(String rightStr, View.OnClickListener clickListener) {
        doubleRightBtn.setOnClickListener(clickListener);
        doubleRightBtn.setText(rightStr);
    }

    /**
     * 设置左键文字和点击事件
     *
     * @param leftStr       文字
     * @param clickListener 点击事件
     */
    public void setLeftButton(String leftStr, View.OnClickListener clickListener) {
        doubleLeftBtn.setOnClickListener(clickListener);
        doubleLeftBtn.setText(leftStr);
    }

    /**
     * 设置提示内容
     *
     * @param str 内容
     */
    public void setHintText(String str) {
        hintTv.setText(str);
        hintTv.setVisibility(View.VISIBLE);
    }

    /**
     * 给两个按钮 设置文字
     *
     * @param leftStr  左按钮文字
     * @param rightStr 右按钮文字
     */
    public void setBtnText(String leftStr, String rightStr) {
        doubleLeftBtn.setText(leftStr);
        doubleRightBtn.setText(rightStr);
    }

    @Override
    public void onBackPressed() {
        dismiss();
    }
}