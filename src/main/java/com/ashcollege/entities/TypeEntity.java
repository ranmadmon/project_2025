package com.ashcollege.entities;

public class TypeEntity extends BaseEntity {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return super.getId()+ "TypeEntity{" +
                "name='" + name + '\'' +
                '}';
    }
}
