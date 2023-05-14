package com.example.token.repository;

import com.example.token.model.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RefreshTokenRepository  extends CrudRepository<RefreshToken, UUID> {

    void deleteByOwnerUuid(UUID userIdFromRefreshToken);
}
