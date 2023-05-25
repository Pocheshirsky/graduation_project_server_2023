package com.example.searching.repository;

import com.example.user.model.UserInfo;

import java.util.List;

public interface UserPoolRepositoryCustom {

    List<UserInfo> findUserInfoByPredicate(UserInfo info);
}
