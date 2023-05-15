package com.example.user.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.UUID;

@Entity
@Data
public class UserInfo {

    @Id
    @GeneratedValue
    private UUID uuid;

    private String name;

    private int age;

    private String avatar;
}
