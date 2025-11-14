package com.org.user.credentials.dto;

public record CreateCredentialRequest(Long userId,
                                      Long organizationId,
                                      String clientSecret) {
}
