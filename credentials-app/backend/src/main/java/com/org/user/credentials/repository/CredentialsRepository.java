package com.org.user.credentials.repository;

import com.org.user.credentials.entity.Credentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CredentialsRepository extends JpaRepository<Credentials, Long> {
    Optional<Credentials> findByClientId(String clientId);
    List<Credentials> findByCreatedById(Long userId);
    @Query("""
        SELECT c 
        FROM Credentials c 
        WHERE c.organization.id = :organizationId 
        AND c.createdBy.email = :email
    """)
    List<Credentials> findByOrganizationIdAndUserEmail(@Param("organizationId") Long organizationId,
                                                       @Param("email") String email);

}


