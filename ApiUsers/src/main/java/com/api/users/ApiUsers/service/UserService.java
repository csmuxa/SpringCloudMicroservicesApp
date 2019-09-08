package com.api.users.ApiUsers.service;

import com.api.users.ApiUsers.shared.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserDto createUser(UserDto userDto);
    UserDto getUserDetailsByEmail(String email);

    UserDto getUserByUserId(String userId);
}
