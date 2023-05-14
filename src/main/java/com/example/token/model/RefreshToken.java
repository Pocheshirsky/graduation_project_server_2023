package com.example.token.model;

import com.example.user.model.User;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Data
@Entity
public class RefreshToken {
    @Id
    @GeneratedValue
    UUID uuid;

    @ManyToOne
    private User owner;
}
