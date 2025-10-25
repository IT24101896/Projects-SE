# Factory Pattern Implementation Guide

## 🏭 Overview

This guide documents the **Factory Pattern** implementation in our User Authentication System, complementing the existing **Singleton Pattern** for a robust design pattern architecture.

---

## 📋 Table of Contents
1. [What is Factory Pattern?](#what-is-factory-pattern)
2. [Why We Use Both Patterns](#why-we-use-both-patterns)
3. [Implementation Details](#implementation-details)
4. [Usage Examples](#usage-examples)
5. [Benefits & Comparison](#benefits--comparison)
6. [Best Practices](#best-practices)

---

## 🎯 What is Factory Pattern?

The **Factory Pattern** is a **creational design pattern** that provides an interface for creating objects without specifying their exact classes. It centralizes object creation logic, making the code more maintainable and flexible.

### Key Characteristics:
- **Centralized Creation**: All object creation logic in one place
- **Flexibility**: Easy to add new object types without changing existing code
- **Encapsulation**: Hides complex creation logic from clients
- **Consistency**: Ensures objects are created uniformly

---

## 🔄 Why We Use Both Patterns

Our project strategically uses **TWO design patterns** for different purposes:

### 1. **Singleton Pattern** → [`PasswordEncoderSingleton`](src/main/java/com/trainreservation/auth/util/PasswordEncoderSingleton.java)
**Purpose**: Ensure ONE shared instance of password encoder
- ✅ Single BCryptPasswordEncoder instance
- ✅ Memory efficient
- ✅ Thread-safe
- ✅ Consistent encoding across application

### 2. **Factory Pattern** → [`DTOMapperFactory`](src/main/java/com/trainreservation/auth/util/DTOMapperFactory.java)
**Purpose**: Create different types of DTOs from User entities
- ✅ Multiple DTO creation methods
- ✅ Flexible mapping strategies
- ✅ Easy to extend with new DTO types
- ✅ Centralized conversion logic

---

## 🛠️ Implementation Details

### Class: `DTOMapperFactory`

**Location**: `src/main/java/com/trainreservation/auth/util/DTOMapperFactory.java`

#### Structure:
```java
public class DTOMapperFactory {
    
    // Private constructor prevents instantiation (Utility class)
    private DTOMapperFactory() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    // Factory Methods
    public static UserDTO createUserDTO(User user)           // Standard DTO
    public static UserDTO createDetailedUserDTO(User user)   // Detailed info
    public static UserDTO createPublicUserDTO(User user)     // Public-safe
    public static UserDTO createAdminUserDTO(User user)      // Admin view
}
```

#### Factory Methods Explained:

| Method | Purpose | Fields Included | Use Case |
|--------|---------|-----------------|----------|
| `createUserDTO()` | Standard conversion | All except password | General purpose |
| `createDetailedUserDTO()` | Extended info | Standard + timestamps* | Admin dashboard |
| `createPublicUserDTO()` | Privacy-focused | ID, username, role only | Public APIs |
| `createAdminUserDTO()` | Complete admin view | All available fields | User management |

*Future enhancement - currently returns standard DTO

---

## 💡 Usage Examples

### Before Factory Pattern (Old Code):
```java
// Scattered conversion logic across service layer
private UserDTO convertToDTO(User user) {
    UserDTO dto = new UserDTO();
    dto.setId(user.getId());
    dto.setUsername(user.getUsername());
    dto.setEmail(user.getEmail());
    dto.setRole(user.getRole());
    dto.setActive(user.isActive());
    return dto;
}

// Used everywhere
return convertToDTO(savedUser);
return userRepository.findAll().stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList());
```

### After Factory Pattern (New Code):
```java
// Centralized in factory
return DTOMapperFactory.createUserDTO(savedUser);

// Stream operations
return userRepository.findAll().stream()
        .map(DTOMapperFactory::createUserDTO)
        .collect(Collectors.toList());

// Different DTO types for different contexts
UserDTO publicDto = DTOMapperFactory.createPublicUserDTO(user);
UserDTO adminDto = DTOMapperFactory.createAdminUserDTO(user);
```

### Real Examples from [`UserServiceImpl`](src/main/java/com/trainreservation/auth/service/UserServiceImpl.java):

#### 1. User Registration:
```java
@Override
public UserDTO registerUser(UserDTO userDTO) {
    User user = new User();
    // ... set user properties ...
    User savedUser = userRepository.save(user);
    return DTOMapperFactory.createUserDTO(savedUser);  // ✅ Factory Pattern
}
```

#### 2. Get All Users:
```java
@Override
public List<UserDTO> getAllUsers() {
    return userRepository.findAll().stream()
            .map(DTOMapperFactory::createUserDTO)  // ✅ Method reference
            .collect(Collectors.toList());
}
```

#### 3. Role-Based Filtering:
```java
@Override
public List<UserDTO> getUsersByRole(UserRole role) {
    return userRepository.findByRole(role).stream()
            .map(DTOMapperFactory::createUserDTO)  // ✅ Clean & concise
            .collect(Collectors.toList());
}
```

---

## 📊 Benefits & Comparison

### Advantages of Factory Pattern:

| Benefit | Description | Impact |
|---------|-------------|--------|
| **Centralization** | One place for all DTO creation logic | Easier maintenance |
| **Flexibility** | Easy to add new DTO types | Future-proof |
| **Consistency** | All DTOs created uniformly | Fewer bugs |
| **Testability** | Factory methods are easy to test | Better quality |
| **Clean Code** | Removes repetitive conversion code | Improved readability |

### Design Pattern Comparison:

| Aspect | Singleton Pattern | Factory Pattern |
|--------|-------------------|-----------------|
| **Purpose** | Ensure single instance | Create object variations |
| **Instances** | ONE instance only | Multiple objects |
| **Use Case** | Shared resources (PasswordEncoder) | Object creation (DTOs) |
| **Flexibility** | Limited (one type) | High (many types) |
| **Example** | `PasswordEncoderSingleton` | `DTOMapperFactory` |

---

## 🎯 Best Practices

### ✅ DO:
1. **Use static factory methods** for stateless operations
2. **Add descriptive method names** (`createPublicUserDTO` not `createDTO2`)
3. **Handle null safely** (check if input is null)
4. **Document each factory method** with purpose and use case
5. **Follow naming conventions** (`create*`, `make*`, `build*`)

### ❌ DON'T:
1. **Mix responsibilities** - Keep factories focused on creation
2. **Make instances** - Use static methods for utility factories
3. **Skip null checks** - Always validate input
4. **Forget documentation** - Explain when to use each method
5. **Hardcode values** - Keep factories configurable

---

## 🔮 Future Enhancements

The Factory Pattern makes these extensions easy:

### 1. Add Timestamp Support:
```java
public static UserDTO createDetailedUserDTO(User user) {
    UserDTO dto = createUserDTO(user);
    dto.setCreatedAt(user.getCreatedAt());
    dto.setUpdatedAt(user.getUpdatedAt());
    return dto;
}
```

### 2. Role-Specific DTOs:
```java
public static PassengerDTO createPassengerDTO(User user) {
    // Include passenger-specific fields
}

public static StaffDTO createStaffDTO(User user) {
    // Include staff-specific fields
}
```

### 3. API Versioning:
```java
public static UserDTOV1 createUserDTOV1(User user) { }
public static UserDTOV2 createUserDTOV2(User user) { }
```

---

## 🧪 Testing Factory Pattern

### Unit Test Example:
```java
@Test
public void testCreateUserDTO() {
    // Arrange
    User user = new User();
    user.setId(1L);
    user.setUsername("testuser");
    user.setEmail("test@example.com");
    user.setRole(UserRole.PASSENGER);
    user.setActive(true);
    
    // Act
    UserDTO dto = DTOMapperFactory.createUserDTO(user);
    
    // Assert
    assertNotNull(dto);
    assertEquals(1L, dto.getId());
    assertEquals("testuser", dto.getUsername());
    assertEquals("test@example.com", dto.getEmail());
    assertNull(dto.getPassword()); // Should NOT include password
}

@Test
public void testCreatePublicUserDTO() {
    // Public DTO should hide sensitive information
    User user = new User();
    user.setEmail("sensitive@example.com");
    
    UserDTO publicDto = DTOMapperFactory.createPublicUserDTO(user);
    
    assertNull(publicDto.getEmail()); // Email hidden in public view
}
```

---

## 📚 Related Documentation

- [`SINGLETON_PATTERN.md`](SINGLETON_PATTERN.md) - Singleton Pattern implementation
- [`SINGLETON_COMPLETE_GUIDE.md`](SINGLETON_COMPLETE_GUIDE.md) - Comprehensive Singleton guide
- [`SECURITY_IMPLEMENTATION.md`](SECURITY_IMPLEMENTATION.md) - Security architecture
- [`README.md`](README.md) - Project overview

---

## 🎓 Summary

### Combined Pattern Strategy:

```
┌─────────────────────────────────────────────┐
│         Design Pattern Architecture         │
├─────────────────────────────────────────────┤
│                                             │
│  Singleton Pattern                          │
│  └─ PasswordEncoderSingleton                │
│     └─ ONE instance of BCryptPasswordEncoder│
│                                             │
│  Factory Pattern                            │
│  └─ DTOMapperFactory                        │
│     ├─ createUserDTO()                      │
│     ├─ createDetailedUserDTO()              │
│     ├─ createPublicUserDTO()                │
│     └─ createAdminUserDTO()                 │
│                                             │
└─────────────────────────────────────────────┘
```

**Key Takeaway**: Use **Singleton** for shared resources and **Factory** for object creation flexibility!

---

## 💬 Questions?

If you have questions about the Factory Pattern implementation:
1. Review the code in [`DTOMapperFactory.java`](src/main/java/com/trainreservation/auth/util/DTOMapperFactory.java)
2. Check usage in [`UserServiceImpl.java`](src/main/java/com/trainreservation/auth/service/UserServiceImpl.java)
3. Compare with [`PasswordEncoderSingleton.java`](src/main/java/com/trainreservation/auth/util/PasswordEncoderSingleton.java)

---

**Last Updated**: 2025-10-24  
**Pattern Type**: Creational Design Pattern  
**Implementation Status**: ✅ Complete
