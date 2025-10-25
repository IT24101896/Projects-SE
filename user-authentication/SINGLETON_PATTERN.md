# 🎯 Singleton Design Pattern Usage

## 📍 Location in Project

The **Singleton Design Pattern** is implemented in your codebase for the `PasswordEncoder` functionality.

---

## 📁 File Location

**Path**: `src/main/java/com/trainreservation/auth/util/PasswordEncoderSingleton.java`

**Full Path**: 
```
c:\Users\Jithmi Abewickrama\Desktop\2nd Year 1st Sem\SE\SE Lab 7 Project\user-authentication\src\main\java\com\trainreservation\auth\util\PasswordEncoderSingleton.java
```

---

## 💻 Complete Code Implementation

```java
package com.trainreservation.auth.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderSingleton {

    // 1. Private static instance variable
    private static PasswordEncoder instance;

    // 2. Private constructor to prevent instantiation
    private PasswordEncoderSingleton() {
        // Private constructor to prevent instantiation
    }

    // 3. Public static method to get the single instance
    public static synchronized PasswordEncoder getInstance() {
        if (instance == null) {
            instance = new BCryptPasswordEncoder();
        }
        return instance;
    }
}
```

---

## 🔍 Singleton Pattern Components Explained

### 1️⃣ **Private Static Instance**
```java
private static PasswordEncoder instance;
```
- **Purpose**: Holds the single instance of the PasswordEncoder
- **Visibility**: `private` - Cannot be accessed directly from outside
- **Scope**: `static` - Belongs to the class, not individual objects

---

### 2️⃣ **Private Constructor**
```java
private PasswordEncoderSingleton() {
    // Private constructor to prevent instantiation
}
```
- **Purpose**: Prevents creation of instances using `new` keyword
- **Visibility**: `private` - Cannot be called from outside the class
- **Effect**: Makes it impossible to do: `new PasswordEncoderSingleton()` ❌

---

### 3️⃣ **Public Static getInstance() Method**
```java
public static synchronized PasswordEncoder getInstance() {
    if (instance == null) {
        instance = new BCryptPasswordEncoder();
    }
    return instance;
}
```
- **Purpose**: Provides global access point to the single instance
- **Lazy Initialization**: Creates instance only when first requested
- **Thread-Safe**: `synchronized` keyword prevents multiple threads from creating multiple instances
- **Returns**: The same `PasswordEncoder` instance every time

---

## 📍 Usage in the Application

### **Where It's Used**: `UserServiceImpl.java`

**Path**: `src/main/java/com/trainreservation/auth/service/UserServiceImpl.java`

**Code**:
```java
package com.trainreservation.auth.service;

import com.trainreservation.auth.util.PasswordEncoderSingleton;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    
    // ⭐ SINGLETON USAGE HERE ⭐
    private final PasswordEncoder passwordEncoder = PasswordEncoderSingleton.getInstance();

    @Override
    public UserDTO registerUser(UserDTO userDTO) {
        // ... code ...
        
        // Using the singleton instance to encode password
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        
        // ... code ...
    }

    @Override
    public UserDTO loginUser(LoginRequest loginRequest) {
        // ... code ...
        
        // Using the singleton instance to verify password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        
        // ... code ...
    }

    @Override
    public boolean changePassword(Long userId, PasswordChangeRequest request) {
        // ... code ...
        
        // Using the singleton instance to verify current password
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        // Using the singleton instance to encode new password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        
        // ... code ...
    }

    @Override
    public boolean resetPassword(PasswordResetRequest request) {
        // ... code ...
        
        // Using the singleton instance to encode new password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        
        // ... code ...
    }
}
```

---

## 🎯 Why Singleton Pattern is Used Here?

### **Purpose**: Ensure only ONE instance of PasswordEncoder exists

### **Benefits**:

1. **Memory Efficiency** 🧠
   - Only ONE `BCryptPasswordEncoder` object is created
   - Shared across all password operations
   - Saves memory by avoiding multiple instances

2. **Performance** ⚡
   - `BCryptPasswordEncoder` initialization is expensive
   - Creating it once and reusing is faster
   - Reduces overhead in password operations

3. **Consistency** 🎯
   - All password encoding uses the SAME configuration
   - Ensures passwords are encrypted uniformly
   - Prevents configuration mismatches

4. **Thread Safety** 🔒
   - `synchronized` keyword ensures thread-safe creation
   - Multiple threads get the SAME instance
   - No race conditions during initialization

---

## 🔄 How It Works (Step-by-Step)

### **First Call**:
```java
PasswordEncoder encoder = PasswordEncoderSingleton.getInstance();
```

**Execution Flow**:
```
1. getInstance() called
2. Check: instance == null? → YES
3. Create: instance = new BCryptPasswordEncoder()
4. Return: instance
```

