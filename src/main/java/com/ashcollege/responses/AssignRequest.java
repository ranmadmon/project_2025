package com.ashcollege.responses;

public class AssignRequest {
    private int taskId;
    private int clientId;
    private String username;
    private int hours;

    public AssignRequest(int taskId, int clientId, int hours, String username) {
        this.taskId = taskId;
        this.clientId = clientId;
        this.hours = hours;
        this.username = username;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }
}
