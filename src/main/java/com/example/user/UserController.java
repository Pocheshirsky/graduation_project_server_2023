package com.example.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @PostMapping ("/user")
    public User addUser (@RequestBody User user) {
        return userService.addUser(user);
    }
}
