package com.spring.usermanagement.Repo.organization;

import com.spring.usermanagement.model.organizationrole.OrganizationRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(exported = false)
public interface OrganizationRoleRepo extends JpaRepository<OrganizationRole, Long> {

}
