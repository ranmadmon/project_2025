package com.ashcollege.entities;

public class LecturerEntity extends BaseEntity{
    private String name;
    private UserEntity user;

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LecturerEntity(String name,UserEntity user){

        this.name=name;
        this.user = user;
    }

    public LecturerEntity() {
    }
}
