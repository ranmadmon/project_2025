// src/main/java/com/ashcollege/responses/PersonalDetailsResponse.java
package com.ashcollege.responses;

import java.util.List;

public class PersonalDetailsResponse {
    private String username;
    private String email;
    private String phone;
    private List<String> abilities;
    private List<String> clients;
    private String teamName;
    private String isLeaderYesOrNo;
    private int hoursWorked;
public PersonalDetailsResponse(){

}
    public PersonalDetailsResponse(
            String username,
            String email,
            String phone,
            List<String> abilities,
            List<String> clients,
            String teamName,
            String isLeaderYesOrNo,
            int hoursWorked
    ) {
        this.username    = username;
        this.email       = email;
        this.phone       = phone;
        this.abilities   = abilities;
        this.clients     = clients;
        this.teamName    = teamName;
        this.isLeaderYesOrNo    = isLeaderYesOrNo;
        this.hoursWorked = hoursWorked;
    }

    // getters בלבד
    public String getUsername()    { return username; }
    public String getEmail()       { return email; }
    public String getPhone()       { return phone; }
    public List<String> getAbilities()   { return abilities; }
    public List<String> getClients()     { return clients; }
    public String getTeamName()    { return teamName; }
    public String getIsLeaderYesOrNo() {return isLeaderYesOrNo;}
    public int getHoursWorked()    { return hoursWorked; }
}
