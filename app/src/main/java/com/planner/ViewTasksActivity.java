package com.planner;

import android.content.Intent;
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
    private ArrayList<Task> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tasks);

        ListView taskListView = findViewById(R.id.tasksListView);
        ImageView imageBack = findViewById(R.id.imageBackAllTasks);

        imageBack.setOnClickListener(v -> onBackPressed());

        tasks = new ArrayList<>();
        TasksViewCustomAdapter taskAdapter = new TasksViewCustomAdapter(ViewTasksActivity.this, tasks);
        taskListView.setAdapter(taskAdapter);
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("tasks");

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tasks.clear();

                for (DataSnapshot s : snapshot.getChildren()) {
                    Task t = s.getValue(Task.class);
                    if (t != null && t.getTitle() != null && !t.isDone()) {
                        tasks.add(t);
                    }
                    if (t == null || t.getTitle() == null) {
                        s.getRef().removeValue();
                    }
                }
                taskAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        taskListView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(view.getContext(), SetTaskCompletedActivity.class);
            intent.putExtra("title", tasks.get(position).getTitle());
            intent.putExtra("reward", tasks.get(position).getReward());
            intent.putExtra("desc", tasks.get(position).getDescription());
            tasks.get(position).setTitle(null);
            startActivityForResult(intent, RequestCodes.REQUEST_CODE_SET_TASK_COMPLETED);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RequestCodes.REQUEST_CODE_SET_TASK_COMPLETED) {
                boolean isOk = data.getBooleanExtra("isOk", false);
                if (isOk) {
                    String title = data.getStringExtra("title");
                    String desc = data.getStringExtra("desc");
                    int reward = data.getIntExtra("reward", 0);
                    Task tmp = new Task(title, desc, reward);
                    tasks.add(tmp);
                }
            }
        }
    }
}