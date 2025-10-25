# 🎯 User Authentication System - Complete Implementation

## 🏗️ Design Patterns Implemented

This project showcases **TWO powerful design patterns**:

### 1️⃣ **Singleton Pattern** - [`PasswordEncoderSingleton`](src/main/java/com/trainreservation/auth/util/PasswordEncoderSingleton.java)
- **Purpose**: Ensures ONE shared instance of BCryptPasswordEncoder
- **Benefits**: Memory efficient, thread-safe, consistent encoding
- **Documentation**: [Complete Singleton Guide](SINGLETON_COMPLETE_GUIDE.md)

### 2️⃣ **Factory Pattern** - [`DTOMapperFactory`](src/main/java/com/trainreservation/auth/util/DTOMapperFactory.java)
- **Purpose**: Creates different types of UserDTOs from User entities
- **Benefits**: Centralized creation logic, flexible, easy to extend
- **Documentation**: [Factory Pattern Guide](FACTORY_PATTERN_GUIDE.md)

📚 **Quick Reference**: [Design Patterns Comparison](DESIGN_PATTERN_COMPARISON.md)

---

## ✅ What Was Fixed

### **BEFORE** (Critical Security Issues)
```java
// SecurityConfig.java - COMPLETELY INSECURE
.authorizeHttpRequests(authz -> authz
    .requestMatchers("/**").permitAll()  // ❌ Everything allowed!
    .anyRequest().permitAll()
)
```

### **AFTER** (Secure Implementation)
```java
// SecurityConfig.java - PROPERLY SECURED
.authorizeHttpRequests(authz -> authz
    .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()  // Public
    .requestMatchers("/api/auth/admin/**").hasAuthority("ADMIN")           // Admin only
    .requestMatchers("/api/auth/users/**").authenticated()                 // Logged in users
    .anyRequest().authenticated()
)
```

---

## 📁 Files Modified

### Backend (Java)
1. **`SecurityConfig.java`**
   - ✅ Implemented role-based access control
   - ✅ Session management enabled
   - ✅ Proper authentication/authorization

2. **`UserController.java`**
   - ✅ Added session creation on login
   - ✅ Added `/current-user` endpoint for authentication check
   - ✅ Added ownership validation for profile/password updates
   - ✅ Added logout endpoint that invalidates session
   - ✅ Fixed admin endpoints to use session-based auth

### Frontend (HTML)
3. **`login.html`**
   - ✅ Removed localStorage storage
   - ✅ Added `credentials: 'include'` for session cookies
   - ✅ Improved error handling

4. **`passenger-dashboard.html`**
   - ✅ Validates authentication with backend on load
   - ✅ Checks user role via `/api/auth/current-user`
   - ✅ Redirects if not PASSENGER
   - ✅ Logout calls backend endpoint

5. **`admin-dashboard.html`**
   - ✅ Validates authentication with backend on load
   - ✅ Checks user role via `/api/auth/current-user`
   - ✅ Redirects if not ADMIN
   - ✅ Admin API calls include credentials

6. **`user-management.html`**
   - ✅ Validates admin role on load
   - ✅ Fixed API URLs (`/api/auth/admin/users`)
   - ✅ All API calls include credentials
   - ✅ Proper error handling for unauthorized access

7. **`profile.html`**
   - ✅ Loads profile data from backend (not localStorage)
   - ✅ Validates authentication on load
   - ✅ Logout calls backend endpoint

8. **`register.html`**
   - ✅ Fixed API URL to use relative path

---

## 🔒 Security Features Implemented

### 1. **Session-Based Authentication**
- ✅ Login creates server-side session
- ✅ Session stores user ID and role
- ✅ `JSESSIONID` cookie sent with every request (HttpOnly)
- ✅ Backend validates session on protected endpoints
- ✅ Logout invalidates session completely

### 2. **Role-Based Access Control (RBAC)**
```
ADMIN:
  ✓ Access admin dashboard
  ✓ View all users
  ✓ Activate/deactivate users
  ✓ Change user roles
  ✓ View any user's profile

PASSENGER:
  ✓ Access passenger dashboard
  ✓ View own profile
  ✓ Edit own profile
  ✗ Cannot view other passengers
  ✗ Cannot access admin functions
  ✗ Cannot modify other users

STAFF:
  ✓ Access staff dashboard
  ✓ View own profile
  ✓ Edit own profile
```

### 3. **Ownership Validation**
```java
// Passengers can ONLY edit their OWN profile
Long currentUserId = (Long) session.getAttribute("USER_ID");
if (!currentUserId.equals(userId)) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
}
```

### 4. **Backend Authorization**
- ✅ Spring Security enforces role requirements
- ✅ Admin endpoints return 403 for non-admins
- ✅ Profile updates verify ownership
- ✅ Password changes verify ownership

### 5. **No Client-Side Security**
- ❌ No localStorage (can be manipulated)
- ❌ No JavaScript-based validation
- ✅ All security enforced server-side
- ✅ Frontend only displays appropriate UI

---

## 🧪 Security Test Cases

### ✅ Test 1: Passenger Cannot Access Admin Dashboard
**Steps:**
1. Login as PASSENGER
2. Try to access `/admin-dashboard.html`

**Expected:**
- Frontend: Checks role via `/api/auth/current-user`
- Detects role is PASSENGER (not ADMIN)
- Shows "Access Denied" alert
- Redirects to `passenger-dashboard.html`

**Backend Protection:**
- If passenger directly calls `/api/auth/admin/users`
- Spring Security returns **403 Forbidden**

---

