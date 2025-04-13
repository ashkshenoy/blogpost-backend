package com.example.techblog.config;

import com.example.techblog.security.JwtFilter;
import com.example.techblog.security.UserDetailsServiceImpl;

import java.util.List;

import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	 return http
    			 	.cors().configurationSource(corsConfigurationSource()) // ✅ Add this line
    	            .and()
    		        .csrf(csrf -> csrf
    		            .ignoringRequestMatchers("/h2-console/**") // Disable CSRF for H2 console only
    		            .disable()
    		        )
    		        .headers(headers -> headers
    		            .frameOptions(frame -> frame.sameOrigin()) // Allow embedded H2 console frames
    		        )
    		        .sessionManagement(sm -> sm
    		            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    		        )
    		        .authorizeHttpRequests(auth -> auth
    		            .requestMatchers("/auth/**", "/h2-console/**", "/auth/**", "/blogs/**").permitAll() // Allow auth & H2 console
    		            .anyRequest().authenticated()
    		        )
    		        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
    		        .build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000")); // React app origin
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true); // Only use if sending cookies or Authorization headers

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
