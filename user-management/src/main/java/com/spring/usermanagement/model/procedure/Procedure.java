package com.spring.usermanagement.model.procedure;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Procedure {
    @Id
    private Long id;

}
