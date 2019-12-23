package com.example.worddict;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangms on 2018/7/16.
 */

public class MyAdapter extends BaseAdapter {

    List<String> data = new ArrayList<>();
    private Context mContext;
    ViewHolder holder;
    private boolean isShowCheckBox = false;//表示当前是否是多选状态。
    private SparseBooleanArray stateCheckedMap = new SparseBooleanArray();//用来存放CheckBox的选中状态，true为选中,false为没有选中

    public MyAdapter(Context context, List<String> data, SparseBooleanArray stateCheckedMap) {
        this.data = data;
        mContext = context;
        this.stateCheckedMap = stateCheckedMap;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item, null);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.checkBox = (CheckBox) convertView.findViewById(R.id.chb_select_way_point);
        holder.mTvData = convertView.findViewById(R.id.tv_data);
        showAndHideCheckBox();//控制CheckBox的那个的框显示与隐藏

        holder.mTvData.setText(data.get(position));
        holder.checkBox.setChecked(stateCheckedMap.get(position));//设置CheckBox是否选中
        return convertView;
    }

    public class ViewHolder {
        public TextView mTvData;
        public CheckBox checkBox;
    }

    private void showAndHideCheckBox() {
        if (isShowCheckBox) {
            holder.checkBox.setVisibility(View.VISIBLE);
        } else {
            holder.checkBox.setVisibility(View.GONE);
        }
    }


    public boolean isShowCheckBox() {
        return isShowCheckBox;
    }

    public void setShowCheckBox(boolean showCheckBox) {
        isShowCheckBox = showCheckBox;
    }

}