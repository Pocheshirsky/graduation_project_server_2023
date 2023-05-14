package com.example.chat.repository;


import com.example.chat.model.ChatMessage;
import com.example.chat.model.MessageStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatMessageRepository extends CrudRepository<ChatMessage, UUID> {

    long countBySenderUuidAndRecipientUuidAndStatus(UUID senderUuid, UUID recipientId, MessageStatus status);
//    void update

    List<ChatMessage> findByChatUuid(String chatUuid);

    @Modifying
    @Query("update ChatMessage m set m.status = :status " +
            "where m.senderUuid = :senderUuid " +
            "and  m.recipientUuid = :recipientUuid")
    void updateChatMessageStatus(
            @Param(value = "senderUuid") UUID senderUuid,
            @Param(value = "recipientUuid") UUID recipientUuid,
            @Param(value = "status") MessageStatus status);
}
