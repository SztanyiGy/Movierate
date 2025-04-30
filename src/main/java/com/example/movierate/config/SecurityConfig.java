package com.example.movierate.config;

import com.example.movierate.service.impl.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authProvider(
            CustomUserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(
                        frame -> frame.disable()))
                .authorizeHttpRequests(
                        auth -> auth
                        // Nyilvános útvonalak - mindenki számára elérhetőek
                        .requestMatchers(
                                "/", "/index", "/register", "/login",
                                "/css/**", "/js/**", "/images/**",
                                "/h2-console/**",
                                "/movies", "/movies/",
                                "/movies/details/**", "/movies/{id}",
                                "/api/reviews/movie/**"
                        ).permitAll()
                                // Védett útvonalak
                        .requestMatchers(
                                "/movies/new",
                                "/movies/edit/**",
                                "/movies/*/delete",
                                "/movies/*/reviews/new",
                                "/reviews/edit/**",
                                "/reviews/delete/**"
                        ).authenticated()
                        // Minden más kérés
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        return http.build();
    }
}
