# 🗺️ Quick Location Map

## 📍 At a Glance

### CRUD Operations - All in Service Layer
```
📁 src/main/java/com/trainreservation/auth/service/UserServiceImpl.java
├── CREATE: registerUser() → Line 23
├── READ:   loginUser() → Line 44
├── READ:   getAllUsers() → Line 113
├── READ:   getUserById() → Line 119
├── UPDATE: updateProfile() → Line 64
├── UPDATE: changePassword() → Line 95
├── UPDATE: updateUserRole() → Line 125
├── DELETE: deleteUser() → Line 151
```

---

### Design Patterns - Both in util/ Package
```
📁 src/main/java/com/trainreservation/auth/util/
├── 🔒 PasswordEncoderSingleton.java (Singleton Pattern)
│   └── getInstance() → Line 14 (Thread-safe, ONE instance)
│
└── 🏭 DTOMapperFactory.java (Factory Pattern)
    ├── createUserDTO() → Line 29
    ├── createDetailedUserDTO() → Line 51
    ├── createPublicUserDTO() → Line 74
    └── createAdminUserDTO() → Line 95
```

---

### Validations - Three Layers

#### 1. Jakarta Bean Validation (Annotations)
```
📁 src/main/java/com/trainreservation/auth/
├── 📄 entity/User.java
│   ├── @NotBlank on username, email, password
│   ├── @Size(min=3, max=50) on username
│   ├── @Size(min=6) on password
│   └── @Email on email
│
└── 📄 dto/
    ├── UserDTO.java → Registration validations
    ├── LoginRequest.java → Login validations
    ├── UserUpdateDTO.java → Update validations
    ├── PasswordChangeRequest.java → Password change validations
    └── PasswordResetRequest.java → Reset validations
```

#### 2. Business Logic Validations
```
📁 src/main/java/com/trainreservation/auth/service/UserServiceImpl.java
├── Username uniqueness → Line 24-26
├── Email uniqueness → Line 27-29
├── Password matching → Line 51-53
├── Active account check → Line 56-58
├── Current password verification → Line 100-102
└── Self-deletion prevention → Line 153-155
```

#### 3. Security Validations
```
📁 src/main/java/com/trainreservation/auth/
├── 📄 config/SecurityConfig.java
│   ├── Public endpoints → Line 32-34
│   ├── Admin-only endpoints → Line 37-38
│   └── Authenticated endpoints → Line 40-46
│
└── 📄 controller/UserController.java
    ├── Session validation → Line 72-74
    ├── Ownership validation (profile) → Line 100-104
    └── Ownership validation (password) → Line 123-127
```

---

## 🎯 Most Important Files

### Top 5 Core Files
1. **`UserServiceImpl.java`** (192 lines)
   - All CRUD business logic
   - Uses both design patterns
   - Contains business validations

2. **`UserController.java`** (195 lines)
   - All REST API endpoints
   - Session management
   - Ownership validations

3. **`SecurityConfig.java`** (64 lines)
   - Role-based access control
   - Endpoint protection
   - Session configuration

4. **`User.java`** (57 lines)
   - Database entity
   - Field validations
   - Timestamps

5. **`UserRepository.java`** (28 lines)
   - Data access methods
   - Custom queries

---

## 📊 File Size Overview

| Category | File | Lines | Purpose |
|----------|------|-------|---------|
| **Controller** | UserController.java | 195 | REST API |
| **Service** | UserServiceImpl.java | 192 | Business logic |
| **Pattern** | DTOMapperFactory.java | 106 | Factory Pattern |
| **Config** | SecurityConfig.java | 64 | Security rules |
| **Entity** | User.java | 57 | Database model |
| **DTO** | UserDTO.java | 29 | Data transfer |
| **Repository** | UserRepository.java | 28 | Data access |
| **Pattern** | PasswordEncoderSingleton.java | 20 | Singleton Pattern |
| **DTO** | PasswordResetRequest.java | 17 | Reset payload |
| **DTO** | PasswordChangeRequest.java | 15 | Change payload |
| **DTO** | UserUpdateDTO.java | 14 | Update payload |
| **DTO** | LoginRequest.java | 13 | Login payload |

---

## 🔍 Find By Feature

### Authentication
- **Login**: `UserController.loginUser()` (Line 36) → `UserServiceImpl.loginUser()` (Line 44)
- **Register**: `UserController.registerUser()` (Line 31) → `UserServiceImpl.registerUser()` (Line 23)
- **Logout**: `UserController.logout()` (Line 61)
- **Current User**: `UserController.getCurrentUser()` (Line 69)

