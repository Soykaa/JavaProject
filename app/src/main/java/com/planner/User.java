package com.planner;

import android.net.Uri;

import java.net.URI;
import java.util.List;

public class User {

    private String name;
    private String profileImage;
    private int points = 0;

    public User() {
    }

    public User(String name, String profileImage) {
        this.name = name;
        this.profileImage = profileImage;
    }

    public User(String name, String profileImage, int points) {
        this.name = name;
        this.profileImage = profileImage;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public int getPoints() {
        return points;
    }
}
