# 🎯 Singleton Pattern - One-Page Cheat Sheet

## 📍 QUICK LOCATION

**Implementation**: `src/main/java/com/trainreservation/auth/util/PasswordEncoderSingleton.java`  
**Usage**: `src/main/java/com/trainreservation/auth/service/UserServiceImpl.java` (Line 20)

---

## 💬 QUICK ANSWER FOR LECTURER

**Q: "What design pattern did you use?"**

**A**: "I used the **Singleton Design Pattern** to ensure only ONE instance of PasswordEncoder exists throughout the application, improving memory efficiency and performance."

---

## 💻 THE CODE (Show This!)

### **Singleton Class**
```java
public class PasswordEncoderSingleton {
    // 1. Private static instance
    private static PasswordEncoder instance;

    // 2. Private constructor
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

### **How It's Used**
```java
public class UserServiceImpl {
    // Get singleton instance
    private final PasswordEncoder passwordEncoder = PasswordEncoderSingleton.getInstance();
    
    // Use in methods
    user.setPassword(passwordEncoder.encode(password));
}
```

---

## 🎯 3 KEY COMPONENTS

| Component | Code | Purpose |
|-----------|------|---------|
| **1. Private Static Instance** | `private static PasswordEncoder instance;` | Holds the single instance |
| **2. Private Constructor** | `private PasswordEncoderSingleton() { }` | Prevents external instantiation |
| **3. Public Static Getter** | `public static synchronized getInstance()` | Global access point |

---

## 🔄 HOW IT WORKS

```
First Call:  getInstance() → Create instance → Return it
Second Call: getInstance() → Instance exists → Return SAME instance
Result: ONE instance shared across entire application! ✅
```

---

## ✅ WHY SINGLETON?

1. **Memory Efficient** - One instance instead of many
2. **Better Performance** - BCryptPasswordEncoder is expensive to create
3. **Thread-Safe** - `synchronized` keyword
4. **Consistent** - All operations use same encoder
5. **Lazy Loading** - Created only when needed

---

## 📊 VISUAL DIAGRAM

```
PasswordEncoderSingleton
        ↓ creates
  BCryptPasswordEncoder (ONE instance)
        ↑ uses
UserServiceImpl & Other Services
```

---

## 🆚 WITH vs WITHOUT

**WITHOUT Singleton**: Each service creates own instance → Wastes memory  
**WITH Singleton**: All services share ONE instance → Efficient!

---

## 📝 WHERE IT'S USED (5 Places in UserServiceImpl)

1. `registerUser()` - Line 35 - Encode new password
2. `loginUser()` - Line 52 - Verify password
3. `changePassword()` - Lines 100, 105 - Verify & encode
4. `resetPassword()` - Line 183 - Encode reset password

**All use the SAME instance!**

---

## 🧪 PROOF IT'S SINGLETON

```java
PasswordEncoder encoder1 = PasswordEncoderSingleton.getInstance();
PasswordEncoder encoder2 = PasswordEncoderSingleton.getInstance();
System.out.println(encoder1 == encoder2); // true ✅ (SAME object!)
```

---

## 🎤 Q&A PREP

**Q: Is it thread-safe?**  
A: Yes, `synchronized` keyword ensures thread safety.

**Q: Why not create new instances?**  
A: BCryptPasswordEncoder is expensive; one instance is efficient.

**Q: How many instances exist?**  
A: Only ONE throughout the entire application lifecycle.

---

**Remember**: This is YOUR code, existing in the project! 🎓
