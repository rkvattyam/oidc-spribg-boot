package com.org.user.credentials.dto;

import com.org.user.credentials.entity.Organization;

public record OrganizationDto(Long id, String name, String vat, String sapId) {
    public static OrganizationDto fromEntity(Organization org) {
        return new OrganizationDto(org.getId(), org.getName(), org.getVat(), org.getSapId());
    }
}
