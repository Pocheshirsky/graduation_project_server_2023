package com.example.searching.service;

import com.example.searching.model.UserPool;
import com.example.searching.repository.UserPoolRepository;
import com.example.user.model.User;
import com.example.user.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchingService {

    @Autowired
    private UserPoolRepository userPoolRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

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

    public Iterable<UserPool> getUsersList() {
        return userPoolRepository.findAll();
    }
}
