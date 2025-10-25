# Design Patterns Comparison: Singleton vs Factory

## 🎯 Quick Decision Guide

**"Should I use Singleton or Factory for my component?"**

Use this decision tree:

```
Do you need ONLY ONE instance globally?
│
├─ YES → Do different components need different configurations?
│   │
│   ├─ NO  → ✅ Use SINGLETON Pattern
│   │        Example: PasswordEncoder, Database Connection Pool
│   │
│   └─ YES → ❌ Don't use Singleton, consider Factory or Dependency Injection
│
└─ NO  → Do you need to create multiple object variations?
    │
    ├─ YES → ✅ Use FACTORY Pattern
    │        Example: DTO creation, User object creation
    │
    └─ NO  → Use standard constructors or Builder pattern
```

---

## 📊 Side-by-Side Comparison

| Criteria | Singleton Pattern | Factory Pattern |
|----------|-------------------|-----------------|
| **Purpose** | Control instance creation to ONE | Encapsulate object creation logic |
| **Instances Created** | Single instance | Multiple instances |
| **Flexibility** | Low (fixed type) | High (multiple types) |
| **Memory Usage** | Very efficient (one instance) | Moderate (multiple instances) |
| **Thread Safety** | Requires explicit handling | Usually not needed |
| **Extensibility** | Difficult to extend | Easy to add new types |
| **Testing** | Can be challenging | Easy to test |
| **Use Cases** | Shared resources, utilities | Object creation variations |

---

## 🏗️ Our Implementation

### 1️⃣ Singleton Pattern: `PasswordEncoderSingleton`

**File**: [`src/main/java/com/trainreservation/auth/util/PasswordEncoderSingleton.java`](src/main/java/com/trainreservation/auth/util/PasswordEncoderSingleton.java)

```java
public class PasswordEncoderSingleton {
    private static PasswordEncoder instance;
    
    private PasswordEncoderSingleton() { }  // Private constructor
    
    public static synchronized PasswordEncoder getInstance() {
        if (instance == null) {
            instance = new BCryptPasswordEncoder();
        }
        return instance;
    }
}
```

**Why Singleton?**
- ✅ Only ONE BCryptPasswordEncoder needed globally
- ✅ Stateless - no configuration differences needed
- ✅ Memory efficient - shared across all services
- ✅ Thread-safe with synchronized

**Used in**: [`UserServiceImpl`](src/main/java/com/trainreservation/auth/service/UserServiceImpl.java)
```java
private final PasswordEncoder passwordEncoder = PasswordEncoderSingleton.getInstance();
```

---

### 2️⃣ Factory Pattern: `DTOMapperFactory`

**File**: [`src/main/java/com/trainreservation/auth/util/DTOMapperFactory.java`](src/main/java/com/trainreservation/auth/util/DTOMapperFactory.java)

```java
public class DTOMapperFactory {
    
    private DTOMapperFactory() { }  // Utility class - no instantiation
    
    public static UserDTO createUserDTO(User user) { }
    public static UserDTO createDetailedUserDTO(User user) { }
    public static UserDTO createPublicUserDTO(User user) { }
    public static UserDTO createAdminUserDTO(User user) { }
}
```

**Why Factory?**
- ✅ Multiple DTO variations needed (standard, public, admin)
- ✅ Different fields for different contexts
- ✅ Easy to extend with new DTO types
- ✅ Centralizes conversion logic

**Used in**: [`UserServiceImpl`](src/main/java/com/trainreservation/auth/service/UserServiceImpl.java)
```java
return DTOMapperFactory.createUserDTO(savedUser);
return DTOMapperFactory.createPublicUserDTO(user);  // Future use
```

---

## 🎨 Pattern Characteristics

### Singleton Pattern

**Structure**:
```
┌─────────────────────────────────┐
│   PasswordEncoderSingleton      │
├─────────────────────────────────┤
│ - instance: PasswordEncoder     │ ← Single instance
│ - PasswordEncoderSingleton()    │ ← Private constructor
│ + getInstance(): PasswordEncoder│ ← Public accessor
└─────────────────────────────────┘
         ↓
    BCryptPasswordEncoder
    (ONE instance shared globally)
```

**Key Components**:
1. **Private static instance** - Holds the single instance
2. **Private constructor** - Prevents external instantiation
3. **Public static getInstance()** - Provides global access point
4. **Lazy initialization** - Creates instance on first use
5. **Thread safety** - Synchronized to prevent race conditions

---

### Factory Pattern

**Structure**:
```
┌─────────────────────────────────┐
│      DTOMapperFactory           │
├─────────────────────────────────┤
│ + createUserDTO()               │ ← Standard DTO
│ + createDetailedUserDTO()       │ ← Detailed info
│ + createPublicUserDTO()         │ ← Public-safe
│ + createAdminUserDTO()          │ ← Admin view
└─────────────────────────────────┘
         ↓
    Multiple UserDTO objects
    (Different configurations)
```

**Key Components**:
1. **Static factory methods** - Create different object types
2. **Private constructor** - Utility class (no instances)
3. **Encapsulated logic** - Hides creation complexity
4. **Flexibility** - Easy to add new creation methods
5. **Null safety** - Handles edge cases

---

## 💼 Real-World Analogies

### Singleton = Government Capital City
- **ONE** capital city per country
- Shared by all citizens
- Centralized authority
- Example: `PasswordEncoder` - one encoder for entire app

### Factory = Car Manufacturing Plant
- **MULTIPLE** cars of different models
- Same assembly line, different outputs
- Flexible production
- Example: `DTOMapperFactory` - many DTOs from same process

---

## 📈 When to Use Each

