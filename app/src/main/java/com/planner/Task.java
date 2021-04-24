package com.planner;

public class Task {
    private String title;
    //date
    //time
    //private String owner;
    //private long cost;
    private String cost;
    private String description;

    public Task() {};

    public Task(String title, String cost, String description) {
        this.title = title;
        this.cost = cost;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getCost() {
        return cost;
    }

    public String getDescription() {
        return description;
    }
}

