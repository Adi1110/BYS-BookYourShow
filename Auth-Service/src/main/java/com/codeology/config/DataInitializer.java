package com.codeology.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.codeology.model.Role;
import com.codeology.model.User;
import com.codeology.repository.RoleRepository;
import com.codeology.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // ensure roles exist
    	Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_USER")));

        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_ADMIN")));

        // create admin user if not exists
        String adminUsername = "admin";
        if (userRepository.findByUsername(adminUsername).isEmpty()) {
            User admin = new User();
            admin.setUsername(adminUsername);
            admin.setEmail("admin@example.com");
            // default admin password: admin123 (encoded) - change in production
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEnabled(true);
            admin.setRoles(Set.of(adminRole));
            userRepository.save(admin);
            System.out.println("Created default admin user: username=admin password=admin123 (change immediately)");
        } else {
            System.out.println("Admin user already present");
        }
    }
}
