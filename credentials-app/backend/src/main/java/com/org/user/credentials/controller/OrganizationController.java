package com.org.user.credentials.controller;

import com.org.user.credentials.dto.UserOrganizationsResponse;
import com.org.user.credentials.service.OrganizationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class OrganizationController {

    private final OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }
    @GetMapping("/organizations/user/{email}")
    public ResponseEntity<UserOrganizationsResponse> getOrganizationsByUsername(@PathVariable String email) {
        return ResponseEntity.ok(organizationService.getOrganizationsByUsername(email));
    }

}

