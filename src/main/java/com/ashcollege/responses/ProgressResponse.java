package com.ashcollege.responses;

public class ProgressResponse {
    private boolean success;
    private String message;
    private Integer mathLevel;
    private Integer verbalLevel;
    private Integer variableLevel;
    private Integer totalCorrectMathAnswers;
    private Integer totalCorrectVerbalAnswers;
    private Integer totalCorrectVariableAnswers;
    private Double mathSuccessRate;
    private Double verbalSuccessRate;
    private Double variableSuccessRate;

    public ProgressResponse(boolean success, String message,
                            Integer mathLevel, Integer verbalLevel, Integer variableLevel,
                            Integer totalCorrectMathAnswers, Integer totalCorrectVerbalAnswers, Integer totalCorrectVariableAnswers,
                            Double mathSuccessRate, Double verbalSuccessRate, Double variableSuccessRate) {
        this.success = success;
        this.message = message;
        this.mathLevel = mathLevel;
        this.verbalLevel = verbalLevel;
        this.variableLevel = variableLevel;
        this.totalCorrectMathAnswers = totalCorrectMathAnswers;
        this.totalCorrectVerbalAnswers = totalCorrectVerbalAnswers;
        this.totalCorrectVariableAnswers = totalCorrectVariableAnswers;
        this.mathSuccessRate = mathSuccessRate;
        this.verbalSuccessRate = verbalSuccessRate;
        this.variableSuccessRate = variableSuccessRate;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Double getVariableSuccessRate() {
        return variableSuccessRate;
    }

    public void setVariableSuccessRate(Double variableSuccessRate) {
        this.variableSuccessRate = variableSuccessRate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getMathLevel() {
        return mathLevel;
    }

    public void setMathLevel(Integer mathLevel) {
        this.mathLevel = mathLevel;
    }

    public Integer getTotalCorrectMathAnswers() {
        return totalCorrectMathAnswers;
    }

    public void setTotalCorrectMathAnswers(Integer totalCorrectMathAnswers) {
        this.totalCorrectMathAnswers = totalCorrectMathAnswers;
    }

    public Integer getVerbalLevel() {
        return verbalLevel;
    }

    public void setVerbalLevel(Integer verbalLevel) {
        this.verbalLevel = verbalLevel;
    }

    public Integer getVariableLevel() {
        return variableLevel;
    }

    public void setVariableLevel(Integer variableLevel) {
        this.variableLevel = variableLevel;
    }

    public Integer getTotalCorrectVerbalAnswers() {
        return totalCorrectVerbalAnswers;
    }

    public void setTotalCorrectVerbalAnswers(Integer totalCorrectVerbalAnswers) {
        this.totalCorrectVerbalAnswers = totalCorrectVerbalAnswers;
    }

    public Integer getTotalCorrectVariableAnswers() {
        return totalCorrectVariableAnswers;
    }

    public void setTotalCorrectVariableAnswers(Integer totalCorrectVariableAnswers) {
        this.totalCorrectVariableAnswers = totalCorrectVariableAnswers;
    }

    public Double getVerbalSuccessRate() {
        return verbalSuccessRate;
    }

    public void setVerbalSuccessRate(Double verbalSuccessRate) {
        this.verbalSuccessRate = verbalSuccessRate;
    }

    public Double getMathSuccessRate() {
        return mathSuccessRate;
    }

    public void setMathSuccessRate(Double mathSuccessRate) {
        this.mathSuccessRate = mathSuccessRate;
    }
}
