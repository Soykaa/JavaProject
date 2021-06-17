package com.planner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class OfferedTasksViewCustomAdapter extends BaseAdapter implements ListAdapter {
    private final Context context;
    private final ArrayList<Task> offeredTaskList;

    public OfferedTasksViewCustomAdapter(Context context, ArrayList<Task> offeredTaskList) {
        this.context = context;
        this.offeredTaskList = offeredTaskList;
    }

    @Override
    public int getCount() {
        return offeredTaskList.size();
    }

    @Override
    public Object getItem(int pos) {
        return offeredTaskList.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.offered_tasks_list_view_layout, null);
        }

        TextView parentName = view.findViewById(R.id.parentName);
        TextView taskDesc = view.findViewById(R.id.taskDesc);
        Task t = offeredTaskList.get(position);
        PlannerCostants.userRef.child(t.getParentId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String pName = snapshot.child("name").getValue().toString();
                parentName.setText("Author: " + pName);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        String text = "\n\nTitle: " + t.getTitle() + "\nReward: " + t.getReward() + "\nDeadline:\n" + t.getDate() + " " + t.getTime();
        taskDesc.setText(text);

        Button accept = view.findViewById(R.id.button_accept_task);
        Button decline = view.findViewById(R.id.button_decline_task);

        decline.setOnClickListener(v -> {
            remove(position);
            Toast.makeText(context, "Task is declined", Toast.LENGTH_LONG).show();
        });

        accept.setOnClickListener(v -> {
            remove(position);
            DatabaseReference taskRef = PlannerCostants.databaseReference.child("tasks").push();
            taskRef.setValue(t);
            String currentUserID = PlannerCostants.mAuth.getCurrentUser().getUid();
            PlannerCostants.databaseReference.child("users").child(currentUserID).child("taskIDs").push().setValue(taskRef.getKey());
            Toast.makeText(context, "Task is accepted", Toast.LENGTH_LONG).show();
        });
        return view;
    }

    private void remove(int position) {
        DatabaseReference databaseReference = PlannerCostants.databaseReference.child("offeredTasks")
                .child(offeredTaskList.get(position).getId());
        databaseReference.removeValue();
    }
}
