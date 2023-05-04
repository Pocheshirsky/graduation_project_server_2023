package com.example.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        if(user.getUuid() == null)
            user.setDatedOfEmployment(new Timestamp(new Date().getTime()));
        return userRepository.save(user);
    }

    public User getUser(UUID userUUID) {
        return userRepository.findById(userUUID).orElseThrow(() -> new RuntimeException("User not found"));
       }

    public void deleteUser(UUID userUUID) {
        userRepository.deleteById(userUUID);
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }
}
