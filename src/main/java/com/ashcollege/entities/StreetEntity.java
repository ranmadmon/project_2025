package com.ashcollege.entities;

public class StreetEntity extends BaseEntity {
    private String name;
    private CityEntity cityEntity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CityEntity getCityEntity() {
        return cityEntity;
    }

    public void setCityEntity(CityEntity cityEntity) {
        this.cityEntity = cityEntity;
    }
}
