package com.example.searching.repository;

import com.example.chat.model.MessageStatus;
import com.example.searching.model.PoolMessage;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PoolMessageRepository extends CrudRepository<PoolMessage, UUID> {

    List<PoolMessage> findPoolMessageByUserUuidAndStatusOrderByTimestampAsc(UUID userUuid, MessageStatus status);

    @Modifying
    @Query("update PoolMessage m set m.messageStatus = :status " +
            "where m.uuid = :uuid ")
    void updatePoolMessageStatus(
            @Param(value = "uuid") UUID uuid,
            @Param(value = "status") MessageStatus status);
}
