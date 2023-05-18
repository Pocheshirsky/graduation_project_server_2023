package com.example.searching.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Data
@Entity
public class UserPool {

    @Id
    @GeneratedValue()
    private UUID uuid;
}
