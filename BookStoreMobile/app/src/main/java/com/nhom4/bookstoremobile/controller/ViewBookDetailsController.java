package com.nhom4.bookstoremobile.controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.nhom4.bookstoremobile.R;
import com.nhom4.bookstoremobile.activity.EditBook;
import com.nhom4.bookstoremobile.activity.ViewBookDetails;
import com.nhom4.bookstoremobile.activity.ViewCart;
import com.nhom4.bookstoremobile.adapter.BookAdapter;
import com.nhom4.bookstoremobile.entities.Book;
import com.nhom4.bookstoremobile.entities.CartItem;
import com.nhom4.bookstoremobile.retrofit.DefaultURL;
import com.nhom4.bookstoremobile.retrofit.RetrofitAPI;
import com.nhom4.bookstoremobile.service.BookService;
import com.nhom4.bookstoremobile.sqlite.CartTable;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewBookDetailsController {
    private final ViewBookDetails view;
    private final String bookID;
    private Book book;

    public ViewBookDetailsController(ViewBookDetails view, String bookID) {
        this.view = view;
        this.bookID = bookID;
    }

    public void getBookDetailFromAPI() {
        BookService bookService = RetrofitAPI.getInstance().create(BookService.class);
        Call<Book> call = bookService.getBookDetailsFromRestAPI(bookID);
        call.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                if (response.isSuccessful()) {
                    book = response.body();
                    String imageUrl = DefaultURL.getUrl() + book.getHinhAnh();
                    book.setHinhAnh(imageUrl);
                    setData(book);
                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
            }
        });

        view.findViewById(R.id.editBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view, EditBook.class);
                intent.putExtra("book_id", book.getId());
                intent.putExtra("book_name", book.getTen());
                intent.putExtra("book_HinhAnh", book.getHinhAnh());
                intent.putExtra("book_TacGia", book.getTacGia());
                intent.putExtra("book_NhaCungCap", book.getNhaCungCap());
                intent.putExtra("book_TonKho", book.getTonKho());
                intent.putExtra("book_Gia", book.getGia());
                intent.putExtra("book_TrongLuong", book.getTrongLuong());
                intent.putExtra("book_KickThuoc", book.getKichThuoc());
                intent.putExtra("book_GioiThieu", book.getGioiThieu());
                view.startActivity(intent);
                view.finish();
            }
        });

        view.findViewById(R.id.deleteBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationPopup();
            }
        });
    }

    private void showConfirmationPopup(String title, String message, DialogInterface.OnClickListener positiveClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", positiveClickListener)
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showConfirmationPopup() {
        TextView idTextView = view.findViewById(R.id.id_TxtView);
        String id = idTextView.getText().toString();
        showConfirmationPopup("Xác nhận", "Bạn muốn xóa sản phẩm " + id + "?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteBookByAPi();
            }
        });
    }

    private void deleteBookByAPi() {
        BookService bookService = RetrofitAPI.getInstance().create(BookService.class);
        Call<ResponseBody> call = bookService.deleteBook(book.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(view, "Xóa thành công sản phẩm " + book.getId(), Toast.LENGTH_SHORT).show();
                    view.finish();
                } else {
                    Toast.makeText(view, "Failed to delete book", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    public void getBookListFromAPI() {
        BookService bookService = RetrofitAPI.getInstance().create(BookService.class);
        Call<List<Book>> call2 = bookService.getBookFromRestAPI();
        call2.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful()) {
                    List<Book> bookList = response.body();
                    if (book != null) {
                        for (Book bookInList : bookList) {
                            if (bookInList.getId().equals(book.getId())) {
                                bookList.remove(bookInList);
                                break;
                            }
                        }
                    }

                    for (Book book : bookList) {
                        String imageUrl = DefaultURL.getUrl() + book.getHinhAnh();
                        book.setHinhAnh(imageUrl);
                    }

                    RecyclerView recyclerView = view.findViewById(R.id.detail_RecyclerView);
                    BookAdapter adapter = new BookAdapter(view, bookList, recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(view, LinearLayoutManager.HORIZONTAL, false));
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
            }
        });
    }

    private void setData(Book book) {
        ImageView bookImage = view.findViewById(R.id.book_Image);
        TextView nameTextView = view.findViewById(R.id.name_TxtView);
        TextView soldTextView = view.findViewById(R.id.sold_TxtView);
        TextView priceTextView = view.findViewById(R.id.price_TxtView);
        TextView idTextView = view.findViewById(R.id.id_TxtView);
        TextView authorTextView = view.findViewById(R.id.author_TxtView);
        TextView publisherTextView = view.findViewById(R.id.publisher_TxtView);
        TextView weightTextView = view.findViewById(R.id.weight_TxtView);
        TextView sizeTextView = view.findViewById(R.id.size_TxtView);
        TextView introductionTextView = view.findViewById(R.id.introduction_TxtView);

        Glide.with(view)
                .load(book.getHinhAnh())
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .fitCenter()
                .into(bookImage);

        nameTextView.setText(book.getTen());
        soldTextView.setText("Đã bán " + book.getDaBan());
        priceTextView.setText(book.getGia());
        idTextView.setText(book.getId());
        authorTextView.setText(book.getTacGia());
        publisherTextView.setText(book.getNhaCungCap());
        weightTextView.setText(book.getTrongLuong() + " gr");
        sizeTextView.setText(book.getKichThuoc());
        introductionTextView.setText(book.getGioiThieu());
    }

    public void openAddCartView() {
        CartItem cartItem = getCartItem(bookID);
        if (cartItem != null) {
            if (cartItem.getQuantity() >= book.getTonKho()) {
                Toast.makeText(view, "Số lượng tồn kho không đủ", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        LayoutInflater inflater = view.getLayoutInflater();
        View addCartLayout = inflater.inflate(R.layout.main_add_cart_item_layout, null);
        FrameLayout layoutContainer = view.findViewById(R.id.addCart_Layout);
        layoutContainer.addView(addCartLayout);

        addCartLayout.setClickable(true);
        addCartLayout.setFocusable(true);
        view.findViewById(R.id.overlayLayout).setVisibility(View.VISIBLE);

        setDataToAddCart(addCartLayout);

        setListenerAddLayout(addCartLayout);

        Animation slideUpAnimation = AnimationUtils.loadAnimation(view, R.anim.slide_up);
        addCartLayout.startAnimation(slideUpAnimation);
    }

    private CartItem getCartItem(String id) {
        CartTable cartTable = new CartTable(view);
        Cursor cursor = cartTable.findCartItem(id);

        if (cursor != null && cursor.moveToFirst()) {
            int quantity = cursor.getInt(cursor.getColumnIndex("quantity"));
            return new CartItem(id, quantity);
        }
        return null;
    }

    private void setDataToAddCart(View addCartLayout) {
        ImageView imageView = addCartLayout.findViewById(R.id.imageView);
        TextView nameTextView = addCartLayout.findViewById(R.id.name_TxtView);
        TextView authorTextView = addCartLayout.findViewById(R.id.author_TxtView);
        TextView priceTextView = addCartLayout.findViewById(R.id.price_TxtView);


        Glide.with(view)
                .load(book.getHinhAnh())
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .fitCenter()
                .into(imageView);

        nameTextView.setText(book.getTen());
        authorTextView.setText(book.getTacGia());
        priceTextView.setText(book.getGia());
    }

    private void addItemToCart(String id, int quantity) {
        try (CartTable cartTable = new CartTable(view)) {
            CartItem cartItem = getCartItem(id);

            if (cartItem != null) {
                cartTable.updateQuantityItem(id, cartItem.getQuantity() + quantity);
            } else {
                cartTable.addToCart(id, quantity);
            }
        }
    }

    public void closeAddCartView() {
        FrameLayout layoutContainer = view.findViewById(R.id.addCart_Layout);
        View addCartLayout = layoutContainer.getChildAt(0);

        view.findViewById(R.id.overlayLayout).setVisibility(View.GONE);
        addCartLayout.setClickable(false);
        addCartLayout.setFocusable(false);
        Animation slideDownAnimation = AnimationUtils.loadAnimation(view, R.anim.slide_down);
        addCartLayout.startAnimation(slideDownAnimation);
        layoutContainer.removeAllViewsInLayout();
    }

    private void setListenerAddLayout(View addCart_Layout) {
        CartItem cartItem = getCartItem(bookID);
        EditText quantity_EditText = addCart_Layout.findViewById(R.id.quantity_EditText);

        addCart_Layout.findViewById(R.id.closeBtn).setOnClickListener(v -> closeAddCartView());
        addCart_Layout.findViewById(R.id.addToCartBtn).setOnClickListener(v -> clickAddBtn(quantity_EditText));
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

                int quantityInCart = 0;
                if (cartItem != null) {
                    quantityInCart = cartItem.getQuantity();
                }

                if (quantity <= 0) {
                    Toast.makeText(view, "Số lượng tối thiểu là 1", Toast.LENGTH_SHORT).show();
                    quantity_EditText.setText("1");
                } else if (book.getTonKho() < (quantity + quantityInCart)) {
                    Toast.makeText(view, "Số lượng tồn kho không đủ", Toast.LENGTH_SHORT).show();
                    int remain = book.getTonKho() - quantityInCart;
                    quantity_EditText.setText(String.valueOf(remain));
                }
            }
        });
        addCart_Layout.findViewById(R.id.plusBtn).setOnClickListener(v -> clickPlusBtn(cartItem, quantity_EditText));
        addCart_Layout.findViewById(R.id.minusBtn).setOnClickListener(v -> clickMinusBtn(quantity_EditText));
    }

    private void clickMinusBtn(EditText quantity_EditText) {
        String quantityRaw = quantity_EditText.getText().toString();
        int quantity = Integer.parseInt(quantityRaw) - 1;

        if (quantity <= 0) {
            Toast.makeText(view, "Số lượng tối thiểu là 1", Toast.LENGTH_SHORT).show();
        } else {
            quantity_EditText.setText(String.valueOf(quantity));
        }
    }

    private void clickPlusBtn(CartItem cartItem, EditText quantity_EditText) {
        String quantityRaw = quantity_EditText.getText().toString();
        int quantityInCart = 0;
        if (cartItem != null) {
            quantityInCart = cartItem.getQuantity();
        }

        int quantity = Integer.parseInt(quantityRaw) + 1;
        if (book.getTonKho() < (quantity + quantityInCart)) {
            Toast.makeText(view, "Số lượng tồn kho không đủ", Toast.LENGTH_SHORT).show();
        } else {
            quantity_EditText.setText(String.valueOf(quantity));
        }
    }

    private void clickAddBtn(EditText quantity_EditText) {
        String quantityRaw = quantity_EditText.getText().toString();
        int quantity = Integer.parseInt(quantityRaw);
        addItemToCart(book.getId(), quantity);
        Toast.makeText(view, "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
        closeAddCartView();
    }

    public void redirectToBookList() {
        view.finish();
        view.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }

    public void reload(SwipeRefreshLayout pullToRefresh) {
        view.recreate();
        pullToRefresh.setRefreshing(false);
    }

    public void redirectToCart() {
        Intent intent = new Intent(view, ViewCart.class);
        view.startActivity(intent);
        view.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }
}
