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
    private String parentName;

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

        TextView tvContact = view.findViewById(R.id.tvContact);
        Task t = offeredTaskList.get(position);
        PlannerCostants.userRef.child(t.getParentId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                parentName = snapshot.child("name").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        String text = t.getTitle() + "\nAuthor:" + parentName + "\n\nReward: " + t.getReward() + "\nDeadline:\n" + t.getDate() + " " + t.getTime();
        tvContact.setText(text);

        Button accept = view.findViewById(R.id.button_accept_task);
        Button decline = view.findViewById(R.id.button_decline_task);

        decline.setOnClickListener(v -> {
            remove(position);
            Toast.makeText(context, "Task is declined", Toast.LENGTH_LONG).show();
        });

        accept.setOnClickListener(v -> {
            DatabaseReference taskRef = PlannerCostants.databaseReference.child("tasks").push();
            taskRef.setValue(t);
            String currentUserID = PlannerCostants.mAuth.getCurrentUser().getUid();
            PlannerCostants.databaseReference.child("users").child(currentUserID).child("taskIDs").push().setValue(taskRef.getKey());
            remove(position);
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
