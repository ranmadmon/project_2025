package com.ashcollege.entities;

import java.util.List;

public class CourseEntity extends BaseEntity{
    private  String name;
    private String description;
    private LecturerEntity lecturerEntity;
    private UserEntity user;
    public LecturerEntity getLecturerEntity() {
        return lecturerEntity;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "CourseEntity{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", lecturerEntity=" + lecturerEntity +
                '}';
    }

    public CourseEntity() {
    }

    public CourseEntity(String name, String description, int lecturerId) {
        this.name = name;
        this.description = description;
        this.lecturerEntity = new LecturerEntity();
        this.lecturerEntity.setId(lecturerId);
    }

    public void setLecturerEntity(LecturerEntity lecturerEntity) {
        this.lecturerEntity = lecturerEntity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
