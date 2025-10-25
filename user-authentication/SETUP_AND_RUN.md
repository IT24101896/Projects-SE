# Setup and Run Guide

## Prerequisites

### 1. Install Java 17 JDK
- Download from: https://adoptium.net/ or https://www.oracle.com/java/technologies/downloads/
- Install to a directory (e.g., `C:\Program Files\Java\jdk-17`)

### 2. Set JAVA_HOME Environment Variable
1. Right-click **This PC** → **Properties**
2. Click **Advanced System Settings**
3. Click **Environment Variables**
4. Under **System Variables**, click **New**
   - **Variable name**: `JAVA_HOME`
   - **Variable value**: `C:\Program Files\Java\jdk-17` (your JDK installation path)
5. Find the **Path** variable in System Variables
6. Click **Edit** → **New** → Add: `%JAVA_HOME%\bin`
7. Click **OK** on all dialogs

### 3. Verify Java Installation
Open PowerShell and run:
```powershell
java -version
```
Should show Java 17.x.x

### 4. Install MySQL Database
- Download MySQL Community Server: https://dev.mysql.com/downloads/mysql/
- Install and remember your root password
- Start MySQL service

### 5. Create Database
Open MySQL command line or MySQL Workbench:
```sql
CREATE DATABASE train_reservation_system;
```

### 6. Configure Database Connection
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/train_reservation_system
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

---

## Running the Application

### Method 1: Using Maven Wrapper (Recommended)
```powershell
cd "c:\Users\Jithmi Abewickrama\Desktop\2nd Year 1st Sem\SE\SE Lab 7 Project\user-authentication"
.\mvnw.cmd spring-boot:run
```

### Method 2: Using IDE
1. Open the project in IntelliJ IDEA or Eclipse
2. Right-click `UserAuthenticationApplication.java`
3. Select **Run 'UserAuthenticationApplication'**

---

## Accessing the Application

Once running, open your browser:

- **Home Page**: http://localhost:8080/
- **Login**: http://localhost:8080/login.html
- **Register**: http://localhost:8080/register.html

---

## Creating Test Users

### Option 1: Via Register Page
1. Go to http://localhost:8080/register.html
2. Create users with different roles:
   - **Admin User**: username=`admin`, role=`ADMIN`
   - **Passenger User**: username=`passenger1`, role=`PASSENGER`

### Option 2: Direct Database Insert
```sql
USE train_reservation_system;

-- Admin user (password: admin123)
INSERT INTO users (username, email, password, role, active, created_at, updated_at)
VALUES ('admin', 'admin@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN', 1, NOW(), NOW());

-- Passenger user (password: pass123)
INSERT INTO users (username, email, password, role, active, created_at, updated_at)
VALUES ('passenger1', 'passenger@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'PASSENGER', 1, NOW(), NOW());
```

---

## Testing Security

### Test 1: Login as Admin
1. Login with admin credentials
2. You should see **Admin Dashboard**
3. Navigate to **User Management**
4. You can view all users, change roles, activate/deactivate

### Test 2: Login as Passenger
1. Login with passenger credentials
2. You should see **Passenger Dashboard**
3. Try to access: http://localhost:8080/admin-dashboard.html
4. **Expected**: Redirected to login or shown access denied

### Test 3: API Security (Browser Console)
1. Login as Passenger
2. Open browser DevTools (F12) → Console
3. Try to call admin API:
   ```javascript
   fetch('/api/auth/admin/users', {credentials: 'include'})
     .then(r => r.json())
     .then(console.log)
   ```
4. **Expected**: 403 Forbidden error

### Test 4: Profile Edit Protection
1. Login as Passenger (ID = 5)
2. Open DevTools → Console
3. Try to edit another user's profile (ID = 8):
   ```javascript
   fetch('/api/auth/users/8/profile', {
     method: 'PUT',
     headers: {'Content-Type': 'application/json'},
     credentials: 'include',
     body: JSON.stringify({username: 'hacked'})
   })
   ```
4. **Expected**: 403 Forbidden

---

## Troubleshooting

### Issue: "JAVA_HOME is not defined"
- Make sure you set JAVA_HOME environment variable
- Restart PowerShell after setting
- Verify: `echo $env:JAVA_HOME`

### Issue: "Cannot connect to database"
- Ensure MySQL is running
- Check username/password in `application.properties`
- Verify database `train_reservation_system` exists

### Issue: "Port 8080 already in use"
- Stop other applications using port 8080
- Or change port in `application.properties`: `server.port=8081`

### Issue: "401 Unauthorized" on protected pages
- Session expired or not logged in
- Log in again via `/login.html`

### Issue: "403 Forbidden" on admin pages
- User does not have ADMIN role
- Check user role in database
- Login with admin account

---

## Application Structure

```
src/main/
├── java/com/trainreservation/auth/
│   ├── config/
│   │   ├── SecurityConfig.java        ← Spring Security configuration
│   │   └── GlobalExceptionHandler.java
│   ├── controller/
│   │   └── UserController.java        ← REST API endpoints
│   ├── service/
│   │   ├── UserService.java
│   │   └── UserServiceImpl.java       ← Business logic
│   ├── repository/
│   │   └── UserRepository.java        ← Database access
│   ├── entity/
│   │   ├── User.java                  ← User entity
│   │   └── UserRole.java              ← PASSENGER, STAFF, ADMIN
│   └── dto/
│       └── *.java                     ← Data transfer objects
└── resources/
    ├── static/
    │   ├── login.html                 ← Login page
    │   ├── register.html              ← Registration
    │   ├── passenger-dashboard.html   ← Passenger view
    │   ├── admin-dashboard.html       ← Admin view
    │   ├── user-management.html       ← Admin: manage users
    │   └── profile.html               ← User profile
    └── application.properties         ← Configuration
```

---

## Key Features Implemented

✅ **Backend Security**
- Spring Security with role-based access control
- Session-based authentication (no tokens)
- Protected admin endpoints
- User-specific data access control

✅ **Frontend Security**
- No localStorage manipulation
- Backend authentication verification
- Role-based page access
- Proper error handling and redirects

✅ **Access Control**
- Passengers can only view/edit their own data
- Admins can manage all users
- Staff can access staff-specific features

✅ **Password Security**
- BCrypt password hashing
- Password change requires current password
- Password reset by email

---

## Default Credentials

After running the application and registering:
- **Admin**: Create via register page with ADMIN role
- **Passenger**: Create via register page with PASSENGER role

Or use the SQL inserts above with these passwords:
- Password: `admin123` (for admin)
- Password: `pass123` (for passenger)

---

## Next Steps

1. Configure session timeout in `application.properties`
2. Add email service for password reset
3. Implement CSRF protection for production
4. Configure HTTPS for production
5. Add rate limiting for login attempts
6. Implement account lockout after failed attempts

---

## Support

For issues or questions, refer to:
- `SECURITY_IMPLEMENTATION.md` - Detailed security architecture
- Spring Security Documentation: https://spring.io/projects/spring-security
- Spring Boot Documentation: https://spring.io/projects/spring-boot
