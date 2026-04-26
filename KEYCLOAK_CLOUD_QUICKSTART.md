# Quick Start: Keycloak.cloud Integration

This guide walks you through integrating your Spring Boot application with **Keycloak.cloud** - a free, hosted Keycloak identity management service.

## Prerequisites

- Spring Boot application running on `http://localhost:8080`
- Free account at https://www.keycloak.cloud
- Java 17+
- Maven 3.6+

---

## Step 1: Sign Up for Keycloak.cloud

1. Open https://www.keycloak.cloud
2. Click **"Sign Up"**
3. Enter your email and password
4. Check your email and verify account
5. Login to your Keycloak.cloud dashboard

---

## Step 2: Create Organization and Realm

1. After login, you'll see the **Organization** management page
2. Click **"Create Organization"** and enter a name (e.g., `demo-org`)
3. Inside your organization, click **"Create Realm"**
4. Enter Realm name: `demo-realm`
5. Click **Create**

**Important**: Note your **Realm Name** - you'll need it later

---

## Step 3: Create OpenID Connect Client

1. In your realm, go to **Clients** (left menu)
2. Click **"Create Client"**
3. Fill in client settings:
   - **Client ID**: `springboot-demo`
   - **Name**: `Spring Boot Demo`
   - **Protocol**: `openid-connect`
   - **Client Type**: `Web application`
4. Click **Next**

5. On the next page, configure:
   - **Valid Redirect URIs**: 
     ```
     http://localhost:8080/*
     http://localhost:8080/
     ```
   - **Valid Post Logout Redirect URIs**:
     ```
     http://localhost:8080
     http://localhost:8080/
     ```
   - **Web Origins**:
     ```
     http://localhost:8080
     ```
6. Click **Save**

7. Go to the **Credentials** tab:
   - Copy the **Client Secret** (long string)
   - **Keep this safe!** You'll need it in the configuration

---

## Step 4: Create Test Users

### Create "testuser"

1. Go to **Users** (left menu)
2. Click **"Add User"**
3. Enter:
   - **Username**: `testuser`
   - **Email**: (optional)
   - **Email verified**: Toggle ON
   - **Enabled**: Toggle ON
4. Click **Create**

