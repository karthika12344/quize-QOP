package com.example.quizonline.project.controller;


import com.example.quizonline.project.entity.User;
import com.example.quizonline.project.service.EmailService;
import com.example.quizonline.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private UserService userService;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private EmailService emailService;

    // Register new user
@PostMapping("/register")
public ResponseEntity<?> register(@RequestBody User user) {
    try {
        User saved = userService.registerUser(user);

        // Try sending email separately (won’t block registration)
        try {
            emailService.sendEmail(
                saved.getEmail(),
                "Welcome to Quiz Platform!",
                "Hello " + saved.getName() + ",\n\nThank you for registering!!\n\nBest regards,\nQuiz Platform Team"
            );
        } catch (Exception e) {
            System.err.println("Email sending failed: " + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
    }
}




    // Simple API login (Postman-friendly). Returns success message only.
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User login) {
        Optional<User> opt = userService.findByEmail(login.getEmail());
        if (opt.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        User u = opt.get();
        if (passwordEncoder.matches(login.getPassword(), u.getPassword())) {
            return ResponseEntity.ok("Login successful for: " + u.getEmail());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    // Find user by email
    @GetMapping("/user")
    public ResponseEntity<?> getByEmail(@RequestParam String email) {
        return userService.findByEmail(email)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found"));
    }

    // List users
    @GetMapping("/users")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
