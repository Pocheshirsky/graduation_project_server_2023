package com.example.searching.controller;

import com.example.searching.model.PoolMessage;
import com.example.searching.service.SearchingService;
import com.example.user.model.User;
import com.example.user.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/api/searching")
public class SearchingController {

    @Autowired
    private SearchingService searchingService;

    @GetMapping("/he")
    private void hne() {
        searchingService.getNewUserInterlocutor();
    }

    @PostMapping("/user")
    private void addUserInPool() {
        searchingService.addUserInPool();
    }

    @GetMapping("/user")
    private ResponseEntity<?> getAllUserInPool() {
        return ResponseEntity.ok(searchingService.getUsersList());
    }

    @DeleteMapping("/user")
    private void deleteUserInPool() {
        searchingService.deleteUserFromPool();
    }

    @GetMapping("/message-pool")
    private List<PoolMessage> findUserPoolMessages() { return searchingService.findUserPoolMessages(); }

    @PutMapping("/message-pool/{messageUuid}/{userInfoUuid}")
    private User updateUserPoolMessageStatus(@PathVariable UUID messageUuid, @PathVariable UUID userInfoUuid) {
        return searchingService.updateStatuses(messageUuid, userInfoUuid);
    }
}
