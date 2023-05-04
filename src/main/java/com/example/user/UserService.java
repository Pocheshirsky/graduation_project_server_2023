package com.example.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    public User addUser(User user) {

        user.setDatedOfEmployment(new Timestamp(new Date().getTime()));
        return userRepository.save(user);
    }
}
