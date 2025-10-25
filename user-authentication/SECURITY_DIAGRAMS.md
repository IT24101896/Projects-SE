# Security Flow Diagrams

## 1. Authentication Flow

```
┌─────────────────────────────────────────────────────────────────┐
│                        LOGIN PROCESS                             │
└─────────────────────────────────────────────────────────────────┘

User                    Frontend                Backend              Database
  │                        │                       │                    │
  │  Enter credentials     │                       │                    │
  ├───────────────────────>│                       │                    │
  │                        │  POST /api/auth/login │                    │
  │                        ├──────────────────────>│                    │
  │                        │  {username, password} │                    │
  │                        │                       │  Find user by     │
  │                        │                       │  username/email   │
  │                        │                       ├──────────────────>│
  │                        │                       │<──────────────────┤
  │                        │                       │  User entity      │
  │                        │                       │                    │
  │                        │                       │  Verify password   │
  │                        │                       │  (BCrypt)         │
  │                        │                       │                    │
  │                        │                       │  Create session   │
  │                        │                       │  Store USER_ID    │
  │                        │                       │  Store USER_ROLE  │
  │                        │                       │                    │
  │                        │<──────────────────────┤                    │
  │                        │  UserDTO + Cookie     │                    │
  │                        │  Set-Cookie:          │                    │
  │                        │  JSESSIONID=xxx       │                    │
  │<───────────────────────┤                       │                    │
  │  Redirect to dashboard │                       │                    │
  │                        │                       │                    │
```

## 2. Access Control Flow - Admin Dashboard

```
┌─────────────────────────────────────────────────────────────────┐
│                  ADMIN ACCESS CONTROL                            │
└─────────────────────────────────────────────────────────────────┘

User                Frontend              Spring Security          Backend
  │                    │                        │                     │
  │  Navigate to       │                        │                     │
  │  /admin-dashboard  │                        │                     │
  ├───────────────────>│                        │                     │
  │                    │  GET /api/auth/        │                     │
  │                    │  current-user          │                     │
  │                    │  Cookie: JSESSIONID    │                     │
  │                    ├───────────────────────>│                     │
  │                    │                        │  Check session      │
  │                    │                        │  exists?            │
  │                    │                        │  ┌──────────┐       │
  │                    │                        │  │ Session  │       │
  │                    │                        │  │ found    │       │
  │                    │                        │  └──────────┘       │
  │                    │                        │                     │
  │                    │                        ├────────────────────>│
  │                    │                        │  Get user by ID     │
  │                    │<───────────────────────┴────────────────────┤
  │                    │  UserDTO {role: ADMIN} │                     │
  │                    │                        │                     │
  │                    │  Check role === ADMIN  │                     │
  │                    │  ✓ Allow access        │                     │
  │                    │                        │                     │
  │                    │  Load admin data       │                     │
  │                    │  GET /api/auth/admin/  │                     │
  │                    │  users                 │                     │
  │                    ├───────────────────────>│                     │
  │                    │                        │  Check authority    │
  │                    │                        │  hasAuthority       │
  │                    │                        │  ("ADMIN")          │
  │                    │                        │  ✓ Authorized       │
  │                    │                        ├────────────────────>│
  │                    │<───────────────────────┴────────────────────┤
  │                    │  List of all users     │                     │
  │<───────────────────┤                        │                     │
  │  Display dashboard │                        │                     │
```

## 3. Access Denied Flow - Passenger Accessing Admin Page

```
┌─────────────────────────────────────────────────────────────────┐
│              UNAUTHORIZED ACCESS ATTEMPT                         │
└─────────────────────────────────────────────────────────────────┘

Passenger           Frontend              Spring Security          Backend
  │                    │                        │                     │
  │  Try to access     │                        │                     │
  │  /admin-dashboard  │                        │                     │
  ├───────────────────>│                        │                     │
  │                    │  GET /api/auth/        │                     │
  │                    │  current-user          │                     │
  │                    ├───────────────────────>│                     │
  │                    │                        │  Check session      │
  │                    │                        ├────────────────────>│
  │                    │<───────────────────────┴────────────────────┤
  │                    │  UserDTO {role: PASSENGER}                   │
  │                    │                        │                     │
  │                    │  Check role === ADMIN  │                     │
  │                    │  ✗ FAIL                │                     │
  │                    │                        │                     │
  │                    │  Alert: Access Denied  │                     │
  │<───────────────────┤                        │                     │
  │  Show error        │                        │                     │
  │                    │  Redirect to           │                     │
  │                    │  passenger-dashboard   │                     │
  │<───────────────────┤                        │                     │
  │                    │                        │                     │
  │                    │                        │                     │
  │  OR if tries API:  │                        │                     │
  │                    │  GET /api/auth/admin/  │                     │
  │                    │  users                 │                     │
  │                    ├───────────────────────>│                     │
  │                    │                        │  Check authority    │
  │                    │                        │  hasAuthority       │
  │                    │                        │  ("ADMIN")          │
  │                    │                        │  ✗ UNAUTHORIZED     │
  │                    │<───────────────────────┤                     │
  │                    │  403 FORBIDDEN         │                     │
  │<───────────────────┤                        │                     │
  │  Error displayed   │                        │                     │
```

