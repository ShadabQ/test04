# Quick Start: Keycloak Cloud Deployment

This guide walks you through deploying Keycloak on cloud platforms and integrating it with your Spring Boot application. Unlike the non-existent keycloak.cloud service, you can deploy Keycloak using established cloud providers.

## Supported Cloud Deployment Options

1. **AWS** - EC2, ECS, or RDS-managed database
2. **Azure** - App Service or Container Instances
3. **Google Cloud** - Compute Engine or Cloud Run
4. **DigitalOcean** - Droplets or App Platform
5. **Self-managed** - Any VPS or cloud provider

## Prerequisites

- Spring Boot application running on `http://localhost:8080`
- Cloud provider account (AWS, Azure, GCP, DigitalOcean, etc.)
- Java 17+
- Maven 3.6+
- Docker (for containerized deployments)
- Basic knowledge of your chosen cloud provider

---

## Step 1: Set Up Hosted Keycloak Instance

Choose one of the deployment options below:

### Option A: AWS EC2 (Recommended for Getting Started)

1. Launch an Ubuntu 22.04 EC2 instance (t3.medium or larger)
2. SSH into the instance
3. Install Docker and Docker Compose:
   ```bash
   sudo apt update && sudo apt install -y docker.io docker-compose
   sudo usermod -aG docker $USER
   ```
4. Create `docker-compose.yml` with the Keycloak service (see below)
5. Run: `docker-compose up -d`
6. Access Keycloak at: `http://<your-ec2-public-ip>:8080`

**Docker Compose Configuration:**
```yaml
version: '3.8'
services:
  keycloak:
    image: keycloak/keycloak:latest
    environment:
      KEYCLOAK_ADMIN: admin
      KEYCLOAK_ADMIN_PASSWORD: YourSecurePassword123
      KC_HOSTNAME: your-keycloak-domain.com
      KC_HOSTNAME_STRICT: 'true'
      KC_DB: postgres
      KC_DB_URL: jdbc:postgresql://keycloak-db:5432/keycloak
      KC_DB_USERNAME: keycloak
      KC_DB_PASSWORD: DbPassword123
    ports:
      - "8080:8080"
    depends_on:
      - keycloak-db

  keycloak-db:
    image: postgres:15
    environment:
      POSTGRES_DB: keycloak
      POSTGRES_USER: keycloak
      POSTGRES_PASSWORD: DbPassword123
    volumes:
      - keycloak_db_data:/var/lib/postgresql/data

volumes:
  keycloak_db_data:
```

### Option B: Azure Container Instances

