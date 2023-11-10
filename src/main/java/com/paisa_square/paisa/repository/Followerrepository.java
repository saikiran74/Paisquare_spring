package com.paisa_square.paisa.repository;

import com.paisa_square.paisa.model.Followers;
import com.paisa_square.paisa.model.Register;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Followerrepository extends JpaRepository<Followers,Long> {
}
