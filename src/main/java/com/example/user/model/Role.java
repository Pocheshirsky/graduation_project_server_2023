package com.example.user.model;

import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.UUID;

@Entity
@ToString
@Table(name = "user_roles")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue
    UUID uuid;
    @Enumerated(EnumType.STRING)
    RoleType roleType;

    @Override
    public String getAuthority() {
        return roleType.name();
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }
}
