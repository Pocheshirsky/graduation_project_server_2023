package com.example.chat.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ChatNotification {

    private UUID id;
    private UUID senderUuid;
    private String senderName;
}
