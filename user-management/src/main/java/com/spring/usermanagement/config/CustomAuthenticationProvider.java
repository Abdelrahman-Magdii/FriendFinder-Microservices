package com.spring.usermanagement.config;

import com.spring.usermanagement.Repo.organization.OrganizationRepo;
import com.spring.usermanagement.model.organization.Organization;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final OrganizationRepo organizationRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication)
                    throws AuthenticationException {

        String referenceId = authentication.getName(); // true
        String password = authentication.getCredentials().toString();
        Optional<Organization> organization =
                        organizationRepo.findByReferenceId(referenceId);

        if (organization.isPresent()) {
            if (passwordEncoder.matches(password, organization.get().getPassword())) {

                return new UsernamePasswordAuthenticationToken(referenceId, password,
                                grantedAuthorities(organization.get()));
            }
        }
        return null;
    }

    private List<GrantedAuthority> grantedAuthorities(Organization organization) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (organization.getRoles() != null && !organization.getRoles().isEmpty()) {
            organization.getRoles().forEach(
                            role -> authorities.add(new SimpleGrantedAuthority(role
                                            .getOrganization().getOrganizationName())));
        }
        return authorities;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
