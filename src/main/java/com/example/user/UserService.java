package com.example.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("username not found"));
    }

    public User createUser(User user) {
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
