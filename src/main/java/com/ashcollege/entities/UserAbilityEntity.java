package com.ashcollege.entities;

public class UserAbilityEntity {
    private int userId;
    private String abilities;

    public UserAbilityEntity() {}

    public UserAbilityEntity(int userId, String abilities) {
        this.userId = userId;
        this.abilities = abilities;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAbilities() {
        return abilities;
    }

    public void setAbilities(String abilities) {
        this.abilities = abilities;
    }
}
