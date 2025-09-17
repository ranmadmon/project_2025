package com.ashcollege.entities;

public class TeamEntity {
    private int id;
    private String name; // 🆕 שם הצוות
    private UserEntity head;

    public TeamEntity() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public UserEntity getHead() { return head; }
    public void setHead(UserEntity head) { this.head = head; }
}
