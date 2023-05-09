package com.example.user;

import com.example.auth.AuthService;
import com.example.dto.UserDTO;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
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

    @PostMapping("/test")
    public String test(@RequestBody UserDTO dto) {

        return "sd";
    }

    @GetMapping("/userinfo")
    private ResponseEntity<?> getUserInfo(){
        return userService.getUserInfo();
    }
}
