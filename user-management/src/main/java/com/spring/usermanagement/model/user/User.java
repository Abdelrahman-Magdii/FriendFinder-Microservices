package com.spring.usermanagement.model.user;

import com.spring.commonlib.model.BaseEntity;
import com.spring.commonlib.model.enums.Language;
import com.spring.commonlib.model.enums.Scope;
import com.spring.usermanagement.model.userrole.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
@Entity(name = "users")
@NoArgsConstructor
@NamedStoredProcedureQuery(name = "User.getUserDetailsByUsername",
                procedureName = "GetUserDetailsByUsername",
                parameters = { @StoredProcedureParameter(mode = ParameterMode.IN,
                                name = "user_name", type = String.class) },
                resultClasses = User.class)
public class User extends BaseEntity {

    @Column(name = "name")
    private String name;
    @Column(name = "user_name")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "email")
    private String email;
    @Column(name = "mobile_phone")
    private String mobilePhone;
    @Column(name = "admin")
    private boolean admin;
    @Enumerated(EnumType.STRING)
    private Language language;
    @Enumerated(EnumType.STRING)
    private Scope scope;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private List<UserRole> roles;

    public User(String name, String loginName, String encode, String email,
                    String mobilePhone, boolean b, Language language, Scope scope,
                    boolean b1) {
        this.name = name;
        this.username = loginName;
        this.password = encode;
        this.email = email;
        this.mobilePhone = mobilePhone;
        this.admin = b;
        this.language = language;
        this.scope = scope;

    }
}
