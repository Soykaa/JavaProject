package com.planner;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class WishesFragment extends Fragment {

    private final String userId;
    private RecyclerView wishesList;
    private DatabaseReference wishesRef;
    private static final String TAG = "Wishes Fragment";


    WishesFragment(String userId) {
        this.userId = userId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View wishesView = inflater.inflate(R.layout.fragment_profile_wishes, container, false);
        wishesList = wishesView.findViewById(R.id.profile_wishes_recycle_view);
        wishesList.setLayoutManager(new LinearLayoutManager(getContext()));
        wishesRef = PlannerCostants.userRef.child(userId).child("wishIDs");
        wishesList.addItemDecoration(new DividerItemDecoration(wishesList.getContext(), DividerItemDecoration.VERTICAL));
        return wishesView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "enterOnStart");
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<String>().setQuery(wishesRef, String.class).build();
        FirebaseRecyclerAdapter<String, WishesFragment.wishesViewHolder> adapter = new FirebaseRecyclerAdapter<String, WishesFragment.wishesViewHolder>(options) {
            @NonNull
            @Override
            public WishesFragment.wishesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                Log.d(TAG, "onCreateViewHolder");
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_wish_item, parent, false);
                return new WishesFragment.wishesViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull WishesFragment.wishesViewHolder wishesViewHolder, int i, @NonNull String s) {
                String wishId = s;
                Log.d(TAG, wishId);
                PlannerCostants.databaseReference.child("wishes").child(wishId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Wish wish = snapshot.getValue(Wish.class);
                            String title = wish.getTitle();
                            String description = wish.getDescription();
                            wishesViewHolder.profileTaskTitle.setText(title);
                            wishesViewHolder.profileTaskDescription.setText(description);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        };
        wishesList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class wishesViewHolder extends RecyclerView.ViewHolder {

        private final TextView profileTaskTitle, profileTaskDescription;

        public wishesViewHolder(@NonNull View itemView) {
            super(itemView);
            profileTaskTitle = itemView.findViewById(R.id.profile_wish_title);
            profileTaskDescription = itemView.findViewById(R.id.profile_wish_description);
        }
    }

}
