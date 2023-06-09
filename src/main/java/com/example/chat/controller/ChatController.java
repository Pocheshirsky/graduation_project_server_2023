package com.example.chat.controller;


import com.example.chat.model.ChatMessage;
import com.example.chat.service.ChatMessageService;
import com.example.chat.service.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/api")
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
        messagingTemplate.convertAndSendToUser(saved.getRecipientUuid().toString(),
                "/queue/messages", chatMessage);
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

    @GetMapping("/messages/chat/{senderUuid}")
    public ResponseEntity<?> findUserChats(@PathVariable UUID senderUuid) {
        return ResponseEntity.ok(chatMessageService.findUserChats(senderUuid));
    }

    @DeleteMapping("/messages/chat/{userUuid}/{recipientUuid}")
    public void deleteChat (@PathVariable UUID userUuid, @PathVariable UUID recipientUuid) {
        chatRoomService.deleteUserChatRoom(userUuid, recipientUuid);
    }
}
