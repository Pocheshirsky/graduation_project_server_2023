package com.example.chat.controller;


import com.example.chat.model.ChatMessage;
import com.example.chat.model.ChatNotification;
import com.example.chat.service.ChatMessageService;
import com.example.chat.service.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@CrossOrigin
@RestController
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private ChatMessageService chatMessageService;
    @Autowired
    private ChatRoomService chatRoomService;

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        var chatId = chatRoomService.getChatUuid(chatMessage.getSenderUuid(), chatMessage.getRecipientUuid(), true);
        chatMessage.setChatUuid(chatId.get());
        ChatMessage saved = chatMessageService.save(chatMessage);
        messagingTemplate.convertAndSendToUser(chatMessage.getRecipientUuid().toString(),
                "/queue/messages", chatMessage.getContent());
    }

    @GetMapping("/chat/{text}")
    public void processMesssage(@PathVariable String text) {
        messagingTemplate.convertAndSend("/user/hne", text);
    }

    @GetMapping("/messages/{senderUuid}/{recipientUuid}/count")
    public ResponseEntity<Long> countNewMessages(@PathVariable UUID senderUuid, @PathVariable UUID recipientUuid) {
        return ResponseEntity.ok(chatMessageService.countNewMessages(senderUuid, recipientUuid));
    }

    @GetMapping("/messages/{senderUuid}/{recipientUuid}")
    public ResponseEntity<?> findChatMessages(@PathVariable UUID senderUuid, @PathVariable UUID recipientUuid) {
        return ResponseEntity.ok(chatMessageService.findChatMessages(senderUuid, recipientUuid));
    }

    @GetMapping("/messages/{messageUuid}")
    public ResponseEntity<?> findMessage(@PathVariable UUID messageUuid) {
        return ResponseEntity.ok(chatMessageService.findById(messageUuid));
    }
}
