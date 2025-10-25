package com.trainreservation.auth.repository;

import com.trainreservation.auth.entity.User;
import com.trainreservation.auth.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsernameOrEmail(String username, String email);
    List<User> findByRole(UserRole role);
    List<User> findByActive(boolean active);

    @Modifying
    @Query("UPDATE User u SET u.password = :password WHERE u.email = :email")
    void updatePassword(String email, String password);

    @Modifying
    @Query("UPDATE User u SET u.role = :role WHERE u.id = :userId")
    void updateUserRole(Long userId, UserRole role);
}