package com.planner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class WishesViewCustomAdapter extends BaseAdapter implements ListAdapter {
    private final Context context;
    private final ArrayList<Wish> wishList;

    public WishesViewCustomAdapter(Context context, ArrayList<Wish> wishList) {
        this.context = context;
        this.wishList = wishList;
    }

    @Override
    public int getCount() {
        return wishList.size();
    }

    @Override
    public Object getItem(int pos) {
        return wishList.get(pos);
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
        Wish w = wishList.get(position);
        String text = Objects.requireNonNull(w).getTitle() + "\n\nPrice: " + w.getCost();
        tvContact.setText(text);
        ImageView imageDelete = view.findViewById(R.id.deleteImage);

        imageDelete.setOnClickListener(v -> {
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            DatabaseReference databaseReference = database.child("wishes").child(wishList.get(position).getId());
            databaseReference.removeValue();
            Toast.makeText(context, "Wish is deleted", Toast.LENGTH_LONG).show();
        });
        return view;
    }
}
