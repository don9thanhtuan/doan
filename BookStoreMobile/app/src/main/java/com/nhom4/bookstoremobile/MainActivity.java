package com.nhom4.bookstoremobile;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.nhom4.bookstoremobile.activity.AddBook;
import com.nhom4.bookstoremobile.activity.ViewBookList;

public class MainActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;
    ImageView addBookImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.view_book_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewBookList.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.add_book_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddBook.class);
                startActivity(intent);
            }
        });
    }
}
