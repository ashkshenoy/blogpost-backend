package com.example.techblog.controller;

import com.example.techblog.model.User;
import com.example.techblog.repository.UserRepository;
import com.example.techblog.security.JwtUtil;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final UserRepository repo;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder encoder;

    public AuthController(AuthenticationManager authManager, UserRepository repo, JwtUtil jwtUtil, PasswordEncoder encoder) {
        this.authManager = authManager;
        this.repo = repo;
        this.jwtUtil = jwtUtil;
        this.encoder = encoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid User user) {
        // Check if username already exists
        if (repo.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Username already exists");
        }
        
        // Check if email already exists
        if (repo.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Email already exists");
        }
        
        // Encode password and save user
        user.setPassword(encoder.encode(user.getPassword()));
        repo.save(user);
        
        return ResponseEntity.ok("User registered!");
    }

    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> creds) {
        authManager.authenticate(
            new UsernamePasswordAuthenticationToken(creds.get("username"), creds.get("password"))
        );
        return jwtUtil.generateToken(creds.get("username"));
    }
}
