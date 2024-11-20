package com.ashcollege.controllers;

import com.ashcollege.entities.UserEntity;
import com.ashcollege.responses.LoginResponse;
import com.ashcollege.service.Persist;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.List;

@RestController
public class GeneralController {

    @Autowired
    private Persist persist;

    @PostConstruct
    public void init(){
        UserEntity ram = new UserEntity("ramr", "1234", "ram", "revivo", "ram@");
        persist.save(ram);

        System.out.println(persist.getMaterialByTitle("loop"));

    }
//    @RequestMapping("/register")
//    public RegisterResponse register(String userName, String password, String name,String lastName,
//                                     String email,String role){
//
//    }

    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    public LoginResponse login(String username, String password) {
        LoginResponse response = new LoginResponse(false, 400);
        UserEntity user = persist.getUserByUsername(username);
        if (user != null){
            response.setSuccess(true);
            response.setLoginSuccessful(password.equals(user.getPassword()));
            if (response.isLoginSuccessful()) {
                response.setErrorCode(0);
            } else {
                response.setErrorCode(401);
            }
        }
        return response;
    }

    public boolean isUsernameExists(String username){
        List<UserEntity> users = persist.loadList(UserEntity.class);
        List<UserEntity> temp = users.stream().filter(user -> user.getUsername().equals(username)).toList();
        return temp.isEmpty();
    }
    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
    public Object hello() {
        return "Hello From Server";
    }



}
