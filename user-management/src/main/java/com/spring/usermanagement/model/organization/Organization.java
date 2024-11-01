package com.spring.usermanagement.model.organization;

import com.spring.commonlib.model.BaseEntity;
import com.spring.commonlib.model.enums.Scope;
import com.spring.usermanagement.model.organizationrole.OrganizationRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity(name = "organization")
@NoArgsConstructor
public class Organization extends BaseEntity {

    @Column(name = "reference_id")
    private String referenceId;
    @Column(name = "organization_name")
    private String organizationName;
    @Column(name = "password")
    private String password;

    @Column(name = "scope")
    @Enumerated(EnumType.STRING)
    private Scope scope;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "organization")
    private List<OrganizationRole> roles;

    public Organization(String referenceId, String organizationName, String encode,
                    Scope scope) {

        this.referenceId = referenceId;
        this.organizationName = organizationName;
        this.password = encode;
        this.scope = scope;

    }

}
