package com.ashcollege.utils;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class GeneralUtils {
    private static final int OTP_MIN_VALUE = 100000;
    private static final int OTP_MAX_VALUE = 999999;



    public static String hashMd5 (String source) {
        try {
            return DatatypeConverter.printHexBinary( MessageDigest.getInstance("MD5").digest(source.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    public static String generateOtp() {
        Random random = new Random();
        return String.valueOf(random.nextInt(OTP_MIN_VALUE, OTP_MAX_VALUE ));
    }

    public static String checkPhoneNumber(String phoneNumber) {
        String digits = Constants.DIGITS;
        String char1;
        String newPhoneNumber="";
        String checkNum;
        if (phoneNumber.length()>13||phoneNumber.length()<10){
            newPhoneNumber="";
        }
        else {
            if (phoneNumber.length()==10){
                newPhoneNumber=phoneNumber.substring(0,2);
                if (!newPhoneNumber.equals("05")){
                    newPhoneNumber="";
                }
                else {
                    for (int i=0;i<phoneNumber.length();i++){
                        char1 = phoneNumber.charAt(i) + "";
                        if (!digits.contains(char1)){
                            newPhoneNumber="";
                            break;

                        }}
                    if (!newPhoneNumber.equals("")){
                        newPhoneNumber=phoneNumber.substring(0,3);
                        newPhoneNumber+='-'+phoneNumber.substring(3,10);
                    }

                } }else if (phoneNumber.length()==11) {
                newPhoneNumber=phoneNumber.substring(0,2);
                if (!newPhoneNumber.equals("05")){
                    newPhoneNumber="";
                }else {
                    char1 = phoneNumber.charAt(3) + "";
                    if (char1.equals("-")){
                        for (int i=2;i<phoneNumber.length();i++){
                            if (i==3){
                                continue;
                            }
                            char1=phoneNumber.charAt(i)+ "";
                            if (!digits.contains(char1)){
                                newPhoneNumber="";
                                break;
                            }
                        }
                        if (!newPhoneNumber.equals("")) {
                            newPhoneNumber=phoneNumber;
                        }

                    }

                }}
            else {
                newPhoneNumber=phoneNumber.substring(0,4);
                if (!newPhoneNumber.equals("9725")){
                    newPhoneNumber="";
                }
                else {
                    for (int i=4;i<phoneNumber.length();i++){
                        char1 = phoneNumber.charAt(i) + "";
                        if (!digits.contains(char1)){
                            newPhoneNumber="";
                            break;

                        }}
                    if (!newPhoneNumber.equals("")){
                        newPhoneNumber="05"+phoneNumber.charAt(4)+"-"+phoneNumber.substring(5);
                    }
                }

            }
        }


        return newPhoneNumber;
    }
}
