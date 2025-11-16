package com.org.user.credentials.service;

import com.org.user.credentials.dto.CredentialResponseDto;
import com.org.user.credentials.entity.Credentials;
import com.org.user.credentials.entity.User;
import com.org.user.credentials.exception.CredentialsAppException;
import com.org.user.credentials.repository.CredentialsRepository;
import com.org.user.credentials.repository.OrganizationRepository;
import com.org.user.credentials.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CredentialService {

    private final CredentialsRepository credentialsRepository;
    private final UserRepository userRepository;

    private final OrganizationRepository organizationRepository;

    public CredentialService(CredentialsRepository credentialsRepository, UserRepository userRepository, OrganizationRepository organizationRepository) {
        this.credentialsRepository = credentialsRepository;
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
    }


    /**
     * Method to update the client secret
     * @param clientId the client id for which the secret is changed
     * @param newSecret updated secret value
     * @return client id
     */
    public String updateClientSecret(String clientId, String newSecret) {
        var credentials = credentialsRepository.findByClientId(clientId)
                .orElseThrow(() -> new CredentialsAppException("Credentials not found for clientId: " + clientId));
        credentials.setClientSecret(newSecret);
        return credentialsRepository.save(credentials).getClientId();
    }

    /**
     * Method to delete the client secret
     * @param clientId the client id of the credential
     */ public void deleteClientSecret(String clientId) {
        var credentials = credentialsRepository.findByClientId(clientId)
                .orElseThrow(() -> new CredentialsAppException("Credentials not found for clientId: " + clientId));
        credentialsRepository.delete(credentials);
    }


    /**
     * Method to create new credential if credential does not exist
     * @param userId the user id of the user
     * @param organizationId  the organisation id of the organisation
     * @param clientSecret the client secret of the credntail
     * @return credentials response
     */
    @Transactional
    public Credentials createCredentialsIfNoneExist(Long userId, Long organizationId, String clientSecret) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new CredentialsAppException("User not found"));

        var org = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new CredentialsAppException("Organization not found"));

        var cred = new Credentials();
        cred.setClientId(String.valueOf(UUID.randomUUID()));
        cred.setClientSecret(clientSecret); // hash in production
        cred.setCreationDate(LocalDateTime.now());
        cred.setExpiryDate(LocalDateTime.now().plusYears(1));
        cred.setOrganization(org);
        cred.setCreatedBy(user);

        return credentialsRepository.save(cred);
    }

    /**
     * Method to retrieve the credentials response
     * @param email email of the user
     * @param organizationId the organisation Id
     * @return list of credentials
     */
    public List<CredentialResponseDto> getCredentialsForUserAndOrganization(String email, Long organizationId) {
        return credentialsRepository.findByOrganizationIdAndUserEmail(organizationId, email)
                .stream()
                .map(CredentialResponseDto::fromEntity)
                .toList();
    }

    /**
     * Method to retrieve the credentials response
     * @param email email of the user
     * @return list of credentials
     */
    public List<CredentialResponseDto> getCredentialsByUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CredentialsAppException("User not found for email: " + email));

        return credentialsRepository.findByCreatedById(user.getId())
                .stream()
                .map(CredentialResponseDto::fromEntity)
                .toList();
    }
}
