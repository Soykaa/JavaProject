package com.planner;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "Profile Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ProfilePagerAdapter profilePagerAdapter = new ProfilePagerAdapter(getSupportFragmentManager());
        String userId = getIntent().getStringExtra("userId");
        profilePagerAdapter.setUserId(userId);
        Log.d(TAG, userId);
        ViewPager viewPager = findViewById(R.id.view_pager_profile);
        viewPager.setAdapter(profilePagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs_profile);
        tabs.setupWithViewPager(viewPager);
    }
}