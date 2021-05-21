package com.planner;

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

public class ViewTasksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tasks);

        ListView taskListView = findViewById(R.id.tasksListView);
        ImageView imageBack = findViewById(R.id.imageBackAllTasks);

        imageBack.setOnClickListener(v -> onBackPressed());

        List<String> tasks = new ArrayList<>();
        List<Task> taskList = new ArrayList<>();
        ArrayAdapter<String> taskAdapter = new ArrayAdapter<>(this, R.layout.wishes_container, tasks);
        taskListView.setAdapter(taskAdapter);

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("tasks");

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tasks.clear();
                for (DataSnapshot s : snapshot.getChildren()) {
                    Task t = s.getValue(Task.class);
                    String txt = t.getTitle() + "\n\nDeadline: " + t.getDate() + " " + t.getTime() + "\nPrice: " + t.getCost() + "\nDescription: " + t.getDescription();
                    tasks.add(txt);
                    taskList.add(t);
                }
                taskAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}