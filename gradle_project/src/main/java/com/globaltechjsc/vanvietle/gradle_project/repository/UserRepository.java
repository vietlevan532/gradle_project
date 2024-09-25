package com.globaltechjsc.vanvietle.gradle_project.repository;

import com.globaltechjsc.vanvietle.gradle_project.domain.Role;
import com.globaltechjsc.vanvietle.gradle_project.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);
    Optional<User> findByUsername(String username);
    Optional<User> findByRole(Role role);
}
