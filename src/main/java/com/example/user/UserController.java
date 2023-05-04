package com.example.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/")
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("/{uuid}")
    public User getUser(@PathVariable UUID uuid) {
        return userService.getUser(uuid);
    }

    @GetMapping("/all")
    public Iterable<User> getALLUser() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/{uuid}")
    public void deleteUser(@PathVariable UUID uuid) {
        userService.deleteUser(uuid);
    }

    @GetMapping("/test")
    public String test() {
        return "hne";
    }
}