## 4. Profile Edit Protection Flow

```
┌─────────────────────────────────────────────────────────────────┐
│           PASSENGER EDITING OWN PROFILE (ALLOWED)               │
└─────────────────────────────────────────────────────────────────┘

Passenger (ID=5)    Frontend              Backend                Database
  │                    │                      │                      │
  │  Edit profile      │                      │                      │
  ├───────────────────>│                      │                      │
  │                    │  PUT /api/auth/      │                      │
  │                    │  users/5/profile     │                      │
  │                    │  Cookie: JSESSIONID  │                      │
  │                    ├─────────────────────>│                      │
  │                    │  {username: "new"}   │  Get session         │
  │                    │                      │  USER_ID = 5         │
  │                    │                      │                      │
  │                    │                      │  Check:              │
  │                    │                      │  requestUserId(5)    │
  │                    │                      │  == sessionUserId(5) │
  │                    │                      │  ✓ MATCH             │
  │                    │                      │                      │
  │                    │                      │  Update profile      │
  │                    │                      ├─────────────────────>│
  │                    │<─────────────────────┤                      │
  │                    │  200 OK              │                      │
  │<───────────────────┤  Updated UserDTO     │                      │
  │  Profile updated   │                      │                      │


┌─────────────────────────────────────────────────────────────────┐
│       PASSENGER EDITING OTHER'S PROFILE (BLOCKED)               │
└─────────────────────────────────────────────────────────────────┘

Passenger (ID=5)    Frontend              Backend
  │                    │                      │
  │  Try to edit       │                      │
  │  user ID=8         │                      │
  ├───────────────────>│                      │
  │                    │  PUT /api/auth/      │
  │                    │  users/8/profile     │
  │                    │  Cookie: JSESSIONID  │
  │                    ├─────────────────────>│
  │                    │                      │  Get session
  │                    │                      │  USER_ID = 5
  │                    │                      │
  │                    │                      │  Check:
  │                    │                      │  requestUserId(8)
  │                    │                      │  == sessionUserId(5)
  │                    │                      │  ✗ MISMATCH
  │                    │                      │
  │                    │<─────────────────────┤
  │                    │  403 FORBIDDEN       │
  │<───────────────────┤                      │
  │  Error: Access     │                      │
  │  Denied            │                      │
```

## 5. Session Management

```
┌─────────────────────────────────────────────────────────────────┐
│                     SESSION LIFECYCLE                            │
└─────────────────────────────────────────────────────────────────┘

Timeline              Server                    Session Store
    │                   │                            │
    │  LOGIN            │                            │
    ├──────────────────>│  Create session            │
    │                   ├───────────────────────────>│
    │                   │  Store:                    │
    │                   │  - USER_ID: 5              │
    │                   │  - USER_ROLE: PASSENGER    │
    │                   │  - SPRING_SECURITY_CONTEXT │
    │                   │                            │
    │                   │  Generate JSESSIONID       │
    │<──────────────────┤  Send cookie to browser    │
    │  Set-Cookie:      │                            │
    │  JSESSIONID=abc123│                            │
    │                   │                            │
    ▼                   │                            │
    │                   │                            │
    │  Each Request     │                            │
    ├──────────────────>│  Cookie: JSESSIONID=abc123 │
    │                   ├───────────────────────────>│
    │                   │  Retrieve session          │
    │                   │<───────────────────────────┤
    │                   │  Session data:             │
    │                   │  USER_ID=5, ROLE=PASSENGER │
    │                   │                            │
    │                   │  Process request with      │
    │                   │  user context              │
    │<──────────────────┤                            │
    │  Response         │                            │
    │                   │                            │
    ▼                   │                            │
    │                   │                            │
    │  LOGOUT           │                            │
    ├──────────────────>│  Invalidate session        │
    │                   ├───────────────────────────>│
    │                   │  Delete session data       │
    │                   │                            │
    │<──────────────────┤  Clear cookie              │
    │  Session destroyed│                            │
    │                   │                            │
```

