package com.org.user.credentials.service;

import com.org.user.credentials.dto.OrganizationDto;
import com.org.user.credentials.dto.UserOrganizationsResponse;
import com.org.user.credentials.repository.OrganizationRepository;
import org.springframework.stereotype.Service;

@Service
public class OrganizationService {

    private final UserService userService;
    private final OrganizationRepository organizationRepository;


    public OrganizationService(UserService userService,
                               OrganizationRepository organizationRepository) {
        this.userService = userService;
        this.organizationRepository = organizationRepository;
    }

    /**
     * Method to get org by user name
     * @param email the email id of the user
     * @return user and organisations associated
     */
    public UserOrganizationsResponse getOrganizationsByUsername(String email) {

        var userOpt = userService.findByEmail(email);

        if (userOpt.isEmpty()) {
            var allOrgs = organizationRepository.findAll().stream()
                    .map(org -> new OrganizationDto(org.getId(), org.getName(), org.getVat(), org.getSapId()))
                    .toList();

            return new UserOrganizationsResponse(null, allOrgs);
        }
        var user = userOpt.get();
        var userOrgs = organizationRepository.findAllByUserEmail(email).stream()
                .map(org -> new OrganizationDto(org.getId(), org.getName(), org.getVat(), org.getSapId()))
                .toList();

        return new UserOrganizationsResponse(user.getId(), userOrgs);
    }
}
