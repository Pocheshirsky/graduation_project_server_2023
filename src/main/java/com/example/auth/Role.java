package com.example.auth;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "user_roles")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue
    UUID uuid;
    String roleType;

    @Override
    public String getAuthority() {
        return roleType;
    }

    public RoleType getRoleType() {
        return RoleType.valueOf(roleType);
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType.name();
    }
}
