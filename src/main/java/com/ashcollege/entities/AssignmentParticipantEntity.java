package com.ashcollege.entities;

public class AssignmentParticipantEntity extends BaseEntity {
    private TaskAssignmentEntity task;
    private UserEntity user;
    private int hours;

    public AssignmentParticipantEntity() {
    }

    public AssignmentParticipantEntity(TaskAssignmentEntity task, int hours, UserEntity user) {
        this.task = task;
        this.hours = hours;
        this.user = user;
    }

    public TaskAssignmentEntity getTask() {
        return task;
    }

    public void setTask(TaskAssignmentEntity task) {
        this.task = task;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
