package com.example.searching.model;

import com.example.chat.model.MessageStatus;
import com.example.user.model.UserInfo;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
public class PoolMessage {

    @Id
    @GeneratedValue()
    private UUID uuid;

    private UUID userUuid;
    private UUID foundUserUuid;

    @OneToOne()
    private UserInfo foundUserInfo;

    private MessageStatus status;

    @DateTimeFormat()
    private Timestamp timestamp;
}
