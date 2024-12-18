package com.ashcollege.responses;

public class ValidationResponse {
    private Integer usernameTaken;
    private Integer phoneTaken;
    private Integer emailTaken;
    private Boolean success;

    public ValidationResponse() {
    }

    public ValidationResponse(Integer usernameTaken, Integer phoneTaken, Integer emailTaken, Boolean success) {
        this.usernameTaken = usernameTaken;
        this.phoneTaken = phoneTaken;
        this.emailTaken = emailTaken;
        this.success = success;
    }

    public Integer getUsernameTaken() {
        return usernameTaken;
    }

    public void setUsernameTaken(Integer usernameTaken) {
        this.usernameTaken = usernameTaken;
    }

    public Integer getPhoneTaken() {
        return phoneTaken;
    }

    public void setPhoneTaken(Integer phoneTaken) {
        this.phoneTaken = phoneTaken;
    }

    public Integer getEmailTaken() {
        return emailTaken;
    }

    public void setEmailTaken(Integer emailTaken) {
        this.emailTaken = emailTaken;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