## 6. Complete Security Architecture

```
┌─────────────────────────────────────────────────────────────────────┐
│                       SECURITY LAYERS                                │
└─────────────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────────┐
│  Layer 1: Frontend Route Protection                              │
│  - Checks /api/auth/current-user on page load                   │
│  - Redirects if not authenticated                                │
│  - Redirects if wrong role                                       │
└──────────────────────────────────────────────────────────────────┘
                              ↓
┌──────────────────────────────────────────────────────────────────┐
│  Layer 2: Spring Security Filter Chain                          │
│  - Validates session cookie (JSESSIONID)                         │
│  - Checks if URL requires authentication                         │
│  - Checks if user has required authority (ADMIN)                 │
│  - Returns 401/403 if unauthorized                               │
└──────────────────────────────────────────────────────────────────┘
                              ↓
┌──────────────────────────────────────────────────────────────────┐
│  Layer 3: Controller-Level Validation                           │
│  - Verifies session exists                                       │
│  - Checks USER_ID in session                                     │
│  - Validates user owns the resource being accessed               │
│  - Returns 403 if user tries to access others' data              │
└──────────────────────────────────────────────────────────────────┘
                              ↓
┌──────────────────────────────────────────────────────────────────┐
│  Layer 4: Service-Level Business Logic                          │
│  - Validates business rules                                      │
│  - Checks user permissions for specific actions                  │
│  - Prevents self-deletion, etc.                                  │
└──────────────────────────────────────────────────────────────────┘
                              ↓
┌──────────────────────────────────────────────────────────────────┐
│  Layer 5: Database                                               │
│  - Password stored as BCrypt hash                                │
│  - User roles and status enforced                                │
└──────────────────────────────────────────────────────────────────┘
```

## 7. Role-Based Access Matrix

```
┌──────────────────────────────────────────────────────────────────────┐
│                    PERMISSIONS MATRIX                                 │
├─────────────────────────┬──────────┬──────────┬─────────────────────┤
│ Action                  │ ADMIN    │ STAFF    │ PASSENGER           │
├─────────────────────────┼──────────┼──────────┼─────────────────────┤
│ View all users          │ ✓        │ ✗        │ ✗                   │
│ Change user roles       │ ✓        │ ✗        │ ✗                   │
│ Activate/Deactivate     │ ✓        │ ✗        │ ✗                   │
│ Delete users            │ ✓        │ ✗        │ ✗                   │
│ View own profile        │ ✓        │ ✓        │ ✓                   │
│ Edit own profile        │ ✓        │ ✓        │ ✓                   │
│ Change own password     │ ✓        │ ✓        │ ✓                   │
│ View others' profiles   │ ✓        │ ✗        │ ✗                   │
│ Edit others' profiles   │ ✗        │ ✗        │ ✗                   │
│ Access admin dashboard  │ ✓        │ ✗        │ ✗                   │
│ Access passenger dash   │ ✗        │ ✗        │ ✓                   │
│ Reset password (email)  │ ✓        │ ✓        │ ✓                   │
└─────────────────────────┴──────────┴──────────┴─────────────────────┘
```

## 8. Endpoint Security Mapping

```
┌────────────────────────────────────────────────────────────────────┐
│                      API ENDPOINTS                                  │
├────────────────────────────────────┬────────────────────────────────┤
│ Endpoint                           │ Security                       │
├────────────────────────────────────┼────────────────────────────────┤
│ POST /api/auth/register            │ Public                         │
│ POST /api/auth/login               │ Public                         │
│ PUT  /api/auth/reset-password      │ Public                         │
├────────────────────────────────────┼────────────────────────────────┤
│ GET  /api/auth/current-user        │ Authenticated                  │
│ POST /api/auth/logout              │ Authenticated                  │
│ PUT  /api/auth/users/{id}/profile  │ Authenticated + Owner check    │
│ PUT  /api/auth/users/{id}/password │ Authenticated + Owner check    │
├────────────────────────────────────┼────────────────────────────────┤
│ GET  /api/auth/admin/users         │ ADMIN role required            │
│ GET  /api/auth/admin/users/{id}    │ ADMIN role required            │
│ PUT  /api/auth/admin/users/{id}/.. │ ADMIN role required            │
│ DELETE /api/auth/admin/users/{id}  │ ADMIN role required            │
└────────────────────────────────────┴────────────────────────────────┘
```
