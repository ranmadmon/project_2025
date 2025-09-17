// src/main/java/com/ashcollege/responses/MeResponse.java
package com.ashcollege.responses;

public class MeResponse {
    private boolean success;
    private int errorCode;
    private int id;
    private String username;
    private int permission;
    private int teamId;

    // getters & setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public int getErrorCode() { return errorCode; }
    public void setErrorCode(int errorCode) { this.errorCode = errorCode; }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public int getPermission() { return permission; }
    public void setPermission(int permission) { this.permission = permission; }
    public int getTeamId() { return teamId; }
    public void setTeamId(int teamId) { this.teamId = teamId; }
}
