package com.example.chat.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ChatRoom {
    private UUID uuid;
    private String chatUuid;
    private UUID senderUuid;
    private UUID recipientUuid;
}
