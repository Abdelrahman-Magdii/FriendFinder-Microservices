package com.spring.usermanagement.services;

import com.spring.usermanagement.model.dto.auth.OrgDto;
import com.spring.usermanagement.model.dto.auth.UserDto;
import jakarta.transaction.SystemException;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public interface AuthService {

    /**
     * login with user
     * @param params
     * @return AuthDto
     */
    UserDto authUser(Map<String, Object> params) throws SystemException;

    /**
     * login with organization
     * @param params
     * @return OrgAuthDto
     */
    OrgDto authOrganization(Map<String, Object> params) throws SystemException;

    /**
     * auth By Token
     * @param token
     * @return
     * @param <T>
     */
    <T> Optional<T> authByToken(String token) throws SystemException;
}
