package com.planner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class SetWishCompletedActivity extends AppCompatActivity {
    private String title;
    private String desc;
    private int cost;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_wish_completed);
        ImageView imageSetWishCompleted = findViewById(R.id.imageChangeStatusWish);
        ImageView imageBack = findViewById(R.id.imageBackSetWishCompleted);

        title = getIntent().getStringExtra("title");
        desc = getIntent().getStringExtra("desc");
        cost = getIntent().getIntExtra("cost", 0);
        pos = getIntent().getIntExtra("pos", 0);

        TextView wishTitle = findViewById(R.id.titleWish);
        wishTitle.setText(title);

        TextView wishDesc = findViewById(R.id.descWish);
        wishDesc.setText(desc);

        TextView wishCost = findViewById(R.id.costWish);
        wishCost.setText("cost: " + cost);

        String userID = PlannerCostants.mAuth.getCurrentUser().getUid();
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(userID)
                .child("points");
        final Long[] points = new Long[1];
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                points[0] = snapshot.getValue(Long.class);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {}
        });

        imageBack.setOnClickListener(v -> onBackPressed());
        imageSetWishCompleted.setOnClickListener(v -> {
           if (points[0] >= cost) {
                Intent intent = new Intent();
                intent.putExtra("isOk", true);
                intent.putExtra("title", title);
                intent.putExtra("desc", desc);
                intent.putExtra("cost", cost);
                intent.putExtra("pos", pos);
                setResult(RESULT_OK, intent);
                subtractCostPoints(cost);
                finish();
            }
            else {
                wishCost.setError("You don't have enough points");
            }
        });
    }

    private void subtractCostPoints(int cost) {
        String userID = PlannerCostants.mAuth.getCurrentUser().getUid();
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(userID)
                .child("points");
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Long points = snapshot.getValue(Long.class);
                databaseRef.setValue(points - cost);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {}
        });
    }
}
