package com.planner;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewWishActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_wish);

        ImageView imageBack = findViewById(R.id.imageBackWish);
        imageBack.setOnClickListener(v -> onBackPressed());

        ImageView saveButton = findViewById(R.id.imageSaveWish);
        saveButton.setOnClickListener(v -> {
            EditText title = findViewById(R.id.inputTitleWish);
            EditText cost = findViewById(R.id.priceTextWish);
            EditText description = findViewById(R.id.inputDescWish);
            Wish wish = new Wish(title.getText().toString(),
                    cost.getText().toString(),
                    description.getText().toString());
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            database.child("wishes").push().setValue(wish);
            finish();
        });
    }
}