package com.ashcollege.entities;

public class LecturerEntity extends BaseEntity{
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LecturerEntity(String name){
        this.name=name;
    }

    public LecturerEntity() {
    }
}
