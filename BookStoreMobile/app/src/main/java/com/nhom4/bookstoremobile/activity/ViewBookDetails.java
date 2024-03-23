package com.nhom4.bookstoremobile.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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

import com.bumptech.glide.Glide;
import com.nhom4.bookstoremobile.R;
import com.nhom4.bookstoremobile.adapter.BookAdapter;
import com.nhom4.bookstoremobile.entities.Book;
import com.nhom4.bookstoremobile.retrofit.RetrofitAPI;
import com.nhom4.bookstoremobile.service.BookService;
import com.nhom4.bookstoremobile.sqlite.CartDB;

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

        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewBookDetails.this, ViewBookList.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
            }
        });
        findViewById(R.id.editButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewBookDetails.this, EditBook.class);
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
                startActivity(intent);
                finish();
            }
        });
        findViewById(R.id.deleteButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationPopup();
            }
        });

        findViewById(R.id.addToCartBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                addCart_Layout = inflater.inflate(R.layout.main_add_cart_item_layout, null);
                FrameLayout layoutContainer = findViewById(R.id.addCart_Layout);
                layoutContainer.addView(addCart_Layout);

                addCart_Layout.setClickable(true);
                addCart_Layout.setFocusable(true);
                findViewById(R.id.overlayLayout).setVisibility(View.VISIBLE);
                addCart_Layout.findViewById(R.id.closeBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        closeAddCart();
                    }
                });
                addCart_Layout.findViewById(R.id.addToCartBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText quantityEditText = findViewById(R.id.quantity_EditText);
                        String quantityRaw = quantityEditText.getText().toString();
                        int quantity = Integer.parseInt(quantityRaw);
                        addItemToCart(id, quantity);
                        Toast.makeText(ViewBookDetails.this, "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                        closeAddCart();
                    }
                });

                addCart_Layout.findViewById(R.id.plusBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText quantity_EditText = findViewById(R.id.quantity_EditText);
                        String quantityRaw = quantity_EditText.getText().toString();
                        int quantity = Integer.parseInt(quantityRaw) + 1;

                        if (book.getTonKho() < quantity) {
                            Toast.makeText(ViewBookDetails.this, "Số lượng tồn kho không đủ", Toast.LENGTH_SHORT).show();
                        } else {
                            quantity_EditText.setText(String.valueOf(quantity));
                        }
                    }
                });

                addCart_Layout.findViewById(R.id.minusBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText quantity_EditText = findViewById(R.id.quantity_EditText);
                        String quantityRaw = quantity_EditText.getText().toString();
                        int quantity = Integer.parseInt(quantityRaw) - 1;

                        if (quantity <= 0) {
                            Toast.makeText(ViewBookDetails.this, "Số lượng tối thiểu là 1", Toast.LENGTH_SHORT).show();
                        } else {
                            quantity_EditText.setText(String.valueOf(quantity));
                        }
                    }
                });
                setDataToAddCart();

                Animation slideUpAnimation = AnimationUtils.loadAnimation(ViewBookDetails.this, R.anim.slide_up);
                addCart_Layout.startAnimation(slideUpAnimation);
            }
        });

        findViewById(R.id.overlayLayout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                closeAddCart();
                return false;
            }
        });

        getBookDetailFromAPI(id);
        getBookListFromAPI();
    }

    private void getBookDetailFromAPI(String bookID) {
        BookService bookService = RetrofitAPI.getInstance().create(BookService.class);
        Call<Book> call = bookService.getBookDetailsFromRestAPI(bookID);
        call.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                if (response.isSuccessful()) {
                    book = response.body();
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
                    for (Book bookInList : bookList) {
                        if (bookInList.getId().equals(book.getId())) {
                            bookList.remove(bookInList);
                            break;
                        }
                    }
                    for (Book book : bookList) {
                        String imageUrl = book.getHinhAnh();
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            book.setHinhAnh("http://10.0.2.2:8080" + imageUrl);
                        }
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
                .load("http://10.0.2.2:8080" + book.getHinhAnh())
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

    private void showConfirmationPopup(Context context, String title, String message, DialogInterface.OnClickListener positiveClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", positiveClickListener)
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showConfirmationPopup() {
        TextView idTextView = findViewById(R.id.id_TxtView);
        String id = idTextView.getText().toString();
        showConfirmationPopup(this, "Xác nhận", "Bạn muốn xóa sản phẩm " + id + "?", new DialogInterface.OnClickListener() {
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
                    Toast.makeText(ViewBookDetails.this, "Xóa thành công sản phẩm " + book.getId(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ViewBookDetails.this, ViewBookList.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ViewBookDetails.this, "Failed to delete book", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
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
        CartDB cartDB = new CartDB(ViewBookDetails.this);
        Cursor cursor = cartDB.findCartItem(id);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int current_Quantity = cursor.getInt(cursor.getColumnIndex("quantity"));

                cartDB.updateQuantityItem(id, current_Quantity + quantity);
            } while (cursor.moveToNext());

            cursor.close();
        } else {
            cartDB.addToCart(id, quantity);
        }
    }

    private void setDataToAddCart() {
        ImageView imageView = addCart_Layout.findViewById(R.id.imageView);
        TextView nameTextView = addCart_Layout.findViewById(R.id.name_TxtView);
        TextView authorTextView = addCart_Layout.findViewById(R.id.author_TxtView);
        TextView priceTextView = addCart_Layout.findViewById(R.id.price_TxtView);


        Glide.with(this)
                .load("http://10.0.2.2:8080" + book.getHinhAnh())
                .into(imageView);

        nameTextView.setText(book.getTen());
        authorTextView.setText(book.getTacGia());
        priceTextView.setText(book.getGia());
    }
}