package com.spring.usermanagement.controller;

import com.spring.usermanagement.model.dto.auth.OrgDto;
import com.spring.usermanagement.model.dto.auth.UserDto;
import com.spring.usermanagement.services.AuthService;
import jakarta.transaction.SystemException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login/user")
    public ResponseEntity<UserDto> loginUser(@RequestBody Map<String, Object> input)
                    throws SystemException {
        return ResponseEntity.ok(authService.authUser(input));
    }

    @GetMapping("/login/organization")
    public ResponseEntity<OrgDto> loginOrganization(
                    @RequestBody Map<String, Object> input) throws SystemException {
        return ResponseEntity.ok(authService.authOrganization(input));
    }

    //    @PostMapping("/token")
    //    public ResponseEntity<Map<String, String>> validateToken(@RequestBody String token) {
    //        return null;
    //    }

}
