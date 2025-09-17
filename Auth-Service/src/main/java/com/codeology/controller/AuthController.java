package com.codeology.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.codeology.config.JwtTokenUtil;
import com.codeology.dto.JwtResponse;
import com.codeology.dto.LoginRequest;
import com.codeology.dto.RegisterRequest;
import com.codeology.model.Role;
import com.codeology.model.User;
import com.codeology.repository.UserRepository;
import com.codeology.service.UserService;

import jakarta.validation.Valid;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService,
                          UserRepository userRepository,
                          JwtTokenUtil jwtTokenUtil,
                          AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterRequest request) {
        User savedUser = userService.registerUser(
                request.getUsername(),
                request.getEmail(),
                request.getPassword()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenUtil.generateJwtToken(authentication);

        User u = userRepository.findByUsername(req.getUsername()).orElseThrow();

        Set<String> roles = u.getRoles().stream().map(r -> r.getName()).collect(Collectors.toSet());
        JwtResponse resp = new JwtResponse(token, "Bearer", u.getId(), u.getUsername(), u.getEmail(), roles);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(org.springframework.security.core.Authentication authentication) {
        if (authentication == null) return ResponseEntity.status(401).body("Unauthenticated");
        String username = authentication.getName();
        User u = userRepository.findByUsername(username).orElse(null);
        if (u == null) return ResponseEntity.status(404).body("User not found");
        // return safe user info
        var result = new java.util.HashMap<String, Object>();
        result.put("id", u.getId());
        result.put("username", u.getUsername());
        result.put("email", u.getEmail());
        result.put("roles", u.getRoles().stream().map(r -> r.getName()).toArray());
        return ResponseEntity.ok(result);
    }
}