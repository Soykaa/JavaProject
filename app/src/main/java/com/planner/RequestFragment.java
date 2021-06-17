package com.planner;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestFragment extends Fragment {

    private RecyclerView requestsList;
    private DatabaseReference requestsRef;
    private String currentUserId;
    private static final String TAG = "Request Fragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View requestsView = inflater.inflate(R.layout.fragment_request, container, false);
        requestsList = requestsView.findViewById(R.id.request_recycle_view);
        requestsList.setLayoutManager(new LinearLayoutManager(getContext()));
        currentUserId = PlannerCostants.mAuth.getCurrentUser().getUid();
        requestsRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId).child("requests");
        return requestsView;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Boolean>().setQuery(requestsRef, Boolean.class).build();
        FirebaseRecyclerAdapter<Boolean, RequestsViewHolder> adapter = new FirebaseRecyclerAdapter<Boolean, RequestsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RequestsViewHolder requestsViewHolder, int i, @NonNull Boolean user) {
                String userId = getRef(i).getKey();
                PlannerCostants.userRef.child(userId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String name = snapshot.child("name").getValue().toString();
                            String imageId = snapshot.child("profileImage").getValue().toString();
                            requestsViewHolder.userName.setText(name);
                            Picasso.get()
                                    .load(imageId)
                                    .placeholder(R.drawable.ic_launcher_foreground)
                                    .into(requestsViewHolder.userImage);
                            requestsViewHolder.acceptRequest.setOnClickListener(v -> {
                                Log.d(TAG, "Accept request from " + name);
                                Log.d(TAG, "Friend's id: " + userId);
                                Log.d(TAG, "My id " + currentUserId);
                                PlannerCostants.userRef.child(currentUserId).child("friends").child(userId).setValue(true);
                                PlannerCostants.userRef.child(currentUserId).child("requests").child(userId).setValue(null);
                                PlannerCostants.userRef.child(userId).child("friends").child(currentUserId).setValue(true);
                                PlannerCostants.userRef.child(userId).child("requests").child(currentUserId).setValue(null);
                            });
                            requestsViewHolder.declineRequest.setOnClickListener(v -> {
                                Log.d(TAG, "Decline request from " + name);
                                PlannerCostants.userRef.child(currentUserId).child("requests").child(userId).setValue(null);
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
            }

            @NonNull
            @Override
            public RequestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_item, parent, false);
                RequestsViewHolder viewHolder = new RequestsViewHolder(view);
                return viewHolder;
            }
        };
        requestsList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class RequestsViewHolder extends RecyclerView.ViewHolder {

        private final TextView userName;
        private final CircleImageView userImage;
        private final Button acceptRequest;
        private final Button declineRequest;

        public RequestsViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.request_name);
            userImage = itemView.findViewById(R.id.request_image);
            acceptRequest = itemView.findViewById(R.id.button_accept_request);
            declineRequest = itemView.findViewById(R.id.button_decline_request);
        }
    }


}
