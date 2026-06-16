package com.example.demo.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/messages").permitAll()
                        .requestMatchers(HttpMethod.GET, "/messages/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/messages").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/messages/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/messages/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )

                .httpBasic(withDefaults())
                .formLogin(form -> form.disable());

        return http.build();
    }

    @Bean
    public UserDetailsService users(PasswordEncoder encoder) {
        UserDetails normalUser = User.builder()
                .username("user")
                .password(encoder.encode("password"))
                .roles("USER")
                .build();

        UserDetails adminUser = User.builder()
                .username("admin")
                .password(encoder.encode("password"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(normalUser, adminUser);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}