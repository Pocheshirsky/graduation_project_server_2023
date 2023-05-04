package com.example.token;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RefreshTokenRepository  extends CrudRepository<RefreshToken, UUID> {

    void deleteByOwnerUuid(UUID userIdFromRefreshToken);
}
