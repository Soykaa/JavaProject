package com.planner;

import android.util.Log;

import com.google.firebase.database.Exclude;

import java.util.Map;

public class CompletedTask {
    private String id;
    private String owner;
    private Object timestamp;

    public CompletedTask() {}
  
    public CompletedTask(String id, String owner, Object timestamp) {
        this.id = id;
        this.owner = owner;
        this.timestamp = timestamp;
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
