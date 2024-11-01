package com.spring.usermanagement.services.impl;

import com.spring.commonlib.exceptions.BusinessException;
import com.spring.commonlib.exceptions.FieldException;
import com.spring.commonlib.exceptions.SysException;
import com.spring.commonlib.model.enums.Language;
import com.spring.commonlib.model.enums.Scope;
import com.spring.usermanagement.Repo.role.RoleRepo;
import com.spring.usermanagement.Repo.role.UserRoleRepo;
import com.spring.usermanagement.Repo.user.UserRepo;
import com.spring.usermanagement.config.Jwt.JwtUtilsUser;
import com.spring.usermanagement.model.dto.auth.UserDto;
import com.spring.usermanagement.model.dto.role.RoleDto;
import com.spring.usermanagement.model.role.Role;
import com.spring.usermanagement.model.user.User;
import com.spring.usermanagement.model.userrole.UserRole;
import com.spring.usermanagement.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final RoleRepo roleRepo;
    private final UserRoleRepo userRoleRepo;
    private final UserRepo userRepository;
    private final JwtUtilsUser jwtUtils;
    private final PasswordEncoder passwordEncoder;

    private static final String USER_ROLE_CODE = "DEFAULT_USER";

    /**
     * Creates a new user with the provided parameters.
     *
     * @param params the user information
     * @return UserDto containing user details and authentication token
     */
    @Override
    @Transactional
    public UserDto create(Map<String, Object> params) {
        validateUserFields(params);

        String name = params.get("name").toString();
        String username = params.get("username").toString();
        String email = params.get("email").toString();
        String password = params.get("password").toString();
        String mobilePhone = params.get("mobilePhone").toString();
        Language language = Language.valueOf(params.get("language").toString());
        Scope scope = Scope.valueOf(params.get("scope").toString());

        Optional<User> existingUser =
                        userRepository.findByUsernameOrEmail(username, email);
        if (existingUser.isPresent()) {
            throw new BusinessException("error.user.loginName.email.exist", "#016",
                            "loginName | email");
        }

        User userCreation = new User(name, username, passwordEncoder.encode(password),
                        email, mobilePhone, false, language, scope, true);
        userCreation = userRepository.save(userCreation);

        Role role = roleRepo.findByCode(USER_ROLE_CODE)
                        .orElseThrow(() -> new SysException("role not exist", "#017"));

        UserRole userRole = new UserRole(userCreation, role);
        userRoleRepo.save(userRole);

        String token = jwtUtils.generateToken(userCreation);
        UserDto userDto = new UserDto(userCreation.getId(), token,
                        String.valueOf(jwtUtils.getExpireAt(token)),
                        jwtUtils.createRefreshToken(userCreation),
                        new RoleDto(role.getCode(), role.getDisplayName()),
                        userCreation.isAdmin(), userCreation.getLanguage(),
                        userCreation.getScope());

        // Optional: Call procedure to add user to friend finder
        // procedureService.addUserToFriendFinder(userDto.getUserId(),
        //                 userDto.getScope().value());

        return userDto;
    }

    /**
     * Validates required user fields.
     *
     * @param params the user information
     */
    private static void validateUserFields(Map<String, Object> params) {
        if (Objects.isNull(params.get("name"))) {
            throw new FieldException("error.name.required", "#007", "name");
        }
        if (Objects.isNull(params.get("username"))) {
            throw new FieldException("error.loginName.required", "#008", "loginName");
        }
        if (Objects.isNull(params.get("password"))) {
            throw new FieldException("error.password.required", "#009", "password");
        }
        if (Objects.isNull(params.get("email"))) {
            throw new FieldException("error.email.required", "#010", "email");
        }
        if (Objects.isNull(params.get("mobilePhone"))) {
            throw new FieldException("error.mobilePhone.required", "#011", "mobilePhone");
        }
        if (Objects.isNull(params.get("language"))) {
            throw new FieldException("error.language.required", "#012", "language");
        }
        if (Objects.isNull(params.get("scope"))) {
            throw new FieldException("error.scope.required", "#013", "scope");
        }

        if (!(Language.ARABIC.value().equals(params.get("language"))
                        || Language.ENGLISH.value().equals(params.get("language")))) {
            throw new FieldException("error.language.invalid", "#014", "language");
        }

        if (!(Scope.USER.value().equals(params.get("scope")))) {
            throw new FieldException("error.scope.user.invalid", "#015", "scope");
        }
    }
}
