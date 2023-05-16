package com.example.user.service;

import com.example.storage.service.StorageService;
import com.example.user.dto.UserDTO;
import com.example.user.model.User;
import com.example.user.model.UserInfo;
import com.example.user.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private StorageService storageService;


    @PostConstruct
    public void setupMapper() {
        modelMapper.createTypeMap(User.class, UserDTO.class).addMappings(m -> {
            m.skip(UserDTO::setPassword);
        });
    }


    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("username not found"));
    }

    public ResponseEntity<?> getUserInfo() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(modelMapper.map(user, UserDTO.class));
    }

    public User getUser(UUID userUUID) {
        return userRepository.findById(userUUID).orElseThrow(() -> new RuntimeException("User " + userUUID + " not found"));
    }

    public void deleteUser(UUID userUUID) {
        userRepository.deleteById(userUUID);
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public ResponseEntity<?> getUserAvatar() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getUserInfo() == null)
            throw new RuntimeException("UserInfo is null");
        if (user.getUserInfo().getAvatar().isEmpty())
            throw new RuntimeException("Avatar is empty");
        Resource resource = storageService.load(user.getUserInfo().getUuid().toString());

        try {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + user.getUserInfo().getAvatar() + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(Path.of(user.getUserInfo().getAvatar()))).body(resource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<?> setUserAvatar(MultipartFile file) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserInfo info;
        if (user.getUserInfo() == null) {
            info = new UserInfo();
        } else info = user.getUserInfo();

        info.setAvatar(file.getOriginalFilename());
        user.setUserInfo(info);
        userRepository.save(user);
        storageService.save(file, info.getUuid().toString());

        return ResponseEntity.ok().build();
    }
}
