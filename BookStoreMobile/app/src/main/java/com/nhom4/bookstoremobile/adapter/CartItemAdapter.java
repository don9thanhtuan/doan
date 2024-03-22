package com.nhom4.bookstoremobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nhom4.bookstoremobile.R;
import com.nhom4.bookstoremobile.entities.Book;
import com.nhom4.bookstoremobile.entities.CartItem;

import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.ViewHolder> {
    private final Context context;
    private final List<CartItem> itemList;
    private final RecyclerView mRecyclerView;

    public CartItemAdapter(Context context, List<CartItem> itemList, RecyclerView mRecyclerView) {
        this.context = context;
        this.itemList = itemList;
        this.mRecyclerView = mRecyclerView;
    }

    @NonNull
    @Override
    public CartItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_cart_item_layout, parent, false);
//        view.setOnClickListener(mOnClickListener);

        return new CartItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemAdapter.ViewHolder holder, int position) {
        CartItem cartItem = itemList.get(position);
        Book book = cartItem.getBook();
        if(book != null) {
            holder.name_TxtView.setText(book.getTen());
            holder.author_TxtView.setText(book.getTacGia());
            holder.price_TxtView.setText(book.getGia());

            Glide.with(context)
                    .load(book.getHinhAnh())
                    .into(holder.imageView);
        }
        holder.bookId = cartItem.getBookID();
        int quantity = cartItem.getQuantity();
        holder.quantity_EditText.setText(String.valueOf(quantity));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        String bookId;
        ImageView imageView;
        TextView name_TxtView;
        TextView author_TxtView;
        TextView price_TxtView;
        EditText quantity_EditText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            name_TxtView = itemView.findViewById(R.id.name_TxtView);
            author_TxtView = itemView.findViewById(R.id.author_TxtView);
            price_TxtView = itemView.findViewById(R.id.price_TxtView);
            quantity_EditText = itemView.findViewById(R.id.quantity_EditText);
        }
    }
}
