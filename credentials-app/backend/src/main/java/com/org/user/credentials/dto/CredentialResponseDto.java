package com.org.user.credentials.dto;

import com.org.user.credentials.entity.Credentials;

import java.time.LocalDateTime;

public record CredentialResponseDto (Long id,
                                     String clientId,
                                     Long userId,
                                     Long organizationId,
                                     LocalDateTime creationDate,
                                     LocalDateTime expiryDate
) {
    public static CredentialResponseDto fromEntity(Credentials cred) {
        return new CredentialResponseDto(
                cred.getId(),
                cred.getClientId(),
                cred.getCreatedBy().getId(),
                cred.getOrganization().getId(),
                cred.getCreationDate(),
                cred.getExpiryDate()
        );
    }
}
