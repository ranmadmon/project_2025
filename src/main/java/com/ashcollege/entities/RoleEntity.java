package com.ashcollege.entities;

public class RoleEntity extends BaseEntity{
    private String name;

    public String getName() {
        return name;
    }

    public RoleEntity() {}

    public void setName(String name) {
        this.name = name;
    }
}