### **Second Call** (from different class):
```java
PasswordEncoder encoder = PasswordEncoderSingleton.getInstance();
```

**Execution Flow**:
```
1. getInstance() called
2. Check: instance == null? → NO (already created)
3. Skip creation
4. Return: same instance (from first call)
```

### **Result**:
```
Both calls receive THE SAME PasswordEncoder object!
encoder1 == encoder2 → TRUE ✅
```

---

## 🧪 Testing the Singleton Behavior

You can verify it's a singleton:

```java
public class SingletonTest {
    public static void main(String[] args) {
        PasswordEncoder encoder1 = PasswordEncoderSingleton.getInstance();
        PasswordEncoder encoder2 = PasswordEncoderSingleton.getInstance();
        
        // Both reference the SAME object
        System.out.println(encoder1 == encoder2); // true ✅
        System.out.println(encoder1.hashCode() == encoder2.hashCode()); // true ✅
    }
}
```

---

## 📊 Singleton Pattern Diagram

```
┌─────────────────────────────────────────────┐
│      PasswordEncoderSingleton Class         │
├─────────────────────────────────────────────┤
│  - instance: PasswordEncoder (static)       │
│                                             │
│  - PasswordEncoderSingleton() (private)     │
│  + getInstance(): PasswordEncoder (static)  │
└─────────────────────────────────────────────┘
                    ↓
        ┌───────────────────────┐
        │ Single Instance       │
        │ BCryptPasswordEncoder │
        └───────────────────────┘
                    ↑
        ┌───────────┴───────────┐
        ↓                       ↓
   UserServiceImpl         Other Services
   (uses instance)         (use same instance)
```

---

## 🆚 Comparison: Without vs With Singleton

### **WITHOUT Singleton** ❌
```java
public class UserServiceImpl {
    // Creates NEW instance every time
    private final PasswordEncoder encoder1 = new BCryptPasswordEncoder();
}

public class AnotherService {
    // Creates ANOTHER instance
    private final PasswordEncoder encoder2 = new BCryptPasswordEncoder();
}

// Result: Multiple instances, wasted memory
```

### **WITH Singleton** ✅
```java
public class UserServiceImpl {
    // Gets THE SAME instance
    private final PasswordEncoder encoder = PasswordEncoderSingleton.getInstance();
}

public class AnotherService {
    // Gets THE SAME instance
    private final PasswordEncoder encoder = PasswordEncoderSingleton.getInstance();
}

// Result: ONE instance, shared across all services
```

---

## 🎓 Key Singleton Characteristics in Your Code

### ✅ **Lazy Initialization**
- Instance created only when first requested
- Not created at class loading time
- Saves resources if never used

### ✅ **Thread-Safe**
- `synchronized` keyword on `getInstance()`
- Prevents multiple threads creating multiple instances
- Ensures thread safety in multi-threaded environment

### ✅ **Global Access Point**
- Static method `getInstance()` accessible from anywhere
- No need to pass encoder around as parameter
- Centralized access to the instance

### ✅ **Controlled Instantiation**
- Private constructor prevents external instantiation
- Class controls its own instance creation
- Impossible to create multiple instances

---

## 📝 Usage Examples in Your Application

### **1. User Registration** (Line 34 in UserServiceImpl.java)
```java
user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
```
**Purpose**: Encrypt user's password before storing

---

### **2. User Login** (Line 51 in UserServiceImpl.java)
```java
if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
    throw new RuntimeException("Invalid password");
}
```
**Purpose**: Verify entered password matches stored encrypted password

---

### **3. Change Password** (Line 101 in UserServiceImpl.java)
```java
// Verify current password
if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
    throw new RuntimeException("Current password is incorrect");
}

// Encode new password
user.setPassword(passwordEncoder.encode(request.getNewPassword()));
```
**Purpose**: Verify old password and encrypt new password

---

### **4. Reset Password** (Line 183 in UserServiceImpl.java)
```java
user.setPassword(passwordEncoder.encode(request.getNewPassword()));
```
**Purpose**: Encrypt new password during reset

---

## 🎯 Summary

### **Pattern**: Singleton Design Pattern
### **Location**: `src/main/java/com/trainreservation/auth/util/PasswordEncoderSingleton.java`
### **Usage**: `src/main/java/com/trainreservation/auth/service/UserServiceImpl.java`

### **Key Features**:
✅ **One instance** of PasswordEncoder for entire application  
✅ **Thread-safe** implementation with synchronized method  
✅ **Lazy initialization** - created only when needed  
✅ **Memory efficient** - shared across all services  
✅ **Consistent** password encryption throughout app  

### **Benefits**:
- 🧠 Saves memory
- ⚡ Better performance
- 🔒 Thread-safe
- 🎯 Consistent encryption
- 📦 Easy to maintain

**This is a perfect example of the Singleton Design Pattern in action!** 🎉
