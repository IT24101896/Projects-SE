# 📍 Complete Component Locations Guide

## Table of Contents
1. [CRUD Operations](#crud-operations)
2. [Design Patterns](#design-patterns)
3. [Validations](#validations)
4. [Security](#security)
5. [Quick Reference Map](#quick-reference-map)

---

## 🗂️ CRUD Operations

### Architecture Layers

```
┌─────────────────────────────────────────────────────┐
│                  Controller Layer                   │
│  📁 src/main/java/.../controller/                  │
│  📄 UserController.java (REST API Endpoints)       │
└─────────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────────┐
│                  Service Layer                      │
│  📁 src/main/java/.../service/                     │
│  📄 UserService.java (Interface)                   │
│  📄 UserServiceImpl.java (Business Logic)          │
└─────────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────────┐
│                  Repository Layer                   │
│  📁 src/main/java/.../repository/                  │
│  📄 UserRepository.java (Data Access)              │
└─────────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────────┐
│                  Entity Layer                       │
│  📁 src/main/java/.../entity/                      │
│  📄 User.java (Database Model)                     │
└─────────────────────────────────────────────────────┘
```

---

### 1️⃣ CREATE Operations

| Layer | File | Method | Lines |
|-------|------|--------|-------|
| **Controller** | `UserController.java` | `registerUser()` | 31-34 |
| **Service** | `UserServiceImpl.java` | `registerUser()` | 23-42 |
| **Repository** | `UserRepository.java` | `save()` (inherited) | - |
| **Entity** | `User.java` | Entity definition | 1-57 |

**Full Path**: 
```
📁 src/main/java/com/trainreservation/auth/
├── controller/UserController.java (Line 31)
├── service/UserServiceImpl.java (Line 23)
├── repository/UserRepository.java (JpaRepository)
└── entity/User.java
```

**Validation**: Username/email uniqueness check (Lines 24-29)

---

### 2️⃣ READ Operations

#### A. Login (Authentication)
| Layer | File | Method | Lines |
|-------|------|--------|-------|
| **Controller** | `UserController.java` | `loginUser()` | 36-59 |
| **Service** | `UserServiceImpl.java` | `loginUser()` | 44-62 |
| **Repository** | `UserRepository.java` | `findByUsernameOrEmail()` | 17 |

**Validation**: Password matching (Line 51-53), Active account (Line 56-58)

---

#### B. Get All Users (Admin)
| Layer | File | Method | Lines |
|-------|------|--------|-------|
| **Controller** | `UserController.java` | `getAllUsers()` | 140-143 |
| **Service** | `UserServiceImpl.java` | `getAllUsers()` | 113-117 |
| **Repository** | `UserRepository.java` | `findAll()` (inherited) | - |

**Uses**: Factory Pattern - `DTOMapperFactory.createUserDTO()`

---

#### C. Get User by ID
| Layer | File | Method | Lines |
|-------|------|--------|-------|
| **Controller** | `UserController.java` | `getUserById()` | 145-148 |
| **Service** | `UserServiceImpl.java` | `getUserById()` | 119-123 |
| **Repository** | `UserRepository.java` | `findById()` (inherited) | - |

---

#### D. Get Current User (Session-Based)
| Layer | File | Method | Lines |
|-------|------|--------|-------|
| **Controller** | `UserController.java` | `getCurrentUser()` | 69-79 |
| **Service** | `UserServiceImpl.java` | `getUserById()` | 119-123 |

**Validation**: Session existence check (Line 72-74)

---

#### E. Get Users by Role
| Layer | File | Method | Lines |
|-------|------|--------|-------|
| **Controller** | `UserController.java` | `getUsersByRole()` | 183-186 |
| **Service** | `UserServiceImpl.java` | `getUsersByRole()` | 171-175 |
| **Repository** | `UserRepository.java` | `findByRole()` | 18 |

---

#### F. Get Users by Status
| Layer | File | Method | Lines |
|-------|------|--------|-------|
| **Controller** | `UserController.java` | `getUsersByStatus()` | 188-191 |
| **Service** | `UserServiceImpl.java` | `getActiveUsers()` | 177-181 |
| **Repository** | `UserRepository.java` | `findByActive()` | 19 |

---

### 3️⃣ UPDATE Operations

#### A. Update Profile
| Layer | File | Method | Lines |
|-------|------|--------|-------|
| **Controller** | `UserController.java` | `updateProfile()` | 91-109 |
| **Service** | `UserServiceImpl.java` | `updateProfile()` | 64-93 |
| **Repository** | `UserRepository.java` | `save()` (inherited) | - |

**Validation**: 
- Ownership check (Lines 100-104)
- Username uniqueness (Lines 70-77)
- Email uniqueness (Lines 79-88)

---

#### B. Change Password
| Layer | File | Method | Lines |
|-------|------|--------|-------|
| **Controller** | `UserController.java` | `changePassword()` | 111-133 |
| **Service** | `UserServiceImpl.java` | `changePassword()` | 95-109 |
| **Repository** | `UserRepository.java` | `save()` (inherited) | - |

**Validation**: 
- Ownership check (Lines 123-127)
- Current password verification (Lines 100-102)

---

#### C. Reset Password
| Layer | File | Method | Lines |
|-------|------|--------|-------|
| **Controller** | `UserController.java` | `resetPassword()` | 81-89 |
| **Service** | `UserServiceImpl.java` | `resetPassword()` | 183-189 |
| **Repository** | `UserRepository.java` | `save()` (inherited) | - |

**Validation**: Email existence check (Line 184)

---

#### D. Update User Role (Admin)
| Layer | File | Method | Lines |
|-------|------|--------|-------|
| **Controller** | `UserController.java` | `updateUserRole()` | 150-155 |
| **Service** | `UserServiceImpl.java` | `updateUserRole()` | 125-131 |
| **Repository** | `UserRepository.java` | `save()` (inherited) | - |

---

#### E. Activate/Deactivate User (Admin)
| Layer | File | Method | Lines |
|-------|------|--------|-------|
| **Controller** | `UserController.java` | `activateUser()` | 165-170 |
| **Controller** | `UserController.java` | `deactivateUser()` | 157-163 |
| **Service** | `UserServiceImpl.java` | `activateUser()` | 143-149 |
| **Service** | `UserServiceImpl.java` | `deactivateUser()` | 133-141 |

---

### 4️⃣ DELETE Operations

| Layer | File | Method | Lines |
|-------|------|--------|-------|
| **Controller** | `UserController.java` | `deleteUser()` | 172-181 |
| **Service** | `UserServiceImpl.java` | `deleteUser()` | 151-162 |
| **Repository** | `UserRepository.java` | `delete()` (inherited) | - |

**Validation**: Self-deletion prevention (Lines 153-155)

---

## 🏭 Design Patterns

### 1️⃣ Singleton Pattern

**File**: `src/main/java/com/trainreservation/auth/util/PasswordEncoderSingleton.java`

```
📁 src/main/java/com/trainreservation/auth/util/
└── 📄 PasswordEncoderSingleton.java (20 lines)
    ├── Line 8: private static PasswordEncoder instance
    ├── Line 10-12: private PasswordEncoderSingleton() {}
    └── Line 14-19: public static synchronized getInstance()
```

**Purpose**: Ensure ONE shared BCryptPasswordEncoder instance

**Used In**:
- `UserServiceImpl.java` (Line 20):
  ```java
  private final PasswordEncoder passwordEncoder = 
      PasswordEncoderSingleton.getInstance();
  ```

**Usage Locations in UserServiceImpl**:
- Line 35: Encode password during registration
- Line 51: Verify password during login
- Line 105: Encode new password
- Line 186: Encode reset password

---

### 2️⃣ Factory Pattern

**File**: `src/main/java/com/trainreservation/auth/util/DTOMapperFactory.java`

```
📁 src/main/java/com/trainreservation/auth/util/
└── 📄 DTOMapperFactory.java (106 lines)
    ├── Line 29-42: createUserDTO() - Standard DTO
    ├── Line 51-65: createDetailedUserDTO() - Extended info
    ├── Line 74-86: createPublicUserDTO() - Privacy-focused
    └── Line 95-103: createAdminUserDTO() - Admin view
```

**Purpose**: Create different types of UserDTOs from User entities

**Used In** (`UserServiceImpl.java`):
| Line | Method | Usage |
|------|--------|-------|
| 41 | `registerUser()` | Convert saved user to DTO |
| 60 | `loginUser()` | Convert authenticated user to DTO |
| 92 | `updateProfile()` | Convert updated user to DTO |
| 115 | `getAllUsers()` | Stream mapping with method reference |
| 122 | `getUserById()` | Convert single user to DTO |
| 130 | `updateUserRole()` | Convert role-updated user to DTO |
| 173 | `getUsersByRole()` | Stream mapping |
| 179 | `getActiveUsers()` | Stream mapping |

---

## ✅ Validations

### 1️⃣ Jakarta Validation Annotations

#### A. Entity Level (`User.java`)
**File**: `src/main/java/com/trainreservation/auth/entity/User.java`

| Field | Annotation | Line | Message |
|-------|-----------|------|---------|
| username | `@NotBlank` | 22 | "Username is required" |
| username | `@Size(min=3, max=50)` | 23 | "Username must be between 3 and 50 characters" |
| username | `@Column(unique=true)` | 24 | Database constraint |
| email | `@NotBlank` | 27 | "Email is required" |
| email | `@Email` | 28 | "Invalid email format" |
| email | `@Column(unique=true)` | 29 | Database constraint |
| password | `@NotBlank` | 32 | "Password is required" |
| password | `@Size(min=6)` | 33 | "Password must be at least 6 characters" |
| role | `@Column(nullable=false)` | 37 | Database constraint |

---

#### B. DTO Level

##### 📄 UserDTO.java (Registration)
**File**: `src/main/java/com/trainreservation/auth/dto/UserDTO.java`

| Field | Annotation | Line |
|-------|-----------|------|
| username | `@NotBlank`, `@Size(min=3, max=50)` | 14-15 |
| email | `@NotBlank`, `@Email` | 18-19 |
| password | `@NotBlank`, `@Size(min=6)` | 22-23 |

---

##### 📄 LoginRequest.java
**File**: `src/main/java/com/trainreservation/auth/dto/LoginRequest.java`

| Field | Annotation | Line |
|-------|-----------|------|
| usernameOrEmail | `@NotBlank` | 8 |
| password | `@NotBlank` | 11 |

---

##### 📄 UserUpdateDTO.java
**File**: `src/main/java/com/trainreservation/auth/dto/UserUpdateDTO.java`

| Field | Annotation | Line |
|-------|-----------|------|
| username | `@Size(min=3, max=50)` (Optional) | 9 |
| email | `@Email` (Optional) | 12 |

---

##### 📄 PasswordChangeRequest.java
**File**: `src/main/java/com/trainreservation/auth/dto/PasswordChangeRequest.java`

| Field | Annotation | Line |
|-------|-----------|------|
| currentPassword | `@NotBlank` | 9 |
| newPassword | `@NotBlank`, `@Size(min=6)` | 12-13 |

---

##### 📄 PasswordResetRequest.java
**File**: `src/main/java/com/trainreservation/auth/dto/PasswordResetRequest.java`

| Field | Annotation | Line |
|-------|-----------|------|
| email | `@NotBlank`, `@Email` | 10-11 |
| newPassword | `@NotBlank`, `@Size(min=6)` | 14-15 |

---

### 2️⃣ Business Logic Validations

**File**: `src/main/java/com/trainreservation/auth/service/UserServiceImpl.java`

| Validation | Method | Lines | Logic |
|-----------|--------|-------|-------|
| **Username uniqueness** | `registerUser()` | 24-26 | Check `findByUsername()` exists |
| **Email uniqueness** | `registerUser()` | 27-29 | Check `findByEmail()` exists |
| **Password matching** | `loginUser()` | 51-53 | `passwordEncoder.matches()` |
| **Active account check** | `loginUser()` | 56-58 | Check `user.isActive()` |
| **Username uniqueness** | `updateProfile()` | 70-77 | Prevent duplicate (exclude self) |
| **Email uniqueness** | `updateProfile()` | 79-88 | Prevent duplicate (exclude self) |
| **Current password verification** | `changePassword()` | 100-102 | `passwordEncoder.matches()` |
| **Self-deletion prevention** | `deleteUser()` | 153-155 | Compare userId with currentUserId |

---

### 3️⃣ Controller-Level Validations

**File**: `src/main/java/com/trainreservation/auth/controller/UserController.java`

| Validation | Method | Lines | Logic |
|-----------|--------|-------|-------|
| **Session existence** | `getCurrentUser()` | 72-74 | Check session != null |
| **Ownership validation** | `updateProfile()` | 100-104 | Session userId == path userId |
| **Ownership validation** | `changePassword()` | 123-127 | Session userId == path userId |
| **Admin authorization** | All `/admin/**` endpoints | Config | Spring Security enforcement |

**Validation Trigger**: `@Valid` annotation on `@RequestBody` parameters

---

### 4️⃣ Security Validations

**File**: `src/main/java/com/trainreservation/auth/config/SecurityConfig.java`

```
📁 src/main/java/com/trainreservation/auth/config/
└── 📄 SecurityConfig.java (64 lines)
```

| Endpoint Pattern | Security Rule | Lines |
|-----------------|---------------|-------|
| `/api/auth/register` | Public | 32 |
| `/api/auth/login` | Public | 33 |
| `/api/auth/reset-password` | Public | 34 |
| `/api/auth/admin/**` | Admin only (`hasAuthority("ADMIN")`) | 37-38 |
| `/api/auth/users/**` | Authenticated (`.authenticated()`) | 40-46 |
| `/api/auth/current-user` | Authenticated | 42 |
| `/api/auth/logout` | Authenticated | 43 |

**Session Management**:
- Line 54-59: Logout configuration
- Invalidates HTTP session
- Deletes JSESSIONID cookie

---

## 🔐 Security

### Session-Based Authentication

**Login Flow** (`UserController.java`, Lines 36-59):
1. Validate credentials via `userService.loginUser()`
2. Create Spring Security authentication token (Line 41-46)
3. Store in SecurityContext (Line 48-49)
4. Create HTTP session (Line 51)
5. Store user ID and role in session (Lines 52-53)

**Logout Flow** (`UserController.java`, Lines 61-68):
1. Get current session (Line 63)
2. Invalidate session (Line 65)
3. Clear SecurityContext (Line 67)

---

### Role-Based Access Control (RBAC)

**Security Configuration** (`SecurityConfig.java`):

```java
// Admin-only endpoints
.requestMatchers("/api/auth/admin/**")
    .hasAuthority("ADMIN")

// Authenticated user endpoints  
.requestMatchers("/api/auth/users/**")
    .authenticated()
```

**Roles**:
- `ADMIN`: Full access to user management
- `PASSENGER`: Access own profile
- `STAFF`: Access own profile

---

### Ownership Validation

**Example** (`UserController.java`, Lines 100-104):
```java
Long currentUserId = (Long) session.getAttribute("USER_ID");
if (!currentUserId.equals(userId)) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
}
```

**Applied In**:
- Update profile (Lines 100-104)
- Change password (Lines 123-127)

---

## 📚 Quick Reference Map

### File Structure
```
src/main/java/com/trainreservation/auth/
│
├── 📁 config/
│   ├── SecurityConfig.java (64 lines) - Spring Security
│   └── GlobalExceptionHandler.java - Error handling
│
├── 📁 controller/
│   └── UserController.java (195 lines) - REST API endpoints
│
├── 📁 service/
│   ├── UserService.java (26 lines) - Interface
│   └── UserServiceImpl.java (192 lines) - Business logic
│
├── 📁 repository/
│   └── UserRepository.java (28 lines) - Data access
│
├── 📁 entity/
│   ├── User.java (57 lines) - Database model
│   └── UserRole.java - Enum (ADMIN, PASSENGER, STAFF)
│
├── 📁 dto/
│   ├── UserDTO.java (29 lines) - User data transfer
│   ├── LoginRequest.java (13 lines) - Login payload
│   ├── UserUpdateDTO.java (14 lines) - Profile update
│   ├── PasswordChangeRequest.java (15 lines) - Password change
│   └── PasswordResetRequest.java (17 lines) - Password reset
│
└── 📁 util/
    ├── PasswordEncoderSingleton.java (20 lines) - Singleton Pattern
    └── DTOMapperFactory.java (106 lines) - Factory Pattern
```

---

### Pattern Usage Summary

| Component | Pattern | File | Lines |
|-----------|---------|------|-------|
| **Password Encoding** | Singleton | `PasswordEncoderSingleton.java` | 1-20 |
| **DTO Mapping** | Factory | `DTOMapperFactory.java` | 1-106 |
| **Data Access** | Repository | `UserRepository.java` | 1-28 |
| **Business Logic** | Service | `UserServiceImpl.java` | 1-192 |
| **REST API** | Controller | `UserController.java` | 1-195 |
| **Security** | Config | `SecurityConfig.java` | 1-64 |

---

### Validation Summary

| Type | Location | Count |
|------|----------|-------|
| **Jakarta Annotations** | Entity + DTOs | 20+ |
| **Business Logic** | Service layer | 8 |
| **Security** | Config + Controller | 6 |
| **Database Constraints** | Entity | 3 (unique, nullable) |

---

## 🎯 Key Locations Cheat Sheet

### CRUD Operations
- **C**: `UserServiceImpl.registerUser()` (Line 23)
- **R**: `UserServiceImpl.getAllUsers()` (Line 113)
- **U**: `UserServiceImpl.updateProfile()` (Line 64)
- **D**: `UserServiceImpl.deleteUser()` (Line 151)

### Design Patterns
- **Singleton**: `util/PasswordEncoderSingleton.java`
- **Factory**: `util/DTOMapperFactory.java`

### Validations
- **Annotations**: `dto/*.java` + `entity/User.java`
- **Business Logic**: `service/UserServiceImpl.java`
- **Security**: `config/SecurityConfig.java`

---

**Last Updated**: 2025-10-24  
**Version**: 1.0
