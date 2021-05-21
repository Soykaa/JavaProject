package com.planner;

public class Task {
    private String title;
    private String date;
    private String time;
    private long cost;
    private String description;

    public Task() {};

    public Task(String title, String date, String time,  long cost, String description) {
        this.title = title;
        this.date = date;
        this.time = time;
        this.cost = cost;
        this.description = description;
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

    public long getCost() {
        return cost;
    }

    public String getDescription() {
        return description;
    }
}

