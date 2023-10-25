package com.thundertaste.recipesite.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
public class CustomAuthConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;  // Autowire the password encoder

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder);   // Use the autowired password encoder here
    }
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                new AntPathRequestMatcher("/"),
                                new AntPathRequestMatcher("/login*"),
                                new AntPathRequestMatcher("/css/*"),
                                new AntPathRequestMatcher("/images/*"),
                                new AntPathRequestMatcher("/images/icons/*"),
                                new AntPathRequestMatcher("/js/*"),
                                new AntPathRequestMatcher("/create-a-review"),
                                new AntPathRequestMatcher("/forgotPassword"),
                                new AntPathRequestMatcher("/index"),
                                new AntPathRequestMatcher("/login"),
                                new AntPathRequestMatcher("/my-account"),
                                new AntPathRequestMatcher("/my-recipes"),
                                new AntPathRequestMatcher("/recipe-search"),
                                new AntPathRequestMatcher("/recipe-view"),
                                new AntPathRequestMatcher("/signup"),
                                new AntPathRequestMatcher("/submit-recipe"),
                                new AntPathRequestMatcher("/home")
                        ).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/index")).hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/index", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .permitAll()
                );

        return http.build();
    }



}
