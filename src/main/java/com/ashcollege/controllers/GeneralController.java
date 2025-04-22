package com.ashcollege.controllers;

import com.ashcollege.entities.*;
import com.ashcollege.responses.*;
import com.ashcollege.service.Persist;
import com.ashcollege.utils.ApiUtils;
import com.ashcollege.utils.Constants;
import com.ashcollege.utils.GeneralUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.logging.Level;

@RestController
public class    GeneralController {
private HashMap<String,UserEntity> tempUsers = new HashMap<>();
    @Autowired
    private Persist persist;


    @RequestMapping("/get-username-by-token")
    public String getNameByToken(String token){
        String name = null;
        UserEntity user = this.persist.getUserByPass(token);
        if (user!=null){
            name = user.getUsername();
        }
        return name;
    }

    @RequestMapping("/get-permission")
    public int getPermission(String token){
        UserEntity user = this.persist.getUserByPass(token);
        return user.getRole().getId();
    }

    @RequestMapping("/check-recovery-password")
     public BasicResponse checkRecoveryPassword(String email,String pass_recovery) {
        boolean isValidPass = false;
        int errorCode = Constants.FAIL;
        UserEntity user = this.persist.getUserByEmailAndPasswordRecovery(email, pass_recovery);
        if (user != null) {
            user.setPasswordRecovery("");
            isValidPass = true;
            errorCode = Constants.SUCCESS;
        }
        return new BasicResponse(isValidPass, errorCode);

    }

    @RequestMapping("/check-otp-to-register")
    public RegisterResponse addUser(String username,String otp) {
        boolean registeredSuccessfully = true;
        int errorCode = Constants.SUCCESS;
        System.out.println(username+"1");
        UserEntity user = this.tempUsers.get(username);
        if (user==null||!user.getOtp().equals(otp)) {
            registeredSuccessfully = false;
            errorCode = Constants.FAIL;
            System.out.println(username+"2");

        }
        if (user!=null&&user.getOtp().equals(otp)){
            user.setOtp("");
            this.tempUsers.remove(username);
            System.out.println("lll"+user);
          this.persist.save(user);
            UserProgressEntity userProgress=new UserProgressEntity(user);
            persist.save(userProgress);
          if (user.getRole().getId()==2){
              AdminEntity admin = new AdminEntity(user.getFullName(),user);
              this.persist.save(admin);
          }
            System.out.println(user);
        }
        return new RegisterResponse(true, errorCode, registeredSuccessfully);
    }
    // send a temporary password via SMS
    @RequestMapping("/forgot-password")
    public BasicResponse forgotPassword(@RequestParam String username) {
        BasicResponse resp = new BasicResponse();
        UserEntity user = persist.getUserByUsername(username);
        if (user != null) {
            // generate a temporary code (e.g. 6‑digit OTP)
            String temp = GeneralUtils.generateOtp();
            user.setPasswordRecovery(temp);
            persist.save(user);
            // send via SMS
            ApiUtils.sendSms(temp, List.of(user.getPhoneNumber()));
            resp.setSuccess(true);
            resp.setErrorCode(Constants.SUCCESS);
        } else {
            resp.setSuccess(false);
            resp.setErrorCode(Constants.USER_NOT_EXIST);
        }
        return resp;
    }

    // אימות הקוד הזמני בלבד – לא מוחק ולא משנה סיסמה
    @PostMapping("/verify-recovery-code")
    public BasicResponse verifyRecoveryCode(
            @RequestParam String username,
            @RequestParam String recovery
    ) {
        BasicResponse resp = new BasicResponse();
        UserEntity user = persist.getUserByUsername(username);
        if (user != null && recovery.equals(user.getPasswordRecovery())) {
            resp.setSuccess(true);
            resp.setErrorCode(Constants.SUCCESS);
        } else {
            resp.setSuccess(false);
            resp.setErrorCode(Constants.FAIL);
        }
        return resp;
    }

