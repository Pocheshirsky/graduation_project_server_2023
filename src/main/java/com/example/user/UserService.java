package com.example.user;

import com.example.dto.UserDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

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

    public ResponseEntity<?> getUserInfo(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return ResponseEntity.ok(modelMapper.map(user,UserDTO.class));
    }

    public User getUser(UUID userUUID) {
        return userRepository.findById(userUUID).orElseThrow(() -> new RuntimeException("User "+userUUID+" not found"));
    }

    public void deleteUser(UUID userUUID) {
        userRepository.deleteById(userUUID);
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }
}
