# Spring Boot Demo Application

A simple, secure Spring Boot application with Thymeleaf templating engine and Spring Security.

## Project Structure

```
├── pom.xml                                    # Maven configuration
├── README.md                                  # This file
└── src/
    ├── main/
    │   ├── java/com/example/demo/
    │   │   ├── Application.java               # Main Spring Boot application
    │   │   ├── controller/
    │   │   │   └── HomeController.java        # Web controllers
    │   │   └── config/
    │   │       └── SecurityConfig.java        # Security configuration
    │   └── resources/
    │       ├── application.properties         # Application configuration
    │       ├── templates/
    │       │   ├── index.html                 # Home page
    │       │   └── dashboard.html             # Protected dashboard
    │       └── static/
    │           └── css/
    │               └── style.css              # Stylesheet
    └── test/
        └── java/com/example/demo/            # Test files
```

## Technologies

- **Spring Boot 3.2.3** - Fast and lightweight framework
- **Spring Web** - REST API support
- **Spring Security** - Authentication and authorization
- **Thymeleaf** - Server-side template engine with security features
- **Java 17** - Minimum JDK requirement
- **Maven** - Build tool

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+

### Building the Project

```bash
mvn clean install
```

### Running the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## Demo Accounts

| Username | Password | Role  |
|----------|----------|-------|
| user     | password | USER  |
| admin    | admin123 | ADMIN |

## Usage

1. **Home Page** - `/` - Accessible to everyone
2. **Dashboard** - `/dashboard` - Requires authentication
3. **Login** - `/login` - Provided by Spring Security
4. **Logout** - `/logout` - Logout functionality

## Security Features

✓ **CSRF Protection** - Enabled by default in Spring Security  
✓ **XSS Protection** - Thymeleaf escapes output by default  
✓ **Password Encoding** - BCrypt algorithm for secure password storage  
✓ **Role-Based Access Control** - USER and ADMIN roles  
✓ **Form-based Authentication** - Built-in login form  
✓ **Session Management** - Secure session handling  

## File Descriptions

### Application.java
Entry point for the Spring Boot application. Bootstraps all components.

### HomeController.java
Handles HTTP requests:
- `GET /` - Displays home page
- `GET /dashboard` - Displays protected dashboard

### SecurityConfig.java
Configures Spring Security:
- Defines authentication provider (in-memory users)
- Sets authorization rules
- Configures login/logout behavior
- BCrypt password encryption

### application.properties
Configuration file for:
- Server port (8080)
- Thymeleaf settings
- Logging levels

### Templates
- **index.html** - Home page with public access
- **dashboard.html** - Protected page for authenticated users

### CSS
- **style.css** - Responsive design with modern styling

## Building for Production

```bash
mvn clean package
```

This creates an executable JAR file in the `target/` directory that can be run with:

```bash
java -jar target/demo-1.0.0.jar
```

## Next Steps

To extend this application:

1. Add a database layer (Spring Data JPA with H2 or PostgreSQL)
2. Implement RESTful APIs with `@RestController`
3. Add service layer for business logic
4. Implement exception handling and validation
5. Add more complex security configurations (OAuth2, JWT)
6. Write unit and integration tests

## License

MIT License - Feel free to use this as a starting template.