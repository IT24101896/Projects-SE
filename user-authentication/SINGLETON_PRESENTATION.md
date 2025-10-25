# 🎯 Singleton Design Pattern - Lecturer Presentation Guide

## 📌 Quick Answer for Lecturer

**Question**: "What design pattern did you use in your code?"

**Your Answer**: 
> "I used the **Singleton Design Pattern** to manage the Password Encoder in my application. This ensures that only ONE instance of the BCryptPasswordEncoder exists throughout the entire application lifecycle, which improves memory efficiency and performance."

---

## 📍 WHERE IS IT LOCATED?

### **1. Singleton Implementation Class**

**File Path**: 
```
src/main/java/com/trainreservation/auth/util/PasswordEncoderSingleton.java
```

**Full Absolute Path**:
```
c:\Users\Jithmi Abewickrama\Desktop\2nd Year 1st Sem\SE\SE Lab 7 Project\user-authentication\src\main\java\com\trainreservation\auth\util\PasswordEncoderSingleton.java
```

### **2. Singleton Usage Class**

**File Path**:
```
src/main/java/com/trainreservation/auth/service/UserServiceImpl.java
```

**Full Absolute Path**:
```
c:\Users\Jithmi Abewickrama\Desktop\2nd Year 1st Sem\SE\SE Lab 7 Project\user-authentication\src\main\java\com\trainreservation\auth\service\UserServiceImpl.java
```

---

## 💻 THE COMPLETE CODE TO SHOW LECTURER

### **Step 1: Show the Singleton Class**

Open: `PasswordEncoderSingleton.java`

```java
package com.trainreservation.auth.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderSingleton {

    // Component 1: Private static instance variable
    private static PasswordEncoder instance;

    // Component 2: Private constructor prevents external instantiation
    private PasswordEncoderSingleton() {
        // Private constructor to prevent instantiation
    }

    // Component 3: Public static method to get the single instance
    public static synchronized PasswordEncoder getInstance() {
        if (instance == null) {
            instance = new BCryptPasswordEncoder();
        }
        return instance;
    }
}
```

### **Step 2: Show How It's Used**

Open: `UserServiceImpl.java` (Line 20)

```java
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    
    // ⭐ SINGLETON PATTERN USAGE HERE ⭐
    private final PasswordEncoder passwordEncoder = PasswordEncoderSingleton.getInstance();

    @Override
    public UserDTO registerUser(UserDTO userDTO) {
        // Using the singleton instance to encode password
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        // ... rest of code
    }

    @Override
    public UserDTO loginUser(LoginRequest loginRequest) {
        // Using the singleton instance to verify password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        // ... rest of code
    }

    @Override
    public boolean changePassword(Long userId, PasswordChangeRequest request) {
        // Using the singleton instance to verify current password
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }
        // Using the singleton instance to encode new password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        // ... rest of code
    }
}
```

---

## 🎓 HOW TO EXPLAIN TO LECTURER

### **1. What is Singleton Pattern?**

> "Singleton is a creational design pattern that ensures a class has only ONE instance and provides a global point of access to that instance."

### **2. Why Did You Use It?**

> "I used Singleton for the PasswordEncoder because:
> - **Memory Efficiency**: Instead of creating multiple PasswordEncoder objects, the entire application shares one instance
> - **Performance**: BCryptPasswordEncoder is expensive to create, so reusing one instance improves performance
> - **Consistency**: All password operations use the same encoder configuration
> - **Thread Safety**: The synchronized keyword ensures thread-safe instance creation"

### **3. The Three Key Components**

Point to each part in your code:

#### **Component 1: Private Static Instance**
```java
private static PasswordEncoder instance;
```
> "This holds the single instance. It's `private` so no one can access it directly, and `static` so it belongs to the class, not individual objects."

#### **Component 2: Private Constructor**
```java
private PasswordEncoderSingleton() {
    // Private constructor to prevent instantiation
}
```
> "This prevents anyone from creating new instances using `new PasswordEncoderSingleton()`. The constructor is private, so only this class can use it."

