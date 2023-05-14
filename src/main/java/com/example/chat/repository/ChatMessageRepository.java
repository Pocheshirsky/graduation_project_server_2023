package com.example.chat.repository;


import com.example.chat.model.ChatMessage;
import com.example.chat.model.MessageStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatMessageRepository extends CrudRepository<ChatMessage, UUID> {

    long countBySenderUuidAndRecipientUuidAndStatus(UUID senderId, UUID recipientId, MessageStatus status);
//    void update

    List<ChatMessage> findByChatId(String chatId);
}
