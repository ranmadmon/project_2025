package com.ashcollege.responses;

public class LoginResponse extends BasicResponse{
    private boolean loginSuccessful;
    private int permission;

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

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
