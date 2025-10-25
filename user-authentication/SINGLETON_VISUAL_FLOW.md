# 🎨 Singleton Pattern - Visual Flow Diagrams

## 📋 How to Use This Document

When your lecturer asks "How does Singleton work?", draw these diagrams on the board or show them on screen.

---

## 🔄 FLOW 1: How getInstance() Works

```
┌─────────────────────────────────────────────────────────┐
│  Someone calls: PasswordEncoderSingleton.getInstance()  │
└───────────────────────┬─────────────────────────────────┘
                        ↓
                 ┌──────────────┐
                 │ Is instance  │
                 │ null?        │
                 └──────┬───────┘
                        │
          ┌─────────────┴─────────────┐
          ↓ YES                       ↓ NO
    ┌──────────────┐          ┌──────────────┐
    │ Create new   │          │ Return       │
    │ BCryptPass.. │          │ existing     │
    │ instance     │          │ instance     │
    └──────┬───────┘          └──────┬───────┘
           │                         │
           ↓                         │
    ┌──────────────┐                 │
    │ Store in     │                 │
    │ static       │                 │
    │ instance var │                 │
    └──────┬───────┘                 │
           │                         │
           └────────────┬────────────┘
                        ↓
              ┌──────────────────┐
              │ Return instance  │
              └──────────────────┘
```

---

## 🎯 FLOW 2: Multiple Calls to getInstance()

```
SCENARIO: Three different services need PasswordEncoder

┌─────────────────┐
│ UserServiceImpl │
│ calls           │
│ getInstance()   │
└────────┬────────┘
         ↓
    ┌────────────────────┐
    │ First Call         │
    │ instance = null    │
    │ → Create NEW       │
    │ → Return Instance1 │
    └────────┬───────────┘
             ↓
         Instance1 ●───────────────────┐
                                       │
┌─────────────────┐                    │
│ AnotherService  │                    │
│ calls           │                    │
│ getInstance()   │                    │
└────────┬────────┘                    │
         ↓                             │
    ┌────────────────────┐             │
    │ Second Call        │             │
    │ instance EXISTS    │             │
    │ → Return SAME      │             │
    │ → Return Instance1 │─────────────┤
    └────────┬───────────┘             │
             ↓                         │
         Instance1 ●                   │
         (SAME!)                       │
                                       │
┌─────────────────┐                    │
│ ThirdService    │                    │
│ calls           │                    │
│ getInstance()   │                    │
└────────┬────────┘                    │
         ↓                             │
    ┌────────────────────┐             │
    │ Third Call         │             │
    │ instance EXISTS    │             │
    │ → Return SAME      │             │
    │ → Return Instance1 │─────────────┘
    └────────┬───────────┘
             ↓
         Instance1 ●
         (SAME!)

RESULT: All three services use ONE shared instance! ✅
```

---

## 🆚 FLOW 3: With vs Without Singleton

### **WITHOUT Singleton** ❌

```
┌─────────────────┐
│ UserServiceImpl │
│ creates new     │
│ Encoder         │
└────────┬────────┘
         ↓
     Instance1 ●  Memory: 100MB


┌─────────────────┐
│ AnotherService  │
│ creates new     │
│ Encoder         │
└────────┬────────┘
         ↓
     Instance2 ●  Memory: 100MB


┌─────────────────┐
│ ThirdService    │
│ creates new     │
│ Encoder         │
└────────┬────────┘
         ↓
     Instance3 ●  Memory: 100MB

═══════════════════════════════
TOTAL MEMORY: 300MB ❌
INSTANCES: 3 different objects
EFFICIENCY: Poor
```

### **WITH Singleton** ✅

```
┌─────────────────┐
│ UserServiceImpl │──┐
│ getInstance()   │  │
└─────────────────┘  │
                     │
┌─────────────────┐  │
│ AnotherService  │──┼──→  Instance1 ●  Memory: 100MB
│ getInstance()   │  │
└─────────────────┘  │
                     │
┌─────────────────┐  │
│ ThirdService    │──┘
│ getInstance()   │
└─────────────────┘

═══════════════════════════════
TOTAL MEMORY: 100MB ✅
INSTANCES: 1 shared object
EFFICIENCY: Excellent!
```

---

## 🏗️ FLOW 4: Class Structure

```
┌───────────────────────────────────────────────────┐
│         PasswordEncoderSingleton                  │
├───────────────────────────────────────────────────┤
│ FIELDS:                                           │
│  - instance: PasswordEncoder (static, private)    │
│    └─► Holds the single instance                 │
├───────────────────────────────────────────────────┤
│ CONSTRUCTOR:                                      │
│  - PasswordEncoderSingleton() (private)           │
│    └─► Prevents: new PasswordEncoderSingleton()  │
├───────────────────────────────────────────────────┤
│ METHODS:                                          │
│  + getInstance(): PasswordEncoder (static)        │
│    └─► Returns the single instance               │
└───────────────────────────────────────────────────┘
                    │
                    │ creates
                    ↓
        ┌───────────────────────┐
        │ BCryptPasswordEncoder │
        │  (Single Instance)    │
        └───────────────────────┘
                    ↑
                    │ uses
        ┌───────────┴───────────┐
        ↓                       ↓
┌──────────────┐        ┌──────────────┐
│UserServiceImpl│       │Other Services│
└──────────────┘        └──────────────┘
```

---

## 🔐 FLOW 5: Why Private Constructor?

### **What Happens WITHOUT Private Constructor** ❌

```
Anyone can do:
PasswordEncoderSingleton obj1 = new PasswordEncoderSingleton(); ❌
PasswordEncoderSingleton obj2 = new PasswordEncoderSingleton(); ❌
PasswordEncoderSingleton obj3 = new PasswordEncoderSingleton(); ❌

Result: Multiple instances → NOT a Singleton!
```