### Use Singleton When:
- ✅ You need exactly ONE instance
- ✅ Instance should be shared globally
- ✅ Object is stateless or has shared state
- ✅ Examples:
  - Configuration managers
  - Logger instances
  - Database connection pools
  - **Our case**: Password encoder

### Use Factory When:
- ✅ You need multiple object variations
- ✅ Creation logic is complex
- ✅ You want to hide implementation details
- ✅ Examples:
  - Document creators (PDF, Word, Excel)
  - UI component generators
  - Database query builders
  - **Our case**: DTO mappers

### Don't Use Either When:
- ❌ Simple objects with straightforward creation
- ❌ Need full dependency injection control
- ❌ Object lifecycle managed by framework (Spring)
- Use: Standard constructors or DI instead

---

## ⚡ Performance Comparison

| Aspect | Singleton | Factory |
|--------|-----------|---------|
| **Memory** | ⭐⭐⭐⭐⭐ (Very low) | ⭐⭐⭐ (Moderate) |
| **Speed** | ⭐⭐⭐⭐⭐ (First call slower) | ⭐⭐⭐⭐ (Consistent) |
| **Scalability** | ⭐⭐⭐ (Limited) | ⭐⭐⭐⭐⭐ (High) |
| **Maintainability** | ⭐⭐⭐ (Can be tricky) | ⭐⭐⭐⭐⭐ (Excellent) |
| **Testability** | ⭐⭐ (Challenging) | ⭐⭐⭐⭐⭐ (Easy) |

---

## 🧪 Testing Considerations

### Singleton Testing Challenges:
```java
// Problem: Hard to mock or reset between tests
@Test
public void testPasswordEncoding() {
    PasswordEncoder encoder = PasswordEncoderSingleton.getInstance();
    // Same instance used across all tests - can cause issues
}
```

**Solution**: Use dependency injection in Spring:
```java
@Autowired
private PasswordEncoder passwordEncoder;  // Spring manages it
```

### Factory Testing (Easy):
```java
@Test
public void testDTOCreation() {
    User user = new User();
    user.setUsername("test");
    
    // Pure function - easy to test
    UserDTO dto = DTOMapperFactory.createUserDTO(user);
    
    assertEquals("test", dto.getUsername());
}
```

---

## 🔄 Combined Pattern Strategy

**Our project uses BOTH patterns strategically:**

```
Application Layer
│
├── Password Encoding (SINGLETON)
│   └── One shared BCryptPasswordEncoder
│       ├── Used by: UserServiceImpl
│       ├── Used by: SecurityConfig (potential)
│       └── Memory efficient, thread-safe
│
└── DTO Mapping (FACTORY)
    └── Multiple UserDTO variations
        ├── Standard DTOs for general use
        ├── Public DTOs for APIs
        ├── Admin DTOs for management
        └── Flexible, extensible, testable
```

**This combination provides**:
- 🎯 **Efficiency** via Singleton (shared resources)
- 🎨 **Flexibility** via Factory (object variations)
- 🛡️ **Best of both worlds**

---

## 📝 Code Examples

### Example 1: User Registration Flow
```java
@Service
public class UserServiceImpl implements UserService {
    
    // Singleton: ONE shared encoder
    private final PasswordEncoder passwordEncoder = 
        PasswordEncoderSingleton.getInstance();
    
    @Override
    public UserDTO registerUser(UserDTO userDTO) {
        User user = new User();
        
        // Use singleton for encoding
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        
        User savedUser = userRepository.save(user);
        
        // Use factory for DTO creation
        return DTOMapperFactory.createUserDTO(savedUser);
    }
}
```

### Example 2: Different DTOs for Different Roles
```java
// Admin gets full details
public UserDTO getAdminView(Long userId) {
    User user = userRepository.findById(userId).orElseThrow();
    return DTOMapperFactory.createAdminUserDTO(user);
}

// Public API gets limited info
public UserDTO getPublicProfile(Long userId) {
    User user = userRepository.findById(userId).orElseThrow();
    return DTOMapperFactory.createPublicUserDTO(user);
}
```

---

## 🎓 Learning Outcomes

After implementing both patterns, you should understand:

1. **Pattern Selection**: How to choose the right pattern
2. **Trade-offs**: Pros and cons of each approach
3. **Combination**: Using multiple patterns together
4. **Best Practices**: Industry-standard implementations
5. **Real-world Application**: Practical usage in production code

---

## 📚 Further Reading

- [Singleton Pattern Complete Guide](SINGLETON_COMPLETE_GUIDE.md)
- [Factory Pattern Guide](FACTORY_PATTERN_GUIDE.md)
- [Gang of Four Design Patterns](https://en.wikipedia.org/wiki/Design_Patterns)
- [Effective Java by Joshua Bloch](https://www.oreilly.com/library/view/effective-java/9780134686097/)

---

## 🎯 Summary Checklist

Before choosing a pattern, ask yourself:

- [ ] Do I need exactly ONE instance? → Consider **Singleton**
- [ ] Do I need MULTIPLE variations? → Consider **Factory**
- [ ] Is the object stateless/shared? → Consider **Singleton**
- [ ] Is creation logic complex? → Consider **Factory**
- [ ] Do I need easy testing? → Consider **Factory**
- [ ] Is memory efficiency critical? → Consider **Singleton**

**Our Project Uses**:
- ✅ **Singleton** for `PasswordEncoder` (one instance, shared globally)
- ✅ **Factory** for `DTOMapper` (multiple DTOs, flexible creation)

---

**Last Updated**: 2025-10-24  
**Author**: SE Lab 7 Project Team  
**Status**: ✅ Production Ready
