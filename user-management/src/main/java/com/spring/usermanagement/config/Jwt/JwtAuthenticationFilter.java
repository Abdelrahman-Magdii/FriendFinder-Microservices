package com.spring.usermanagement.config.Jwt;

import com.spring.usermanagement.config.SecurityConfig;
import com.spring.usermanagement.model.dto.auth.OrgDto;
import com.spring.usermanagement.model.dto.auth.UserDto;
import com.spring.usermanagement.model.dto.role.RoleDto;
import com.spring.usermanagement.services.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final HandlerExceptionResolver handlerExceptionResolver;
    private final AuthService authService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request)
                    throws ServletException {
        String path = request.getServletPath();
        return checkPath(path, SecurityConfig.PUBLIC_APIS);
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                    @NonNull HttpServletResponse response,
                    @NonNull FilterChain filterChain)
                    throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        logger.info("Authorization Header: " + authHeader);

        if (authHeader != null && authHeader.startsWith("Basic ")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = authHeader.substring(7);
            logger.info("Extracted JWT: " + jwt);

            if (jwt.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            Optional<Object> auth = authService.authByToken(jwt);

            if (auth.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            // Authenticate user or organization
            UsernamePasswordAuthenticationToken authenticationToken = null;
            if (auth.get() instanceof UserDto) {
                UserDto userDto = (UserDto) auth.get();
                authenticationToken = createAuthenticationToken(userDto, request);
            } else if (auth.get() instanceof OrgDto) {
                OrgDto orgDto = (OrgDto) auth.get();
                authenticationToken = createAuthenticationToken(orgDto, request);
            }

            // Set the authentication in the security context if valid
            if (authenticationToken != null) {
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

            filterChain.doFilter(request, response);

        } catch (Exception exception) {
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }
    }

    // Creates authentication token based on UserDto or OrgDto
    private UsernamePasswordAuthenticationToken createAuthenticationToken(Object dto,
                    HttpServletRequest request) {
        Set<RoleDto> roles = (dto instanceof UserDto) ? ((UserDto) dto).getRoles()
                        : ((OrgDto) dto).getRoles();
        List<SimpleGrantedAuthority> authorities = extractRoles(roles);

        UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(dto, null, authorities);

        authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));
        return authenticationToken;
    }

    // Check if the request path is in the public APIs
    private boolean checkPath(String path, String[] publicPaths) {
        String[] parts = path.split("/");
        if (parts.length >= 3) {
            String newPath = "/" + parts[1] + "/" + parts[2] + "/**";
            return Arrays.asList(publicPaths).contains(newPath);
        }
        return false;
    }

    // Extract roles from a set of RoleDto
    private List<SimpleGrantedAuthority> extractRoles(Set<RoleDto> roles) {
        return (roles != null && !roles.isEmpty()) ? roles.stream()
                        .map(role -> new SimpleGrantedAuthority(role.getCode()))
                        .collect(Collectors.toList()) : Collections.emptyList();
    }
}
