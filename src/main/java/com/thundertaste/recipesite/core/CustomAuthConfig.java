package com.thundertaste.recipesite.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


// This is the configuration class for custom authentication and authorization settings.
@Configuration
// Enable web security for the application.
@EnableWebSecurity
public class CustomAuthConfig {

    // Injects the custom user details service to manage user data.
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    // Injects the BCrypt password encoder used for hashing passwords.
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;  // Autowire the password encoder



    // Configures global settings for authentication.
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder);   // Use the autowired password encoder here
    }
    // Define a bean to create a SecurityFilterChain which holds the security filter chain.
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Begin configuration of authorization requests.
                .authorizeHttpRequests(auth -> auth
                        // Match these paths and permit all users (unauthenticated or any role) to access them.
                        .requestMatchers(
                                new AntPathRequestMatcher("/"),
                                new AntPathRequestMatcher("/login*"),
                                new AntPathRequestMatcher("/css/*"),
                                new AntPathRequestMatcher("/images/*"),
                                new AntPathRequestMatcher("/images/icons/*"),
                                new AntPathRequestMatcher("/images/profileImages/*"),
                                new AntPathRequestMatcher("/images/recipePhotos/*"),
                                new AntPathRequestMatcher("/js/*"),
                                new AntPathRequestMatcher("/create-a-review"),
                                new AntPathRequestMatcher("/forgotPassword"),
                                new AntPathRequestMatcher("/index"),
                                new AntPathRequestMatcher("/login"),
                                new AntPathRequestMatcher("/my-account"),
                                new AntPathRequestMatcher("/my-recipes"),
                                new AntPathRequestMatcher("/recipe-search"),
                                new AntPathRequestMatcher("/search"),
                                new AntPathRequestMatcher("/search?"),
                                new AntPathRequestMatcher("/recipe-view"),
                                new AntPathRequestMatcher("/recipe/*"),
                                new AntPathRequestMatcher("/recipes/*"),
                                new AntPathRequestMatcher("/signup"),
                                new AntPathRequestMatcher("/home")
                        ).permitAll()
                        // This line restricts access to /index only to users with roles USER or ADMIN.
                            //.requestMatchers(new AntPathRequestMatcher("/index")).hasAnyRole("USER", "ADMIN")
                        // Any other request must be authenticated.
                        .anyRequest().authenticated()
                )
                // Configure form login.
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/index", true)
                        .permitAll()
                )
                // Configure logout behavior.
                .logout(logout -> logout
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .permitAll()
                );
        // Build and return the security filter chain configured above.
        return http.build();
    }



}
