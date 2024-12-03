package com.ashcollege.entities;

import org.springframework.web.bind.annotation.RequestMapping;

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

    public static final int TYPE_PRESENTATION = 1;
    public static final int TYPE_EXERCISE = 2;
    public static final int TYPE_SOLUTION = 3;
    public static final int TYPE_DEFAULT = 4;
    public static final int TAG_ALGO = 1;
    public static final int TAG_MATH = 2;
    public static final int TAG_DEFAULT = 3;



    public MaterialEntity () {}


    public MaterialEntity(String title, String type ,int userId, int courseId, String description, String tag, String content) {
        this.title = title;
        this.userEntity = new UserEntity();
        this.userEntity.setId(userId);
        this.typeEntity = new TypeEntity();
        this.typeEntity.setId(setTypeId(type));
        this.courseEntity = new CourseEntity();
        this.courseEntity.setId(courseId);
        this.description = description;
        this.tagEntity = new TagEntity();
        this.tagEntity.setId(getTagId(tag));
        Content = content;
        this.uploadDate = new Date();
    }

    public int getTagId(String tag) {
        int typeId = TAG_DEFAULT;
        switch (tag) {
            case "אלגו" -> typeId = TAG_ALGO;
            case "מתמטיקה" -> typeId = TAG_MATH;
        }
        return typeId;
    }


    public int setTypeId(String type) {
        int typeId = TYPE_DEFAULT;
        switch (type) {
            case "מצגת" -> typeId = TYPE_PRESENTATION;
            case "תרגיל" -> typeId = TYPE_EXERCISE;
            case "פתרון" -> typeId = TYPE_SOLUTION;
        }
        return typeId;
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
