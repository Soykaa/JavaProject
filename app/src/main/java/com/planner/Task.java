package com.planner;

public class Task {
    private String title;
    private String date;
    private String time;
    private int reward;
    private int deadlinePenalty;
    private String description;
    private long timestamp;
    private String id;

    public Task() {};

    public Task(String title, String date, String time, int reward, int deadlinePenalty, String description, String id, long timestamp) {
        this.title = title;
        this.date = date;
        this.time = time;
        this.reward = reward;
        this.deadlinePenalty = deadlinePenalty;
        this.description = description;
        this.id = id;
        this.timestamp = timestamp;
    }

    public Task(String title, String date, String time, int reward, int deadlinePenalty, String description, String id) {
        this.title = title;
        this.date = date;
        this.time = time;
        this.reward = reward;
        this.deadlinePenalty = deadlinePenalty;
        this.description = description;
        this.id = id;
    }

    public Task(String title, String date, String time, int reward, String description, String id, int timestamp) {
        this.title = title;
        this.date = date;
        this.time = time;
        this.reward = reward;
        this.description = description;
        this.id = id;
        this.timestamp = timestamp;
    }

    public Task(String title, String description, int reward) {
        this.title = title;
        this.description = description;
        this.reward = reward;
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

