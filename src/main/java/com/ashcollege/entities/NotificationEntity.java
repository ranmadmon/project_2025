package com.ashcollege.entities;

import java.util.Date;

public class NotificationEntity extends BaseEntity{
    private UserEntity fromUser;
    private CourseEntity course;
    private String title;
    private String content;
    private Date date;

    public CourseEntity getCourse() {
        return course;
    }

    public void setCourse(CourseEntity course) {
        this.course = course;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public NotificationEntity() {
    }

    public NotificationEntity(UserEntity fromUser, CourseEntity course, String title, String content) {
        this.fromUser = fromUser;
        this.course = course;
        this.title = title;
        this.content = content;
        this.date = new Date();
    }

    public UserEntity getFromUser() {
        return fromUser;
    }

    public void setFromUser(UserEntity fromUser) {
        this.fromUser = fromUser;
    }
}
