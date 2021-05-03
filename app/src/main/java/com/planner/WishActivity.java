package com.planner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

        imageAddWish.setOnClickListener(v -> startActivityForResult(
                new Intent(getApplicationContext(), NewWishActivity.class), RequestCodes.REQUEST_CODE_ADD_WISH));

        List<String> wishes = new ArrayList<>();
        ArrayAdapter<String> wishAdapter = new ArrayAdapter<>(this, R.layout.wishes_container, wishes);
        wishListView.setAdapter(wishAdapter);

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("wishes");
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                wishes.clear();
                for (DataSnapshot s : snapshot.getChildren()) {
                    ArrayList<String> list = new ArrayList<>();
                    for (DataSnapshot ss : s.getChildren()) {
                        list.add(Objects.requireNonNull(ss.getValue()).toString());
                    }
                    wishes.add(list.get(2));
                }
                wishAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}