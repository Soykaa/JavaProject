package com.planner;

import android.os.Bundle;
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

public class ViewTasksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tasks);

        ListView taskListView = findViewById(R.id.tasksListView);
        ImageView imageBack = findViewById(R.id.imageBackAllTasks);

        imageBack.setOnClickListener(v -> onBackPressed());

        ArrayList<Task> tasks = new ArrayList<>();
        TasksViewCustomAdapter taskAdapter = new TasksViewCustomAdapter(ViewTasksActivity.this, tasks);
        taskListView.setAdapter(taskAdapter);
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("tasks");

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tasks.clear();

                for (DataSnapshot s : snapshot.getChildren()) {
                    Task t = s.getValue(Task.class);
                    if (t != null) {
                        tasks.add(t);
                    }
                }
                taskAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}