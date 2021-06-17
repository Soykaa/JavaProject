package com.planner;

public class Task {
    private String parentId;
    private String title;
    private String date;
    private String time;
    private int reward;
    private String description;
    private String id;

    public Task() {};

    public Task(String parentId, String title, String date, String time, int reward, String description, String id) {
        this.parentId = parentId;
        this.title = title;
        this.date = date;
        this.time = time;
        this.reward = reward;
        this.description = description;
        this.id = id;
    }

//    public Task(String parentId, String title, String description, int reward) {
//        this.parentId = parentId;
//        this.title = title;
//        this.description = description;
//        this.reward = reward;
//    }

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

    public String getDescription() {
        return description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }
}

