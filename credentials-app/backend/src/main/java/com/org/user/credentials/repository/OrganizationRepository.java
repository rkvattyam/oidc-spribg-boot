package com.org.user.credentials.repository;

import com.org.user.credentials.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationRepository  extends JpaRepository<Organization, Long> {

    @Query("""
    SELECT o FROM Organization o
    JOIN o.users u
    WHERE u.email = :email
""")
    List<Organization> findAllByUserEmail(@Param("email") String email);
}
