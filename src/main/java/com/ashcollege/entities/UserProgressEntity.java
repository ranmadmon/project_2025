package com.ashcollege.entities;


public class UserProgressEntity {

    private int id;


    private UserEntity user;

    private int mathematicalExercisesLevel;
    private int verbalQuestionsLevel;
    private int variableQuestionsLevel;
    private int mathematicalExercisesScore;
    private int verbalQuestionsScore;
    private int variableQuestionsScore;

    public int getVariableQuestionsLevel() {
        return variableQuestionsLevel;
    }

    public void setVariableQuestionsLevel(int variableQuestionsLevel) {
        this.variableQuestionsLevel = variableQuestionsLevel;
    }

    public int getVariableQuestionsScore() {
        return variableQuestionsScore;
    }

    public void setVariableQuestionsScore(int variableQuestionsScore) {
        this.variableQuestionsScore = variableQuestionsScore;
    }

    public int getMathematicalExercisesScore() {
        return mathematicalExercisesScore;
    }

    public void setMathematicalExercisesScore(int mathematicalExercisesScore) {
        this.mathematicalExercisesScore = mathematicalExercisesScore;
    }

    public int getVerbalQuestionsScore() {
        return verbalQuestionsScore;
    }

    public void setVerbalQuestionsScore(int verbalQuestionsScore) {
        this.verbalQuestionsScore = verbalQuestionsScore;
    }

    public UserProgressEntity(UserEntity user) {
        this.mathematicalExercisesScore=0;
        this.verbalQuestionsScore=0;
        this.mathematicalExercisesLevel = 1;
        this.verbalQuestionsLevel = 1;
        this.variableQuestionsLevel=1;
        this.variableQuestionsScore=0;
        this.user = user;
    }
    public  UserProgressEntity(){

    }

// Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public int getMathematicalExercisesLevel() {
        return mathematicalExercisesLevel;
    }

    public void setMathematicalExercisesLevel(int mathematicalExercisesLevel) {
        this.mathematicalExercisesLevel = mathematicalExercisesLevel;
    }

    public int getVerbalQuestionsLevel() {
        return verbalQuestionsLevel;
    }

    public void setVerbalQuestionsLevel(int verbalQuestionsLevel) {
        this.verbalQuestionsLevel = verbalQuestionsLevel;
    }

    @Override
    public String toString() {
        return "UserProgressEntity{" +
                "id=" + id +
                ", user=" + user.getUsername() +
                ", mathematicalExercisesLevel=" + mathematicalExercisesLevel +
                ", verbalQuestionsLevel=" + verbalQuestionsLevel +
                '}';
    }
}
