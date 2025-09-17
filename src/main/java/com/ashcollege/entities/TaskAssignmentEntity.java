package com.ashcollege.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TaskAssignmentEntity {

    private int id;

    private TaskEntity task;

    private ClientEntity client;

    // Owner of the assignment (highest-role participant)

    private UserEntity user;

    private int hours;

    private String status;

    private Date assignedDate;


    public TaskAssignmentEntity() {}

    // getters & setters...

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public TaskEntity getTask() { return task; }
    public void setTask(TaskEntity task) { this.task = task; }

    public ClientEntity getClient() { return client; }
    public void setClient(ClientEntity client) { this.client = client; }

    public UserEntity getUser() { return user; }
    public void setUser(UserEntity user) { this.user = user; }

    public int getHours() { return hours; }
    public void setHours(int hours) { this.hours = hours; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getAssignedDate() { return assignedDate; }
    public void setAssignedDate(Date assignedDate) { this.assignedDate = assignedDate; }

}
