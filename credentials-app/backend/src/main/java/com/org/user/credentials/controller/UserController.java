package com.org.user.credentials.controller;

import com.org.user.credentials.dto.OrganizationDto;
import com.org.user.credentials.dto.UserResponse;
import com.org.user.credentials.entity.User;
import com.org.user.credentials.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<UserResponse> createUser(@RequestBody User userRequest) {
        return userService.findByEmail(userRequest.getEmail())
                .map(existingUser -> {
                    List<OrganizationDto> orgDtos = existingUser.getOrganizations().stream()
                            .map(OrganizationDto::fromEntity)
                            .toList();
                    UserResponse response = new UserResponse(
                            existingUser.getId(),
                            existingUser.getFirstName(),
                            existingUser.getLastName(),
                            existingUser.getEmail(),
                            orgDtos
                    );
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    UserResponse savedUser = userService.createUserWithOrganizations(userRequest);
                    return ResponseEntity.ok(savedUser);
                });
    }
}
