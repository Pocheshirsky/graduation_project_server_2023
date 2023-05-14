package com.example.chat.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.util.UUID;

@Data
public class ChatMessage {
    private UUID uuid;
    private String chatUuid;
    private UUID senderUuid;
    private UUID recipientUuid;
    private String senderName;
    private String recipientName;
    private String content;
    @DateTimeFormat()
    private Timestamp timestamp;
    private MessageStatus status;
}