### Profile Management
- **View Profile**: `UserServiceImpl.getUserById()` (Line 119)
- **Update Profile**: `UserController.updateProfile()` (Line 91) → `UserServiceImpl.updateProfile()` (Line 64)
- **Change Password**: `UserController.changePassword()` (Line 111) → `UserServiceImpl.changePassword()` (Line 95)
- **Reset Password**: `UserController.resetPassword()` (Line 81) → `UserServiceImpl.resetPassword()` (Line 183)

### User Management (Admin)
- **List Users**: `UserController.getAllUsers()` (Line 140) → `UserServiceImpl.getAllUsers()` (Line 113)
- **Get User Details**: `UserController.getUserById()` (Line 145)
- **Update Role**: `UserController.updateUserRole()` (Line 150)
- **Activate User**: `UserController.activateUser()` (Line 165)
- **Deactivate User**: `UserController.deactivateUser()` (Line 157)
- **Delete User**: `UserController.deleteUser()` (Line 172)

### Security Features
- **Session Creation**: `UserController.loginUser()` (Lines 51-53)
- **Role Check**: `SecurityConfig` (Lines 37-38, 40-46)
- **Ownership Validation**: `UserController.updateProfile()` (Lines 100-104)
- **Password Encoding**: `PasswordEncoderSingleton.getInstance()` used in `UserServiceImpl` (Line 20)

---

## 🎨 Pattern Usage Locations

### Singleton Pattern Usage
**Definition**: `util/PasswordEncoderSingleton.java`

**Used in** `UserServiceImpl.java`:
- Line 20: Instance initialization
- Line 35: Encode password (register)
- Line 51: Match password (login)
- Line 105: Encode password (change)
- Line 186: Encode password (reset)

### Factory Pattern Usage
**Definition**: `util/DTOMapperFactory.java`

**Used in** `UserServiceImpl.java`:
- Line 41: `registerUser()` - Convert saved user
- Line 60: `loginUser()` - Convert authenticated user
- Line 92: `updateProfile()` - Convert updated user
- Line 115: `getAllUsers()` - Stream mapping
- Line 122: `getUserById()` - Convert single user
- Line 130: `updateUserRole()` - Convert role-updated user
- Line 173: `getUsersByRole()` - Stream mapping
- Line 179: `getActiveUsers()` - Stream mapping

---

## ✅ Validation Checklist

### Input Validations (DTOs)
- ✅ Username: 3-50 chars, not blank
- ✅ Email: valid format, not blank
- ✅ Password: min 6 chars, not blank
- ✅ All validated via `@Valid` in controller

### Business Validations (Service)
- ✅ Username must be unique
- ✅ Email must be unique
- ✅ Password must match on login
- ✅ Account must be active to login
- ✅ Current password must be correct to change
- ✅ Cannot delete own account

### Security Validations (Config + Controller)
- ✅ Public: register, login, reset password
- ✅ Admin-only: `/api/auth/admin/**`
- ✅ Authenticated: profile, change password
- ✅ Ownership: Can only edit own profile
- ✅ Session-based authentication

---

## 📖 Documentation Files

### Design Pattern Docs
- `SINGLETON_COMPLETE_GUIDE.md` - Comprehensive Singleton guide
- `FACTORY_PATTERN_GUIDE.md` - Comprehensive Factory guide
- `DESIGN_PATTERN_COMPARISON.md` - Singleton vs Factory
- `DESIGN_PATTERNS_VISUAL.md` - Visual diagrams
- `DESIGN_PATTERNS_QUICK_REFERENCE.md` - Quick reference
- `FINAL_RECOMMENDATION.md` - Pattern recommendation

### Component Location Docs
- `COMPONENT_LOCATIONS_GUIDE.md` - Complete location guide (this is the detailed one)
- `QUICK_LOCATION_MAP.md` - This file (quick reference)

### Security Docs
- `SECURITY_IMPLEMENTATION.md` - Security details
- `SECURITY_DIAGRAMS.md` - Security diagrams

### General Docs
- `README.md` - Project overview
- `SETUP_AND_RUN.md` - Setup instructions

---

## 🚀 Quick Navigation

Want to find...
- **CRUD operations?** → `service/UserServiceImpl.java`
- **REST endpoints?** → `controller/UserController.java`
- **Validations?** → `dto/*.java` + `service/UserServiceImpl.java`
- **Design patterns?** → `util/PasswordEncoderSingleton.java` + `util/DTOMapperFactory.java`
- **Security rules?** → `config/SecurityConfig.java`
- **Database model?** → `entity/User.java`
- **Data access?** → `repository/UserRepository.java`

---

**Quick Tip**: Use Ctrl+F (or Cmd+F) with file names or line numbers to quickly navigate to specific locations in your IDE!

**Last Updated**: 2025-10-24
