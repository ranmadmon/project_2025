package com.ashcollege.entities;

import java.util.Date;

public class ProgressNotificationEntity extends BaseEntity {
    private UserEntity user;
    private String subject;
    private String message;
    private boolean read;
    private Date timestamp;

    public ProgressNotificationEntity(){
        this.timestamp = new Date();
        read = false;
    }
    public ProgressNotificationEntity(UserEntity user, String subject, String Message){
        this.user = user;
        this.subject = subject;
        this.message = Message;
        read = false;
        this.timestamp=new Date();
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
