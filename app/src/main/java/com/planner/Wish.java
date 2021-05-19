package com.planner;

public class Wish {
    private String title;
    private String cost;
    private String description;
    private String id;

    // must-have for database correct work!
    public Wish() {}

    public Wish(String title, String cost, String description, String id) {
        this.title = title;
        this.cost = cost;
        this.description = description;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getCost() {
        return cost;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
