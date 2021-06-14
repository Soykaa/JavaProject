package com.planner;

import android.util.Log;

import com.google.firebase.database.Exclude;

import java.util.Map;

public class CompletedTask {
    private String id;
    private String owner;
    private Object timestamp;
    private String upload;

    public CompletedTask() {
    }


    public CompletedTask(String id, String owner, Object timestamp) {
        this.id = id;
        this.owner = owner;
        this.timestamp = timestamp;
        this.upload = null;
    }

    public CompletedTask(String id, String owner, Object timestamp, String upload) {
        this.id = id;
        this.owner = owner;
        this.timestamp = timestamp;
        this.upload = upload;
    }


    public String getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public Map<String, String> getTimestamp() {
        return (Map<String, String>) timestamp;
    }

    @Exclude
    public Long getTimestampLong() {
        return (Long) timestamp;
    }
}
