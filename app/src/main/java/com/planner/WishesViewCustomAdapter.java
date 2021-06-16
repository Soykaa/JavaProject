package com.planner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WishesViewCustomAdapter extends BaseAdapter implements ListAdapter {
    private final Context context;
    private final ArrayList<Wish> unfilteredWishList;
    private ArrayList<Wish> filteredWishList;
    private final ItemFilter filter = new ItemFilter();

    public WishesViewCustomAdapter(Context context, ArrayList<Wish> wishList) {
        this.context = context;
        unfilteredWishList = wishList;
        filteredWishList = wishList;
    }

    @Override
    public int getCount() {
        return filteredWishList.size();
    }

    @Override
    public Object getItem(int pos) {
        return filteredWishList.get(pos);
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
        Wish w = filteredWishList.get(position);
        String text = Objects.requireNonNull(w).getTitle() + "\n\nPrice: " + Integer.toString(w.getCost());
        tvContact.setText(text);
        ImageView imageDelete = view.findViewById(R.id.deleteImage);

        imageDelete.setOnClickListener(v -> {
            DatabaseReference databaseReference = PlannerCostants.databaseReference.child("wishes")
                    .child(filteredWishList.get(position).getId());
            databaseReference.removeValue();
            Toast.makeText(context, "Wish is deleted", Toast.LENGTH_LONG).show();
        });
        return view;
    }

    public ItemFilter getFilter() {
        return filter;
    }

    public ArrayList<Wish> getFilteredWishList() {
        return filteredWishList;
    }

    class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            final List<Wish> list = unfilteredWishList;
            int count = list.size();
            final ArrayList<Wish> filteredList = new ArrayList<>(count);

            for (int i = 0; i < count; i++) {
                Wish w = list.get(i);
                String title = w.getTitle();
                if (title.toLowerCase().contains(filterString)) {
                    filteredList.add(w);
                }
            }

            results.values = filteredList;
            results.count = filteredList.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredWishList = (ArrayList<Wish>) results.values;
            notifyDataSetChanged();
        }
    }
}
