package com.example.user.repository;

import com.example.user.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {
    Optional<User> findByUsername(String username);

    Optional<User> findByUserInfoUuid(UUID userInfoUuid);
}
