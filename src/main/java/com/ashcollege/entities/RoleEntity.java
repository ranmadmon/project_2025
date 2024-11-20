package com.ashcollege.entities;

public class RoleEntity extends BaseEntity{
    private String name;

    public String getName() {
        return name;
    }

    public RoleEntity(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
