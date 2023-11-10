package com.paisa_square.paisa.repository;

import com.paisa_square.paisa.model.Advertise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Advertiserepository extends JpaRepository<Advertise,Long> {

    Advertise findById(Integer id);
}
