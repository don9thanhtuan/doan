package com.nhom4.bookstoremobile.controller;

import android.content.Intent;
import android.net.Uri;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.nhom4.bookstoremobile.R;
import com.nhom4.bookstoremobile.activity.EditBook;
import com.nhom4.bookstoremobile.activity.ViewBookDetails;
import com.nhom4.bookstoremobile.entities.Book;
import com.nhom4.bookstoremobile.retrofit.RetrofitAPI;
import com.nhom4.bookstoremobile.service.BookService;
import com.nhom4.bookstoremobile.service.ExceptionHandler;

import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditBookController {
    private final EditBook view;
    boolean changeImage = false;
    private ImageView imagePreview;
    private Uri selectedImage;
    private final Book book;

    public EditBookController(EditBook view) {
        this.view = view;
        book = getDataFromIntent();
        imagePreview = view.findViewById(R.id.imagePreview);
        setUpLayout();
    }

    public Uri getSelectedImage() {
        return selectedImage;
    }

    public void setSelectedImage(Uri selectedImage) {
        this.selectedImage = selectedImage;
    }

    public boolean isChangeImage() {
        return changeImage;
    }

    public void setChangeImage(boolean changeImage) {
        this.changeImage = changeImage;
    }

    public ImageView getImagePreview() {
        return imagePreview;
    }

    public void setImagePreview(ImageView imagePreview) {
        this.imagePreview = imagePreview;
    }

    private Book getDataFromIntent() {
        String book_ID = view.getIntent().getStringExtra("book_id");
        String book_Name = view.getIntent().getStringExtra("book_name");
        String book_HinhAnh = view.getIntent().getStringExtra("book_HinhAnh");
        String book_TacGia = view.getIntent().getStringExtra("book_TacGia");
        String book_NhaCungCap = view.getIntent().getStringExtra("book_NhaCungCap");
        int book_TonKho = view.getIntent().getIntExtra("book_TonKho", 0);
        String book_GiaR = view.getIntent().getStringExtra("book_Gia");
        double book_TrongLuong = view.getIntent().getDoubleExtra("book_TrongLuong", 0.0);
        String book_KickThuoc = view.getIntent().getStringExtra("book_KickThuoc");
        String book_GioiThieu = view.getIntent().getStringExtra("book_GioiThieu");

        return new Book(book_ID, book_Name, book_HinhAnh, book_TacGia, book_NhaCungCap, book_TonKho, book_GiaR, book_TrongLuong, book_KickThuoc, book_GioiThieu);
    }

    private void setUpLayout() {
        TextView addBook = view.findViewById(R.id.titleTxtView);
        addBook.setText("Chỉnh sửa sản phẩm");

        Button saveBtn = view.findViewById(R.id.addBookBtn);
        saveBtn.setText("Lưu");
    }

    public void editBookByAPI() {
        Book newBook = new ExceptionHandler().handleExceptionBook(view);
        if (newBook == null) {
            return;
        }
        newBook.setId(book.getId());
        String imageURL = book.getHinhAnh().replace("http://10.0.2.2:8080", "");
        newBook.setHinhAnh(imageURL);

        MultipartBody.Part imagePart;

        if (changeImage) {
            imagePart = prepareFilePart(selectedImage);
        } else {
            RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), "");
            imagePart = MultipartBody.Part.createFormData("image", "", requestBody);;
        }

        BookService bookService = RetrofitAPI.getInstance().create(BookService.class);
        Call<String> call = bookService.editBook(newBook.getId(), imagePart, newBook);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {redirectToCart();}
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {redirectToCart();}
        });
    }

    private MultipartBody.Part prepareFilePart(Uri uri) {
        try {
            InputStream inputStream = view.getContentResolver().openInputStream(uri);
            byte[] fileBytes = new byte[inputStream.available()];
            inputStream.read(fileBytes);
            RequestBody requestFile = RequestBody.create(MediaType.parse(view.getContentResolver().getType(uri)), fileBytes);
            return MultipartBody.Part.createFormData("image", "image.jpg", requestFile);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setBookData() {
        EditText nameEditText = view.findViewById(R.id.add_name);
        EditText priceEditText = view.findViewById(R.id.add_price);
        EditText authorEditText = view.findViewById(R.id.add_author);
        EditText publisherEditText = view.findViewById(R.id.add_publisher);
        EditText weightEditText = view.findViewById(R.id.add_weight);
        EditText sizeEditText = view.findViewById(R.id.add_size);
        EditText stockEditText = view.findViewById(R.id.add_stock);
        EditText introductionEditText = view.findViewById(R.id.add_introduction);

        Glide.with(view)
                .load(book.getHinhAnh())
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .fitCenter()
                .into(imagePreview);

        nameEditText.setText(book.getTen());

        String priceRaw = book.getGia();
        String price = priceRaw.replaceAll("[^0-9]", "");
        priceEditText.setText(price);

        authorEditText.setText(book.getTacGia());
        publisherEditText.setText(book.getNhaCungCap());
        weightEditText.setText(String.valueOf(book.getTrongLuong()));
        sizeEditText.setText(book.getKichThuoc());
        stockEditText.setText(String.valueOf(book.getTonKho()));
        introductionEditText.setText(book.getGioiThieu());
    }

    private void redirectToCart() {
        Toast.makeText(view, "Chỉnh sửa thành công", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(view, ViewBookDetails.class);
        intent.putExtra("book_id", book.getId());
        view.startActivity(intent);
        view.finish();
    }

    public void redirectBack() {
        Intent intent = new Intent(view, ViewBookDetails.class);
        intent.putExtra("book_id", book.getId());
        view.startActivity(intent);
        view.finish();
    }
}
