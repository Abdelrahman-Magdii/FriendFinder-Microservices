package com.spring.usermanagement.Repo.role;

import com.spring.usermanagement.model.userrole.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(exported = false)
public interface UserRoleRepo extends JpaRepository<UserRole, Long> {

}
