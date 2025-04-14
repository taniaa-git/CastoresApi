package com.castores.api.repository;

import com.castores.api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
   User findFirstByUsername(String username);
}
