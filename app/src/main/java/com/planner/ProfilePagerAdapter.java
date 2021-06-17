package com.planner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ProfilePagerAdapter extends FragmentPagerAdapter {
    public ProfilePagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    private String userId;

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new TasksFragment(userId);
            case 1:
                return new WishesFragment(userId);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case 0:
                return "TASKS";
            case 1:
                return "WISHES";
            default:
                return null;
        }
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
