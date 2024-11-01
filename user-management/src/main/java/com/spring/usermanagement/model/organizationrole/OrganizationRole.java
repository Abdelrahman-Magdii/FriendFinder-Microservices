package com.spring.usermanagement.model.organizationrole;

import com.spring.usermanagement.model.organization.Organization;
import com.spring.usermanagement.model.role.CompositeKey;
import com.spring.usermanagement.model.role.Role;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity(name = "organization_role")
@NoArgsConstructor
public class OrganizationRole implements Serializable {

    @EmbeddedId
    private CompositeKey compositeKey = new CompositeKey();

    @ManyToOne
    @JoinColumn(name = "organization_id")
    @MapsId("integrationId")
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "role_id")
    @MapsId("roleId")
    private Role role;

    public OrganizationRole(Organization organizationCreation, Role role) {
        this.organization = organizationCreation;
        this.role = role;
    }
}
