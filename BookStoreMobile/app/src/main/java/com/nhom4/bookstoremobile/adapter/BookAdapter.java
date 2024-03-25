package com.nhom4.bookstoremobile.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nhom4.bookstoremobile.R;
import com.nhom4.bookstoremobile.activity.ViewBookDetails;
import com.nhom4.bookstoremobile.entities.Book;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    private final Context context;
    private final List<Book> bookList;
    private final RecyclerView mRecyclerView;
    private final boolean setWidth;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int itemPosition = mRecyclerView.getChildLayoutPosition(view);
            Book book = bookList.get(itemPosition);
            Intent intent = new Intent(context, ViewBookDetails.class);
            intent.putExtra("book_id", book.getId());
            context.startActivity(intent);
        }
    };

    public BookAdapter(Context context, List<Book> bookList, RecyclerView recyclerView) {
        this.context = context;
        this.bookList = bookList;
        mRecyclerView = recyclerView;
        this.setWidth = true;
    }

    public BookAdapter(Context context, List<Book> bookList, RecyclerView recyclerView, boolean setWidth) {
        this.context = context;
        this.bookList = bookList;
        mRecyclerView = recyclerView;
        this.setWidth = setWidth;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_page_book_layout, parent, false);
        view.setOnClickListener(mOnClickListener);

        if(setWidth) {
            setWidth(view);
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = bookList.get(position);

        holder.bookId = book.getId();
        holder.list_Name.setText(book.getTen());
        holder.list_Author.setText(book.getTacGia());
        holder.list_Price.setText(book.getGia());
        holder.list_Sold.setText("Đã bán " + book.getDaBan());

        Glide.with(context)
                .load(book.getHinhAnh())
                .into(holder.BookImageMainPage);
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        String bookId;
        ImageView BookImageMainPage;
        TextView list_Name;
        TextView list_Author;
        TextView list_Price;
        TextView list_Sold;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            BookImageMainPage = itemView.findViewById(R.id.list_Image);
            list_Name = itemView.findViewById(R.id.list_Name);
            list_Author = itemView.findViewById(R.id.list_Author);
            list_Price = itemView.findViewById(R.id.list_Price);
            list_Sold = itemView.findViewById(R.id.list_Sold);

        }
    }

    private void setWidth(View view) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.width = 600;
        view.setLayoutParams(layoutParams);
    }
}
