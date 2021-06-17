package com.planner;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewOfferedTasksActivity extends AppCompatActivity {
    private ArrayList<Task> offeredTasks;
    private OfferedTasksViewCustomAdapter offeredTaskAdapter;
    private String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_offered_tasks);

        ImageView imageBack = findViewById(R.id.imageBackOfferedTasks);
        ListView offeredTaskListView = findViewById(R.id.offeredTasksListView);

        currentUserID = PlannerCostants.mAuth.getCurrentUser().getUid();
        offeredTasks = new ArrayList<>();
        offeredTaskAdapter = new OfferedTasksViewCustomAdapter(ViewOfferedTasksActivity.this, offeredTasks);
        offeredTaskListView.setAdapter(offeredTaskAdapter);

        imageBack.setOnClickListener(v -> onBackPressed());
        PlannerCostants.databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                offeredTasks.clear();
                for (DataSnapshot s : snapshot.child("users").child(currentUserID).child("offeredTaskIDs").getChildren()) {
                    String offeredTaskID = s.getValue(String.class);
                    Task t = snapshot.child("offeredTasks").child(offeredTaskID).getValue(Task.class);
                    if (t != null) {
                        offeredTasks.add(t);
                    } else {
                        s.getRef().removeValue();
                    }
                }
                offeredTaskAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}