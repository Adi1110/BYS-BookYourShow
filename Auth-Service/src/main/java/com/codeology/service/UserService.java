package com.codeology.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.codeology.config.JwtTokenUtil;
import com.codeology.dto.JwtResponse;
import com.codeology.dto.LoginRequest;
import com.codeology.model.Role;
import com.codeology.model.User;
import com.codeology.repository.RoleRepository;
import com.codeology.repository.UserRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenProvider;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenProvider, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.jwtTokenProvider = jwtTokenProvider;
    }

    public User registerUser(String username, String email, String rawPassword) {

        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already taken");
        }
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already used");
        }

        User u = new User();
        u.setUsername(username);
        u.setEmail(email);
        u.setPassword(passwordEncoder.encode(rawPassword));
        u.setEnabled(true);

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName("ROLE_USER");
                    return roleRepository.save(role);
                });

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        u.setRoles(roles);

        return userRepository.save(u);
    }

   
    
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
