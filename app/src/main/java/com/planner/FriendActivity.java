package com.planner;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

public class FriendActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        ViewPager viewPager = findViewById(R.id.view_pager);
        FriendPagerAdapter friendPagerAdapter = new FriendPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(friendPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(view -> openDialog());
    }

    private void openDialog() {
        RequestDialog requestDialog = new RequestDialog();
        requestDialog.show(getSupportFragmentManager(), "request dialog");
    }
}