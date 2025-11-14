package com.org.user.credentials.service;


import com.org.user.credentials.dto.OrganizationDto;
import com.org.user.credentials.dto.UserResponse;
import com.org.user.credentials.entity.Organization;
import com.org.user.credentials.entity.User;
import com.org.user.credentials.repository.OrganizationRepository;
import com.org.user.credentials.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;

    public UserService(UserRepository userRepository, OrganizationRepository organizationRepository) {
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
    }

    /**
     * Method to find user by email
     * @param email the email id of the user
     * @return the user object
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Method to save the user if the user logs in for the first time
     * @param userRequest the incoming user request
     * @return user response object
     */
    @Transactional
    public UserResponse createUserWithOrganizations(User userRequest) {
        var user = new User();
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        final var orgIds = userRequest.getOrganizations().stream().map(Organization::getId).toList();
        var orgs = organizationRepository.findAllById(orgIds);
        for (var org : orgs) {
            user.addOrganization(org); // ensures both sides are updated
        }
        User savedUser = userRepository.save(user);
        List<OrganizationDto> orgDtos = savedUser.getOrganizations().stream()
                .map(OrganizationDto::fromEntity)
                .toList();

        return new UserResponse(
                savedUser.getId(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getEmail(),
                orgDtos
        );
    }
}
