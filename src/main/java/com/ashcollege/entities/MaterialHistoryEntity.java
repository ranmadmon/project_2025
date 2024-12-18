package com.ashcollege.entities;

public class MaterialHistoryEntity extends BaseEntity{
    private UserEntity user;
    private MaterialEntity material;
    public MaterialHistoryEntity(){}
    public MaterialHistoryEntity(UserEntity user, MaterialEntity material) {
        this.user = user;
        this.material = material;
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
}
