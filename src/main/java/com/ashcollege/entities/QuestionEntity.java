package com.ashcollege.entities;

import java.util.Date;

public class QuestionEntity extends BaseEntity {
    private int type;
    private String text;
    private int correctAnswer;
    private int difficultyLevel;
    private UserEntity user;
    private String correct;
    private Date timeStamp;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public QuestionEntity() {
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public QuestionEntity(int type,UserEntity user, String correct, int difficultyLevel, int correctAnswer, String text) {
        this.type = type;
        this.user = user;
        this.correct = correct;
        this.difficultyLevel = difficultyLevel;
        this.correctAnswer = correctAnswer;
        this.text = text;
        this.timeStamp = new Date();
    }

    public String getCorrect() {
        return correct;
    }

    public void setCorrect(String correct) {
        this.correct = correct;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }
}
