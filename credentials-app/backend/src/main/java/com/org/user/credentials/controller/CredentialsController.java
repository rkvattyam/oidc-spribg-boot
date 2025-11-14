package com.org.user.credentials.controller;

import com.org.user.credentials.dto.CreateCredentialRequest;
import com.org.user.credentials.dto.CredentialResponseDto;
import com.org.user.credentials.service.CredentialService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
public class CredentialsController {

    private final CredentialService credentialService;

    public CredentialsController(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @PutMapping("/credentials/{clientId}/secret")
    public String updateClientSecret(@PathVariable String clientId, @RequestParam String newSecret) {
        return credentialService.updateClientSecret(clientId, newSecret);
    }

    @DeleteMapping("/credentials/{clientId}")
    public String deleteClientSecret(@PathVariable String clientId) {
        credentialService.deleteClientSecret(clientId);
        return "Deleted credentials for clientId: " + clientId;
    }

    @PostMapping("/credentials")
    public CredentialResponseDto createCredential(@RequestBody CreateCredentialRequest request) {
        var credentials =  credentialService.createCredentialsIfNoneExist(
                request.userId(),
                request.organizationId(),
                request.clientSecret()
        );
        return CredentialResponseDto.fromEntity(credentials);
    }

    @GetMapping("/credentials/{organizationId}")
    public ResponseEntity<List<CredentialResponseDto>> getCredentialsByOrganizationForUser(
            @PathVariable Long organizationId,
            @RequestParam String email
    ) {
        List<CredentialResponseDto> credentials =
                credentialService.getCredentialsForUserAndOrganization(email, organizationId);

        return ResponseEntity.ok(credentials);
    }

}
