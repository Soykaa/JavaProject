package com.planner;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewWishActivity extends AppCompatActivity {
    private boolean inputIsCorrect(EditText title, EditText cost, EditText desc) {
        boolean flag = true;
        if (TextUtils.isEmpty(title.getText().toString())) {
            title.setError("You did not enter a title");
            flag = false;
        }

        if (TextUtils.isEmpty(cost.getText().toString())) {
            cost.setError("You did not enter a cost");
            flag = false;
        } else {
            if (Integer.parseInt(cost.getText().toString()) < 20) {
                cost.setError("Cost is too small");
                flag = false;
            }
        }

        if (TextUtils.isEmpty(desc.getText().toString())) {
            desc.setError("You did not enter a description");
            flag = false;
        }
        return flag;
    }

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

            if (!inputIsCorrect(title, cost, description)) {
                return;
            }

            FirebaseAuth mAuth = FirebaseAuth.getInstance();

            DatabaseReference wishRef = PlannerCostants.databaseReference.child("wishes").push();
            Wish wish = new Wish(title.getText().toString(),
                    Integer.parseInt(cost.getText().toString()),
                    description.getText().toString(), wishRef.getKey());
            wishRef.setValue(wish);

            String currentUserID = mAuth.getCurrentUser().getUid();
            PlannerCostants.databaseReference.child("users").child(currentUserID).child("wishIDs").push().setValue(wishRef.getKey());

            finish();
        });
    }
}