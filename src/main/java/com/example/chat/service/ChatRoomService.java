package com.example.chat.service;


import com.example.chat.model.ChatRoom;
import com.example.chat.repository.ChatRoomRepository;
import com.example.user.dto.UserDTO;
import com.example.user.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<UserDTO> findUserChats(UUID senderUuid) {
        var Users = chatRoomRepository.findAllBySenderUuid(senderUuid).stream()
                .map(chatRoom -> chatRoom.getRecipientUuid()).collect(Collectors.toSet());
        return StreamSupport.stream(userRepository.findAllById(Users).spliterator(),false)
                .map(user -> modelMapper.map(user,UserDTO.class)).collect(Collectors.toList());
    }

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
