package com.ashcollege.entities;

import java.io.Serializable;

public class TaskRequirementEntity implements Serializable {
    private int    taskId;
    private String abilities;

    public TaskRequirementEntity(int taskId, String abilities) {
        this.taskId = taskId;
        this.abilities = abilities;
    }

    public TaskRequirementEntity() {}

    public int getTaskId() { return taskId; }
    public void setTaskId(int taskId) { this.taskId = taskId; }

    // **must** match the property name="abilities"
    public String getAbilities() { return abilities; }
    public void   setAbilities(String abilities) { this.abilities = abilities; }

    // equals/hashCode omitted…
}
