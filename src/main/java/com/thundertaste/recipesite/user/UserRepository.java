package com.thundertaste.recipesite.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    // Custom queries can be added here if needed
    Optional<User> findByUsername(String username);

    @Query("SELECT new com.thundertaste.recipesite.user.UserTransferObject(u) FROM User u WHERE u.username = :username")
    Optional<UserTransferObject> findUserTransferObjectByUsername(@Param("username") String username);


    Optional<User> findByEmail(String email);


    boolean existsByUsername(String username);

    boolean existsByEmail(String email);



}
