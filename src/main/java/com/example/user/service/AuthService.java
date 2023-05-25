package com.example.user.service;

import com.example.user.model.Role;
import com.example.user.model.RoleType;
import com.example.user.dto.TokenDTO;
import com.example.user.dto.UserDTO;
import com.example.jwt.JwtHelper;
import com.example.token.model.RefreshToken;
import com.example.token.repository.RefreshTokenRepository;
import com.example.user.model.User;
import com.example.user.model.UserInfo;
import com.example.user.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.*;

@RestController
@CrossOrigin
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private Environment env;

    @PostConstruct
    public void runAfterStartup() {
        var user = userRepository.findByUsername("admin");
        if (!user.isPresent()) {
            var dto = new UserDTO();
            dto.setUsername(env.getProperty("admin.username"));
            dto.setPassword(env.getProperty("admin.password"));
            signup(dto, Arrays.asList(RoleType.ADMIN));
        }
    }

    @Transactional
    public ResponseEntity<?> login(UserDTO dto) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setOwner(user);
        refreshTokenRepository.save(refreshToken);

        String accessToken = jwtHelper.generateAccessToken(user);
        String refreshTokenString = jwtHelper.generateRefreshToken(user, refreshToken);

        return ResponseEntity.ok(new TokenDTO(modelMapper.map(user, UserDTO.class), accessToken, refreshTokenString));

    }

    public ResponseEntity<?> changeUserInfo(UserDTO user) {
        if (user.getUuid() == null)
            throw new RuntimeException("user uuid is empty");
        var usr = userService.getUser(user.getUuid());
        if (user.getPassword() != null)
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getUserInfo() != null)
            usr.setUserInfo(modelMapper.map(user.getUserInfo(), UserInfo.class));
        return ResponseEntity.ok(userRepository.save(usr));
    }

    @Transactional
    public ResponseEntity<?> signup(UserDTO dto) {
        return signup(dto, Arrays.asList(RoleType.USER));
    }

    @Transactional
    public ResponseEntity<?> signup(UserDTO dto, Collection<RoleType> roles) {
        Optional<User> usr = userRepository.findByUsername(dto.getUsername());
        if (usr.isPresent()) {
            throw new RuntimeException("User '" + dto.getUsername() + "' alredy exist");
        }
        User user = new User(dto.getUsername(), passwordEncoder.encode(dto.getPassword()));
        if (dto.getUserInfo() != null)
            user.setUserInfo(modelMapper.map(dto.getUserInfo(), UserInfo.class));
        Set<Role> set = new HashSet<>();
        for (var role : roles) {
            set.add(new Role() {{
                setRoleType(role);
            }});
        }
        user.setRoles(set);
        userRepository.save(user);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setOwner(user);
        refreshTokenRepository.save(refreshToken);

        String accessToken = jwtHelper.generateAccessToken(user);
        String refreshTokenString = jwtHelper.generateRefreshToken(user, refreshToken);

        return ResponseEntity.ok(new TokenDTO(modelMapper.map(user, UserDTO.class), accessToken, refreshTokenString));

    }

    @Transactional
    public ResponseEntity<?> logout(TokenDTO dto) {
        String refreshTokenString = dto.getRefreshToken();
        if (jwtHelper.validateRefreshToken(refreshTokenString) && refreshTokenRepository.existsById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString))) {
            // valid and exists in db
            refreshTokenRepository.deleteById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString));
            return ResponseEntity.ok().build();
        }

        throw new BadCredentialsException("invalid token");

    }

    @Transactional
    public ResponseEntity<?> logoutAll(TokenDTO dto) {
        String refreshTokenString = dto.getRefreshToken();
        if (jwtHelper.validateRefreshToken(refreshTokenString) && refreshTokenRepository.existsById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString))) {
            // valid and exists in db
            var a = jwtHelper.getUserIdFromRefreshToken(refreshTokenString);
            refreshTokenRepository.deleteByOwnerUuid(a);
            return ResponseEntity.ok().build();
        }

        throw new BadCredentialsException("invalid token");

    }

    public ResponseEntity<?> accessToken(TokenDTO dto) {
        String refreshTokenString = dto.getRefreshToken();
        if (jwtHelper.validateRefreshToken(refreshTokenString) && refreshTokenRepository.existsById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString))) {

            User user = userService.getUser(jwtHelper.getUserIdFromRefreshToken(refreshTokenString));
            String accessToken = jwtHelper.generateAccessToken(user);

            return ResponseEntity.ok(new TokenDTO(modelMapper.map(user, UserDTO.class), accessToken, refreshTokenString));
        }

        throw new BadCredentialsException("invalid token");

    }

    public ResponseEntity<?> refreshToken(TokenDTO dto) {
        String refreshTokenString = dto.getRefreshToken();
        if (jwtHelper.validateRefreshToken(refreshTokenString) && refreshTokenRepository.existsById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString))) {
            // valid and exists in db

            refreshTokenRepository.deleteById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString));

            User user = userService.getUser(jwtHelper.getUserIdFromRefreshToken(refreshTokenString));

            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setOwner(user);
            refreshTokenRepository.save(refreshToken);

            String accessToken = jwtHelper.generateAccessToken(user);
            String newRefreshTokenString = jwtHelper.generateRefreshToken(user, refreshToken);

            return ResponseEntity.ok(new TokenDTO(modelMapper.map(user, UserDTO.class), accessToken, newRefreshTokenString));
        }

        throw new BadCredentialsException("invalid token");

    }
}
