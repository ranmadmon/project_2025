// src/main/java/com/ashcollege/responses/TeamMemberDTO.java
package com.ashcollege.responses;

import java.util.List;

public class TeamMemberDTO {
    private String username;
    private int hoursWorked;
    private List<String> clients;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public int getHoursWorked() { return hoursWorked; }
    public void setHoursWorked(int hoursWorked) { this.hoursWorked = hoursWorked; }
    public List<String> getClients() { return clients; }
    public void setClients(List<String> clients) { this.clients = clients; }
}
