package com.trainreservation.auth.controller;

import com.trainreservation.auth.dto.*;
import com.trainreservation.auth.entity.User;
import com.trainreservation.auth.entity.UserRole;
import com.trainreservation.auth.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class UserController {
    private final UserService userService;

    // PUBLIC ENDPOINTS
    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody UserDTO userDTO) {
        UserDTO registeredUser = userService.registerUser(userDTO);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> loginUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        UserDTO user = userService.loginUser(loginRequest);
        
        // Create authentication and store in session
        org.springframework.security.authentication.UsernamePasswordAuthenticationToken authentication =
                new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                        user.getId().toString(),
                        null,
                        Collections.singletonList(new SimpleGrantedAuthority(user.getRole().toString()))
                );
        
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        
        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
        session.setAttribute("USER_ID", user.getId());
        session.setAttribute("USER_ROLE", user.getRole().toString());
        
        return ResponseEntity.ok(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logged out successfully");
    }

    @GetMapping("/current-user")
    public ResponseEntity<UserDTO> getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("USER_ID") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        Long userId = (Long) session.getAttribute("USER_ID");
        UserDTO user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody PasswordResetRequest request) {
        boolean success = userService.resetPassword(request);
        return success ?
                ResponseEntity.ok("Password reset successfully") :
                ResponseEntity.badRequest().body("Password reset failed");
    }

    // USER PROFILE MANAGEMENT - Users can only edit their own profile
    @PutMapping("/users/{userId}/profile")
    public ResponseEntity<UserDTO> updateProfile(
            @PathVariable Long userId,
            @Valid @RequestBody UserUpdateDTO userUpdateDTO,
            HttpServletRequest request) {
        
        // Verify user is updating their own profile
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("USER_ID") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        Long currentUserId = (Long) session.getAttribute("USER_ID");
        if (!currentUserId.equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        }
        
        UserDTO updatedUser = userService.updateProfile(userId, userUpdateDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/users/{userId}/change-password")
    public ResponseEntity<String> changePassword(
            @PathVariable Long userId,
            @Valid @RequestBody PasswordChangeRequest request,
            HttpServletRequest httpRequest) {
        
        // Verify user is changing their own password
        HttpSession session = httpRequest.getSession(false);
        if (session == null || session.getAttribute("USER_ID") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        Long currentUserId = (Long) session.getAttribute("USER_ID");
        if (!currentUserId.equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        }
        
        boolean success = userService.changePassword(userId, request);
        return success ?
                ResponseEntity.ok("Password changed successfully") :
                ResponseEntity.badRequest().body("Password change failed");
    }

    // ADMIN USER MANAGEMENT - Only accessible by ADMIN role (enforced by SecurityConfig)
    @GetMapping("/admin/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/admin/users/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/admin/users/{userId}/role")
    public ResponseEntity<UserDTO> updateUserRole(
            @PathVariable Long userId,
            @RequestParam UserRole newRole) {
        UserDTO updatedUser = userService.updateUserRole(userId, newRole);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/admin/users/{userId}/deactivate")
    public ResponseEntity<String> deactivateUser(@PathVariable Long userId) {
        boolean success = userService.deactivateUser(userId);
        return success ?
                ResponseEntity.ok("User deactivated successfully") :
                ResponseEntity.badRequest().body("User deactivation failed");
    }

    @PutMapping("/admin/users/{userId}/activate")
    public ResponseEntity<String> activateUser(@PathVariable Long userId) {
        boolean success = userService.activateUser(userId);
        return success ?
                ResponseEntity.ok("User activated successfully") :
                ResponseEntity.badRequest().body("User activation failed");
    }

    @DeleteMapping("/admin/users/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Long currentUserId = (Long) session.getAttribute("USER_ID");
        
        boolean success = userService.deleteUser(userId, currentUserId);
        return success ?
                ResponseEntity.ok("User deleted successfully") :
                ResponseEntity.badRequest().body("Cannot delete user");
    }

    @GetMapping("/admin/users/role/{role}")
    public ResponseEntity<List<UserDTO>> getUsersByRole(@PathVariable UserRole role) {
        List<UserDTO> users = userService.getUsersByRole(role);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/admin/users/status/{active}")
    public ResponseEntity<List<UserDTO>> getUsersByStatus(@PathVariable boolean active) {
        List<UserDTO> users = userService.getActiveUsers(active);
        return ResponseEntity.ok(users);
    }
}