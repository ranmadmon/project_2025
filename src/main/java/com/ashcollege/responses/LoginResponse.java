package com.ashcollege.responses;

public class LoginResponse extends BasicResponse{
    private boolean loginSuccessful;
    public LoginResponse(boolean success, Integer errorCode) {
        super(success, errorCode);
    }
    public boolean isLoginSuccessful() {
        return loginSuccessful;
    }

    public void setLoginSuccessful(boolean loginSuccessful) {
        this.loginSuccessful = loginSuccessful;
    }
}
