package com.example.user.dto;

import com.example.user.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TokenDTO {
    private UserDTO user;
    private String accessToken;
    private String refreshToken;
}
