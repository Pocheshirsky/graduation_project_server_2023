package com.example.searching.repository;

import com.example.searching.model.UserPool;
import com.example.user.model.UserInfo;

import java.util.List;

public interface UserPoolRepositoryCustom {

    List<UserPool> findUserInfoByPredicate(UserInfo info);
}
