package com.ashcollege.entities;

import java.util.Date;

public class MaterialHistoryEntity extends BaseEntity {
    private UserEntity user;
    private MaterialEntity material;
    private Date time;

    public MaterialHistoryEntity() {
    }

    public MaterialHistoryEntity(UserEntity user, MaterialEntity material) {
        this.user = user;
        this.material = material;
        this.time = new Date();
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public MaterialEntity getMaterial() {
        return material;
    }

    public void setMaterial(MaterialEntity material) {
        this.material = material;
    }

    @Override
    public String toString() {
        return "MaterialHistoryEntity{" +
                "user=" + user +
                ", material=" + material +
                '}';
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
