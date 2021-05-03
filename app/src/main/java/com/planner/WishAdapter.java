package com.planner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WishAdapter extends RecyclerView.Adapter<WishAdapter.WishViewHolder> {
    private List<Wish> wishes;

    public WishAdapter(List<Wish> wishes) {
        this.wishes = wishes;
    }

    @NonNull
    @Override
    public WishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WishViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.wishes_container, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WishViewHolder holder, int position) {
        holder.setWish(wishes.get(position));
    }

    @Override
    public int getItemCount() {
        return wishes.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class WishViewHolder extends RecyclerView.ViewHolder {

        TextView textTitle, textDesc, textPrice;

        public WishViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textDesc = itemView.findViewById(R.id.textDesc);
            textPrice = itemView.findViewById(R.id.textPrice);
        }

        void setWish(Wish wish) {
            textTitle.setText(wish.getTitle());
            if (wish.getDescription().trim().isEmpty()) {
                textDesc.setVisibility(View.GONE);
            } else {
                textDesc.setText(wish.getDescription());
            }
            textDesc.setText(wish.getDescription());
        }
    }
}
