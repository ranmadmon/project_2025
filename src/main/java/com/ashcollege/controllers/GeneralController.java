package com.ashcollege.controllers;

import com.ashcollege.entities.*;
import com.ashcollege.responses.BasicResponse;
import com.ashcollege.responses.LoginResponse;
import com.ashcollege.responses.RegisterResponse;
import com.ashcollege.responses.ValidationResponse;
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

    }
    @RequestMapping("/add-material-to-history")
    public void addMaterialToHistory(String token,int materialId){
        System.out.println(token);
        System.out.println("pppp"+materialId);
       UserEntity user =  this.persist.getUserByPass(token);
        MaterialEntity material = this.persist.loadObject(MaterialEntity.class,materialId);
       if (user!=null){
           System.out.println("ttttt "+user);
           this.persist.save(new MaterialHistoryEntity(user,material));
       }
    }
    @RequestMapping("/get-material-history")
    public List<MaterialEntity> getMaterialHistory(String token){
        List<MaterialEntity> materialHistoryEntities  = new ArrayList<>();
        System.out.println(token);
        UserEntity user = this.persist.getUserByPass(token);
        if (user!=null){
            System.out.println(user.getId());
            materialHistoryEntities = this.persist.getMaterialHistoryByUserId(user.getId());
        }
        System.out.println(materialHistoryEntities);
        return materialHistoryEntities;
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
    void addMaterial(String title, String type,
                     String token, int courseId, String description, String tag, String content) {

        try {
            UserEntity userEntity = this.persist.getUserByPass(token);
            int userId = userEntity.getId();
            MaterialEntity materialEntity = new MaterialEntity(title, type, userId, courseId, description, tag, content);
            this.persist.save(materialEntity);
            RecoveryEntity recovery = new RecoveryEntity("My Title", "My OTP",
                    "shaigivati464@gmail.com");
            this.persist.save(recovery);

        } catch (Exception e) {
            e.printStackTrace();
        }

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

    @RequestMapping("/check-otp-to-register")
    public RegisterResponse addUser(String username,String otp) {
        boolean registeredSuccessfully = true;
        int errorCode = Constants.SUCCESS;
        UserEntity user = this.tempUsers.get(username);
        if (user==null||!user.getOtp().equals(otp)) {
            registeredSuccessfully = false;
            errorCode = Constants.FAIL;
        }
        if (user!=null&&user.getOtp().equals(otp)){
            user.setOtp("");
            this.tempUsers.remove(username);
            System.out.println("lll"+user);
          this.persist.save(user);
            System.out.println(user);
        }
        return new RegisterResponse(true, errorCode, registeredSuccessfully);
    }
    @RequestMapping("/register")
    public ValidationResponse register(String userName, String password, String name, String lastName,
                                       String email, String role, String phoneNumber){
        System.out.println("YYYYyy"+email);
        System.out.println("kkkk"+phoneNumber);
        boolean isValid = true;
        ValidationResponse validationResponse = new ValidationResponse();
        try {
            if (!isValid(userName,phoneNumber,email,validationResponse)) {
                isValid = false;
            } else {
                String hashed = GeneralUtils.hashMd5(password);
                UserEntity user = new UserEntity(userName, hashed, name, lastName, email, role,phoneNumber);
                String otp = GeneralUtils.generateOtp();
                user.setOtp(otp);
                this.tempUsers.put(user.getUsername(), user);
                ApiUtils.sendSms(user.getOtp(), List.of(user.getPhoneNumber()));
            }
            System.out.println(userName);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        validationResponse.setSuccess(isValid);
        return validationResponse;
    }

    private boolean isValid(String userName, String phoneNumber, String email,ValidationResponse validationResponse) {
        boolean isValid = true;
        if (!isUsernameExists(userName)){
            validationResponse.setUsernameTaken(Constants.USERNAME_TAKEN);
            isValid = false;
        }else if (!isValidPhoneNumber(phoneNumber)){
            validationResponse.setPhoneTaken(Constants.PHONE_TAKEN);
            isValid = false;
        }else if (!isEmailExists(email)){
            validationResponse.setEmailTaken(Constants.EMAIL_TAKEN);
            isValid = false;
        }
        return isValid;
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        if(GeneralUtils.isValidPhoneNumber(phoneNumber)){
            List<UserEntity> users = persist.loadList(UserEntity.class);
            System.out.println(users);
            List<UserEntity> temp = users.stream().filter(user ->user.getPhoneNumber()!=null&&user.getPhoneNumber().equals(phoneNumber)).toList();
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
        System.out.println("pppppp"+user);
        if (user != null) {
            if (user.getOtp().equals(otp)){
                response.setSuccess(true);
                user.setOtp("");
                response.setPermission(user.getRole().getId());
                System.out.println(hash);
                response.setToken(hash);
                if (response.isSuccess()) {
                    response.setErrorCode(Constants.SUCCESS);
                } else {
                    response.setErrorCode(Constants.FAIL);
                }
            }
            System.out.println("checkk"+user);

            persist.save(user);
        }

        return response;
    }
    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    public BasicResponse login(String username, String password){
        System.out.println("kk"+username);
        BasicResponse response = new BasicResponse();
        String hash = GeneralUtils.hashMd5(password);
        System.out.println(hash);
        UserEntity user = persist.getUserByUsernameAndPass(username, hash);

        if (user != null){
            String otp = GeneralUtils.generateOtp();
            user.setOtp(otp);
            System.out.println("jjjj"+otp);
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


    public boolean isUsernameExists(String username) {
        List<UserEntity> users = persist.loadList(UserEntity.class);
        List<UserEntity> temp = users.stream().filter(user -> user.getUsername().equals(username)).toList();
        return temp.isEmpty();
    }
    public boolean isEmailExists( String email) {
        List<UserEntity> users = persist.loadList(UserEntity.class);
        List<UserEntity> temp = users.stream().filter(user -> user.getEmail().equals(email)).toList();
        return temp.isEmpty();
    }

    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
    public Object hello() {
        return "Hello From Server";
    }


}
