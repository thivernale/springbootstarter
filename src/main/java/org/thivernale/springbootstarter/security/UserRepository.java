package org.thivernale.springbootstarter.security;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.thivernale.springbootstarter.security.models.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserName(String username);
}
