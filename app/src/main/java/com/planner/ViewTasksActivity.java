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

public class ViewTasksActivity extends AppCompatActivity {
    private ArrayList<Task> tasks;
    private TasksViewCustomAdapter taskAdapter;

    private void makeCompleted(int position, Task tmp) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference taskRef = database.child("completedTasks").push();
        taskRef.setValue(tmp);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String currentUserID = mAuth.getCurrentUser().getUid();
        database.child("users").child(currentUserID).child("completedTaskIDs").push().setValue(taskRef.getKey());
        DatabaseReference databaseReference = database.child("tasks").child(tasks.get(position).getId());
        databaseReference.removeValue();
        taskAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tasks);

        ImageView imageAddWish = findViewById(R.id.imageAddTask);
        ListView taskListView = findViewById(R.id.tasksListView);
        ImageView imageBack = findViewById(R.id.imageBackAllTasks);

        imageBack.setOnClickListener(v -> onBackPressed());
        imageAddWish.setOnClickListener(v -> startActivityForResult(
                new Intent(this, NewTaskActivity.class), RequestCodes.REQUEST_CODE_ADD_TASK));

        tasks = new ArrayList<>();
        taskAdapter = new TasksViewCustomAdapter(ViewTasksActivity.this, tasks);
        taskListView.setAdapter(taskAdapter);
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String currentUserID = mAuth.getCurrentUser().getUid();

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tasks.clear();

                for (DataSnapshot s : snapshot.child("users").child(currentUserID).child("taskIDs").getChildren()) {
                    String taskID = s.getValue(String.class);
                    Task t = snapshot.child("tasks").child(taskID).getValue(Task.class);
                    if (t != null) {
                        tasks.add(t);
                    } else {
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
            intent.putExtra("pos", position);
            taskAdapter.notifyDataSetChanged();
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
                    int position = data.getIntExtra("pos", -1);
                    Task tmp = new Task(title, desc, reward);
                    makeCompleted(position, tmp);
                }
            }
        }
    }
}