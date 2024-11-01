package com.spring.usermanagement.services.impl;

import com.spring.commonlib.exceptions.BusinessException;
import com.spring.commonlib.exceptions.FieldException;
import com.spring.commonlib.exceptions.SysException;
import com.spring.commonlib.model.enums.Scope;
import com.spring.usermanagement.Repo.organization.OrganizationRepo;
import com.spring.usermanagement.Repo.organization.OrganizationRoleRepo;
import com.spring.usermanagement.Repo.role.RoleRepo;
import com.spring.usermanagement.config.Jwt.JwtUtilsOrganization;
import com.spring.usermanagement.model.dto.auth.OrgDto;
import com.spring.usermanagement.model.dto.role.RoleDto;
import com.spring.usermanagement.model.organization.Organization;
import com.spring.usermanagement.model.organizationrole.OrganizationRole;
import com.spring.usermanagement.model.role.Role;
import com.spring.usermanagement.services.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepo organizationRepository;

    private final PasswordEncoder passwordEncoder;

    private final OrganizationRoleRepo organizationRoleRepository;

    private final RoleRepo roleRepository;
    private final JwtUtilsOrganization jwtUtilsOrganization;

    //    private ProcedureService procedureService;

    private final String ORGANIZATION_ROLE_CODE = "DEFAULT_USER";

    @Override
    @Transactional
    public OrgDto create(Map<String, Object> params) {
        //        validateOrganizationFields(params);

        String referenceId = params.get("reference_id").toString();
        String organizationName = params.get("organization_name").toString();
        String password = params.get("password").toString();
        Scope scope = Scope.valueOf(params.get("scope").toString());

        Optional<Organization> organization =
                        organizationRepository.findByReferenceId(referenceId);

        if (organization.isPresent()) {
            throw new BusinessException("error.organization.referenceId.exist", "#020",
                            "loginName | email");
        }

        Organization organizationCreation = new Organization(referenceId,
                        organizationName, passwordEncoder.encode(password), scope);

        organizationCreation = organizationRepository.save(organizationCreation);

        Optional<Role> role = roleRepository.findByCode(ORGANIZATION_ROLE_CODE);

        if (!role.isPresent()) {
            throw new SysException("role not exist", "#017");
        }

        OrganizationRole organizationRole =
                        new OrganizationRole(organizationCreation, role.get());

        // add ORGANIZATION ROLE to organization creation
        organizationRoleRepository.save(organizationRole);

        //         create token
        String token = jwtUtilsOrganization.generateToken(organizationCreation);

        OrgDto orgDto = new OrgDto(organizationCreation.getId(), token,
                        String.valueOf(jwtUtilsOrganization.getExpireAt(token)),
                        jwtUtilsOrganization.createRefreshToken(organizationCreation),
                        new RoleDto(role.get().getCode(), role.get().getDisplayName()),
                        organizationCreation.getScope());

        // call procedure to add user to friend finder
        //                procedureService.addUserToFriendFinder(orgDto.getOrgId(), orgDto.getScope().value());

        return orgDto;
    }

    /**
     * validate Organization Fields
     * @param params
     */
    private static void validateOrganizationFields(Map<String, Object> params) {
        if (Objects.isNull(params.get("reference_id"))) {
            throw new FieldException("error.referenceId.required", "#005",
                            "reference_id");
        }
        if (Objects.isNull(params.get("organization_name"))) {
            throw new FieldException("error.organizationName.required", "#018",
                            "organization_name");
        }
        if (Objects.isNull(params.get("password"))) {
            throw new FieldException("error.password.required", "#009", "password");
        }
        if (Objects.isNull(params.get("scope"))) {
            throw new FieldException("error.scope.required", "#013", "scope");
        }

        if (!(Scope.ORGANIZATION.value().equals(params.get("scope")))) {
            throw new FieldException("error.scope.organization.invalid", "#019", "scope");
        }

    }
}
