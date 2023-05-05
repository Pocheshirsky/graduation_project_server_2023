package com.example.user;

import com.example.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

//    @PostMapping("/")
//    public User createUser(@RequestBody User user) {
//        return userService.createUser(user);
//    }

    @PostMapping("/")
    public ResponseEntity<?> changeUSerInfo(@RequestBody User user){
        return authService.changeUserInfo(user);
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
        throw new RuntimeException("fdf");
    }
}
