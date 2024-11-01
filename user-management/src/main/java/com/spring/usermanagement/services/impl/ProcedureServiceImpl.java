package com.spring.usermanagement.services.impl;

import com.spring.usermanagement.Repo.procedure.ProcedureRepo;
import com.spring.usermanagement.services.ProcedureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ProcedureServiceImpl implements ProcedureService {

    @Autowired
    private ProcedureRepo procedureRepository;

    /**
     * add User To Friend Finder
     * @param userId
     * @param scope
     */
    @Override
    public void addUserToFriendFinder(int userId, String scope) {
        procedureRepository.addUserToFriendFinder(userId, scope);
    }

}
