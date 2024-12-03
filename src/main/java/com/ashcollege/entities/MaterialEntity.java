package com.ashcollege.entities;

import java.util.Date;

public class MaterialEntity extends BaseEntity {
    private String title;
    private TypeEntity typeEntity;
    private UserEntity userEntity;
    private Date uploadDate;
    private CourseEntity courseEntity;
    private String description;
    private TagEntity tagEntity;
    private String Content;
    public MaterialEntity () {}

    public MaterialEntity(String title, UserEntity userEntity, TypeEntity typeEntity, CourseEntity courseEntity, String description, TagEntity tagEntity, String content) {
        this.title = title;
        this.userEntity = userEntity;
        this.typeEntity = typeEntity;
        this.courseEntity = courseEntity;
        this.description = description;
        this.tagEntity = tagEntity;
        Content = content;
        this.uploadDate = new Date();
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public TagEntity getTagEntity() {
        return tagEntity;
    }

    public void setTagEntity(TagEntity tagEntity) {
        this.tagEntity = tagEntity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TypeEntity getTypeEntity() {
        return typeEntity;
    }

    public void setTypeEntity(TypeEntity typeEntity) {
        this.typeEntity = typeEntity;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public CourseEntity getCourseEntity() {
        return courseEntity;
    }

    public void setCourseEntity(CourseEntity courseEntity) {
        this.courseEntity = courseEntity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "MaterialEntity{" +
                "title='" + title + '\'' +
                ", typeEntity=" + typeEntity +
                ", userEntity=" + userEntity +
                ", uploadDate=" + uploadDate +
                ", courseEntity=" + courseEntity +
                ", description='" + description + '\'' +
                ", tagEntity=" + tagEntity +
                '}';
    }
}
