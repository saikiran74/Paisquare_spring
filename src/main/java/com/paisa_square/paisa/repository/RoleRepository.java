package com.paisa_square.paisa.repository;

import com.paisa_square.paisa.model.Role;
import com.paisa_square.paisa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByRoleName(String role);
}