### **What Happens WITH Private Constructor** ✅

```
Try to do:
PasswordEncoderSingleton obj = new PasswordEncoderSingleton(); ❌

Compiler Error:
"PasswordEncoderSingleton() has private access in PasswordEncoderSingleton"

Only Way:
PasswordEncoder encoder = PasswordEncoderSingleton.getInstance(); ✅

Result: Forced to use getInstance() → Always gets same instance!
```

---

## 🧵 FLOW 6: Thread Safety

### **WITHOUT synchronized** ❌

```
Thread 1                    Thread 2
   │                           │
   ├─ getInstance() called     │
   ├─ Check: instance == null  │
   │  (TRUE)                   │
   │                           ├─ getInstance() called
   │                           ├─ Check: instance == null
   │                           │  (TRUE) ← Problem!
   ├─ Create BCrypt1           │
   │                           ├─ Create BCrypt2
   ├─ instance = BCrypt1       │
   │                           ├─ instance = BCrypt2
   └─ Return BCrypt1           └─ Return BCrypt2

Result: TWO instances created! ❌ NOT a proper Singleton!
```

### **WITH synchronized** ✅

```
Thread 1                    Thread 2
   │                           │
   ├─ getInstance() called     │
   ├─ LOCK acquired            │
   ├─ Check: instance == null  │
   │  (TRUE)                   │
   │                           ├─ getInstance() called
   │                           ├─ WAITING for lock...
   ├─ Create BCrypt            │
   ├─ instance = BCrypt        │
   ├─ LOCK released            │
   ├─ Return BCrypt            │
   │                           ├─ LOCK acquired
   │                           ├─ Check: instance == null
   │                           │  (FALSE) ← Already created!
   │                           ├─ LOCK released
   │                           └─ Return existing BCrypt

Result: ONE instance! ✅ Thread-safe!
```

---

## 🎯 FLOW 7: Complete Application Flow

```
APPLICATION STARTS
        ↓
┌───────────────────┐
│ Spring Boot       │
│ Initializes       │
└────────┬──────────┘
         ↓
┌───────────────────┐
│ UserServiceImpl   │
│ bean created      │
└────────┬──────────┘
         ↓
┌────────────────────────────────────┐
│ Line 20 executes:                  │
│ passwordEncoder =                  │
│   PasswordEncoderSingleton         │
│      .getInstance()                │
└────────┬───────────────────────────┘
         ↓
┌────────────────────────────────────┐
│ getInstance() called for FIRST time│
│ - instance == null? YES            │
│ - Create new BCryptPasswordEncoder │
│ - Store in static instance         │
│ - Return instance                  │
└────────┬───────────────────────────┘
         ↓
    PasswordEncoder ●─────┐
         ↑                │
         │                │
    ┌────┴────┐      ┌────┴────┐
    │ User    │      │ User    │
    │ Registers│     │ Logs In │
    └─────────┘      └─────────┘
         │                │
         └────────┬───────┘
                  ↓
     ┌─────────────────────────┐
     │ All password operations │
     │ use SAME instance!      │
     └─────────────────────────┘
```

---

## 📊 FLOW 8: Memory Comparison

### **WITHOUT Singleton**

```
Time: 0ms
Memory: [                                        ]  0MB

User1 registers
Memory: [████████                                ]  100MB
        Instance1

User2 registers  
Memory: [████████████████                        ]  200MB
        Instance1  Instance2

User3 registers
Memory: [████████████████████████                ]  300MB
        Instance1  Instance2  Instance3

User4 registers
Memory: [████████████████████████████████        ]  400MB
        Instance1  Instance2  Instance3  Instance4
```

### **WITH Singleton**

```
Time: 0ms
Memory: [                                        ]  0MB

User1 registers
Memory: [████████                                ]  100MB
        Instance1 (created)

User2 registers  
Memory: [████████                                ]  100MB
        Instance1 (reused)

User3 registers
Memory: [████████                                ]  100MB
        Instance1 (reused)

User4 registers
Memory: [████████                                ]  100MB
        Instance1 (reused)

Memory stays at 100MB! ✅
```

---

## 🎓 HOW TO DRAW ON BOARD

### **Simple 3-Box Diagram**

```
Draw this on the board when explaining:

┌──────────────┐
│  Service 1   │─┐
└──────────────┘ │
                 │
┌──────────────┐ │    ┌─────────────────┐
│  Service 2   │─┼───→│ ONE Instance    │
└──────────────┘ │    │ BCryptPassword  │
                 │    │ Encoder         │
┌──────────────┐ │    └─────────────────┘
│  Service 3   │─┘
└──────────────┘

Say: "All services point to ONE shared instance!"
```

---

## 💡 KEY POINTS TO EMPHASIZE

1. **First call** → Creates instance
2. **Subsequent calls** → Return SAME instance
3. **Private constructor** → Prevents direct instantiation
4. **synchronized** → Thread-safe
5. **static** → One instance for entire class
6. **Lazy** → Created only when needed
7. **Efficient** → Saves memory and time

---

## 🎯 WHEN LECTURER ASKS...

**"Draw how Singleton works"**  
→ Draw FLOW 2 (Multiple Calls)

**"Why is it better?"**  
→ Draw FLOW 3 (With vs Without)

**"Is it thread-safe?"**  
→ Draw FLOW 6 (Thread Safety)

**"Show me the structure"**  
→ Draw FLOW 4 (Class Structure)

---

**These diagrams will make your explanation crystal clear! 🎨**
