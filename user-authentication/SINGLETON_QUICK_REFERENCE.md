# 🎯 Singleton Pattern - Quick Reference

## 📍 **WHERE IS IT?**

### **Implementation File**:
```
📁 Location: src/main/java/com/trainreservation/auth/util/
📄 File: PasswordEncoderSingleton.java
```

### **Usage File**:
```
📁 Location: src/main/java/com/trainreservation/auth/service/
📄 File: UserServiceImpl.java
📍 Line: 18
```

---

## 💻 **THE CODE**

### **Singleton Class** (PasswordEncoderSingleton.java):
```java
public class PasswordEncoderSingleton {
    // 1. Private static instance
    private static PasswordEncoder instance;

    // 2. Private constructor (prevents new PasswordEncoderSingleton())
    private PasswordEncoderSingleton() { }

    // 3. Public static getter (thread-safe)
    public static synchronized PasswordEncoder getInstance() {
        if (instance == null) {
            instance = new BCryptPasswordEncoder();
        }
        return instance;
    }
}
```

### **How It's Used** (UserServiceImpl.java):
```java
public class UserServiceImpl implements UserService {
    
    // Get the singleton instance
    private final PasswordEncoder passwordEncoder = PasswordEncoderSingleton.getInstance();
    
    // Use it throughout the class
    public UserDTO registerUser(UserDTO userDTO) {
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
    }
}
```

---

## 🎯 **WHY SINGLETON?**

### **Problem Without Singleton**:
```java
// ❌ Multiple instances created
Service1: new BCryptPasswordEncoder()  // Instance 1
Service2: new BCryptPasswordEncoder()  // Instance 2
Service3: new BCryptPasswordEncoder()  // Instance 3
```
**Result**: Wasted memory, slower performance

### **Solution With Singleton**:
```java
// ✅ ONE instance shared
Service1: getInstance() → Same Instance
Service2: getInstance() → Same Instance
Service3: getInstance() → Same Instance
```
**Result**: Memory efficient, faster

---

## 📊 **3 KEY COMPONENTS**

```
┌────────────────────────────────────────┐
│ 1. PRIVATE STATIC INSTANCE             │
│    private static PasswordEncoder      │
│    instance;                           │
├────────────────────────────────────────┤
│ 2. PRIVATE CONSTRUCTOR                 │
│    private PasswordEncoderSingleton()  │
│    { }                                 │
├────────────────────────────────────────┤
│ 3. PUBLIC STATIC GETTER                │
│    public static synchronized          │
│    PasswordEncoder getInstance() { }   │
└────────────────────────────────────────┘
```

---

## 🔄 **HOW IT WORKS**

```
First Call:
getInstance() → instance == null? YES
              → Create new BCryptPasswordEncoder()
              → Return instance

Second Call:
getInstance() → instance == null? NO
              → Return existing instance

Result: SAME object both times! ✅
```

---

## 📍 **USAGE LOCATIONS IN YOUR CODE**

### **Line 18** - Initialization:
```java
private final PasswordEncoder passwordEncoder = PasswordEncoderSingleton.getInstance();
```

### **Line 34** - Register User:
```java
user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
```

### **Line 51** - Login User:
```java
if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()))
```

### **Line 101** - Change Password:
```java
if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword()))
user.setPassword(passwordEncoder.encode(request.getNewPassword()));
```

### **Line 183** - Reset Password:
```java
user.setPassword(passwordEncoder.encode(request.getNewPassword()));
```

---

## ✅ **BENEFITS**

| Benefit | Description |
|---------|-------------|
| 🧠 **Memory Efficient** | Only ONE instance in memory |
| ⚡ **Better Performance** | No repeated expensive initialization |
| 🔒 **Thread-Safe** | `synchronized` prevents race conditions |
| 🎯 **Consistency** | All operations use same encoder |
| 📦 **Easy Maintenance** | Change config in ONE place |

---

## 🧪 **TEST IT**

```java
PasswordEncoder encoder1 = PasswordEncoderSingleton.getInstance();
PasswordEncoder encoder2 = PasswordEncoderSingleton.getInstance();

System.out.println(encoder1 == encoder2); // true ✅
```

---

## 📝 **SUMMARY**

**What**: Singleton Design Pattern for PasswordEncoder  
**Where**: `PasswordEncoderSingleton.java` + `UserServiceImpl.java`  
**Why**: One shared instance saves memory & ensures consistency  
**How**: Private constructor + static getInstance() method  

**Key Point**: Entire application uses ONE PasswordEncoder instance! 🎯