5. Go to **Credentials** tab:
   - Set password: `test1234`
   - **Temporary**: Toggle OFF (so user doesn't need to change it)
   - Click **Set Password**

6. Go to **Role Mapping** tab:
   - Under **Realm Roles**, add: `user`

### Create "adminuser"

Repeat the above, but:
- **Username**: `adminuser`
- **Password**: `admin1234`
- **Roles**: Add both `admin` and `user`

---

## Step 5: Get Your Keycloak Instance URL

1. Go to **Realm Settings** (left menu)
2. Look for **Endpoints** section
3. Click **"OpenID Provider Configuration"**
4. The URL will show your instance. It looks like:
   ```
   https://your-instance-id.keycloak.cloud/realms/demo-realm/.well-known/openid-configuration
   ```

5. Extract the base URL (without `/.well-known...`):
   ```
   https://your-instance-id.keycloak.cloud
   ```

---

## Step 6: Configure Your Spring Boot Application

### Option A: Using Environment Variables (Recommended for Docker/Cloud)

```bash
export KEYCLOAK_ENABLED=true
export KEYCLOAK_REALM=demo-realm
export KEYCLOAK_AUTH_SERVER_URL=https://your-instance-id.keycloak.cloud
export KEYCLOAK_RESOURCE=springboot-demo
export KEYCLOAK_CREDENTIALS_SECRET=your_client_secret_here
```

Then run:
```bash
java -Dspring.profiles.active=keycloak -jar target/demo-1.0.0.jar
```

### Option B: Using application-keycloak.properties (Recommended for Development)

Edit `src/main/resources/application-keycloak.properties`:

```properties
keycloak.enabled=true
keycloak.realm=demo-realm
keycloak.auth-server-url=https://your-instance-id.keycloak.cloud
keycloak.ssl-required=external
keycloak.resource=springboot-demo
keycloak.credentials.secret=your_client_secret_here
keycloak.use-resource-role-mappings=true

spring.security.oauth2.resourceserver.jwt.issuer-uri=https://your-instance-id.keycloak.cloud/realms/demo-realm
spring.security.oauth2.resourceserver.jwt.jwk-set-uri=https://your-instance-id.keycloak.cloud/realms/demo-realm/protocol/openid-connect/certs
```

Replace:
- `your-instance-id` - Your Keycloak instance name
- `your_client_secret_here` - The client secret from Step 3

---

## Step 7: Run the Application with Keycloak

```bash
# Build the project
mvn clean package -DskipTests

# Run with Keycloak profile
java -Dspring.profiles.active=keycloak -jar target/demo-1.0.0.jar
```

Or with Maven:
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=keycloak"
```

You should see output like:
```
Started Application in 5.234 seconds (JVM running for 5.892)
```

---

## Step 8: Test the Application

1. Open browser: `http://localhost:8080`
2. You'll see the home page
3. Click **"Login"** button
4. You'll be redirected to Keycloak login page
5. Login with:
   - **Username**: `testuser`
   - **Password**: `test1234`
6. After successful login, you'll be redirected back to your app
7. You should see the **Dashboard** page with your username

---

## Troubleshooting

### "Redirect URI mismatch"
- **Problem**: Keycloak rejects your redirect
- **Solution**: Ensure `Valid Redirect URIs` includes both:
  - `http://localhost:8080/`
  - `http://localhost:8080/*`

### "Client secret invalid"
- **Problem**: Authentication fails
- **Solution**: 
  - Double-check you copied the secret correctly
  - No extra spaces or characters
  - Verify in Keycloak > Clients > springboot-demo > Credentials

### "Cannot connect to keycloak server"
- **Problem**: Network error
- **Solution**:
  - Check internet connection
  - Verify URL is correct
  - Try accessing the URL directly in browser
  - Check Keycloak.cloud status

### "Realm not found"
- **Problem**: 404 error
- **Solution**:
  - Verify realm name spelling in config
  - Make sure realm exists in Keycloak.cloud
  - Check instance URL is correct

### Login page shows errors
- **Problem**: Keycloak login page has errors
- **Solution**:
  - Check browser console for detailed errors
  - Verify client credentials
  - Ensure users are enabled in Keycloak

---

## Switching Between Authentication Methods

### Use In-Memory Auth (Quick Test)
```bash
mvn spring-boot:run
# or
java -jar target/demo-1.0.0.jar
```

Credentials:
- **user** / **password**
- **admin** / **admin123**

### Use Keycloak Auth
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=keycloak"
# or
java -Dspring.profiles.active=keycloak -jar target/demo-1.0.0.jar
```

---

## Application Features

### Protected Endpoints

- `/` - Public (home page)
- `/css/**`, `/js/**` - Public (static resources)
- `/login` - Public login page
- `/dashboard` - **Protected** (requires authentication)
- `/logout` - Logout

### Security Features

✓ OAuth2/OpenID Connect authentication  
✓ JWT token validation  
✓ Role-based access control (RBAC)  
✓ Automatic token refresh  
✓ CSRF protection  
✓ XSS protection via Thymeleaf  

---

## API Integration (Optional)

To call your app from another service:

1. Get access token:
```bash
curl -X POST \
  https://your-instance-id.keycloak.cloud/realms/demo-realm/protocol/openid-connect/token \
  -H 'Content-Type: application/x-www-form-urlencoded' \
  -d 'grant_type=client_credentials' \
  -d 'client_id=springboot-demo' \
  -d 'client_secret=your_client_secret'
```

2. Use token in API call:
```bash
curl -H "Authorization: Bearer {access_token}" \
  http://localhost:8080/dashboard
```

---

## Production Deployment

For production use:

1. Update redirect URIs to your domain:
   ```
   https://yourdomain.com/
   https://yourdomain.com/*
   ```

2. Change SSL requirement:
   - In Keycloak: `keycloak.ssl-required=all`

3. Use environment variables for secrets:
   ```bash
   java -Dspring.profiles.active=keycloak \
     -Dkeycloak.credentials.secret=$CLIENT_SECRET \
     -jar demo-1.0.0.jar
   ```

4. Enable HTTPS on your application

---

## References

- **Keycloak.cloud**: https://www.keycloak.cloud
- **Keycloak Docs**: https://www.keycloak.org/documentation
- **Spring Security OAuth2**: https://spring.io/projects/spring-security-oauth2-resource-server
- **OpenID Connect**: https://openid.net/connect/

---

## Support

For issues:
1. Check Keycloak logs in Keycloak.cloud dashboard
2. Check Spring Boot application logs
3. Enable DEBUG logging:
   ```
   logging.level.org.springframework.security=DEBUG
   logging.level.org.keycloak=DEBUG
   ```
4. Review browser network tab (F12 > Network)
