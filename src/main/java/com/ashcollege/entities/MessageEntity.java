package com.ashcollege.entities;

import org.apache.tomcat.jni.User;

import javax.xml.crypto.Data;
import java.util.Date;

public class MessageEntity extends BaseEntity {
    private String text;
    private UserEntity sender;
    private Date date;

    public MessageEntity(String text, UserEntity sender) {
        this.text = text;
        this.sender = sender;
        this.date = new Date();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UserEntity getSender() {
        return sender;
    }

    public void setSender(UserEntity sender) {
        this.sender = sender;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    public MessageEntity(){}

    @Override
    public String toString() {
        return "MessageEntity{" +
                "text='" + text + '\'' +
                ", sender=" + sender +
                ", date=" + date +
                '}';
    }
}
