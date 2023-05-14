package com.example.chat.service;


import com.example.chat.model.ChatMessage;
import com.example.chat.model.MessageStatus;
import com.example.chat.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ChatMessageService {
    @Autowired private ChatMessageRepository repository;
    @Autowired private ChatRoomService chatRoomService;


    public ChatMessage save(ChatMessage chatMessage) {
        chatMessage.setStatus(MessageStatus.RECEIVED);
        repository.save(chatMessage);
        return chatMessage;
    }

    public long countNewMessages(UUID senderUuid, UUID recipientUuid) {
        return repository.countBySenderUuidAndRecipientUuidAndStatus(
                senderUuid, recipientUuid, MessageStatus.RECEIVED);
    }

    public List<ChatMessage> findChatMessages(UUID senderUuid, UUID recipientUuid) {
        var chatId = chatRoomService.getChatUuid(senderUuid, recipientUuid, false);

        var messages =
                chatId.map(cId -> repository.findByChatId(cId)).orElse(new ArrayList<>());

        if(messages.size() > 0) {
            updateStatuses(senderUuid, recipientUuid, MessageStatus.DELIVERED);
        }

        return messages;
    }

    public ChatMessage findById(UUID uuid) {
        return repository
                .findById(uuid)
                .map(chatMessage -> {
                    chatMessage.setStatus(MessageStatus.DELIVERED);
                    return repository.save(chatMessage);
                })
                .orElseThrow(() ->
                        new RuntimeException("can't find message (" + uuid + ")"));
    }

    public void updateStatuses(UUID senderUuid, UUID recipientUuid, MessageStatus status) {
//        Query query = new Query(
//                Criteria
//                        .where("senderUuid").is(senderUuid)
//                        .and("recipientUuid").is(recipientUuid));
//        Update update = Update.update("status", status);
//        mongoOperations.updateMulti(query, update, ChatMessage.class);
    }
}
