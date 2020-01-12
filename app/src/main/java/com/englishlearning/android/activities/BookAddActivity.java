package com.englishlearning.android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.englishlearning.android.MyApplication;
import com.englishlearning.android.R;
import com.englishlearning.android.dataBase.SqlHelper;
import com.englishlearning.android.wordlist;

public class BookAddActivity extends AppCompatActivity  implements View.OnClickListener {


    private TextView ok_bookCreate;
    private ImageView back_bookCreate;
    private EditText title_content;
    private ImageView book_image_1;
    private ImageView book_image_2;
    private ImageView book_image_3;
    private ImageView book_image_4;
    private ImageView book_image_5;
    private ImageView book_image_6;
    /** 当前的ImageView */
    private ImageView currentImage;

    private int MaxNum=10;//标题最大字符数
    private MyApplication app; //应用程序MyApplication
    private SqlHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar() !=null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.book_create);
        app = (MyApplication)getApplication();//获得我们的应用程序MyApplication
        helper=new SqlHelper();
        initView();
        initEvent();
    }
    private void initView() {
        book_image_1 = (ImageView) findViewById(R.id.book_image_1);
        book_image_1.setTag("book_1");
        book_image_2 = (ImageView) findViewById(R.id.book_image_2);
        book_image_2.setTag("book_2");
        book_image_3 = (ImageView) findViewById(R.id.book_image_3);
        book_image_3.setTag("book_3");
        book_image_4 = (ImageView) findViewById(R.id.book_image_4);
        book_image_4.setTag("book_4");
        book_image_5 = (ImageView) findViewById(R.id.book_image_5);
        book_image_5.setTag("book_5");
        book_image_6 = (ImageView) findViewById(R.id.book_image_6);
        book_image_6.setTag("book_6");
        back_bookCreate = (ImageView) findViewById(R.id.back_bookCreate);
        ok_bookCreate=(TextView)findViewById(R.id.ok_bookCreate);
        title_content=(EditText)findViewById(R.id.title_content);
    }

    private  void initEvent() {

        book_image_1.setOnClickListener(this);
        book_image_2.setOnClickListener(this);
        book_image_3.setOnClickListener(this);
        book_image_4.setOnClickListener(this);
        book_image_5.setOnClickListener(this);
        book_image_6.setOnClickListener(this);
        //默认给第一个ImageView加边框
        book_image_1.setImageResource(R.drawable.border);
        currentImage = book_image_1;
        back_bookCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_content.addTextChangedListener(new TextWatcher() {
            private CharSequence wordNum;//记录输入的字数
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                wordNum= s;//实时记录输入的字数
            }

            @Override
            public void afterTextChanged(Editable s) {
                int number = MaxNum - s.length();
                //TextView显示剩余字数
                //tvWordNumber.setText("" + number);
                selectionStart=title_content.getSelectionStart();
                selectionEnd = title_content.getSelectionEnd();
                if (wordNum.length() > MaxNum) {
                    //删除多余输入的字（不会显示出来）
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    title_content.setText(s);
                    title_content.setSelection(tempSelection);//设置光标在最后
                }
            }
        });
        ok_bookCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values=new ContentValues();
                String inputText=title_content.getText().toString();
                if(inputText.trim().equals("")){
                    Toast.makeText(BookAddActivity.this,"请输入词单标题",Toast.LENGTH_SHORT).show();
                }else {
                    Cursor cursor = app.getDb().query("Book",null,null,null,null,null,null);
                    values.put("bookId","book_"+cursor.getCount());
                    values.put("bookTitle",inputText);
                    values.put("bookImage",getResources().getIdentifier(currentImage.getTag().toString(), "drawable", "com.englishlearning.android"));
                    values.put("wordNum",0);
                    app.getDb().replace("Book",null,values);
                    //创建词单表
                    helper.setTableName("book_"+cursor.getCount(),(MyApplication) getApplication());
                    helper.CreateTable(MyApplication.getContext(),"book_"+cursor.getCount());
                    //进入单词具体界面
                    Intent intent=new Intent(BookAddActivity.this, wordlist.class);
                    intent.putExtra("bookId","book_"+cursor.getCount());
                    startActivity(intent);
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        handleImageView((ImageView) v);
    }
    /**
     * 给点击选中的ImageView加边框，并将之前的ImageView边框清除
     * @param imageView 要添加边框的ImageView
     */
    public void handleImageView(ImageView imageView){
        currentImage.setImageDrawable(null);
        imageView.setImageResource(R.drawable.border);
        currentImage = imageView;
}
}
