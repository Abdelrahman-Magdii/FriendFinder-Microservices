package com.spring.usermanagement.model.role;

import com.spring.commonlib.model.BaseEntity;
import com.spring.usermanagement.model.organizationrole.OrganizationRole;
import com.spring.usermanagement.model.userrole.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
@Entity(name = "role")
public class Role extends BaseEntity {

    @Column(unique = true)
    private String code;
    private String displayName;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "role")
    private List<UserRole> users = new ArrayList<>();
    @OneToMany(mappedBy = "role")
    private Collection<OrganizationRole> organizationRole;

    public Collection<OrganizationRole> getOrganizationRole() {
        return organizationRole;
    }

    public void setOrganizationRole(Collection<OrganizationRole> organizationRole) {
        this.organizationRole = organizationRole;
    }
}
