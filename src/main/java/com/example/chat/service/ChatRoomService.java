package com.example.chat.service;


import com.example.chat.model.ChatRoom;
import com.example.chat.repository.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    public Optional<String> getChatUuid(UUID senderUuid, UUID recipientUuid, boolean createIfNotExist) {
        return chatRoomRepository
                .findBySenderUuidAndRecipientUuid(senderUuid, recipientUuid)
                .map(ChatRoom::getChatUuid)
                .or(() -> {
                    if (!createIfNotExist) {
                        return Optional.empty();
                    }
                    var chatUuid = String.format("%s_%s", senderUuid, recipientUuid);

                    ChatRoom senderRecipient = ChatRoom
                            .builder()
                            .chatUuid(chatUuid)
                            .senderUuid(senderUuid)
                            .recipientUuid(recipientUuid)
                            .build();

                    ChatRoom recipientSender = ChatRoom
                            .builder()
                            .chatUuid(chatUuid)
                            .senderUuid(recipientUuid)
                            .recipientUuid(senderUuid)
                            .build();
                    chatRoomRepository.save(senderRecipient);
                    chatRoomRepository.save(recipientSender);

                    return Optional.of(chatUuid);
                });
    }
}
