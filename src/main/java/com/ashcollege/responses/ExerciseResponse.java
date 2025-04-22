package com.ashcollege.responses;

public class ExerciseResponse {
    private String exercise;
    private double result;
    private int level;
    private int score;
    public int getLevel() {
        return level;
    }

    public ExerciseResponse() {
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public ExerciseResponse(String exercise, double result,int level,int score) {
        this.exercise = exercise;
        this.result = result;
        this.level = level;
        this.score=score;
    }

    public String getExercise() {
        return exercise;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }
}
