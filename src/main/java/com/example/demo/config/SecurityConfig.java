package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${keycloak.enabled:false}")
    private boolean keycloakEnabled;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/", "/css/**", "/js/**", "/login").permitAll()
                .anyRequest().authenticated()
            );

        if (keycloakEnabled) {
            // OAuth2 Resource Server configuration for Keycloak
            http
                .oauth2ResourceServer(oauth2 -> oauth2
                    .jwt(jwt -> jwt.jwtAuthenticationConverter(new JwtAuthenticationConverter()))
                )
                .logout(logout -> logout
                    .logoutSuccessUrl("/")
                );
        } else {
            // Form-based login for in-memory authentication
            http
                .formLogin(form -> form
                    .defaultSuccessUrl("/dashboard", true)
                )
                .logout(logout -> logout
                    .logoutSuccessUrl("/")
                );
        }

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        if (keycloakEnabled) {
            // When using Keycloak, we don't need in-memory users
            return new InMemoryUserDetailsManager();
        }

        // In-memory users for development/testing
        UserDetails user = User.builder()
            .username("user")
            .password(passwordEncoder().encode("password"))
            .roles("USER")
            .build();

        UserDetails admin = User.builder()
            .username("admin")
            .password(passwordEncoder().encode("admin123"))
            .roles("ADMIN")
            .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Custom JWT Authentication Converter for Keycloak
     * Converts JWT claims to Spring Security authorities
     */
    public static class JwtAuthenticationConverter extends org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter {
        public JwtAuthenticationConverter() {
            super();
            // Convert realm_access roles to Spring authorities
            this.setJwtGrantedAuthoritiesConverter(jwt -> {
                var realmAccess = jwt.getClaimAsMap("realm_access");
                if (realmAccess == null) {
                    return java.util.List.of();
                }

                @SuppressWarnings("unchecked")
                var roles = (java.util.List<String>) realmAccess.get("roles");
                if (roles == null) {
                    return java.util.List.of();
                }

                return roles.stream()
                    .map(role -> new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + role))
                    .collect(java.util.stream.Collectors.toList());
            });
        }
    }
}
