package com.example.chat.repository;

import com.example.chat.model.ChatRoom;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatRoomRepository extends CrudRepository<ChatRoom, UUID> {
    Optional<ChatRoom> findBySenderUuidAndRecipientUuid(UUID senderUuid, UUID recipientUuid);

    List<ChatRoom> findAllBySenderUuid(UUID senderUuid);
}
