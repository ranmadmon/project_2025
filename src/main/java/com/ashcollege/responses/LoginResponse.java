package com.ashcollege.responses;

public class LoginResponse extends BasicResponse{
    private int permission;
    private String token;

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public LoginResponse() {

    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
