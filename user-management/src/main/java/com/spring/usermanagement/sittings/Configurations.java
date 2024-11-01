package com.spring.usermanagement.sittings;

import com.spring.management.sittings.helpers.TokenConfiguration;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class Configurations {

    private TokenConfiguration token;
}
