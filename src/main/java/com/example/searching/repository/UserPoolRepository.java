package com.example.searching.repository;

import com.example.searching.model.UserPool;
import com.example.user.model.UserInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.UUID;

@Repository
public interface UserPoolRepository extends CrudRepository<UserPool, UUID>, UserPoolRepositoryCustom  {

    boolean existsByUserInfoUuid(UUID userInfoUuid);

    void deleteByUserInfoUuid(UUID userInfoUuid);

    void findAllByUserInfoList(List<UserInfo> userInfoList);
}
