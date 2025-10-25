# 🚀 Quick Reference Guide

## 🏃 Quick Start

### 1. Setup Java
```powershell
# Set JAVA_HOME
setx JAVA_HOME "C:\Program Files\Java\jdk-17"
setx PATH "%PATH%;%JAVA_HOME%\bin"

# Verify
java -version
```

### 2. Setup Database
```sql
CREATE DATABASE train_reservation_system;
```

### 3. Configure Application
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

### 4. Run Application
```powershell
.\mvnw.cmd spring-boot:run
```

### 5. Access
- Open: http://localhost:8080/login.html
- Register users with different roles

---

## 🔑 Default Test Credentials

Create these users via http://localhost:8080/register.html:

| Username | Email | Password | Role |
|----------|-------|----------|------|
| admin | admin@test.com | admin123 | ADMIN |
| passenger1 | pass1@test.com | pass123 | PASSENGER |
| passenger2 | pass2@test.com | pass123 | PASSENGER |

---

## 🛡️ Security Checklist

### ✅ Backend Protection (Spring Security)
- [x] Role-based access control enabled
- [x] Admin endpoints require ADMIN role
- [x] Session-based authentication
- [x] Ownership validation on user operations
- [x] Passwords encrypted with BCrypt

### ✅ Frontend Integration
- [x] No localStorage used for auth
- [x] Backend verification on page load
- [x] Session cookies sent with requests
- [x] Proper error handling (401/403)
- [x] Role-based redirects

---

## 📡 API Endpoints Quick Reference

### Public Endpoints
```
POST /api/auth/register        - Create account
POST /api/auth/login           - Login
PUT  /api/auth/reset-password  - Reset password
```

### Authenticated Endpoints
```
GET  /api/auth/current-user           - Get logged-in user
POST /api/auth/logout                 - Logout
PUT  /api/auth/users/{id}/profile     - Update own profile (ownership check)
PUT  /api/auth/users/{id}/password    - Change own password (ownership check)
```

### Admin-Only Endpoints
```
GET    /api/auth/admin/users              - List all users
GET    /api/auth/admin/users/{id}         - Get user by ID
PUT    /api/auth/admin/users/{id}/role    - Change user role
PUT    /api/auth/admin/users/{id}/activate   - Activate user
PUT    /api/auth/admin/users/{id}/deactivate - Deactivate user
DELETE /api/auth/admin/users/{id}         - Delete user
GET    /api/auth/admin/users/role/{role}  - Users by role
GET    /api/auth/admin/users/status/{active} - Users by status
```

---

## 🎭 Roles & Permissions

| Action | ADMIN | PASSENGER |
|--------|-------|-----------|
| View all users | ✅ | ❌ |
| Change user roles | ✅ | ❌ |
| Activate/deactivate users | ✅ | ❌ |
| Delete users | ✅ | ❌ |
| View own profile | ✅ | ✅ |
| Edit own profile | ✅ | ✅ |
| Change own password | ✅ | ✅ |
| View other users' profiles | ✅ | ❌ |
| Edit other users' profiles | ❌ | ❌ |

---

## 🧪 Security Tests

### Test 1: Passenger Cannot Access Admin Page
```
1. Login as PASSENGER
2. Go to: http://localhost:8080/admin-dashboard.html
3. Expected: Redirected to passenger-dashboard.html
```

### Test 2: Passenger Cannot Call Admin API
```javascript
// Login as PASSENGER, then in browser console:
fetch('/api/auth/admin/users', {credentials: 'include'})
  .then(r => r.json())
  .then(console.log)
// Expected: 403 Forbidden
```

### Test 3: Passenger Cannot Edit Other's Profile
```javascript
// Login as Passenger ID=5, then:
fetch('/api/auth/users/8/profile', {
  method: 'PUT',
  headers: {'Content-Type': 'application/json'},
  credentials: 'include',
  body: JSON.stringify({username: 'hacked'})
})
// Expected: 403 Forbidden
```

### Test 4: Admin Can Manage Users
```
1. Login as ADMIN
2. Go to: http://localhost:8080/user-management.html
3. Expected: See all users, can change roles, activate/deactivate
```

---

## 🔧 Troubleshooting

