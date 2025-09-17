package com.ashcollege.responses;

import java.util.List;

public class TaskRequest {
    private String name;
    private int averageTime;
    private List<String> requires;

    public TaskRequest() { }  // ברירת מחדל, נדרש ל־Jackson

    public TaskRequest(String name, int averageTime, List<String> requires) {
        this.name = name;
        this.averageTime = averageTime;
        this.requires = requires;
    }

    // getters & setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAverageTime() {
        return averageTime;
    }

    public void setAverageTime(int averageTime) {
        this.averageTime = averageTime;
    }

    public List<String> getRequires() {
        return requires;
    }

    public void setRequires(List<String> requires) {
        this.requires = requires;
    }
}
