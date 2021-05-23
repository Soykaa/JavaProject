package com.planner;

public class CompletedTask {
    private String owner;
    private Long timestamp;


    public CompletedTask() { }


    public CompletedTask(String owner, Long timestamp) {
        this.owner = owner;
        this.timestamp = timestamp;
    }

    public String getOwner() {
        return owner;
    }

    public Long getTimestamp() {
        return timestamp;
    }
}
