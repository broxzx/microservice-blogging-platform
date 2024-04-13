package com.example.security.repository;

import com.example.security.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The UserRepository interface is a data access object that provides methods for managing UserEntity objects in the database.
 * It extends the JpaRepository interface to inherit common CRUD operations.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    /**
     * Retrieves a user entity by username.
     *
     * @param username the username of the user
     * @return an {@code Optional} containing the user entity if found, otherwise empty
     */
    Optional<UserEntity> findByUsername(String username);
}