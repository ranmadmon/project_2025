package com.ashcollege.controllers;

import com.ashcollege.entities.CourseEntity;
import com.ashcollege.entities.LecturerEntity;
import com.ashcollege.entities.UserEntity;
import com.ashcollege.responses.LoginResponse;
import com.ashcollege.responses.RegisterResponse;
import com.ashcollege.service.Persist;
import com.ashcollege.utils.GeneralUtils;
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

        //  CourseEntity course2= new CourseEntity("Data Structures", "Data", lecturer.getName());
        //this.persist.save(course);
//        this.persist.save(course2);
        //System.out.println(this.persist.loadObject(UserEntity.class,2));
    }
    @RequestMapping("/register")
    public RegisterResponse register(String userName, String password, String name, String lastName,
                                     String email, String role){
        boolean registeredSuccessfully = true;
        if (!isUsernameExists(userName)){
            registeredSuccessfully = false;
        }else{
            String hashed = GeneralUtils.hashMd5(password);
          UserEntity user = new UserEntity(userName,hashed,name,lastName,email,role);
          this.persist.save(user);
        }
         return new RegisterResponse(true,200,registeredSuccessfully);
    }



    @RequestMapping("/get-lecturers")
    public List<LecturerEntity> getLecturers(){
        return this.persist.loadList(LecturerEntity.class);
    }


    @RequestMapping("/get-all-courses")
    public List<CourseEntity> getAllCourses(){
        return this.persist.loadList(CourseEntity.class);
    }
    @RequestMapping("/add-course")
    public void addCourses(String name,String description,int lecturer){
        CourseEntity course = new CourseEntity(name,description,lecturer);
        this.persist.save(course);
    }

    @RequestMapping("/add-lecturer")
    public void addLecturer(String name){
        LecturerEntity lecturerEntity= new LecturerEntity(name);
        this.persist.save(lecturerEntity);
    }



    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    public LoginResponse login(String username, String password) {

        LoginResponse response = new LoginResponse();
        UserEntity user = persist.getUserByUsername(username);
        if (user != null){
            response.setSuccess(GeneralUtils.hashMd5(password).equals(user.getPassword()));
            response.setPermission(user.getRole().getId());
            String hash = GeneralUtils.hashMd5(password);
            System.out.println(hash);
            response.setToken(hash);
            if (response.isSuccess()) {
                response.setErrorCode(200);
            } else {
                response.setErrorCode(401);
            }
        }

        return response;
    }

    @RequestMapping("/update-password")
    public boolean updatePassword(String username, String password){
        UserEntity user = persist.getUserByUsername(username);
        if (user != null){
            user.setPassword(password);
            persist.save(user);
            return true;
        }
        return false;
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
