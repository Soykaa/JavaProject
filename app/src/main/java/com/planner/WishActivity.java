package com.planner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

        List<String> wishes = new ArrayList<>();
        List<Wish> wishList = new ArrayList<>();
        ArrayAdapter<String> wishAdapter = new ArrayAdapter<>(this, R.layout.wishes_container, wishes);
        wishListView.setAdapter(wishAdapter);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String currentUserID = mAuth.getCurrentUser().getUid();

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        //DatabaseReference wishesRef = FirebaseDatabase.getInstance().getReference().child("wishes");
        //DatabaseReference userWishesIDRef = FirebaseDatabase.getInstance().getReference().
                                            //child("users").child(currentUserID).child("wishIDs");


        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                wishes.clear();
                for (DataSnapshot s : snapshot.child("users").child(currentUserID).child("wishIDs").getChildren()) {
                    String wishID = s.getValue(String.class);
                    Wish w = snapshot.child("wishes").child(wishID).getValue(Wish.class);

                    String txt = Objects.requireNonNull(w).getTitle() + "\nPrice: " + w.getPrice();
                    wishes.add(txt);
                    wishList.add(w);
                }
                wishAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
}
}