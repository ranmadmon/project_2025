package com.ashcollege.responses;

public class PlayerProgressResponse {
    private String username;
    private int mathematicalLevel;
    private int verbalQuestionsLevel;
    private int variableLevel;            // רמת שאלות Variable
    private double successRateMath;
    private double successRateVerbal;
    private double successRateVariable;   // שיעור הצלחה עבור Variable

    // קונסטרוקטור ברירת מחדל
    public PlayerProgressResponse() {
    }

    // קונסטרוקטור עם פרמטרים
    public PlayerProgressResponse(String username, int mathematicalLevel, int verbalQuestionsLevel, int variableLevel,
                                  double successRateMath, double successRateVerbal, double successRateVariable) {
        this.username = username;
        this.mathematicalLevel = mathematicalLevel;
        this.verbalQuestionsLevel = verbalQuestionsLevel;
        this.variableLevel = variableLevel;
        this.successRateMath = successRateMath;
        this.successRateVerbal = successRateVerbal;
        this.successRateVariable = successRateVariable;
    }

    // Getters and Setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getMathematicalLevel() {
        return mathematicalLevel;
    }

    public void setMathematicalLevel(int mathematicalLevel) {
        this.mathematicalLevel = mathematicalLevel;
    }

    public int getVerbalQuestionsLevel() {
        return verbalQuestionsLevel;
    }

    public void setVerbalQuestionsLevel(int verbalQuestionsLevel) {
        this.verbalQuestionsLevel = verbalQuestionsLevel;
    }

    public int getVariableLevel() {
        return variableLevel;
    }

    public void setVariableLevel(int variableLevel) {
        this.variableLevel = variableLevel;
    }

    public double getSuccessRateMath() {
        return successRateMath;
    }

    public void setSuccessRateMath(double successRateMath) {
        this.successRateMath = successRateMath;
    }

    public double getSuccessRateVerbal() {
        return successRateVerbal;
    }

    public void setSuccessRateVerbal(double successRateVerbal) {
        this.successRateVerbal = successRateVerbal;
    }

    public double getSuccessRateVariable() {
        return successRateVariable;
    }

    public void setSuccessRateVariable(double successRateVariable) {
        this.successRateVariable = successRateVariable;
    }
}
