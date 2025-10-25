package com.trainreservation.auth.service;

import com.trainreservation.auth.dto.*;
import com.trainreservation.auth.entity.UserRole;
import java.util.List;

public interface UserService {
    // Authentication
    UserDTO registerUser(UserDTO userDTO);
    UserDTO loginUser(LoginRequest loginRequest);
    boolean resetPassword(PasswordResetRequest request);

    // User Management (Admin only)
    List<UserDTO> getAllUsers();
    UserDTO getUserById(Long id);
    UserDTO updateUserRole(Long userId, UserRole newRole);
    boolean deactivateUser(Long userId);
    boolean activateUser(Long userId);
    boolean deleteUser(Long userId, Long currentUserId);
    List<UserDTO> getUsersByRole(UserRole role);
    List<UserDTO> getActiveUsers(boolean active);

    // User Self-management
    UserDTO updateProfile(Long userId, UserUpdateDTO userUpdateDTO);
    boolean changePassword(Long userId, PasswordChangeRequest request);
}