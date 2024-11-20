package com.ashcollege.responses;

public class RegisterResponse extends BasicResponse{

    private boolean registeredSuccessfully;

    public RegisterResponse(boolean success, Integer errorCode,boolean registeredSuccessfully) {
        super(success, errorCode);
        this.registeredSuccessfully = registeredSuccessfully;

    }


}
