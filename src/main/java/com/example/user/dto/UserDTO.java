package com.example.user.dto;

import com.example.user.model.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class UserDTO {

    private UUID uuid;
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private Set<Role> roles;
    private UserInfoDTO userInfo;
}
