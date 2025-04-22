package com.ashcollege.entities;

import javax.persistence.*;

public class QuestionTemplateEntity extends BaseEntity {


    private String template;

    private String operation;

    private int level;

    private String objects;

    private String names;

    // Getters and Setters

    public QuestionTemplateEntity( String names, String objects, int level, String operation, String template) {
        this.names = names;
        this.objects = objects;
        this.level = level;
        this.operation = operation;
        this.template = template;
    }

    public QuestionTemplateEntity() {

    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getObjects() {
        return objects;
    }

    public void setObjects(String objects) {
        this.objects = objects;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    @Override
    public String toString() {
        return "QuestionTemplateEntity{" +
                ", template='" + template + '\'' +
                ", operation='" + operation + '\'' +
                ", level=" + level +
                ", objects='" + objects + '\'' +
                ", names='" + names + '\'' +
                '}';
    }
}