#### **Component 3: Public Static getInstance() Method**
```java
public static synchronized PasswordEncoder getInstance() {
    if (instance == null) {
        instance = new BCryptPasswordEncoder();
    }
    return instance;
}
```
> "This is the global access point. It checks if an instance exists:
> - If `null` (first call): Creates new instance
> - If exists: Returns existing instance
> The `synchronized` keyword makes it thread-safe."

---

## 🔄 HOW IT WORKS - VISUAL DEMONSTRATION

### **First Call**
```
UserServiceImpl calls getInstance()
    ↓
Is instance null? → YES
    ↓
Create new BCryptPasswordEncoder()
    ↓
Store in static instance variable
    ↓
Return the instance
```

### **Second Call (and all subsequent calls)**
```
Another service calls getInstance()
    ↓
Is instance null? → NO (already created)
    ↓
Return the SAME existing instance
```

### **Result**
```
Service 1 → getInstance() → Instance #1
Service 2 → getInstance() → Instance #1 (SAME)
Service 3 → getInstance() → Instance #1 (SAME)

Only ONE instance exists! ✅
```

---

## 🆚 WITH vs WITHOUT SINGLETON

### **WITHOUT Singleton** ❌

```java
public class UserServiceImpl {
    // Creates a NEW instance
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();
}

public class AnotherService {
    // Creates ANOTHER instance
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();
}

// Result: Multiple instances = Wasted memory
```

### **WITH Singleton** ✅

```java
public class UserServiceImpl {
    // Gets the SAME instance
    private final PasswordEncoder encoder = PasswordEncoderSingleton.getInstance();
}

public class AnotherService {
    // Gets the SAME instance
    private final PasswordEncoder encoder = PasswordEncoderSingleton.getInstance();
}

// Result: ONE shared instance = Efficient!
```

---

## 📊 CLASS DIAGRAM TO SHOW LECTURER

```
┌─────────────────────────────────────────────┐
│      PasswordEncoderSingleton               │
├─────────────────────────────────────────────┤
│  - instance: PasswordEncoder (static)       │
├─────────────────────────────────────────────┤
│  - PasswordEncoderSingleton()               │  ← Private Constructor
│  + getInstance(): PasswordEncoder (static)  │  ← Public Access Point
└─────────────────────────────────────────────┘
                    ↓ creates
        ┌───────────────────────┐
        │   BCryptPasswordEncoder  │  ← Single Instance
        │   (ONE instance only)    │
        └───────────────────────┘
                    ↑
        ┌───────────┴───────────┐
        ↓                       ↓
   UserServiceImpl         Other Services
   (uses instance)         (share same instance)
```

---

## 🎯 KEY BENEFITS TO MENTION

1. ✅ **Controlled Instance Creation**
   - Only one instance exists
   - No duplicate objects

2. ✅ **Memory Efficiency**
   - Single shared instance
   - Reduces memory footprint

3. ✅ **Performance**
   - BCryptPasswordEncoder is expensive to create
   - Reusing one instance is faster

4. ✅ **Thread Safety**
   - `synchronized` keyword
   - Safe in multi-threaded environment

5. ✅ **Global Access**
   - Available from anywhere
   - Consistent across application

6. ✅ **Lazy Initialization**
   - Instance created only when needed
   - Not created at startup

---

## 🧪 PROOF IT'S A SINGLETON (If Lecturer Asks)

You can demonstrate with this test:

```java
public class SingletonProof {
    public static void main(String[] args) {
        // Get instance twice
        PasswordEncoder encoder1 = PasswordEncoderSingleton.getInstance();
        PasswordEncoder encoder2 = PasswordEncoderSingleton.getInstance();
        
        // Compare references
        System.out.println(encoder1 == encoder2); // true ✅
        
        // Compare hash codes
        System.out.println(encoder1.hashCode() == encoder2.hashCode()); // true ✅
        
        // Both point to SAME object in memory!
    }
}
```

**Output**: Both will be `true`, proving only ONE instance exists!

---

