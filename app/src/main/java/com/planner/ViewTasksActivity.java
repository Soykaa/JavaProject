package com.planner;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewTasksActivity extends AppCompatActivity {
    private ArrayList<Task> tasks;
    private TasksViewCustomAdapter taskAdapter;
    private String currentUserID;
    private static final String TAG = "ViewTasksActivity";

    private void makeCompleted(CompletedTask tmp) {
        Log.d(TAG, "makeCompleted");
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("completedTasks").child(tmp.getId()).setValue(tmp);
        database.child("users").child(currentUserID).child("completedTaskIDs").push().setValue(tmp.getId());
        Query tasksQuery = database.child("users").child(currentUserID).child("taskIDs").orderByValue().equalTo(tmp.getId());
        tasksQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "enter remove " + dataSnapshot.toString());
                for (DataSnapshot taskSnapshot : dataSnapshot.getChildren()) {
                    Log.e(TAG, "remove " + taskSnapshot.toString());
                    taskSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });
        taskAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tasks);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        ImageView imageAddWish = findViewById(R.id.imageAddTask);
        ListView taskListView = findViewById(R.id.tasksListView);
        ImageView imageBack = findViewById(R.id.imageBackAllTasks);
        EditText tasksSearch = findViewById(R.id.inputSearchTasks);

        imageBack.setOnClickListener(v -> onBackPressed());
        imageAddWish.setOnClickListener(v -> startActivityForResult(
                new Intent(this, NewTaskActivity.class), RequestCodes.REQUEST_CODE_ADD_TASK));

        tasks = new ArrayList<>();
        taskAdapter = new TasksViewCustomAdapter(ViewTasksActivity.this, tasks);
        taskListView.setAdapter(taskAdapter);
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

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
            tasks = taskAdapter.getFilteredTaskList();
            Intent intent = new Intent(view.getContext(), SetTaskCompletedActivity.class);
            intent.putExtra("title", tasks.get(position).getTitle());
            intent.putExtra("reward", tasks.get(position).getReward());
            intent.putExtra("desc", tasks.get(position).getDescription());
            intent.putExtra("pos", position);
            taskAdapter.notifyDataSetChanged();
            startActivityForResult(intent, RequestCodes.REQUEST_CODE_SET_TASK_COMPLETED);
        });
        tasksSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                (ViewTasksActivity.this).taskAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RequestCodes.REQUEST_CODE_SET_TASK_COMPLETED) {
                boolean isOk = data.getBooleanExtra("isOk", false);
                if (isOk) {
                    int position = data.getIntExtra("pos", -1);
                    String uploadId = data.getStringExtra("uploadId");
                    CompletedTask tmp = new CompletedTask(tasks.get(position).getId(), currentUserID, ServerValue.TIMESTAMP, uploadId);
                    makeCompleted(tmp);
                }
            }
        }
    }
}