### ✅ Test 2: Passenger Cannot View Other Passengers
**Steps:**
1. Login as Passenger (ID = 5)
2. Try to access: `PUT /api/auth/users/8/profile`

**Expected:**
- Backend checks session: `USER_ID = 5`
- Compares with request: `userId = 8`
- Returns **403 Forbidden**

---

### ✅ Test 3: Admin Can Manage All Users
**Steps:**
1. Login as ADMIN
2. Access `/user-management.html`
3. View all users, change roles, activate/deactivate

**Expected:**
- Frontend calls `/api/auth/admin/users`
- Spring Security checks: `hasAuthority("ADMIN")` ✓
- Returns list of all users
- Admin can modify any user

---

### ✅ Test 4: Session Expiration
**Steps:**
1. Login as any user
2. Wait for session timeout (or restart server)
3. Try to access protected page

**Expected:**
- Session no longer exists
- `/api/auth/current-user` returns 401 Unauthorized
- Frontend redirects to `login.html`

---

### ✅ Test 5: Direct API Call Blocked
**Steps:**
1. Login as PASSENGER
2. Open browser console (F12)
3. Execute:
```javascript
fetch('/api/auth/admin/users', {credentials: 'include'})
```

**Expected:**
- Spring Security intercepts request
- Checks user role: PASSENGER
- Returns **403 Forbidden**
- Console shows error

---

## 📊 Security Layers

```
┌────────────────────────────────────────────┐
│  Layer 1: Frontend                         │
│  - Role-based UI display                   │
│  - Backend authentication check on load    │
│  - Redirects for wrong role                │
└────────────────────────────────────────────┘
                    ↓
┌────────────────────────────────────────────┐
│  Layer 2: Spring Security                  │
│  - Session validation                      │
│  - Role-based endpoint access              │
│  - Returns 401/403 for unauthorized        │
└────────────────────────────────────────────┘
                    ↓
┌────────────────────────────────────────────┐
│  Layer 3: Controller                       │
│  - Ownership validation                    │
│  - Business logic checks                   │
│  - Prevents unauthorized modifications     │
└────────────────────────────────────────────┘
                    ↓
┌────────────────────────────────────────────┐
│  Layer 4: Service Layer                    │
│  - Additional business rules               │
│  - Password validation                     │
│  - Prevents self-deletion                  │
└────────────────────────────────────────────┘
```

---

## 🚀 How to Run

### Prerequisites
1. **Install Java 17**
2. **Set JAVA_HOME** environment variable
3. **Install MySQL**
4. **Create database**: `train_reservation_system`
5. **Configure** `application.properties` with MySQL credentials

### Start Application
```powershell
cd "user-authentication"
.\mvnw.cmd spring-boot:run
```

### Access Application
- **Home**: http://localhost:8080/
- **Login**: http://localhost:8080/login.html
- **Register**: http://localhost:8080/register.html

### Create Test Users
**Via Register Page:**
1. Register admin: `username=admin`, `role=ADMIN`
2. Register passenger: `username=passenger1`, `role=PASSENGER`

---

## 📚 Documentation Files

1. **`SECURITY_IMPLEMENTATION.md`**
   - Detailed security architecture
   - How backend security works
   - How frontend validation works
   - Implementation checklist

2. **`SECURITY_DIAGRAMS.md`**
   - Visual flow diagrams
   - Authentication flow
   - Access control flow
   - Session management
   - Security layers

3. **`SETUP_AND_RUN.md`**
   - Setup instructions
   - Environment configuration
   - Running the application
   - Troubleshooting guide

4. **`README.md`** (this file)
   - Quick summary
   - What was fixed
   - Security features
   - Test cases

---

## 🎓 Key Takeaways

### ❌ **What NOT to Do**
1. Don't disable Spring Security completely
2. Don't store user credentials in localStorage
3. Don't rely on frontend JavaScript for security
4. Don't allow unrestricted API access
5. Don't trust client-side role checks

### ✅ **What TO Do**
1. Use Spring Security for authentication/authorization
2. Implement session-based authentication
3. Enforce role-based access control
4. Validate ownership on backend
5. Use secure cookies (HttpOnly, SameSite)
6. Always validate on the server side

---

## 🔐 Security Principles Applied

1. **Defense in Depth**: Multiple security layers
2. **Least Privilege**: Users only access what they need
3. **Secure by Default**: Everything denied unless explicitly allowed
4. **Server-Side Validation**: Never trust the client
5. **Session Management**: Proper authentication tracking
6. **Role-Based Access**: Granular permission control

---

## 📝 Summary

### Problem Statement
"Passengers shouldn't be able to edit or view other passengers' details. Only admins should see those things."

### Solution Implemented
✅ **Backend Security (Spring Security)**
- Role-based access control with `hasAuthority("ADMIN")`
- Session-based authentication
- Ownership validation for profile/password updates
- Admin endpoints protected at security layer

✅ **Frontend Integration**
- No localStorage manipulation
- Backend authentication verification
- Proper redirects based on role
- API calls include session credentials

✅ **Result**
- ✅ Passengers can only view/edit their own data
- ✅ Admins can view/manage all users
- ✅ No JavaScript validation bypass possible
- ✅ All security enforced on backend
- ✅ Proper error handling and user feedback

---

## 🎯 Conclusion

The application now has **proper security** implemented with:
- ✅ Session-based authentication
- ✅ Role-based access control
- ✅ Ownership validation
- ✅ No client-side security bypass
- ✅ Backend enforcement of all rules

**No JavaScript validations are used for security** - everything is handled by Spring Security and backend validation logic. The frontend only provides appropriate UI and user experience.
