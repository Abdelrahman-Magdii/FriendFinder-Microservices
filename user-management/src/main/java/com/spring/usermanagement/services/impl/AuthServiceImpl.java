package com.spring.usermanagement.services.impl;

import com.spring.commonlib.exceptions.FieldException;
import com.spring.commonlib.exceptions.SysException;
import com.spring.commonlib.model.enums.Scope;
import com.spring.usermanagement.Repo.organization.OrganizationRepo;
import com.spring.usermanagement.Repo.user.UserRepo;
import com.spring.usermanagement.config.Jwt.JwtUtilsOrganization;
import com.spring.usermanagement.config.Jwt.JwtUtilsUser;
import com.spring.usermanagement.exceptions.BadAuthException;
import com.spring.usermanagement.model.dto.auth.OrgDto;
import com.spring.usermanagement.model.dto.auth.UserDto;
import com.spring.usermanagement.model.dto.role.RoleDto;
import com.spring.usermanagement.model.organization.Organization;
import com.spring.usermanagement.model.user.User;
import com.spring.usermanagement.services.AuthService;
import jakarta.transaction.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OrganizationRepo organizationRepository;
    private final JwtUtilsUser jwtUtilsUser;
    private final JwtUtilsOrganization jwtUtilsOrganization;

    @Autowired
    public AuthServiceImpl(UserRepo userRepository, PasswordEncoder passwordEncoder,
                    OrganizationRepo organizationRepository, JwtUtilsUser jwtUtils,
                    JwtUtilsOrganization jwtUtilsOrganization) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.organizationRepository = organizationRepository;
        this.jwtUtilsUser = jwtUtils;
        this.jwtUtilsOrganization = jwtUtilsOrganization;
    }

    // ----------------------- Public Methods ---------------------------

    @Override
    public UserDto authUser(final Map<String, Object> params) throws SystemException {
        String username = (String) params.get("username");
        String email = (String) params.get("email");
        String password = (String) params.get("password");

        validateUserParams(username, email, password);
        User user = validateUserAuth(username, email, password);

        String token = jwtUtilsUser.generateToken(user);

        return new UserDto(user.getId(), token,
                        String.valueOf(jwtUtilsUser.getExpireAt(token)),
                        jwtUtilsUser.createRefreshToken(user), extractRoles(user),
                        user.isAdmin(), user.getLanguage(), user.getScope());
    }

    @Override
    public OrgDto authOrganization(final Map<String, Object> params)
                    throws SystemException {
        String referenceId = (String) params.get("reference_id");
        String password = (String) params.get("password");

        validateOrganizationParams(referenceId, password);
        Organization organization = validateOrganizationAuth(referenceId, password);

        String token = jwtUtilsOrganization.generateToken(organization);

        return new OrgDto(organization.getId(), token,
                        String.valueOf(jwtUtilsOrganization.getExpireAt(token)),
                        jwtUtilsOrganization.createRefreshToken(organization),
                        extractRoles(organization), organization.getScope());
    }

    @Override
    public <T> Optional<T> authByToken(final String token) throws SystemException {
        String scope = jwtUtilsUser.getScope(token);
        String subject;

        if (Scope.USER.value().equals(scope)) {
            subject = jwtUtilsUser.getSubject(token);
            return (Optional<T>) userRepository.findByUsername(subject)
                            .map(user -> createUserDto(user, token));
        } else if (Scope.ORGANIZATION.value().equals(scope)) {
            subject = jwtUtilsOrganization.getSubject(token);
            return (Optional<T>) organizationRepository.findByReferenceId(subject)
                            .map(organization -> createOrgDto(organization, token));
        }
        return Optional.empty();
    }

    // ----------------------- Private Methods ---------------------------

    private UserDto createUserDto(User user, String token) {
        try {
            return new UserDto(user.getId(), token,
                            String.valueOf(jwtUtilsUser.getExpireAt(token)),
                            jwtUtilsUser.createRefreshToken(user), extractRoles(user),
                            user.isAdmin(), user.getLanguage(), user.getScope());
        } catch (SystemException e) {
            throw new RuntimeException("Error creating UserDto", e);
        }
    }

    private OrgDto createOrgDto(Organization organization, String token) {
        try {
            return new OrgDto(organization.getId(), token,
                            String.valueOf(jwtUtilsOrganization.getExpireAt(token)),
                            jwtUtilsOrganization.createRefreshToken(organization),
                            extractRoles(organization), organization.getScope());
        } catch (SystemException e) {
            throw new RuntimeException("Error creating OrgDto", e);
        }
    }

    // -------------------- Validation and Auth Logic --------------------

    private void validateUserParams(String username, String email, String password) {
        log.info("Validating user params: username = {}, email = {}, password = {}",
                        username, email, password != null ? "******" : null);
        if ((username == null || username.trim().isEmpty())
                        && (email == null || email.trim().isEmpty())) {
            throw new FieldException("error.emailOrLoginName.required", "#001",
                            "email or loginName");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new FieldException("error.password.required", "#002", "Password");
        }
    }

    private User validateUserAuth(String username, String email, String password) {
        //        User user = (username != null)
        //                        ? userRepository.findByUsername(username)
        //                                        .orElseThrow(() -> new BadAuthException(
        //                                                        "error.loginNameOrEmail.invalid",
        //                                                        "#003"))
        //                        : userRepository.findByEmail(email)
        //                                        .orElseThrow(() -> new BadAuthException(
        //                                                        "error.loginNameOrEmail.invalid",
        //                                                        "#003"));

        Optional<User> user1 = userRepository.findByUsername(username);
        Optional<User> user2 = userRepository.findByEmail(email);
        if (user1.isPresent()
                        && passwordEncoder.matches(password, user1.get().getPassword())) {
            return user1.get();
        } else if (user2.isPresent()
                        && passwordEncoder.matches(password, user2.get().getPassword())) {
            return user2.get();
        } else if (!passwordEncoder.matches(password, user1.get().getPassword())) {
            throw new BadAuthException("error.password.invalid", "#004");
        } else {
            throw new BadAuthException("error.loginNameOrEmail.invalid", "#003");
        }

    }

    private void validateOrganizationParams(String referenceId, String password) {
        if (referenceId == null || referenceId.trim().isEmpty()) {
            throw new FieldException("error.referenceId.required", "#005", "referenceId");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new FieldException("error.password.required", "#006", "Password");
        }
    }

    private Organization validateOrganizationAuth(String referenceId, String password) {
        Organization organization =
                        organizationRepository.findByReferenceId(referenceId)
                                        .orElseThrow(() -> new BadAuthException(
                                                        "error.referenceId.invalid",
                                                        "#007"));

        if (!passwordEncoder.matches(password, organization.getPassword())) {
            throw new BadAuthException("error.password.invalid", "#008");
        }
        return organization;
    }

    private <T> Set<RoleDto> extractRoles(T userType) throws SystemException {
        if (userType instanceof User) {
            return extractRolesFromUser((User) userType);
        } else if (userType instanceof Organization) {
            return extractRolesFromOrganization((Organization) userType);
        } else {
            throw new SysException("Must pass a valid User or Organization", "#018");
        }
    }

    private Set<RoleDto> extractRolesFromUser(User user) {
        return user.getRoles().stream()
                        .map(role -> new RoleDto(role.getRole().getCode(),
                                        role.getRole().getDisplayName()))
                        .collect(Collectors.toSet());
    }

    private Set<RoleDto> extractRolesFromOrganization(Organization organization) {
        return organization.getRoles().stream()
                        .map(role -> new RoleDto(role.getRole().getCode(),
                                        role.getRole().getDisplayName()))
                        .collect(Collectors.toSet());
    }
}
