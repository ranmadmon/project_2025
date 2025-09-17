package com.ashcollege.responses;

public class ParticipantDTO {
    private String username;
    private int hoursAssigned;

    public ParticipantDTO() {}

    public ParticipantDTO(String username, int hoursAssigned) {
        this.username = username;
        this.hoursAssigned = hoursAssigned;
    }

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

    public static ParticipantDTO fromEntity(com.ashcollege.entities.AssignmentParticipantEntity p) {
        return new ParticipantDTO(
                p.getUser().getUsername(),
                p.getHours()
        );
    }
}