## 📝 WHERE IT'S USED IN YOUR APPLICATION

The singleton PasswordEncoder is used in **5 different methods** in UserServiceImpl:

1. **registerUser()** - Line 35
   - Encodes new user's password
   ```java
   user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
   ```

2. **loginUser()** - Line 52
   - Verifies password during login
   ```java
   if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()))
   ```

3. **changePassword()** - Lines 100, 105
   - Verifies current password
   - Encodes new password
   ```java
   if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword()))
   user.setPassword(passwordEncoder.encode(request.getNewPassword()));
   ```

4. **resetPassword()** - Line 183
   - Encodes reset password
   ```java
   user.setPassword(passwordEncoder.encode(request.getNewPassword()));
   ```

**All these use the SAME instance!** 🎯

---

## 🎤 SAMPLE Q&A WITH LECTURER

### **Q: "What design pattern did you use?"**
**A**: "I used the Singleton Design Pattern for managing the PasswordEncoder in my application."

### **Q: "Where is it implemented?"**
**A**: "It's in the `PasswordEncoderSingleton` class located at `src/main/java/com/trainreservation/auth/util/PasswordEncoderSingleton.java`"

### **Q: "Why did you choose Singleton?"**
**A**: "Because BCryptPasswordEncoder is expensive to create and we need only one instance throughout the application. This improves memory efficiency and performance while ensuring consistency."

### **Q: "How does it work?"**
**A**: "It has three components:
1. A private static instance variable
2. A private constructor to prevent external instantiation
3. A public static getInstance() method that returns the single instance"

### **Q: "Is it thread-safe?"**
**A**: "Yes, I used the `synchronized` keyword on the getInstance() method to ensure thread safety in a multi-threaded environment."

### **Q: "Where is it used?"**
**A**: "It's used in UserServiceImpl for all password operations - registration, login, password changes, and password resets. All these operations share the same PasswordEncoder instance."

---

## 🚀 QUICK NAVIGATION FOR DEMO

### **To Show Singleton Implementation:**
1. Open IDE
2. Navigate to: `src/main/java/com/trainreservation/auth/util/`
3. Open: `PasswordEncoderSingleton.java`
4. Point out the three components

### **To Show Singleton Usage:**
1. Navigate to: `src/main/java/com/trainreservation/auth/service/`
2. Open: `UserServiceImpl.java`
3. Go to Line 20 (the getInstance() call)
4. Show examples in methods (lines 35, 52, 100, 105, 183)

---

## 📋 CHECKLIST FOR PRESENTATION

Before meeting with lecturer, verify you can:

- [ ] Open the PasswordEncoderSingleton.java file quickly
- [ ] Explain what Singleton pattern is
- [ ] Point to the three key components in code
- [ ] Explain why you chose Singleton
- [ ] Show where it's used in UserServiceImpl
- [ ] Explain how getInstance() works
- [ ] Describe the benefits (memory, performance, thread-safety)
- [ ] Answer: "Is it thread-safe?" (Yes, synchronized)
- [ ] Answer: "How many instances exist?" (Only ONE)
- [ ] Draw the class diagram if needed

---

## 🎯 SUMMARY

**Pattern**: Singleton Design Pattern  
**Location**: `PasswordEncoderSingleton.java` + `UserServiceImpl.java`  
**Purpose**: Ensure only ONE PasswordEncoder instance exists  
**Benefits**: Memory efficient, better performance, thread-safe, consistent  
**Usage**: All password operations throughout the application  

**Key Point**: The entire application uses ONE shared PasswordEncoder instance! 🎯

---

## 💡 PRO TIP

If lecturer asks: "Could you use other patterns?"

**Answer**: "Yes, but Singleton is ideal here because:
- **Factory Pattern**: Would create new instances (not efficient for PasswordEncoder)
- **Dependency Injection**: Spring does this, but Singleton ensures single instance
- **Singleton**: Perfect for expensive-to-create, stateless, reusable objects like PasswordEncoder"

---

**Good Luck with Your Presentation! 🎓**
