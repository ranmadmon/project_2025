package com.ashcollege.responses;

public class ValidationRespons {
    private boolean success;
    private String message;

    public ValidationRespons() {
    }

    public ValidationRespons(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
