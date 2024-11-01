package com.spring.usermanagement.controller;

import com.spring.commonlib.exceptions.SysException;
import com.spring.usermanagement.model.dto.auth.UserDto;
import com.spring.usermanagement.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserDto> create(@RequestBody Map<String, Object> params)
                    throws SysException {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(params));
    }

}
