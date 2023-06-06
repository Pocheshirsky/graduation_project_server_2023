package com.example.searching.service;

import com.example.chat.model.ChatMessage;
import com.example.chat.model.MessageStatus;
import com.example.searching.model.PoolMessage;
import com.example.searching.model.UserPool;
import com.example.searching.repository.PoolMessageRepository;
import com.example.searching.repository.UserPoolRepository;
import com.example.user.dto.UserDTO;
import com.example.user.model.User;
import com.example.user.model.UserInfo;
import com.example.user.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class SearchingService {

    @Autowired
    private UserPoolRepository userPoolRepository;
    @Autowired
    private PoolMessageRepository poolMessageRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    public void addUserInPool() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getUserInfo() != null) {
            if (userPoolRepository.existsByUserInfoUuid(user.getUserInfo().getUuid()))
                throw new RuntimeException("User already in pool");
            UserPool userInPool = new UserPool();
            userInPool.setUserUuid(user.getUuid());
            userInPool.setUserInfo(user.getUserInfo());
            userPoolRepository.save(userInPool);
        } else throw new RuntimeException("UserInfo is not created");
    }

    @Transactional
    public void deleteUserFromPool() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getUserInfo() != null) {
            userPoolRepository.deleteByUserInfoUuid(user.getUserInfo().getUuid());
        } else throw new RuntimeException("UserInfo is not created");
    }

    //@Scheduled(fixedDelay = 60_000)
    public void getNewUserInterlocutor() {
        System.err.println("Searching created");
        Iterable<UserPool> userPools = getUsersList();
        List<UserPool> userWithPartner = new ArrayList<>();
        for (var userPl : userPools) {
            var userPartnerList = userPoolRepository.findUserInfoByPredicate(userPl.getUserInfo());
            var poolMessagesList = userPartnerList.stream().map(userPool -> {
                var pMessage = new PoolMessage();
                pMessage.setUserUuid(userPool.getUserUuid());
                pMessage.setFoundUserInfo(userPool.getUserInfo());
                pMessage.setStatus(MessageStatus.DELIVERED);
                pMessage.setTimestamp(new Timestamp(new Date().getTime()));
                return pMessage;
            }).toList();

            if (!poolMessagesList.isEmpty()) {
                poolMessageRepository.saveAll(poolMessagesList);
                messagingTemplate.convertAndSend("/user/" + userPl.getUserInfo().getUuid() + "/hne", poolMessagesList);

                userWithPartner.add(userPl);
            }
        }
        userPoolRepository.deleteAll(userWithPartner);
    }

    public List<PoolMessage> findUserPoolMessages() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return poolMessageRepository.findPoolMessageByUserUuidAndStatusOrderByTimestampAsc(user.getUserInfo().getUuid(), MessageStatus.DELIVERED);
    }

    @Transactional
    public User updateStatuses(UUID messageUuid, UUID userInfoUuid) {
        poolMessageRepository.updatePoolMessageStatus(messageUuid, MessageStatus.RECEIVED);
        return userRepository.findByUserInfoUuid(userInfoUuid).orElseThrow(() -> new RuntimeException());
    }

    public Iterable<UserPool> getUsersList() {
        return userPoolRepository.findAll();
    }
}
