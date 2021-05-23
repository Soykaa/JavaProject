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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class FeedFragment extends Fragment {

    private View feedView;
    private RecyclerView recyclerView;
    private DatabaseReference userRef, databaseReference;
    private Query doneTasksRef;
    private FirebaseAuth mAuth;
    private String currentUserId;
    private static final String TAG = "Feed Fragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        feedView = inflater.inflate(R.layout.fragment_feed, container, false);
        recyclerView = feedView.findViewById(R.id.feed_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        userRef = databaseReference.child("users");
        doneTasksRef = databaseReference.child("doneTasks").orderByChild("timestamp");
        return feedView;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<DoneTask>().setQuery(doneTasksRef, DoneTask.class).build();
        FirebaseRecyclerAdapter<DoneTask, FeedFragment.tasksViewHolder> adapter = new FirebaseRecyclerAdapter<DoneTask, tasksViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull tasksViewHolder tasksViewHolder, int i, @NonNull DoneTask s) {
                String userId = s.getOwner();
                userRef.child(currentUserId).child("friends").child(userId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            userRef.child(userId).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String name = snapshot.child("name").getValue().toString();
                                    Log.d(TAG, "owner " + name);
                                    String imageId = snapshot.child("profileImage").getValue().toString();
                                    tasksViewHolder.userName.setText(name);
                                    Picasso.get()
                                            .load(imageId)
                                            .placeholder(R.drawable.ic_launcher_foreground)
                                            .into(tasksViewHolder.userImage);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            databaseReference.child("tasks").child(getRef(i).getKey()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot innerSnapshot) {
                                    if (innerSnapshot.exists()) {
                                        Task task = innerSnapshot.getValue(Task.class);
                                        Log.d(TAG, "title " + task.getTitle());
                                        tasksViewHolder.taskTitle.setText(task.getTitle());
                                        tasksViewHolder.taskDescription.setText(task.getDescription());
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        } else {
                            tasksViewHolder.itemView.getLayoutParams().height = 0;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @NonNull
            @Override
            public tasksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.done_task_item, parent, false);
                FeedFragment.tasksViewHolder viewHolder = new FeedFragment.tasksViewHolder(view);
                return viewHolder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


    public static class tasksViewHolder extends RecyclerView.ViewHolder {

        private final TextView taskTitle, taskDescription, userName;
        private final CircleImageView userImage;

        public tasksViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.task_user_name);
            userImage = itemView.findViewById(R.id.task_user_image);
            taskTitle = itemView.findViewById(R.id.user_task_title);
            taskDescription = itemView.findViewById(R.id.user_task_description);
        }
    }
}
