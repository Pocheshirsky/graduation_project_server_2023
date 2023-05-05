package com.example.auth;

import com.example.dto.LoginDTO;
import com.example.dto.SignupDTO;
import com.example.dto.TokenDTO;
import com.example.jwt.JwtHelper;
import com.example.token.RefreshToken;
import com.example.token.RefreshTokenRepository;
import com.example.user.User;
import com.example.user.UserRepository;
import com.example.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.*;

@RestController
public class AuthService {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtHelper jwtHelper;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserService userService;

    @Autowired
    private Environment env;

    @PostConstruct
    public void runAfterStartup() {
        var user = userRepository.findByUsername("admin");
        if (!user.isPresent()) {
            var dto = new SignupDTO();
            dto.setUsername(env.getProperty("admin.username"));
            dto.setPassword(env.getProperty("admin.password"));
            signup(dto, Arrays.asList(RoleType.ADMIN));
        }
    }

    @Transactional
    public ResponseEntity<?> login(LoginDTO dto) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setOwner(user);
        refreshTokenRepository.save(refreshToken);

        String accessToken = jwtHelper.generateAccessToken(user);
        String refreshTokenString = jwtHelper.generateRefreshToken(user, refreshToken);

        return ResponseEntity.ok(new TokenDTO(user.getRoles().toString(), accessToken, refreshTokenString));

    }

    public ResponseEntity<?> changeUserInfo(User user) {
        if (user.getUuid() == null)
            throw new RuntimeException("user uuid is empty");
        var usr = userService.getUser(user.getUuid());
        if (user.getPassword() != null)
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        return ResponseEntity.ok(userRepository.save(user));
    }

    @Transactional
    public ResponseEntity<?> signup(SignupDTO dto) {
        return signup(dto, Arrays.asList(RoleType.USER));
    }

    @Transactional
    public ResponseEntity<?> signup(SignupDTO dto, Collection<RoleType> roles) {
        Optional<User> usr = userRepository.findByUsername(dto.getUsername());
        if (usr.isPresent()) {
            throw new RuntimeException("User '" + dto.getUsername() + "' alredy exist");
        }
        User user = new User(dto.getUsername(), passwordEncoder.encode(dto.getPassword()));
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

        return ResponseEntity.ok(new TokenDTO(user.getUuid().toString(), accessToken, refreshTokenString));

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

            return ResponseEntity.ok(new TokenDTO(user.getUuid().toString(), accessToken, refreshTokenString));
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

            return ResponseEntity.ok(new TokenDTO(user.getUuid().toString(), accessToken, newRefreshTokenString));
        }

        throw new BadCredentialsException("invalid token");

    }
}
