package com.spring.usermanagement.services;

import com.spring.usermanagement.model.dto.auth.UserDto;

import java.util.Map;

public interface UserService {

    UserDto create(Map<String, Object> params);
}
