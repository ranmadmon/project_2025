package com.ashcollege.controllers;

import com.ashcollege.entities.ProgressNotificationEntity;
import com.ashcollege.entities.UserEntity;
import com.ashcollege.service.Persist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class ProgressNotificationController {

    @Autowired
    private Persist persist;

    /**
     * Endpoint לשליפת ההתראות עבור משתמש לפי token.
     */
    @GetMapping("/get-notifications")
    public List<ProgressNotificationEntity> getNotifications(@RequestParam String token) {
        UserEntity user = persist.getUserByPass(token);
        if (user != null) {
            return persist.getNotificationsByUser(user);
        }
        return null;
    }

    /**
     * Endpoint לסימון התראה כנקראת.
     */
    @PostMapping("/mark-notification-read")
    public void markNotificationRead(@RequestParam int notificationId) {
        persist.markNotificationAsRead(notificationId);
    }
}
