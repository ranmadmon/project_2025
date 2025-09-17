package com.ashcollege.responses;

import java.util.List;

public class TaskResponse {
    private int id;
    private String name;
    private int averageTime;
    private List<String> requires;

    // בנאי
    public TaskResponse(int id, String name, int averageTime, List<String> requires) {
        this.id = id;
        this.name = name;
        this.averageTime = averageTime;
        this.requires = requires;
    }

    // getters
    public int getId() { return id; }
    public String getName() { return name; }
    public int getAverageTime() { return averageTime; }
    public List<String> getRequires() { return requires; }
}
