package com.example.chat.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@Entity
public class ChatMessage {
    @Id
    @GeneratedValue()
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
