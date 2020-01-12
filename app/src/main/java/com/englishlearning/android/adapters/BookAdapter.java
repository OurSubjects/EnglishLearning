package com.englishlearning.android.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.englishlearning.android.models.Book;
import com.englishlearning.android.R;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.MyViewHolder> {

    //RecyclerView填充Item数据的List对象
    List<Book> datas;
    private OnItemClickListener onItemClickListener = null; //声明点击接口
    public BookAdapter(List<Book> datas){
        this.datas = datas;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //实例化得到Item布局文件的View对象
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_1,parent,false);
        //返回MyViewHolder的对象
        return new MyViewHolder(v);
    }

    //绑定数据
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final Book book= datas.get(position);
        holder.bookTitle.setText(book.getTitle());
        holder.bookImage.setImageResource(book.getImage());
        holder.wordNum.setText("词汇量："+book.getNum());
        holder.bookView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v,position,book);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        CardView bookView; //保存子项最外层布局实例
        ImageView bookImage;
        TextView bookTitle;
        TextView wordNum;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            bookView=(CardView)itemView;
            bookImage = itemView.findViewById(R.id.single_book_image);
            bookTitle=itemView.findViewById(R.id.single_book_name);
            wordNum=itemView.findViewById(R.id.single_book_word_num);
        }
    }
    /**
     * 定义RecyclerView选项单击事件的回调接口
     */
    public static interface OnItemClickListener{
        void onItemClick(View v,int position, Book city);  //处理item点击事件
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
