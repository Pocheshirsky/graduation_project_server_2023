package com.example.searching.model;

import com.example.user.model.UserInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
public class UserPool {

    @Id
    @GeneratedValue()
    private UUID uuid;

    @OneToOne()
    private UserInfo userInfo;
}
