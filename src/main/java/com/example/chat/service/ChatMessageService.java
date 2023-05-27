package com.example.chat.service;


import com.example.chat.model.ChatMessage;
import com.example.chat.model.MessageStatus;
import com.example.chat.repository.ChatMessageRepository;
import com.example.user.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ChatMessageService {
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private ChatRoomService chatRoomService;

    public ChatMessage save(ChatMessage chatMessage) {
        chatMessage.setStatus(MessageStatus.RECEIVED);
        chatMessageRepository.save(chatMessage);
        return chatMessage;
    }

    public long countNewMessages(UUID senderUuid, UUID recipientUuid) {
        return chatMessageRepository.countBySenderUuidAndRecipientUuidAndStatus(
                senderUuid, recipientUuid, MessageStatus.RECEIVED);
    }

    public List<UserDTO> findUserChats(UUID senderUuid){
        return chatRoomService.findUserChats(senderUuid);
    }

//    @Scheduled(fixedDelay = 100_000)
//    public void getHne(){
//        System.err.println("hne");
//    }

    @Transactional
    public List<ChatMessage> findChatMessages(UUID senderUuid, UUID recipientUuid) {
        var chatUuid = chatRoomService.getChatUuid(senderUuid, recipientUuid, false); //TODO: возможно переделать на true!!!!!!!

        var messages =
                chatUuid.map(cId -> chatMessageRepository.findByChatUuidOrderByTimestampAsc(cId)).orElse(new ArrayList<>());

        if (messages.size() > 0) {
            updateStatuses(senderUuid, recipientUuid, MessageStatus.DELIVERED);
        }

        return messages;
    }

    public ChatMessage findById(UUID uuid) {
        return chatMessageRepository
                .findById(uuid)
                .map(chatMessage -> {
                    chatMessage.setStatus(MessageStatus.DELIVERED);
                    return chatMessageRepository.save(chatMessage);
                })
                .orElseThrow(() ->
                        new RuntimeException("can't find message (" + uuid + ")"));
    }

    @Transactional
    public void updateStatuses(UUID senderUuid, UUID recipientUuid, MessageStatus status) {
        chatMessageRepository.updateChatMessageStatus(senderUuid, recipientUuid, status);
    }
}
