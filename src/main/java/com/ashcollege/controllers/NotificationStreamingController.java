package com.ashcollege.controllers;

import com.ashcollege.entities.MessageEntity;
import com.ashcollege.entities.NotificationEntity;
import com.ashcollege.service.Persist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/sseNotification")
public class NotificationStreamingController {

    private final ConcurrentHashMap<String, SseEmitter> sseNotificationEmitters = new ConcurrentHashMap<>();
@Autowired
private Persist persist;


    private void sendNotification(SseEmitter emitter){
        List<NotificationEntity> notifications = this.persist.loadList(NotificationEntity.class);
        try {
            emitter.send(notifications);

        }catch (IOException e){
            System.out.println("error");
        }
    }


    @GetMapping("/stream")
    public SseEmitter stream(String token) {
        SseEmitter emitter = new SseEmitter(30 * 60 * 1000L); // 30 minutes
        sseNotificationEmitters.put(token, emitter);

        emitter.onCompletion(() -> sseNotificationEmitters.remove(token));
        emitter.onError(throwable -> sseNotificationEmitters.remove(token));
        emitter.onTimeout(() -> sseNotificationEmitters.remove(token));
        sendNotification(emitter);
        return emitter;
    }

    public void sendNotificationToAll(NotificationEntity notification) {
        {
            List<NotificationEntity> notifications = new ArrayList<>();
            notifications.add(notification);
            for (String token : sseNotificationEmitters.keySet()) {
                try {
                    sseNotificationEmitters.get(token).send(notifications);
                } catch (IOException e) {
                    System.err.println("Failed to send message to token: " + token);
                    sseNotificationEmitters.remove(token);
                }

            }
        }
    }
}
