package com.example.user.controller;

import com.example.user.service.AuthService;
import com.example.user.dto.UserDTO;
import com.example.user.model.User;
import com.example.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;


    @PostMapping("/")
    public ResponseEntity<?> changeUSerInfo(@RequestBody UserDTO user) {
        return authService.changeUserInfo(user);
    }

    @GetMapping("/{uuid}")
    public User getUser(@PathVariable UUID uuid) {
        return userService.getUserByUuid(uuid);
    }

    @GetMapping("/all")
    public Iterable<User> getALLUser() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/{uuid}")
    public void deleteUser(@PathVariable UUID uuid) {
        userService.deleteUser(uuid);
    }

    @PostMapping("/test")
    public String test(@RequestBody UserDTO dto) {
        throw new RuntimeException("HWHWHWHW");
//        return "sd";
    }

    @GetMapping("/userinfo")
    public ResponseEntity<?> getUserInfo(){
        return userService.getUserInfo();
    }

    @GetMapping("/avatar/{userUuid}")
    public ResponseEntity<?> getUserAvatar(@PathVariable UUID userUuid) { return userService.getUserAvatar(userUuid); }

    @PostMapping("/avatar")
    public ResponseEntity<?> setUserAvatar(@RequestBody MultipartFile file) { return userService.setUserAvatar(file); }
}
