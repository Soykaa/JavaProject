package com.planner;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class WishActivity extends AppCompatActivity {

    private RecyclerView wishRecyclerView;
    private List<Wish> wishes;
    private WishAdapter wishAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish);

        ImageView imageAddWish = findViewById(R.id.imageAdd);
        imageAddWish.setOnClickListener(v -> startActivityForResult(
                new Intent(getApplicationContext(), NewWishActivity.class), RequestCodes.REQUEST_CODE_ADD_WISH));

        wishRecyclerView = findViewById(R.id.wishesRecyclerView);
        wishRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        wishes = new ArrayList<>();
        wishAdapter = new WishAdapter(wishes);
        wishRecyclerView.setAdapter(wishAdapter);
        //getWishes();
    }

    private void getWishes() {
        @SuppressLint("StaticFieldLeak")
        class GetWishesTask extends AsyncTask<Void, Void, List<Wish>> {

            @Override
            protected List<Wish> doInBackground(Void... voids) {
                //TODO
                return null;
            }

            @Override
            protected void onPostExecute(List<Wish> wishList) {
                super.onPostExecute(wishList);
                if (wishes.size() == 0) {
                    wishes.addAll(wishList);
                    wishAdapter.notifyDataSetChanged();
                } else {
                    wishes.add(0, wishList.get(0));
                    wishAdapter.notifyItemInserted(0);
                }
                wishRecyclerView.smoothScrollToPosition(0);
            }
        }
        new GetWishesTask().execute();
    }
}