package com.ashcollege.controllers;

import com.ashcollege.entities.*;
import com.ashcollege.responses.BasicResponse;
import com.ashcollege.responses.LoginResponse;
import com.ashcollege.responses.RegisterResponse;
import com.ashcollege.service.Persist;
import com.ashcollege.utils.ApiUtils;
import com.ashcollege.utils.Constants;
import com.ashcollege.utils.GeneralUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class GeneralController {
private HashMap<String,UserEntity> tempUsers = new HashMap<>();
    @Autowired
    private Persist persist;

    @PostConstruct
    public void init() {

        //  CourseEntity course2= new CourseEntity("Data Structures", "Data", lecturer.getName());
        //this.persist.save(course);
//        this.persist.save(course2);
        //System.out.println(this.persist.loadObject(UserEntity.class,2));
    }

    @RequestMapping("/get-course")
    public CourseEntity getCourse(int id) {
        return this.persist.loadObject(CourseEntity.class, id);
    }

    @RequestMapping("/get-types")
    List<TypeEntity> getAllTypes() {
        return this.persist.loadList(TypeEntity.class);
    }

    @RequestMapping("/get-tags")
    List<TagEntity> getAllTags() {
        return this.persist.loadList(TagEntity.class);
    }

    @RequestMapping("/get-materials")
    public List<MaterialEntity> getMaterials() {
        return this.persist.loadList(MaterialEntity.class);
    }

    @RequestMapping("/get-materials-by-course-id")
    public List<MaterialEntity> getMaterialsByCourseId(int courseId) {
        return persist.getMaterialByCourseId(courseId);
    }

    @RequestMapping("/add-material")
    void addMaterial(String title, String type, String username,
                     String token, int courseId, String description, String tag, String content) {
        int userId = this.persist.getUserByUsernameAndPass(username, token).getId();
        MaterialEntity materialEntity = new MaterialEntity(title, type, userId, courseId, description, tag, content);
        this.persist.save(materialEntity);

    }

    @RequestMapping("/recovery-password")
    public BasicResponse recoveryPassword(String email){
        boolean isExist = false;
        int errorCode = Constants.FAIL;
        UserEntity user = this.persist.getUserByEmail(email);
        if (user!=null){
            isExist = true;
            errorCode  =Constants.SUCCESS;
            String otp = GeneralUtils.generateOtp();
        RecoveryEntity recovery = new RecoveryEntity(user.getFullName(),otp,email);
        user.setPasswordRecovery(otp);
        this.persist.save(user);
        this.persist.save(recovery);
        }
        return new BasicResponse(isExist,errorCode);
    }

    @RequestMapping("/check-recovery-password")
     public BasicResponse checkRecoveryPassword(String email,String pass_recovery){
        boolean isValidPass = false;
        int errorCode = Constants.FAIL;
        UserEntity user = this.persist.getUserByEmailAndPasswordRecovery(email,pass_recovery);
        if (user!=null){
            user.setPasswordRecovery("");
            isValidPass  =true;
            errorCode = Constants.SUCCESS;
        }
        return new BasicResponse(isValidPass,errorCode);

    }

    @RequestMapping("/get-notifications")
    public List<NotificationEntity> getNotifications() {
        return this.persist.loadList(NotificationEntity.class);
    }
//    @RequestMapping("/get-notifications")
//    public List<QueryHistoryEntity> getQueryHistory() {
//        return this.persist.loadList(QueryHistoryEntity.class);
//    }

    @RequestMapping("/check-otp-to-register")
    public RegisterResponse addUser(String username,String otp) {
        boolean registeredSuccessfully = true;
        System.out.println("R");
        int errorCode = Constants.SUCCESS;
        UserEntity user = this.tempUsers.get(username);
        if (user==null||!user.getOtp().equals(otp)) {
            registeredSuccessfully = false;
            errorCode = Constants.FAIL;
        } else {
            user.setOtp("");
            this.tempUsers.remove(username);
          this.persist.save(user);

        }
        return new RegisterResponse(true, errorCode, registeredSuccessfully);
    }
    @RequestMapping("/register")
    public RegisterResponse register(String userName, String password, String name, String lastName,
                                     String email, String role,String phoneNumber){
        //קודם נבדוק שאין את הערכים האלה בטבלה במידה ואין נשלח הודעת sms
        boolean registeredSuccessfully = true;
        System.out.println("R2");
        int errorCode = Constants.SUCCESS;
        if (!isUsernameOrEmailExists(userName,email)||isValidPhoneNumber(phoneNumber)) {
            registeredSuccessfully = false;
            errorCode = Constants.FAIL;
        } else {
            String hashed = GeneralUtils.hashMd5(password);
            UserEntity user = new UserEntity(userName, hashed, name, lastName, email, role,phoneNumber);
            String otp = GeneralUtils.generateOtp();
            user.setOtp(otp);
            this.tempUsers.put(user.getUsername(), user);
            ApiUtils.sendSms(user.getOtp(), List.of(user.getPhoneNumber()));
        }
        //במידה ותקין נשלח sms
        return new RegisterResponse(true, errorCode, registeredSuccessfully);
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
      //  String phone = GeneralUtils.checkPhoneNumber(phoneNumber);
        if(!phoneNumber.isEmpty()){
            List<UserEntity> users = persist.loadList(UserEntity.class);
            List<UserEntity> temp = users.stream().filter(user -> user.getPhoneNumber().equals(phoneNumber)).toList();
            return temp.isEmpty();
        }
        return false;
    }


    @RequestMapping("/get-lecturers")
    public List<LecturerEntity> getLecturers() {
        return this.persist.loadList(LecturerEntity.class);
    }


    @RequestMapping("/get-all-courses")
    public List<CourseEntity> getAllCourses() {
        return this.persist.loadList(CourseEntity.class);
    }

    @RequestMapping("/add-course")
    public void addCourses(String name, String description, int lecturer) {
        CourseEntity course = new CourseEntity(name, description, lecturer);
        this.persist.save(course);
    }

    @RequestMapping("/add-lecturer")
    public void addLecturer(String name) {
        LecturerEntity lecturerEntity = new LecturerEntity(name);
        this.persist.save(lecturerEntity);
    }


    @RequestMapping(value = "/check-otp", method = {RequestMethod.GET, RequestMethod.POST})
    public LoginResponse checkOtp(String username, String password,String otp) {

        LoginResponse response = new LoginResponse();
        String hash = GeneralUtils.hashMd5(password);
        UserEntity user = persist.getUserByUsernameAndPass(username, hash);
        if (user != null) {
            if (user.getOtp().equals(otp)){
                response.setSuccess(true);
                response.setPermission(user.getRole().getId());
                System.out.println(hash);
                response.setToken(hash);
                if (response.isSuccess()) {
                    response.setErrorCode(Constants.SUCCESS);
                } else {
                    response.setErrorCode(Constants.FAIL);
                }
            }
            user.setOtp("");
            persist.save(user);
        }

        return response;
    }
    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    public BasicResponse login(String username, String password){
        BasicResponse response = new BasicResponse();
        String hash = GeneralUtils.hashMd5(password);
        UserEntity user = persist.getUserByUsernameAndPass(username, hash);
        if (user != null){
            String otp = GeneralUtils.generateOtp();
            user.setOtp(otp);
            persist.save(user);
            response.setSuccess(true);
            response.setErrorCode(Constants.SUCCESS);
            ApiUtils.sendSms(user.getOtp(), List.of(user.getPhoneNumber()));
        }
        return response;
    }
    //

    @RequestMapping("/update-password")
    public BasicResponse updatePassword(String username, String password) {
        boolean isExist = false;
            int errorCode = Constants.FAIL;
        UserEntity user = persist.getUserByUsername(username);
        if (user != null) {
            user.setPassword(password);
            persist.save(user);
            isExist = true;
            errorCode = Constants.SUCCESS;
        }
        return new BasicResponse(isExist,errorCode);
    }


    public boolean isUsernameOrEmailExists(String username, String email) {
        List<UserEntity> users = persist.loadList(UserEntity.class);
        List<UserEntity> temp = users.stream().filter(user -> user.getUsername().equals(username)||user.getEmail().equals(email)).toList();
        return temp.isEmpty();
    }

    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
    public Object hello() {
        return "Hello From Server";
    }


}
