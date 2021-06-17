package com.planner;

public class Task {
    private String parentId;
    private String title;
    private String date;
    private String time;
    private int reward;
    private int deadlinePenalty;
    private String description;
    private long timestamp;
    private String id;

    public Task() {};

    public Task(String parentId, String title, String date, String time, int reward, int deadlinePenalty, String description, String id, long timestamp) {
        this.parentId = parentId;
        this.title = title;
        this.date = date;
        this.time = time;
        this.reward = reward;
        this.deadlinePenalty = deadlinePenalty;
        this.description = description;
        this.id = id;
        this.timestamp = timestamp;
    }

    public String getParentId() {
        return parentId;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public int getReward() {
        return reward;
    }

    public int getDeadlinePenalty() {
        return deadlinePenalty;
    }

    public String getDescription() {
        return description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }
}

