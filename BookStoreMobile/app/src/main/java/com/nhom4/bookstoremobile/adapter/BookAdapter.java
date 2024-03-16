package com.nhom4.bookstoremobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nhom4.bookstoremobile.R;
import com.nhom4.bookstoremobile.entities.Book;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    private final Context context;
    private final List<Book> bookList;

    public BookAdapter(Context context, List<Book> bookList) {
        this.context = context;
        this.bookList = bookList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_page_book_layout, parent, false);

        /*DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        int itemWidth = (screenWidth / 2) - 40;
        view.setLayoutParams(new ViewGroup.LayoutParams(itemWidth, ViewGroup.LayoutParams.WRAP_CONTENT));*/

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = bookList.get(position);

        holder.list_Name.setText(book.getTen());
        holder.list_Author.setText(book.getTacGia());
        holder.list_Price.setText(book.getGia());

        // Load image using Glide
        Glide.with(context)
                .load(book.getHinhAnh())
                .into(holder.BookImageMainPage);
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView BookImageMainPage;
        TextView list_Name;
        TextView list_Author;
        TextView list_Price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            BookImageMainPage = itemView.findViewById(R.id.list_Image);
            list_Name = itemView.findViewById(R.id.list_Name);
            list_Author = itemView.findViewById(R.id.list_Author);
            list_Price = itemView.findViewById(R.id.list_Price);
        }
    }
}
