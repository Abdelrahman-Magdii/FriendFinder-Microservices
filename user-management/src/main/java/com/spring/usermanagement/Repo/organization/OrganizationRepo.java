package com.spring.usermanagement.Repo.organization;

import com.spring.usermanagement.model.organization.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RepositoryRestResource(exported = false)
public interface OrganizationRepo extends JpaRepository<Organization, Long> {

    Optional<Organization> findByReferenceId(String referenceId);
}
