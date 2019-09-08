package com.api.users.ApiUsers.repository;

import com.api.users.ApiUsers.entity.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity,Long> {
UserEntity findByEmail(String email);

    UserEntity findByUserId(String userId);
}
