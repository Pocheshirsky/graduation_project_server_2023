package com.example.searching.repository;

import com.example.searching.model.UserPool;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import java.util.UUID;

@Repository
public interface UserPoolRepository extends CrudRepository<UserPool, UUID>, UserPoolRepositoryCustom  {

    boolean existsByUserInfoUuid(UUID userInfoUuid);

    void deleteByUserInfoUuid(UUID userInfoUuid);


}
