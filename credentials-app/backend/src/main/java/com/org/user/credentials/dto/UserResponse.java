package com.org.user.credentials.dto;

import java.util.List;

public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        List<OrganizationDto> organizations
) {}
