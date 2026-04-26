# Keycloak Integration Guide

This guide explains how to integrate Keycloak as an Identity Provider for your Spring Boot application.

## Quick Start Options

### Option 1: Local Keycloak (Docker) - Recommended for Development

#### Setup Steps:

1. **Start Keycloak using Docker Compose:**
   ```bash
   docker-compose up -d
   ```
   This starts Keycloak on `http://localhost:8180`

2. **Access Keycloak Admin Console:**
   - URL: `http://localhost:8180`
   - Username: `admin`
   - Password: `admin123`

3. **Create a Realm:**
   - Click "Master" dropdown (top-left) → "Create Realm"
   - Name: `demo`
   - Click "Create"

4. **Create a Client:**
   - Go to Realm Settings → Clients
   - Click "Create client"
   - Client ID: `springboot-app`
   - Protocol: `openid-connect`
   - Client authentication: `On` (confidential)
   - Following redirects:
     - Valid redirect URIs: `http://localhost:8080/*`
     - Admin URL: `http://localhost:8080`
   - Click "Save"

5. **Get Client Secret:**
   - Go to Client Details → Credentials tab
   - Copy the "Client secret"

6. **Create Test Users:**
   - Go to Users → Create new user
   - Username: `testuser`
   - Email: `test@example.com`
   - Set password (non-temporary)
   - Create another user `admin` with ADMIN role

7. **Assign Roles:**
   - Go to Users → select user
   - Role mapping → add roles

8. **Update application.properties:**
   ```properties
   keycloak.enabled=true
   keycloak.realm=demo
   keycloak.auth-server-url=http://localhost:8180
   keycloak.ssl-required=none
   keycloak.resource=springboot-app
   keycloak.credentials.secret=YOUR_CLIENT_SECRET_HERE
   keycloak.use-resource-role-mappings=true
   ```

9. **Run Spring Boot Application:**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

10. **Test the Integration:**
    - Open `http://localhost:8080`
    - Click "Login" 
    - You'll be redirected to Keycloak login
    - Login with `testuser / password`
    - Access protected dashboard

---

### Option 2: Keycloak Cloud (Hosted Service)

#### Free Hosted Options:

**Best Option: Keycloak.cloud**
- Website: https://www.keycloak.cloud
- Free tier available
- Managed service (no maintenance)
- Good for testing and small projects

**Setup Steps:**

1. **Sign up for free account**
   - Visit https://www.keycloak.cloud
   - Create account

2. **Create Organization**
   - Create new org (free tier available)

3. **Create Realm**
   - Realm name: `demo` or your preference

4. **Create OpenID Connect Client**
   - Client name: `springboot-app`
   - Redirect URIs: `http://localhost:8080/*`
   - Credentials: Copy the client secret

5. **Update application.properties:**
   ```properties
   keycloak.enabled=true
   keycloak.realm=YOUR_REALM_NAME
   keycloak.auth-server-url=https://your-instance.keycloak.cloud
   keycloak.ssl-required=external
   keycloak.resource=YOUR_CLIENT_ID
   keycloak.credentials.secret=YOUR_CLIENT_SECRET
   keycloak.use-resource-role-mappings=true
   ```

6. **Test the integration** same as Option 1

---

### Option 3: AWS/Azure/GCP Free Tier + Docker

Host Keycloak container on cloud free tiers:

**AWS ECS Fargate (free tier: 30 days)**
- Deploy Docker container
- Use RDS free tier for persistence

**Azure Container Instances**
- Free 50 ACU hours/month
- Sufficient for testing

**Google Cloud Run**
- Free tier: 2M requests/month
- Easy Docker deployment

---

## Application Configuration

### Environment Variables (Alternative to application.properties)

```bash
# Enable Keycloak
export KEYCLOAK_ENABLED=true

# Keycloak Server
export KEYCLOAK_REALM=demo
export KEYCLOAK_AUTH_SERVER_URL=http://localhost:8180
export KEYCLOAK_SSL_REQUIRED=none

# Client Credentials
export KEYCLOAK_RESOURCE=springboot-app
export KEYCLOAK_CREDENTIALS_SECRET=your-client-secret
```

### Fallback to In-Memory Authentication

To disable Keycloak and use in-memory authentication:

```properties
keycloak.enabled=false
```

The application will automatically fall back to the test users:
- Username: `user` | Password: `password`
- Username: `admin` | Password: `admin123`

---

## Keycloak Token Structure

Example JWT payload from Keycloak:
```json
{
  "sub": "userid",
  "name": "User Name",
  "preferred_username": "username",
  "email": "user@example.com",
  "email_verified": true,
  "roles": ["user", "admin"],
  "iat": 1234567890,
  "exp": 1234571490
}
```

---

## API Examples

### Protected Endpoint
Require valid JWT token in Authorization header:
```bash
curl -H "Authorization: Bearer YOUR_TOKEN" http://localhost:8080/dashboard
```

### Get User Info
```bash
curl -H "Authorization: Bearer YOUR_TOKEN" http://localhost:8080/api/user
```

---

## Troubleshooting

### 1. Connection refused (localhost:8180)
- Ensure Docker is running: `docker ps`
- If not running: `docker-compose up -d`

### 2. Invalid client secret
- Verify secret in Keycloak → Client → Credentials tab
- Update application.properties with correct secret

### 3. Redirect URI mismatch
- Check Keycloak client settings
- Valid redirect URIs must match your application URL
- For local: `http://localhost:8080/*`

### 4. CORS errors
- Configure CORS in Keycloak → Realm → CORS

### 5. Token expired errors
- Keycloak tokens expire by default (15 minutes)
- Get a new token from the token endpoint

---

## Security Best Practices

1. **Never commit credentials:**
   ```bash
   git add --all
   git reset src/main/resources/application.properties
   ```

2. **Use environment variables for secrets:**
   ```bash
   export KEYCLOAK_CREDENTIALS_SECRET=your-secret
   ```

3. **Enable HTTPS in production:**
   - Set `keycloak.ssl-required=external`
   - Use valid SSL certificates

4. **Rotate client secrets regularly**

5. **Implement token refresh logic**

---

## Production Deployment

For production, consider:
1. Use managed Keycloak service (Keycloak.cloud, AWS Cognito, Azure AD)
2. Enable HTTPS/TLS
3. Use strong authentication for Keycloak admin
4. Implement audit logging
5. Configure token expiration appropriately
6. Use Redis/external cache for sessions
7. Enable rate limiting on auth endpoints

---

## Multiple Environment Profiles

Create environment-specific property files:

**application-local.properties**
```properties
keycloak.enabled=true
keycloak.auth-server-url=http://localhost:8180
```

**application-prod.properties**
```properties
keycloak.enabled=true
keycloak.auth-server-url=https://your-instance.keycloak.cloud
```

Run with profile:
```bash
mvn spring-boot:run -Dspring-boot.run.arguments='--spring.profiles.active=local'
```

---

## Resources

- Keycloak Documentation: https://www.keycloak.org/documentation
- Spring Boot Security: https://spring.io/guides/gs/authenticating-ldap/
- OAuth2/OIDC Spec: https://openid.net/
- Keycloak.cloud: https://www.keycloak.cloud
