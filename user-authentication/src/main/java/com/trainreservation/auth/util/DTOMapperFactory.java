package com.trainreservation.auth.util;

import com.trainreservation.auth.dto.UserDTO;
import com.trainreservation.auth.entity.User;

/**
 * Factory Pattern Implementation for DTO Mapping
 * 
 * This class provides factory methods to create different types of DTOs from User entities.
 * Using Factory Pattern here provides:
 * - Centralized object creation logic
 * - Easy to add new DTO types (e.g., PublicUserDTO, DetailedUserDTO)
 * - Consistent mapping across the application
 * - Better testability and maintainability
 */
public class DTOMapperFactory {

    // Private constructor to prevent instantiation (Utility class pattern)
    private DTOMapperFactory() {
        throw new UnsupportedOperationException("Utility class - cannot be instantiated");
    }

    /**
     * Creates a standard UserDTO from a User entity
     * Contains all basic user information including role and active status
     * 
     * @param user The User entity to convert
     * @return UserDTO with all standard fields populated
     */
    public static UserDTO createUserDTO(User user) {
        if (user == null) {
            return null;
        }

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setActive(user.isActive());
        // Note: Password is NOT included in DTO for security
        return dto;
    }

    /**
     * Creates a detailed UserDTO with additional metadata
     * Future enhancement: Can include timestamps, last login, etc.
     * 
     * @param user The User entity to convert
     * @return UserDTO with detailed information (currently same as standard)
     */
    public static UserDTO createDetailedUserDTO(User user) {
        if (user == null) {
            return null;
        }

        // Currently returns standard DTO
        // Can be extended to include createdAt, updatedAt, etc.
        UserDTO dto = createUserDTO(user);
        
        // Future enhancements can be added here:
        // dto.setCreatedAt(user.getCreatedAt());
        // dto.setUpdatedAt(user.getUpdatedAt());
        
        return dto;
    }

    /**
     * Creates a public-safe UserDTO with limited information
     * Useful for public APIs where sensitive info should be hidden
     * 
     * @param user The User entity to convert
     * @return UserDTO with only public-safe fields
     */
    public static UserDTO createPublicUserDTO(User user) {
        if (user == null) {
            return null;
        }

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        // Email is excluded for privacy
        dto.setRole(user.getRole());
        // Active status excluded for security
        return dto;
    }

    /**
     * Creates a UserDTO for admin view with all available information
     * Includes all fields that administrators need to see
     * 
     * @param user The User entity to convert
     * @return UserDTO with complete information for admin purposes
     */
    public static UserDTO createAdminUserDTO(User user) {
        if (user == null) {
            return null;
        }

        // For now, same as detailed DTO
        // Can be extended with admin-specific fields
        return createDetailedUserDTO(user);
    }
}
