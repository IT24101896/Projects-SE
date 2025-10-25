# Design Patterns Visual Guide

## 🎨 Visual Representations

---

## 1️⃣ Singleton Pattern Architecture

### Class Diagram
```
┌──────────────────────────────────────┐
│   PasswordEncoderSingleton          │
├──────────────────────────────────────┤
│ - instance: PasswordEncoder [static]│ ← Single instance
├──────────────────────────────────────┤
│ - PasswordEncoderSingleton()        │ ← Private constructor
│ + getInstance(): PasswordEncoder    │ ← Public accessor
│   [static, synchronized]             │
└──────────────────────────────────────┘
           ↓ creates
┌──────────────────────────────────────┐
│    BCryptPasswordEncoder             │
│    (ONE instance only)               │
└──────────────────────────────────────┘
```

### Flow Diagram
```
Thread 1 calls getInstance()
    ↓
Check: instance == null?
    ↓
   YES → Create new BCryptPasswordEncoder
    ↓         (synchronized - thread safe)
   NO  → Return existing instance
    ↓
Return instance to Thread 1

Thread 2 calls getInstance()
    ↓
Check: instance == null?
    ↓
   NO  → Return SAME instance
    ↓
Return instance to Thread 2

Result: Thread 1 and Thread 2 use SAME encoder instance ✅
```

---

## 2️⃣ Factory Pattern Architecture

### Class Diagram
```
┌──────────────────────────────────────┐
│      DTOMapperFactory                │
├──────────────────────────────────────┤
│ - DTOMapperFactory()                 │ ← Private constructor
│                                      │    (Utility class)
├──────────────────────────────────────┤
│ + createUserDTO(User): UserDTO      │ ← Standard
│ + createDetailedUserDTO(User):      │ ← Detailed
│     UserDTO                          │
│ + createPublicUserDTO(User):        │ ← Public-safe
│     UserDTO                          │
│ + createAdminUserDTO(User):         │ ← Admin view
│     UserDTO                          │
└──────────────────────────────────────┘
           ↓ creates
┌──────────────────────────────────────┐
│         UserDTO Objects              │
├──────────────────────────────────────┤
│ • Standard DTO (all fields)          │
│ • Public DTO (limited fields)        │
│ • Admin DTO (full details)           │
│ • Detailed DTO (with timestamps)     │
└──────────────────────────────────────┘
```

### Flow Diagram
```
User Entity
    ↓
DTOMapperFactory.createUserDTO(user)
    ↓
┌─────────────────────────────────────┐
│  Factory Method Logic:              │
│  1. Check if user is null           │
│  2. Create new UserDTO()            │
│  3. Set id, username, email         │
│  4. Set role, active status         │
│  5. Exclude password (security)     │
│  6. Return populated DTO            │
└─────────────────────────────────────┘
    ↓
UserDTO (Safe to send to frontend)
```

---

## 3️⃣ Combined Pattern Usage

### System Architecture
```
┌─────────────────────────────────────────────────────┐
│              UserServiceImpl                        │
│                                                     │
│  ┌─────────────────────────────────────────────┐  │
│  │  Password Encoding (SINGLETON)              │  │
│  │                                             │  │
│  │  passwordEncoder =                          │  │
│  │    PasswordEncoderSingleton.getInstance()   │  │
│  │                                             │  │
│  │  encode() ────────┐                         │  │
│  │  matches() ───────┤→ SAME instance          │  │
│  │                   │                         │  │
│  └───────────────────┼─────────────────────────┘  │
│                      │                            │
│  ┌─────────────────────────────────────────────┐  │
│  │  DTO Mapping (FACTORY)                      │  │
│  │                                             │  │
│  │  DTOMapperFactory.createUserDTO(user1) ──┐  │  │
│  │  DTOMapperFactory.createUserDTO(user2) ──┼─→│  │
│  │  DTOMapperFactory.createPublicDTO(user3) ┘  │  │
│  │                                             │  │
│  │  MULTIPLE DTOs created                      │  │
│  └─────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────┘
```

### Data Flow Example: User Registration
```
Registration Request
    ↓
registerUser(UserDTO userDTO)
    ↓
┌─────────────────────────────────────┐
│ Create User entity                  │
│ user.setUsername(...)               │
│ user.setEmail(...)                  │
└─────────────────────────────────────┘
    ↓
┌─────────────────────────────────────┐
│ SINGLETON: Encode password          │
│ encoder = PasswordEncoder           │
│   Singleton.getInstance()           │
│ user.setPassword(                   │
│   encoder.encode(password)          │
│ )                                   │
└─────────────────────────────────────┘
    ↓
Save to database
    ↓
┌─────────────────────────────────────┐
│ FACTORY: Convert to DTO             │
│ return DTOMapperFactory             │
│   .createUserDTO(savedUser)         │
└─────────────────────────────────────┘
    ↓
Return UserDTO to controller
    ↓
Send JSON response to frontend
```

---

## 4️⃣ Pattern Comparison

### Memory Usage
```
SINGLETON PATTERN
┌──────────────┐
│  Instance 1  │ ← Only ONE instance in memory
└──────────────┘
  Used by all:
  - Service 1
  - Service 2
  - Service 3
  
Memory: ⭐⭐⭐⭐⭐ (Very efficient)

FACTORY PATTERN
┌──────────────┐ ┌──────────────┐ ┌──────────────┐
│   DTO 1      │ │   DTO 2      │ │   DTO 3      │
└──────────────┘ └──────────────┘ └──────────────┘
  Request 1      Request 2        Request 3

Memory: ⭐⭐⭐ (Multiple objects)
```

