package com.example.user.controller;

import com.example.user.service.AuthService;
import com.example.user.dto.TokenDTO;
import com.example.user.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO dto) {
        return authService.login(dto);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserDTO dto) {
        return authService.signup(dto);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody TokenDTO dto) {
        return authService.logout(dto);
    }

    @PostMapping("/logout-all")
    public ResponseEntity<?> logoutAll(@RequestBody TokenDTO dto) {
        return authService.logoutAll(dto);
    }

    @PostMapping("/access-token")
    public ResponseEntity<?> accessToken(@RequestBody TokenDTO dto) {
        return authService.accessToken(dto);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody TokenDTO dto) {
        return authService.refreshToken(dto);
    }
}
