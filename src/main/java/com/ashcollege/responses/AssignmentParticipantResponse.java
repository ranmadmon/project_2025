package com.ashcollege.responses;

public class AssignmentParticipantResponse {
    private String username;
    private int    hoursAssigned;

    public AssignmentParticipantResponse() { }

    public AssignmentParticipantResponse(String username, int hoursAssigned) {
        this.username      = username;
        this.hoursAssigned = hoursAssigned;
    }

    // ********************
    // גטרים וסטרים נדרשים
    // ********************
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public int getHoursAssigned() {
        return hoursAssigned;
    }
    public void setHoursAssigned(int hoursAssigned) {
        this.hoursAssigned = hoursAssigned;
    }
}
