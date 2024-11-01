package com.spring.usermanagement.Repo.procedure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProcedureRepo extends
                JpaRepository<com.spring.usermanagement.model.procedure.Procedure, Long> {

    @Modifying
    @Query(value = "CALL ADD_USER_OR_ORG_FROM_USER_MANAGEMENT(:user_id, :scope)",
                    nativeQuery = true)
    void addUserToFriendFinder(@Param("user_id") int userId,
                    @Param("scope") String scope);
    //
    //    @Procedure(procedureName = "ADD_USER_OR_ORG_FROM_USER_MANAGEMENT")
    //    void addUserOrOrgFromUserManagement(@Param("p_USER_ID") Integer userId,
    //                    @Param("p_SCOPE") String scope);

}
