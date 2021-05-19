package com.planner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WishActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish);

        ImageView imageAddWish = findViewById(R.id.imageAdd);
        ListView wishListView = findViewById(R.id.wishesListView);
        ImageView imageBack = findViewById(R.id.imageBackAllWishes);

        imageBack.setOnClickListener(v -> onBackPressed());
        imageAddWish.setOnClickListener(v -> startActivityForResult(
                new Intent(getApplicationContext(), NewWishActivity.class), RequestCodes.REQUEST_CODE_ADD_WISH));

        ArrayList<Wish> wishes = new ArrayList<>();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        MyCustomAdapter wishAdapter = new MyCustomAdapter(WishActivity.this, wishes);
        wishListView.setAdapter(wishAdapter);
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        String currentUserID = mAuth.getCurrentUser().getUid();

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                wishes.clear();

                for (DataSnapshot s : snapshot.child("users").child(currentUserID).child("wishIDs").getChildren()) {
                    String wishID = s.getValue(String.class);
                    Wish w = snapshot.child("wishes").child(wishID).getValue(Wish.class);
                    if (w != null) {
                        wishes.add(w);
                    } else {
                        s.getRef().removeValue();
                    }
                }
                wishAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}