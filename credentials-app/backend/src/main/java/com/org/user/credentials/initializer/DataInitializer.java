package com.org.user.credentials.initializer;

import com.org.user.credentials.entity.Organization;
import com.org.user.credentials.entity.User;
import com.org.user.credentials.repository.OrganizationRepository;
import com.org.user.credentials.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;

    public DataInitializer(OrganizationRepository organizationRepository,
                           UserRepository userRepository) {
        this.organizationRepository = organizationRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (organizationRepository.count() > 0) {
            System.out.println("Organizations already exist — skipping preload.");
            return;
        }

        // ===== Create organizations =====
        Organization org1 = createOrganization("TechNova Ltd", "VAT123456", "SAP001");
        Organization org2 = createOrganization("GlobalSoft GmbH", "VAT654321", "SAP002");
        Organization org3 = createOrganization("InnoCore Systems", "VAT789012", "SAP003");

        organizationRepository.saveAll(List.of(org1, org2, org3));

        // ===== Create users =====
        createUser("Alice", "Johnson", "alice@technova.com", Set.of(org1));
        createUser("Bob", "Müller", "bob@globalsoft.com", Set.of(org2));
        createUser("Charlie", "Smith", "charlie@innocore.com", Set.of(org3));
    }

    // Helper method to create an organization
    private Organization createOrganization(String name, String vat, String sapId) {
        Organization org = new Organization();
        org.setName(name);
        org.setVat(vat);
        org.setSapId(sapId);
        return org;
    }

    // Helper method to create a user with assigned organizations
    private void createUser(String firstName, String lastName, String email, Set<Organization> orgs) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setOrganizations(orgs);
        userRepository.save(user);
    }
}

