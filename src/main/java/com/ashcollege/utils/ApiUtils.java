package com.ashcollege.utils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class ApiUtils {
    public static void main(String[] args) {
        sendSms("Hi", List.of("0546369855", "0502392298", "0526990440"));
    }

    public static boolean sendSms (String text, List<String> phones) {
        String token = Constants.SMS_TOKEN;
        String url = "https://capi.inforu.co.il/api/v2/SMS/SendSms";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Message", text);
        JSONArray recipients = new JSONArray();
        for (String phone : phones) {
            JSONObject recipient = new JSONObject();
            recipient.put("Phone", phone);
            recipients.put(recipient);

        }
        jsonObject.put("Recipients", recipients);
        JSONObject settingsJson = new JSONObject();
        settingsJson.put("Sender", "KB");
        jsonObject.put("Settings", settingsJson);
        System.out.println(jsonObject);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        httpHeaders.add("Authorization", "Basic " + token);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<?> httpEntity = new HttpEntity<Object>(jsonObject.toString(), httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
        return true;

    }
}
