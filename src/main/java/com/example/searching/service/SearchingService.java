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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    private ModelMapper modelMapper;

    public void addUserInPool() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getUserInfo() != null) {
            if (userPoolRepository.existsByUserInfoUuid(user.getUserInfo().getUuid()))
                throw new RuntimeException("User already in pool");
            UserPool userInPool = new UserPool();
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

    public void getNewUserInterlocutor() {
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        var info = user.getUserInfo();
        Iterable<UserPool> userPools = getUsersList();
        List<UserPool> userWithPartner = new ArrayList<>();
        for (var user : userPools) {
            var userPartnerList = userPoolRepository.findUserInfoByPredicate(user.getUserInfo());
//            messagingTemplate.convertAndSend("/user/"+user.getUserInfo().getUuid()+"/hne",users);

            if (!userPartnerList.isEmpty()) {
                messagingTemplate.convertAndSend("/user/"+user.getUserInfo().getUuid()+"/hne", userPartnerList);

                userWithPartner.add(user);
            }
        }
//        userPoolRepository.deleteAll(userWithPartner);
    }

    public List<PoolMessage> savePoolMessages(List<UserInfo> userPartnerList) {
        var PoolMessages = UserPoolRepository.findAllByUserInfoList(userPartnerList).stream()
                .map(userPool -> userPool.getUuid()).collect(Collectors.toSet());
        return StreamSupport.stream(userPoolRepository.findAllByUserInfoList(userPartnerList).spliterator(),false)
                .map(user -> modelMapper.map(user,UserDTO.class)).collect(Collectors.toList());
    }

    public List<PoolMessage> savePoolMessages(List<PoolMessage> poolMessages) {
        return poolMessages.stream()
                .map(this::savePoolMessage)
                .collect(Collectors.toList());
    }

    //Зная UserPool сохранить текущего User, найденного User и UserInfo найденного User?????????????????????????????
    public void savePoolMessages(List<UserInfo> userInfoList) {
        Stream<UserInfo> stream = userInfoList.stream();
    }

    public void savePoolMessages(List<UserInfo> userInfoList) {
        List<Object[]> poolMessage = userInfoList.stream()
                .map(userInfo -> new Object[] )//?????????????????????
                .collect(Collectors.toList());
    }

    //Ну хуйня же ну
    public void createPoolMessage(UserInfo userInfo){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PoolMessage poolMessage = new PoolMessage();
        poolMessage.setUserUuid(user.getUuid());
        poolMessage.setFoundUserUuid();
        poolMessage.setFoundUserInfo(userInfo);
    }

    //

    public List<PoolMessage> findUserPoolMessages() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return poolMessageRepository.findPoolMessageByUserUuidAndStatusOrderByTimestampAsc(user.getUuid(), MessageStatus.DELIVERED);
    }

    @Transactional
    public void updateStatuses(UUID messageUuid) {
        poolMessageRepository.updatePoolMessageStatus(messageUuid, MessageStatus.RECEIVED);
    }

    public Iterable<UserPool> getUsersList() {
        return userPoolRepository.findAll();
    }
}
