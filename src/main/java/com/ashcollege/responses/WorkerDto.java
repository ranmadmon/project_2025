package com.ashcollege.responses;
import java.util.List;

public class WorkerDto {
    private int id;
    private String username;
    private int roleId;
    private int teamId;
    private List<String> abilities;
    private int hoursWorked;
    public WorkerDto(String username, int roleId, int teamId,
                     List<String> abilities, int hoursWorked) {
        this.id = id; this.username = username; this.roleId = roleId;
        this.teamId = teamId; this.abilities = abilities; this.hoursWorked = hoursWorked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(int hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    public List<String> getAbilities() {
        return abilities;
    }

    public void setAbilities(List<String> abilities) {
        this.abilities = abilities;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
