package com.planner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TasksViewCustomAdapter extends BaseAdapter implements ListAdapter {
    private final Context context;
    private final ArrayList<Task> taskList;

    public TasksViewCustomAdapter(Context context, ArrayList<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    @Override
    public int getCount() {
        return taskList.size();
    }

    @Override
    public Object getItem(int pos) {
        return taskList.get(pos);
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
            view = inflater.inflate(R.layout.custom_list_view_layout, null);
        }

        TextView tvContact = view.findViewById(R.id.tvContact);
        Task t = taskList.get(position);
        String text = t.getTitle() + "\n\nReward: " + t.getReward() + "\nDeadline: " + t.getDate() + " " + t.getTime();
        tvContact.setText(text);

        return view;
    }
}
