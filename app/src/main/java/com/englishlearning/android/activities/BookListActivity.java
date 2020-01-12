package com.englishlearning.android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.englishlearning.android.MyApplication;
import com.englishlearning.android.models.Book;
import com.englishlearning.android.R;
import com.englishlearning.android.adapters.BookAdapter;
import com.englishlearning.android.wordlist;

import java.util.ArrayList;
import java.util.List;

public class BookListActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView backBtn; //返回按钮
    private ImageView addBtn; //添加词单按钮
    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;//适配器
    private MyApplication app; //应用程序MyApplication

    private String bookChangeFlag;

    protected List<Book> bookList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar() !=null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.book_list);
        Intent intent = getIntent();
        bookChangeFlag=intent.getStringExtra("bookChangeFlag");
        initResId();
        app = (MyApplication)getApplication();//获得我们的应用程序MyApplication
        addBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);//如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        bookAdapter = new BookAdapter(bookList);
        recyclerView.setAdapter(bookAdapter);
        if(bookChangeFlag.equals("1")) {
            bookAdapter.setOnItemClickListener(new BookAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View v, int position, Book book) {
                    app.setBookTitle(book.getTitle());
                    app.setWordNum(book.getNum());
                    app.setBookImage(book.getImage());
                    app.setBookId(book.getBookId());
                    SharedPreferences.Editor editor=app.getSharedPreferences().edit();
                    editor.putString("bookTitle",book.getTitle());
                    editor.putString("bookId",book.getBookId());
                    editor.putInt("bookImage",book.getImage());
                    editor.putInt("wordNum",book.getNum());
                    editor.apply();
                    finish();
                }
            });
        }else{
            bookAdapter.setOnItemClickListener(new BookAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View v, int position, Book book) {
                    Intent intent=new Intent(BookListActivity.this, wordlist.class);
                    intent.putExtra("bookId",book.getBookId());
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(bookChangeFlag.equals("1")){
            addBtn.setVisibility(View.GONE);
        }
        initData();
        if(bookList.size()==0){
            Intent intent_add = new Intent(BookListActivity.this, BookAddActivity.class);
            startActivity(intent_add);
            finish();
        }
    }

    //从数据库读取用户保存的地点数据
    private void initData(){
        //查询Book中所有数据
        Cursor cursor = app.getDb().query("Book",null,null,null,null,null,null);
        if(cursor.moveToFirst()){ //将数据的指针移动到第一行位置
            do{
                //getColumnIndex获取某一列在表中对应的位置索引
                String bookId = cursor.getString(cursor.getColumnIndex("bookId"));
                String bookTitle = cursor.getString(cursor.getColumnIndex("bookTitle"));
                int bookImage = cursor.getInt(cursor.getColumnIndex("bookImage"));
                int wordNum = cursor.getInt(cursor.getColumnIndex("wordNum"));
                bookList.add(new Book(bookId,bookTitle,bookImage,wordNum));
            }while (cursor.moveToNext());
        }
        cursor.close();
    }
    //初始化控件
    private void initResId(){
        backBtn = (ImageView)findViewById(R.id.back_bookList);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView_book);
        addBtn = (ImageView)findViewById(R.id.add_bookList);
    }
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_bookList:
                finish();
                break;
            case R.id.add_bookList:
                Intent intent_add = new Intent(BookListActivity.this, BookAddActivity.class);
                startActivity(intent_add);
                break;
            default:
                break;
        }
    }
    // 如果用户不是通过点击返回按钮，而是通过按下Back键返回上一个活动，就会执行onBackPressed()方法
    public void onBackPressed() {
        finish();
    }

}
