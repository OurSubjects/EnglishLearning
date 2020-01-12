package com.englishlearning.android.models;

public class Book {
    private String title;
    private String bookId;
    private int image;
    private int num;

    public Book(String bookId,String title,int image,int num){
        this.bookId=bookId;
        this.title=title;
        this.image=image;
        this.num=num;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
}
