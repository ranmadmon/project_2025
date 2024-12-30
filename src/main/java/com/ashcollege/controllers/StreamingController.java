package com.ashcollege.controllers;

import com.ashcollege.entities.MessageEntity;
import com.ashcollege.service.Persist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/sse")
public class StreamingController {

    private final ConcurrentHashMap<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @Autowired
    private Persist persist;


    @PostConstruct
    public void init() {
        //scheduler.scheduleAtFixedRate(this::sentTestMessage, 0, 5, TimeUnit.SECONDS);
    }

   private void sendMessageHistory(SseEmitter emitter){
       List<MessageEntity> messages = this.persist.loadList(MessageEntity.class);
       try {
           emitter.send(messages);

       }catch (IOException e){
           System.out.println("error");
       }
   }

    private void sentTestMessage() {
        System.out.println("Sending to " + sseEmitters.size());
        for (String token : sseEmitters.keySet()) {
            try {
                sseEmitters.get(token).send("Hello from the server: " + new Date());
            } catch (IOException e) {
                System.err.println("Failed to send message to token: " + token);
                sseEmitters.remove(token);
            }
        }
    }

    @GetMapping("/stream")
    public SseEmitter stream(String token) {
        SseEmitter emitter = new SseEmitter(30 * 60 * 1000L); // 30 minutes
        sseEmitters.put(token, emitter);

        emitter.onCompletion(() -> sseEmitters.remove(token));
        emitter.onError(throwable -> sseEmitters.remove(token));
        emitter.onTimeout(() -> sseEmitters.remove(token));
        sendMessageHistory(emitter);
        return emitter;
    }

    public void sendToAll(MessageEntity message) {
        List<MessageEntity> messages = new ArrayList<>();
        messages.add(message);
        for (String token : sseEmitters.keySet()) {
            //!
            //if (!token.equals(message.getSender().getPassword())) { // Replace with secure check
               try {
                    sseEmitters.get(token).send(messages);
               } catch (IOException e) {
                   System.err.println("Failed to send message to token: " + token);
                   sseEmitters.remove(token);
               }
           // }
        }
    }
}
