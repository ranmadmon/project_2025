package com.ashcollege.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/sse")
public class StreamingController {
    private List<SseEmitter> sseEmitters = new ArrayList<>();

    @PostConstruct
    public void init () {
        new Thread(() -> {
            while (true) {
                try {
                    System.out.println("Sending");
                    sentTestMesssage();
                    try {
                        Thread.sleep(5 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void sentTestMesssage () {
        System.out.println("sending to " + sseEmitters.size());
        try {
            for (SseEmitter sseEmitter : sseEmitters) {
                try {
                    sseEmitter.send("Hello from the server: " + new Date());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/stream")
    public SseEmitter stream () {
        System.out.println("created emitter");
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        this.sseEmitters.add(emitter);
        emitter.onCompletion(() -> {
            sseEmitters.remove(emitter);
        });
        emitter.onError((throwable) -> {
            sseEmitters.remove(emitter);
        });
        emitter.onTimeout(() -> {
            sseEmitters.remove(emitter);
        });
        return emitter;

    }
}
