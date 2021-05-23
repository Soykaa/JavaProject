package com.planner;

public class DoneTask {
    private String owner;
    private Long timestamp;


    public DoneTask() { }


    public DoneTask(String owner, Long timestamp) {
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
