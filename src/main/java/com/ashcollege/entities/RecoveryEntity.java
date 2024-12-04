package com.ashcollege.entities;

import com.ashcollege.utils.GeneralUtils;

import java.util.Date;

public class RecoveryEntity extends BaseEntity{
    private String title;
    private String body;
    private Date time;
    private String email;

    public RecoveryEntity(String title,String otp, String email) {
        this.title = title;
        this.body = setText(otp);
        this.email = email;
        this.time = new Date();
    }

    private String setText(String otp) {
        return "לשחזור הסיסמא הכנס את הקוד" + otp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