| Problem | Solution |
|---------|----------|
| JAVA_HOME not defined | Set environment variable, restart PowerShell |
| Cannot connect to DB | Check MySQL running, verify credentials |
| Port 8080 in use | Change port in application.properties |
| 401 Unauthorized | Session expired, login again |
| 403 Forbidden | Insufficient permissions, check user role |
| Session lost on restart | Normal - sessions stored in memory |

---

## 📁 Project Structure

```
src/main/
├── java/com/trainreservation/auth/
│   ├── config/
│   │   └── SecurityConfig.java       ← Spring Security config
│   ├── controller/
│   │   └── UserController.java       ← REST endpoints
│   ├── service/
│   │   └── UserServiceImpl.java      ← Business logic
│   ├── repository/
│   │   └── UserRepository.java       ← Database
│   └── entity/
│       ├── User.java                 ← User entity
│       └── UserRole.java             ← ADMIN/PASSENGER/STAFF
└── resources/
    ├── static/
    │   ├── login.html               ← Login page
    │   ├── passenger-dashboard.html ← Passenger view
    │   ├── admin-dashboard.html     ← Admin view
    │   └── user-management.html     ← Admin: manage users
    └── application.properties       ← Configuration
```

---

## 🔐 How Security Works

### Authentication Flow
```
1. User logs in → Backend validates credentials
2. Backend creates session with user ID and role
3. Browser receives JSESSIONID cookie
4. Every request includes cookie
5. Backend validates session and role
```

### Authorization Flow
```
1. User tries to access /admin-dashboard.html
2. Frontend calls /api/auth/current-user
3. Backend checks session, returns user data
4. Frontend checks role === ADMIN
5. If PASSENGER → redirect to passenger-dashboard
6. If ADMIN → load admin dashboard
```

### API Protection
```
1. User calls /api/auth/admin/users
2. Spring Security checks session
3. Validates hasAuthority("ADMIN")
4. If PASSENGER → return 403 Forbidden
5. If ADMIN → return user list
```

---

## 💡 Key Security Principles

1. **Never trust the client** - All validation on backend
2. **Session-based auth** - No localStorage manipulation
3. **Role-based access** - ADMIN vs PASSENGER permissions
4. **Ownership validation** - Users can only edit their own data
5. **Defense in depth** - Multiple security layers

---

## 📞 Common Commands

### Build
```powershell
.\mvnw.cmd clean install
```

### Run
```powershell
.\mvnw.cmd spring-boot:run
```

### Test
```powershell
.\mvnw.cmd test
```

### Package
```powershell
.\mvnw.cmd package
```

---

## 🌐 URLs

| Page | URL | Role Required |
|------|-----|---------------|
| Home | http://localhost:8080/ | Public |
| Login | http://localhost:8080/login.html | Public |
| Register | http://localhost:8080/register.html | Public |
| Passenger Dashboard | http://localhost:8080/passenger-dashboard.html | PASSENGER |
| Admin Dashboard | http://localhost:8080/admin-dashboard.html | ADMIN |
| User Management | http://localhost:8080/user-management.html | ADMIN |
| Profile | http://localhost:8080/profile.html | Authenticated |

---

## ✅ Implementation Highlights

### What Changed
- ❌ Before: Security completely disabled
- ✅ After: Role-based access control

### How It Works
- ✅ Spring Security validates every request
- ✅ Sessions stored server-side (not localStorage)
- ✅ Passengers can only access their own data
- ✅ Admins can manage all users
- ✅ All security enforced on backend

### No JavaScript Security
- ❌ No client-side validation for security
- ✅ All checks done server-side
- ✅ Frontend only displays appropriate UI

---

## 📚 Documentation Files

- **README.md** - Overview and summary
- **SECURITY_IMPLEMENTATION.md** - Detailed security docs
- **SECURITY_DIAGRAMS.md** - Visual flow diagrams
- **SETUP_AND_RUN.md** - Setup instructions
- **QUICK_REFERENCE.md** - This file

---

## 🎯 Quick Validation

After running, verify:
- ✅ Login as ADMIN → See admin dashboard
- ✅ Login as PASSENGER → See passenger dashboard
- ✅ Passenger tries admin page → Access denied
- ✅ Passenger calls admin API → 403 Forbidden
- ✅ Passenger edits own profile → Success
- ✅ Passenger edits other's profile → 403 Forbidden

---

**All security is enforced on the backend. No JavaScript validations are used for security purposes.**
