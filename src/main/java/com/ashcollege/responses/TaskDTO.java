package com.ashcollege.responses;

import com.ashcollege.entities.TaskAssignmentEntity;
import java.util.Date;

public class TaskDTO {
    private int id;
    private String taskName;
    private String clientName;
    private String username;
    private String status;
    private Date assignedDate;        // <-- java.util.Date

    public TaskDTO() {}

    public TaskDTO(int id, String taskName, String clientName,
                   String username, String status, Date assignedDate) {
        this.id           = id;
        this.taskName     = taskName;
        this.clientName   = clientName;
        this.username     = username;
        this.status       = status;
        this.assignedDate = assignedDate;
    }

    public static TaskDTO from(TaskAssignmentEntity e) {
        return new TaskDTO(
                e.getId(),
                e.getTask().getName(),
                e.getClient().getName(),
                e.getUser().getUsername(),
                e.getStatus(),
                e.getAssignedDate()      // now matches Date
        );
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Date getAssignedDate() {
        return assignedDate;
    }
    public void setAssignedDate(Date assignedDate) {
        this.assignedDate = assignedDate;
    }

    // ... שאר getters/setters ללא שינוי ...
}
