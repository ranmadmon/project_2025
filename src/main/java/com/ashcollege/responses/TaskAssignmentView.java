// com/ashcollege/responses/TaskAssignmentView.java
package com.ashcollege.responses;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TaskAssignmentView {
    private int id;
    private String taskName;
    private String clientName;
    private String username;         // מי ה־owner
    private String status;
    private Date assignedDate;
    private int hoursRequired;
    private int hoursLogged;
    private int hoursRemaining;
    private List<ParticipantDTO> participants;  // כל המשתתפים

    public TaskAssignmentView() {}

    public TaskAssignmentView(int id,
                              String taskName,
                              String clientName,
                              String username,
                              String status,
                              Date assignedDate,
                              int hoursRequired,
                              int hoursLogged,
                              List<ParticipantDTO> participants) {
        this.id = id;
        this.taskName = taskName;
        this.clientName = clientName;
        this.username = username;
        this.status = status;
        this.assignedDate = assignedDate;
        this.hoursRequired = hoursRequired;
        this.hoursLogged = hoursLogged;
        this.hoursRemaining = hoursRequired - hoursLogged;
        this.participants = participants;
    }

    // === getters/setters omitted for brevity ===
    // generate getters and setters for all fields, including participants

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTaskName() { return taskName; }
    public void setTaskName(String taskName) { this.taskName = taskName; }

    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getAssignedDate() { return assignedDate; }
    public void setAssignedDate(Date assignedDate) { this.assignedDate = assignedDate; }

    public int getHoursRequired() { return hoursRequired; }
    public void setHoursRequired(int hoursRequired) { this.hoursRequired = hoursRequired; }

    public int getHoursLogged() { return hoursLogged; }
    public void setHoursLogged(int hoursLogged) { this.hoursLogged = hoursLogged; }

    public int getHoursRemaining() { return hoursRemaining; }
    public void setHoursRemaining(int hoursRemaining) { this.hoursRemaining = hoursRemaining; }

    public List<ParticipantDTO> getParticipants() { return participants; }
    public void setParticipants(List<ParticipantDTO> participants) {
        this.participants = participants;
    }

    public static TaskAssignmentView fromEntity(
            com.ashcollege.entities.TaskAssignmentEntity ta,
            List<com.ashcollege.entities.AssignmentParticipantEntity> parts
    ) {
        int logged = parts.stream()
                .mapToInt(com.ashcollege.entities.AssignmentParticipantEntity::getHours)
                .sum();

        List<ParticipantDTO> dtos = parts.stream()
                .map(ParticipantDTO::fromEntity)
                .collect(Collectors.toList());

        return new TaskAssignmentView(
                ta.getId(),
                ta.getTask().getName(),
                ta.getClient().getName(),
                ta.getUser().getUsername(),
                ta.getStatus(),
                ta.getAssignedDate(),
                ta.getHours(),
                logged,
                dtos
        );
    }
}
