package com.spring.usermanagement.model.role;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Embeddable
public class CompositeKey implements Serializable {

    //    @Column(name = "integration_id")
    private Long integrationId;

    //    @Column(name = "role_id")
    private Long roleId;
}
