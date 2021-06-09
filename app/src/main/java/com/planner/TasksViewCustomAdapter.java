package com.planner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TasksViewCustomAdapter extends BaseAdapter implements ListAdapter {
    private final Context context;
    private final ArrayList<Task> unfilteredTaskList;
    private ArrayList<Task> filteredTaskList;
    private final ItemFilter filter = new ItemFilter();

    public TasksViewCustomAdapter(Context context, ArrayList<Task> taskList) {
        this.context = context;
        unfilteredTaskList = taskList;
        filteredTaskList = taskList;
    }

    public ArrayList<Task> getFilteredTaskList() {
        return filteredTaskList;
    }

    @Override
    public int getCount() {
        return filteredTaskList.size();
    }

    @Override
    public Object getItem(int pos) {
        return filteredTaskList.get(pos);
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
        Task t = filteredTaskList.get(position);
        String text = t.getTitle() + "\n\nReward: " + t.getReward() + "\nDeadline: " + t.getDate() + " " + t.getTime();
        tvContact.setText(text);

        return view;
    }

    public ItemFilter getFilter() {
        return filter;
    }

    class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            final List<Task> list = unfilteredTaskList;
            int count = list.size();
            final ArrayList<Task> filteredList = new ArrayList<>(count);

            for (int i = 0; i < count; i++) {
                Task t = list.get(i);
                String title = t.getTitle();
                if (title.toLowerCase().contains(filterString)) {
                    filteredList.add(t);
                }
            }

            results.values = filteredList;
            results.count = filteredList.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredTaskList = (ArrayList<Task>) results.values;
            notifyDataSetChanged();
        }
    }
}