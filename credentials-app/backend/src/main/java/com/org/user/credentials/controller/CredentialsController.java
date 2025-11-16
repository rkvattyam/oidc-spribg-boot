package com.org.user.credentials.controller;

import com.org.user.credentials.dto.CreateCredentialRequest;
import com.org.user.credentials.dto.CredentialResponseDto;
import com.org.user.credentials.dto.UpdateCredentialRequest;
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
    public ResponseEntity<String> updateClientSecret(@PathVariable String clientId, @RequestBody UpdateCredentialRequest request) {
        return ResponseEntity.ok(credentialService.updateClientSecret(clientId, request.newSecret()));
    }

    @DeleteMapping("/credentials/{clientId}")
    public void deleteClientSecret(@PathVariable String clientId) {
        credentialService.deleteClientSecret(clientId);
    }

    @PostMapping("/credentials")
    public ResponseEntity<CredentialResponseDto> createCredential(@RequestBody CreateCredentialRequest request) {
        var credentials =  credentialService.createCredentialsIfNoneExist(
                request.userId(),
                request.organizationId(),
                request.clientSecret()
        );
        return ResponseEntity.ok(CredentialResponseDto.fromEntity(credentials));
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

    @GetMapping("/credentials/user")
    public ResponseEntity<List<CredentialResponseDto>> getCredentialsByUser(@RequestParam String email) {
        List<CredentialResponseDto> credentials = credentialService.getCredentialsByUser(email);

        return ResponseEntity.ok(credentials);
    }

}
