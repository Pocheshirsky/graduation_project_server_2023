package com.example.searching.repository;

import com.example.chat.model.ChatMessage;
import com.example.searching.model.UserPool;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.UUID;

@Repository
public interface UserPoolRepository extends CrudRepository<UserPool, UUID> {

    boolean existsByUserInfoUuid(UUID userInfoUuid);

    void deleteByUserInfoUuid(UUID userInfoUuid);


}
