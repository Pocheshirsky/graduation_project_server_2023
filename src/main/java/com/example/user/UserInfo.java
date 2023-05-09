package com.example.user;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
public class UserInfo {

    @Id
    @GeneratedValue
    private UUID uuid;

    private String name;

    private int age;
}
