package com.ashcollege.entities;

public class TaskEntity {
    private int id;
    private String name;
    private int averageTime;
    private String status;

    public TaskEntity(int id, int averageTime, String name, String status) {
        this.id = id;
        this.averageTime = averageTime;
        this.name = name;
        this.status = status;
    }

    public TaskEntity() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAverageTime() { return averageTime; }
    public void setAverageTime(int averageTime) { this.averageTime = averageTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
