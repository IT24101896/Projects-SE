package com.trainreservation.auth.service;

import com.trainreservation.auth.dto.*;
import com.trainreservation.auth.entity.User;
import com.trainreservation.auth.entity.UserRole;
import com.trainreservation.auth.repository.UserRepository;
import com.trainreservation.auth.util.PasswordEncoderSingleton;
import com.trainreservation.auth.util.DTOMapperFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = PasswordEncoderSingleton.getInstance();

    @Override
    public UserDTO registerUser(UserDTO userDTO) {
        // Validation: Check if username or email already exists
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        // Create new user
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(userDTO.getRole() != null ? userDTO.getRole() : UserRole.PASSENGER);
        user.setActive(true);

        User savedUser = userRepository.save(user);
        return DTOMapperFactory.createUserDTO(savedUser);
    }

    @Override
    public UserDTO loginUser(LoginRequest loginRequest) {
        // Find user by username or email
        User user = userRepository.findByUsernameOrEmail(loginRequest.getUsernameOrEmail(),
                        loginRequest.getUsernameOrEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Validate password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // Check if account is active
        if (!user.isActive()) {
            throw new RuntimeException("Account is deactivated");
        }

        return DTOMapperFactory.createUserDTO(user);
    }

    @Override
    public UserDTO updateProfile(Long userId, UserUpdateDTO userUpdateDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update allowed fields only
        if (userUpdateDTO.getUsername() != null && !userUpdateDTO.getUsername().isEmpty()) {
            // Check if new username is not taken by others
            userRepository.findByUsername(userUpdateDTO.getUsername())
                    .ifPresent(existingUser -> {
                        if (!existingUser.getId().equals(userId)) {
                            throw new RuntimeException("Username already taken");
                        }
                    });
            user.setUsername(userUpdateDTO.getUsername());
        }

        if (userUpdateDTO.getEmail() != null && !userUpdateDTO.getEmail().isEmpty()) {
            // Check if new email is not taken by others
            userRepository.findByEmail(userUpdateDTO.getEmail())
                    .ifPresent(existingUser -> {
                        if (!existingUser.getId().equals(userId)) {
                            throw new RuntimeException("Email already taken");
                        }
                    });
            user.setEmail(userUpdateDTO.getEmail());
        }

        User updatedUser = userRepository.save(user);
        return DTOMapperFactory.createUserDTO(updatedUser);
    }

    @Override
    public boolean changePassword(Long userId, PasswordChangeRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Verify current password
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        // Set new password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return true;
    }

    // ADMIN METHODS
    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(DTOMapperFactory::createUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return DTOMapperFactory.createUserDTO(user);
    }

    @Override
    public UserDTO updateUserRole(Long userId, UserRole newRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        user.setRole(newRole);
        User updatedUser = userRepository.save(user);
        return DTOMapperFactory.createUserDTO(updatedUser);
    }

    @Override
    public boolean deactivateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        user.setActive(false);
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean activateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        user.setActive(true);
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean deleteUser(Long userId, Long currentUserId) {
        // Prevent self-deletion
        if (userId.equals(currentUserId)) {
            throw new RuntimeException("Cannot delete your own account");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        userRepository.delete(user);
        return true;
    }

    @Override
    public List<UserDTO> getUsersByRole(UserRole role) {
        return userRepository.findByRole(role).stream()
                .map(DTOMapperFactory::createUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getActiveUsers(boolean active) {
        return userRepository.findByActive(active).stream()
                .map(DTOMapperFactory::createUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean resetPassword(PasswordResetRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + request.getEmail()));
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return true;
    }

    // Removed: convertToDTO method replaced by DTOMapperFactory.createUserDTO()
    // This demonstrates the Factory Pattern - centralized object creation logic
}