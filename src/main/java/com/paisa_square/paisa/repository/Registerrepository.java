package com.paisa_square.paisa.repository;

import com.paisa_square.paisa.model.Register;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Registerrepository extends JpaRepository<Register,Long> {
    Register findByEmail(String email);
    Register findByEmailAndPassword(String email,String password);
}
