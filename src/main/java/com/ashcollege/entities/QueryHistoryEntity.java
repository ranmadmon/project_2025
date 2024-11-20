package com.ashcollege.entities;

public class QueryHistoryEntity extends BaseEntity{

    private String text;
    private UserEntity userEntity;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
}
