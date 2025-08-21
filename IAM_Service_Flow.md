# 🛡️ IAM Service API Flow (README)

This document describes the step-by-step flow for creating an account, onboarding a root user, assigning roles, and testing user access via JWT authentication.

---

## 1. Create an Account
**Endpoint**  
```
POST /api/v1/accounts/create
```

**Request**
```json
{
  "name": "commvault",
  "description": "test-account",
  "type": "ORGANIZATION"
}
```

**Response**
```json
{
  "id": 1,
  "name": "commvault",
  "description": "test-account",
  "type": "ORGANIZATION",
  "status": "CREATED"
}
```

---

## 2. Create Root User
**Endpoint**  
```
POST /api/v1/accounts/{accountId}/users/root/create
```

**Request**
```json
{
  "email": "ganeshamoorthy060@gmail.com",
  "firstName": "ganesh",
  "lastName": "moorthy",
  "description": "This is a root account for test purpose"
}
```

**Response**
```json
{
  "id": 1,
  "firstName": "ganesh",
  "lastName": "moorthy",
  "email": "ganeshamoorthy060@gmail.com",
  "type": "PASSWORD",
  "accountId": 1,
  "status": "CREATED",
  "roles": [
    {
      "id": 1,
      "name": "ROOT",
      "description": "Root role with all permissions"
    }
  ]
}
```

---

## 3. Root User OTP Validation
**Endpoint**  
```
POST /api/v1/otp/validate
```

**Request**
```json
{
  "email": "ganeshamoorthy060@gmail.com",
  "otp": 631462,
  "accountId": 1,
  "password": "password"
}
```

**Response**
```json
{
  "status": "VALID"
}
```

---

## 4. Set Password for Root User
**Endpoint**  
```
POST /api/v1/accounts/{accountId}/users/set-password
```

**Request**
```json
{
  "email": "ganeshamoorthy060@gmail.com",
  "password": "password"
}
```

**Response**  
`204 No Content`

---

## 5. Root User Login
**Endpoint**  
```
POST /api/v1/auth/root-login
```

**Request**
```json
{
  "email": "ganeshamoorthy060@gmail.com",
  "password": "password"
}
```

**Response Headers**
```
Authorization: Bearer <JWT-TOKEN>
```

---

## 6. Create Role
**Endpoint**  
```
POST /api/v1/accounts/{accountId}/roles/create
```

**Request**
```json
{
  "name": "USER MANAGER",
  "description": "This role can make CRUD to users",
  "permissions": [
    { "id": 1, "name": "IAM:USER:CREATE" },
    { "id": 2, "name": "IAM:USER:READ" },
    { "id": 3, "name": "IAM:USER:UPDATE" },
    { "id": 4, "name": "IAM:USER:DELETE" }
  ]
}
```

**Response**
```json
{
  "id": 2,
  "name": "USER MANAGER",
  "description": "This role can make CRUD to users",
  "accountId": 1,
  "permissions": []
}
```

---

## 7. Create a Normal User
**Endpoint**  
```
POST /api/v1/accounts/{accountId}/users/create
```

**Request**
```json
{
  "email": "ganeshama@appranix.com",
  "firstName": "ganesh",
  "lastName": "moorthy",
  "description": "This is a user account",
  "roles": [{ "id": 2 }]
}
```

**Response**
```json
{
  "id": 2,
  "firstName": "ganesh",
  "lastName": "moorthy",
  "email": "ganeshama@appranix.com",
  "type": "PASSWORD",
  "accountId": 1,
  "status": "CREATED"
}
```

---

## 8. Set Password for Normal User
**Endpoint**  
```
POST /api/v1/accounts/{accountId}/users/set-password
```

**Request**
```json
{
  "email": "ganeshama@appranix.com",
  "password": "password"
}
```

**Response**  
`204 No Content`

---

## 9. Normal User Login
**Endpoint**  
```
POST /api/v1/auth/login
```

**Request**
```json
{
  "email": "ganeshama@appranix.com",
  "accountId": 1,
  "password": "password"
}
```

**Response Headers**
```
Authorization: Bearer <JWT-TOKEN>
```

---

## 🔒 10. Test Access Based on Role
Now use the `Authorization: Bearer <token>` header from login when calling protected endpoints.

- **Root User Token** → full access to all APIs.  
- **Normal User Token (USER MANAGER)** → access restricted to user CRUD operations (`IAM:USER:*`).  
- If a normal user tries to access resources outside their permissions → API should return `403 Forbidden`.

---

### ✅ Summary of Flow
1. **Create Account → Root User → Validate OTP → Set Password → Root Login**  
   → You now have a **root token**.  
2. **Root User → Create Role → Create Normal User → Set Password → Normal Login**  
   → You now have a **normal user token** with assigned permissions.  
3. **Test Role-Based Access** using the appropriate JWT in `Authorization` header.  
