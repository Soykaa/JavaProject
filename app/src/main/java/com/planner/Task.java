package com.planner;

public class Task {
    private String title;
    private String date;
    private String time;
    private long reward;
    private String description;
    private boolean isDone;

    public Task() {};

    public Task(String title, String date, String time, long reward, String description) {
        this.title = title;
        this.date = date;
        this.time = time;
        this.reward = reward;
        this.description = description;
        isDone = false;
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

    public long getReward() {
        return reward;
    }

    public String getDescription() {
        return description;
    }

    public void setIsDone() {
        isDone = true;
    }

    public boolean isDone() {
        return isDone;
    }
}