    // איפוס הסיסמה (מניח שהקוד כבר אומת בשלב הקודם)
    @PostMapping("/reset-password")
    public BasicResponse resetPassword(
            @RequestParam String username,
            @RequestParam("newPassword") String newPass
    ) {
        BasicResponse resp = new BasicResponse();
        UserEntity user = persist.getUserByUsername(username);
        if (user != null) {
            // חשישת הסיסמה החדשה
            String hashed = GeneralUtils.hashMd5(username, newPass);
            user.setPassword(hashed);
            // ניקוי קוד ההתאוששות
            user.setPasswordRecovery("");
            persist.save(user);
            resp.setSuccess(true);
            resp.setErrorCode(Constants.SUCCESS);
        } else {
            resp.setSuccess(false);
            resp.setErrorCode(Constants.FAIL);
        }
        return resp;
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
                String hashed = GeneralUtils.hashMd5(userName , password);
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
        }
        if (!isValidPhoneNumber(phoneNumber)){
            validationResponse.setPhoneTaken(Constants.PHONE_TAKEN);
            isValid = false;
        }
        if (!isEmailExists(email)){
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

    @RequestMapping(value = "/check-otp", method = {RequestMethod.GET, RequestMethod.POST})
    public LoginResponse checkOtp(String username, String password,String otp) {

        LoginResponse response = new LoginResponse();
        String hash = GeneralUtils.hashMd5(username , password);
        UserEntity user = persist.getUserByUsernameAndPass(username, hash);
        System.out.println("pppppp"+user);
        if (user != null) {
            if (user.getOtp().equals(otp)){
                response.setSuccess(true);
                user.setOtp("");
                response.setPermission(user.getRole().getId());
                System.out.println(hash);
                response.setToken(hash);
                response.setId(user.getId());
                if (response.isSuccess()) {
                    response.setErrorCode(Constants.SUCCESS);
                } else {
                    response.setErrorCode(Constants.FAIL);
                }
            }
            persist.save(user);
        }

        return response;
    }
    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    public BasicResponse login(String username, String password){
        System.out.println("kk"+username);
        BasicResponse response = new BasicResponse();
        String hash = GeneralUtils.hashMd5(username,password);
        System.out.println(hash);
        UserEntity user = persist.getUserByUsernameAndPass(username, hash);

        if (user != null){
            String otp = GeneralUtils.generateOtp();
            user.setOtp(otp);
            persist.save(user);
            response.setSuccess(true);
            response.setErrorCode(Constants.SUCCESS);
            ApiUtils.sendSms(user.getOtp(), List.of(user.getPhoneNumber()));
        }else {
            response.setErrorCode(Constants.USER_NOT_EXIST);
        }
        return response;
    }

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
    @RequestMapping("/get-level")
    public int gelLevel(String token,int type) {
        if (type==1){
            return persist.getMathematicalExercisesLevelByPass(token);
        }else if (type==2){
            return persist.getVerbalQuestionsLevelByUser(persist.getUserByPass(token));
        }else {
            return persist.getVariableQuestionsLevelByPass(token);
        }
    }
    @RequestMapping("/get-questions-history")
    public List<QuestionEntity> getQuestionsHistory(String token) {
        int userId = persist.getUserIdByPass    (token);
        return persist.getQuestionsByUserId(userId);
    }


    @RequestMapping("/generate-exercise")
    public ExerciseResponse generateExercise(String token) {
        int level = persist.getMathematicalExercisesLevelByUser(persist.getUserByPass(token));
        int score=persist.getMathematicalExercisesScoreByPass(token);
        Random random = new Random();
        String exercise = "";
        int result = 0; // הגדרנו את התוצאה כ-int כדי לוודא תמיד מספר שלם
        if (level==12){
            level = random.nextInt(11) + 1; // בוחר רמה אקראית בין 1 ל-11
        }

        switch (level) {
            case 1: // חיבור פשוט
                int num1 = random.nextInt(9) + 1; // בין 1 ל-9
                int num2 = random.nextInt(9) + 1;
                exercise = num1 + " + " + num2;
                result = num1 + num2;
                break;

            case 2: // חיסור פשוט (ללא אפס)
                int num3 = random.nextInt(20) + 1;
                int num4 = random.nextInt(num3) + 1; // למנוע תוצאה שלילית
                exercise = num3 + " - " + num4;
                result = num3 - num4;
                break;

            case 3: // כפל פשוט
                int num5 = random.nextInt(10) + 2;
                int num6 = random.nextInt(10) + 2;
                exercise = num5 + " * " + num6;
                result = num5 * num6;
                break;

            case 4: // חילוק פשוט עם תוצאה שלמה
                int divisor = random.nextInt(9) + 1; // מחלק בין 1 ל-9
                int dividend = divisor * (random.nextInt(10) + 1); // מספר שהוא כפולה של המחלק
                exercise = dividend + " / " + divisor;
                result = dividend / divisor;
                break;

            case 5: // שורש ריבועי של מספר מושלם
                int square = (random.nextInt(10) + 1); // מספר בין 1 ל-10
                int squareResult = square * square; // מספר מושלם
                exercise = "√" + squareResult;
                result = square;
                break;

            case 6: // אקספוננטים פשוטים (תוצאה שלמה)
                int base = random.nextInt(5) + 2; // בסיס בין 2 ל-6
                int exp = random.nextInt(3) + 2; // מעריך בין 2 ל-4
                exercise = base + "^" + exp;
                result = (int) Math.pow(base, exp);
                break;

            case 7: // שילוב חיבור וכפל
                int num10 = random.nextInt(10) + 1;
                int num11 = random.nextInt(10) + 1;
                int num12 = random.nextInt(10) + 1;
                exercise = num10 + " + " + "(" + num11 + " * " + num12 + ")";
                result = num10 + (num11 * num12);
                break;

            case 8: // שילוב שורש וכפל
                int num13 = random.nextInt(10) + 1; // בסיס לשורש
                int num14 = num13 * num13;  // מספר מושלם
                int num15 = random.nextInt(10) + 1;
                exercise = "√" + num14 + " * " + num15;
                result = num13 * num15;
                break;

            case 9: // שילוב מורכב
                int num16 = random.nextInt(20) + 1;
                int num17 = random.nextInt(10) + 2;
                int num18 = random.nextInt(5) + 2;
                exercise = "(" + num16 + " + " + num17 + ") * " + num18;
                result = (num16 + num17) * num18;
                break;

            case 10: // לוגריתמים עם תוצאה שלמה
                int logBase = random.nextInt(3) + 2; // בסיס בין 2 ל-4
                int logResult = random.nextInt(8) + 2; // תוצאה בין 2 ל-9
                int logValue = (int) Math.pow(logBase, logResult);
                exercise = "log_" + logBase + "(" + logValue + ")";
                result = logResult;
                break;

            case 11: // שילוב לוגריתם וחיבור עם תוצאה שלמה
                int logBase2 = random.nextInt(3) + 2;
                int logResult2 = random.nextInt(8) + 2;
                int logValue2 = (int) Math.pow(logBase2, logResult2);
                int num19 = random.nextInt(20) + 1;
                exercise = "log_" + logBase2 + "(" + logValue2 + ") + " + num19;
                result = logResult2 + num19;
                break;

            default:
                return new ExerciseResponse("Invalid level", 0, level,score);
        }

        return new ExerciseResponse(exercise, result, level,score);
    }

    @RequestMapping("/generate-variable-exercise")
    public ExerciseResponse generateVariableExercise(String token) {
        // שליפת רמת הקושי והציון (ניתן להשתמש באותה לוגיקה כמו בשאלות מתמטיות)
        int level = persist.getVariableQuestionsLevelByPass(token);
        int score = persist.getVariableQuestionsScoreByPass(token);

        Random random = new Random();
        String exercise = "";
        int result = 0; // הפתרון עבור המשתנה x

        switch (level) {
            case 1:
                // רמה 1: משוואה לינארית פשוטה: a*x + b = c
                // a – משמש לכפל, ולכן נבחר ערך בין 2 ל-9 (ולא 1)
                int a1 = random.nextInt(8) + 2;
                int x1 = random.nextInt(11) - 5;
                int b1 = random.nextInt(21) - 10;
                int c1 = a1 * x1 + b1;
                exercise = a1 + "x + " + b1 + " = " + c1;
                result = x1;
                break;

            case 2:
                // רמה 2: משוואה עם נעלם משני הצדדים: a*x + b = c*x + d
                int a2 = random.nextInt(8) + 2; // בין 2 ל-9
                int x2 = random.nextInt(11) - 5;
                int b2 = random.nextInt(21) - 10;
                int c2 = random.nextInt(8) + 2; // בין 2 ל-9
                while (c2 == a2) {
                    c2 = random.nextInt(8) + 2;
                }
                int d2 = a2 * x2 + b2 - c2 * x2;
                exercise = a2 + "x + " + b2 + " = " + c2 + "x + " + d2;
                result = x2;
                break;

            case 3:
                // רמה 3: משוואה עם סוגריים: a*(x + b) = c
                int a3 = random.nextInt(8) + 2;
                int x3 = random.nextInt(11) - 5;
                int b3 = random.nextInt(21) - 10;
                int c3 = a3 * (x3 + b3);
                exercise = a3 + "(x + " + b3 + ") = " + c3;
                result = x3;
                break;

            case 4:
                // רמה 4: משוואה מהצורה: a*x - b = c
                int a4 = random.nextInt(8) + 2;
                int x4 = random.nextInt(11) - 5;
                int b4 = random.nextInt(21);
                int c4 = a4 * x4 - b4;
                exercise = a4 + "x - " + b4 + " = " + c4;
                result = x4;
                break;

            case 5:
                // רמה 5: משוואה ריבועית עם פתרון יחיד: a*(x - r)^2 = 0
                int a5 = random.nextInt(8) + 2;
                int r5 = random.nextInt(11) - 5;
                exercise = a5 + "(x - " + r5 + ")^2 = 0";
                result = r5;
                break;

            case 6:
                // רמה 6: משוואה עם הפצת כפל: a*(x + b) + c = d*(x + e) + f
                int x6 = random.nextInt(11) - 5;
                int a6 = random.nextInt(8) + 2;
                int b6 = random.nextInt(21) - 10;
                int c6 = random.nextInt(21) - 10;
                int d6 = random.nextInt(8) + 2;
                int e6 = random.nextInt(21) - 10;
                int f6 = a6 * (x6 + b6) + c6 - d6 * (x6 + e6);
                exercise = a6 + "(x + " + b6 + ") + " + c6 + " = " + d6 + "(x + " + e6 + ") + " + f6;
                result = x6;
                break;

            case 7:
                // רמה 7: משוואה עם שבר: (x + a) / b = c, כאשר x = c * b - a
                int a7 = random.nextInt(21) - 10; // a7 יכול להיות בכל טווח
                // b7 – המכנה – לא יכול להיות 1, לכן נבחר ערך בין 2 ל-9
                int b7 = random.nextInt(8) + 2;
                int c7 = random.nextInt(21) - 10;
                exercise = "(x + " + a7 + ") / " + b7 + " = " + c7;
                result = c7 * b7 - a7;
                break;

            case 8:
                // רמה 8: משוואה מורכבת: a*(x + b) = c*(x + d) + e
                int x8 = random.nextInt(11) - 5;
                int a8 = random.nextInt(8) + 2;
                int b8 = random.nextInt(21) - 10;
                int c8 = random.nextInt(8) + 2;
                while (a8 == c8) {
                    c8 = random.nextInt(8) + 2;
                }
                int d8 = random.nextInt(21) - 10;
                int e8 = a8 * (x8 + b8) - c8 * (x8 + d8);
                exercise = a8 + "(x + " + b8 + ") = " + c8 + "(x + " + d8 + ") + " + e8;
                result = x8;
                break;

            case 9:
                // רמה 9: משוואה לוגריתמית: log_base(a*x + b) = c
                int base9 = random.nextInt(3) + 2; // בסיס בין 2 ל-4
                int c9 = random.nextInt(3) + 1;
                int target9 = (int) Math.pow(base9, c9);
                // a9 – המשמש לכפל, נבחר בין 2 ל-5
                int a9 = random.nextInt(4) + 2;
                int x9 = random.nextInt(11) - 5;
                int b9 = target9 - a9 * x9;
                exercise = "log_" + base9 + "(" + a9 + "x + " + b9 + ") = " + c9;
                result = x9;
                break;

            case 10:
                // רמה 10: משוואה מעריכית: a^(x) + c = b
                int a10 = random.nextInt(4) + 2; // כבר בין 2 ל-5
                int x10 = random.nextInt(6);
                int c10 = random.nextInt(11) - 5;
                int b10 = (int) Math.pow(a10, x10) + c10;
                exercise = a10 + "^x + " + c10 + " = " + b10;
                result = x10;
                break;

            case 11:
                // רמה 11: משוואת שורש: √(a*x + b) = c   ==> a*x + b = c^2
                int a11 = random.nextInt(4) + 2; // בין 2 ל-5
                int x11 = random.nextInt(11) - 5;
                int c11 = random.nextInt(5) + 1;
                int b11 = c11 * c11 - a11 * x11;
                exercise = "√(" + a11 + "x + " + b11 + ") = " + c11;
                result = x11;
                break;

            default:
                int aDef = random.nextInt(8) + 2;
                int xDef = random.nextInt(11) - 5;
                int bDef = random.nextInt(21) - 10;
                int cDef = aDef * xDef + bDef;
                exercise = aDef + "x + " + bDef + " = " + cDef;
                result = xDef;
                level = 1;
                break;
        }

        return new ExerciseResponse(exercise, result, level, score);
    }








    @RequestMapping(value = "/send-answer", method = RequestMethod.POST)
    public BasicResponse sendAnswer(
            @RequestParam String question,
            @RequestParam int correctAnswer,
            @RequestParam int answerGiven,
            @RequestParam int exerciseLevel,
            @RequestParam String token,
            @RequestParam int type) {
        int errorCode = Constants.FAIL;
        int level = 0;
        int score = 0;
        String correct;
        boolean added = false;

        try {
            UserEntity user = persist.getUserByPass(token);
            if (user == null) {
                return new BasicResponse(false, errorCode);
            }

            if (type == 1) {
                level = persist.getMathematicalExercisesLevelByPass(token);
                score = persist.getMathematicalExercisesScoreByPass(token);
            } else if (type == 2) {
                level = persist.getVerbalQuestionsLevelByUser(user);
                score = persist.getVerbalQuestionsScoreScoreByPass(token);
            } else if (type == 3) {
                level = persist.getVariableQuestionsLevelByPass(token);
                score = persist.getVariableQuestionsScoreByPass(token);
            }

            if (correctAnswer == answerGiven) {
                correct = "Correct";
                score++;
                if (type == 1) {
                    persist.updateMathematicalExercisesScoreByPass(token, 1);
                    if (score >= 3 && level < 12) {
                        persist.incrementMathematicalExercisesLevel(user);
                        persist.updateMathematicalExercisesScoreByPass(token, 0);
                        score = 0;
                    }
                } else if (type == 2) {
                    persist.updateVerbalQuestionsScoreByPass(token, 1);
                    if (score >= 3 && level < 4) {
                        persist.incrementVerbalQuestionsLevelLevel(user);
                        persist.updateVerbalQuestionsScoreByPass(token, 0);
                        score = 0;
                    }
                } else if (type == 3) {
                    persist.updateVariableQuestionsScoreByPass(token, 1);
                    if (score >= 3 && level < 12) {
                        persist.incrementVariableQuestionsLevel(user);
                        persist.updateVariableQuestionsScoreByPass(token, 0);
                        score = 0;
                    }
                }
            } else {
                correct = "Wrong";
                if (type == 1) {
                    persist.updateMathematicalExercisesScoreByPass(token, -1);
                } else if (type == 2) {
                    persist.updateVerbalQuestionsScoreByPass(token, -1);
                } else if (type == 3) {
                    persist.updateVariableQuestionsScoreByPass(token, -1);
                }
            }

            QuestionEntity questionEntity = new QuestionEntity(type, user, correct, exerciseLevel, correctAnswer, question);
            persist.save(questionEntity);
            added = true;
            errorCode = Constants.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new BasicResponse(added, errorCode);
    }


    @RequestMapping(value = "/get-progress-data", method = RequestMethod.GET)
    public ProgressResponse getProgressData(@RequestParam String token) {
        UserEntity user = persist.getUserByPass(token);

        if (user == null) {
            // יש לעדכן את ה־ProgressResponse לקבלת 11 פרמטרים (או להשתמש במבנה אחר)
            return new ProgressResponse(false, "Invalid token", null, null, null, null, null, null, null, null, null);
        }

        int userId = user.getId();
        int mathLevel = persist.getMathematicalExercisesLevelByPass(token);
        int verbalLevel = persist.getVerbalQuestionsLevelByUser(user);
        int variableLevel = persist.getVariableQuestionsLevelByPass(token);

        List<QuestionEntity> userQuestions = persist.getQuestionsByUserId(userId);

        // מתמטיקה
        long totalMathQuestions = userQuestions.stream()
                .filter(q -> q.getType() == 1)
                .count();
        long correctMathAnswers = userQuestions.stream()
                .filter(q -> q.getType() == 1 && q.getCorrect().equalsIgnoreCase("Correct"))
                .count();
        double mathSuccessRate = totalMathQuestions > 0 ? ((double) correctMathAnswers / totalMathQuestions) * 100.0 : 0.0;

        // מיליות (Verbal)
        long totalVerbalQuestions = userQuestions.stream()
                .filter(q -> q.getType() == 2)
                .count();
        long correctVerbalAnswers = userQuestions.stream()
                .filter(q -> q.getType() == 2 && q.getCorrect().equalsIgnoreCase("Correct"))
                .count();
        double verbalSuccessRate = totalVerbalQuestions > 0 ? ((double) correctVerbalAnswers / totalVerbalQuestions) * 100.0 : 0.0;

        // Variable
        long totalVariableQuestions = userQuestions.stream()
                .filter(q -> q.getType() == 3)
                .count();
        long correctVariableAnswers = userQuestions.stream()
                .filter(q -> q.getType() == 3 && q.getCorrect().equalsIgnoreCase("Correct"))
                .count();
        double variableSuccessRate = totalVariableQuestions > 0 ? ((double) correctVariableAnswers / totalVariableQuestions) * 100.0 : 0.0;

        // החזר את כל הנתונים – יש לוודא ש־ProgressResponse מותאם לקבל 11 פרמטרים
        return new ProgressResponse(
                true,
                "Success",
                mathLevel,
                verbalLevel,
                variableLevel,
                (int) correctMathAnswers,
                (int) correctVerbalAnswers,
                (int) correctVariableAnswers,
                Math.round(mathSuccessRate * 100.0) / 100.0,
                Math.round(verbalSuccessRate * 100.0) / 100.0,
                Math.round(variableSuccessRate * 100.0) / 100.0
        );
    }

    @RequestMapping("/get-all-players-progress")
    public List<PlayerProgressResponse> getAllPlayersProgress() {
        List<UserEntity> allUsers = persist.loadList(UserEntity.class);
        List<PlayerProgressResponse> progressList = new ArrayList<>();

        for (UserEntity user : allUsers) {
            int mathematicalLevel = persist.getMathematicalExercisesLevelByUser(user);
            int verbalQuestionsLevel = persist.getVerbalQuestionsLevelByUser(user);
            int variableLevel = persist.getVariableQuestionsLevelByUser(user); // חדש

            List<QuestionEntity> userQuestions = persist.getQuestionsByUserId(user.getId());

            // מתמטיקה
            long totalMathQuestions = userQuestions.stream()
                    .filter(q -> q.getType() == 1)
                    .count();
            long correctMathAnswers = userQuestions.stream()
                    .filter(q -> q.getType() == 1 && q.getCorrect().equalsIgnoreCase("Correct"))
                    .count();
            double mathSuccessRate = totalMathQuestions > 0 ? ((double) correctMathAnswers / totalMathQuestions) * 100.0 : 0.0;

            // מיליות (Verbal)
            long totalVerbalQuestions = userQuestions.stream()
                    .filter(q -> q.getType() == 2)
                    .count();
            long correctVerbalAnswers = userQuestions.stream()
                    .filter(q -> q.getType() == 2 && q.getCorrect().equalsIgnoreCase("Correct"))
                    .count();
            double verbalSuccessRate = totalVerbalQuestions > 0 ? ((double) correctVerbalAnswers / totalVerbalQuestions) * 100.0 : 0.0;

            // Variable
            long totalVariableQuestions = userQuestions.stream()
                    .filter(q -> q.getType() == 3)
                    .count();
            long correctVariableAnswers = userQuestions.stream()
                    .filter(q -> q.getType() == 3 && q.getCorrect().equalsIgnoreCase("Correct"))
                    .count();
            double variableSuccessRate = totalVariableQuestions > 0 ? ((double) correctVariableAnswers / totalVariableQuestions) * 100.0 : 0.0;

            System.out.println("mathSuccessRate: " + Math.round(mathSuccessRate * 100.0) / 100.0);
            System.out.println("verbalSuccessRate: " + Math.round(verbalSuccessRate * 100.0) / 100.0);
            System.out.println("variableSuccessRate: " + Math.round(variableSuccessRate * 100.0) / 100.0);

            progressList.add(new PlayerProgressResponse(
                    user.getUsername(),
                    mathematicalLevel,
                    verbalQuestionsLevel,
                    variableLevel,
                    Math.round(mathSuccessRate * 100.0) / 100.0,
                    Math.round(verbalSuccessRate * 100.0) / 100.0,
                    Math.round(variableSuccessRate * 100.0) / 100.0
            ));
        }
        return progressList;
    }


    // שינוי שם משתמש
    @RequestMapping(value = "/change-username", method = RequestMethod.POST)
    public DetailsResponse changeUsername(@RequestBody Map<String, String> payload) {
        String token = payload.get("token");
        String newUsername = payload.get("newUsername");

        System.out.println("Token received: " + token);
        System.out.println("New username: " + newUsername);

        UserEntity user = persist.getUserByPass(token);
        if (user == null) {
            return new DetailsResponse(false, "User does not exist.", Constants.USER_NOT_EXIST);
        }

        if (!this.isUsernameExists(newUsername)) {
            return new DetailsResponse(false, "Username is already taken.", Constants.USERNAME_TAKEN);
        }

        user.setUsername(newUsername);
        persist.save(user);
        return new DetailsResponse(true, "Username updated successfully.", Constants.SUCCESS);
    }

    // שינוי שם פרטי
    @RequestMapping(value = "/change-first-name", method = RequestMethod.POST)
    public DetailsResponse changeFirstName(@RequestBody Map<String, String> payload) {
        String token = payload.get("token");
        String newFirstName = payload.get("newFirstName");

        System.out.println("Token received: " + token);
        System.out.println("New first name: " + newFirstName);

        UserEntity user = persist.getUserByPass(token);
        if (user == null) {
            return new DetailsResponse(false, "User does not exist.", Constants.USER_NOT_EXIST);
        }

        user.setFirstName(newFirstName);
        persist.save(user);
        return new DetailsResponse(true, "First name updated successfully.", Constants.SUCCESS);
    }

    // שינוי שם משפחה
    @RequestMapping(value = "/change-last-name", method = RequestMethod.POST)
    public DetailsResponse changeLastName(@RequestBody Map<String, String> payload) {
        String token = payload.get("token");
        String newLastName = payload.get("newLastName");

        System.out.println("Token received: " + token);
        System.out.println("New last name: " + newLastName);

        UserEntity user = persist.getUserByPass(token);
        if (user == null) {
            return new DetailsResponse(false, "User does not exist.", Constants.USER_NOT_EXIST);
        }

        user.setLastName(newLastName);
        persist.save(user);
        return new DetailsResponse(true, "Last name updated successfully.", Constants.SUCCESS);
    }

    // שינוי מספר טלפון
    @RequestMapping(value = "/change-phone-number", method = RequestMethod.POST)
    public DetailsResponse changePhoneNumber(@RequestBody Map<String, String> payload) {
        String token = payload.get("token");
        String phoneNumber = payload.get("phoneNumber");

        System.out.println("Token received: " + token);
        System.out.println("Phone number: " + phoneNumber);

        UserEntity user = persist.getUserByPass(token);
        if (user == null) {
            return new DetailsResponse(false, "User does not exist.", Constants.USER_NOT_EXIST);
        }

        if (!this.isValidPhoneNumber(phoneNumber)) {
            return new DetailsResponse(false, "Phone number is already taken.", Constants.PHONE_TAKEN);
        }

        user.setPhoneNumber(phoneNumber);
        persist.save(user);
        return new DetailsResponse(true, "Phone number updated successfully.", Constants.SUCCESS);
    }

    // שינוי אימייל
    @RequestMapping(value = "/change-email", method = RequestMethod.POST)
    public DetailsResponse changeEmail(@RequestBody Map<String, String> payload) {
        String token = payload.get("token");
        String newEmail = payload.get("newEmail");

        System.out.println("Token received: " + token);
        System.out.println("New email: " + newEmail);

        UserEntity user = persist.getUserByPass(token);
        if (user == null) {
            return new DetailsResponse(false, "User does not exist.", Constants.USER_NOT_EXIST);
        }

        if (!this.isEmailExists(newEmail)) {
            return new DetailsResponse(false, "Email is already taken.", Constants.EMAIL_TAKEN);
        }

        user.setEmail(newEmail);
        persist.save(user);
        return new DetailsResponse(true, "Email updated successfully.", Constants.SUCCESS);
    }
    // פונקציה לקבלת פרטי המשתמש
    @RequestMapping("/user-details")
    public UserResponse getUserDetails(@RequestParam String token) {
        System.out.println(token);
        UserEntity user = persist.getUserByPass(token);

        if (user == null) {
            return new UserResponse(false, "Invalid token", null, null, null, null, null);
        }

        return new UserResponse(
                true,
                "Success",
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber()
        );
    }
    @RequestMapping(value = "/generate-exercise-from-template", method = RequestMethod.GET)
    public ExerciseResponse generateExerciseFromTemplate(@RequestParam String token) {
        UserEntity user = persist.getUserByPass(token);
        if (user == null) {
            return new ExerciseResponse("Invalid token", 0, 0, 0);
        }

        int score = persist.getVerbalQuestionsScoreScoreByPass(token);
        int level = persist.getVerbalQuestionsLevelByUser(user);
        Random random = new Random();

        // Fetch question templates for the user level
        List<QuestionTemplateEntity> templates = persist.getQuestionTemplatesByLevel(level);
        if (templates.isEmpty()) {
            return new ExerciseResponse("No templates found for this level", 0, level, score);
        }

        // Choose a random template
        QuestionTemplateEntity selectedTemplate = templates.get(random.nextInt(templates.size()));

        // Parse objects and names
        List<String> objects = Arrays.asList(selectedTemplate.getObjects().split(","));
        List<String> names = Arrays.asList(selectedTemplate.getNames().split(","));

        // Generate random values
        int value1 = 0;
        int value2 = 0;
        int value3 = 0;
        String questionText;
        int result;

        // Adjust values based on operation type
        switch (selectedTemplate.getOperation()) {
            case "addition":
                value1 = random.nextInt(10) + 2; // Numbers start from 2
                value2 = random.nextInt(10) + 2;
                result = value1 + value2;
                break;

            case "subtraction":
                value1 = random.nextInt(20) + 1; // Number 1 is larger
                value2 = random.nextInt(value1) + 1; // Ensures no negative results
                result = value1 - value2;
                break;

            case "multiplication":
                value1 = random.nextInt(10) + 1;
                value2 = random.nextInt(10) + 1;
                result = value1 * value2;
                break;

            case "division":
                value2 = random.nextInt(9) + 2; // Divisor starts from 2
                result = random.nextInt(10) + 2; // Ensure quotient > 1
                value1 = value2 * result; // Dividend is calculated to ensure integer division
                break;

            case "mixed-multiplication-addition":
                value1 = random.nextInt(10) + 1;
                value2 = random.nextInt(10) + 1;
                value3 = random.nextInt(10) + 1; // Additional value for addition
                result = (value1 * value2) + value3;
                break;

            default:
                return new ExerciseResponse("Invalid operation", 0, level, score);
        }

        // Choose random object and name
        String object = objects.get(random.nextInt(objects.size()));
        String name1 = names.get(random.nextInt(names.size()));
        String name2 = names.get(random.nextInt(names.size()));

        // Create the question text
        questionText = selectedTemplate.getTemplate()
                .replace("{name1}", name1)
                .replace("{name2}", name2)
                .replace("{object}", object)
                .replace("{num1}", String.valueOf(value1))
                .replace("{num2}", String.valueOf(value2))
                .replace("{num3}", String.valueOf(value3)); // Include third number for mixed operations

        return new ExerciseResponse(questionText, result, level, score);
    }



    @RequestMapping(value = "/add-question-template", method = RequestMethod.POST)
    public BasicResponse addQuestionTemplate(@RequestBody QuestionTemplateEntity template) {
        try {
            persist.save(template);
            return new BasicResponse(true, Constants.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new BasicResponse(false, Constants.FAIL);
        }
    }

    @RequestMapping(value = "/get-templates", method = RequestMethod.GET)
    public List<QuestionTemplateEntity> getTemplates() {
        return persist.loadList(QuestionTemplateEntity.class);
    }

    @RequestMapping(value = "/get-templates-by-level", method = RequestMethod.GET)
    public List<QuestionTemplateEntity> getTemplatesByLevel(@RequestParam int level) {
        return persist.getTemplatesByLevel(level);
    }


}
