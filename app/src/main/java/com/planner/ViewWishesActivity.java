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

public class ViewWishesActivity extends AppCompatActivity {
    ArrayList<Wish> wishes;
    WishesViewCustomAdapter wishAdapter;

    private void makeArchived(int position, Wish tmp) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference wishRef = database.child("archivedWishes").push();
        wishRef.setValue(tmp);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String currentUserID = mAuth.getCurrentUser().getUid();
        database.child("users").child(currentUserID).child("archivedWishesIDs").push().setValue(wishRef.getKey());
        DatabaseReference databaseReference = database.child("wishes").child(wishes.get(position).getId());
        databaseReference.removeValue();
        wishAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_wishes);

        ImageView imageAddWish = findViewById(R.id.imageAdd);
        ListView wishListView = findViewById(R.id.wishesListView);
        ImageView imageBack = findViewById(R.id.imageBackAllWishes);

        imageBack.setOnClickListener(v -> onBackPressed());
        imageAddWish.setOnClickListener(v -> startActivityForResult(
                new Intent(this, NewWishActivity.class), RequestCodes.REQUEST_CODE_ADD_WISH));

        wishes = new ArrayList<>();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        wishAdapter = new WishesViewCustomAdapter(ViewWishesActivity.this, wishes);
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

        wishListView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(view.getContext(), SetWishCompletedActivity.class);
            Wish wish = wishes.get(position);
            intent.putExtra("title", wish.getTitle());
            intent.putExtra("cost", wish.getCost());
            intent.putExtra("desc", wish.getDescription());
            intent.putExtra("pos", position);
            wishAdapter.notifyDataSetChanged();
            startActivityForResult(intent, RequestCodes.REQUEST_CODE_SET_WISH_COMPLETED);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RequestCodes.REQUEST_CODE_SET_WISH_COMPLETED) {
                boolean isOk = data.getBooleanExtra("isOk", false);
                if (isOk) {
                    String title = data.getStringExtra("title");
                    String desc = data.getStringExtra("desc");
                    int cost = data.getIntExtra("cost", 0);
                    int position = data.getIntExtra("pos", -1);
                    Wish tmp = new Wish(title,cost, desc, "");
                    makeArchived(position, tmp);
                }
            }
        }
    }
}

