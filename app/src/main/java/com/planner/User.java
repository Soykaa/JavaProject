package com.planner;

import android.net.Uri;

import java.net.URI;
import java.util.List;

public class User {

    private String name;
    private String profileImage;

    public User() {
    }

    public User(String name, String profileImage) {
        this.name = name;
        this.profileImage = profileImage;
    }

    public String getName() {
        return name;
    }

    public String getProfileImage() {
        return profileImage;
    }
}
