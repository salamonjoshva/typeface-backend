package com.typeface.notification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    public NotificationController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @SendTo("/topic/message")
    public String broadcastMessage(@Payload String textMessageDTO) {
        return textMessageDTO;
    }

    @PostMapping("/send")
    public ResponseEntity<Void> sendMessage(@RequestHeader String message) {
        messagingTemplate.convertAndSend("/topic/message", message);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
