package com.ashcollege.responses;

public class ClientResponse {
    private int id;
    private String name;
    private String managerUsername;
    private int    managerTeamId;

    public ClientResponse(int id, String name, String managerUsername, int managerTeamId) {
        this.id              = id;
        this.name            = name;
        this.managerUsername = managerUsername;
        this.managerTeamId   = managerTeamId;
    }

    public int getId()                 { return id; }
    public String getName()            { return name; }
    public String getManagerUsername() { return managerUsername; }
    public int    getManagerTeamId()   { return managerTeamId; }
}
