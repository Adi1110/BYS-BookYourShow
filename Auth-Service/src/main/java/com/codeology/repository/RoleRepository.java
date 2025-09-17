package com.codeology.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.codeology.model.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}

