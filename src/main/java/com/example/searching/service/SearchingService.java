package com.example.searching.service;

import com.example.searching.model.UserPool;
import com.example.searching.repository.UserPoolRepository;
import com.example.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SearchingService {

    @Autowired
    private UserPoolRepository userPoolRepository;

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

    public Iterable<UserPool> getUsersList() {
        return userPoolRepository.findAll();
    }
}
