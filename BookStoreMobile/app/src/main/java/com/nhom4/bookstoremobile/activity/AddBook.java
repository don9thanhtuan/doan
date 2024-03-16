package com.nhom4.bookstoremobile.activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.nhom4.bookstoremobile.R;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddBook extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;
    ImageView addBookImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        addBookImage = findViewById(R.id.addBookImage);

        final EditText i_1 = findViewById(R.id.add_name);
        final EditText i_2 = findViewById(R.id.add_price);
        final EditText i_3 = findViewById(R.id.add_author);
        final EditText i_4 = findViewById(R.id.add_publisher);
        final EditText i_5 = findViewById(R.id.add_weight);
        final EditText i_6 = findViewById(R.id.add_size);
        final EditText i_7 = findViewById(R.id.add_stock);
        final EditText i_8 = findViewById(R.id.add_introduction);

        findViewById(R.id.addBookImageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });

        findViewById(R.id.add_book_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(selectedImage);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                addBookImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
