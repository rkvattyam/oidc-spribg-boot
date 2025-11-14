package com.org.user.credentials.dto;

import java.util.List;

public record UserOrganizationsResponse(
        Long userId,
        List<OrganizationDto> organizations
) {}
