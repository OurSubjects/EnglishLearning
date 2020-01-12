package com.englishlearning.android.fragments;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.englishlearning.android.MyApplication;
import com.englishlearning.android.R;
import com.englishlearning.android.activities.BookListActivity;
import com.englishlearning.android.activities.OralCardActivity;
import com.englishlearning.android.learnMeaning;
import com.englishlearning.android.wordlist;

import java.util.ArrayList;
import java.util.List;

public class PracticeFragment extends Fragment implements View.OnClickListener {
    private Button to_phonetic;
    private Button book_change;
    private Button to_word;
    private Button to_listen;
    private ImageView book_image;
    private TextView book_name;
    private TextView book_word_num;
    private MyApplication app;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_practice,null);
        app = (MyApplication)getActivity().getApplication();
        initViews(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(app.getBookImage()==0 && app.getBookId().equals("")&&app.getWordNum()==0&& app.getBookTitle().equals("")){
            book_image.setVisibility(View.GONE);
            book_word_num.setVisibility(View.GONE);
            to_word.setVisibility(View.GONE);
            book_name.setTextSize(15);
            book_name.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
            book_name.setText("您还没有词单，快去添加词单上传单词吧!");
        }else {
            book_image.setVisibility(View.VISIBLE);
            book_word_num.setVisibility(View.VISIBLE);
            to_word.setVisibility(View.VISIBLE);
            book_name.setTextSize(25);
            book_name.setFilters(new InputFilter[] { new InputFilter.LengthFilter(10) });
            book_image.setImageResource(app.getBookImage());
            book_name.setText(app.getBookTitle());
            book_word_num.setText("词汇量："+app.getWordNum());
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        to_phonetic.setOnClickListener(this);
        book_change.setOnClickListener(this);
        to_word.setOnClickListener(this);
        to_listen.setOnClickListener(this);
    }
    private void initViews(View view){
        to_phonetic=(Button)view.findViewById(R.id.to_phonetic);
        book_change=(Button)view.findViewById(R.id.book_change);
        to_word=(Button)view.findViewById(R.id.to_word);
        to_listen=(Button)view.findViewById(R.id.to_listen);
        book_image=(ImageView)view.findViewById(R.id.book_image);
        book_name=(TextView)view.findViewById(R.id.book_name);
        book_word_num=(TextView)view.findViewById(R.id.book_word_num);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.to_phonetic:
                if (Build.VERSION.SDK_INT >= 23 && !app.isFlag())
                    requestPermission();
                else {
                    intent=new Intent(getActivity(), OralCardActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.to_word:
                Intent intent2 = new Intent();
                intent2.setClass(getActivity(), learnMeaning.class);
                startActivity(intent2);
                break;
            case R.id.book_change:
                intent=new Intent(getActivity(), BookListActivity.class);
                intent.putExtra("bookChangeFlag","1");
                startActivity(intent);
                break;
            case R.id.to_listen:
                break;
        }
    }
    /**
     * android 6.0之后动态申请权限
     */
    private void requestPermission() {
        // 创建一个权限列表，把需要使用而没用授权的的权限存放在这里
        List<String> permissionList = new ArrayList<>();
        // 判断权限是否已经授予，没有就把该权限添加到列表中
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.CAMERA);
        }

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.RECORD_AUDIO);
        }
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        // 如果列表为空，就是全部权限都获取了，不用再次获取了。不为空就去申请权限
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(),
                    permissionList.toArray(new String[permissionList.size()]), 100);
        } else {
            app.setFlag(true);
            Intent intent=new Intent(getActivity(), OralCardActivity.class);
            startActivity(intent);
        }
    }
}
