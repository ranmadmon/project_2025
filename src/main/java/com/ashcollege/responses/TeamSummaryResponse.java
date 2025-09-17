// src/main/java/com/ashcollege/responses/TeamSummaryResponse.java
package com.ashcollege.responses;

public class TeamSummaryResponse {
    private int id;
    private String name;

    public TeamSummaryResponse() { }

    public TeamSummaryResponse(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