### Flexibility
```
SINGLETON PATTERN
┌──────────────────────┐
│  Fixed type:         │
│  BCryptPassword      │
│  Encoder             │
└──────────────────────┘
Flexibility: ⭐⭐ (Limited to one type)

FACTORY PATTERN
┌──────────────────────┐
│  Multiple types:     │
│  • Standard DTO      │
│  • Public DTO        │
│  • Admin DTO         │
│  • Detailed DTO      │
│  • (Easy to add)     │
└──────────────────────┘
Flexibility: ⭐⭐⭐⭐⭐ (Highly flexible)
```

---

## 5️⃣ Thread Safety Visualization

### Singleton Thread Safety
```
Time ─────────────────────────────────────────→

Thread 1:  ──→ getInstance() ──┐
                                ├─→ synchronized ─→ create instance ─→ return
Thread 2:  ────────→ getInstance() ┘
                                    (waits)
                                    
Thread 3:  ──────────────────→ getInstance() ─→ return existing
```

### Factory (No Thread Safety Needed)
```
Thread 1:  ──→ createUserDTO(user1) ─→ return DTO1

Thread 2:  ──→ createUserDTO(user2) ─→ return DTO2
                  (parallel execution)
Thread 3:  ──→ createUserDTO(user3) ─→ return DTO3

Each call is independent - no shared state
```

---

## 6️⃣ Usage Patterns in Code

### Singleton Usage Pattern
```java
// STEP 1: Get the singleton instance (once per service)
private final PasswordEncoder passwordEncoder = 
    PasswordEncoderSingleton.getInstance();

// STEP 2: Use it multiple times (same instance)
┌────────────────────────────────────┐
│ registerUser()                     │
│   encoder.encode(password)  ────┐  │
│                                 │  │
│ loginUser()                     │  │
│   encoder.matches(pwd, hash) ───┤→ Same encoder instance
│                                 │  │
│ changePassword()                │  │
│   encoder.encode(newPwd)  ──────┘  │
└────────────────────────────────────┘
```

### Factory Usage Pattern
```java
// Different calls, different objects
┌────────────────────────────────────┐
│ registerUser()                     │
│   return DTOMapperFactory          │
│     .createUserDTO(user)  ──→ DTO1 │
│                                    │
│ getAllUsers()                      │
│   users.stream()                   │
│     .map(DTOMapperFactory          │
│       ::createUserDTO)  ──→ DTOs   │
│                                    │
│ getPublicProfile()                 │
│   return DTOMapperFactory          │
│     .createPublicDTO(user) ──→ DTO2│
└────────────────────────────────────┘
```

---

## 7️⃣ Evolution Timeline

### Before Patterns
```
UserServiceImpl
├── convertToDTO() ──┐
├── convertToDTO() ──┤ Duplicate code
├── convertToDTO() ──┤ in multiple places
└── convertToDTO() ──┘

Issues:
❌ Code duplication
❌ Hard to maintain
❌ Difficult to extend
```

### After Singleton
```
PasswordEncoderSingleton
└── getInstance() → BCryptPasswordEncoder

Benefits:
✅ Single instance
✅ Memory efficient
✅ Thread-safe
```

### After Factory
```
DTOMapperFactory
├── createUserDTO()
├── createDetailedUserDTO()
├── createPublicUserDTO()
└── createAdminUserDTO()

Benefits:
✅ Centralized logic
✅ Easy to extend
✅ Clean code
```

---

## 8️⃣ Design Decision Tree

```
                    New Component Needed
                           │
                           ↓
            ┌──────────────┴──────────────┐
            │                             │
    Need ONE instance?           Need MULTIPLE variations?
            │                             │
            ↓                             ↓
    ┌───────────────┐           ┌────────────────┐
    │   SINGLETON   │           │    FACTORY     │
    │               │           │                │
    │ • Shared      │           │ • Different    │
    │   resource    │           │   types        │
    │ • Stateless   │           │ • Complex      │
    │ • Global      │           │   creation     │
    │   access      │           │ • Flexible     │
    └───────────────┘           └────────────────┘
            │                             │
            ↓                             ↓
    PasswordEncoder              DTOMapperFactory
```

---

## 9️⃣ Future Enhancement Visualization

### Easy to Add with Factory
```
Current State:
DTOMapperFactory
├── createUserDTO()
├── createDetailedUserDTO()
├── createPublicUserDTO()
└── createAdminUserDTO()

Future Enhancement 1: Add Timestamps
├── createDetailedUserDTO() ← Enhanced
│   └── includes createdAt, updatedAt

Future Enhancement 2: Role-Specific DTOs
├── createPassengerDTO()  ← New
├── createStaffDTO()      ← New
└── createAdminDTO()      ← New

Future Enhancement 3: API Versioning
├── createUserDTOV1()     ← New
├── createUserDTOV2()     ← New
└── createUserDTOV3()     ← New

All without changing existing code! ✅
```

---

## 🎯 Quick Visual Reference

### Singleton = ONE
```
    ┌─────────┐
    │    1    │  ← Only ONE instance
    └─────────┘
```

### Factory = MANY
```
┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐
│  1  │ │  2  │ │  3  │ │  4  │  ← Many instances
└─────┘ └─────┘ └─────┘ └─────┘
```

---

## 📚 Related Documentation

- [Singleton Complete Guide](SINGLETON_COMPLETE_GUIDE.md)
- [Factory Pattern Guide](FACTORY_PATTERN_GUIDE.md)
- [Design Pattern Comparison](DESIGN_PATTERN_COMPARISON.md)
- [Quick Reference](DESIGN_PATTERNS_QUICK_REFERENCE.md)

---

**Visual Guide Version**: 1.0  
**Last Updated**: 2025-10-24
