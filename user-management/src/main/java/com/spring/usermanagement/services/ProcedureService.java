package com.spring.usermanagement.services;

import org.springframework.stereotype.Repository;

@Repository
public interface ProcedureService {

    /**
     * add User To Friend Finder
     * @param userId
     * @param scope
     */
    void addUserToFriendFinder(int userId, String scope);
}
