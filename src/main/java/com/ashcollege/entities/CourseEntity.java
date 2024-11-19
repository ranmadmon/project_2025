package com.ashcollege.entities;

public class CourseEntity extends BaseEntity{
    private  String name;
    private String description;
    private LecturerEntity lecturerEntity;

    public LecturerEntity getLecturerEntity() {
        return lecturerEntity;
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
