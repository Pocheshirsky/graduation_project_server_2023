package com.example.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class UserInfoDTO {
    private UUID uuid;
    private String name;
    private int age;
    private List<String> hehe;
}