1. Create resource group: `az group create --name keycloak-rg --location eastus`
2. Deploy using container: See [Azure Container Instances documentation](https://learn.microsoft.com/en-us/azure/container-instances/)
3. Access via your assigned DNS name

### Option C: DigitalOcean App Platform

1. Log in to DigitalOcean
2. Create new App
3. Select Docker image: `keycloak/keycloak:latest`
4. Configure environment variables (as per Docker Compose above)
5. Deploy

### Option D: Google Cloud Run (Serverless)

1. Push Keycloak image to Google Container Registry
2. Deploy using: `gcloud run deploy keycloak --image gcr.io/project/keycloak --port 8080`
3. Configure custom domain

**For all options, configure a domain/DNS:**
- Set up your domain's DNS to point to your Keycloak instance
- Update `KC_HOSTNAME` environment variable with your domain

---

## Step 2: Access Your Keycloak Instance

1. Open your browser and navigate to your Keycloak instance
   - EC2: `http://<your-ec2-public-ip>:8080`
   - Or use your domain: `https://your-keycloak-domain.com`

2. Click **Admin Console** link or navigate to `/admin`

3. Login with:
   - **Username**: `admin`
   - **Password**: Your configured admin password (e.g., `YourSecurePassword123`)

---

## Step 3: Create a Realm

1. You'll see the **Master Realm** dropdown (top-left)
2. Click **"Create Realm"**
3. Enter Realm name: `demo-realm`
4. Click **Create**

**Important**: Note your **Realm Name** - you'll need it later

---

## Step 4: Create OpenID Connect Client

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
   https://your-keycloak-domain.com/realms/demo-realm/.well-known/openid-configuration
   ```

5. Extract the base URL (without `/.well-known...`):
   ```
   https://your-keycloak-domain.com
   ```

**Note**: Replace `your-keycloak-domain.com` with:
- Your EC2 public IP or DNS name
- Your Azure custom domain
- Your DigitalOcean app domain
- Your Google Cloud Run domain
- Or your actual registered domain if you've set up DNS forwarding

---

## Step 6: Configure Your Spring Boot Application

### Option A: Using Environment Variables (Recommended for Docker/Cloud)

```bash
export KEYCLOAK_ENABLED=true
export KEYCLOAK_REALM=demo-realm
export KEYCLOAK_AUTH_SERVER_URL=https://your-keycloak-domain.com
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
keycloak.auth-server-url=https://your-keycloak-domain.com
keycloak.ssl-required=external
keycloak.resource=springboot-demo
keycloak.credentials.secret=your_client_secret_here
keycloak.use-resource-role-mappings=true

spring.security.oauth2.resourceserver.jwt.issuer-uri=https://your-keycloak-domain.com/realms/demo-realm
spring.security.oauth2.resourceserver.jwt.jwk-set-uri=https://your-keycloak-domain.com/realms/demo-realm/protocol/openid-connect/certs
```

Replace:
- `your-keycloak-domain.com` - Your deployed Keycloak domain/URL
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
- **Solution**: Ensure `Valid Redirect URIs` includes:
  - `http://localhost:8080/`
  - `http://localhost:8080/*`
  - `https://yourapp.com/` (for production)

### "Client secret invalid"
- **Problem**: Authentication fails
- **Solution**: 
  - Double-check you copied the secret correctly
  - No extra spaces or characters
  - Verify in Keycloak > Clients > springboot-demo > Credentials

### "Cannot connect to keycloak server"
- **Problem**: Network error or unreachable
- **Solution**:
  - Check internet connection
  - Verify URL is correct (use your domain/IP)
  - Try accessing the URL directly in browser
  - Check cloud provider firewall rules (security groups in AWS, NSG in Azure, etc.)
  - Verify Keycloak container is running: `docker ps`
  - Check container logs: `docker logs keycloak`

### "SSL Certificate Error"
- **Problem**: HTTPS connection fails
- **Solution**:
  - Use Let's Encrypt for free SSL certificates
  - Configure nginx/Apache reverse proxy with automatic cert renewal
  - Set `KC_HOSTNAME_STRICT: 'true'` in production

### "Realm not found"
- **Problem**: 404 error
- **Solution**:
  - Verify realm name spelling in config
  - Make sure realm exists in your Keycloak instance
  - Check instance URL is correct and accessible

### Login page shows errors
- **Problem**: Keycloak login page has errors
- **Solution**:
  - Check browser console for detailed errors
  - Verify client credentials
  - Ensure users are enabled in Keycloak
  - Check database connectivity in container
  - Review Keycloak logs: `docker logs -f keycloak`

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
  https://your-keycloak-domain.com/realms/demo-realm/protocol/openid-connect/token \
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

1. **Use HTTPS everywhere**:
   - Set up SSL/TLS certificate (Let's Encrypt recommended)
   - Update Keycloak: `KC_HOSTNAME_STRICT: 'true'`
   - Update redirect URIs to production domain:
     ```
     https://yourdomain.com/
     https://yourdomain.com/*
     ```
   - Update Spring Boot config: `keycloak.ssl-required=all`

2. **Secure environment variables**:
   - Use cloud provider secrets management:
     - AWS Secrets Manager
     - Azure Key Vault
     - Google Secret Manager
   - Never commit secrets to version control
   - Use environment variables for secrets:
     ```bash
     java -Dspring.profiles.active=keycloak \
       -Dkeycloak.credentials.secret=$CLIENT_SECRET \
       -jar demo-1.0.0.jar
     ```

3. **Database configuration**:
   - Use managed database service (RDS, Cloud SQL, Azure Database)
   - Keep database backups enabled
   - Use strong passwords

4. **Network security**:
   - Restrict Keycloak admin console to VPN/specific IPs
   - Use cloud provider firewall rules
   - Enable DDoS protection if available

5. **Monitoring and logging**:
   - Enable audit logs in Keycloak
   - Set up cloud provider monitoring
   - Configure log aggregation (CloudWatch, Stackdriver, etc.)

---

## References and Deployment Guides

### Official Documentation
- **Keycloak Docs**: https://www.keycloak.org/documentation
- **Keycloak Docker**: https://hub.docker.com/r/keycloak/keycloak
- **Spring Security OAuth2**: https://spring.io/projects/spring-security-oauth2-resource-server
- **OpenID Connect**: https://openid.net/connect/

### Cloud Deployment Guides
- **AWS EC2**: https://docs.aws.amazon.com/AWSEC2/
- **AWS RDS**: https://docs.aws.amazon.com/rds/
- **Azure App Service**: https://learn.microsoft.com/en-us/azure/app-service/
- **Google Cloud Run**: https://cloud.google.com/run/docs
- **Google Cloud SQL**: https://cloud.google.com/sql/docs
- **DigitalOcean**: https://docs.digitalocean.com/

### SSL/TLS
- **Let's Encrypt**: https://letsencrypt.org/
- **Certbot**: https://certbot.eff.org/

### Related Topics
- **Keycloak Server Administration**: https://www.keycloak.org/guides
- **Docker Compose**: https://docs.docker.com/compose/
- **Reverse Proxy Setup** (nginx): https://www.nginx.com/resources/admin-guide/

---

## Troubleshooting Support

For issues:

1. **Check Keycloak logs**:
   ```bash
   docker logs -f keycloak
   docker logs -f keycloak-db
   ```

2. **Check Spring Boot application logs**:
   ```bash
   tail -f application.log
   ```

3. **Enable DEBUG logging in `application-keycloak.properties`**:
   ```properties
   logging.level.org.springframework.security=DEBUG
   logging.level.org.springframework.security.oauth2=DEBUG
   logging.level.org.keycloak=DEBUG
   ```

4. **Browser debugging**:
   - Review browser network tab (F12 > Network)
   - Check browser console for errors (F12 > Console)
   - Check application logs for exceptions

5. **Test connection**:
   ```bash
   curl -k https://your-keycloak-domain.com/
   curl -k https://your-keycloak-domain.com/realms/demo-realm
   ```

6. **Verify firewall/networking**:
   - Check security groups (AWS)
   - Check network security groups (Azure)
   - Check firewall rules on your cloud provider
   - Verify ports are exposed correctly: `docker port keycloak`
