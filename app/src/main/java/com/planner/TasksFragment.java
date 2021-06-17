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

import de.hdodenhof.circleimageview.CircleImageView;

public class TasksFragment extends Fragment {

    private final String userId;
    private RecyclerView tasksList;
    private DatabaseReference tasksRef;
    private static final String TAG = "Tasks Fragment";

    TasksFragment(String userId) {
        this.userId = userId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View tasksView = inflater.inflate(R.layout.fragment_profile_tasks, container, false);
        tasksList = tasksView.findViewById(R.id.profile_tasks_recycle_view);
        tasksList.setLayoutManager(new LinearLayoutManager(getContext()));
        tasksRef = PlannerCostants.userRef.child(userId).child("taskIDs");
        tasksList.addItemDecoration(new DividerItemDecoration(tasksList.getContext(), DividerItemDecoration.VERTICAL));
        return tasksView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "enterOnStart");
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<String>().setQuery(tasksRef, String.class).build();
        FirebaseRecyclerAdapter<String, TasksFragment.tasksViewHolder> adapter = new FirebaseRecyclerAdapter<String, tasksViewHolder>(options) {
            @NonNull
            @Override
            public tasksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                Log.d(TAG, "onCreateViewHolder");
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_task_item, parent, false);
                return new tasksViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull tasksViewHolder tasksViewHolder, int i, @NonNull String s) {
                String taskId = s;
                Log.d(TAG, taskId);
                PlannerCostants.databaseReference.child("tasks").child(taskId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Task task = snapshot.getValue(Task.class);
                            String title = task.getTitle();
                            String time = task.getTime();
                            String date = task.getDate();
                            tasksViewHolder.profileTaskTitle.setText(title);
                            tasksViewHolder.profileTaskDeadline.setText(date + " " + time);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        };
        tasksList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class tasksViewHolder extends RecyclerView.ViewHolder {

        private final TextView profileTaskTitle, profileTaskDeadline;

        public tasksViewHolder(@NonNull View itemView) {
            super(itemView);
            profileTaskTitle = itemView.findViewById(R.id.profile_task_title);
            profileTaskDeadline = itemView.findViewById(R.id.profile_task_deadline);
        }
    }
}
