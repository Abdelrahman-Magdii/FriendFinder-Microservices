package com.spring.usermanagement.model.userrole;

import com.spring.usermanagement.model.role.CompositeKey;
import com.spring.usermanagement.model.role.Role;
import com.spring.usermanagement.model.user.User;
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
@Entity(name = "user_role")
@NoArgsConstructor
public class UserRole implements Serializable {

    @EmbeddedId
    private CompositeKey compositeKey = new CompositeKey();

    @ManyToOne
    @JoinColumn(name = "user_id")
    @MapsId("integrationId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "role_id")
    @MapsId("roleId")
    private Role role;

    public UserRole(User userCreation, Role role) {
        this.user = userCreation;
        this.role = role;
    }
}
