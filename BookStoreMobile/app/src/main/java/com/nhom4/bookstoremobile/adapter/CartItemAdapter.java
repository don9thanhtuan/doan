package com.nhom4.bookstoremobile.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nhom4.bookstoremobile.R;
import com.nhom4.bookstoremobile.activity.ViewBookDetails;
import com.nhom4.bookstoremobile.entities.Book;
import com.nhom4.bookstoremobile.entities.CartItem;
import com.nhom4.bookstoremobile.sqlite.CartDB;

import java.util.ArrayList;
import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.ViewHolder> {
    private final Context context;
    private final List<CartItem> itemList;
    private final RecyclerView mRecyclerView;
    private List<Boolean> isChecked = new ArrayList<>();
    private Button paymentBtn;
    private TextView totalPriceTxtView;
    private CheckBox totalCheckBox;


    public CartItemAdapter(Context context, List<CartItem> itemList, RecyclerView mRecyclerView) {
        this.context = context;
        this.itemList = itemList;
        this.mRecyclerView = mRecyclerView;
        for (int i = 0; i < itemList.size(); i++) {
            isChecked.add(false);
        }
    }

    public void setPaymentBtn(Button paymentBtn) {
        this.paymentBtn = paymentBtn;
    }

    public void setTotalPriceTxtView(TextView totalPriceTxtView) {
        this.totalPriceTxtView = totalPriceTxtView;
    }

    public void setTotalCheckBox(CheckBox totalCheckBox) {
        this.totalCheckBox = totalCheckBox;
    }

    @NonNull
    @Override
    public CartItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_cart_item_layout, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int itemPosition = mRecyclerView.getChildLayoutPosition(view);
                CartItem cartItem = itemList.get(itemPosition);
                Intent intent = new Intent(context, ViewBookDetails.class);
                intent.putExtra("book_id", cartItem.getBookID());
                context.startActivity(intent);
            }
        });

        return new CartItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemAdapter.ViewHolder holder, int p) {
        int position = holder.getAdapterPosition();
        CartItem cartItem = itemList.get(position);
        Book book = cartItem.getBook();

        holder.bookId = cartItem.getBookID();

        int quantity = cartItem.getQuantity();
        EditText quantity_EditText = holder.quantity_EditText;
        quantity_EditText.setText(String.valueOf(quantity));

        if (book != null) {
            holder.name_TxtView.setText(book.getTen());
            holder.author_TxtView.setText(book.getTacGia());
            holder.price_TxtView.setText(book.getGia());

            Glide.with(context)
                 .load(book.getHinhAnh())
                 .into(holder.imageView);
        }

        boolean isCheck = isChecked.get(position);
        holder.checkBox.setChecked(isCheck);

        setListener(holder);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void checkAllCartItem(boolean isCheckedAll) {
        for (int i = 0; i < itemList.size(); i++) {
            isChecked.set(i, isCheckedAll);
        }
        notifyDataSetChanged();
    }

    private void addItemToCart(String id, int quantity) {
        CartDB cartDB = new CartDB(context);
        Cursor cursor = cartDB.findCartItem(id);

        if (cursor != null && cursor.moveToFirst()) {
            cartDB.updateQuantityItem(id, quantity);
            cursor.close();
        } else {
            cartDB.addToCart(id, quantity);
        }
        notifyDataSetChanged();
    }

    private void getTotalQuantity() {
        int totalQuantity = 0;

        for (int i = 0; i < itemList.size(); i++) {
            if (isChecked.get(i)) {
                totalQuantity += itemList.get(i).getQuantity();
            }
        }

        String text = "Thanh toán (" + totalQuantity + ")";
        paymentBtn.setText(text);
    }

    private void getTotalPrice() {
        int totalPrice = 0;

        for (int i = 0; i < itemList.size(); i++) {
            if (isChecked.get(i)) {
                Book book = itemList.get(i).getBook();
                if (book != null) {
                    String priceRaw = book.getGia();
                    priceRaw = priceRaw.replace("₫", "");
                    priceRaw = priceRaw.replaceAll("\\s+", "");
                    priceRaw = priceRaw.replace(".", "");

                    int price = Integer.parseInt(priceRaw);
                    totalPrice += (itemList.get(i).getQuantity() * price);
                }
            }
        }

        String totalPriceString = String.format("%,d", totalPrice).replace(',', '.') + " ₫";
        totalPriceTxtView.setText(totalPriceString);
    }

    public void deleteCartItem() {
        for (int i = 0; i < itemList.size(); i++) {
            if (isChecked.get(i)) {
                CartItem cartItem = itemList.get(i);

                CartDB cartDB = new CartDB(context);
                cartDB.removeFromCart(cartItem.getBookID());

                isChecked.remove(i);
                itemList.remove(i);
                i--;
            }
        }
        if (itemList.size() == 0) {
            getTotalQuantity();
            getTotalPrice();
        }
        for (int i = 0; i < itemList.size(); i++) {
            isChecked.set(i, false);
        }

        notifyDataSetChanged();
    }

    public void updateTotal() {
        getTotalQuantity();
        getTotalPrice();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        String bookId;
        ImageView imageView;
        TextView name_TxtView;
        TextView author_TxtView;
        TextView price_TxtView;
        EditText quantity_EditText;
        ImageButton plusBtn;
        ImageButton minusBtn;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            name_TxtView = itemView.findViewById(R.id.name_TxtView);
            author_TxtView = itemView.findViewById(R.id.author_TxtView);
            price_TxtView = itemView.findViewById(R.id.price_TxtView);
            quantity_EditText = itemView.findViewById(R.id.quantity_EditText);
            plusBtn = itemView.findViewById(R.id.plusBtn);
            minusBtn = itemView.findViewById(R.id.minusBtn);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }

    private void setListener(ViewHolder holder) {
        int position = holder.getAdapterPosition();
        CartItem cartItem = itemList.get(position);
        Book book = cartItem.getBook();

        EditText quantity_EditText = holder.quantity_EditText;
        if(book != null) {
            quantity_EditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    String quantityRaw = s.toString();
                    int quantity = 0;
                    if (!quantityRaw.isEmpty()) {
                        quantity = Integer.parseInt(quantityRaw);
                    }

                    if (quantity <= 0) {
                        Toast.makeText(context, "Số lượng tối thiểu là 1", Toast.LENGTH_SHORT).show();
                        cartItem.setQuantity(1);
                        addItemToCart(holder.bookId, 1);
                    } else if (book.getTonKho() < quantity) {
                        Toast.makeText(context, "Số lượng tồn kho không đủ", Toast.LENGTH_SHORT).show();
                        cartItem.setQuantity(book.getTonKho());
                        addItemToCart(holder.bookId, book.getTonKho());
                    }
                    getTotalQuantity();
                    getTotalPrice();
                }
            });
        }

        holder.plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantityRaw = quantity_EditText.getText().toString();
                int quantity = Integer.parseInt(quantityRaw) + 1;

                if (book.getTonKho() < quantity) {
                    Toast.makeText(context, "Số lượng tồn kho không đủ", Toast.LENGTH_SHORT).show();
                } else {
                    cartItem.setQuantity(quantity);
                    addItemToCart(holder.bookId, quantity);
                    getTotalQuantity();
                    getTotalPrice();
                }
            }
        });

        holder.minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantityRaw = quantity_EditText.getText().toString();
                int quantity = Integer.parseInt(quantityRaw) - 1;

                if (quantity <= 0) {
                    Toast.makeText(context, "Số lượng tối thiểu là 1", Toast.LENGTH_SHORT).show();
                } else {
                    cartItem.setQuantity(quantity);
                    addItemToCart(holder.bookId, quantity);
                    getTotalQuantity();
                    getTotalPrice();
                }
            }
        });

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isCheck) {
                RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();

                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition >= firstVisibleItemPosition && adapterPosition <= lastVisibleItemPosition) {
                    isChecked.set(position, isCheck);
                    getTotalQuantity();
                    getTotalPrice();
                }
                setCheckedTotal();
            }
        });
    }

    private void setCheckedTotal() {
        for (boolean check : isChecked) {
            if (!check) {
                return;
            }
        }
        totalCheckBox.setChecked(true);
    }
}
