package com.planner;

public class Wish {
    private String title;
    private String price;
    private String description;

    public Wish() {};

    public Wish(String title, String price, String description) {
        this.title = title;
        this.price = price;
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }
}
