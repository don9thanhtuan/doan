package com.nhom4.bookstoremobile.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.nhom4.bookstoremobile.R;
import com.nhom4.bookstoremobile.adapter.BookAdapter;
import com.nhom4.bookstoremobile.entities.Book;
import com.nhom4.bookstoremobile.retrofit.DefaultURL;
import com.nhom4.bookstoremobile.retrofit.RetrofitAPI;
import com.nhom4.bookstoremobile.service.BookService;
import com.nhom4.bookstoremobile.sqlite.CartTable;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewBookDetails extends AppCompatActivity {
    private Book book;
    private View addCart_Layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
        String id = getIntent().getStringExtra("book_id");

        setListenerMainLayout();

        getBookDetailFromAPI(id);
        getBookListFromAPI();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }

    private void getBookDetailFromAPI(String bookID) {
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
    }

    private void getBookListFromAPI() {
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

                    RecyclerView recyclerView = findViewById(R.id.detail_RecyclerView);
                    BookAdapter adapter = new BookAdapter(ViewBookDetails.this, bookList, recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(ViewBookDetails.this, LinearLayoutManager.HORIZONTAL, false));
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
            }
        });
    }


    private void setData(Book book) {
        ImageView bookImage = findViewById(R.id.book_Image);
        TextView nameTextView = findViewById(R.id.name_TxtView);
        TextView soldTextView = findViewById(R.id.sold_TxtView);
        TextView priceTextView = findViewById(R.id.price_TxtView);
        TextView idTextView = findViewById(R.id.id_TxtView);
        TextView authorTextView = findViewById(R.id.author_TxtView);
        TextView publisherTextView = findViewById(R.id.publisher_TxtView);
        TextView weightTextView = findViewById(R.id.weight_TxtView);
        TextView sizeTextView = findViewById(R.id.size_TxtView);
        TextView introductionTextView = findViewById(R.id.introduction_TxtView);

        Glide.with(this)
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

    private void closeAddCart() {
        FrameLayout layoutContainer = findViewById(R.id.addCart_Layout);

        findViewById(R.id.overlayLayout).setVisibility(View.GONE);
        addCart_Layout.setClickable(false);
        addCart_Layout.setFocusable(false);
        Animation slideDownAnimation = AnimationUtils.loadAnimation(ViewBookDetails.this, R.anim.slide_down);
        addCart_Layout.startAnimation(slideDownAnimation);
        layoutContainer.removeAllViewsInLayout();
    }

    private void addItemToCart(String id, int quantity) {
        CartTable cartTable = new CartTable(ViewBookDetails.this);
        Cursor cursor = cartTable.findCartItem(id);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int current_Quantity = cursor.getInt(cursor.getColumnIndex("quantity"));

                cartTable.updateQuantityItem(id, current_Quantity + quantity);
            } while (cursor.moveToNext());

            cursor.close();
        } else {
            cartTable.addToCart(id, quantity);
        }
    }

    private void setDataToAddCart() {
        ImageView imageView = addCart_Layout.findViewById(R.id.imageView);
        TextView nameTextView = addCart_Layout.findViewById(R.id.name_TxtView);
        TextView authorTextView = addCart_Layout.findViewById(R.id.author_TxtView);
        TextView priceTextView = addCart_Layout.findViewById(R.id.price_TxtView);


        Glide.with(this)
                .load(book.getHinhAnh())
                .into(imageView);

        nameTextView.setText(book.getTen());
        authorTextView.setText(book.getTacGia());
        priceTextView.setText(book.getGia());
    }

    private void setListenerAddLayout(View addCart_Layout) {
        EditText quantity_EditText = addCart_Layout.findViewById(R.id.quantity_EditText);

        addCart_Layout.findViewById(R.id.closeBtn).setOnClickListener(v -> closeAddCart());
        addCart_Layout.findViewById(R.id.addToCartBtn).setOnClickListener(v -> {
            String quantityRaw = quantity_EditText.getText().toString();
            int quantity = Integer.parseInt(quantityRaw);
            addItemToCart(book.getId(), quantity);
            Toast.makeText(ViewBookDetails.this, "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
            closeAddCart();
        });
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
                    Toast.makeText(ViewBookDetails.this, "Số lượng tối thiểu là 1", Toast.LENGTH_SHORT).show();
                    quantity_EditText.setText("1");
                } else if (book.getTonKho() < quantity) {
                    Toast.makeText(ViewBookDetails.this, "Số lượng tồn kho không đủ", Toast.LENGTH_SHORT).show();
                    quantity_EditText.setText(String.valueOf(book.getTonKho()));
                }
            }
        });

        addCart_Layout.findViewById(R.id.plusBtn).setOnClickListener(v -> {
            String quantityRaw = quantity_EditText.getText().toString();
            int quantity = Integer.parseInt(quantityRaw) + 1;

            if (book.getTonKho() < quantity) {
                Toast.makeText(ViewBookDetails.this, "Số lượng tồn kho không đủ", Toast.LENGTH_SHORT).show();
            } else {
                quantity_EditText.setText(String.valueOf(quantity));
            }
        });

        addCart_Layout.findViewById(R.id.minusBtn).setOnClickListener(v -> {
            String quantityRaw = quantity_EditText.getText().toString();
            int quantity = Integer.parseInt(quantityRaw) - 1;

            if (quantity <= 0) {
                Toast.makeText(ViewBookDetails.this, "Số lượng tối thiểu là 1", Toast.LENGTH_SHORT).show();
            } else {
                quantity_EditText.setText(String.valueOf(quantity));
            }
        });
    }

    private void setListenerMainLayout() {
        findViewById(R.id.backButton).setOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
        });

        findViewById(R.id.cartBtn).setOnClickListener(v -> {
            Intent intent = new Intent(ViewBookDetails.this, ViewCart.class);
            startActivity(intent);
        });

        findViewById(R.id.addToCartBtn).setOnClickListener(v -> {
            LayoutInflater inflater = getLayoutInflater();
            addCart_Layout = inflater.inflate(R.layout.main_add_cart_item_layout, null);
            FrameLayout layoutContainer = findViewById(R.id.addCart_Layout);
            layoutContainer.addView(addCart_Layout);

            addCart_Layout.setClickable(true);
            addCart_Layout.setFocusable(true);
            findViewById(R.id.overlayLayout).setVisibility(View.VISIBLE);

            setDataToAddCart();

            setListenerAddLayout(addCart_Layout);

            Animation slideUpAnimation = AnimationUtils.loadAnimation(ViewBookDetails.this, R.anim.slide_up);
            addCart_Layout.startAnimation(slideUpAnimation);
        });

        findViewById(R.id.overlayLayout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                closeAddCart();
                return false;
            }
        });

        SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(() -> {
            recreate();
            pullToRefresh.setRefreshing(false);
        });
    }
}