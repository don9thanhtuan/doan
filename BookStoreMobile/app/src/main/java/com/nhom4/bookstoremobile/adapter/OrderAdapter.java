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
import com.nhom4.bookstoremobile.entities.Order; // Đổi thành package của lớp Order
import com.nhom4.bookstoremobile.retrofit.DefaultURL;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private final Context mContext;
    private final List<Order> mOrderList;
    private final RecyclerView mRecyclerView;

    public OrderAdapter(Context context, List<Order> orderList, RecyclerView recyclerView) {
        mContext = context;
        mOrderList = orderList;
        this.mRecyclerView = recyclerView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.main_order_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = mOrderList.get(position);
        Book book = order.getCuonSachDau();

        holder.textViewOrderId.setText(order.getMaDonHang());
        holder.textViewStatus.setText(order.getTrangThai());

        Glide.with(mContext)
                .load(book.getHinhAnh())
                .into(holder.imageViewFirstBook);

        holder.textViewFirstBook.setText(book.getTen());
        holder.textViewOrderPrice.setText(order.getThanhTien());
        String soSP = "Và " + order.getSoSanPham() + " cuốn sách khác";
        holder.textViewOtherBook.setText(soSP);
    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewOrderId;
        public TextView textViewStatus;
        public ImageView imageViewFirstBook;
        public TextView textViewFirstBook;
        public TextView textViewOrderPrice;
        public TextView textViewOtherBook;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewOrderId = itemView.findViewById(R.id.orderID);
            textViewStatus = itemView.findViewById(R.id.orderStatus);
            imageViewFirstBook = itemView.findViewById(R.id.firstBookImage);
            textViewFirstBook = itemView.findViewById(R.id.firstBookName);
            textViewOrderPrice = itemView.findViewById(R.id.orderPrice);
            textViewOtherBook = itemView.findViewById(R.id.otherBook);
        }
    }
}
